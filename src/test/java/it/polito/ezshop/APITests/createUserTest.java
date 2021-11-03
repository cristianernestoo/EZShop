package it.polito.ezshop.APITests;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;


public class createUserTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
    }
    @Test
    public void testCorrect() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        EZShop.getInstance().createUser("test", "test", "Cashier");
        mUser u = new mUser(1,"test", "test", "Cashier");
        assertTrue(u.equals(EZShop.getInstance().getListOfUsers().get(0)));
    }
    @Test
    public void testSameUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
        EZShop.getInstance().createUser("test", "test", "Cashier");
        assertEquals(-1,EZShop.getInstance().createUser("test", "test", "Cashier"),0);
    }
    @Test
    public void testInvalidUsername() {
        assertThrows(InvalidUsernameException.class, () -> EZShop.getInstance().createUser("", "test", "Cashier"));
    }
    @Test
    public void testInvalidPassword() {
        assertThrows(InvalidPasswordException.class, () -> EZShop.getInstance().createUser("test", "", "Cashier"));
    }
    @Test
    public void testInvalidRole() {
        assertThrows(InvalidRoleException.class, () -> EZShop.getInstance().createUser("test", "test", ""));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}