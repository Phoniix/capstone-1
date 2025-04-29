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
                    "(6) [CS] Custom Search // Coming Soon\n" +
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
            } else if (userSelection == '6') {
                activityLogger("Opened Search By Anything Screen");
                ResultHelper sba = searchByAnything(scanner);
                if (returner(sba)) {return sba;}
            } else if (userSelection == 'L') {
                activityLogger("Returned To Ledger Menu From Reports Screen");
                ResultHelper ledger = LedgerScreen.ledgerScreen(scanner);
                if (returner(ledger)) {return ledger;}
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

    public static ResultHelper searchByAnything (Scanner scanner) throws  IOException, InterruptedException {
        Scanner userInput = new Scanner(System.in);
        boolean doneSearching = false;
        while (!doneSearching) {
            titleNewLineTop();
            System.out.println("Here you can search by a variety of factors that include:\n" +
                    "Start Date, End Date, Description, Vendor, or Amount.\n" +
                    promptUser());
            System.out.println("(L) Return To Ledger Menu");
            System.out.println("(R) Return to Reports Menu");
            System.out.println("(0) Return to Main Menu");
            System.out.println("(X) Exit");
            lineBottom();
            System.out.print("\n\nEnter:  ");
            String userInputInput = userInput.nextLine().trim().replaceAll("\\s+", " ");
            if (checkIfEmpty(userInputInput)) {thisFieldCantBeEmpty();continue;}
            ResultHelper uii = allowUserToExitOrReturn(userInputInput); if (returner(uii)) {return uii;}

            char uiiChoice = userInputInput.toUpperCase().charAt(0);
            if (uiiChoice == 'R') {
                ResultHelper reports = reportsScreen(userInput);
                if (returner(reports)) {return reports;}
            } else if (uiiChoice == 'L') {
                ResultHelper ledger = LedgerScreen.ledgerScreen(userInput);
                if (returner(ledger)) {return ledger;}
            }


                if (isDate(userInputInput)) {
                    boolean confirmStartVSEnd = false;
                    while (!confirmStartVSEnd) {
                        titleNewLineTop();
                        System.out.println("Date Entered: " + userInputInput);
                        System.out.println("Is this the date you want to start a search from?\n" +
                                "Or Is this the date you want to stop displaying results after?\n" +
                                promptUser() + "\n" +
                                "(S) Start Date\n" +
                                "(E) End Date\n" +
                                "(0) Return To Main Menu\n" +
                                "(X) Exit App");
                        titleLineBottom();
                        System.out.print("\n\nEnter:  ");
                        String userStartOrEndDate = scanner.nextLine().trim().replaceAll("\\s+", "");
                        if (checkIfEmpty(userStartOrEndDate)) {continue;}
                        ResultHelper soed = allowUserToExitOrReturn(userStartOrEndDate); if (returner(soed)) {return soed;}

                        char userFunction = userStartOrEndDate.toUpperCase().charAt(0);

                        if (userFunction == 'S') {
                            ResultHelper sbd =  searchByDate(userInputInput, userFunction, userInput);
                            if (returner(sbd)) {return sbd;}
                        } else if (userFunction == 'E') {
                            ResultHelper sbd = searchByDate(userInputInput, userFunction, userInput);
                            if (returner(sbd)) {return sbd;}
                        } else {System.out.println("Invalid Input. Please choose from listed options");}
                    }
                } else if (isDescription(userInputInput)) {
                    boolean descriptionConfirmed = false;
                    while (!descriptionConfirmed) {
                        System.out.println("Description Entered: " + userInputInput);
                        titleNewLineTop();
                        System.out.println("Did you mean to search for a vendor? (Y) or (N).\n" +
                                "(Y) or (N).\n" +
                                promptUser());
                        titleLineBottom();
                        System.out.print("Enter:  ");
                        String userConfirmInput = userInput.nextLine().trim().replaceAll("\\s+", "");
                        if (checkIfEmpty(userConfirmInput)) {thisFieldCantBeEmpty();continue;}
                        ResultHelper uci = allowUserToExitOrReturn(userConfirmInput); if (returner(uci)) {return uci;}

                        char userConfirm = userConfirmInput.toUpperCase().charAt(0);

                        if (userConfirm == 'Y') {
                            break;
                        } else if (userConfirm == 'N') {
                            ResultHelper sbv = searchByDescription(userInputInput, userInput);
                            descriptionConfirmed = true;
                            if (returner(sbv)) {return sbv;}
                        } else if (userConfirm == '0') {
                            ResultHelper home = homeScreen(userInput);
                            if (returner(home)) {return home;}
                        } else {
                            System.out.println("Invalid Input. Please choose from listed options.");
                        }
                    }
                } else if (isVendor(userInputInput)) {
                    boolean vendorConfirm = false;
                    while (!vendorConfirm) {
                        System.out.println("Vendor Entered: " + userInputInput);
                        titleNewLineTop();
                        System.out.println("Did you mean to search for a description? (Y) or (N).\n" +
                                "(Y) or (N).\n" +
                                promptUser());
                        titleLineBottom();
                        System.out.print("Enter:  ");
                        String userConfirmInput = userInput.nextLine().trim().replaceAll("\\s+", "");
                        if (checkIfEmpty(userConfirmInput)) {thisFieldCantBeEmpty();continue;}
                        ResultHelper uci = allowUserToExitOrReturn(userConfirmInput); if (returner(uci)) {return uci;}

                        char userConfirm = userConfirmInput.toUpperCase().charAt(0);

                        if (userConfirm == 'Y') {
                            break;
                        } else if (userConfirm == 'N') {
                            vendorConfirm = true;
                            ResultHelper sbv = searchByVendor(userInputInput, userInput);
                            if (returner(sbv)) {return sbv;}
                        } else if (userConfirm == '0') {
                            ResultHelper home = homeScreen(userInput);
                            if (returner(home)) {return home;}
                        } else {
                            System.out.println("Invalid Input. Please choose from listed options.");
                        }
                    }
                } else if (isAmount(userInputInput)) {
                    System.out.println("Amount Entered: " + userInputInput);
                    ResultHelper sba =  searchByAmount(userInputInput, userInput);
                    if (returner(sba)) {return sba;}
                } else {
                    System.out.println("...");
                }

        }

        return new ResultHelper('0', true);
    }

    // Back End // ----------------------------------------------------------------------------------------------------
    enum expectedSBAInputs {
        DATE, DESCRIPTION, VENDOR, AMOUNT
    } // TODO: use for menu option next project // -----------------------------------------
    public static boolean isDate (String input) {
        try {
            LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isAmount (String input) {
        return input.matches("-?\\d+(\\.\\d{1,2})?");
    }
    public static boolean isVendor (String input) {
        return !input.isEmpty() && !input.matches("\\d+") && input.split(" ").length <= 2;
    }
    public static boolean isDescription (String input) {
        return !input.isEmpty() && !input.matches("\\d+") && input.split(" ").length >= 2;
    }
    public static ResultHelper searchByDate (String input, char function, Scanner scanner) throws IOException, InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate targetDate = LocalDate.parse(input, formatter);
        if (function == 'E') {
            boolean doneSearching = false;
            while (!doneSearching) {
                targetDate = LocalDate.parse(input, formatter);
                LocalDate endDate = targetDate;
                titleNewLineTop();
                System.out.println("What is your start date?\n" +
                        "(Your searches will stop at your end date [" + endDate + "].\n" +
                        promptUser());
                titleLineBottom();
                System.out.print("Enter:  ");
                String userInput = scanner.nextLine().trim().replaceAll("\\s+", "");
                if (checkIfEmpty(userInput)) {System.out.println("This field can't be empty!"); continue;}
                ResultHelper ui = allowUserToExitOrReturn(userInput); if (returner(ui)) {return ui;}
                if (!isDate(userInput)) {System.out.println("Please use yyyy-MM-dd format.");continue;}
                LocalDate startDate = LocalDate.parse(userInput, formatter);

                boolean found = false;
                for (String line : allTransactions()) {
                    String [] inputParts = line.split("\\|");
                    if (inputParts.length > 2) {

                        LocalDate fileDate = LocalDate.parse(inputParts [0], formatter);

                        if ((fileDate.isAfter(startDate) || fileDate.isEqual(startDate)) &&
                            (fileDate.isBefore(endDate) || fileDate.isEqual(endDate))) {

                            System.out.println(line);
                            lineBottom();
                            found = true;
                        }
                    }
                }
                if (!found) {
                    System.out.println("No Results Found Between [ " + startDate + " ] and [ " + endDate + " ].");
                }
                boolean goNextSearch = false;
                while (!goNextSearch) {
                    titleNewLineTop();
                    System.out.println("Here are your matching searches between [ " + startDate + " ] and [ " + endDate + " ]\n" +
                            "Start a new search here by entering another END DATE,\nor return to reports menu by entering (R).\n" +
                            promptUser());
                    titleLineBottom();
                    System.out.print("Enter:  ");

                    input = scanner.nextLine().trim().replaceAll("\\s+", "");
                    if (checkIfEmpty(input)) {System.out.println("This field can't be empty!"); continue;}
                    ResultHelper ui2 = allowUserToExitOrReturn(input); if (returner(ui2)) {return ui2;}

                    char searchOrExit = input.toUpperCase().charAt(0);

                    if (searchOrExit == 'L') {
                        ResultHelper ledger = LedgerScreen.ledgerScreen(scanner);
                        if (returner(ledger)) {
                            return ledger;
                        }
                    } else if (searchOrExit == '0') {
                        return new ResultHelper('0', true);
                    } else if (searchOrExit == 'R') {
                        ResultHelper reports = reportsScreen(scanner);
                        return new ResultHelper('R', true);
                    } else {
                        if (!isDate(input)) {System.out.println("Please use yyyy-MM-dd format.");continue;}
                        goNextSearch = true;
                    }
                }
            }

        } else if (function == 'S') { /// ---------------------------------------------------------------------------///
            boolean doneSearching = false;
            while (!doneSearching) {
                targetDate = LocalDate.parse(input, formatter);
                LocalDate startDate = targetDate;
                titleNewLineTop();
                System.out.println("What is your end date?\n" +
                        "(Your searches will start at your start date [" + startDate + "].\n" +
                        promptUser());
                String userInput = scanner.nextLine().trim().replaceAll("\\s+", "");
                if (checkIfEmpty(userInput)) {System.out.println("This field can't be empty!"); continue;}
                ResultHelper ui = allowUserToExitOrReturn(userInput); if (returner(ui)) {return ui;}
                if (!isDate(userInput)) {System.out.println("Please use yyyy-MM-dd format.");continue;}
                LocalDate endDate = LocalDate.parse(userInput, formatter);

                boolean found = false;
                for (String line : allTransactions()) {
                    String [] inputParts = line.split("\\|");
                    if (inputParts.length > 2) {

                        LocalDate fileDate = LocalDate.parse(inputParts [0], formatter);

                        if ((fileDate.isAfter(startDate) || fileDate.isEqual(startDate)) &&
                                (fileDate.isBefore(endDate) || fileDate.isEqual(endDate))) {

                            System.out.println(line);
                            lineBottom();
                            found = true;
                        }
                    }
                }
                if (!found) {
                    System.out.println("No Results Found Between [ " + startDate + " ] and [ " + endDate + " ].");
                }

                boolean goNextSearch = false;
                while (!goNextSearch) {
                    titleNewLineTop();
                    System.out.println("Here are your matching searches between [ " + startDate + " ] and [ " + endDate + " ]\n" +
                            "Start a new search here by entering another START DATE,\nor return to reports menu by entering (R).\n" +
                            promptUser());

                    input = scanner.nextLine().trim().replaceAll("\\s+", "");
                    if (checkIfEmpty(input)) {System.out.println("This field can't be empty!"); continue;}
                    ResultHelper ui2 = allowUserToExitOrReturn(input); if (returner(ui2)) {return ui2;}

                    char searchOrExit = input.toUpperCase().charAt(0);

                    if (searchOrExit == 'L') {
                        ResultHelper ledger = LedgerScreen.ledgerScreen(scanner);
                        if (returner(ledger)) {
                            return ledger;
                        }
                    } else if (searchOrExit == '0') {
                        return new ResultHelper('0', true);
                    } else if (searchOrExit == 'R') {
                        ResultHelper reports = reportsScreen(scanner);
                        return new ResultHelper('R', true);
                    } else {
                        if (!isDate(input)) {System.out.println("Please use yyyy-MM-dd format.");continue;}
                        goNextSearch = true;
                    }
                }
            }
        }

        return new ResultHelper('0', true);
    }
    public static ResultHelper searchByDescription (String input, Scanner scanner) throws IOException {
        for (String line : allTransactions()) {
            String [] lineParts =  line.split("\\|");
            if (lineParts.length > 3) {
                String fileDescription = lineParts [2];
                if (fileDescription.toLowerCase().contains(input.toLowerCase())) {
                    System.out.println(line);
                    lineBottom();
                }
            }
        }

        return new ResultHelper('0', true);
    }
    public static ResultHelper searchByVendor (String input, Scanner scanner) {

        return new ResultHelper('0', true);
    }
    public static ResultHelper searchByAmount (String input, Scanner scanner) {

        return new ResultHelper('0', true);
    }

}
