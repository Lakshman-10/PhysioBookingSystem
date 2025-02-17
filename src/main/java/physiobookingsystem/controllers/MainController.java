/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package physiobookingsystem.controllers;

/**
 *
 * @author USER
 */
public class MainController {
    private PatientController patientController;
    
    public MainController() {
        this.patientController = new PatientController();
    }
    
    public void displayWelcomeMessage() {
        System.out.println("############################################");
        System.out.println("#         Welcome to Boost Physio Clinic!  #");
        System.out.println("############################################");
        System.out.println("Your trusted solution for health and wellness!");
        System.out.println();
    }
    
    public void start() {
        boolean exit = false;
        while (!exit) {
            
            System.out.println("\nMain Menu:");
            System.out.println("1. Manage Patients");
            System.out.println("2. Manage Appointments");
            System.out.println("3. Generate Reports");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            
            try{
                int choice = new java.util.Scanner(System.in).nextInt();

                switch (choice) {
                    case 1:
                        patientController.start();
                        break;
                    case 2:
                         System.out.println("Entered 2");
                        break;
                    case 3:
                         System.out.println("Entered 3");
                        break;
                    case 4:
                        exit = true;
                        System.out.println("Entered 4");
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

}
