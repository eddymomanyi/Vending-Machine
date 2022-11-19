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
                itemLocations.add(lineArr[0]);

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
            System.out.println("Problem with file");
        }
    }

    public static List<Item> getStockItems() {
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

    public static void purchaseMenu() {

        boolean stay = true;
        while (stay) {
            UserOutput.displayPurchaseMenuOptions();
            String choice = UserInput.getPurchaseMenuOptions();

            if (choice.equals("Money Accepted")) {
                boolean waitForMoney = true;
                while (waitForMoney) {
                    UserOutput.displayMessage("Please insert cash");
                    try {
                        double cash = Double.parseDouble(userInputScan.nextLine());
                        if (cash == 0) {
                            waitForMoney = false;
                        }
                        if (cash == 1.0 || cash == 5.0 || cash == 10.0 || cash == 20.0) {
                            funds.setTotalCash(funds.getTotalCash() + cash);
                            logger.write("MONEY FED: " + "$" + cash + " " + "CURRENT BALANCE " + "$" + funds.getTotalCash());
                            UserOutput.displayMessage("Your current cash is: " + funds.getTotalCash());
                        } else {
                            UserOutput.displayMessage("Please only insert whole bills (ex. $1, $5, $10, or $20)");
                            continue;
                        }
                    } catch (Exception e) {
                        UserOutput.displayMessage("Sorry, please insert cash");
                        continue;
                    }

                    UserOutput.displayMessage("Are you finished inserting money? (Y/N)");
                    String yesOrNo = userInputScan.nextLine().toUpperCase();
                    if (yesOrNo.equals("Y")) {
                        UserOutput.displayMessage("Your total cash is: " + funds.getTotalCash());
                        waitForMoney = false;
                    } else if (!yesOrNo.equals("N")) {
                        UserOutput.displayMessage("Sorry, incorrect response!");
                    }
                }
            } else if (choice.equals("Select Item")) {
                UserOutput.displayStock();
                UserOutput.displayMessage("Please select item to purchase: ");
                String itemSelected = userInputScan.nextLine().toUpperCase();

                if (itemLocations.contains(itemSelected)) {
                    for (int i = 0; i < stockItems.size(); i++) {
                        String stockLocation = stockItems.get(i).getLocation();
                        Item currentItem = stockItems.get(i);
                        if (itemSelected.equals(stockLocation) && inStock(currentItem) && funds.getTotalCash() >= currentItem.getPrice()) {
                            UserOutput.displayMessage("Thanks for choosing " + currentItem.getItemName());
                            UserOutput.displayMessage(currentItem.getMessage());
                            double remainingCash = currentItem.purchase(funds.getTotalCash(), purchaseCounter);
                            purchaseCounter++;
                            logger.write(currentItem.getItemName() + " " + itemSelected + " " + "CURRENT BALANCE: " + "$" + funds.getTotalCash() + " " + "REMAINING BALANCE: " + "$" + remainingCash);
                            funds.setTotalCash(remainingCash);
                            System.out.println(currentItem.getStockQty());
                            UserOutput.displayMessage("You now have " + funds.getTotalCash());
                        } else if (itemSelected.equals(stockLocation) && !inStock(currentItem)) {
                            UserOutput.displayMessage("Your selected item is no longer available");
                            break;
                        } else if (funds.getTotalCash() < currentItem.getPrice()) {
                            UserOutput.displayMessage("Sorry, you need to insert cash first!");
                            break;
                        }
                    }
                } else {
                    UserOutput.displayMessage("Sorry, please select item from the list(e.g. A1)");
                }
            } else if (choice.equals("Finish")) {
                UserOutput.displayMessage(funds.change(funds.getTotalCash()));
                funds.setTotalCash(0);
                break;
            } else {
                UserOutput.displayMessage("Sorry, please select M, S, or F");
            }
        }
    }

    public static boolean inStock(Item item) {
        return item.getStockQty() > 0;
    }

}

