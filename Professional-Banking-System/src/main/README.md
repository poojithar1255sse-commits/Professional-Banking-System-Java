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