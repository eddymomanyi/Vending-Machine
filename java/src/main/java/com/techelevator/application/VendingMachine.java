package com.techelevator.application;

import com.techelevator.models.*;
import com.techelevator.ui.UserInput;
import com.techelevator.ui.UserOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class VendingMachine {
    //List to hold all items in the vending machine
    private static List<Item> stockItems = new ArrayList<>();
    private static Funds funds = new Funds();
    private static Scanner userInputScan = new Scanner(System.in);
    //Creates reference for Item class which will contain a single Candy/Munchy/Drinky/Gum object
    private static Item item;
    //List containing all item locations (i.e. A1, B1, C1)
    private static List<String> itemLocations = new ArrayList<>();
    //Keeps track of total Balance
    private static double totalCash = 0.0;
    //Tracks the amount of purchases for the BOGODO  discount
    private static int purchaseCounter;
    //Creating logger object to write to audit file
    private static Logger logger = new Logger("audit.txt");

    public void loadFile() {
        File file = new File("catering1.csv");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] lineArr = line.split("\\,");
                String itemCategory = lineArr[3];
                //adds each item location to a list
                itemLocations.add(lineArr[0]);


                // reads each line and creates an object based on category type
                if (itemCategory.equals("Munchy")) {
                    item = new Munchy(lineArr[0], (lineArr[1]), Double.parseDouble(lineArr[2]), lineArr[3]);

                } else if (itemCategory.equals("Candy")) {
                    item = new Candy(lineArr[0], (lineArr[1]), Double.parseDouble(lineArr[2]), lineArr[3]);

                } else if (itemCategory.equals("Drink")) {
                    item = new Drink(lineArr[0], (lineArr[1]), Double.parseDouble(lineArr[2]), lineArr[3]);

                } else if (itemCategory.equals("Gum")) {
                    item = new Gum(lineArr[0], (lineArr[1]), Double.parseDouble(lineArr[2]), lineArr[3]);
                }

                stockItems.add(item);


            }

        } catch (FileNotFoundException e) {
            // exception for wrong file input
            System.out.println("Problem with file");
        }
    }

    public static List<Item> getStockItems() {
        // returns a list of every item from txt file
        return stockItems;
    }

    public void run() {
        loadFile();
        purchaseCounter = 1;

        while (true) {
            UserOutput.displayHomeScreen();
            String choice = UserInput.getHomeScreenOption();

            if (choice.equals("display")) {
                UserOutput.displayStock();
            } else if (choice.equals("purchase")) {
                purchaseMenu();

            } else if (choice.equals("exit")) {
                UserOutput.displayMessage("Goodbye");
                break;
            } else {
                UserOutput.displayMessage("Sorry, please select D, P, or E");
            }
        }

    }

    // level 2 menu for handling transactions
    public static void purchaseMenu() {

        boolean stay = true;
        // keeps customer in purchase menu
        while (stay) {
            UserOutput.displayPurchaseMenuOptions();
            String choice = UserInput.getPurchaseMenuOptions();

            if (choice.equals("Insert Money")) {
                boolean waitForMoney = true;
                // while loop to wait for customer to insert money
                while (waitForMoney) {
                    UserOutput.displayMessage("Please insert cash");
                    try {
                        // gets userinput - switches it to cash
                        double cash = Double.parseDouble(userInputScan.nextLine());
                        if (cash == 0) {
                            waitForMoney = false;
                        }
                        if (cash == 1.0 || cash == 5.0 || cash == 10.0 || cash == 20.0) { // only accepts these bill denominations
                            funds.setTotalCash(funds.getTotalCash() + cash); // updates funds
                            logger.write("MONEY FED: " + "$" + cash + " " + "CURRENT BALANCE " + "$" + funds.getTotalCash()); // writes funds to logger
                            UserOutput.displayMessage("Your current cash is: " + funds.getTotalCash());
                        } else {
                            // catch if user types in wrong numeric value (e.g. 7)
                            UserOutput.displayMessage("Please only insert whole bills (ex. $1, $5, $10, or $20)");
                            continue;
                        }
                    } catch (Exception e) {
                        // catch if user types in non-numeric value
                        UserOutput.displayMessage("Sorry, please insert cash");
                        continue;
                    }

                    UserOutput.displayMessage("Are you finished inserting money? (Y/N)");
                    String yesOrNo = userInputScan.nextLine().toUpperCase();
                    if (yesOrNo.equals("Y")) {
                        UserOutput.displayMessage("Your total cash is: " + funds.getTotalCash());
                        waitForMoney = false; // gets out of loop
                    } else if (!yesOrNo.equals("N")) {
                        UserOutput.displayMessage("Sorry, incorrect response!");
                    }
                }
            } else if (choice.equals("Select Item")) {
                UserOutput.displayStock(); // shows machine items
                UserOutput.displayMessage("Please select item to purchase: ");
                // forces all text to uppercase
                String itemSelected = userInputScan.nextLine().toUpperCase();

                // checks if the location user provided is in our list of locations
                if (itemLocations.contains(itemSelected)) {
                    // loops through stock list to choose item
                    for (int i = 0; i < stockItems.size(); i++) {
                        // variable for each item location in the loop
                        String stockLocation = stockItems.get(i).getLocation();
                        // creates item object for each item in the loop
                        Item currentItem = stockItems.get(i);
                        // checks if user input has correct location / item is in stock / user has inserted cash
                        if (itemSelected.equals(stockLocation) && inStock(currentItem) && funds.getTotalCash() >= currentItem.getPrice()) {
                            UserOutput.displayMessage("Thanks for choosing " + currentItem.getItemName());
                            // displays category comment, yum yum!
                            UserOutput.displayMessage(currentItem.getMessage());
                            // defines remaining cash for use in audit and updates stock quantity after purchase
                            double remainingCash = currentItem.purchase(funds.getTotalCash(), purchaseCounter);
                            // adds purchase to the counter for bogodo discount
                            purchaseCounter++;
                            logger.write(currentItem.getItemName() + " " + itemSelected + " " + "CURRENT BALANCE: " + "$" + funds.getTotalCash() + " " + "REMAINING BALANCE: " + "$" + remainingCash); // writes to the audit txt
                            // updates funds after transaction
                            funds.setTotalCash(remainingCash);
                            UserOutput.displayMessage("You now have " + String.format("%.2f", funds.getTotalCash())); // from stackoverflow, used to show cash to two decimal places

                            // catch for if the item is out of stock
                        } else if (itemSelected.equals(stockLocation) && !inStock(currentItem)) {
                            UserOutput.displayMessage("Your selected item is no longer available");
                            break;
                        } else if (itemSelected.equals(stockLocation) && funds.getTotalCash() < currentItem.getPrice()) {
                            UserOutput.displayMessage("Sorry, you need to insert cash first!");
                            break;
                        }
                    }
                } else {
                    UserOutput.displayMessage("Sorry, please select item from the list(e.g. A1)");
                }
            } else if (choice.equals("Finish")) {
                UserOutput.displayMessage(funds.change(funds.getTotalCash()));
                // resets the vending machine balance to zero after change is given
                funds.setTotalCash(0);
                break;
            } else {
                UserOutput.displayMessage("Sorry, please select M, S, or F");
            }
        }
    }

    public static boolean inStock(Item item) {
        return item.getStockQty() > 0;
    } // used to change stock quantity to a boolean

}

