package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class deleteSaleTransactionTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().deleteSaleTransaction(1));
    }
    @Test
    public void testCorrect() throws InvalidTransactionIdException, UnauthorizedException {
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        assertTrue(EZShop.getInstance().deleteSaleTransaction(saleId));
    }
    @Test
    public void testCorrect2() throws InvalidTransactionIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidProductIdException {
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        String code1 = "333333333331";
        String code2 = "55555555555555";
        EZShop.getInstance().createProductType("Test",code1,1,null);
        EZShop.getInstance().createProductType("Test",code2,1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().getInventory().get(2).setQuantity(1);
        EZShop.getInstance().addProductToSale(1,code1,1);
        EZShop.getInstance().addProductToSale(1,code2,1);
        EZShop.getInstance().deleteProductType(1);
        EZShop.getInstance().endSaleTransaction(1);
        assertTrue(EZShop.getInstance().deleteSaleTransaction(saleId));
    }
    @Test
    public void testAbsentSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        assertFalse(EZShop.getInstance().deleteSaleTransaction(1));
    }
    @Test
    public void testAbsentSaleTransaction2() throws InvalidTransactionIdException, UnauthorizedException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        EZShop.getInstance().startSaleTransaction();
        assertFalse(EZShop.getInstance().deleteSaleTransaction(10));
    }
    @Test
    public void testAlreadyPayed() throws InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Cashier"));
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().endSaleTransaction(saleId);
        EZShop.getInstance().receiveCashPayment(saleId, 1);
        assertFalse(EZShop.getInstance().deleteSaleTransaction(saleId));
    }
    @Test
    public void testNullId() {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Cashier"));
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().deleteSaleTransaction(null));
    }
    @Test
    public void testIdEqual0() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().deleteSaleTransaction(0));
    }
    @Test
    public void testIdLessThan0() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().deleteSaleTransaction(-1));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
