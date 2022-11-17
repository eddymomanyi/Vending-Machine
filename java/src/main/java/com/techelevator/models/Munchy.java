package com.techelevator.models;

public class Munchy extends Item{

    public Munchy(String location, String itemName, double price, String itemType) {
        super(location, itemName, price, itemType);
    }

    public String getMessage(){
        return "Munchy, Munchy, so Good!";
    }
}
