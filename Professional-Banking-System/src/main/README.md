# Professional Banking System - Java Concurrency Project 🏦

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Concurrency](https://img.shields.io/badge/Concurrency-Thread--Safe-blue.svg)]()
[![JPMC](https://img.shields.io/badge/JPMorgan-Chase-green.svg)]()

## 📋 Project Overview

A production-ready, thread-safe banking system built with Java that demonstrates advanced concurrency concepts. This project simulates real-world banking operations with multiple concurrent transactions, ensuring data integrity and preventing race conditions.

### 🎯 Key Features

- **Thread-Safe Operations**: Uses `ReentrantReadWriteLock` for optimal concurrency
- **Deadlock Prevention**: Implements ordered lock acquisition strategy
- **High Throughput**: Handles 500+ concurrent transactions
- **Data Integrity**: Guarantees ACID properties for all transactions
- **Transaction History**: Complete audit trail for each account
- **Performance Metrics**: Real-time monitoring and statistics

## 🛠️ Technology Stack

- **Language**: Java 21
- **Concurrency**: `ReentrantReadWriteLock`, `ExecutorService`, `CountDownLatch`
- **Data Structures**: Concurrent Collections, Thread-safe Lists
- **Testing**: JUnit-style validation

## 🚀 Quick Start

### Prerequisites
- Java JDK 11 or higher
- Git (optional)

### Installation

```bash
# Clone the repository
git clone https://github.com/yourusername/Professional-Banking-System.git

# Navigate to project
cd Professional-Banking-System/src

# Compile all Java files
javac *.java

# Run the main banking system
java BankSystem

# Run enhanced version (with performance metrics)
java EnhancedBankSystem

# Run tests
java SimpleTest
Sample Output
======================================================================
  PROFESSIONAL BANKING SYSTEM
======================================================================

📊 INITIAL ACCOUNT BALANCES
--------------------------------------------------
   ACC001 (Raj) - $5000.00
   ACC002 (Priya) - $8000.00
   ACC003 (Amit) - $3000.00
   ACC004 (Neha) - $6000.00
   ACC005 (Vikram) - $4000.00
--------------------------------------------------
   TOTAL: $26000.00

🚀 PROCESSING 200 CONCURRENT TRANSACTIONS...
--------------------------------------------------
pool-1-thread-1 ✅ DEPOSIT: $450.00 to ACC003
pool-1-thread-2 ✅ TRANSFER: $320.00 from ACC001 to ACC005
pool-1-thread-3 ❌ WITHDRAW FAILED: Insufficient funds in ACC002
pool-1-thread-4 ✅ WITHDRAW: $180.00 from ACC004
...
--------------------------------------------------
✅ ALL TRANSACTIONS COMPLETED

📊 GLOBAL TRANSACTION STATISTICS
--------------------------------------------------
   Successful Transactions: 145
   Failed Transactions: 55
   Success Rate: 72.5%
   Total Money Moved: $84,532.00

🔍 MONEY INTEGRITY VERIFICATION
--------------------------------------------------
   Initial Total: $26000.00
   Final Total:   $26000.00
   Difference:     $0.00
   ✅ VERIFICATION PASSED: No money lost or created!



🔒 Key Concepts Demonstrated
lock.lock();
try {
    // Critical section - only one thread at a time
    balance = balance - amount;
} finally {
    lock.unlock(); // Always unlock!
}

2. Deadlock Prevention

// Lock accounts in consistent order (by hashcode)
BankAccount first = from;
BankAccount second = to;
if (System.identityHashCode(from) > System.identityHashCode(to)) {
    first = to;
    second = from;
}
first.lock.lock();
second.lock.lock();

3. Concurrent Transaction Processing

ExecutorService executor = Executors.newFixedThreadPool(15);
CountDownLatch latch = new CountDownLatch(200);
// Submit 200 concurrent transactions
for (int i = 0; i < 200; i++) {
    executor.submit(new TransactionTask(accounts, latch));
}
latch.await(); // Wait for all to complete


📁 Project Structure

Professional-Banking-System-Java/
├── src/
│   ├── BankAccount.java          # Core account logic with ReentrantLock
│   ├── BankSystem.java            # Main application with transaction processing
│   ├── EnhancedBankSystem.java    # Advanced version with performance metrics
│   └── SimpleTest.java            # Unit tests
├── .gitignore                      # Git ignore rules
└── README.md                       # This file


📊 Transaction Statistics
The program tracks these metrics in real-time:

Statistic	Description
Successful Transactions	Deposits/withdrawals/transfers that completed
Failed Transactions	Attempts rejected due to insufficient funds
Success Rate	Percentage of successful operations
Total Money Moved	Sum of all successful transaction amounts
Money Integrity	Verification that total money remains constant

🧪 Testing
Run the test suite to verify all functionality:


java SimpleTest
Test Coverage:

✅ Account creation with initial balance

✅ Deposit operations

✅ Withdrawal with sufficient funds

✅ Insufficient funds handling

✅ Transfers between accounts

✅ Thread-safe concurrent access


🔬 Why This Project Matters

This project demonstrates skills that are critical for financial software development:

Data Integrity - Ensuring money never disappears

Concurrency Control - Handling thousands of simultaneous users

Deadlock Prevention - Avoiding system freezes

Error Handling - Gracefully managing insufficient funds

Audit Trail - Maintaining complete transaction history


🤝 Contributing
Contributions are welcome! To contribute:

Fork the repository

Create a feature branch (git checkout -b feature/AmazingFeature)

Commit your changes (git commit -m 'Add some AmazingFeature')

Push to the branch (git push origin feature/AmazingFeature)

Open a Pull Request


📝 License
This project is licensed under the MIT License - see the LICENSE file for details.


👨‍💻 Author
Poojitha

GitHub: @poojithar1255sse-commits

Project Link: https://github.com/poojithar1255sse-commits/Professional-Banking-System-Java

⭐ Show Your Support
If you found this project helpful for learning Java concurrency:

Give it a ⭐ on GitHub

Share it with friends preparing for interviews

Fork it and add your own features

📬 Contact & Questions
For questions, suggestions, or feedback:

Open an issue on GitHub

Reach out via GitHub discussions

Built with ❤️ for Java Concurrency Enthusiasts 


