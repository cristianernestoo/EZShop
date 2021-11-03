package it.polito.ezshop.tests;

import org.junit.Test;

import static org.junit.Assert.*;

public class checkCreditCardValidityTest {
    @Test
    public void testCorrectCreditCard(){
        String creditcard = "4733750149246197";
        assertTrue(it.polito.ezshop.data.EZShop.checkCreditCardValidity(creditcard));
    }
    @Test
    public void testOneDigitCreditCard(){
        String creditcard = "1";
        assertFalse(it.polito.ezshop.data.EZShop.checkCreditCardValidity(creditcard));
    }
    @Test
    public void testInvalidCreditCard(){
        String creditcard = "0000123456789125";
        assertFalse(it.polito.ezshop.data.EZShop.checkCreditCardValidity(creditcard));
    }
    @Test
    public void testInvalidCreditCardOutOfBoundaries(){
        String creditcard = "000012345678aaaa";
        assertFalse(it.polito.ezshop.data.EZShop.checkCreditCardValidity(creditcard));
    }
    @Test
    public void testNullCreditCard(){
        assertFalse(it.polito.ezshop.data.EZShop.checkCreditCardValidity(null));
    }
}
