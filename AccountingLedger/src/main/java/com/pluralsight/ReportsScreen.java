package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.pluralsight.AccountingLedger.*;

public class ReportsScreen {

    public static ResultHelper reportsScreen (Scanner scanner) throws IOException, InterruptedException {
        boolean keepGoing = true;
        while (keepGoing) {
            titleNewLineTop();
            System.out.println("Welcome To Your Account Ledger Reporting\n" +
                    "What service do you need?\n\n" +
                    "(1) [MTD] Month To Date Transactions\n" +
                    "(2) [PM] Previous Month Transactions\n" +
                    "(3) [YTD] Year To Date Transactions\n" +
                    "(4) [PY] Previous Year Transactions\n" +
                    "(5) [SBV] Search By Vendor\n" +
                    "(L) Return To Ledger Screen\n" +
                    "(0) Return To Main Menu\n" +
                    "(X) Exit Program");
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            String userSelectionInput = scanner.nextLine().trim().replaceAll("\\s+", "");
            if (checkIfEmpty(userSelectionInput)) {thisFieldCantBeEmpty();continue;}
            ResultHelper usi = allowUserToExitOrReturn(userSelectionInput); if (returner(usi)) {return usi;}
            char userSelection = userSelectionInput.toUpperCase().charAt(0);

            if (userSelection == '1') {
                activityLogger("Displayed Month TO Date Transactions");
                ResultHelper mtd = monthToDate(scanner);
               if (returner(mtd)) {return mtd;}
            } else if (userSelection == '2') {
                activityLogger("Displayed Previous Month Transactions");
                ResultHelper prevMonth = previousMonth(scanner);
                if (returner(prevMonth)) {return prevMonth;}
            } else if (userSelection == '3') {
                activityLogger("Displayed Year To Date Transactions");
                ResultHelper ytd = yearToDate(scanner);
                if (returner(ytd)) {return ytd;}
            } else if (userSelection == '4') {
                activityLogger("Displayed Previous Year Transactions");
                ResultHelper prevYear = previousYear(scanner);
                if (returner(prevYear)) {return prevYear;}
            } else if (userSelection == '5') {
                activityLogger("Opened Search By Vendor Screen");
                ResultHelper sbv = searchByVendor(scanner);
                if (returner(sbv)) {return sbv;}
            }  else if (userSelection == 'L') {
                activityLogger("Returned To Ledger Menu From Reports Screen");
                ResultHelper ledger = LedgerScreen.ledgerScreen(scanner);
                if (returner(ledger)) {return ledger;}
            } else {
                System.out.println("Invalid Input. Please Choose From Listed Options");
            }
        }
        return null;
    }

