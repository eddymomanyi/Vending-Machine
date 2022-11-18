package com.techelevator.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ItemCategoryTest {

    Candy getMessage;

    @Before

    public void setup() {
        getMessage = new Candy("A3", "Snykkers", 4.25, "Candy");
    }

    @Test

    public void get_message_test_works () {

        String messageTester = getMessage.getMessage();
        String expectedResult = "Sugar, Sugar, so Sweet!";

        assertEquals(expectedResult, messageTester);

    }

}
