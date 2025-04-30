package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static com.pluralsight.AccountingLedger.*;

public class LedgerScreen {

    public static ResultHelper ledgerScreen (Scanner scanner) throws IOException, InterruptedException {
        boolean keepGoing = true;
        while (keepGoing) {
            activityLogger("Opened Ledger Screen");
            titleNewLineTop();
            System.out.println("Welcome To Your Account Ledger Reporting\n" +
                    "What service do you need?\n\n" +
                    "(A) Display ALl Activity\n" +
                    "(D) Display All Deposits\n" +
                    "(P) Display All Payments\n" +
                    "(R) Search by Report/Custom Search\n" +
                    "(0) Return To Main Menu\n" +
                    "(X) Exit");
            titleLineBottom();
            System.out.print("\n\nEnter:  ");
            String userSelectionInput = scanner.nextLine().trim().replaceAll("\\s+", "");
            ResultHelper usi = allowUserToExitOrReturn(userSelectionInput); if (returner(usi)) {return usi;}
            char userSelection = userSelectionInput.toUpperCase().charAt(0);

            if (userSelection == 'A') {
                activityLogger("Displayed All Transactions");
                ResultHelper allReports = displayAllTransactions(scanner);
                if (returner(allReports)) {return allReports;}
            } else if (userSelection == 'D') {
                activityLogger("Displayed All Deposits");
                ResultHelper depositTransactions = displayDepositTransactions(scanner);
                if (returner(depositTransactions)) {return depositTransactions;}
            } else if (userSelection == 'P') {
                activityLogger("Displayed All Payments");
                ResultHelper paymentTransactions = displayPaymentTransactions(scanner);
                if (returner(paymentTransactions)) {return paymentTransactions;}
            } else if (userSelection == 'R') {
                activityLogger("Opened Reports Screen");
                ResultHelper reports = ReportsScreen.reportsScreen(scanner);
                if (returner(reports)) {return reports;}
                if (reports.getFunction() == 'L') {continue;}
            } else {

            }
        }
        return new ResultHelper('z', true);
    }

    // Functions // ---------------------------------------------------------------------------------------------------
    public static ResultHelper displayAllTransactions (Scanner scanner) throws IOException, InterruptedException {
        BufferedReader lilJon = new BufferedReader(new FileReader("transactions.csv"));
        scanner = new Scanner(lilJon);
        scanner.nextLine();
        System.out.println("\n");
        while (scanner.hasNextLine()) {
            timer(110);
            String line = scanner.nextLine();
            System.out.println(line);
            lineBottom();
        }

        Scanner userInput = new Scanner(System.in);
        ResultHelper changeScreen = screenChange(userInput, "Here are all of your transactions!");
        if (returner(changeScreen)) {return changeScreen;}

        return new ResultHelper('0', true);
    }

    public static ResultHelper displayDepositTransactions (Scanner scanner) throws IOException, InterruptedException {
        BufferedReader lilJon = new BufferedReader(new FileReader("transactions.csv"));
        scanner = new Scanner(lilJon);
        scanner.nextLine();
        System.out.println("\n");

        while (scanner.hasNextLine()) {
            timer(110);
            String line = scanner.nextLine();
            String [] lineParts = line.split("\\|");
            if (lineParts.length > 4) {
                if (Double.parseDouble(lineParts[4]) > 0) {
                    System.out.println(line);
                    lineBottom();
                }
            }
        }


        Scanner userInput = new Scanner(System.in);
        ResultHelper changeScreen = screenChange(userInput, "Here are all of your deposit transactions!");
        if (returner(changeScreen)) {return changeScreen;}

        return new ResultHelper('0', true);
    }

    public static ResultHelper displayPaymentTransactions (Scanner scanner) throws IOException, InterruptedException {
        BufferedReader lilJon = new BufferedReader(new FileReader("transactions.csv"));
        scanner = new Scanner(lilJon);
        scanner.nextLine();
        System.out.println("\n");

        while (scanner.hasNextLine()) {
            timer(110);
            String line = scanner.nextLine();
            String [] lineParts = line.split("\\|");
            if (lineParts.length > 4) {
                if (Double.parseDouble(lineParts[4]) < 0) {
                    System.out.println(line);
                    lineBottom();
                }
            }
        }

        Scanner userInput = new Scanner(System.in);
        ResultHelper changeScreen = screenChange(userInput, "Here are all of your payment transactions!");
        if (returner(changeScreen)) {return changeScreen;}

        return new ResultHelper('0', true);
    }

    //Back End // -----------------------------------------------------------------------------------------------------
}
