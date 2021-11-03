package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mProductType;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class applyDiscountRateToProductTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().applyDiscountRateToProduct(1,"55555555555555",1));
    }
    @Test
    public void testCorrect() throws UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException {
        mProductType p = new mProductType("","description","55555555555555",2);
        p.setQuantity(10);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().addProductToSale(saleId,p.getBarCode(),2);
        assertTrue(EZShop.getInstance().applyDiscountRateToProduct(saleId,"55555555555555",0.5));
    }
    @Test
    public void testAbsentProduct() throws UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        mProductType p = new mProductType("","description","55555555555555",2);
        p.setQuantity(10);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        assertFalse(EZShop.getInstance().applyDiscountRateToProduct(saleId,"55555555555555",0.5));
    }
    @Test
    public void testAbsentSaleTransaction() throws UnauthorizedException, InvalidProductCodeException, InvalidTransactionIdException, InvalidDiscountRateException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Cashier"));
        assertFalse(EZShop.getInstance().applyDiscountRateToProduct(1,"55555555555555",0.5));
    }
    @Test
    public void testNullId() {
        mProductType p = new mProductType("","description","55555555555555",2);
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().applyDiscountRateToProduct(null, p.getBarCode(),0.2));
    }
    @Test
    public void testIdEqual0() {
        mProductType p = new mProductType("","description","55555555555555",2);
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().applyDiscountRateToProduct(0, p.getBarCode(),0.2));
    }
    @Test
    public void testIdLessThan0() {
        mProductType p = new mProductType("","description","55555555555555",2);
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().applyDiscountRateToProduct(-1, p.getBarCode(),0.2));
    }
    @Test
    public void testNullProductCode() {
        assertThrows(InvalidProductCodeException.class, () -> EZShop.getInstance().applyDiscountRateToProduct(1,null,0.2));
    }
    @Test
    public void testEmptyProductCode() {
        assertThrows(InvalidProductCodeException.class, () -> EZShop.getInstance().applyDiscountRateToProduct(1,"",0.2));
    }
    @Test
    public void testInvalidProductCode() {
        assertThrows(InvalidProductCodeException.class, () -> EZShop.getInstance().applyDiscountRateToProduct(1,"123",0.2));
    }
    @Test
    public void testDiscountRateLessThan0() {
        assertThrows(InvalidDiscountRateException.class, () -> EZShop.getInstance().applyDiscountRateToProduct(1,"55555555555555",-1));
    }
    @Test
    public void testDiscountRateGreaterThan1() {
        assertThrows(InvalidDiscountRateException.class, () -> EZShop.getInstance().applyDiscountRateToProduct(1,"55555555555555",2));
    }
    @Test
    public void testDiscountRateEqual1() {
        assertThrows(InvalidDiscountRateException.class, () -> EZShop.getInstance().applyDiscountRateToProduct(1,"55555555555555",2));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
