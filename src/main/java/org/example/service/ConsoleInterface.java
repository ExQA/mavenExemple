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
        System.out.println("Welcome! Type the command or 'help' for support.");

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
                default -> System.out.println("Unknown command. Enter 'help' to view available commands");
            }
        }
    }

    private void printHelp() {
        System.out.println("Available commands: ");
        System.out.println("- help: display this help");
        System.out.println("- register: register a new user");
        System.out.println("- login: log in to the system");
        System.out.println("- logout: log out");
        System.out.println("- games: display a list of all games");
        System.out.println("- addgame: add new game");
        System.out.println("- credit: top up your account");
        System.out.println("- purchase: buying a game");
        System.out.println("- exit: exit from the program");
    }

    private void exit() {
        try {
            DatabaseConnection.getConnection().close();
            System.out.println("Thank you for using the application. Goodbye!");
            System.exit(0);
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    private void registerUser() {
        System.out.println("Enter data to register a new user.");

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Nickname: ");
        String nickname = scanner.nextLine();

        System.out.print("Date of birth (year-month-day): ");
        Date birthday = Date.valueOf(scanner.nextLine());

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            User newUser = userService.registerNewUser(name, nickname, birthday, password);
            System.out.println("The user is registered: " + newUser);
        } catch (IllegalArgumentException e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    private void loginUser() {
        System.out.println("Enter your login credentials.");

        System.out.print("Nickname: ");
        String nickname = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            User loggedInUser = userService.login(nickname, password);
            System.out.println("The user is logged in: " + loggedInUser);
            currentUser = loggedInUser;
        } catch (IllegalArgumentException e) {
            System.out.println("Login error: " + e.getMessage());
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
        System.out.println("List of available games: ");
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
        System.out.println("The " + game.getName() + "is purchased!");
    }

    private void addNewGame() {
        System.out.println("Enter data to add a new game.");

        System.out.print("Name of the game: ");
        String name = scanner.nextLine();

        System.out.print("Release date (year-month-day): ");
        Date releaseDate = Date.valueOf(scanner.nextLine());

        System.out.print("Rating: ");
        float rating = Float.parseFloat(scanner.nextLine());

        System.out.print("Price: ");
        float cost = Float.parseFloat(scanner.nextLine());

        System.out.print("Description: ");
        String description = scanner.nextLine();

        Game newGame = gameService.addNewGame(name, releaseDate, rating, cost, description);
        System.out.println("Game " + newGame.getName() + " successfully added.");
    }

    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            GameService gameService = new GameService(new GameRepositoryImpl(connection));
            UserService userService = new UserService(new UserRepositoryImpl(connection), gameService);
            ConsoleInterface consoleInterface = new ConsoleInterface(userService, gameService);
            consoleInterface.start();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }
}