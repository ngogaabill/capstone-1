package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transactions> transactionsArrayList = new ArrayList<>();
    static String fileName = "src/main/resources/transactions.csv";

    public static void main(String[] args) {
        System.out.println("========== WELCOME TO BILL'S LEDGER APP ==========");
        System.out.println("|                     :)                          |");
        System.out.println("===================================================");

        loadTransactionsFromFile();
        homePage();
    }

    /**
     * Load Existing Transactions into an ArrayList of Transactions.
     */
    private static void loadTransactionsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String data;
            while ((data = reader.readLine()) != null) {
                String[] parts = data.split("\\|");
                //skip the header
                if (parts[0].equalsIgnoreCase("date")) {
                    continue;
                }
                String date = parts[0];
                String time = parts[1];
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                Transactions transaction = new Transactions(date, time, description, vendor, amount);
                transactionsArrayList.add(transaction);
            }
        } catch (IOException E) {
            System.err.println("File Not Found");
        }
    }

    /**
     * HomePage: Print the Home Page Screen
     */
    public static void homePage() {
        boolean exit = false;
        while (!exit) {
            System.out.print("""
                    Please Enter a Letter Corresponding to Your Choice
                        D) Add Deposit
                        P) Make Payment
                        L) Ledger
                        X) Exit
                    Your Choice: """);
            char userChoice = Character.toUpperCase(scanner.next().charAt(0));
            switch (userChoice) {
                case 'D':
                    addDeposit();
                    break;
                case 'P':
                    makePayment();
                    break;
                case 'L':
                    ledger();
                    break;
                case 'X':
                    System.out.println("GOOD BYE!!");
                    exit = true;

            }

        }

    }

    /**
     * Add a New Deposit to the ArrayList
     */
    public static void addDeposit() {
        System.out.println("Please Enter Debit Description:");
        loadUserInfoToFile(false);

    }

    /**
     * Add New Payments to the ArrayList
     */
    public static void makePayment() {
        System.out.println("Please Enter Payment Description:");
        loadUserInfoToFile(true);
    }

    /**
     * Ledger Screen Manu
     */
    public static void ledger() {
        boolean returnHome = false;
        while (!returnHome) {
            System.out.println("""
                    A) Display all entries
                    D) Deposits
                    P) Payments
                    R) Reports
                    H) Home
                    Your Choice:""");

            char userChoice = Character.toUpperCase(scanner.next().charAt(0));
            switch (userChoice) {
                case 'A':
                    allEntries();
                    break;
                case 'D':
                    deposits();
                    break;
                case 'P':
                    payments();
                    break;
                case 'R':
                    report();
                    break;
                case 'H':
                    returnHome = true;
                    break;
            }
        }

    }



    /**
     * Get Info from user and save to CSV File and append to Transaction ArrayLists
     *
     * @param isPayment
     */
    public static void loadUserInfoToFile(boolean isPayment) {

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss");
        String timeDateStamp = currentTime.format(formatter);
        String[] timeDateParts = timeDateStamp.split("\\|");
        String date = timeDateParts[0];
        String time = timeDateParts[1];

        scanner.nextLine();
        String description = scanner.nextLine();
        System.out.println("Vendor:");
        String vendor = scanner.nextLine();
        System.out.println("Amount:");
        double amount = scanner.nextDouble();

        if (isPayment) {
            amount *= -1;
        }

        scanner.nextLine();//consume the new Line
        Transactions transactions = new Transactions(date, time, description, vendor, amount);
        transactionsArrayList.add(transactions);
        System.out.println("Transaction Added!!");

        File file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Can't Create File");
        }
        boolean noHeader = file.length() == 0;
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true))) {
            if (noHeader) {
                String header = "date|time|description|vendor|amount";
                bufferedWriter.write(header);
                bufferedWriter.newLine();
            }

            String dataToFile = date + "|" + time + "|" + description + "|" + vendor + "|" + amount;
            bufferedWriter.write(dataToFile);
            bufferedWriter.newLine(); // Add a new line after each transaction

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * * Print Debits in the Transaction List
     */
    public static void deposits() {
        System.out.println("Your Deposit List:");
        Collections.sort(transactionsArrayList);
        for (Transactions transactions:transactionsArrayList) {
            if (transactions.getAmount() > 0) {
                System.out.println(transactions.toString());
            }
        }
    }

    /**
     * Print Payments in Transaction List
     */
    public static void payments() {
        System.out.println("Your Payment List:");
        Collections.sort(transactionsArrayList);
        for (Transactions transactions:transactionsArrayList) {
            if (transactions.getAmount() < 0) {
                System.out.println(transactions.toString());
            }
        }
    }

    /**
     * * Print All Transaction in the ArrayList from Newest-Oldest
     */
    public static void allEntries() {
        System.out.println("All Entries:");
        Collections.sort(transactionsArrayList);
        for (Transactions transactions:transactionsArrayList) {
            System.out.println(transactions.toString());
        }
    }

    /**
     * Report method to display the menu
     */
    public static void report() {
        System.out.print("""
                1) Month To Date
                2) Previous Month
                3) Year To Date
                4) Previous Year
                5) Search by Vendor
                6) Custom Search
                0) Back
                Your Choice:""");

        int userChoice = scanner.nextInt();
        switch (userChoice) {
            case 1:
                monthToDate();
                break;
            case 2:
                previousMonth();
                break;
            case 3:
                yearToDate();
                break;
            case 4:
                previousYear();
                break;
            case 5:
                searchByVendor();
                break;
            case 6:
                customSearch();
                break;
            case 0:
                ledger();
                break;
        }
    }

    /**
     * Print Transactions of the Current Month
     */
    public static void monthToDate() {
        System.out.println("Transactions For Current Month");
        currentAndPreviousDates("current-month");
    }

    /**
     * Print Transactions of the Previous Month
     */
    public static void previousMonth() {
        System.out.println("Transactions For Previous Month");
        currentAndPreviousDates("previous-month");
    }

    /**
     * Print Transaction of the Current Year
     */
    public static void yearToDate() {
        System.out.println("Transactions For Year");
        currentAndPreviousDates("current-year");
    }

    /**
     * Print Transactions ot the Previous Year
     */
    public static void previousYear() {
        System.out.println("Transactions For Previous Year");
        currentAndPreviousDates("previous-year");
    }

    /**
     * Handle Date Sorting based on their status
     *
     * @param statusReport
     */
    public static void currentAndPreviousDates(String statusReport) {
        boolean found = false;
        //current Times
        LocalDate currentDate = LocalDate.now();
        int monthValue = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        //Previous Times
        LocalDate previousMonthDate = currentDate.minusMonths(1);
        int previousMonth = previousMonthDate.getMonthValue();
        int previousYear = previousMonthDate.getYear() - 1;

        for (int i = transactionsArrayList.size() - 1; i >= 0; i--) {
            Transactions transactions = transactionsArrayList.get(i);

            String[] dateParts = transactions.getDate().split("-");
            int currentMonthPart = Integer.parseInt(dateParts[1]);
            int currentYearPart = Integer.parseInt(dateParts[0]);
            //current Month
            if (statusReport.equals("current-month") && (currentYear == currentYearPart) && monthValue == currentMonthPart) {
                System.out.println(transactions.toString());
                found = true;
            }
            //Previous Month
            if ((previousMonth == currentMonthPart) && statusReport.equalsIgnoreCase("previous-month")) {
                System.out.println((transactions.toString()));
                found = true;
            }
            //Current year
            if ((currentYear == currentYearPart) && statusReport.equalsIgnoreCase("current-year")) {
                System.out.println((transactions.toString()));
                found = true;
            }
            //Previous Year
            if ((previousYear == currentYearPart) && statusReport.equalsIgnoreCase("previous-year")) {
                System.out.println((transactions.toString()));
                found = true;
            }
        }
        if (!found) {
            System.out.println("No Data Found!");
        }
    }

    /**
     * Print Transaction Based to the Entered Details
     */
    private static void customSearch() {
        System.out.println("Please Enter all required details for the custom Search/Enter to Continue");
        System.out.println("Start Date(yyyy-mm-dd)");
        scanner.nextLine();
        String userStartDate = scanner.nextLine().trim();
        System.out.println("End Date");
        String userEndDate = scanner.nextLine().trim();
        System.out.println("Description");
        String description = scanner.nextLine().trim();
        System.out.println("Vendor");
        String vendor = scanner.nextLine();
        System.out.println("Amount");
        String inputAmount = scanner.nextLine();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Transactions trans : transactionsArrayList) {
            boolean found = true;
            LocalDate transactionDate = LocalDate.parse(trans.getDate(), dateFormatter);//parse the transaction string date into a local date
            if (!userStartDate.isEmpty()) {
                LocalDate startDate = LocalDate.parse(userStartDate, dateFormatter);
                if (transactionDate.isBefore(startDate)) {
                    found = false;
                }
            }
            if (!userEndDate.isEmpty()) {
                LocalDate endDate = LocalDate.parse(userEndDate, dateFormatter);
                if (transactionDate.isAfter(endDate)) {
                    found = false;
                }
            }
            if (!description.isEmpty() && !trans.getDescription().contains(description)) {
                found = false;
            }
            if (!vendor.isEmpty() && !trans.getVendor().contains(vendor)) {
                found = false;
            }
            if (!inputAmount.isEmpty()) {
                double amount = Double.parseDouble(inputAmount);
                if (trans.getAmount() != amount) {
                    found = false;
                }
            }
            if (found) {
                System.out.println(trans);
            }
        }
    }

    /**
     * Search Vendor by name and Print if Found
     */
    public static void searchByVendor() {
        scanner.nextLine();
        boolean vendorFound = false;
        System.out.println("Enter Vendor To Search For:");
        String vendorSearch = scanner.nextLine();
        for (int i = 0; i < transactionsArrayList.size(); i++) {
            if (transactionsArrayList.get(i).getVendor().equalsIgnoreCase(vendorSearch)) {
                System.out.println(transactionsArrayList.get(i).toString());
                vendorFound = true;
            }
        }
        if (!vendorFound) {
            System.out.println("Vendor is not Found");
        }
    }
}







