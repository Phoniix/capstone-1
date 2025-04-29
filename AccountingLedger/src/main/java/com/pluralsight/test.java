package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class test {
    public static void main(String[] args) throws IOException {

        BufferedReader lilJon = new BufferedReader(new FileReader("transactions.csv"));
        Scanner scanner = new Scanner(lilJon);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);  // Debugging line
            String[] input = line.split("\\|");
        }
    }
}
