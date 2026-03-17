/**
 * BankAccount.java
 * Thread-safe bank account implementation using ReentrantReadWriteLock
 * COMPLETELY FIXED VERSION - All methods properly locked
 */

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.*;
import java.io.*;

public class BankAccount {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private List<Transaction> transactionHistory = new ArrayList<>();
    
    // Constructor with account holder name
    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        addToHistory("ACCOUNT_CREATED", initialBalance, "Initial deposit", initialBalance);
    }
    
    // Simplified constructor
    public BankAccount(String accountNumber, double initialBalance) {
        this(accountNumber, "Customer-" + accountNumber, initialBalance);
    }
    
    // Thread-safe deposit
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println(Thread.currentThread().getName() + 
                " ❌ DEPOSIT FAILED: Amount must be positive");
            return;
        }
        
        lock.writeLock().lock();
        try {
            double oldBalance = balance;
            balance = balance + amount;
            String message = String.format("Deposited $%.2f", amount);
            addToHistory("DEPOSIT", amount, message, balance);
            
            System.out.println(Thread.currentThread().getName() + 
                String.format(" ✅ DEPOSIT: $%.2f to %s (%s) | New Balance: $%.2f", 
                amount, accountNumber, accountHolderName, balance));
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    // Thread-safe withdraw
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println(Thread.currentThread().getName() + 
                " ❌ WITHDRAW FAILED: Amount must be positive");
            return false;
        }
        
        lock.writeLock().lock();
        try {
            if (balance >= amount) {
                double oldBalance = balance;
                balance = balance - amount;
                String message = String.format("Withdrew $%.2f", amount);
                addToHistory("WITHDRAW", amount, message, balance);
                
                System.out.println(Thread.currentThread().getName() + 
                    String.format(" ✅ WITHDRAW: $%.2f from %s (%s) | New Balance: $%.2f", 
                    amount, accountNumber, accountHolderName, balance));
                return true;
            } else {
                System.out.println(Thread.currentThread().getName() + 
                    String.format(" ❌ WITHDRAW FAILED: Insufficient funds in %s | Available: $%.2f, Requested: $%.2f", 
                    accountNumber, balance, amount));
                addToHistory("WITHDRAW_FAILED", amount, "Insufficient funds", balance);
                return false;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    // Thread-safe transfer with proper locking
    public static void transfer(BankAccount from, BankAccount to, double amount) {
        if (amount <= 0) {
            System.out.println(Thread.currentThread().getName() + " ❌ TRANSFER FAILED: Amount must be positive");
            return;
        }
        
        // Deadlock prevention: lock accounts in order of their hashcodes
        BankAccount firstLock = from;
        BankAccount secondLock = to;
        
        if (System.identityHashCode(from) > System.identityHashCode(to)) {
            firstLock = to;
            secondLock = from;
        }
        
        // Lock both accounts
        firstLock.lock.writeLock().lock();
        secondLock.lock.writeLock().lock();
        
        try {
            if (from.balance >= amount) {
                double fromOldBalance = from.balance;
                double toOldBalance = to.balance;
                
                // Perform transfer while holding both locks
                from.balance = from.balance - amount;
                to.balance = to.balance + amount;
                
                String fromMessage = String.format("Transferred $%.2f to %s", amount, to.accountNumber);
                String toMessage = String.format("Received $%.2f from %s", amount, from.accountNumber);
                
                from.addToHistory("TRANSFER_OUT", amount, fromMessage, from.balance);
                to.addToHistory("TRANSFER_IN", amount, toMessage, to.balance);
                
                System.out.println(Thread.currentThread().getName() + 
                    String.format(" ✅ TRANSFER: $%.2f from %s (%s) to %s (%s)", 
                    amount, from.accountNumber, from.accountHolderName, 
                    to.accountNumber, to.accountHolderName));
            } else {
                System.out.println(Thread.currentThread().getName() + 
                    String.format(" ❌ TRANSFER FAILED: Insufficient funds in %s | Available: $%.2f, Requested: $%.2f", 
                    from.accountNumber, from.balance, amount));
                from.addToHistory("TRANSFER_FAILED", amount, "Insufficient funds", from.balance);
            }
        } finally {
            // Unlock in reverse order
            secondLock.lock.writeLock().unlock();
            firstLock.lock.writeLock().unlock();
        }
    }
    
    // Thread-safe balance check
    public double getBalance() {
        lock.readLock().lock();
        try {
            return balance;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // Get account info (thread-safe)
    public String getAccountInfo() {
        lock.readLock().lock();
        try {
            return String.format("%s (%s) - Balance: $%.2f", 
                accountNumber, accountHolderName, balance);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // Add transaction to history - FIXED: Now takes balanceAfter as parameter
    private void addToHistory(String type, double amount, String description, double balanceAfter) {
        transactionHistory.add(new Transaction(type, amount, balanceAfter, description));
    }
    
    // Print transaction history
    public void printTransactionHistory() {
        lock.readLock().lock();
        try {
            System.out.println("\n📋 Transaction History for " + accountNumber + " (" + accountHolderName + "):");
            System.out.println("   " + "=".repeat(60));
            int startIdx = Math.max(0, transactionHistory.size() - 10);
            for (int i = startIdx; i < transactionHistory.size(); i++) {
                System.out.println("   " + transactionHistory.get(i));
            }
            if (transactionHistory.size() > 10) {
                System.out.println("   ... and " + (transactionHistory.size() - 10) + " more transactions");
            }
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // Export transaction history to file
    public void exportHistoryToFile() throws IOException {
        lock.readLock().lock();
        try {
            String filename = "account_" + accountNumber + "_history.txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println("Transaction History for Account: " + accountNumber);
                writer.println("Account Holder: " + accountHolderName);
                writer.println("=".repeat(60));
                for (Transaction t : transactionHistory) {
                    writer.println(t.toFileString());
                }
                writer.println("=".repeat(60));
                writer.println("Current Balance: $" + balance);
            }
            System.out.println("✅ Exported history to " + filename);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // Inner class for transactions
    private static class Transaction {
        private final String type;
        private final double amount;
        private final double balanceAfter;
        private final String description;
        private final Date timestamp;
        
        Transaction(String type, double amount, double balanceAfter, String description) {
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
            this.description = description;
            this.timestamp = new Date();
        }
        
        @Override
        public String toString() {
            return String.format("[%tT] %-12s | %s | Balance: $%.2f", 
                timestamp, type, description, balanceAfter);
        }
        
        public String toFileString() {
            return String.format("%tF %tT | %-12s | $%.2f | %s | Balance: $%.2f", 
                timestamp, timestamp, type, amount, description, balanceAfter);
        }
    }
    
    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getAccountHolderName() {
        return accountHolderName;
    }
}