package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mProductType;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class addProductToSaleTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().addProductToSale(1,"55555555555555",1));
    }
    @Test
    public void testCorrect() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException {
        mProductType p = new mProductType("","description","55555555555555",2);
        p.setQuantity(10);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        assertTrue(EZShop.getInstance().addProductToSale(saleId,p.getBarCode(),2));
    }
    @Test
    public void testCorrect2() throws UnauthorizedException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException {
        mProductType p = new mProductType("","description","55555555555555",2);
        p.setQuantity(10);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().addProductToSale(saleId,p.getBarCode(),2);
        assertTrue(EZShop.getInstance().addProductToSale(saleId,p.getBarCode(),3));
    }
    @Test
    public void testAbsentProduct() throws UnauthorizedException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        assertFalse(EZShop.getInstance().addProductToSale(saleId,"55555555555555",2));
    }
    @Test
    public void testProductQuantityNotEnough() throws UnauthorizedException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException {
        //EZShop.getInstance().setLoggedUser(new mUser("a","a","Cashier"));
        mProductType p = new mProductType("","description","55555555555555",2);
        p.setQuantity(1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        assertFalse(EZShop.getInstance().addProductToSale(saleId,p.getBarCode(),2));
    }
    @Test
    public void testAbsentSale() throws UnauthorizedException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException {
        mProductType p = new mProductType("","description","55555555555555",2);
        p.setQuantity(10);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        assertFalse(EZShop.getInstance().addProductToSale(1,p.getBarCode(),2));
    }
    @Test
    public void testNullId() {
        mProductType p = new mProductType("","description","55555555555555",2);
        p.setQuantity(10);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().addProductToSale(null,p.getBarCode(),2));
    }
    @Test
    public void testIdEqual0() {
        mProductType p = new mProductType("","description","55555555555555",2);
        p.setQuantity(10);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().addProductToSale(0,p.getBarCode(),2));
    }
    @Test
    public void testIdLessThan0() {
        mProductType p = new mProductType("","description","55555555555555",2);
        p.setQuantity(10);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().addProductToSale(-2,p.getBarCode(),2));
    }
    @Test
    public void testNullProductCode() {
        assertThrows(InvalidProductCodeException.class, () -> EZShop.getInstance().addProductToSale(1,null,2));
    }
    @Test
    public void testEmptyProductCode() {
        assertThrows(InvalidProductCodeException.class, () -> EZShop.getInstance().addProductToSale(1,"",2));
    }
    @Test
    public void testInvalidProductCode() {
        assertThrows(InvalidProductCodeException.class, () -> EZShop.getInstance().addProductToSale(1,"123",2));
    }
    @Test
    public void testNegativeQuantity()  {
        mProductType p = new mProductType("","description","55555555555555",2);
        assertThrows(InvalidQuantityException.class, () -> EZShop.getInstance().addProductToSale(1,p.getBarCode(),-2));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
