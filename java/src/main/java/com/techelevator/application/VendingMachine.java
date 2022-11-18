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
    private static List<Item> stockItems = new ArrayList<>();

    private static Scanner userInputScan = new Scanner(System.in);

    private static Item item;

    private static List<String> itemLocations = new ArrayList<>();

    private static double totalCash = 0.0;

    private static int purchaseCounter;

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
                for (Item item : stockItems) {
                    System.out.println(item);
                }
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
                        if (cash == (int) cash && (cash == 1.0 || cash == 5.0 || cash == 10.0 || cash == 20.0)) {
                            totalCash += cash;
                            logger.write("MONEY FED: " + "$" + cash + " " + "CURRENT BALANCE " + "$" + totalCash);
                            UserOutput.displayMessage("Your current cash is: " + totalCash);

                        } else {
                            UserOutput.displayMessage("Please insert bills only at a time (ex. $1, $5, $10, or $20)");
                            continue;
                        }
                    } catch (Exception e) {
                        UserOutput.displayMessage("Sorry, please insert cash");
                        continue;
                    }
                    UserOutput.displayMessage("Are you finished inserting money? (Y/N)");
                    String yesOrNo = userInputScan.nextLine().toUpperCase();
                    if (yesOrNo.equals("Y")) {
                        UserOutput.displayMessage("Your total cash is: " + totalCash);
                        waitForMoney = false;
                    } else if (!yesOrNo.equals("N")) {
                        UserOutput.displayMessage("Please select Y or N");
                    }


                }
            } else if (choice.equals("Select Item")) {
                UserOutput.displayStock();
                UserOutput.displayMessage("Please select item to purchase: ");
                String itemSelected = userInputScan.nextLine().toUpperCase();

                if (itemLocations.contains(itemSelected)) {
                    for (int i = 0; i < stockItems.size(); i++) {
                        String stockLocation = stockItems.get(i).getLocation();
                        int stockQty = stockItems.get(i).getStockQty();
                        Item currentItem = stockItems.get(i);
                        if (itemSelected.equals(stockLocation) && stockQty > 0 && totalCash >= currentItem.getPrice()) {
                            UserOutput.displayMessage("Thanks for choosing " + currentItem.getItemName());
                            UserOutput.displayMessage(currentItem.getMessage());
                            double remainingCash = currentItem.purchase(totalCash, purchaseCounter);
                            purchaseCounter++;
                            logger.write(currentItem.getItemName() + " " + itemSelected + " " + "CURRENT BALANCE: " + "$" + totalCash + " " + "REMAINING BALANCE: " + "$" + remainingCash);
                            totalCash = remainingCash;
                            System.out.println(currentItem.getStockQty());

                            UserOutput.displayMessage("You now have " + totalCash);
                        } else if (itemSelected.equals(stockLocation) && stockQty == 0) {
                            UserOutput.displayMessage("Your selected item is no longer available");
                            break;
                        } else if (totalCash < currentItem.getPrice()) {
                            UserOutput.displayMessage("Sorry, you need to insert cash first!");
                            break;
                        }

                    }
                } else {
                    UserOutput.displayMessage("Sorry, please select item from the list(e.g. A1)");
                }


            } else if (choice.equals("Finish")) {
                UserOutput.displayMessage(change(totalCash));
                totalCash = 0;
                break;
            } else {
                UserOutput.displayMessage("Sorry, please select M, S, or F");
            }

        }

    }

    public static String change(double finalChange) {
        double change = finalChange * 100;
        int dollars = (int) (change / 100);
        change = change % 100;
        int quarters = (int) (change / 25);
        change = change % 25;
        int dimes = (int) (change / 10);
        change = change % 10;
        int nickels = (int) (change / 5);

        double backToZero = 0;

        logger.write("CHANGE GIVEN: " + "$" + finalChange + " " + "$" + backToZero);

        return "Your change is " + dollars + " dollars, " + quarters + " quarters, " + dimes + " dimes, and " + nickels + " nickels.";

    }
}

