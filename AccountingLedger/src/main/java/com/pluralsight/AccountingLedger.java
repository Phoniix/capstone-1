package com.pluralsight;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class AccountingLedger {

    public static void main(String[] args) throws InterruptedException, NumberFormatException, IOException {
        activityLogger("App Launched");
        allTransactions();
        char function = 'z';
        Scanner scanner = new Scanner(System.in);
        ResultHelper welcome = welcomeScreen(scanner); function = welcome.getFunction();
        boolean keepGoing = true;
        while (keepGoing) {
            if (function == '0') {
                activityLogger("Returned To Main Menu");
                ResultHelper home;
                do {
                    home = homeScreen(scanner);
                    function = home.getFunction();
                    if (function == 'X') {
                        break;
                    }
                } while (function == '0');
            }
            //------------------------------------------//
                if (function == 'D') {
                    activityLogger("Opened Deposit Menu");
                    ResultHelper makeDeposit = makeDeposit(scanner);
                    if (programQuitter(makeDeposit)) {function = makeDeposit.getFunction();}
                    if (!programQuitter(makeDeposit)) {function = makeDeposit.getFunction();}
                } else if (function == 'P') {
                    activityLogger("Opened Payment Menu");
                    ResultHelper makePayment = makePayment(scanner);
                    if (programQuitter(makePayment)) {function = makePayment.getFunction();}
                    if (!programQuitter(makePayment)) {function = makePayment.getFunction();}
                } else if (function == 'L') {
                    activityLogger("Opened Account Ledger Screen");
                    while (function == 'L') {
                        ResultHelper ledger = LedgerScreen.ledgerScreen(scanner);
                        if (programQuitter(ledger)) {function = ledger.getFunction();}
                        if (!programQuitter(ledger)) {function = ledger.getFunction();}
                    }
                } else if (function == 'R') {
                    activityLogger("Opened Reports Screen");
                    ResultHelper reports = ReportsScreen.reportsScreen(scanner);
                    if (programQuitter(reports)) {function = reports.getFunction();}
                    if (!programQuitter(reports)) {function = reports.getFunction();}
                } else if (function == 'X') {
                    exitSequence();
                } else {
                    System.out.println("\nInvalid Input. CHECK");
                }
            if (function == 'X') {
                activityLogger("App Close");
                exitSequence();
                keepGoing = false;
            }

        } // KeepGoing Loop End // ------------------------------------------------------------------------------------


    } // Main End // --------------------------------------------------------------------------------------------------

    // Welcome and Exit Sequences // ----------------------------------------------------------------------------------
    public static ResultHelper welcomeScreen (Scanner scanner) {
        char userSelection = '0';
        boolean keepGoing = true;
        while (keepGoing) {
            titleNewLineTop();
            System.out.println("Welcome User! Use Account Ledger App to monitor and make transactions!\n" +
                    "What Service do you need today?\n\n" +
                    "(D) Make Deposit\n" +
                    "(P) Make Payment\n" +
                    "(L) Open Ledger to View & Manage\n" +
                    "(X) Exit App");
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            String userSelectionInput = scanner.nextLine().trim().replaceAll("\\s+", "");
            if (checkIfEmpty(userSelectionInput)) {thisFieldCantBeEmpty(); continue;}
            ResultHelper usi = allowUserToExitOrReturn(userSelectionInput); if (returner(usi)) {return usi;}
            userSelection = userSelectionInput.toUpperCase().charAt(0);

            if (userSelection != 'D' && userSelection !='P' && userSelection != 'L' && userSelection != 'X') {
                System.out.println("\nPlease choose only from listed options. Or Enter X to exit.");
                continue;
            }
            keepGoing = false;
        }
        return new ResultHelper(userSelection, true) ;
    }
    public static ResultHelper homeScreen (Scanner scanner) {
        char userSelection = '0';
        boolean keepGoing = true;
        while (keepGoing) {
            titleNewLineTop();
            System.out.println("Use Account Ledger App to monitor and make transactions!\n" +
                    "What Service do you need today?\n\n" +
                    "(D) Make Deposit\n" +
                    "(P) Make Payment\n" +
                    "(L) Open Ledger to View & Manage\n" +
                    "(X) Exit App");
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            String userSelectionInput = scanner.nextLine().trim().replaceAll("\\s+", "");
            if (checkIfEmpty(userSelectionInput)) {thisFieldCantBeEmpty();continue;}
            ResultHelper usi = allowUserToExitOrReturn(userSelectionInput); if (returner(usi)) {return usi;}
            userSelection = userSelectionInput.toUpperCase().charAt(0);

            if (userSelection != 'D' && userSelection !='P' && userSelection != 'L' && userSelection != 'X') {
                System.out.println("\nPlease choose only from listed options. Or Enter X to exit.");
                continue;
            }


            keepGoing = false;
        }
        return new ResultHelper(userSelection, true);
    }
    public static void exitSequence () throws InterruptedException {
        titleNewLineTop();
        System.out.println("Thank you for using Account Ledger App!");
        System.out.println("Goodbye!");
        titleLineBottom();
        timer(1500);
    }

    // Functions // ---------------------------------------------------------------------------------------------------
    public static ResultHelper makeDeposit (Scanner scanner) throws NumberFormatException, InterruptedException, IOException {
        double transactionAmnt = 0;
        boolean keepGoing = true;
        while (keepGoing) {
            titleNewLineTop();
            System.out.println("Your Current Account Balance Is: " + currentBalance(scanner));
            System.out.println("Would you like to make a deposit?\n" +
                    "Enter deposit amount to enter 0 to return to menu.\n" +
                    "Or press (X) to close the app\n" +
                    "(0) Go back\n" +
                    "(X) Exit");
            titleLineBottom();
            System.out.print("\n\nEnter:  ");

            // Catches Exit Sequence // -------------------------------------------------------------------------------
            String transactionInput = scanner.nextLine().trim().replaceAll("\\s+", "");
            if (checkIfEmpty(transactionInput)) {thisFieldCantBeEmpty();continue;}
            ResultHelper transInput = allowUserToExitOrReturn(transactionInput); if (returner(transInput)) {return transInput;}

            // If not Exit Sequence // --------------------------------------------------------------------------------
            try {

                transactionAmnt = Double.parseDouble(transactionInput);
            } catch (NumberFormatException ignored) {}

            timer(750);
            boolean descDone = false;
            String finalDesc = "";
            while (!descDone) {
                titleNewLineTop();
                System.out.println("In 300 character or less. Write a short description of the transaction." +
                        promptUser());
                titleLineBottom();
                System.out.print("\n\nEnter:  ");

                // Input // Catches Exit / Return Request // ----------------------------------------------------------
                String descInput = scanner.nextLine().trim().replaceAll("\\s+", " ");
                if (checkIfEmpty(descInput)) {thisFieldCantBeEmpty();continue;}
                ResultHelper desc = allowUserToExitOrReturn(descInput); if (returner(desc)) {return desc;}

                // If no exit / return call //-------------------------------------------------------------------------
                if (descInput.length() <= 300) {
                   finalDesc = autoLineBreakAt100UpTo300(descInput);
                   descDone = true;
                   break;
                } else {
                    System.out.println("Invalid Input or too many characters!");
                }
            }

            timer(750);
            String vendor = "";
            titleNewLineTop();
            System.out.println("Is this money coming from a company? Or a personal deposit.\n" +
                    "Enter the name of the company or simply enter (P) for a Personal Deposit\n" +
                    promptUser());
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            String vendorInput = scanner.nextLine().trim().replaceAll("\\s+", " ");
            ResultHelper vend = allowUserToExitOrReturn(vendorInput); if (returner(vend)) {return vend;}

            if (vendorInput.equalsIgnoreCase("P")) {
                vendor = "Personal Deposit";
            } else {
                vendor = autoCapitalizeFirstLetter(vendorInput);
            }


            // (".*[^a-zA-Z0-9 ].*")
            boolean confirmTransaction = false;
            while (!confirmTransaction) {
                newLineTop();
                System.out.println("Amount: [$" + transactionAmnt + "] Entered\n" +
                        "Description: " + finalDesc + "\n" +
                        "Vendor: " + vendor);
                lineBottom();
                System.out.println("\nIs this info correct? (Y) or (N)\n" +
                        "(Y)es or (N)o?\n" +
                        promptUser());
                String userConfirmInput = scanner.nextLine().trim().replaceAll("\\s+", "");
                if (checkIfEmpty(userConfirmInput)) {thisFieldCantBeEmpty();continue;}
                ResultHelper uci = allowUserToExitOrReturn(userConfirmInput); if (returner(uci)) {return uci;}
                char userConfirm = userConfirmInput.toUpperCase().charAt(0);

                if (userConfirm == 'Y') {
                    confirmTransaction = true;
                    break;
                } else if (userConfirm == 'N') {
                    timer1000();
                    boolean confirmInfo = false;
                    while (!confirmInfo) {
                        titleNewLineTop();
                        System.out.println("(1) Amount: [$" + transactionAmnt + "] Entered\n" +
                                "(2) Description: " + finalDesc + "\n" +
                                "(3) Vendor: " + vendor);
                        System.out.println("Which part needs to be fixed?\n\n" +
                                "(1) Amount\n" +
                                "(2) Description\n" +
                                "(3) Vendor\n" +
                                "(4) Nothing, it looks fine.");
                        titleLineBottom();
                        char pickPart = scanner.nextLine().trim().toUpperCase().charAt(0);

                        if (pickPart == '1') {
                            titleNewLineTop();
                            System.out.println("Please Enter Amount.");
                            titleLineBottom();
                            System.out.print("\n\nEnter:  ");
                            transactionAmnt = Double.parseDouble(scanner.nextLine().trim().replaceAll("\\s+", " "));
                            break;
                        } else if (pickPart == '2') {
                            boolean reEnterDesc = false;
                            while (!reEnterDesc) {
                                titleNewLineTop();
                                System.out.println("In 300 character or less. Write a short description of the transaction.");
                                titleLineBottom();
                                System.out.print("\n\nEnter:  ");
                                String descInput = scanner.nextLine().trim().replaceAll("\\s+", " ");

                                if (descInput.length() <= 300) {
                                    finalDesc = autoLineBreakAt100UpTo300(descInput);
                                    break;
                                } else {
                                    System.out.println("Invalid Input or too many characters!");
                                }
                            }
                        } else if (pickPart == '3') {
                            titleNewLineTop();
                            System.out.println("Is this money coming from a company? Or a personal deposit.\n" +
                                    "Enter the name of the company or simply enter (P) for a Personal Deposit\n");
                            titleLineBottom();
                            System.out.print("\n\nEnter:  ");
                            vendorInput = scanner.nextLine().trim().replaceAll("\\s+", " ");

                            if (vendorInput.equalsIgnoreCase("P")) {
                                vendor = "Personal Deposit";
                            } else {
                                vendor = autoCapitalizeFirstLetter(vendorInput);
                            }

                            break;
                        } else if (pickPart == '4') {
                            break;
                        } else {
                            System.out.println("\n Invalid Input. Please choose from listed options.");
                        }

                    }

                } else {
                    System.out.println("\nInvalid Input. (Y) or (N).");
                }
            } // TC Loop End // ---------------------------------------------------------------------------------------

            transactionLogger(transactionAmnt, finalDesc, vendor);
            timer1500();
            titleNewLineTop();
            System.out.println("Your Payment has been posted and will be reflected in your account balance.");
            System.out.println("Thank you for your deposit and continued use of Account Ledger App!");
            System.out.println("Do you need to make another deposit? (Y) or (N)");
            System.out.println("(Y)es or (N)o?");
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            char userConfirm = scanner.nextLine().trim().toUpperCase().charAt(0);

            if (userConfirm == 'Y') {
                newLineTop();
                System.out.println("Setting up next transaction...");
                lineBottom();
                timer(500);

            } else if (userConfirm == 'N') {
                titleNewLineTop();
                System.out.println("Exit App or Go To Main Menu? (0) or (X).");
                System.out.println("(X) Exit App");
                System.out.println("(0) Main Menu");
                titleLineBottom();
                System.out.print("Enter:  ");
                String userChoice = scanner.nextLine().trim().replaceAll("\\s+", "");
                ResultHelper lastCall = allowUserToExitOrReturn(userChoice); if (returner(lastCall)) {return lastCall;}

            } else {
                System.out.println("\nInvalid Input. Please choose from listed options.");
            }

            return new ResultHelper('0', true);
        } // Keep Going End // ----------------------------------------------------------------------------------------
        return new ResultHelper('0', true);
    }
    public static ResultHelper makePayment (Scanner scanner) throws NumberFormatException, InterruptedException, IOException {
        double transactionAmnt = 0;
        boolean keepGoing = true;
        while (keepGoing) {
            titleNewLineTop();
            System.out.println("Your Current Account Balance Is: " + currentBalance(scanner));
            System.out.println("Would you like to make a payment?\n" +
                    "Enter payment amount to enter 0 to return to menu.\n" +
                    "Or press (X) to close the app\n" +
                    "(0) Go back\n" +
                    "(X) Exit");
            titleLineBottom();
            System.out.print("\n\nEnter:  ");

            // Catches Exit Sequence // -------------------------------------------------------------------------------
            String transactionInput = scanner.nextLine().trim().replaceAll("\\s+", "");
            if (checkIfEmpty(transactionInput)) {thisFieldCantBeEmpty();continue;}
            ResultHelper transInput = allowUserToExitOrReturn(transactionInput); if (returner(transInput)) {return transInput;}

            // If not Exit Sequence // --------------------------------------------------------------------------------
            try {
                transactionAmnt = Double.parseDouble(transactionInput);
            } catch (NumberFormatException ignored) {}

            timer(750);
            boolean descDone = false;
            String finalDesc = "";
            while (!descDone) {
                titleNewLineTop();
                System.out.println("In 300 character or less. Write a short description of the transaction." +
                        promptUser());
                titleLineBottom();
                System.out.print("\n\nEnter:  ");

                // Input // Catches Exit / Return Request // ----------------------------------------------------------
                String descInput = scanner.nextLine().trim().replaceAll("\\s+", " ");
                ResultHelper desc = allowUserToExitOrReturn(descInput); if (returner(desc)) {return desc;}

                // If no exit / return call //-------------------------------------------------------------------------
                if (descInput.length() <= 300) {
                    finalDesc = autoLineBreakAt100UpTo300(descInput);
                    descDone = true;
                    break;
                } else {
                    System.out.println("Invalid Input or too many characters!");
                }
            }

            timer(750);
            String vendor = "";
            titleNewLineTop();
            System.out.println("Is this money going to a company or a personal withdraw?\n" +
                    "Enter the name of the company or simply enter (P) for a Personal Withdraw\n" +
                    promptUser());
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            String vendorInput = scanner.nextLine().trim().replaceAll("\\s+", " ");
            ResultHelper vend = allowUserToExitOrReturn(vendorInput); if (returner(vend)) {return vend;}

            if (vendorInput.equalsIgnoreCase("P")) {
                vendor = "Personal Withdraw";
            } else {
                vendor = autoCapitalizeFirstLetter(vendorInput);
            }


            // (".*[^a-zA-Z0-9 ].*")
            boolean confirmTransaction = false;
            while (!confirmTransaction) {
                newLineTop();
                System.out.println("Amount: [$" + transactionAmnt + "] Entered\n" +
                        "Description: " + finalDesc + "\n" +
                        "Vendor: " + vendor);
                lineBottom();
                System.out.println("\nIs this info correct? (Y) or (N)\n" +
                        "(Y)es or (N)o?\n" +
                        promptUser());
                String userConfirmInput = scanner.nextLine().trim().replaceAll("\\s+", "");
                if (checkIfEmpty(userConfirmInput)) {thisFieldCantBeEmpty();continue;}
                ResultHelper uci = allowUserToExitOrReturn(userConfirmInput); if (returner(uci)) {return uci;}
                char userConfirm = userConfirmInput.toUpperCase().charAt(0);

                if (userConfirm == 'Y') {
                    confirmTransaction = true;
                    break;
                } else if (userConfirm == 'N') {
                    timer1000();
                    boolean confirmInfo = false;
                    while (!confirmInfo) {
                        titleNewLineTop();
                        System.out.println("(1) Amount: [$" + transactionAmnt + "] Entered\n" +
                                "(2) Description: " + finalDesc + "\n" +
                                "(3) Vendor: " + vendor);
                        System.out.println("Which part needs to be fixed?\n\n" +
                                "(1) Amount\n" +
                                "(2) Description\n" +
                                "(3) Vendor\n" +
                                "(4) Nothing, it looks fine.");
                        titleLineBottom();
                        char pickPart = scanner.nextLine().trim().toUpperCase().charAt(0);

                        if (pickPart == '1') {
                            titleNewLineTop();
                            System.out.println("Please Enter Amount.");
                            titleLineBottom();
                            System.out.print("\n\nEnter:  ");
                            transactionAmnt = Double.parseDouble(scanner.nextLine().trim().replaceAll("\\s+", " "));
                            break;
                        } else if (pickPart == '2') {
                            boolean reEnterDesc = false;
                            while (!reEnterDesc) {
                                titleNewLineTop();
                                System.out.println("In 300 character or less. Write a short description of the transaction.");
                                titleLineBottom();
                                System.out.print("\n\nEnter:  ");
                                String descInput = scanner.nextLine().trim().replaceAll("\\s+", " ");

                                if (descInput.length() <= 300) {
                                    finalDesc = autoLineBreakAt100UpTo300(descInput);
                                    break;
                                } else {
                                    System.out.println("Invalid Input or too many characters!");
                                }
                            }
                        } else if (pickPart == '3') {
                            titleNewLineTop();
                            System.out.println("Is this money coming from a company? Or a personal deposit.\n" +
                                    "Enter the name of the company or simply enter (P) for a Personal Deposit\n");
                            titleLineBottom();
                            System.out.print("\n\nEnter:  ");
                            vendorInput = scanner.nextLine().trim().replaceAll("\\s+", " ");

                            if (vendorInput.equalsIgnoreCase("P")) {
                                vendor = "Personal Withdraw";
                            } else {
                                vendor = autoCapitalizeFirstLetter(vendorInput);
                            }

                            break;
                        } else if (pickPart == '4') {
                            break;
                        } else {
                            System.out.println("\n Invalid Input. Please choose from listed options.");
                        }

                    }

                } else {
                    System.out.println("\nInvalid Input. (Y) or (N).");
                }
            } // TC Loop End // ---------------------------------------------------------------------------------------

            transactionLogger(-1*(transactionAmnt), finalDesc, vendor);
            titleNewLineTop();
            System.out.println("Your Payment has been posted and will be reflected in your account balance.");
            System.out.println("Thank you for your deposit and continued use of Account Ledger App!");
            System.out.println("Do you need to make another deposit? (Y) or (N)");
            System.out.println("(Y)es or (N)o?");
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            char userConfirm = scanner.nextLine().trim().toUpperCase().charAt(0);

            if (userConfirm == 'Y') {
                newLineTop();
                System.out.println("Setting up next transaction...");
                lineBottom();
                timer(500);

            } else if (userConfirm == 'N') {
                titleNewLineTop();
                System.out.println("Exit App or Go To Main Menu? (0) or (X).");
                System.out.println("(X) Exit App");
                System.out.println("(0) Main Menu");
                titleLineBottom();
                System.out.print("Enter:  ");
                String userChoice = scanner.nextLine().trim().replaceAll("\\s+", "");
                ResultHelper lastCall = allowUserToExitOrReturn(userChoice); if (returner(lastCall)) {return lastCall;}

            } else {
                System.out.println("\nInvalid Input. Please choose from listed options.");
            }

            return new ResultHelper('0', true);
        } // Keep Going End // ----------------------------------------------------------------------------------------
        return new ResultHelper('0', true);
    }

    // Back End // ----------------------------------------------------------------------------------------------------
    public static void transactionLogger (double transactionAmnt, String description, String vendor) throws IOException, NumberFormatException {
        LocalDate dateInput = LocalDate.now();
        DateTimeFormatter formatterD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateInput.format(formatterD);
        LocalTime timeInput = LocalTime.now();
        DateTimeFormatter formatterT = DateTimeFormatter.ofPattern("hh:mm:ss");
        String time = timeInput.format(formatterT);
        try {
            BufferedWriter lilJon = new BufferedWriter(new FileWriter("transactions.csv", true));
            lilJon.write("\n" + date + "|" + time + "|" + description + "|" + vendor + "|" + transactionAmnt);
            lilJon.flush();
        } catch (IOException ignored) {}
    }
    public static void activityLogger (String action) throws IOException {
        LocalDate dateInput = LocalDate.now();
        DateTimeFormatter formatterD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateInput.format(formatterD);
        LocalTime timeInput = LocalTime.now();
        DateTimeFormatter formatterT = DateTimeFormatter.ofPattern("hh:mm:ss");
        String time = timeInput.format(formatterT);
        try {
            BufferedWriter lilJon = new BufferedWriter(new FileWriter("logs.txt", true));
            lilJon.write("\n" + date + "|" + time + "|" + action);
            lilJon.flush();
        } catch (IOException ignored) {}
    }
    public static String currentBalance (Scanner scanner) throws IOException {
        double transaction = 0;
        BufferedReader lilTim = new BufferedReader(new FileReader("transactions.csv"));
        scanner = new Scanner(lilTim);
        scanner.nextLine();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String [] inputParts = input.split("\\|");
            if (inputParts.length > 4) {
                transaction += Double.parseDouble(inputParts[4]);
            }
        }

        BigDecimal balance = BigDecimal.valueOf(transaction).setScale(2, RoundingMode.HALF_UP);
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String formattedBalance = formatter.format(balance);
        return formattedBalance;
    }
    public static ResultHelper allowUserToExitOrReturn (String input) throws NumberFormatException {
        boolean keepGoing = true;
        input = input.trim().replaceAll("\\s+", " ");
        char function = input.toUpperCase().charAt(0);
        if (function == '0') {keepGoing = true;}
        if (function == 'X') {keepGoing = false;}
        return new ResultHelper(function, keepGoing);
    }
    public static boolean returner (ResultHelper instance) {
        return instance.getFunction() == '0' || instance.getFunction() == 'X';
    }
    public static boolean programQuitter (ResultHelper mainInstance) {
        return mainInstance.getFunction() == 'X';
    }
    public static boolean checkIfEmpty (String input) {
        if (input.isEmpty()) {return true;} else {return false;}
    }
    public static ArrayList<String> allTransactions () throws IOException {
        BufferedReader lilTim = new BufferedReader(new FileReader("transactions.csv"));
        Scanner fileReader = new Scanner(lilTim);
        ArrayList<String> allTransactions =new ArrayList<>();

        fileReader.nextLine();
        while (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            allTransactions.add(line);
        }
        fileReader.close();

        Collections.reverse(allTransactions);

        return allTransactions;
    }

    // Design Elements // ---------------------------------------------------------------------------------------------
    public static void titleNewLineTop () {
        System.out.println("\n☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F");
    }
    public static void titleLineBottom () {
        System.out.println("☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F☠\uFE0F");
    }
    public static void newLineTop() {
        System.out.println("\n───────────────────────────────────────────────────────────────");
    }
    public static void lineBottom() {
        System.out.println("───────────────────────────────────────────────────────────────");
    }
    public static void timer(int millis) throws InterruptedException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void timer1500 () {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void timer1000 () {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static String promptUser () {
        return "You can also enter (0) to return to menu, or (X) to exit app.";

    }
    public static String autoLineBreakAt100UpTo300 (String input) {
        String returnInput = "";
        if (input.length() > 100) {
            while (input.length() > 100) {
                int breaker = input.lastIndexOf(' ', 100);
                returnInput += input.substring(0, breaker) + "\n";
                input = input.substring(breaker + 1);
            }
            returnInput += input;
        } if (input.length() < 100) {
            returnInput = input;
        }
        return returnInput;
    }
    public static String autoCapitalizeFirstLetter (String input) {
        String [] inputParts = input.toLowerCase().split(" ");
        for (int i = 0; i < inputParts.length; i++) {
            inputParts [i] = inputParts [i].substring(0, 1).toUpperCase() + inputParts [i].substring(1);
        }
        input = String.join(" ", inputParts);
        return input;
    }
    public static void thisFieldCantBeEmpty () {
        System.out.println("This Field Cannot Be Empty!");
    }


}
