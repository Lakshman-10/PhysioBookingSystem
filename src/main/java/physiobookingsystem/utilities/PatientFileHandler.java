/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package physiobookingsystem.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import physiobookingsystem.models.Patient;

/**
 *
 * @author  Lakshman 23086585
 */
public class PatientFileHandler {
    private static final String FILE_PATH = "D:\\herts\\Sem B\\PSE\\PhysioBookingSystem\\src\\main\\java\\physiobookingsystem\\files\\patients.txt";
    //private static final String FILE_PATH = "files/patients.txt".replace("\\", "/");

    // Reads patient data from the file and returns a list of Patient objects
    public static ArrayList<Patient> readPatientsFromFile() {
        ArrayList<Patient> patients = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", 3); // Split by comma
                if (data.length == 3) { // Ensure all fields are present
                    int id = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    String address = data[2].substring(0, data[2].lastIndexOf(',')).trim();
                    String phone = data[2].substring(data[2].lastIndexOf(',') + 1).trim();
                    patients.add(new Patient(id, name, address, phone));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading patients file: " + e.getMessage());
        }
        return patients;
    }

    // Save a patient to the file
    public static boolean savePatientToFile(Patient patient) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) { // Append mode
            File file = new File(FILE_PATH);

            // Check if the file exists and is not empty
            boolean isEmpty = !file.exists() || file.length() == 0;

            // Write the patient data to a new line
            String patientData = patient.getId() + "," + patient.getFullName() + "," + patient.getAddress() + "," + patient.getPhone();
            if (!isEmpty) {
               writer.newLine();
            }// Add a newline at the end
            writer.write(patientData);
            return true; // Successfully saved
        } catch (IOException e) {
            System.out.println("Error saving patient to file: " + e.getMessage());
            return false; // Failed to save
        }
    }
}
