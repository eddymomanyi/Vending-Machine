package com.techelevator.models;

public class Stock {

    private String location;
    private String itemName;
    private double price;
    private String itemType;
    private int stockQty;

    public Stock(String location, String itemName, double price, String itemType) {
        this.location = location;
        this.itemName = itemName;
        this.price = price;
        this.itemType = itemType;
        this.stockQty = 6;
    }

    public String getLocation() {
        return location;
    }

    public String getItemName() {
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public String getItemType() {
        return itemType;
    }

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    public String toString() {

        return location + ") " + itemName + ": $" + price;
    }

}
