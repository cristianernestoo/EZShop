package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mProductType;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class getProductTypeByBarCodeTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().getProductTypeByBarCode("55555555555555"));
    }
    @Test
    public void testFoundProductType() throws UnauthorizedException, InvalidProductCodeException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        String barcode = "55555555555555";
        mProductType mpt = new mProductType("note","description",barcode,2);
        EZShop.getInstance().getInventory().put(mpt.getId(),mpt);
        mProductType x = (mProductType) EZShop.getInstance().getProductTypeByBarCode(barcode);
        assertSame(mpt, x);
    }
    @Test
    public void testAbsentProductType() throws UnauthorizedException, InvalidProductCodeException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
        String barcode = "55555555555555";
        mProductType mpt = new mProductType("note","description","333333333331",2);
        EZShop.getInstance().getInventory().put(mpt.getId(),mpt);
        mProductType x = (mProductType) EZShop.getInstance().getProductTypeByBarCode(barcode);
        assertNull(x);
    }
    @Test
    public void testInvalidBarcode() {
        assertThrows(InvalidProductCodeException.class,() -> EZShop.getInstance().getProductTypeByBarCode("123"));
    }
    @Test
    public void testEmptyBarcode() {
        assertThrows(InvalidProductCodeException.class,() -> EZShop.getInstance().getProductTypeByBarCode(""));
    }
    @Test
    public void testNullBarcode(){
        assertThrows(InvalidProductCodeException.class,() -> EZShop.getInstance().getProductTypeByBarCode(null));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
