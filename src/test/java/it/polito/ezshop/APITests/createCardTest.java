package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class createCardTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().createCard());
    }
    @Test
    public void testCorrect1() throws UnauthorizedException {
        assertNotEquals("",EZShop.getInstance().createCard());
    }
    @Test
    public void testCorrect2() throws UnauthorizedException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        assertNotEquals("",EZShop.getInstance().createCard());
    }
    @Test
    public void testCorrect3() throws UnauthorizedException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Cashier"));
        assertNotEquals("",EZShop.getInstance().createCard());
    }
//    @Test
//    public void testDbUnreachable() throws UnauthorizedException {
//        assertEquals("",EZShop.getInstance().createCard());
//    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
