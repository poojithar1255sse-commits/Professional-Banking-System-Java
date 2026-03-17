/**
 * BankSystem.java
 * Main application demonstrating thread-safe banking operations
 */

import java.util.*;
import java.util.concurrent.*;
import java.text.SimpleDateFormat;

public class BankSystem {
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_THREADS = 15;
    private static final int NUM_TRANSACTIONS = 200;
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    
    public static void main(String[] args) {
        printHeader();
        
        // Create bank accounts
        List<BankAccount> accounts = createAccounts();
        
        // Display initial state
        displayAccounts(accounts, "INITIAL ACCOUNT BALANCES");
        
        // Run concurrent transactions
        runConcurrentTransactions(accounts);
        
        // Display final state
        displayAccounts(accounts, "FINAL ACCOUNT BALANCES");
        
        // Verify money integrity
        verifyMoneyIntegrity(accounts);
        
        // Show transaction history for one account
        accounts.get(0).printTransactionHistory();
        
        // Export history to file
        exportHistory(accounts.get(0));
        
        printFooter();
    }
    
    private static void printHeader() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  PROFESSIONAL BANKING SYSTEM");
        System.out.println("  " + new Date());
        System.out.println("=".repeat(70));
    }
    
    private static List<BankAccount> createAccounts() {
        List<BankAccount> accounts = new ArrayList<>();
        String[] names = {"Raj", "Priya", "Amit", "Neha", "Vikram"};
        double[] initialBalances = {5000, 8000, 3000, 6000, 4000};
        
        for (int i = 0; i < NUM_ACCOUNTS; i++) {
            String accNum = String.format("ACC%03d", i + 1);
            accounts.add(new BankAccount(accNum, names[i], initialBalances[i]));
        }
        
        return accounts;
    }
    
    private static void displayAccounts(List<BankAccount> accounts, String title) {
        System.out.println("\n📊 " + title);
        System.out.println("-".repeat(50));
        double total = 0;
        for (BankAccount acc : accounts) {
            System.out.println("   " + acc.getAccountInfo());
            total += acc.getBalance();
        }
        System.out.println("-".repeat(50));
        System.out.printf("   TOTAL MONEY IN SYSTEM: $%.2f\n", total);
    }
    
    private static void runConcurrentTransactions(List<BankAccount> accounts) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        System.out.println("\n🚀 PROCESSING " + NUM_TRANSACTIONS + " CONCURRENT TRANSACTIONS...");
        System.out.println("   Thread Pool Size: " + NUM_THREADS);
        System.out.println("-".repeat(50));
        
        CountDownLatch latch = new CountDownLatch(NUM_TRANSACTIONS);
        
        for (int i = 0; i < NUM_TRANSACTIONS; i++) {
            executor.submit(new TransactionTask(accounts, latch));
        }
        
        try {
            latch.await();
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("-".repeat(50));
        System.out.println("✅ ALL TRANSACTIONS COMPLETED");
    }
    
    private static void verifyMoneyIntegrity(List<BankAccount> accounts) {
        double initialTotal = 5000 + 8000 + 3000 + 6000 + 4000;
        
        double finalTotal = 0;
        for (BankAccount acc : accounts) {
            finalTotal += acc.getBalance();
        }
        
        System.out.println("\n🔍 MONEY INTEGRITY VERIFICATION");
        System.out.println("-".repeat(50));
        System.out.printf("   Initial Total: $%.2f\n", initialTotal);
        System.out.printf("   Final Total:   $%.2f\n", finalTotal);
        System.out.printf("   Difference:     $%.2f\n", finalTotal - initialTotal);
        
        if (Math.abs(finalTotal - initialTotal) < 0.01) {
            System.out.println("   ✅ VERIFICATION PASSED: No money lost or created!");
            System.out.println("   💡 All transactions were thread-safe!");
        } else {
            System.out.println("   ❌ VERIFICATION FAILED: Money discrepancy detected!");
        }
    }
    
    private static void exportHistory(BankAccount account) {
        try {
            account.exportHistoryToFile();
        } catch (Exception e) {
            System.out.println("❌ Failed to export history: " + e.getMessage());
        }
    }
    
    private static void printFooter() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  END OF DEMONSTRATION");
        System.out.println("  " + new Date());
        System.out.println("=".repeat(70));
    }
    
    // Task for concurrent transactions
    static class TransactionTask implements Runnable {
        private final List<BankAccount> accounts;
        private final CountDownLatch latch;
        private final Random random = new Random();
        
        TransactionTask(List<BankAccount> accounts, CountDownLatch latch) {
            this.accounts = accounts;
            this.latch = latch;
        }
        
        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(50));
                
                int type = random.nextInt(4);
                double amount = 100 + random.nextInt(900);
                
                int fromIdx = random.nextInt(accounts.size());
                int toIdx;
                do {
                    toIdx = random.nextInt(accounts.size());
                } while (toIdx == fromIdx);
                
                BankAccount acc1 = accounts.get(fromIdx);
                BankAccount acc2 = accounts.get(toIdx);
                
                switch(type) {
                    case 0:
                        acc1.deposit(amount);
                        break;
                    case 1:
                        acc2.withdraw(amount);
                        break;
                    case 2:
                        BankAccount.transfer(acc1, acc2, amount);
                        break;
                    case 3:
                        double bal = acc1.getBalance();
                        if (random.nextInt(10) == 0) {
                            System.out.printf("   %s checked balance of %s: $%.2f%n", 
                                Thread.currentThread().getName(), 
                                acc1.getAccountNumber(), bal);
                        }
                        break;
                }
            } catch (Exception e) {
                System.out.println("❌ Transaction error: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        }
    }
}