package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class attachCardToCustomerTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().attachCardToCustomer("4485370086",1));
    }
    @Test
    public void testCorrect() throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerNameException {
        Integer id = EZShop.getInstance().defineCustomer("Mario Rossi");
        assertTrue(EZShop.getInstance().attachCardToCustomer("4485370086", id));
    }
    @Test
    public void testCorrect2() throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerNameException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        Integer id1 = EZShop.getInstance().defineCustomer("Mario Rossi");
        EZShop.getInstance().attachCardToCustomer("4485370086", id1);
        Integer id2 = EZShop.getInstance().defineCustomer("Massimo Verdi");
        assertTrue(EZShop.getInstance().attachCardToCustomer("1234567890", id2));
    }
    @Test
    public void testCardAssignedToAnotherUser() throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerNameException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Cashier"));
        Integer id1 = EZShop.getInstance().defineCustomer("Mario Rossi");
        EZShop.getInstance().attachCardToCustomer("4485370086", id1);
        Integer id2 = EZShop.getInstance().defineCustomer("Massimo Verdi");
        assertFalse(EZShop.getInstance().attachCardToCustomer("4485370086", id2));
    }
    @Test
    public void testCustomerNotFound() throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerCardException {
        assertFalse(EZShop.getInstance().attachCardToCustomer("4485370086", 1));
    }
    @Test
    public void testCustomerIdNull() {
        assertThrows(InvalidCustomerIdException.class, () -> EZShop.getInstance().attachCardToCustomer("4485370086", null));
    }
    @Test
    public void testCustomerIdEqual0() {
        assertThrows(InvalidCustomerIdException.class, () -> EZShop.getInstance().attachCardToCustomer("4485370086", 0));
    }
    @Test
    public void testCustomerIdLessThan0() {
        assertThrows(InvalidCustomerIdException.class, () -> EZShop.getInstance().attachCardToCustomer("4485370086", -2));
    }
    @Test
    public void testCustomerCardNull() {
        assertThrows(InvalidCustomerCardException.class, () -> EZShop.getInstance().attachCardToCustomer(null, 1));
    }
    @Test
    public void testCustomerCardEmpty() {
        assertThrows(InvalidCustomerCardException.class, () -> EZShop.getInstance().attachCardToCustomer("", 1));
    }
    @Test
    public void testCustomerCardInvalidFormat() {
        assertThrows(InvalidCustomerCardException.class, () -> EZShop.getInstance().attachCardToCustomer("123", 1));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
