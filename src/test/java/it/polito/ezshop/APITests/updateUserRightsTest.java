package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class updateUserRightsTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
    }
    @Test
    public void testCorrect() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        String newrole = "ShopManager";
        mUser mu = new mUser("test1","test1","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        Integer id = EZShop.getInstance().createUser("test", "test", "Cashier");
        assertTrue(EZShop.getInstance().updateUserRights(id,newrole));
    }
    @Test
    public void testInvalidPermission(){
        mUser mu = new mUser("test","test","Cashier");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().updateUserRights(2,"Cashier"));
    }
    @Test
    public void testInvalidId(){
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(InvalidUserIdException.class, () -> EZShop.getInstance().updateUserRights(-100,"Cashier"));
    }
    @Test
    public void testInvalidRole(){
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertThrows(InvalidRoleException.class, () -> EZShop.getInstance().updateUserRights(1,"Administratorr"));
    }
    @Test
    public void testNoUserFound() throws InvalidUserIdException, UnauthorizedException, InvalidRoleException {
        mUser mu = new mUser("test","test","Administrator");
        EZShop.getInstance().setLoggedUser(mu);
        assertFalse(EZShop.getInstance().updateUserRights(1,"Cashier"));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
