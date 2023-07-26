
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Multi_threaded_Application {
    private static final int NUM_USERS = 5;
    private static final double INITIAL_BALANCE = 1000;

    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount(INITIAL_BALANCE);

        for (int i = 0; i < NUM_USERS; i++) {
            Thread userThread = new Thread(new User(bankAccount, "User " + (i + 1)));
            userThread.start();
        }
    }
}

class BankAccount {
    private double balance;
    private Lock lock = new ReentrantLock();

    public BankAccount(double initialBalance) {
        balance = initialBalance;
    }

    public void deposit(double amount, String user) {
        lock.lock();
        try {
            balance += amount;
            System.out.printf("[%s] Deposited %.2f, Current Balance: %.2f%n", user, amount, balance);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double amount, String user) {
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                System.out.printf("[%s] Withdrawn %.2f, Current Balance: %.2f%n", user, amount, balance);
            } else {
                System.out.printf("[%s] Insufficient balance, Current Balance: %.2f%n", user, balance);
            }
        } finally {
            lock.unlock();
        }
    }
}

class User implements Runnable {
    private BankAccount bankAccount;
    private String name;

    public User(BankAccount bankAccount, String name) {
        this.bankAccount = bankAccount;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000); // Simulate some delay before starting transactions
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 3; i++) {
            double amount = 200 * Math.random(); // Random amount to deposit/withdraw
            if (Math.random() < 0.5) {
                bankAccount.deposit(amount, name);
            } else {
                bankAccount.withdraw(amount, name);
            }

            try {
                Thread.sleep(500); // Simulate some delay between transactions
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
