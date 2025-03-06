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
    
    //this method is used to check whether the patient has duplicate booking
    public static boolean canBookSlot(String patientId, String bookingId) {
        List<Timetable> slots = readTimetableFromFile();

        Timetable selectedSlot = slots.stream()
                .filter(slot -> slot.getId() == Integer.parseInt(bookingId))
                .findFirst()
                .orElse(null);

        if (selectedSlot == null) {
            System.out.println("No Appointment found with the booking ID " + bookingId);
            return false;
        }

        if (!(selectedSlot.getStatus().equalsIgnoreCase("Available") || selectedSlot.getStatus().equalsIgnoreCase("Cancelled"))) {
            System.out.println("This slot is already booked.");
            return false;
        }

        boolean duplicateBooking = slots.stream()
                .anyMatch(slot -> 
                        slot.getPatientId() == Integer.parseInt(patientId) &&
                        slot.getDate().equals(selectedSlot.getDate()) &&
                        slot.getTime().equals(selectedSlot.getTime())
                );

        if (duplicateBooking) {
            System.out.println("You already have another booking at this time.");
            return false;
        }

        return true; // Booking is allowed
    }
    
    //this method is used to check whether the patient has booked the particular booking slot
    public static boolean canCancelSlot(String patientId, String bookingId) {
        List<Timetable> slots = readTimetableFromFile();

        Timetable selectedSlot = slots.stream()
                .filter(slot -> slot.getId() == Integer.parseInt(bookingId))
                .findFirst()
                .orElse(null);

        if(selectedSlot == null){
            System.out.println("No Appointment found with the booking ID " + bookingId);
            return false;
        }
        
        if(selectedSlot.getStatus().equalsIgnoreCase("Attended")){
            System.out.println("This slot is already attended");
            return false;
        }

        if(!selectedSlot.getStatus().equalsIgnoreCase("Booked") || selectedSlot.getPatientId() != Integer.parseInt(patientId)) {
            System.out.println("This slot is not booked by the patient Id " + patientId);
            return false;
        }  

        return true; // Cancelling is allowed
    }
    
    //this method is used to check whether the patient can attend the particular booking slot
    public static boolean canAttendSlot(String patientId, String bookingId) {
        List<Timetable> slots = readTimetableFromFile();

        Timetable selectedSlot = slots.stream()
                .filter(slot -> slot.getId() == Integer.parseInt(bookingId))
                .findFirst()
                .orElse(null);

        if(selectedSlot == null){
            System.out.println("No Appointment found with the booking ID " + bookingId);
            return false;
        }
        
        if(selectedSlot.getStatus().equalsIgnoreCase("Attended")){
            System.out.println("This slot is already attended");
            return false;
        }

        if(!selectedSlot.getStatus().equalsIgnoreCase("Booked") || selectedSlot.getPatientId() != Integer.parseInt(patientId)) {
            System.out.println("This slot is not booked by the patient Id " + patientId);
            return false;
        }  

        return true; // Attending is allowed
    }
    
    //this method is used to update the booking in file
    public static boolean updateBooking(String patientId, String bookingId, String status) {
        List<Timetable> slots = readTimetableFromFile();
        boolean success = false;
        try{
            for (Timetable slot : slots) {
                if (slot.getId() == Integer.parseInt(bookingId)) {
                    slot.setPatientId(Integer.parseInt(patientId));
                    slot.setStatus(status);
                }
            }
        
            objectMapper.writeValue(new File(FILE_PATH), slots);
            System.out.println("Booking successfully " + status);
            success = true;
            return success;
        }
        catch (IOException e) {
            System.out.println("Error reading timetable: " + e.getMessage());
            return false;
        }
    }
           
    
}
