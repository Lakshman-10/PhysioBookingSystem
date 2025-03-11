/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package physiobookingsystem.controllers;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import physiobookingsystem.models.Patient;
import physiobookingsystem.utilities.PatientFileHandler;

/**
 *
 * @author USER
 */
public class PatientControllerTest {
    
    private PatientController patientController;
    private List<Patient> originalPatients; // Store original data for restoration
    
    public PatientControllerTest() {
    }

    @BeforeEach
    void setUp() {
        MainController mainController = new MainController(); 
        patientController = new PatientController(mainController);
        
        // Save original data before modifying the file
        originalPatients = PatientFileHandler.readPatientsFromFile();
    }

    @AfterEach
    void tearDown() {
        // Restore original data after each test
        PatientFileHandler.savePatientsToJson(originalPatients);
    }
     
    //test to view all patients
    @Test
    void testViewAllPatients() {
        List<Patient> patients = PatientFileHandler.readPatientsFromFile();
        assertNotNull(patients, "Patient list should not be null");
        assertTrue(patients.size() > 0, "There should be at least one patient");
    } 
    
    //test to add a valid patient
    @Test
    void testAddPatient() {  
        // Create a new patient and add it to the list
        int id = patientController.generateNextPatientId();
        Patient newPatient = new Patient(id, "Test Name1", "Test Address1", "1234567890");
        patientController.savePatient(newPatient);

        List<Patient> patients = PatientFileHandler.readPatientsFromFile();
        assertFalse(patients.isEmpty(), "Patient list should not be empty");

        boolean patientExists = patients.stream().anyMatch(p -> p.getFullName().equals("Test Name1"));
        assertTrue(patientExists, "The new patient should be in the saved list");
    }
    
    // Test Adding a Patient with Missing Name
    @Test
    void testAddPatient_MissingName() {
        int id = patientController.generateNextPatientId();
        Patient newPatient = new Patient(id, "", "Test Address2", "1234567890"); // Empty name
        boolean result = patientController.savePatient(newPatient);
        assertFalse(result, "Patient should not be added without a name");
    }
    
    // Test Adding a Patient with Missing Address
    @Test
    void testAddPatient_MissingAddress() {
        int id = patientController.generateNextPatientId();
        Patient newPatient = new Patient(id, "Test Name3", " ", "1234567890"); // Empty address
        boolean result = patientController.savePatient(newPatient);
        assertFalse(result, "Patient should not be added without a address");
    }
    
    // Test Adding a Patient with Missing Phone Number
    @Test
    void testAddPatient_MissingPhone() {
        int id = patientController.generateNextPatientId();
        Patient newPatient = new Patient(id, "Test Name4", "Test Address4", ""); // Empty phone number
        boolean result = patientController.savePatient(newPatient);
        assertFalse(result, "Patient should not be added without a phone number");
    }
    
    // Test Adding a Patient with Duplicate Id
    @Test
    void testAddPatient_DuplicateId() {
        int id = patientController.generateNextPatientId()-1;
        Patient newPatient = new Patient(id, "Test Name1", "Test Address1", "077098765"); // Empty phone number
        boolean result = patientController.savePatient(newPatient);
        assertFalse(result, "Patient should not be added with duplicate ID");
    }
    
    //test to delete a valid patient
    @Test
    void testDeletePatient_Success() {
        List<Patient> patientsBefore = PatientFileHandler.readPatientsFromFile();
        assertEquals(originalPatients.size(), patientsBefore.size(), "Initial patient list should match the count of the patients");

        // deletion 
        patientsBefore.removeIf(p -> p.getId() == 1);
        PatientFileHandler.savePatientsToJson(patientsBefore);

        List<Patient> patientsAfter = PatientFileHandler.readPatientsFromFile();
        assertEquals(patientsBefore.size(), patientsAfter.size(), "Patient list should match the count of the patient after deletion");

        boolean patientDeleted = patientsAfter.stream().noneMatch(p -> p.getId() == 1);
        assertTrue(patientDeleted, "Patient with ID 1 should be deleted");
    }
    
    //test to delete a patient with a incorrect ID
    @Test
    void testDeletePatient_NotFound() {
        List<Patient> patientsBefore = PatientFileHandler.readPatientsFromFile();
        assertEquals(originalPatients.size(), patientsBefore.size(), "Initial patient list should match the count of the patients");

        // Attempt to delete a non-existent patient
        patientsBefore.removeIf(p -> p.getId() == 99);
        PatientFileHandler.savePatientsToJson(patientsBefore);

        List<Patient> patientsAfter = PatientFileHandler.readPatientsFromFile();
        assertEquals(patientsBefore.size(), patientsAfter.size(), "Patient list should remain unchanged");
    }
    
}
