package com.pluralsight;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static com.pluralsight.AccountingLedger.*;
import static com.pluralsight.CustomSearch.customSearch;

public class ReportsScreen {

    public static ResultHelper reportsScreen (Scanner scanner) throws IOException, InterruptedException {
        boolean keepGoing = true;
        activityLogger("Opened Reports Screen");
        while (keepGoing) {
            titleNewLineTop();
            System.out.println("Welcome To Your Account Ledger Reporting\n" +
                    "What service do you need?\n\n" +
                    "(1) [MTD] Month To Date Transactions\n" +
                    "(2) [PM] Previous Month Transactions\n" +
                    "(3) [YTD] Year To Date Transactions\n" +
                    "(4) [PY] Previous Year Transactions\n" +
                    "(5) [SBV] Search By Vendor\n" +
                    "(6) Custom Search\n\n" +
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
                activityLogger("Displayed Month To Date Transactions");
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
            } else if (userSelection == '6') {
                activityLogger("Opened Custom Search Screen");
                ResultHelper cs = customSearch(scanner);
                if (returner(cs)) {return cs;}
            } else if (userSelection == 'L') {
                activityLogger("Returned To Ledger Menu From Reports Screen");
                ResultHelper ledger = LedgerScreen.ledgerScreen(scanner);
                if (returner(ledger)) {return ledger;}
            } else {
                System.out.println("Invalid Input. Please Choose From Listed Options");
            }
        }
        return null; //TODO: Use for next project
    }

    // Functions // ---------------------------------------------------------------------------------------------------
    public static ResultHelper monthToDate (Scanner scanner) throws IOException, InterruptedException {
        LocalDate today = LocalDate.now();
        LocalDate targetDate = getTargetDate(today);
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
        notFound(found);

        Scanner userInput = new Scanner(System.in);
        ResultHelper changeScreen = screenChange(userInput, "Here are all of your (MTD) Month to Date transactions!");
        if (returner(changeScreen)) {return changeScreen;}

        return null;
    }

    public static ResultHelper previousMonth (Scanner scanner) throws IOException, InterruptedException {
        LocalDate today = LocalDate.now();
        LocalDate targetDate = getTargetDatePreviousMonth(today);
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
        notFound(found);

        Scanner userInput = new Scanner(System.in);
        ResultHelper changeScreen = screenChange(userInput, "Here are all of your (PM) Previous Month's transactions!");
        if (returner(changeScreen)) {return changeScreen;}

        return null;
    }

    public static ResultHelper yearToDate (Scanner scanner) throws IOException, InterruptedException {
        LocalDate today = LocalDate.now();
        LocalDate targetDate = getTargetDate(today);
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
        notFound(found);

        Scanner userInput = new Scanner(System.in);
        ResultHelper changeScreen = screenChange(userInput, "Here are all of your (YTD) Year to Date transactions!");
        if (returner(changeScreen)) {return changeScreen;}

        return null;
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
        notFound(found);

        Scanner userInput = new Scanner(System.in);
        ResultHelper changeScreen = screenChange(userInput, "Here are all of your (PY) Previous Year's transactions!");
        if (returner(changeScreen)) {return changeScreen;}

        return null;
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

            enteredSomething = true;
        }
        ResultHelper uii = allowUserToExitOrReturn(userInputInput);if (returner(uii)) {return uii;}
        if (userInputInput.equalsIgnoreCase("P")) {userInputInput = "Personal";}
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
            notFound(found);

            ResultHelper changeScreen = screenChange(userInput, "Here are all of your searches matching [" + userInputInput + "].");
            if (returner(changeScreen)) {return changeScreen;}
        }

        return null;
    }

    // Back End // ----------------------------------------------------------------------------------------------------
//    enum expectedSBAInputs {
//        DATE, DESCRIPTION, VENDOR, AMOUNT
//    } // TODO: use for menu option next project // -----------------------------------------

    private static LocalDate getTargetDatePreviousMonth(LocalDate today) {
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
        return targetDate;
    }
    public static LocalDate getTargetDate (LocalDate today) {
        int todayYear = today.getYear();
        int todayMonth = today.getMonthValue();
        int todayDay = today.getDayOfMonth();
        LocalDate targetDate = LocalDate.of(todayYear, todayMonth, todayDay);
        return targetDate;
    } //MTD & YTD Can use // ------------------------------
    public static void notFound (boolean found) {
        if (!found) {
            newLineTop();
            System.out.println("No Results Found.");
            lineBottom();
        }
    }


}