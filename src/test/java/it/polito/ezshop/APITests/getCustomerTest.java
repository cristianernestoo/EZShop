package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mCustomer;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class getCustomerTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testNull() throws UnauthorizedException, InvalidCustomerIdException {
        assertNull(EZShop.getInstance().getCustomer(10));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().getCustomer(10));
    }
    @Test
    public void testInvalidId1() {
        assertThrows(InvalidCustomerIdException.class, () -> EZShop.getInstance().getCustomer(-1));
    }
    @Test
    public void testInvalidId2() {
        assertThrows(InvalidCustomerIdException.class, () -> EZShop.getInstance().getCustomer( null));
    }
    @Test
    public void testCorrect() throws InvalidCustomerNameException, UnauthorizedException {
        mCustomer cu = new mCustomer("Mario Rossi");
        EZShop.getInstance().defineCustomer("Mario Rossi");
        assertTrue(cu.equals(EZShop.getInstance().getListOfCustomers().get(0)));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
