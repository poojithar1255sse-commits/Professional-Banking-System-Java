/**
 * EnhancedBankSystem.java
 * Advanced version with more features and analytics
 */

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class EnhancedBankSystem {
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_THREADS = 20;
    private static final int NUM_TRANSACTIONS = 500;
    
    private static AtomicInteger successfulTransactions = new AtomicInteger(0);
    private static AtomicInteger failedTransactions = new AtomicInteger(0);
    private static ConcurrentHashMap<String, Long> transactionTimes = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        System.out.println("\n" + "🌟".repeat(35));
        System.out.println("  ENHANCED BANKING SYSTEM - PERFORMANCE EDITION");
        System.out.println("🌟".repeat(35));
        
        List<BankAccount> accounts = createEnhancedAccounts();
        printAccountSummary(accounts, "INITIAL STATE");
        runBenchmark(accounts);
        printAccountSummary(accounts, "FINAL STATE");
        printPerformanceMetrics();
        verifyDataIntegrity(accounts);
    }
    
    private static List<BankAccount> createEnhancedAccounts() {
        List<BankAccount> accounts = new ArrayList<>();
        String[] names = {"Raj Sharma", "Priya Patel", "Amit Kumar", "Neha Singh", "Vikram Mehta"};
        double[] balances = {10000, 15000, 8000, 12000, 9000};
        
        for (int i = 0; i < NUM_ACCOUNTS; i++) {
            accounts.add(new BankAccount(
                String.format("ACC%03d", i + 1),
                names[i],
                balances[i]
            ));
        }
        return accounts;
    }
    
    private static void runBenchmark(List<BankAccount> accounts) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_TRANSACTIONS);
        
        System.out.println("\n🚀 RUNNING PERFORMANCE BENCHMARK");
        System.out.println("   Transactions: " + NUM_TRANSACTIONS);
        System.out.println("   Threads: " + NUM_THREADS);
        System.out.println("-".repeat(50));
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < NUM_TRANSACTIONS; i++) {
            executor.submit(new EnhancedTransactionTask(accounts, latch, i));
        }
        
        try {
            latch.await();
            long endTime = System.currentTimeMillis();
            
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
            
            System.out.println("-".repeat(50));
            System.out.printf("⏱️  Total Time: %d ms\n", (endTime - startTime));
            System.out.printf("📊 Throughput: %.2f transactions/second\n", 
                (NUM_TRANSACTIONS * 1000.0 / (endTime - startTime)));
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static void printAccountSummary(List<BankAccount> accounts, String title) {
        System.out.println("\n📊 " + title);
        System.out.println("-".repeat(60));
        double total = 0;
        for (BankAccount acc : accounts) {
            System.out.printf("   %-15s | %-15s | $%8.2f\n", 
                acc.getAccountNumber(), 
                acc.getAccountHolderName(), 
                acc.getBalance());
            total += acc.getBalance();
        }
        System.out.println("-".repeat(60));
        System.out.printf("   TOTAL: $%.2f\n", total);
    }
    
    private static void printPerformanceMetrics() {
        System.out.println("\n📈 PERFORMANCE METRICS");
        System.out.println("-".repeat(50));
        System.out.printf("   Successful Transactions: %d\n", successfulTransactions.get());
        System.out.printf("   Failed Transactions: %d\n", failedTransactions.get());
        System.out.printf("   Success Rate: %.2f%%\n", 
            (successfulTransactions.get() * 100.0 / 
             (successfulTransactions.get() + failedTransactions.get())));
    }
    
    private static void verifyDataIntegrity(List<BankAccount> accounts) {
        double total = accounts.stream()
            .mapToDouble(BankAccount::getBalance)
            .sum();
        
        double expectedTotal = 10000 + 15000 + 8000 + 12000 + 9000;
        
        System.out.println("\n🔒 DATA INTEGRITY CHECK");
        System.out.println("-".repeat(50));
        System.out.printf("   Expected Total: $%.2f\n", expectedTotal);
        System.out.printf("   Actual Total:   $%.2f\n", total);
        System.out.printf("   Difference:      $%.2f\n", total - expectedTotal);
        
        if (Math.abs(total - expectedTotal) < 0.01) {
            System.out.println("   ✅ INTEGRITY VERIFIED: No money lost!");
        } else {
            System.out.println("   ❌ INTEGRITY VIOLATION: Money discrepancy!");
        }
    }
    
    static class EnhancedTransactionTask implements Runnable {
        private final List<BankAccount> accounts;
        private final CountDownLatch latch;
        private final int taskId;
        private final Random random = new Random();
        
        EnhancedTransactionTask(List<BankAccount> accounts, CountDownLatch latch, int taskId) {
            this.accounts = accounts;
            this.latch = latch;
            this.taskId = taskId;
        }
        
        @Override
        public void run() {
            long startTime = System.nanoTime();
            boolean success = false;
            
            try {
                Thread.sleep(random.nextInt(10));
                
                int type = random.nextInt(5);
                double amount = 50 + random.nextInt(950);
                
                int idx1 = random.nextInt(accounts.size());
                int idx2;
                do {
                    idx2 = random.nextInt(accounts.size());
                } while (idx2 == idx1);
                
                BankAccount acc1 = accounts.get(idx1);
                BankAccount acc2 = accounts.get(idx2);
                
                switch(type) {
                    case 0:
                        acc1.deposit(amount);
                        success = true;
                        break;
                    case 1:
                        success = acc2.withdraw(amount);
                        break;
                    case 2:
                        BankAccount.transfer(acc1, acc2, amount);
                        success = true;
                        break;
                    case 3:
                        double bal = acc1.getBalance();
                        success = true;
                        break;
                    case 4:
                        performComplexTransaction(acc1, acc2);
                        success = true;
                        break;
                }
                
                if (success) {
                    successfulTransactions.incrementAndGet();
                } else {
                    failedTransactions.incrementAndGet();
                }
                
            } catch (Exception e) {
                failedTransactions.incrementAndGet();
            } finally {
                long endTime = System.nanoTime();
                transactionTimes.put("Task-" + taskId, endTime - startTime);
                latch.countDown();
            }
        }
        
        private void performComplexTransaction(BankAccount acc1, BankAccount acc2) {
            double amount1 = 100 + random.nextInt(200);
            double amount2 = 50 + random.nextInt(100);
            
            acc1.withdraw(amount1);
            acc2.deposit(amount1);
            acc1.deposit(amount2);
        }
    }
}