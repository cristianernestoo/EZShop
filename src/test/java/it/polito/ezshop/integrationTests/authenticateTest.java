package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import org.junit.After;
import it.polito.ezshop.exceptions.*;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class authenticateTest {
    @BeforeClass
    public static void clearBeforeClass() {
        EZShop.getInstance().reset();
    }
    @After
    public void clearAfter() {
        EZShop.getInstance().reset();
    }
    @Test
    public void scenario5_1() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {
        //preconditions
        EZShop.getInstance().createUser("a","a","Administrator");

        EZShop.getInstance().login("a","a");
        assertEquals("a",EZShop.getInstance().getLoggedUser().getUsername());
    }
    @Test
    public void scenario5_2() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {
        //preconditions
        EZShop.getInstance().createUser("a","a","Administrator");

        EZShop.getInstance().login("a","a");
        EZShop.getInstance().logout();
        assertNull(EZShop.getInstance().getLoggedUser());
    }
}