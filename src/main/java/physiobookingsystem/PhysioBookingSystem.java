/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package physiobookingsystem;

import physiobookingsystem.controllers.MainController;

/**
 *
 * @author  Lakshman 23086585
 */
public class PhysioBookingSystem {

    public static void main(String[] args) {
        MainController mainController = new MainController();
        
        mainController.displayWelcomeMessage();
        mainController.start();
    }
}
