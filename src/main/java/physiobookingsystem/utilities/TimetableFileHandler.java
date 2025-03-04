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
import java.util.stream.Collectors;
import physiobookingsystem.models.Timetable;

/**
 *
 * @author Lakshman 23086585
 */
public class TimetableFileHandler {
    private static final String FILE_PATH = "src/main/java/physiobookingsystem/files/timetable.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    //Read all timetable slots from JSON
    public static List<Timetable> readTimetableFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>(); // Return empty list if file does not exist
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<Timetable>>() {});
        } catch (IOException e) {
            System.out.println("Error reading timetable: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    //Get all time slots for a specific expertise area
    public static List<Timetable> getSlotsByExpertise(String expertise) {
        List<Timetable> allSlots = readTimetableFromFile();

        return allSlots.stream()
                    .filter(slot -> slot.getExpertiseArea().toLowerCase().contains(expertise.toLowerCase()))
                    .collect(Collectors.toList());
    }
    
    //Get all time slots for a specific physio
    public static List<Timetable> getSlotsByPhysio(List<Integer> matchingPhysioIds) {
        List<Timetable> allSlots = readTimetableFromFile();

        return allSlots.stream()
                .filter(slot -> matchingPhysioIds.contains(slot.getPhysioId()))
                .collect(Collectors.toList());
    }
    
    
}
