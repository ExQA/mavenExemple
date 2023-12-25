package org.example.service;

import org.example.config.DatabaseConnection;
import org.example.model.Game;
import org.example.model.User;
import org.example.repository.GameRepositoryImpl;
import org.example.repository.UserRepositoryImpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ConsoleInterface {

    private final Scanner scanner;
    private final UserService userService;
    private final GameService gameService;

    private User currentUser = null;

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
                case "logout" -> logout();
                case "games" -> showAllGames();
                case "addgame" -> addNewGame();
                case "purchase" -> purchase();
                case "credit" -> credit();
                case "info" -> info();
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
        System.out.println("- logout: log out");
        System.out.println("- games: вивести список усіх ігор");
        System.out.println("- addgame: додати нову гру");
        System.out.println("- credit: попонить счет");
        System.out.println("- purchase: покупка игры");
        System.out.println("- exit: вийти з програми");
    }

    private void exit() {
        try {
            DatabaseConnection.getConnection().close();
            System.out.println("Дякуємо за використання додатку. До побачення!");
            System.exit(0);
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
            currentUser = loggedInUser;
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка входу: " + e.getMessage());
        }
    }

    public void logout() {
        currentUser = null;
    }

    public void info() {
        if (currentUser == null) {
            System.out.println("No active user, please log in");
            return;
        }
        System.out.println(currentUser);
    }

    private void showAllGames() {
        List<Game> games = gameService.getAllGames();
        System.out.println("Список доступних ігор:");
        games.forEach(game -> System.out.println(game));
    }

    private void credit() {
        if (currentUser == null) {
            System.out.println("No active user, please log in");
            return;
        }

        try {
            System.out.println("Do you want credit account for " + currentUser.getNickname() + " user y/n ?");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("y")) {
                System.out.println("Enter amount: ");
                String inputAmount = scanner.nextLine();
                userService.creditUser(currentUser, Double.parseDouble(inputAmount));
                System.out.println("The account is credited for " + inputAmount);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void purchase() {
        if (currentUser == null) {
            System.out.println("No active user, please log in");
            return;
        }
        System.out.println("Enter Game name: ");
        String gameName = scanner.nextLine();

        if (currentUser.getGames().stream().anyMatch(g -> g.getName().equalsIgnoreCase(gameName))) {
            System.out.println("You already have this game.");
            return;
        }

        Game game = gameService.findByName(gameName);

        if (game == null) {
            System.out.println("No such game: " + gameName);
            return;
        }

        if (currentUser.getAmount() < game.getCost()) {
            System.out.println("You don't have enough money. " + "The " + game.getName() + " cost " + game.getCost());
            return;
        }
        userService.buyGame(currentUser, game);
        System.out.println(""); // todo add message
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
            Connection connection = DatabaseConnection.getConnection();
            GameService gameService = new GameService(new GameRepositoryImpl(connection));
            UserService userService = new UserService(new UserRepositoryImpl(connection), gameService);
            ConsoleInterface consoleInterface = new ConsoleInterface(userService, gameService);
            consoleInterface.start();
        } catch (SQLException e) {
            System.out.println("Помилка підключення до бази даних: " + e.getMessage());
        }
    }
}