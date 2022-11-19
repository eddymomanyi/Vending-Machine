package com.techelevator.models;

import com.techelevator.application.VendingMachine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class ItemTest {
    private static final double DELTA = 0.001;

    Item sampleItem;

    @Before

    // sample item for tests below
    public void setup(){
        sampleItem = new Munchy("A4","Chippos",3.85,"Munchy");
    }


    // checks if balance is updated
    @Test
    public void purchase_works_for_first_purchase() {
        double totalCashInput = 10.0;
        int purchaseCounterInput = 1;
        double expectedResult = 6.15;
        double actualResult = sampleItem.purchase(totalCashInput, purchaseCounterInput);
        assertEquals(expectedResult, actualResult, DELTA);

    }

    // checks if bogodo is working
    @Test
    public void purchase_works_for_BOGODO_purchase() {
        double totalCashInput = 10.0;
        int purchaseCounterInput = 2;
        double expectedResult = 7.15;
        double actualResult = sampleItem.purchase(totalCashInput, purchaseCounterInput);
        assertEquals(expectedResult, actualResult, DELTA);

    }

    // checks if stock quantity is updated
    @Test
    public void purchase_works_for_stock_QTY() {
        double totalCashInput = 10.0;
        int purchaseCounterInput = 1;
        int expectedResult = 5;
        double cashWorks = sampleItem.purchase(totalCashInput, purchaseCounterInput);
        int actualResult = sampleItem.getStockQty();
        assertEquals(expectedResult, actualResult);

    }

    // checks if item is in stock is working
    @Test
    public void inStock_returns_true_for_stock_above_0(){
        sampleItem.setStockQty(5);
      //  boolean expectedResult = true;
        boolean actualResult = VendingMachine.inStock(sampleItem);
        Assert.assertTrue(actualResult);
    }

    // checks if item is out of stock
    @Test
    public void inStock_returns_false_for_stock_at_0(){
        sampleItem.setStockQty(0);
        //  boolean expectedResult = false;
        boolean actualResult = VendingMachine.inStock(sampleItem);
        Assert.assertFalse(actualResult);
    }

}