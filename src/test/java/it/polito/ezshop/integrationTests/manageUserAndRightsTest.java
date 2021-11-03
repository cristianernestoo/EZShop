package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class manageUserAndRightsTest {
    @Before
    public void wipeOut() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        EZShop.getInstance().reset();
        EZShop.getInstance().createUser("test","test","Administrator");
        EZShop.getInstance().login("test","test");
    }
    @Test
    public void Scenario2_1() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        Integer id = EZShop.getInstance().createUser("test1","test1","Cashier");
        assertNotEquals(-1,id,0);
    }
    @Test
    public void Scenario2_2() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidUserIdException {
        Integer id = EZShop.getInstance().createUser("test1","test1","Cashier");
        assertTrue(EZShop.getInstance().deleteUser(id));
    }
    @Test
    public void Scenario2_3() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        Integer id = EZShop.getInstance().createUser("test1","test1","Cashier");
        assertTrue(EZShop.getInstance().updateUserRights(id,"ShopManager"));
    }

}
