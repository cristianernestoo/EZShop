package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mCustomer;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class modifyCustomerTest {
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
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().modifyCustomer(1,"Cust","1010101010"));
    }
    @Test
    public void invalidName1Test(){
        assertThrows(InvalidCustomerNameException.class, () -> EZShop.getInstance().modifyCustomer(1,null,"1010101010"));
    }
    @Test
    public void invalidName2Test(){
        assertThrows(InvalidCustomerNameException.class, () -> EZShop.getInstance().modifyCustomer(1,"","1010101010"));
    }
    @Test
    public void nameAlreadyUsedTest() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        mCustomer c = new mCustomer("Alex");
        EZShop.getInstance().getListOfCustomers().add(c);
        EZShop.getInstance().getListOfCustomers().add(new mCustomer("Alexa"));
        assertFalse(EZShop.getInstance().modifyCustomer(1,"Alexa","1010101010"));
    }
    @Test
    public void cardAlreadyExistsTest() {
        mCustomer c = new mCustomer(1,"Alex","1010101010",10);
        EZShop.getInstance().getListOfCustomers().add(c);
        assertThrows(InvalidCustomerNameException.class, () -> EZShop.getInstance().modifyCustomer(1,"","1010101010"));
    }
    @Test
    public void noCustomerFoundTest() throws InvalidCustomerIdException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException {
        assertFalse(EZShop.getInstance().modifyCustomer(1,"Alex","1010101010"));
    }
    @Test
    public void cardRemovedModifyTest() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        mCustomer c = new mCustomer(1,"Alex","1010101010",10);
        EZShop.getInstance().getListOfCustomers().add(c);
        c.insert();
        assertTrue(EZShop.getInstance().modifyCustomer(1,"Cust",""));
        assertEquals("Cust",c.getCustomerName());
        assertEquals("",c.getCustomerCard());
        c.delete();
    }
    @Test
    public void cardUntouchedModifyTest() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        mCustomer c = new mCustomer(1,"Alex","1010101010",10);
        EZShop.getInstance().getListOfCustomers().add(c);
        c.insert();
        assertTrue(EZShop.getInstance().modifyCustomer(1,"Cust",null));
        assertEquals("Cust",c.getCustomerName());
        assertEquals("1010101010",c.getCustomerCard());
        c.delete();
    }
    @Test
    public void customerModifiedTest() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        mCustomer c = new mCustomer(1,"Alex","1010101010",10);
        EZShop.getInstance().getListOfCustomers().add(c);
        c.insert();
        assertTrue(EZShop.getInstance().modifyCustomer(1,"Cust","3030303030"));
        assertEquals("Cust",c.getCustomerName());
        assertEquals("3030303030",c.getCustomerCard());
        c.delete();
    }
    @AfterClass
    public static void clean() {
        EZShop.getInstance().reset();
    }
}
