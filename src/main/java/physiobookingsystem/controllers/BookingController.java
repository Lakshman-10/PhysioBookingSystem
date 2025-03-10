/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package physiobookingsystem.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import physiobookingsystem.models.Patient;
import physiobookingsystem.models.Physio;
import physiobookingsystem.models.Timetable;
import physiobookingsystem.utilities.PatientFileHandler;
import physiobookingsystem.utilities.PhysioFileHandler;
import physiobookingsystem.utilities.TimetableFileHandler;

/**
 *
 * @author Lakshman 23086585
 */
public class BookingController {
    private MainController mainController;
    Scanner scanner = new Scanner(System.in);
    public boolean bookingMenuexit;
    public boolean cancelMenuexit;
    public boolean attendMenuexit;
    
    public BookingController(MainController mainController) {
        this.mainController = mainController;
        this.scanner = new Scanner(System.in);
        this.bookingMenuexit = false;
        this.cancelMenuexit = false;
        this.attendMenuexit = false;
    }
    
    public void start() {
        bookingMenuexit = false;
        while (!bookingMenuexit) {
            System.out.println("\nBook an Appointment:");
            System.out.println("1. Search by Expertise Area");
            System.out.println("2. Search by Physiotherapist Name"); 
            System.out.println("3. Return to Main Menu");
            System.out.print("Enter your choice: ");
            try{
                int choice = scanner.nextInt(); 
                scanner.nextLine(); 
                
                switch (choice) {
                    case 1: 
                        searchByExpertiseArea();
                        //If user is redirected to rebooking then Exit option is disabled because we can exit from the cancel function
                        if(cancelMenuexit){
                            isExit();
                        }                        
                        break;
                    case 2: 
                        searchByPhysioId();
                        //If user is redirected to rebooking then Exit option is disabled because we can exit from the cancel function
                        if(cancelMenuexit){
                            isExit();
                        }   
                        break; 
                    case 3:
                        bookingMenuexit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                    }
                } 
                catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                    System.out.println("Please enter a valid input.");
                }
        }
    }
    
    //method is used to show all the appointments for a specific expertise area
    public void searchByExpertiseArea() { 
        try{
            List<Timetable> availableSlots;
            
            System.out.print("Enter Expertise Area: ");
            String expertise = scanner.nextLine().trim(); 
            availableSlots = TimetableFileHandler.getSlotsByExpertise(expertise);
            
            if(availableSlots.size()>0){
                // Display available appointments
                System.out.println("\nAvailable Slots for " + expertise);
                displayAvailableSlots(expertise, availableSlots);
                
                System.out.print("\nDo you want to book an appointment? (Y/N): ");
                String responseBooking = scanner.next();
                if (responseBooking.equalsIgnoreCase("y")) {
                     bookingModification("Booked");
                }
            }
            else{
                System.out.println("\nNo Available Slots for " + expertise);
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    
    //method is used to show all the appointments for a specific physio
    public void searchByPhysioId() { 
        try{
            List<Timetable> availableSlots;
            List<Physio> physios = PhysioFileHandler.readPhysiosFromFile();
            
            System.out.print("Enter Physio Name: ");
            String physioName = scanner.nextLine().trim();
                        
            List<Integer> matchingPhysioIds = new ArrayList<>();
            for (Physio physio : physios) {
                if (physio.getFullName().toLowerCase().contains(physioName.toLowerCase())) {
                    matchingPhysioIds.add(physio.getId());
                } 
            }
            
            if(matchingPhysioIds.size()>0){
                availableSlots = TimetableFileHandler.getSlotsByPhysio(matchingPhysioIds);
            
                if(availableSlots.size()>0){
                    // Display available appointments
                    System.out.println("\nAvailable Slots for " + physioName);
                    displayAvailableSlots(physioName, availableSlots);
                    
                    System.out.print("\nDo you want to book an appointment? (Y/N): ");
                    String responseBooking = scanner.next();
                    if (responseBooking.equalsIgnoreCase("y")) {
                         bookingModification("Booked");
                    }
                }
                else{
                    System.out.println("\nNo Available Slots for " + physioName);
                }
            }
            else{
                System.out.println("No physiotherapists found with the name " + physioName );
            }
            
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } 
    }
    
    //this method is used to display the timetable slots
    public void displayAvailableSlots(String searchTxt, List<Timetable> availableSlots ){
        try{
          List<Physio> physios = PhysioFileHandler.readPhysiosFromFile();
          
          System.out.printf("%-5s%-25s%-30s%-30s%-15s%-15s%-15s%n", "ID", "Physio ID", "Expertise", "Treatment", "Date", "Time", "Status");
          System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");

          for (int i = 0; i < availableSlots.size(); i++) {
              Timetable slot = availableSlots.get(i);

              String physioName = "Unknown";
              for (Physio physio : physios) {
                  if (physio.getId() == slot.getPhysioId()) {
                      physioName = physio.getFullName();
                      break;
                  }
              }

              System.out.printf("%-5d%-25s%-30s%-30s%-15s%-15s%-15s%n",
                      slot.getId(), physioName, slot.getExpertiseArea(), slot.getTreatment(),
                      slot.getDate(),slot.getTime(), slot.getStatus());
          }  
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        
    }
    
    //this method is used to book or cancel or attent an appointment
    public void bookingModification(String status){
        try{
            System.out.print("Enter Booking Id: ");
            String bookingID = scanner.next();
            
            System.out.print("Enter Patient Id: ");
            String patientId = scanner.next();

            // Load all patients from the file
            List<Patient> patients = PatientFileHandler.readPatientsFromFile();
            Patient patientToBook = null;

            // Search for the patient by ID
            for (Patient patient : patients) {
                if (patient.getId() == Integer.parseInt(patientId)) {
                    patientToBook = patient;
                    break;
                }
            }

            //Check whether there is a patient registered with the ID
            if (patientToBook != null) {
                if(status == "Booked" && TimetableFileHandler.canBookSlot(patientId, bookingID) ){
                    //only used for booking an appointment only. This will validates the duplicate booking
                    if(TimetableFileHandler.updateBooking(patientId, bookingID, status)){
                        
                        // Display the booked slot
                        List<Timetable> slots = TimetableFileHandler.readTimetableFromFile();
                        for (Timetable slot : slots) {
                            if (slot.getId() == Integer.parseInt(bookingID)) {
                                List<Timetable> slotList = Collections.singletonList(slot);
                                System.out.println("\nBooking Details");
                                displayAvailableSlots("Booking Details", slotList);  
                            }
                        }
                    } 
                }
                else if(status == "Cancelled" && TimetableFileHandler.canCancelSlot(patientId, bookingID) ){
                    //only used for cancelling appointment only.
                    if(TimetableFileHandler.updateBooking("0", bookingID, status)){
                        // Display the booked slot
                        List<Timetable> slots = TimetableFileHandler.readTimetableFromFile();
                        for (Timetable slot : slots) {
                            if (slot.getId() == Integer.parseInt(bookingID)) {
                                List<Timetable> slotList = Collections.singletonList(slot);
                                System.out.println("\nBooking Details");
                                displayAvailableSlots("Booking Details", slotList);  
                            }
                        }
                        //Ask the user whether needed another booking
                        System.out.print("\nDo you want to book an appointment again? (Y/N): ");
                        String responseReBooking = scanner.next();
                        if (responseReBooking.equalsIgnoreCase("y")) {
                            bookingMenuexit = false;
                            start();
                        }
                    } 
                }
                else if(status == "Attended" && TimetableFileHandler.canAttendSlot(patientId, bookingID)){
                    //only used for attending appointment only.
                    if(TimetableFileHandler.updateBooking(patientId, bookingID, status)){
                        // Display the booked slot
                        List<Timetable> slots = TimetableFileHandler.readTimetableFromFile();
                        for (Timetable slot : slots) {
                            if (slot.getId() == Integer.parseInt(bookingID)) {
                                List<Timetable> slotList = Collections.singletonList(slot);
                                System.out.println("\nBooking Details");
                                displayAvailableSlots("Booking Details", slotList);  
                            }
                        } 
                    } 
                }
            }
            else{
                System.out.println("No patient found with the ID " + patientId);
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        
    }
    
    //Cancelling the booking
    public void cancelBooking(){
        cancelMenuexit = false;
        while (!cancelMenuexit) {  
            System.out.println("\nCancel an Appointment:"); 

            bookingModification("Cancelled");
            System.out.print("\nDo you want to cancel another appointment (Y/N): ");
            String responseAttend = scanner.next();
            if (responseAttend.equalsIgnoreCase("n")) {
                cancelMenuexit = true;
                bookingMenuexit = true;
                break;
            }
        }
    }
    
    //Attending the booking
    public void attendBooking(){
        attendMenuexit = false;
        while (!attendMenuexit) {  
            System.out.println("\nAttend an Appointment:"); 

            bookingModification("Attended");
            System.out.print("\nDo you want to attend another appointment (Y/N): ");
            String responseCancel = scanner.next();
            if (responseCancel.equalsIgnoreCase("n")) {
                attendMenuexit = true;
                bookingMenuexit = true;
                break;
            }
        }
    }
    
    public void isExit(){
        // Ask if the user wants to exit or continue
        System.out.print("\nDo you want to exit the program? (Y/N): ");
        String response = scanner.next();
        if (response.equalsIgnoreCase("y")) {
            mainController.exit = true; 
            bookingMenuexit = true;
            cancelMenuexit = true;
            System.out.println("Exiting the program. Goodbye!");
        }
    }
    
}
