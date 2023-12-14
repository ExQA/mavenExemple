package org.shelter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.shelter.model.PetShelterData;

import java.io.File;

public class PetJsonSerializer {

    private final static ObjectMapper mapper = new ObjectMapper();
    private final static String file = "src/main/resources/shelterData.json";

    public static PetShelterData readData() {
        try {
            return mapper.readValue(new File(file), PetShelterData.class);
        } catch (Exception e) {
            throw new RuntimeException("Error reading file " + file);
        }
    }

    public static boolean writeData(PetShelterData data) {
        try {
            mapper.writeValue(new File(file), data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