    // Functions // ---------------------------------------------------------------------------------------------------
    public static ResultHelper monthToDate (Scanner scanner) throws IOException, InterruptedException {
        LocalDate today = LocalDate.now();
        int todayYear = today.getYear();
        int todayMonth = today.getMonthValue();
        int todayDay = today.getDayOfMonth();
        LocalDate targetDate = LocalDate.of(todayYear, todayMonth, todayDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("\n");
        boolean found = false;
        for (String line : allTransactions()) {
            String [] lineParts = line.split("\\|");
            if (lineParts.length > 1) {
                String getTransactionDate = lineParts[0];
                LocalDate transactionDate = LocalDate.parse(getTransactionDate, formatter);
                if (transactionDate.getMonth() == targetDate.getMonth() && transactionDate.getYear() == targetDate.getYear()) {
                    timer(110);
                    System.out.println(line);
                    lineBottom();
                    found = true;
                }
            }
        }
        if (!found) {
            newLineTop();
            System.out.println("No Results Found.");
            lineBottom();
        }

        boolean goToLedger = false;
        Scanner userInput = new Scanner(System.in);
        while (!goToLedger) {
            titleNewLineTop();
            System.out.println("Here are your (MTD) month to date transactions!");
            System.out.println(promptUser());
            System.out.println("(R) Go Back To Reports Menu");
            System.out.println("(L) Go Back To Ledger Menu");
            System.out.println("(0) Go Back To Main Menu");
            System.out.println("(X) Exit");
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            String userChoiceInput = userInput.nextLine().trim().replaceAll("\\s+", "");
            if (checkIfEmpty(userChoiceInput)) {thisFieldCantBeEmpty();continue;}
            ResultHelper uci = allowUserToExitOrReturn(userChoiceInput); if (returner(uci)) {return uci;}
            char userChoice = userChoiceInput.toUpperCase().charAt(0);

            if (userChoice == 'L') {
                ResultHelper ledger = LedgerScreen.ledgerScreen(userInput);
                if (returner(ledger)) {return ledger;}
            } else if (userChoice == '0') {
                return new ResultHelper('0', true);
            } else if (userChoice == 'R') {
                return new ResultHelper('R', true);
            } else {
                System.out.println("\nInvalid Input. Please choose from listed options.");
            }
        }

        return new ResultHelper('0', true);
    }

    public static ResultHelper previousMonth (Scanner scanner) throws IOException, InterruptedException {
        LocalDate today = LocalDate.now();
        int todayYear = today.getYear();
        int todayMonth = today.getMonthValue();
        int todayDay = today.getDayOfMonth();
        int targetMonth = todayMonth - 1;
        int targetYear = todayYear;
        LocalDate targetDate = null;
        if (targetMonth == 0) { // Handles The Case of Previous Month Being December In January // --------------------
            targetMonth = 12;
            targetYear -= 1;
        }
        targetDate = LocalDate.of(targetYear, targetMonth, todayDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("\n");
        boolean found = false;
        for (String line : allTransactions()) {
            String [] lineParts = line.split("\\|");
            if (lineParts.length > 1) {
                String getTransactionDate = lineParts[0];
                LocalDate transactionDate = LocalDate.parse(getTransactionDate, formatter);
                if (transactionDate.getMonth() == targetDate.getMonth() && transactionDate.getYear() == targetDate.getYear()) {
                    timer(110);
                    System.out.println(line);
                    lineBottom();
                    found = true;
                }
            }
        }
        if (!found) {
            newLineTop();
            System.out.println("No Results Found.");
            lineBottom();
        }

        boolean goToLedger = false;
        Scanner userInput = new Scanner(System.in);
        while (!goToLedger) {
            titleNewLineTop();
            System.out.println("Here are your (PV) previous month transactions!");
            System.out.println(promptUser());
            System.out.println("(R) Go Back To Reports Menu");
            System.out.println("(L) Go Back To Ledger Menu");
            System.out.println("(0) Go Back To Main Menu");
            System.out.println("(X) Exit");
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            String userChoiceInput = userInput.nextLine().trim().replaceAll("\\s+", "");
            if (checkIfEmpty(userChoiceInput)) {thisFieldCantBeEmpty();continue;}
            ResultHelper uci = allowUserToExitOrReturn(userChoiceInput); if (returner(uci)) {return uci;}
            char userChoice = userChoiceInput.toUpperCase().charAt(0);

            if (userChoice == 'L') {
                ResultHelper ledger = LedgerScreen.ledgerScreen(userInput);
                if (returner(ledger)) {return ledger;}
            } else if (userChoice == '0') {
                return new ResultHelper('0', true);
            } else if (userChoice == 'R') {
                return new ResultHelper('R', true);
            } else {
                System.out.println("\nInvalid Input. Please choose from listed options.");
            }
        }

        return new ResultHelper('0', true);
    }

    public static ResultHelper yearToDate (Scanner scanner) throws IOException, InterruptedException {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        int todayYear = today.getYear();
        int todayMonth = today.getMonthValue();
        int todayDay = today.getDayOfMonth();
        LocalDate targetDate = LocalDate.of(todayYear, todayMonth, todayDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("\n");
        boolean found = false;
        for (String line : allTransactions()) {
            String [] lineParts = line.split("\\|");
            if (lineParts.length > 1) {
                String getTransactionDate = lineParts[0];
                LocalDate transactionDate = LocalDate.parse(getTransactionDate, formatter);
                if (transactionDate.getYear() == targetDate.getYear()) {
                    timer(110);
                    System.out.println(line);
                    lineBottom();
                    found = true;
                }
            }
        }
        if (!found) {
            newLineTop();
            System.out.println("No Results Found.");
            lineBottom();
        }

        boolean goToLedger = false;
        Scanner userInput = new Scanner(System.in);
        while (!goToLedger) {
            titleNewLineTop();
            System.out.println("Here are your (YTD) year to date transactions!");
            System.out.println(promptUser());
            System.out.println("(R) Go Back To Reports Menu");
            System.out.println("(L) Go Back To Ledger Menu");
            System.out.println("(0) Go Back To Main Menu");
            System.out.println("(X) Exit");
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            String userChoiceInput = userInput.nextLine().trim().replaceAll("\\s+", "");
            if (checkIfEmpty(userChoiceInput)) {thisFieldCantBeEmpty();continue;}
            ResultHelper uci = allowUserToExitOrReturn(userChoiceInput); if (returner(uci)) {return uci;}
            char userChoice = userChoiceInput.toUpperCase().charAt(0);

            if (userChoice == 'L') {
                ResultHelper ledger = LedgerScreen.ledgerScreen(userInput);
                if (returner(ledger)) {return ledger;}
            } else if (userChoice == '0') {
                return new ResultHelper('0', true);
            } else if (userChoice == 'R') {
                return new ResultHelper('R', true);
            } else {
                System.out.println("\nInvalid Input. Please choose from listed options.");
            }
        }

        return new ResultHelper('0', true);
    }

    public static ResultHelper previousYear (Scanner scanner) throws IOException, InterruptedException {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        int todayYear = today.getYear();
        int todayMonth = today.getMonthValue();
        int todayDay = today.getDayOfMonth();
        LocalDate targetDate = LocalDate.of(todayYear - 1, todayMonth, todayDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("\n");
        boolean found = false;
        for  (String line: allTransactions()) {
            String [] lineParts = line.split("\\|");
            if (lineParts.length > 1) {
                String getTransactionDate = lineParts[0];
                LocalDate transactionDate = LocalDate.parse(getTransactionDate, formatter);
                if (transactionDate.getYear() == targetDate.getYear()) {
                    timer(110);
                    System.out.println(line);
                    lineBottom();
                    found = true;
                }
            }
        }
        if (!found) {
            newLineTop();
            System.out.println("No Results Found.");
            lineBottom();
        }

        boolean goToLedger = false;
        Scanner userInput = new Scanner(System.in);
        while (!goToLedger) {
            titleNewLineTop();
            System.out.println("Here are your (PY) previous year transactions!");
            System.out.println(promptUser());
            System.out.println("(R) Go Back To Reports Menu");
            System.out.println("(L) Go Back To Ledger Menu");
            System.out.println("(0) Go Back To Main Menu");
            System.out.println("(X) Exit");
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            String userChoiceInput = userInput.nextLine().trim().replaceAll("\\s+", "");
            if (checkIfEmpty(userChoiceInput)) {thisFieldCantBeEmpty();continue;}
            ResultHelper uci = allowUserToExitOrReturn(userChoiceInput); if (returner(uci)) {return uci;}
            char userChoice = userChoiceInput.toUpperCase().charAt(0);

            if (userChoice == 'L') {
                ResultHelper ledger = LedgerScreen.ledgerScreen(userInput);
                if (returner(ledger)) {return ledger;}
            } else if (userChoice == '0') {
                return new ResultHelper('0', true);
            } else if (userChoice == 'R') {
                return new ResultHelper('R', true);
            } else {
                System.out.println("\nInvalid Input. Please choose from listed options.");
            }
        }

        return new ResultHelper('0', true);
    }

    public static ResultHelper searchByVendor (Scanner scanner) throws IOException, InterruptedException {
        Scanner userInput = new Scanner(System.in);
        String userInputInput = "";
        boolean enteredSomething = false;
        while (!enteredSomething) {

            titleNewLineTop();
            System.out.println("Here you can search by vendor name.\n" +
                    "If you are searching for personal deposits or withdraws you can enter (P).\n" +
                    promptUser());
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            userInputInput = userInput.nextLine().trim().replaceAll("\\s+", " ");
            activityLogger("User Searched: " + userInputInput);
            if (checkIfEmpty(userInputInput)) {thisFieldCantBeEmpty();}
            ResultHelper uii = allowUserToExitOrReturn(userInputInput);if (returner(uii)) {return uii;}
            if (userInputInput.equalsIgnoreCase("P")) {userInputInput = "Personal";}
            enteredSomething = true;
        }

        boolean doneSearching = false;
        boolean found = false;
        while (!doneSearching) {
            for (String line : allTransactions()) {
                String[] lineParts = line.split("\\|");
                if (lineParts.length > 3) {
                    String vendor = lineParts[3];
                    if (vendor.toLowerCase().contains(userInputInput.toLowerCase())) {
                        timer(110);
                        System.out.println(line);
                        lineBottom();
                        found = true;
                    }
                }
            }
            if (!found) {
                newLineTop();
                System.out.println("No Results Found For " + userInputInput);
                lineBottom();
            }

            boolean goToLedger = false;
            while (!goToLedger) {
                titleNewLineTop();
                System.out.println("Here are transactions matching your search!");
                System.out.println("You can also start another search here.");
                System.out.println(promptUser());
                System.out.println("(R) Go Back To Reports Menu");
                System.out.println("(L) Go Back To Ledger Menu");
                System.out.println("(0) Go Back To Main Menu");
                System.out.println("(X) Exit");
                titleLineBottom();
                System.out.print("\n\nEnter:  ");
                String userChoiceInput = userInput.nextLine().trim().replaceAll("\\s+", "");
                if (checkIfEmpty(userChoiceInput)) {thisFieldCantBeEmpty();continue;}
                ResultHelper uci = allowUserToExitOrReturn(userChoiceInput);
                if (returner(uci)) {return uci;}

                char userChoice = userChoiceInput.toUpperCase().charAt(0);

                if (userChoice == 'L') {
                    ResultHelper ledger = LedgerScreen.ledgerScreen(userInput);
                    if (returner(ledger)) {
                        return ledger;
                    }
                } else if (userChoice == '0') {
                    return new ResultHelper('0', true);
                } else if (userChoice == 'R') {
                    return new ResultHelper('R', true);
                } else {
                    userInputInput = userChoiceInput;
                    goToLedger = true;
                }
            }
        }

        return new ResultHelper('0', true);
    }

    // Back End // ----------------------------------------------------------------------------------------------------
//    enum expectedSBAInputs {
//        DATE, DESCRIPTION, VENDOR, AMOUNT
//    } // TODO: use for menu option next project // -----------------------------------------


}
