package org.example.service;

import org.example.config.DatabaseConnection;
import org.example.model.Game;
import org.example.model.User;
import org.example.repository.GameRepositoryImpl;
import org.example.repository.UserRepositoryImpl;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ConsoleInterface {

    private final Scanner scanner;
    private final UserService userService;
    private final GameService gameService;

    public ConsoleInterface(UserService userService, GameService gameService) throws SQLException {
        this.scanner = new Scanner(System.in);
        this.userService = userService;
        this.gameService = gameService;
    }

    public void start() {
        System.out.println("Ласкаво просимо! Введіть команду або 'help' для отримання довідки.");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim();

            switch (command.toLowerCase()) {
                case "help" -> printHelp();
                case "register" -> registerUser();
                case "login" -> loginUser();
                case "games" -> showAllGames();
                case "addgame" -> addNewGame();
                case "exit" -> exit();
                default -> System.out.println("Невідома команда. Введіть 'help' для перегляду доступних команд.");
            }
        }
    }

    private void printHelp() {
        System.out.println("Доступні команди:");
        System.out.println("- help: вивести цю довідку");
        System.out.println("- register: зареєструвати нового користувача");
        System.out.println("- login: увійти до системи");
        System.out.println("- games: вивести список усіх ігор");
        System.out.println("- addgame: додати нову гру");
        System.out.println("- exit: вийти з програми");
    }

    private void exit() {
        try {
            DatabaseConnection.getConnection().close();
            System.out.println("Дякуємо за використання додатку. До побачення!");
        } catch (SQLException e) {
            System.out.println("Помилка при закритті з'єднання: " + e.getMessage());
        }
    }

    private void registerUser() {
        System.out.println("Введіть дані для реєстрації нового користувача.");

        System.out.print("Ім'я: ");
        String name = scanner.nextLine();

        System.out.print("Нікнейм: ");
        String nickname = scanner.nextLine();

        System.out.print("Дата народження (рік-місяць-день): ");
        Date birthday = Date.valueOf(scanner.nextLine());

        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        try {
            User newUser = userService.registerNewUser(name, nickname, birthday, password);
            System.out.println("Користувач зареєстрований: " + newUser);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка при реєстрації: " + e.getMessage());
        }
    }

    private void loginUser() {
        System.out.println("Введіть дані для входу в систему.");

        System.out.print("Нікнейм: ");
        String nickname = scanner.nextLine();

        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        try {
            User loggedInUser = userService.login(nickname, password);
            System.out.println("Користувач увійшов в систему: " + loggedInUser);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка входу: " + e.getMessage());
        }
    }

    private void showAllGames() {
        List<Game> games = gameService.getAllGames();
        System.out.println("Список доступних ігор:");
        games.forEach(game -> System.out.println(game));
    }

    private void addNewGame() {
        System.out.println("Введіть дані для додавання нової гри.");

        System.out.print("Назва: ");
        String name = scanner.nextLine();

        System.out.print("Дата випуску (рік-місяць-день): ");
        Date releaseDate = Date.valueOf(scanner.nextLine());

        System.out.print("Рейтинг: ");
        float rating = Float.parseFloat(scanner.nextLine());

        System.out.print("Вартість: ");
        float cost = Float.parseFloat(scanner.nextLine());

        System.out.print("Опис: ");
        String description = scanner.nextLine();

        Game newGame = gameService.addNewGame(name, releaseDate, rating, cost, description);
        System.out.println("Гра " + newGame.getName() + " успішно додана.");
    }

    public static void main(String[] args) {
        try {
            UserService userService = new UserService(new UserRepositoryImpl(DatabaseConnection.getConnection()));
            GameService gameService = new GameService(new GameRepositoryImpl(DatabaseConnection.getConnection()));
            ConsoleInterface consoleInterface = new ConsoleInterface(userService, gameService);
            consoleInterface.start();
        } catch (SQLException e) {
            System.out.println("Помилка підключення до бази даних: " + e.getMessage());
        }
    }
}