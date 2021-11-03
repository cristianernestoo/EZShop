package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mCustomer;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class defineCustomerTest {
    @BeforeClass
    public static void wipeOut() {
        EZShop.getInstance().reset();
    }
    @Before
    public void preconditions(){
        EZShop.getInstance().setLoggedUser(new mUser(1,"a","a","Administrator"));
        EZShop.getInstance().getListOfCustomers().clear();
        mCustomer.reset();
    }
    @Test
    public void unauthorizedTest(){
        EZShop.getInstance().setLoggedUser(null);
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().defineCustomer("Cust"));
    }
    @Test
    public void invalidName1Test(){
        assertThrows(InvalidCustomerNameException.class, () -> EZShop.getInstance().defineCustomer(null));
    }
    @Test
    public void invalidName2Test(){
        assertThrows(InvalidCustomerNameException.class, () -> EZShop.getInstance().defineCustomer(""));
    }
    @Test
    public void nameAlreadyUsedTest() throws InvalidCustomerNameException, UnauthorizedException {
        mCustomer c = new mCustomer("Alex");
        EZShop.getInstance().getListOfCustomers().add(c);
        assertSame(-1,EZShop.getInstance().defineCustomer("Alex"));
    }
    @Test
    public void dbErrorTest() throws InvalidCustomerNameException, UnauthorizedException {
        mCustomer c = new mCustomer("Alex");
        c.insert();
        mCustomer.reset();
        assertSame(-1,EZShop.getInstance().defineCustomer("Alex"));
        c.delete();
    }
    @Test
    public void customerCreatedTest() throws InvalidCustomerNameException, UnauthorizedException {
        assertNotSame(-1,EZShop.getInstance().defineCustomer("Alex"));
        EZShop.getInstance().getListOfCustomers().get(0).delete();
    }
    @AfterClass
    public static void clean() {
        EZShop.getInstance().reset();
    }
}
