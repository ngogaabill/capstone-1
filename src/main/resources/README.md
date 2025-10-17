#  Capstone-1 (Ledger App)
# Author
Name: Bill Ngoga Ngabo
Done by: 10/16/2025
---

**Bill's Ledger App** is a Java-based transaction tracker built to help users take control of their personal finances.  
It allows you to record income as **Deposits** and expenses as **Payments**, storing each entry in a structured CSV file.  
The app also generates various reports and provides a complete transaction history.

---

## 🔑 Key Features

- **Transaction Management**: Add deposits and payments with automatic timestamping
- **Persistent Storage**: All transactions saved in CSV format for portability
- **Smart Filtering**: View all transactions or filter by deposits/payments
- **Advanced Reporting**: Monthly, yearly, and custom date range reports
- **Vendor Search**: Find all transactions with specific vendors
- **Custom Search**: Filter by date range, description, vendor, and amount
- **Automatic Sorting**: Newest transactions displayed first using Java’s `Comparable` interface

---
> _These screenshots showcase the app’s user-friendly prompts and core functionality._

### 🏠 Home Screen
![Home Screen Screenshot](images/HomePage.png)

---

### 📋 All Entries
![All Entries Screenshot](images/All.png)

---

### 📊 Report Screen
# User has multiple choices to onto what he wants to interact with.
---
![Report Screen Screenshot](images/REPORT.png)

---

### 📁 Ledger Menu
![Ledger Screenshot](images/Ledger.png)

---

# 🔍 Custom Search
# User Inputs Start & End Dates,Description,Vendor and Amount; get a well Sorted transaction list based on his criteria.
---
![Custom Search Screenshot](images/CUSTOM%20SEARCH.png)

---
## Interesting Code
----
# Transactions Comparator Implementation

## Overview
This README documents an overridden `compareTo` method from a Java class, likely implementing the `Comparable` interface for a custom `Transactions` class. The method is designed to compare two `Transactions` objects based on their `date` and `time` fields. It sorts transactions in **descending order** by date first, and if dates are equal, by time in descending order.

This code is useful for scenarios where you need to sort a list of transactions (e.g., in a financial application) so that the most recent ones appear first.


```java
import java.time.LocalDate;
import java.time.LocalTime;

public class Transactions implements Comparable<Transactions> {
   
    private String date;  
    private String time;  

    @Override
    public int compareTo(Transactions other) {
        LocalDate thisDate = LocalDate.parse(this.date);
        LocalDate otherDate = LocalDate.parse(other.date);

        int dateCompare = otherDate.compareTo(thisDate);  // Compares in descending order
        if (dateCompare != 0) {
            return dateCompare;
        }
        
        LocalTime thisTime = LocalTime.parse(this.time);
        LocalTime otherTime = LocalTime.parse(other.time);
        return otherTime.compareTo(thisTime);  // Compares in descending order
    }
}
