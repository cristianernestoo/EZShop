package it.polito.ezshop.tests;

import org.junit.Test;

import static org.junit.Assert.*;

public class attemptRefundTest {
    @Test
    public void testCorrectParameters() {
        String creditCard = "4716258050958645";
        double refund = 1.0;
        assertTrue(it.polito.ezshop.data.EZShop.getInstance().attemptRefund(creditCard, refund));
    }
    @Test
    public void testCreditCardNull() {
        double refund = 1.0;
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().attemptRefund(null, refund));
    }
    @Test
    public void testInvalidCreditCard() {
        String creditCard = "0000123456789125";
        double refund = 1.0;
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().attemptRefund(creditCard, refund));
    }
    @Test
    public void testNegativeRefund() {
        String creditCard = "4716258050958645";
        double refund = -1.0;
        assertThrows(IllegalArgumentException.class,() -> it.polito.ezshop.data.EZShop.getInstance().attemptRefund(creditCard, refund));
    }
    @Test
    public void testNonExistentCreditCard() {
        String creditCard = "4716258050958649";
        double refund = 1.0;
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().attemptRefund(creditCard, refund));
    }
}
