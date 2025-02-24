/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package physiobookingsystem.controllers;

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
        // Load physiotherapists from JSON file
        List<Physio> physios = PhysioFileHandler.readPhysiosFromFile();

        if (physios != null && !physios.isEmpty()) {
            System.out.println("\n Physios Details \n");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            System.out.printf("%-15s%-20s%-30s%-40s%n", "Physio ID", "Full Name", "Expertise Area", "Treatments");
            System.out.println("--------------------------------------------------------------------------------------------------------");

            for (Physio physio : physios) {
                boolean firstRow = true;

                for (Map.Entry<String, List<String>> entry : physio.getExpertiseAreas().entrySet()) {
                    String expertiseArea = entry.getKey();
                    String treatments = String.join(", ", entry.getValue()); // Convert list to a string

                    if (firstRow) {
                        // Print full row for the first expertise area
                        System.out.printf("%-5s%-20s%-30s%-50s%n", 
                            physio.getId(), 
                            physio.getFullName(), 
                            expertiseArea, 
                            treatments);
                        firstRow = false;
                    } else {
                        // Print only expertise area and treatments in a new row
                        System.out.printf("%-5s%-20s%-30s%-50s%n", 
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
