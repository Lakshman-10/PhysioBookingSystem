/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package physiobookingsystem.controllers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import physiobookingsystem.models.Patient;
import physiobookingsystem.models.Physio;
import physiobookingsystem.models.Timetable;
import physiobookingsystem.utilities.PatientFileHandler;
import physiobookingsystem.utilities.PhysioFileHandler;
import physiobookingsystem.utilities.TimetableFileHandler;

/**
 *
 * @author Lakshman 23086585
 */
public class ReportController  extends JFrame {
    
    public ReportController() {
        setTitle("Physiotherapist Report");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Load data
        List<Physio> physios = PhysioFileHandler.readPhysiosFromFile();
        List<Timetable> timetableSlots = TimetableFileHandler.readTimetableFromFile();
        List<Patient> patients = PatientFileHandler.readPatientsFromFile();

        if (physios.isEmpty() || timetableSlots.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data available for generating the report.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Sort physiotherapists by number of attended appointments (Descending)
        physios.sort((p1, p2) -> {
            long attendedCount1 = timetableSlots.stream()
                    .filter(slot -> slot.getPhysioId() == p1.getId() && slot.getStatus().equalsIgnoreCase("Attended"))
                    .count();
            long attendedCount2 = timetableSlots.stream()
                    .filter(slot -> slot.getPhysioId() == p2.getId() && slot.getStatus().equalsIgnoreCase("Attended"))
                    .count();
            return Long.compare(attendedCount2, attendedCount1);
        });

        // Create a Scrollable Panel
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        // Generate UI for each physiotherapist
        for (Physio physio : physios) {
            JPanel physioPanel = new JPanel();
            physioPanel.setLayout(new BorderLayout());
            physioPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

            // Physiotherapist Name Label
            JLabel physioLabel = new JLabel(physio.getFullName());
            physioLabel.setFont(new Font("Arial", Font.BOLD, 18));
            physioLabel.setForeground(new Color(0, 102, 204));
            physioPanel.add(physioLabel, BorderLayout.NORTH);

            // Appointments Table
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Treatment");
            model.addColumn("Patient");
            model.addColumn("Appointment Time");
            model.addColumn("Status");

            // Fetch appointments for this physiotherapist
            List<Timetable> physioAppointments = timetableSlots.stream()
                    .filter(slot -> slot.getPhysioId() == (physio.getId()))
                    .collect(Collectors.toList());

            if (physioAppointments.isEmpty()) {
                model.addRow(new Object[]{"No Appointments", "", "", ""});
            } else {
                for (Timetable slot : physioAppointments) {
                    String patientName = getPatientNameById(Integer.toString(slot.getPatientId()), patients);
                    model.addRow(new Object[]{slot.getTreatment(), patientName, slot.getDate() +"  " + slot.getTime(), slot.getStatus()});
                }
            }

            JTable table = new JTable(model);
            table.setRowHeight(25);
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            JScrollPane tableScroll = new JScrollPane(table);
            tableScroll.setPreferredSize(new Dimension(850, table.getRowHeight() * (table.getRowCount() + 1)));

            physioPanel.add(tableScroll, BorderLayout.CENTER);
            mainPanel.add(physioPanel);
        }

        setVisible(true);
    }

    // method to get patient's name by ID
    private static String getPatientNameById(String patientId, List<Patient> patients) {
        return patients.stream()
                .filter(patient -> patient.getId() == Integer.parseInt(patientId))
                .map(Patient::getFullName)
                .findFirst()
                .orElse("-");
    }
}
