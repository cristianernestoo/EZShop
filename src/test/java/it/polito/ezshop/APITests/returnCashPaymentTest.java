package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class returnCashPaymentTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().returnCashPayment(1));
    }
    @Test
    public void testInvalidTransactionId1() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().returnCashPayment(null));
    }
    @Test
    public void testInvalidTransactionId2() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().returnCashPayment(-1));
    }
    @Test
    public void testAbsentTransaction() throws InvalidTransactionIdException, UnauthorizedException {
        assertEquals(-1, EZShop.getInstance().returnCashPayment(1), 0);
    }
    @Test
    public void testAbsentReturnTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(2);
        EZShop.getInstance().addProductToSale(1,"333333333331",2);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 2);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().returnProduct(1,"333333333331",1);
        EZShop.getInstance().endReturnTransaction(1, true);
        EZShop.getInstance().deleteReturnTransaction(1);
        EZShop.getInstance().recordBalanceUpdate(2);
        assertEquals(-1, EZShop.getInstance().returnCashPayment(1), 0);
    }
    @Test
    public void testCorrect() throws InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(2);
        EZShop.getInstance().addProductToSale(1,"333333333331",2);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 2);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().returnProduct(1,"333333333331",1);
        EZShop.getInstance().endReturnTransaction(1, true);
        EZShop.getInstance().recordBalanceUpdate(2);
        assertEquals(1, EZShop.getInstance().returnCashPayment(1), 0);
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
