package org.shelter.service;

import org.shelter.model.Pet;
import org.shelter.model.PetShelterData;

import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

public class PetShelter {
    private Scanner scanner = new Scanner(System.in);
    private PetShelterData data = PetJsonSerializer.readData();

    public void run() {
        System.out.println("Hello this is the " + data.getLabel());

        while (true) {
            System.out.println("Enter you command...");
            String cmd = scanner.next();
            switch (cmd) {
                case "show" -> show();
                case "add" -> add();
                case "take" -> remove();
                case "exit" -> exit();
                default ->
                        System.out.println("Unknown command, please try again. Available commands are: show, add, take, exit");

            }
        }
    }

    public void show() {
        System.out.println("Here all our animals");
        data.getPets().forEach(System.out::println);
    }

    public void add() {
        System.out.println("name?");
        String name = scanner.next();
        System.out.println("breed?");
        String breed = scanner.next();

        int age = -1;
        while (age < 0) {
            try {
                System.out.println("age?");
                age = Integer.parseInt(scanner.next());
            } catch (Exception e) {
                // do nothing
            }
        }
        Pet pet = new Pet(name, breed, age);
        System.out.println("Adding new pet: " + pet);
        data.getPets().add(pet);
    }

    public void remove() {
        System.out.println("Would you like to remove?");
        String name = scanner.next();

        Optional<Pet> optionalPet = data.getPets().stream()
                .filter(p -> name.equalsIgnoreCase(p.getName()))
                .findFirst();
        if (optionalPet.isPresent()) {
            System.out.println("The " + name + " is your.");
            Pet pet = optionalPet.get();
            data.getPets().remove(pet);
        } else {
            System.out.println("Cannot find. Try again");
        }
    }

    public void exit() {
        System.out.println("Saving the state");
        boolean success = PetJsonSerializer.writeData(data);
        System.out.println("Saving status: " + success);
        System.out.println("Exiting the app");
        System.exit(0);
    }
}
