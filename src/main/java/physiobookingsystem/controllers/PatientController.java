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
        patientMenuexit = false;
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
                        addPatient();
                        isExit();
                        break;
                    case 2:
                         deletePatient();
                         isExit();
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
    
    public void addPatient() {
    try {
        // Generate auto-incremented ID
        int newId = generateNextPatientId();

        System.out.print("\nEnter Full Name: ");
        String fullName = scanner.next();

        System.out.print("Enter Address: ");
        scanner.nextLine(); // Consume leftover newline
        String address = scanner.nextLine();

        System.out.print("Enter Phone Number: ");
        String phone = scanner.next();

        // Create a new patient object
        Patient newPatient = new Patient(newId, fullName, address, phone);

        // Save the patient to the file
        if (PatientFileHandler.savePatientToFile(newPatient)) {
            System.out.println("Patient added successfully and saved to file!");
        } else {
            System.out.println("Error: Could not save the patient to the file.");
        }
    } catch (Exception e) {
        System.out.println("An error occurred while adding the patient: " + e.getMessage());
    }
}
    
    // this function is used to set a new ID to the newly added patient
    private int generateNextPatientId() {
        ArrayList<Patient> patients = PatientFileHandler.readPatientsFromFile();
        int maxId = 0;

        // Find the maximum existing ID
        for (Patient patient : patients) {
            try {
                int currentId = patient.getId();
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Patient ID format: " + patient.getId());
            }
        }

        // Increment the maximum ID by 1
        return maxId + 1;
    }
    
    //this function is used to delete a patients
    public void deletePatient() {
        boolean deleteMore = true;

        while (deleteMore) {
            System.out.print("\nEnter the Patient ID to delete: ");
            String patientId = scanner.next();

            // Load all patients from the file
            ArrayList<Patient> patients = PatientFileHandler.readPatientsFromFile();
            Patient patientToDelete = null;

            // Search for the patient by ID
            for (Patient patient : patients) {
                if (patient.getId() == Integer.parseInt(patientId)) {
                    patientToDelete = patient;
                    break;
                }
            }

            if (patientToDelete != null) {
                // Display the patient details                
                System.out.println("\n Patient Details \n");
                System.out.println("--------------------------------------------------------------------------------------------------------");
                System.out.printf("%-15s%-20s%-40s%-15s%n", "Patient ID", "Full Name", "Address", "Phone");
                System.out.println("--------------------------------------------------------------------------------------------------------");
                System.out.printf("%-15s%-20s%-40s%-15s%n", patientToDelete.getId(), patientToDelete.getFullName(), patientToDelete.getAddress(), patientToDelete.getPhone());
                System.out.println("--------------------------------------------------------------------------------------------------------");

                // Ask for confirmation to delete
                System.out.print("Are you sure you want to delete this patient? (Y/N): ");
                String confirmation = scanner.next();

                if (confirmation.equalsIgnoreCase("y")) {
                    // Remove the patient and update the file
                    patients.remove(patientToDelete);
                    if (PatientFileHandler.saveAllPatientsToFile(patients)) {
                        System.out.println("Patient deleted successfully!");
                    } else {
                        System.out.println("Error: Could not update the file.");
                    }
                } else {
                    System.out.println("Patient deletion canceled.");
                }
            } else {
                System.out.println("Patient with ID " + patientId + " not found.");
            }

            // Ask if the user wants to delete another patient
            System.out.print("\nDo you want to delete another patient? (Y/N): ");
            String response = scanner.next();
            deleteMore = response.equalsIgnoreCase("y");
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
