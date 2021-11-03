package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mCustomer;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class getAllCustomersTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testEmpty() throws UnauthorizedException{
        assertEquals(new ArrayList<mCustomer>(), EZShop.getInstance().getAllCustomers());
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().getAllCustomers());
    }
    @Test
    public void testCorrect() throws InvalidCustomerNameException, UnauthorizedException {
        EZShop.getInstance().defineCustomer("Mario Rossi");
        mCustomer cu = new mCustomer(1, "Mario Rossi",null,0);
        assertTrue(cu.equals((mCustomer) EZShop.getInstance().getAllCustomers().get(0)));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
