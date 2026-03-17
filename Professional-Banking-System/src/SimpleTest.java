/**
 * SimpleTest.java
 * Basic tests to verify functionality
 */

public class SimpleTest {
    private static int testsPassed = 0;
    private static int testsTotal = 0;
    
    public static void main(String[] args) {
        System.out.println("\n🧪 RUNNING BANKING SYSTEM TESTS");
        System.out.println("=".repeat(50));
        
        testAccountCreation();
        testDeposit();
        testWithdraw();
        testInsufficientFunds();
        testTransfer();
        
        System.out.println("=".repeat(50));
        System.out.printf("✅ TESTS PASSED: %d/%d\n", testsPassed, testsTotal);
        System.out.println("=".repeat(50));
    }
    
    private static void testAccountCreation() {
        testsTotal++;
        System.out.print("Test 1: Account Creation... ");
        
        BankAccount acc = new BankAccount("TEST001", "Test User", 1000);
        
        if (acc.getAccountNumber().equals("TEST001") && 
            acc.getAccountHolderName().equals("Test User") && 
            acc.getBalance() == 1000) {
            System.out.println("✅ PASSED");
            testsPassed++;
        } else {
            System.out.println("❌ FAILED");
        }
    }
    
    private static void testDeposit() {
        testsTotal++;
        System.out.print("Test 2: Deposit... ");
        
        BankAccount acc = new BankAccount("TEST002", 1000);
        acc.deposit(500);
        
        if (acc.getBalance() == 1500) {
            System.out.println("✅ PASSED");
            testsPassed++;
        } else {
            System.out.println("❌ FAILED");
        }
    }
    
    private static void testWithdraw() {
        testsTotal++;
        System.out.print("Test 3: Withdraw... ");
        
        BankAccount acc = new BankAccount("TEST003", 1000);
        boolean success = acc.withdraw(300);
        
        if (success && acc.getBalance() == 700) {
            System.out.println("✅ PASSED");
            testsPassed++;
        } else {
            System.out.println("❌ FAILED");
        }
    }
    
    private static void testInsufficientFunds() {
        testsTotal++;
        System.out.print("Test 4: Insufficient Funds... ");
        
        BankAccount acc = new BankAccount("TEST004", 100);
        boolean success = acc.withdraw(200);
        
        if (!success && acc.getBalance() == 100) {
            System.out.println("✅ PASSED");
            testsPassed++;
        } else {
            System.out.println("❌ FAILED");
        }
    }
    
    private static void testTransfer() {
        testsTotal++;
        System.out.print("Test 5: Transfer... ");
        
        BankAccount from = new BankAccount("FROM001", 1000);
        BankAccount to = new BankAccount("TO001", 500);
        
        BankAccount.transfer(from, to, 300);
        
        if (from.getBalance() == 700 && to.getBalance() == 800) {
            System.out.println("✅ PASSED");
            testsPassed++;
        } else {
            System.out.println("❌ FAILED");
        }
    }
}