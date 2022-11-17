package com.techelevator.models;

public class Drink extends Item{

    public Drink(String location, String itemName, double price, String itemType) {
        super(location, itemName, price, itemType);
    }

    public String getMessage(){
        return "Drinky, Drinky, Slurp Slurp!";
    }
}
