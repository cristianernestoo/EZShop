package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class logoutTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
    }
    @Test
    public void testCorrect() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        EZShop.getInstance().createUser("test", "test", "Cashier");
        EZShop.getInstance().login("test","test");
        assertTrue(EZShop.getInstance().logout());
    }
    @Test
    public void testInvalidLogout(){
        assertFalse(EZShop.getInstance().logout());
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}