package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class receiveCreditCardPaymentTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().receiveCreditCardPayment(1, "4485370086510891"));
    }
    @Test
    public void testInvalidCreditCard1() {
        assertThrows(InvalidCreditCardException.class, () -> EZShop.getInstance().receiveCreditCardPayment(1, null));
    }
    @Test
    public void testInvalidCreditCard2() {
        assertThrows(InvalidCreditCardException.class, () -> EZShop.getInstance().receiveCreditCardPayment(1, ""));
    }
    @Test
    public void testInvalidTransactionId1() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().receiveCreditCardPayment(-1, "4485370086510891"));
    }
    @Test
    public void testInvalidTransactionId2() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().receiveCreditCardPayment(null, "4485370086510891"));
    }
    @Test
    public void testAbsentTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidCreditCardException {
        assertFalse(EZShop.getInstance().receiveCreditCardPayment(1, "4485370086510891"));
    }
    @Test
    public void testCorrect() throws InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidCreditCardException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().addProductToSale(1,"333333333331",1);
        EZShop.getInstance().endSaleTransaction(1);
        assertTrue(EZShop.getInstance().receiveCreditCardPayment(1, "4485370086510891"));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
