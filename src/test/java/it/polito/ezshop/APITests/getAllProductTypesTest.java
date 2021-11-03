package it.polito.ezshop.APITests;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mProductType;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class getAllProductTypesTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
    }
    @Test
    public void testCorrect() throws UnauthorizedException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
        ArrayList<mProductType> a = new ArrayList<>(EZShop.getInstance().getInventory().values());
        assertEquals(a,EZShop.getInstance().getAllProductTypes());
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().getAllProductTypes());
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
