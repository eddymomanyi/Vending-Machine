package com.techelevator.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class ItemTest {
    private static final double DELTA = 0.001;

    Item sampleItem;

    @Before
    public void setup(){
        sampleItem = new Munchy("A4","Chippos",3.85,"Munchy");
    }


    @Test
    public void purchase_works_for_first_purchase() {
        double totalCashInput = 10.0;
        int purchaseCounterInput = 1;
        double expectedResult = 6.15;


        double actualResult = sampleItem.purchase(totalCashInput, purchaseCounterInput);

        assertEquals(expectedResult, actualResult, DELTA);

    }

    @Test
    public void purchase_works_for_BOGODO_purchase() {
        double totalCashInput = 10.0;
        int purchaseCounterInput = 2;
        double expectedResult = 7.15;


        double actualResult = sampleItem.purchase(totalCashInput, purchaseCounterInput);

        assertEquals(expectedResult, actualResult, DELTA);

    }

    @Test
    public void purchase_works_for_stock_QTY() {
        double totalCashInput = 10.0;
        int purchaseCounterInput = 1;
        int expectedResult = 5;
//        double leftOverCash = 6.15;


        double cashWorks = sampleItem.purchase(totalCashInput, purchaseCounterInput);
        int actualResult = sampleItem.getStockQty();

        assertEquals(expectedResult, actualResult);

    }

}