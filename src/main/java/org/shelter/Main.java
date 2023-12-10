package org.shelter;

import org.shelter.service.PetJsonSerializer;
import org.shelter.model.Pet;
import org.shelter.model.PetShelterData;
import org.shelter.service.PetShelter;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        PetShelter petShelter = new PetShelter();
        petShelter.run();
    }

    private static void testRead() {
        PetShelterData data = PetJsonSerializer.readData();
        System.out.println(data);
    }
    private static void testWrite() {
        Pet pet1 = new Pet("Tom", "Dog", 11);
        Pet pet2 = new Pet("Mirt", "Cat", 1);
        Pet pet3 = new Pet("Lucky", "Dog", 6);

        List<Pet> pets = Arrays.asList(pet1, pet2, pet3);
        PetShelterData data = new PetShelterData("Super Shelter",pets);
        System.out.println(data);

        PetJsonSerializer.writeData(data);
    }


}