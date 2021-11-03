package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class deleteCustomerTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testCustomerNotFound() throws UnauthorizedException, InvalidCustomerIdException {
        assertFalse(EZShop.getInstance().deleteCustomer(10));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().deleteCustomer(10));
    }
    @Test
    public void testInvalidId1() {
        assertThrows(InvalidCustomerIdException.class, () -> EZShop.getInstance().deleteCustomer(-1));
    }
    @Test
    public void testInvalidId2() {
        assertThrows(InvalidCustomerIdException.class, () -> EZShop.getInstance().deleteCustomer( null));
    }
    @Test
    public void testCorrect() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
        Integer id = EZShop.getInstance().defineCustomer("Mario Rossi");
        assertTrue(EZShop.getInstance().deleteCustomer(id));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
