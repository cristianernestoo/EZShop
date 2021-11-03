package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class receiveCashPaymentTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().receiveCashPayment(1,1));
    }
    @Test
    public void testInvalidPayment() {
        assertThrows(InvalidPaymentException.class, () -> EZShop.getInstance().receiveCashPayment(1,-1));
    }
    @Test
    public void testInvalidTransactionId1() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().receiveCashPayment(null,1));
    }
    @Test
    public void testInvalidTransactionId2() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().receiveCashPayment(-1,1));
    }
    @Test
    public void testAbsentTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException {
        assertEquals(-1, EZShop.getInstance().receiveCashPayment(1,1), 0);
    }
    @Test
    public void testCorrect() throws InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().addProductToSale(1,"333333333331",1);
        EZShop.getInstance().endSaleTransaction(1);
        assertEquals(1, EZShop.getInstance().receiveCashPayment(1,2), 0);
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
