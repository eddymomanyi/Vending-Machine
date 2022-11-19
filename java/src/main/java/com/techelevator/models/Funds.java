package com.techelevator.models;

import com.techelevator.application.Logger;

public class Funds {

    private double totalCash;
    private static Logger logger = new Logger("audit.txt");

    public double getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(double totalCash) {
        this.totalCash = totalCash;
    }

    public String change(double totalCash) {
        double change = totalCash* 100;
        int dollars = (int) (change / 100);
        change = change % 100;
        int quarters = (int) (change / 25);
        change = change % 25;
        int dimes = (int) (change / 10);
        change = change % 10;
        int nickels = (int) (change / 5);

        double backToZero = 0;

        logger.write("CHANGE GIVEN: " + "$" + totalCash + " " + "$" + backToZero);

        return "Your change is " + dollars + " dollar(s), " + quarters + " quarter(s), " + dimes + " dime(s), and " + nickels + " nickel(s).";

    }

}
