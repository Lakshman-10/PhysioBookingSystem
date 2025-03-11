/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package physiobookingsystem.utilities;
 
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import physiobookingsystem.models.Patient;

/**
 *
 * @author Lakshman 23086585
 */
public class PatientFileHandler {
    private static final String FILE_PATH = "src/main/java/physiobookingsystem/files/patients.json";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Save all patients to file
    public static boolean savePatientsToJson(List<Patient> patients) {
        try {
            objectMapper.writeValue(new File(FILE_PATH), patients);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving patients: " + e.getMessage());
            return false;
        }
    }

    // Read all patients from file
    public static List<Patient> readPatientsFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>(); // Return empty list if file does not exist
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<Patient>>() {});
        } catch (IOException e) {
            System.out.println("Error reading patients: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
