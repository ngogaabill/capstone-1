package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transactions> debitTransactions = new ArrayList<>();//hold the deposits +
    static ArrayList<Transactions> paymentTransactions = new ArrayList<>();//hold the payments
    static String fileName = "src/main/resources/transactions.csv";

    public static void main(String[] args) {
        System.out.println("========== WELCOME TO BILL'S LEDGER APP ==========");
        System.out.println("|                     :)                          |");
        System.out.println("===================================================");

        homepage();
    }

    /**
     * Homepage() gucci
     *
     */

    public static void homepage() {
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
        getInfoFromUserToFile(debitTransactions, false);

    }

    /**
     * Add Payments to the list
     */
    public static void makePayment() {
        getInfoFromUserToFile(paymentTransactions, true);
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
     * @param transactionsType
     * @param isPayment
     */
    public static void getInfoFromUserToFile(ArrayList<Transactions> transactionsType, boolean isPayment) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss");
        String timeDateStamp = currentTime.format(formatter);
        String[] timeDateParts = timeDateStamp.split("\\|");
        String date = timeDateParts[0];
        String time = timeDateParts[1];
        System.out.println("Enter Debit/Payment Description:");
        String description = scanner.nextLine();
        System.out.println("Vendor:");
        String vendor = scanner.nextLine();
        System.out.println("Amount:");
        double amount = scanner.nextDouble();

        if (isPayment) { //if so turn into neg amount
            amount *= -1;
        }

        scanner.nextLine();//consume the newline
        Transactions transactions = new Transactions(date, time, description, vendor, amount);
        transactionsType.add(transactions);

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
     * * Print Debits in the debit List
     */
    public static void deposits() {
        System.out.println("Your Deposit List:");
        for (int i = 0; i < debitTransactions.size(); i++) {
            System.out.println(debitTransactions.get(i).toString());
        }
    }

    /**
     * Print payments in the payment List
     */
    public static void payments() {
        System.out.println("Your Payment List:");
        for (int i = 0; i < paymentTransactions.size(); i++) {
            System.out.println(paymentTransactions.get(i).toString());
        }
    }

    /**
     * * Print all
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

    }

    public static void previousMonth() {
    }

    /**
     * Search Vendor by name and print if match is found
     */
    public static void searchByVendor() {
        String filedata;
        System.out.println("Enter Vendor Name To Search For:");
        scanner.nextLine();
        String vendorName = scanner.nextLine();
        boolean matchFound = false;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            while ((filedata = bufferedReader.readLine()) != null) {
                String[] fileparts = filedata.split("\\|");
                if (fileparts[0].equalsIgnoreCase("date")) {
                    continue;//skip the header
                }
                if (vendorName.equalsIgnoreCase(fileparts[3])) {
                    System.out.println(filedata);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                System.out.println("Vendor Not found");
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void previousYear() {
    }

    public static void monthToDate() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String entries;
            System.out.println("Audit For Current Month:");
            LocalDate currentDate = LocalDate.now();
            int currentMonth = currentDate.getMonthValue();
            boolean isCurrentMonth = false;

            while ((entries = bufferedReader.readLine()) != null) {
                String[] parts = entries.split("\\|");

                String date = parts[0];
                if (parts[0].equalsIgnoreCase("date")) {
                    continue;//skip the header
                }

                String[] dateparts = date.split("-");
                int currentMonthPart = Integer.parseInt(dateparts[1]);
                if (currentMonth == currentMonthPart) {
                    System.out.println(entries);
                    isCurrentMonth = true;
                }
            }
            if (!isCurrentMonth) {
                System.out.println("There's no record for this month");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}




