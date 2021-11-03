package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class getAllUserTest{
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
    }
    @Test
    public void testCorrect() throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        mUser mu = new mUser("test1","test1","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        EZShop.getInstance().createUser("test", "test", "Cashier");
       assertEquals(EZShop.getInstance().getListOfUsers(),EZShop.getInstance().getAllUsers());
    }
    @Test
    public void testInvalidPermission() {
        mUser mu = new mUser("test","test","Cashier");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(UnauthorizedException.class, ()->EZShop.getInstance().getAllUsers());
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
