package com.techelevator.models;

public class Gum extends Item{

    public Gum(String location, String itemName, double price, String itemType) {
        super(location, itemName, price, itemType);
    }


    public String getMessage() {
        return "Chewy, Chewy, Lots O Bubbles!";
    }
}
