package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class returnProductTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().returnProduct(1,"333333333331",0));
    }
    @Test
    public void testInvalidId() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().returnProduct(-1,"333333333331",0));
    }
    @Test
    public void testInvalidCode() {
        assertThrows(InvalidProductCodeException.class, () -> EZShop.getInstance().returnProduct(1,"333333333332",0));
    }
    @Test
    public void testInvalidQuantity() {
        assertThrows(InvalidQuantityException.class, () -> EZShop.getInstance().returnProduct(1,"333333333331",-1));
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
        EZShop.getInstance().deleteProductType(1);
        assertFalse(EZShop.getInstance().returnProduct(1,"333333333331",1));
    }
    @Test
    public void testAbsentReturnTransaction() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException {
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        assertFalse(EZShop.getInstance().returnProduct(1,"333333333331",1));
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
        assertFalse(EZShop.getInstance().returnProduct(1,"333333333331",1));
    }
    @Test
    public void testInvalidReturnQuantity() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().addProductToSale(1,"333333333331",1);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 1);
        EZShop.getInstance().startReturnTransaction(1);
        assertFalse(EZShop.getInstance().returnProduct(1,"333333333331",2));
    }
    @Test
    public void testCorrect() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1, null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().addProductToSale(1,"333333333331",1);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 1);
        EZShop.getInstance().startReturnTransaction(1);
        assertTrue(EZShop.getInstance().returnProduct(1,"333333333331",1));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
