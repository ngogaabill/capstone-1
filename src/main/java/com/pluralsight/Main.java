package com.pluralsight;

import java.io.*;
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
        System.out.println("==========WELCOME TO LEDGER APPLICATION==========");
        homepage();
    }

    /**
     * Homepage() gucci
     *
     */

    private static void homepage() {
        boolean exit = false;
        while (!exit) {
            System.out.println("D) Add Deposit\n" +
                    "P) Make Payment (Debit)\n" +
                    "L) Ledger\n" +
                    "E) Exit\n" +
                    "Your Choice:");
            String input = scanner.nextLine();
            char userChoice = input.charAt(0);
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
                    exit = true;

            }

        }

    }

    /**
     * Adds deposits to the arrayList
     *
     */
    private static void addDeposit() {
        getInfoFromUser(debitTransactions, false);

    }

    /**
     * Add Payments to the list
     */
    private static void makePayment() {
        getInfoFromUser(paymentTransactions, true);
    }
    /**
     * ledger Manuel
     */
    private static void ledger() {
        boolean returnHome = false;

        while (!returnHome) {
            System.out.println("A)Display all entries\n" +
                    "D) Deposits\n" +
                    "account\n" +
                    "P) Payments\n" +
                    "R) Reports\n" +
                    "H) Home\n" +
                    "Your Choice:");
            String input = scanner.nextLine();
            char userChoice = input.charAt(0);
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

    private static void report() {

    }

    /**
     * Print Debits in the debit List
     */
    private static void deposits() {
        System.out.println("Your Deposit List:");
        for (int i = 0; i < debitTransactions.size(); i++) {
            System.out.println(debitTransactions.get(i).toString());
        }
    }

    /**
     * Print payments in the payment List
     */
    private static void payments() {
        System.out.println("Your Payment List:");
        for (int i = 0; i < paymentTransactions.size(); i++) {
            System.out.println(paymentTransactions.get(i).toString());
        }
    }

    /**
     * Print all data reading them from the file
     */
    private static void allEntries() {
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
     * Get Info from user based on the transactions save to respective ArrayLists
     * @param transactionsType
     * @param isPayment
     */
    public static void getInfoFromUser(ArrayList<Transactions> transactionsType, boolean isPayment) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss");
        String timeDateStamp = currentTime.format(formatter);
        String[] timeDateParts = timeDateStamp.split("\\|");
        String date = timeDateParts[0];
        String time = timeDateParts[1];
        System.out.println("Enter payment Description:");
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
}




