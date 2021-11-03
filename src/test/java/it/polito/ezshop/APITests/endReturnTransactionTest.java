package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class endReturnTransactionTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().endReturnTransaction(1, true));
    }
    @Test
    public void testInvalidId1() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().endReturnTransaction(null,true));
    }
    @Test
    public void testInvalidId2() throws InvalidTransactionIdException, UnauthorizedException {
        assertFalse(EZShop.getInstance().endReturnTransaction(1, true));
    }
    @Test
    public void testCommitFalse() throws InvalidTransactionIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().addProductToSale(1,"333333333331",1);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 1);
        EZShop.getInstance().startReturnTransaction(1);
        assertTrue(EZShop.getInstance().endReturnTransaction(1, false));
    }
    @Test
    public void testAbsentSaleTransaction() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().addProductToSale(1,"333333333331",1);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().deleteSaleTransaction(1);
        assertFalse(EZShop.getInstance().endReturnTransaction(1, false));
    }
    @Test
    public void testAbsentProduct() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().addProductToSale(1,"333333333331",1);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 1);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().returnProduct(1,"333333333331",1);
        EZShop.getInstance().deleteProductType(1);
        assertFalse(EZShop.getInstance().endReturnTransaction(1, true));
    }
    @Test
    public void testInvalidQuantity() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().addProductToSale(1,"333333333331",1);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 1);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().returnProduct(1,"333333333331",1);
        EZShop.getInstance().getOpenReturnTransaction().getEntries().get(0).setAmount(2);
        assertFalse(EZShop.getInstance().endReturnTransaction(1, true));
    }
    @Test
    public void testCorrect1() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().addProductToSale(1,"333333333331",1);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 1);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().returnProduct(1,"333333333331",1);
        assertTrue(EZShop.getInstance().endReturnTransaction(1, true));
    }
    @Test
    public void testCorrect2() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(2);
        EZShop.getInstance().addProductToSale(1,"333333333331",2);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 2);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().returnProduct(1,"333333333331",1);
        assertTrue(EZShop.getInstance().endReturnTransaction(1, true));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
