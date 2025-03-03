/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package physiobookingsystem.models;

/**
 *
 * @author Lakshman 23086585
 */
public class Timetable {
    private int id;  
    private int physioId;  
    private int patientId;  
    private String status;  
    private String date;
    private String time;
    private String expertiseArea;
    private String treatment;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the physioId
     */
    public int getPhysioId() {
        return physioId;
    }

    /**
     * @param physioId the physioId to set
     */
    public void setPhysioId(int physioId) {
        this.physioId = physioId;
    }

    /**
     * @return the patientId
     */
    public int getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the expertiseArea
     */
    public String getExpertiseArea() {
        return expertiseArea;
    }

    /**
     * @param expertiseArea the expertiseArea to set
     */
    public void setExpertiseArea(String expertiseArea) {
        this.expertiseArea = expertiseArea;
    }

    /**
     * @return the treatment
     */
    public String getTreatment() {
        return treatment;
    }

    /**
     * @param treatment the treatment to set
     */
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
}
