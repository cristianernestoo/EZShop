package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class getUserTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
    }
    @Test
    public void testCorrect() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        mUser mu = new mUser("test1","test1","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        Integer id = EZShop.getInstance().createUser("test", "test", "Cashier");
        mUser m1 = EZShop.getInstance().getListOfUsers().get(0);
        assertEquals(m1,EZShop.getInstance().getUser(id));
    }
    @Test
    public void testInvalidPermission() {
        mUser mu = new mUser("test","test","Cashier");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(UnauthorizedException.class, ()->EZShop.getInstance().getUser(1));
    }
    @Test
    public void testInvalidId() {
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(InvalidUserIdException.class, ()->EZShop.getInstance().getUser(-100));
    }
    @Test
    public void testUserNotFound() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        EZShop.getInstance().createUser("test", "test", "Cashier");
        assertNull(EZShop.getInstance().getUser(100));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
