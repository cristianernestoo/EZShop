package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class loginTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
    }
    @Test
    public void testCorrect() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        EZShop.getInstance().createUser("test", "test", "Cashier");
        assertEquals(EZShop.getInstance().login("test","test"),EZShop.getInstance().getLoggedUser());
    }
    @Test
    public void testInvalidUsername() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        EZShop.getInstance().createUser("test", "test", "Cashier");
        assertThrows(InvalidUsernameException.class, ()->EZShop.getInstance().login("","test"));
    }
    @Test
    public void testInvalidPassword() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        EZShop.getInstance().createUser("test", "test", "Cashier");
        assertThrows(InvalidPasswordException.class, () -> EZShop.getInstance().login("test","") );
    }
    @Test
    public void testInvalidUser() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        EZShop.getInstance().createUser("test", "test", "Cashier");
        assertNull(EZShop.getInstance().login("testt","testtt"));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
