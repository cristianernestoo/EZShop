package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class deleteReturnTransactionTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().deleteReturnTransaction(1));
    }
    @Test
    public void testInvalidId1() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().deleteReturnTransaction(-1));
    }
    @Test
    public void testInvalidId2() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().deleteReturnTransaction(null));
    }
    @Test
    public void testAbsentReturnTransaction() throws InvalidTransactionIdException, UnauthorizedException {
        assertFalse(EZShop.getInstance().deleteReturnTransaction(1));
    }
    @Test
    public void testAbsentSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(EZShop.getInstance().getInventory().keySet().toArray()[0]).setQuantity(2);
        EZShop.getInstance().addProductToSale(1,"333333333331",2);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 2);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().returnProduct(1,"333333333331",1);
        EZShop.getInstance().endReturnTransaction(1, true);
        EZShop.getInstance().getReturnTransaction(1).setSaleId(2);
        assertFalse(EZShop.getInstance().deleteReturnTransaction(1));
    }
    @Test
    public void testPresentBalanceOperation() throws InvalidTransactionIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(EZShop.getInstance().getInventory().keySet().toArray()[0]).setQuantity(2);
        EZShop.getInstance().addProductToSale(1,"333333333331",2);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 2);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().returnProduct(1,"333333333331",1);
        EZShop.getInstance().endReturnTransaction(1, true);
        EZShop.getInstance().getReturnTransaction(1).setBalanceId(1);
        assertFalse(EZShop.getInstance().deleteReturnTransaction(1));
    }
    @Test
    public void testCorrect() throws InvalidTransactionIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(EZShop.getInstance().getInventory().keySet().toArray()[0]).setQuantity(2);
        EZShop.getInstance().addProductToSale(1,"333333333331",2);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 2);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().returnProduct(1,"333333333331",1);
        EZShop.getInstance().endReturnTransaction(1, true);
        assertTrue(EZShop.getInstance().deleteReturnTransaction(1));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
