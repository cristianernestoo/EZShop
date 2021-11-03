package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class deleteProductTypeTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
    }
    @Test
    public void testCorrect() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        Integer id = EZShop.getInstance().createProductType("test","333333333331",2.0,"test");
        assertTrue(EZShop.getInstance().deleteProductType(id));
    }
    @Test
    public void testInvalidPermission(){
        mUser mu = new mUser("test","test","Cashier");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(UnauthorizedException.class, ()->EZShop.getInstance().deleteProductType(1));
    }
    @Test
    public void testInvalidId() {
        mUser mu = new mUser("test", "test", "Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(InvalidProductIdException.class, () ->
            EZShop.getInstance().deleteProductType(-3)
        );
    }
    @Test
    public void testNoProductFound() throws UnauthorizedException,  InvalidProductIdException {
        mUser mu = new mUser("test", "test", "Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertFalse(EZShop.getInstance().deleteProductType(2));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
