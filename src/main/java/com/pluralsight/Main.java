package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transactions> transactionsArrayList = new ArrayList<>();
    static String fileName = "src/main/resources/transactions.csv";

    public static void main(String[] args) {
        System.out.println("========== WELCOME TO BILL'S LEDGER APP ==========");
        System.out.println("|                     :)                          |");
        System.out.println("===================================================");

        loadTransactionsFromFile();//Load existing info from file to arraylist.
        homePage();
    }

    private static void loadTransactionsFromFile() {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String data;
            while((data = reader.readLine()) != null){
                String[] parts = data.split("\\|");
                if(parts[0].equalsIgnoreCase("date")){
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
        }catch (IOException E){
            System.err.println("File Not Found");
        }

    }

    /**
     * HomePage Method to Print the Home Page Screen
     */
    public static void homePage() {
        boolean exit = false;
        while (!exit) {
            System.out.print("Please Enter a Letter Corresponding to Your Choice\nD) Add Deposit\n" +
                    "P) Make Payment (Debit)\n" +
                    "L) Ledger\n" +
                    "E) Exit\n" +
                    "Your Choice:");
            char userChoice = Character.toUpperCase(scanner.next().charAt(0));//read the char
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
                case 'E':
                    System.out.println("GOOD BYE!!E");
                    exit = true;

            }

        }

    }

    /**
     * Adds deposits to the arrayList
     *
     */
    public static void addDeposit() {
        System.out.println("Please Enter Debit Description:");
        loadUserInfoToFile(false);

    }

    /**
     * Add Payments to the list
     */
    public static void makePayment() {
        System.out.println("Please Enter Payment Description:");
        loadUserInfoToFile(true);
    }


    /**
     * ledger Manuel
     */
    public static void ledger() {
        boolean returnHome = false;
        while (!returnHome) {
            System.out.println("A)Display all entries\n" +
                    "D) Deposits\n" +
                    "account\n" +
                    "P) Payments\n" +
                    "R) Reports\n" +
                    "H) Home\n" +
                    "Your Choice:");

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

            }
        }

    }


    /**
     * Get Info from user based on the transactions save to FILE and respective ArrayLists
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

        if (isPayment) { //if so turn into negative amount
            amount *= -1;
        }

        scanner.nextLine();//consume the newline
        Transactions transactions = new Transactions(date, time, description, vendor, amount);
        transactionsArrayList.add(transactions);

        File file = new File(fileName);
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
        for (int i = 0; i < transactionsArrayList.size(); i++) {
            if (transactionsArrayList.get(i).getAmount() > 0) {
                System.out.println(transactionsArrayList.get(i).toString());
            }
        }
    }

    /**
     * Print payments in Transaction List
     */
    public static void payments() {
        System.out.println("Your Payment List:");
        for (int i = 0; i < transactionsArrayList.size(); i++) {
            if (transactionsArrayList.get(i).getAmount() < 0) {
                System.out.println(transactionsArrayList.get(i).toString());
            }
        }

    }

    /**
     * * Print All Transaction Read From the File
     */
    public static void allEntries() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String entries;
            while ((entries = bufferedReader.readLine()) != null) {
                System.out.println(entries);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Report method to display the menu
     */
    public static void report() {
        System.out.println("1) Month To Date\n" +
                "2) Previous Month\n" +
                "3) Year To Date\n" +
                "4) Previous Year\n" +
                "5) Search by Vendor\n" +
                "0) Back\n" +
                "Choice: ");
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
            case 0:
                ledger();
                break;

        }
    }

    public static void yearToDate() {
        currentAndPreviousMonth("current-year");
    }

    public static void previousMonth() {
        currentAndPreviousMonth("previous-month");
    }

    /**
     * Search Vendor by name and print if match is found
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

    public static void previousYear() {
        currentAndPreviousMonth("previous-year");
    }

    public static void monthToDate() {

        currentAndPreviousMonth("current-month");
    }

    public static void currentAndPreviousMonth(String statusReport) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String entries;
            boolean found = false;
            //current Times
            LocalDate currentDate = LocalDate.now();
            int monthValue = currentDate.getMonthValue();
            int currentYear = currentDate.getYear();
            //Previous Times
            int previousMonth = currentDate.getMonthValue() - 1;
            int previousYear = currentDate.getYear() - 1;

            while ((entries = bufferedReader.readLine()) != null) {
                String[] parts = entries.split("\\|");
                String date = parts[0];
                if (parts[0].equalsIgnoreCase("date")) {
                    continue;//skip the header
                }

                String[] dateParts = date.split("-");
                int currentMonthPart = Integer.parseInt(dateParts[1]);
                int currentYearPart = Integer.parseInt(dateParts[0]);

                //current Month
                if (statusReport.equals("current-month") && (currentYear == currentYearPart) && monthValue == currentMonthPart) {
                    System.out.println(entries);
                    found = true;
                }
                //Previous Month
                if ((previousMonth == currentMonthPart) && statusReport.equalsIgnoreCase("previous-month")) {
                    System.out.println(entries);
                    found = true;
                }
                //Current year
                if ((currentYear == currentYearPart) && statusReport.equalsIgnoreCase("current-Year")) {
                    System.out.println(entries);
                    found = true;
                }
                //Previous Year
                if ((previousYear == currentYearPart) && statusReport.equalsIgnoreCase("previous-Year")) {
                    System.out.println(entries);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No Data Found!");
            }

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}





