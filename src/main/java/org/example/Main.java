package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the first number:");
        double num1 = scanner.nextDouble();

        System.out.println("Enter the second number:");
        double num2 = scanner.nextDouble();

        System.out.println("Select the operation (+, -, x, /):");
        String operation = scanner.next();

        double result = calculate(num1, num2, operation);
        System.out.println("Result: " + result);
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
                    System.out.println("It is not possible to divide by zero.");
                    System.exit(1);
                }
            default:
                System.out.println("Incorrect operation. Operations are supported: +, -, x, /");
                System.exit(1);
                return 0;
        }
    }
}
