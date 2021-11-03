package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mProduct;
import it.polito.ezshop.model.mProductType;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;

public class addProductToSaleRFIDTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() throws UnauthorizedException {
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().addProductToSaleRFID(saleId,"111111111111"));
    }
    @Test
    public void testCorrect() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidRFIDException {
        mProductType pt = new mProductType("","description","55555555555555",2);
        mProduct p = new mProduct("111111111111");
        pt.addProduct(p);
        pt.setQuantity(pt.getQuantity() + 1);
        EZShop.getInstance().getInventory().put(pt.getId(),pt);
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        assertTrue(EZShop.getInstance().addProductToSaleRFID(saleId,"111111111111"));
    }
    @Test
    public void testCorrect2() throws UnauthorizedException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidRFIDException {
        mProductType pt = new mProductType("","description","55555555555555",2);
        mProduct p1 = new mProduct("111111111111");
        mProduct p2 = new mProduct("222222222222");
        pt.addProduct(p1);
        pt.addProduct(p2);
        pt.setQuantity(pt.getQuantity() + 2);
        EZShop.getInstance().getInventory().put(pt.getId(),pt);
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().addProductToSaleRFID(saleId,"111111111111");
        assertTrue(EZShop.getInstance().addProductToSaleRFID(saleId,"222222222222"));
    }
    @Test
    public void testAbsentProduct() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException {
        mProductType pt = new mProductType("","description","55555555555555",2);
        EZShop.getInstance().getInventory().put(pt.getId(),pt);
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        assertFalse(EZShop.getInstance().addProductToSaleRFID(saleId,"111111111111"));
    }
    @Test
    public void testAbsentSale() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException {
        EZShop.getInstance().startSaleTransaction();
        mProductType pt = new mProductType("","description","55555555555555",2);
        mProduct p = new mProduct("111111111111");
        pt.setQuantity(10);
        pt.addProduct(p);
        pt.setQuantity(pt.getQuantity() + 1);
        EZShop.getInstance().getInventory().put(pt.getId(),pt);
        assertFalse(EZShop.getInstance().addProductToSaleRFID(1,"222222222222"));
    }
    @Test
    public void testNullId() throws UnauthorizedException {
        EZShop.getInstance().startSaleTransaction();
        mProductType pt = new mProductType("","description","55555555555555",2);
        mProduct p = new mProduct("111111111111");
        pt.setQuantity(10);
        pt.addProduct(p);
        pt.setQuantity(pt.getQuantity() + 1);
        EZShop.getInstance().getInventory().put(pt.getId(),pt);
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().addProductToSaleRFID(null,"222222222222"));
    }
    @Test
    public void testIdEqual0() throws UnauthorizedException {
        EZShop.getInstance().startSaleTransaction();
        mProductType pt = new mProductType("","description","55555555555555",2);
        mProduct p = new mProduct("111111111111");
        pt.setQuantity(10);
        pt.addProduct(p);
        pt.setQuantity(pt.getQuantity() + 1);
        EZShop.getInstance().getInventory().put(pt.getId(),pt);
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().addProductToSaleRFID(0,"222222222222"));
    }
    @Test
    public void testIdLessThan0() throws UnauthorizedException {
        EZShop.getInstance().startSaleTransaction();
        mProductType pt = new mProductType("","description","55555555555555",2);
        mProduct p = new mProduct("111111111111");
        pt.setQuantity(10);
        pt.addProduct(p);
        pt.setQuantity(pt.getQuantity() + 1);
        EZShop.getInstance().getInventory().put(pt.getId(),pt);
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().addProductToSaleRFID(-1,"222222222222"));
    }
    @Test
    public void testNullRFID() throws UnauthorizedException {
        EZShop.getInstance().startSaleTransaction();
        assertThrows(InvalidRFIDException.class, () -> EZShop.getInstance().addProductToSaleRFID(1,null));
    }
    @Test
    public void testEmptyRFID() throws UnauthorizedException {
        EZShop.getInstance().startSaleTransaction();
        assertThrows(InvalidRFIDException.class, () -> EZShop.getInstance().addProductToSaleRFID(1,""));
    }
    @Test
    public void testInvalidRFID() throws UnauthorizedException {
        EZShop.getInstance().startSaleTransaction();
        assertThrows(InvalidRFIDException.class, () -> EZShop.getInstance().addProductToSaleRFID(1,"1"));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
