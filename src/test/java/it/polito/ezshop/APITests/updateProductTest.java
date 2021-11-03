package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class updateProductTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
    }

    @Test
    public void testCorrect() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {
        mUser mu = new mUser("test", "test", "Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        Integer id = EZShop.getInstance().createProductType("test", "333333333331", 2.0, "test");
        assertTrue(EZShop.getInstance().updateProduct(id, "test1", "555555555555", 3.0, "test1"));

    }

    @Test
    public void testInvalidPermission() {
        mUser mu = new mUser("test", "test", "Cashier");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(UnauthorizedException.class, () ->
            EZShop.getInstance().updateProduct(1, "test", "333333333331", 2.0, "test")
        );
    }

    @Test
    public void testInvalidId(){
        mUser mu = new mUser("test", "test", "Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(InvalidProductIdException.class, () ->
            EZShop.getInstance().updateProduct(-3, "test", "333333333331", 2.0, "test")
        );
    }

    @Test
    public void testInvalidProductCode() {
        mUser mu = new mUser("test", "test", "Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(InvalidProductCodeException.class, () ->
            EZShop.getInstance().updateProduct(1, "test", "333333333331345253453", 2.0, "test")
        );
    }

    @Test
    public void testInvalidPriceperUnit() {
        mUser mu = new mUser("test", "test", "Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(InvalidPricePerUnitException.class, () ->
            EZShop.getInstance().updateProduct(1, "test", "333333333331", -45.0, "test")
        );
    }
    @Test
    public void testInvalidDescription(){
        mUser mu = new mUser("test", "test", "Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(InvalidProductDescriptionException.class, () -> EZShop.getInstance().updateProduct(1,"","333333333331",2.0,"test") );
    }
    @Test
    public void testNoProductFound() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidProductCodeException {
        mUser mu = new mUser("test", "test", "Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertFalse(EZShop.getInstance().updateProduct(2,"test","333333333331",2.0,"test"));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
