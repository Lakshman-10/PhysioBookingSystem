/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package physiobookingsystem.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import physiobookingsystem.models.Physio;
import physiobookingsystem.models.Timetable;
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
    
    public BookingController(MainController mainController) {
        this.mainController = mainController;
        this.scanner = new Scanner(System.in);
        this.bookingMenuexit = false;
    }
    
    public void start() {
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
                        isExit();
                        break;
                    case 2: 
                        searchByPhysioId();
                        isExit();
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
                displayAvailableSlots(expertise, availableSlots);
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
                    displayAvailableSlots(physioName, availableSlots);
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
    
    public void displayAvailableSlots(String searchTxt, List<Timetable> availableSlots ){
        try{
          List<Physio> physios = PhysioFileHandler.readPhysiosFromFile();
          System.out.println("\nAvailable Slots for " + searchTxt);
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
    
    public void isExit(){
        // Ask if the user wants to exit or continue
        System.out.print("\nDo you want to exit the program? (Y/N): ");
        String response = scanner.next();
        if (response.equalsIgnoreCase("y")) {
            mainController.exit = true; 
            bookingMenuexit = true;
            System.out.println("Exiting the program. Goodbye!");
        }
    }
    
}
