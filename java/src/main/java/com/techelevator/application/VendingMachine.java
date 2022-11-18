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

    private static Logger logger = new Logger( "audit.txt");

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
                    System.out.println("Please insert cash");
                    double cash = Double.parseDouble(userInputScan.nextLine());

                    if (cash == (int) cash) {
                        totalCash += cash;
                        logger.write("MONEY FED: " + "$" + cash + " " + "CURRENT BALANCE " + "$" + totalCash);
                        System.out.println("Your current cash is: " + totalCash);

                    } else {
                        System.out.println("Please insert a valid whole dollar amount");
                        continue;
                    }
                    System.out.println("Are you finished inserting money? (Y/N)");
                    String yesOrNo = userInputScan.nextLine();
                    if (yesOrNo.equals("Y")) {
                        System.out.println("Your total cash is: " + totalCash);
                        System.out.println();
                        waitForMoney = false;
                    }

                }
            } else if (choice.equals("Select Item")) {
                UserOutput.displayStock();
                System.out.println("Please select item to purchase: ");
                String itemSelected = userInputScan.nextLine().toUpperCase();

                if (itemLocations.contains(itemSelected)) {
                    for (int i = 0; i < stockItems.size(); i++) {
                        String stockLocation = stockItems.get(i).getLocation();
                        int stockQty = stockItems.get(i).getStockQty();
                        if (itemSelected.equals(stockLocation) && stockQty > 0 && totalCash >= stockItems.get(i).getPrice()) {
                            System.out.println("Thanks for choosing " + stockItems.get(i).getItemName());
                            System.out.println(stockItems.get(i).getMessage());
                            double remainingCash = stockItems.get(i).purchase(totalCash);
                            stockQty = stockItems.get(i).getStockQty();
                            if(purchaseCounter % 2 == 0) {
                                remainingCash = remainingCash + 1;
                            }
                            purchaseCounter++;
                            logger.write(stockItems.get(i).getItemName() + " " + itemSelected + " " + "CURRENT BALANCE: " + "$" + totalCash + " " + "REMAINING BALANCE: " + "$" + remainingCash);
                            totalCash = remainingCash;
                            System.out.println(stockQty);

                            System.out.println("You now have " + totalCash);
                        } else if (itemSelected.equals(stockLocation) && stockQty == 0) {
                            System.out.println("Your selected item is no longer available");
                            break;
                        } else if (totalCash < stockItems.get(i).getPrice()) {
                            System.out.println("Sorry, you need to insert cash first!");
                            System.out.println();
                            break;
                        }

                    }
                }


            } else if (choice.equals("Finish")) {
                System.out.println(change(totalCash));
                totalCash = 0;
                break;
            }

        }

    } public static String change (double finalChange) {
        double change = finalChange * 100;
        int dollars = (int)(change/100);
        change = change %100;
        int quarters = (int)(change / 25);
        change = change %25;
        int dimes = (int)(change /10);
        change = change %10;
        int nickels = (int)(change / 5);

        double backToZero = 0;

        logger.write("CHANGE GIVEN: " + "$" + finalChange + " " + "$" + backToZero);

        return "Your change is " + dollars + " dollars, " + quarters +" quarters, " + dimes + " dimes, and " + nickels + " nickels.";

    }
}

