package com.techelevator.models;

public class Candy extends Item {

    public Candy(String location, String itemName, double price, String itemType) {
        super(location, itemName, price, itemType);
    }

    public String getMessage(){return "Sugar, Sugar, so Sweet!";}

}
