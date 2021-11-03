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

public class modifyPointsOnCardTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().modifyPointsOnCard("4485370086510891",1));
    }
    @Test
    public void testCorrect() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        String customerCard = "4485370086";
        String customerName = "Mario Rossi";
        Integer id = EZShop.getInstance().defineCustomer(customerName);
        EZShop.getInstance().modifyCustomer(id,customerName,customerCard);
        assertTrue(EZShop.getInstance().modifyPointsOnCard(customerCard,2));
    }
    @Test
    public void testCardAbsent() throws UnauthorizedException, InvalidCustomerCardException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Cashier"));
        assertFalse(EZShop.getInstance().modifyPointsOnCard("4485370086",2));
    }
    @Test
    public void testCardAbsent2() throws UnauthorizedException, InvalidCustomerCardException, InvalidCustomerNameException, InvalidCustomerIdException {
        String customerCard = "4485370086";
        String customerName = "Mario Rossi";
        EZShop.getInstance().defineCustomer("Massimo Verdi");
        Integer id = EZShop.getInstance().defineCustomer(customerName);
        EZShop.getInstance().modifyCustomer(id,customerName,customerCard);
        assertFalse(EZShop.getInstance().modifyPointsOnCard("1234567890",2));
    }
    @Test
    public void testNotEnoughPoints() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        String customerCard = "4485370086";
        String customerName = "Mario Rossi";
        Integer id = EZShop.getInstance().defineCustomer(customerName);
        EZShop.getInstance().modifyCustomer(id,customerName,customerCard);
        EZShop.getInstance().modifyPointsOnCard(customerCard,2);
        assertFalse(EZShop.getInstance().modifyPointsOnCard(customerCard,-5));
    }
    @Test
    public void testCustomerCardNull() {
        assertThrows(InvalidCustomerCardException.class, () -> EZShop.getInstance().modifyPointsOnCard(null,2));
    }
    @Test
    public void testCustomerCardEmpty() {
        assertThrows(InvalidCustomerCardException.class, () -> EZShop.getInstance().modifyPointsOnCard("",2));
    }
    @Test
    public void testCustomerCardInvalidFormat() {
        assertThrows(InvalidCustomerCardException.class, () -> EZShop.getInstance().modifyPointsOnCard("123",2));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
