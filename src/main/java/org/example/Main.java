package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Enter the first number:");
            double num1 = readDoubleInput(scanner);

            System.out.println("Enter the second number:");
            double num2 = readDoubleInput(scanner);

            System.out.println("Enter the operation (+, -, x, /):");
            String operation = readOperation(scanner);

            double result = calculate(num1, num2, operation);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static double readDoubleInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextDouble();
            } catch (Exception e) {
                System.out.println("Incorrect number. Enter again:");
                scanner.next();
            }
        }
    }

    private static String readOperation(Scanner scanner) {
        while (true) {
            String operation = scanner.next();
            if (operation.matches("[+\\-x/]")) {
                return operation;
            } else {
                System.out.println("Incorrect operation. Enter again: +, -, x, /");
            }
        }
    }

    private static double calculate(double num1, double num2, String operation) {
        switch (operation) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "x":
                return num1 * num2;
            case "/":
                if (num2 != 0) {
                    return num1 / num2;
                } else {
                    throw new ArithmeticException("It is not possible to divide by zero..");
                }
            default:
                throw new IllegalArgumentException("Incorrect operation.");
        }
    }
}
