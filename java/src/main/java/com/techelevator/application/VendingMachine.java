package com.techelevator.application;

import com.techelevator.models.Stock;
import com.techelevator.ui.UserInput;
import com.techelevator.ui.UserOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachine {
    private static List<Stock> stockItems = new ArrayList<>();

    private static Scanner userInputScan = new Scanner(System.in);

    public void loadFile() {
        File file = new File("catering.csv");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] lineArr = line.split("\\,");
                Stock stock = new Stock(lineArr[0], (lineArr[1]), Double.parseDouble(lineArr[2]), (lineArr[3]));
                stockItems.add(stock);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Problem with file");
        }
    }

    public void run() {
        loadFile();

        while (true) {
            UserOutput.displayHomeScreen();
            String choice = UserInput.getHomeScreenOption();

            if (choice.equals("display")) {
                for (Stock stock : stockItems) {
                    System.out.println(stock);
                }
            } else if (choice.equals("purchase")) {
                // make a purchase
            } else if (choice.equals("exit")) {
                // good bye
                break;
            }
        }

    }

    public static void purchaseMenu() {

        boolean stay = true;
        while (stay) {
            UserOutput.displayPurchaseMenuOptions();
            String choice = UserInput.getPurchaseMenuOptions();

            if(choice.equals("M")) {

                boolean waitForMoney = true;
                double totalCash = 0.0;

                while (waitForMoney) {
                    System.out.println("Please insert cash");
                    double cash = Double.parseDouble(userInputScan.nextLine());

                    if(cash == (int)cash) {
                        totalCash += cash;

                        System.out.println("Your total cash is:" + totalCash);
                } else {
                        System.out.println("Please insert a valid whole dollar amount");
                    }
                    System.out.println("Are you finished inserting money? (Y/N)");
                    String yesOrNo = userInputScan.nextLine();
                    if(yesOrNo.equals("Y")) {
                        waitForMoney = false;
                    }

//            } else if(choice.equals("S")) {
//                String teamName = UserInput.getTeamName();
//                for(Team team: teams) {
//
//                    if(team.getName().equalsIgnoreCase(teamName)) {
//                        int currentLosses = team.getLosses();
//                        team.setWins(currentLosses + 1);
//                    }
//                }
//
//            } else if (choice.equals("exit")) {
//                stay = false;
//
            }
        }
        }
    }
}
