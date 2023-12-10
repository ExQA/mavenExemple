package org.shelter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.shelter.model.PetShelterData;

import java.io.File;

public class PetJsonSerializer {

    private final static ObjectMapper mapper = new ObjectMapper();
    private final static String fileName = "src/main/resources/data.json";

    public static PetShelterData readData() {
        try {
            return mapper.readValue(new File(fileName), PetShelterData.class);
        } catch (Exception e) {
            throw new RuntimeException("Error reading file " + fileName);
        }
    }

    public static boolean writeData(PetShelterData data) {
        try {
            mapper.writeValue(new File(fileName), data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
