/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package physiobookingsystem.utilities;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import physiobookingsystem.models.Timetable;

/**
 *
 * @author Lakshman 23086585
 */
public class TimetableFileHandlerTest {
    
    private List<Timetable> originalTimeSlots; 
    
    public TimetableFileHandlerTest() {
    }
    
    @BeforeEach
    public void setUp() {
        // Save original data before modifying the file
        originalTimeSlots = TimetableFileHandler.readTimetableFromFile();
    }
    
    @AfterEach
    public void tearDown() {
        // Restore original data after each test
        TimetableFileHandler.saveTimetableToJson(originalTimeSlots);
    }

    //Test read all the slots in the timetable
    @Test
    void testReadTimetableFromFile() {
        List<Timetable> slots = TimetableFileHandler.readTimetableFromFile(); 
        assertNotNull(slots, "Timetable slot list should not be null");
        assertTrue(slots.size() > 0, "There should be at least one slot");
    }
    
    //test with valid expertise area
    @Test
    void testGetSlotsByExpertise() {
        List<Timetable> expertiseSlots = TimetableFileHandler.getSlotsByExpertise("Physiotherapy");
        assertEquals(true, expertiseSlots.get(0).getExpertiseArea().contains("Physiotherapy"));
    }
    
    //test with invalid expertise area
    @Test
    void testGetSlotsByExpertise_invalid() {
        List<Timetable> expertiseSlots = TimetableFileHandler.getSlotsByExpertise("abc");
        assertEquals(0, expertiseSlots.size());
    }
    
    //test with valid physio ids
    @Test
    void testGetSlotsByPhysio() {
        List<Timetable> physioSlots = TimetableFileHandler.getSlotsByPhysio(Arrays.asList(1, 2));
        assertTrue(physioSlots.stream().allMatch(slot -> slot.getPhysioId() == 1 || slot.getPhysioId() == 2));
    }
    
    //test with invalid physio ids
    @Test
    void testGetSlotsByPhysio_invalid() {
        List<Timetable> physioSlots = TimetableFileHandler.getSlotsByPhysio(Arrays.asList(101, 102));
        assertEquals(0, physioSlots.size());    
    }
    
    // checking whether a slot can be booked for a valid patient
    @Test
    void testCanBookSlot_Success() {
        assertTrue(TimetableFileHandler.canBookSlot("1", "6"));
    }

    // checking whether a slot can be booked for a invalid bookingID
    @Test
    void testCanBookSlot_invalidBookingId() {
        assertFalse(TimetableFileHandler.canBookSlot("1", "1066"));
    }
    
    // checking whether a slot can be booked again by another patient
    @Test
    void testCanBookSlot_BookbyAnotherPatient() {
        assertFalse(TimetableFileHandler.canBookSlot("1", "5"));
    }
    
    // checking whether a slot can be booked by same patient at same timeslot
    @Test
    void testCanBookSlot_BookAnotherSameTimeSlot() {
        assertFalse(TimetableFileHandler.canBookSlot("2", "6"));
    }
    
    // checking whether a slot can be cancelled by the same patient who booked
    @Test
    void testCanCancelSlot_Success() {
        assertTrue(TimetableFileHandler.canCancelSlot("2", "5"));
    }

    // checking whether a slot can be cancelled by the different patient who didn't book
    @Test
    void testCanCancelSlot_Failed() {
        assertFalse(TimetableFileHandler.canCancelSlot("1", "5")); // Not booked by this patientId 1
    }
    
    // checking whether a slot can be cancelled but the slot is already attended
    @Test
    void testCanCancelSlot_Attended() {
        assertFalse(TimetableFileHandler.canCancelSlot("1", "1")); 
    }
    
    // checking whether a slot can be attended by the same patient who booked
    @Test
    void testCanAttendSlot_Success() {
        assertTrue(TimetableFileHandler.canAttendSlot("2", "5"));
    }
    
    // checking whether a slot can be attended by the different patient who didn't book
    @Test
    void testCanAttendSlot_AlreadyAttended() {
        assertFalse(TimetableFileHandler.canAttendSlot("1", "5")); // // Not booked by this patientId 1
    }
    
    // Only valid parameters will be passed to this function because parameteres are validated with above functions
    @Test
    void testUpdateBooking_Success() {
        assertTrue(TimetableFileHandler.updateBooking("2", "5", "Cancelled"));
    }
    
}
