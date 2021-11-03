package it.polito.ezshop.APITests;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;


public class deleteUserTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
    }
    @Test
    public void testCorrect() throws InvalidUserIdException, UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        EZShop.getInstance().createUser("test1","test1","Cashier");
        assertTrue(EZShop.getInstance().deleteUser(2));
    }
    @Test
    public void testInvalidPermission() {
        mUser mu = new mUser("test","test","Cashier");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(UnauthorizedException.class, ()->EZShop.getInstance().deleteUser(1));
    }
    @Test
    public void testInvalidId() {
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(InvalidUserIdException.class, ()->EZShop.getInstance().deleteUser(-100));
    }
    @Test
    public void testNoUserFound() throws InvalidUserIdException, UnauthorizedException {
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertFalse(EZShop.getInstance().deleteUser(100));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}