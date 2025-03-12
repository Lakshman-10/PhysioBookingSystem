/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package physiobookingsystem.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import physiobookingsystem.models.Physio;
import physiobookingsystem.utilities.PhysioFileHandler;

/**
 *
 * @author Lakshman 23086585
 */
public class PhysioControllerTest {
    
    private PhysioController physioController;
    private List<Physio> originalPhysios; // Store original data for restoration
    
    public PhysioControllerTest() {
    }
     
    @BeforeEach
    public void setUp() {
        MainController mainController = new MainController(); 
        physioController = new PhysioController(mainController);
        
        // Save original data before modifying the file
        originalPhysios = PhysioFileHandler.readPhysiosFromFile();
    }
    
    @AfterEach
    public void tearDown() {
        // Restore original data after each test
        PhysioFileHandler.savePhysiosToJson(originalPhysios);
    }

     

    // test for view all physios
    @Test
    public void testPhysiolist() {
        List<Physio> physios = PhysioFileHandler.readPhysiosFromFile();
        assertNotNull(physios, "Physio list should not be null");
        assertTrue(physios.size() > 0, "There should be at least one physio");
    }

    //test for add a valid physio
    @Test
    public void testAddPhysio() {
        // Create a new patient and add it to the list
        int id = physioController.generateNextPhysiotId();
        Map<String, List<String>> expertiseAreas = new HashMap<>();
        expertiseAreas.put("Physiotherapy", Arrays.asList("Swedish Massage","Acupuncture"));
        expertiseAreas.put("Rehabilitation", Arrays.asList("Rehabilitation"));
        
        Physio newPhysio = new Physio(id, "Test Name1", "Test Address1", "1234567890", expertiseAreas);
        physioController.savePhysio(newPhysio);

        List<Physio> physios = PhysioFileHandler.readPhysiosFromFile();
        assertFalse(physios.isEmpty(), "Physio list should not be empty");

        boolean physioExists = physios.stream().anyMatch(p -> p.getFullName().equals("Test Name1"));
        assertTrue(physioExists, "The new physio should be in the saved list");
    }
    
    // Test Adding a Physio with Missing Name
    @Test
    void testAddPatient_MissingName() {
        int id = physioController.generateNextPhysiotId();
        Map<String, List<String>> expertiseAreas = new HashMap<>();
        expertiseAreas.put("Physiotherapy", Arrays.asList("Swedish Massage","Acupuncture"));
        expertiseAreas.put("Rehabilitation", Arrays.asList("Rehabilitation"));
        
        Physio newPhysio = new Physio(id, "", "Test Address2", "1234567890", expertiseAreas); // Empty name
        boolean result = physioController.savePhysio(newPhysio);
        assertFalse(result, "Physio should not be added without a name");
    }
    
    // Test Adding a Physio with Missing Address
    @Test
    void testAddPatient_MissingAddress() {
        int id = physioController.generateNextPhysiotId();
        Map<String, List<String>> expertiseAreas = new HashMap<>();
        expertiseAreas.put("Physiotherapy", Arrays.asList("Swedish Massage","Acupuncture"));
        expertiseAreas.put("Rehabilitation", Arrays.asList("Rehabilitation"));
        
        Physio newPhysio = new Physio(id, "Test Name3", " ", "1234567890", expertiseAreas); // Empty address
        boolean result = physioController.savePhysio(newPhysio);
        assertFalse(result, "Physio should not be added without a address");
    }
    
    // Test Adding a Physio with Missing Phone Number
    @Test
    void testAddPatient_MissingPhone() {
        int id = physioController.generateNextPhysiotId();
        Map<String, List<String>> expertiseAreas = new HashMap<>();
        expertiseAreas.put("Physiotherapy", Arrays.asList("Swedish Massage","Acupuncture"));
        expertiseAreas.put("Rehabilitation", Arrays.asList("Rehabilitation"));
        
        Physio newPhysio = new Physio(id, "Test Name4", "Test Address4", "", expertiseAreas); // Empty phone number
        boolean result = physioController.savePhysio(newPhysio);
        assertFalse(result, "Physio should not be added without a phone number");
    }
    
    // Test Adding a Physio with Duplicate Id
    @Test
    void testAddPatient_DuplicateId() {
        int id = physioController.generateNextPhysiotId()-1;
        Map<String, List<String>> expertiseAreas = new HashMap<>();
        expertiseAreas.put("Physiotherapy", Arrays.asList("Swedish Massage","Acupuncture"));
        expertiseAreas.put("Rehabilitation", Arrays.asList("Rehabilitation"));
        
        Physio newPhysio = new Physio(id, "Test Name1", "Test Address1", "077098765", expertiseAreas); // Duplicate Id
        boolean result = physioController.savePhysio(newPhysio);
        assertFalse(result, "Physio should not be added with duplicate ID");
    }
    
    //test to delete a valid patient
    @Test
    void testDeletePatient_Success() {
        List<Physio> physiosBefore = PhysioFileHandler.readPhysiosFromFile();
        assertEquals(originalPhysios.size(), physiosBefore.size(), "Initial physio list should match the count of the physios");

        // deletion 
        physiosBefore.removeIf(p -> p.getId() == 1);
        PhysioFileHandler.savePhysiosToJson(physiosBefore);

        List<Physio> patientsAfter = PhysioFileHandler.readPhysiosFromFile();
        assertEquals(physiosBefore.size(), patientsAfter.size(), "Physio list should match the count of the physio after deletion");

        boolean patientDeleted = patientsAfter.stream().noneMatch(p -> p.getId() == 1);
        assertTrue(patientDeleted, "Patient with ID 1 should be deleted");
    }
    
    //test to delete a patient with a incorrect ID
    @Test
    void testDeletePatient_NotFound() {
        List<Physio> physiosBefore = PhysioFileHandler.readPhysiosFromFile();
        assertEquals(originalPhysios.size(), physiosBefore.size(), "Initial physio list should match the count of the physios");

        // Attempt to delete a non-existent patient
        physiosBefore.removeIf(p -> p.getId() == 99);
        PhysioFileHandler.savePhysiosToJson(physiosBefore);

        List<Physio> patientsAfter = PhysioFileHandler.readPhysiosFromFile();
        assertEquals(physiosBefore.size(), patientsAfter.size(), "Physio list should remain unchanged");
    }
    
    
    
}
