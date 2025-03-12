/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package physiobookingsystem.utilities;
 
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import physiobookingsystem.models.Physio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lakshman 23086585
 */
public class PhysioFileHandler {
    //private static final String FILE_PATH = "D:\\herts\\Sem B\\PSE\\PhysioBookingSystem\\src\\main\\java\\physiobookingsystem\\files\\physios.json";
    private static final String FILE_PATH = "src/main/java/physiobookingsystem/files/physios.json";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Save all physiotherapists to file
    public static boolean savePhysiosToJson(List<Physio> physios) {
        try {
            objectMapper.writeValue(new File(FILE_PATH), physios);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving physiotherapists: " + e.getMessage());
            return false;
        }
    }

    // Read all physiotherapists from file
    public static List<Physio> readPhysiosFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>(); // Return empty list if file does not exist
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<Physio>>() {});
        } catch (IOException e) {
            System.out.println("Error reading physiotherapists: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
