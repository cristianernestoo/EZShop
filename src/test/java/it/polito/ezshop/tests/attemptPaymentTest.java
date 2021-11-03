package it.polito.ezshop.tests;

import it.polito.ezshop.data.EZShop;
import org.junit.Test;


import static org.junit.Assert.*;

public class attemptPaymentTest {
    @Test
    public void testCorrectParameters(){
        String creditCard = "4733750149246197";
        EZShop.getInstance().getListofCreditCards().put(creditCard,150.0);
        double cost = 1.0;
        assertTrue(it.polito.ezshop.data.EZShop.getInstance().attemptPayment(creditCard, cost));
    }
    @Test
    public void testCreditCardNull() {
        double cost = 1.0;
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().attemptPayment(null, cost));
    }

    @Test
    public void testInvalidCreditCard(){
        String creditCard = "0000123456789125";
        double cost = 1.0;
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().attemptPayment(creditCard, cost));
    }
    @Test
    public void testNegativeCost(){
        String creditCard = "4716258050958645";
        double cost = -1.0;
        assertThrows(IllegalArgumentException.class,() -> it.polito.ezshop.data.EZShop.getInstance().attemptPayment(creditCard, cost));
    }
    @Test
    public void testInsufficientCredit() {
        String creditCard = "4716258050958645";
        double cost = 500.0;
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().attemptPayment(creditCard, cost));
    }
    @Test
    public void testNonExistentCreditCard() {
        String creditCard = "4716258050958649";
        double cost = 1.0;
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().attemptPayment(creditCard, cost));
    }
}
