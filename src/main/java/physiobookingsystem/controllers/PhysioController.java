/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package physiobookingsystem.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import physiobookingsystem.models.Physio;
import physiobookingsystem.utilities.PhysioFileHandler;

/**
 *
 * @author Lakshman 23086585
 */
public class PhysioController {
    private MainController mainController;
    Scanner scanner = new Scanner(System.in);
    public boolean physioMenuexit;
    
    public PhysioController(MainController mainController) {
        this.mainController = mainController;
        this.scanner = new Scanner(System.in);
        this.physioMenuexit = false;
    }
    
    public void start() {
        while (!physioMenuexit) {
            System.out.println("\nManage Physios:");
            System.out.println("1. Add a Physio");
            System.out.println("2. Remove a Physio");
            System.out.println("3. View All Physios");
            System.out.println("4. Return to Main Menu");
            System.out.print("Enter your choice: ");
            try{
                int choice = new java.util.Scanner(System.in).nextInt();

                switch (choice) {
                    case 1:
                        addPhysio();
                        isExit();
                        break;
                    case 2:
                         isExit();
                        break;
                    case 3:
                        physiolist();
                        isExit();
                        break;
                    case 4:
                        physioMenuexit = true;
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
    
    public void physiolist() { 
        try{
            // Load physiotherapists from JSON file
            List<Physio> physios = PhysioFileHandler.readPhysiosFromFile();

            if (physios != null && !physios.isEmpty()) {
                System.out.println("\n Physios Details \n");
                System.out.println("--------------------------------------------------------------------------------------------------------");
                System.out.printf("%-5s%-25s%-25s%-50s%n", "Physio ID", "Full Name", "Expertise Area", "Treatments");
                System.out.println("--------------------------------------------------------------------------------------------------------");

                for (Physio physio : physios) {
                    boolean firstRow = true;

                    for (Map.Entry<String, List<String>> entry : physio.getExpertiseAreas().entrySet()) {
                        String expertiseArea = entry.getKey();
                        String treatments = String.join(", ", entry.getValue()); // Convert list to a string

                        if (firstRow) {
                            // Print full row for the first expertise area
                            System.out.printf("%-5s%-25s%-25s%-50s%n", 
                                physio.getId(), 
                                physio.getFullName(), 
                                expertiseArea, 
                                treatments);
                            firstRow = false;
                        } else {
                            // Print only expertise area and treatments in a new row
                            System.out.printf("%-5s%-25s%-25s%-50s%n", 
                                "", // Empty ID to avoid duplicate
                                "", // Empty Name to avoid duplicate
                                expertiseArea, 
                                treatments);
                        }
                    }
                }
                System.out.println("--------------------------------------------------------------------------------------------------------");
            } else {
                System.out.println("No physios found.");
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    
    // Method to get user input and save physiotherapist
    public void addPhysio() {
        try{
            boolean addMore = true;

            while (addMore) {
                int physioId = generateNextPhysiotId();

                System.out.print("\nEnter Full Name: ");
                String fullName = scanner.nextLine().trim();

                System.out.print("Enter Address: ");
                String address = scanner.nextLine().trim();

                System.out.print("Enter Phone Number: ");
                String phone = scanner.nextLine().trim();

                Map<String, List<String>> expertiseAreas = new HashMap<>();

                while (true) {
                    System.out.print("Enter Expertise Area (or type 'done' to finish): ");
                    String expertiseArea = scanner.nextLine().trim();

                    if (expertiseArea.equalsIgnoreCase("done")) break;

                    System.out.print("Enter Treatments for " + expertiseArea + " (comma-separated): ");
                    String[] treatmentsArray = scanner.nextLine().trim().split(",");

                    List<String> treatments = new ArrayList<>();
                    for (String treatment : treatmentsArray) {
                        treatments.add(treatment.trim());  
                    }
                    expertiseAreas.put(expertiseArea, treatments);
                }

                // Create Physio object
                Physio newPhysio = new Physio(physioId, fullName, address, phone, expertiseAreas);

                // Load existing physios, add new physio, and save
                List<Physio> physios = PhysioFileHandler.readPhysiosFromFile();
                physios.add(newPhysio);
                PhysioFileHandler.savePhysiosToJson(physios);

                System.out.println("\nPhysiotherapist added successfully!");
                addMore = false;
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred while adding the physio: " + e.getMessage());
        }
        
    }
    
    // this function is used to set a new ID to the newly added physio
    private int generateNextPhysiotId() {
        List<Physio> physios = PhysioFileHandler.readPhysiosFromFile();
        int maxId = 0;

        // Find the maximum existing ID
        for (Physio physio : physios) {
            try {
                int currentId = physio.getId();
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Physio ID format: " + physio.getId());
            }
        }

        // Increment the maximum ID by 1
        return maxId + 1;
    }

    
    public void isExit(){
        // Ask if the user wants to exit or continue
        System.out.print("\nDo you want to exit the program? (Y/N): ");
        String response = scanner.next();
        if (response.equalsIgnoreCase("y")) {
            mainController.exit = true; 
            physioMenuexit = true;
            System.out.println("Exiting the program. Goodbye!");
        }
    }
    
}
