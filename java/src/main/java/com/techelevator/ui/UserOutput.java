package com.techelevator.ui;

import com.techelevator.application.VendingMachine;
import com.techelevator.models.Item;


/**
 * Responsibilities: This class should handle formatting and displaying ALL
 * messages to the user
 * 
 * Dependencies: None
 */
public class UserOutput
{

    public static void displayMessage(String message)
    {
        System.out.println();
        System.out.println(message);
        System.out.println();
    }

    public static void displayHomeScreen()
    {
        System.out.println();
        System.out.println("***************************************************");
        System.out.println("                      Home");
        System.out.println("***************************************************");
        System.out.println();
    }

    public static void displayPurchaseMenuOptions() {
        System.out.println("Type (M) to insert money");
        System.out.println("Type (S) to select item");
        System.out.println("Type (F) to finish transaction");
}

    public static void displayStock(){
        for (Item item : VendingMachine.getStockItems()) {
            System.out.println(item);
        }
    }
}
