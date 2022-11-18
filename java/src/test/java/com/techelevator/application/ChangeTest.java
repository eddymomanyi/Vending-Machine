package com.techelevator.application;

import com.techelevator.application.VendingMachine;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChangeTest {

    VendingMachine changeTest;

    @Before
    public void setup() {
        changeTest = new VendingMachine();
    }

    @Test
    public void change_test_works() {
        Double inputA = 7.40;
        String expectedResult = "Your change is " + "7" + " dollar(s), " + "1" + " quarter(s), " + "1" + " dime(s), and " + "1" + " nickel(s).";
        String actualResult = changeTest.change(inputA);

        assertEquals(expectedResult, actualResult);

    }

}