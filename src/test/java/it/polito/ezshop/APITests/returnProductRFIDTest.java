package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mProduct;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class returnProductRFIDTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().returnProductRFID(1,"123456789012"));
    }
    @Test
    public void testCorrect() throws InvalidTransactionIdException, UnauthorizedException, InvalidDiscountRateException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidRFIDException, InvalidQuantityException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        mProduct x = new mProduct("123456789012");
        EZShop.getInstance().getInventory().get(1).addProduct(x);
        EZShop.getInstance().addProductToSaleRFID(1,x.getRFID());
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 1);
        EZShop.getInstance().startReturnTransaction(1);
        assertTrue(EZShop.getInstance().returnProductRFID(1,"123456789012"));
    }
    @Test
    public void testAbsentProduct() throws InvalidTransactionIdException, UnauthorizedException, InvalidRFIDException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPaymentException, InvalidProductIdException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        mProduct x = new mProduct("123456789012");
        EZShop.getInstance().getInventory().get(1).addProduct(x);
        EZShop.getInstance().addProductToSaleRFID(1,x.getRFID());
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 1);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().deleteProductType(1);
        assertFalse(EZShop.getInstance().returnProductRFID(1,"123456789012"));
    }
    @Test
    public void testAbsentProductInTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidRFIDException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        mProduct x = new mProduct("123456789012");
        EZShop.getInstance().getInventory().get(1).addProduct(x);
        EZShop.getInstance().addProductToSaleRFID(1,x.getRFID());
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 1);
        EZShop.getInstance().startReturnTransaction(1);
        assertFalse(EZShop.getInstance().returnProductRFID(1,"111112222211"));
    }
    @Test
    public void testAbsentReturnTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidRFIDException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        mProduct x = new mProduct("123456789012");
        EZShop.getInstance().getInventory().get(1).addProduct(x);
        assertFalse(EZShop.getInstance().returnProductRFID(1,"123456789012"));
    }
    @Test
    public void testAbsentSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidRFIDException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException{
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        mProduct x = new mProduct("123456789012");
        EZShop.getInstance().getInventory().get(1).addProduct(x);
        EZShop.getInstance().addProductToSaleRFID(1,x.getRFID());
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().deleteSaleTransaction(1);
        assertFalse(EZShop.getInstance().returnProductRFID(1,"123456789012"));
    }
    @Test
    public void testNullId() {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Cashier"));
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().returnProductRFID(null,"123456789012"));
    }
    @Test
    public void testIdEqual0() {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().returnProductRFID(0,"123456789012"));
    }
    @Test
    public void testIdLessThan0() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().returnProductRFID(-1, "123456789012"));
    }
    @Test
    public void testNullRFID() {
        assertThrows(InvalidRFIDException.class, () -> EZShop.getInstance().returnProductRFID(1,null));
    }
    @Test
    public void testEmptyRFID() {
        assertThrows(InvalidRFIDException.class, () -> EZShop.getInstance().returnProductRFID(1,""));
    }
    @Test
    public void testInvalidRFID() {
        assertThrows(InvalidRFIDException.class, () -> EZShop.getInstance().returnProductRFID(1, "123"));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
