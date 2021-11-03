package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class createProductTypeTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
    }
    @Test
    public void testCorrect() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        Integer id = -1;
        assertNotEquals(id,EZShop.getInstance().createProductType("test","333333333331",2.0,"test"));
    }
    @Test
    public void testInvalidPermission(){
        mUser mu = new mUser("test","test","Cashier");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(UnauthorizedException.class, ()->EZShop.getInstance().createProductType("test","333333333331",2.0,"test"));
    }
    @Test
    public void testInvalidDescription() {
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(InvalidProductDescriptionException.class,()->EZShop.getInstance().createProductType("","333333333331",2.0,"test"));
    }
    @Test
    public void testInvalidProductCode(){
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(InvalidProductCodeException.class,()->EZShop.getInstance().createProductType("test","333333333331345253453",2.0,"test"));
    }
    @Test
    public void testInvalidPriceperUnit() {
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(InvalidPricePerUnitException.class,()->EZShop.getInstance().createProductType("test","333333333331",-45.0,"test"));
    }
    @Test
    public void testInvalidProduct() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        EZShop.getInstance().createProductType("test","333333333331",45.0,"test");
        Integer id = -1;
        assertEquals(id,EZShop.getInstance().createProductType("test","333333333331",45.0,"test"));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
