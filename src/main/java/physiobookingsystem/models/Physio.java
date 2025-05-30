/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package physiobookingsystem.models;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Lakshman 23086585
 */
public class Physio {

    private int id;
    private String fullName;
    private String address;
    private String phone;
    private Map<String, List<String>> expertiseAreas; // Stores expertise & treatments
    
    public Physio() {}
    
    public Physio(int id, String fullName, String address, String phone, Map<String, List<String>> expertiseAreas) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.expertiseAreas = expertiseAreas;
    }

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
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    
    /**
     * @return the expertiseAreas
     */
    public Map<String, List<String>> getExpertiseAreas() {
        return expertiseAreas;
    }

    /**
     * @param expertiseAreas the expertiseAreas to set
     */
    public void setExpertiseAreas(Map<String, List<String>> expertiseAreas) {
        this.expertiseAreas = expertiseAreas;
    }
 
}
