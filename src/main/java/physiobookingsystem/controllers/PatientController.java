/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package physiobookingsystem.controllers;

import java.util.ArrayList;
import java.util.Scanner; 
import physiobookingsystem.models.Patient;
import physiobookingsystem.utilities.PatientFileHandler;

/**
 *
 * @author  Lakshman 23086585
 */
public class PatientController {
    private MainController mainController;
    Scanner scanner = new Scanner(System.in);
    public boolean patientMenuexit;
    
    public PatientController(MainController mainController) {
        this.mainController = mainController;
        this.scanner = new Scanner(System.in);
        this.patientMenuexit = false;
    }
    
    public void start() {
        while (!patientMenuexit) {
            System.out.println("\nManage Patients:");
            System.out.println("1. Add a Patient");
            System.out.println("2. Remove a Patient");
            System.out.println("3. View All Patients");
            System.out.println("4. Return to Main Menu");
            System.out.print("Enter your choice: ");
            try{
                int choice = new java.util.Scanner(System.in).nextInt();

                switch (choice) {
                    case 1:
                        System.out.println("Entered 1");
                        break;
                    case 2:
                         System.out.println("Entered 2");
                        break;
                    case 3:
                        patientlist();
                        isExit();
                        break;
                    case 4:
                        patientMenuexit = true;
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
    
    public void patientlist(){ 
        // Load patients from file
        ArrayList<Patient> patients = PatientFileHandler.readPatientsFromFile();
        
        if (patients != null && !patients.isEmpty()) {
            System.out.println("\n Patients Details \n");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            System.out.printf("%-15s%-20s%-40s%-15s%n", "Patient ID", "Full Name", "Address", "Phone");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            for (Patient patient : patients) {
                System.out.printf("%-15s%-20s%-40s%-15s%n", patient.getId(), patient.getFullName(), patient.getAddress(), patient.getPhone());
            }
            System.out.println("--------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println("No patients found.");
        }
        
    }
    
    public void isExit(){
        // Ask if the user wants to exit or continue
        System.out.print("\nDo you want to exit the program? (Y/N): ");
        String response = scanner.next();
        if (response.equalsIgnoreCase("y")) {
            mainController.exit = true; 
            patientMenuexit = true;
            System.out.println("Exiting the program. Goodbye!");
        }
    }
}
