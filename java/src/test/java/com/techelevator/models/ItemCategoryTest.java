package com.techelevator.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ItemCategoryTest {


    @Test

    public void Candy_get_message_test_works () {
        Candy candy = new Candy("A3", "Snykkers", 4.25, "Candy");
        String messageTester = candy.getMessage();
        String expectedResult = "Sugar, Sugar, so Sweet!";
        assertEquals(expectedResult, messageTester);
    }

    @Test
    public void Munchy_get_message_test_works () {
        Munchy munchy = new Munchy("B1", "Stackers",2.65, "Munchy");
        String messageTester = munchy.getMessage();
        String expectedResult = "Munchy, Munchy, so Good!";
        assertEquals(expectedResult, messageTester);
    }

    @Test
    public void Drinky_get_message_test_works () {
        Drink drinky = new Drink("A2", "Ginger Ayle",1.85, "Drink");
        String messageTester = drinky.getMessage();
        String expectedResult = "Drinky, Drinky, Slurp Slurp!";
        assertEquals(expectedResult, messageTester);
    }

    @Test
    public void Gum_get_message_test_works () {
        Gum gummy = new Gum("D1", "Teaberry",1.65, "Gum");
        String messageTester = gummy.getMessage();
        String expectedResult = "Chewy, Chewy, Lots O Bubbles!";
        assertEquals(expectedResult, messageTester);
    }

}
