package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class recordBalanceUpdateTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().recordBalanceUpdate(1));
    }
    @Test
    public void testNegativeBalance() throws UnauthorizedException {
        assertFalse(EZShop.getInstance().recordBalanceUpdate(-5));
    }
    @Test
    public void testCredit() throws UnauthorizedException {
        assertTrue(EZShop.getInstance().recordBalanceUpdate(10));
    }
    @Test
    public void testDebit() throws UnauthorizedException {
        EZShop.getInstance().recordBalanceUpdate(10);
        assertTrue(EZShop.getInstance().recordBalanceUpdate(-5));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}