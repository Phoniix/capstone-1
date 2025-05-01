package com.pluralsight;


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static com.pluralsight.AccountingLedger.*;
import static com.pluralsight.ReportsScreen.*;

public class CustomSearch {

    public static ResultHelper customSearch (Scanner scanner) throws IOException, InterruptedException {
        boolean keepGoing = true;
        activityLogger("Opened Custom Search Function");
        while (keepGoing) {
            titleNewLineTop();
            System.out.println("Custom Search\n" +
                    "What filters do you need? Enter All Selections at the same time separated by commas\n" +
                    "(For Example: 1,2,3,4,5)\n\n" +
                    "(1) Start Date\n" +
                    "(2) End Date\n" +
                    "(3) Description\n" +
                    "(4) Vendor\n" +
                    "(5) Transaction amount\n\n" +
                    "(R) Return To Reports Screen\n" +
                    "(L) Return To Ledger Screen\n" +
                    "(0) Return To Main Menu\n" +
                    "(X) Exit Program");
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            String userSelectionInput = scanner.nextLine().trim().replaceAll("\\s+", "");
            if (checkIfEmpty(userSelectionInput)) {thisFieldCantBeEmpty();continue;}
            ResultHelper usi = allowUserToExitOrReturn(userSelectionInput); if (returner(usi)) {return usi;}

            char userSelection = userSelectionInput.toUpperCase().charAt(0);
            if (userSelection == 'L') {
                activityLogger("Returned To Ledger Menu From Custom Search Screen");
                ResultHelper ledger = LedgerScreen.ledgerScreen(scanner);
                if (returner(ledger)) {return ledger;}
            } if (userSelection == 'R') {
                ResultHelper reports = reportsScreen(scanner);
                if (returner(reports)) {return reports;}
            }

            String filters = combineFilters(userSelectionInput);
            LocalDate startDate = null;
            LocalDate endDate = null;
            String description = "";
            String vendor = "";
            double transactionAMNT = 0;

            if (filters.contains("1")) {
                startDate = promptForDate(scanner, "Start Date", "after this date");
            } if (filters.contains("2")) {
                endDate = promptForDate(scanner, "End Date", "before this date");
            } if (filters.contains("3")) {
                description = promptForString(scanner, "Description", "including this description");
            } if (filters.contains("4")) {
                vendor = promptForString(scanner, "Vendor", "including this vendor name");
            } if (filters.contains("5")) {
               transactionAMNT = promptForDouble(scanner, "Amount", "that has this exact amount");
            }

            activityLogger("User requested transactions with: " +
                    (!dateLog(startDate).isEmpty() ? "[" + dateLog(startDate) + "], " : "") +
                    (!dateLog(endDate).isEmpty() ? "[" + dateLog(endDate) + "], " : "")  +
                    (!description.isEmpty() ? "[" + description + "], " : "") +
                    (!vendor.isEmpty() ? "[" + vendor + "], ": "") +
                    (!doubleLog(transactionAMNT).isEmpty() ? "[" + transactionAMNT + "]" : "") +
                    " as parameter(s).");

            boolean found = false;
            for (String line : allTransactions()) {
                boolean hideTransaction = true;

                String [] lineParts = line.split("\\|");
                if (lineParts.length >= 5) {
                    LocalDate fileDate = convertStringToDate(lineParts[0]);
                    String fileDescription = lineParts[2];
                    String fileVendor = lineParts[3];
                    double fileAMNT = Double.parseDouble(lineParts[4]);
                    double targetAMNT = Math.abs(transactionAMNT) - Math.abs(fileAMNT);
                    if (startDate != null && fileDate.isBefore(startDate)) {
                        hideTransaction = false;
                    } if (endDate != null && fileDate.isAfter(endDate)) {
                        hideTransaction = false;
                    } if (!description.isEmpty() && !fileDescription.toLowerCase().contains(description.toLowerCase())) {
                        hideTransaction = false;
                    } if (!vendor.isEmpty() && !fileVendor.toLowerCase().contains(vendor.toLowerCase())) {
                        hideTransaction = false;
                    } if (transactionAMNT != 0 && fileAMNT == transactionAMNT) {
                        if (fileAMNT < 0)
                        hideTransaction = false;
                    } if (hideTransaction) {
                        System.out.println(line);
                        lineBottom();
                        found = true;
                    }
                }
            }
            if (!found) {
                newLineTop();
                System.out.println("No results found.");
                titleNewLineTop();
            }

            boolean searchAgainConfirm = false;
            while (!searchAgainConfirm) {
                titleNewLineTop();
                System.out.println("Would you like to search again? (Y) or (N)\n" +
                        promptUser());
                titleLineBottom();
                System.out.print("Enter:  ");
                String searchAgain = scanner.nextLine().trim().replaceAll("\\s+", "");
                ResultHelper sa = allowUserToExitOrReturn(searchAgain); if (returner(sa)) {return sa;}
                char userConfirm = searchAgain.toUpperCase().charAt(0);

                if (userConfirm == 'Y') {
                    System.out.println("Setting up next search...");
                    timer(750);
                    searchAgainConfirm = true;
                    continue;
                } else if (userConfirm == 'N') {
                    screenChange(scanner, "Thank you for using Account Ledger Custom Search");
                    break;
                } else {
                    System.out.println("Invalid Input. Please choose from listed options.");
                }
            }
        }
        return null; //TODO: Use for next project
    }

// Back End // --------------------------------------------------------------------------------------------------------
    public static String [] separateFilters (String userSelectionInput) {
        String [] filters = {};
        try {
            filters = userSelectionInput.split(",\\s+");
        } catch (Exception ignored) {}
        return filters;
    }
    public static String combineFilters (String userSelectionInput) {
        String filters = "";
        try {
            String [] getFilters = separateFilters(userSelectionInput); // Separates the filters
            filters = String.join("", getFilters); // Rejoins the filters in reusable string
        } catch (Exception ignored) {}
        return filters;
    }
    public static String getInfo (Scanner scanner, String message, String action) {
        boolean somethingEntered = false;
        String userInput = "";
        while (!somethingEntered) {
            titleNewLineTop();
            System.out.println("What is your " + message + "?\n" +
                    "Transactions " + action + " will be displayed.");
            titleLineBottom();
            System.out.print("Enter:  ");
            userInput = scanner.nextLine().trim().replaceAll("\\s+", "");
            if (checkIfEmpty(userInput)) {thisFieldCantBeEmpty();continue;}
            somethingEntered = true;
        }
        return userInput;
    }
    public static boolean isDate (String dateInput) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(dateInput, formatter);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid Date. Please use 'yyyy-MM-dd' format."); return false;
        }

    }
    public static boolean isDouble (String doubleInput) {
        try {
            Double.parseDouble(doubleInput);
            return true;
        } catch (Exception e) {
            System.out.println("Please enter a valid number.");
            return false;
        }
    }
    public static LocalDate convertStringToDate (String dateInput) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = null;
        try {
            date = LocalDate.parse(dateInput, formatter);
        } catch (Exception e) {
            System.out.println("Invalid Date. Please use 'yyyy-MM-dd' format.");
        }
        return date;
    }
    public static String dateLog (LocalDate dateInput) {
        String date = dateInput != null ? dateInput.toString() : "";
        return date;
    }
    public static String stringLog (String input) {
        String string = !input.isEmpty() ?  input : "";
        return string;
    }
    public static String doubleLog (double input) {
        String theDouble = input != 0 ? String.valueOf(input) : "";
        return theDouble;
    }
    public static LocalDate promptForDate (Scanner scanner, String message, String action) {
        boolean dateEntered = false;
        String dateInput = null;
        while (!dateEntered) {
            dateInput = getInfo(scanner, message, action);
            if (checkIfEmpty(dateInput)) {thisFieldCantBeEmpty();continue;}
            if (isDate(dateInput) == false) {continue;}
            dateEntered = true;
        }
        return convertStringToDate(dateInput);
    }
    public static String promptForString (Scanner scanner, String message, String action) {
        boolean somethingEntered = false;
        String input = null;
        while (!somethingEntered) {
            input = getInfo(scanner, message, action );
            if (checkIfEmpty(input)) {continue;}
            somethingEntered = true;
        }
        return  autoCapitalizeFirstLetter(input);
    }
    public static double promptForDouble (Scanner scanner, String message, String action) {
        boolean transactionAMNTEntered = false;
        String transactionAMNTInput = null;
        while (!transactionAMNTEntered) {
            transactionAMNTInput = getInfo(scanner, message, action);
            if (checkIfEmpty(transactionAMNTInput)) {thisFieldCantBeEmpty();continue;}
            if (!isDouble(transactionAMNTInput)) {continue;}
            transactionAMNTEntered = true;
        }
        return Double.parseDouble(transactionAMNTInput);
    }
}
