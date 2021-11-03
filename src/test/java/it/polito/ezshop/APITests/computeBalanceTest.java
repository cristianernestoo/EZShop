package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class computeBalanceTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().computeBalance());
    }
    @Test
    public void testCorrect() throws UnauthorizedException {
        EZShop.getInstance().recordBalanceUpdate(10);
        EZShop.getInstance().recordBalanceUpdate(-5);
        assertEquals(5,EZShop.getInstance().computeBalance(),0);
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}