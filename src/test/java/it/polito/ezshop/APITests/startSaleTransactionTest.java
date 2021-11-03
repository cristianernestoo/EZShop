package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

public class startSaleTransactionTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().startSaleTransaction());
    }
    @Test
    public void testCorrect() throws UnauthorizedException {
        assertNotEquals(Integer.valueOf(-1), EZShop.getInstance().startSaleTransaction());
    }
    @Test
    public void testCorrect2() throws UnauthorizedException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Cashier"));
        assertNotEquals(Integer.valueOf(-1), EZShop.getInstance().startSaleTransaction());
    }
    @Test
    public void testCorrect3() throws UnauthorizedException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        assertNotEquals(Integer.valueOf(-1), EZShop.getInstance().startSaleTransaction());
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
