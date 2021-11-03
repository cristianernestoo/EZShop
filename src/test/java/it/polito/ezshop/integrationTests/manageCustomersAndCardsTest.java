package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class manageCustomersAndCardsTest {
    @Before
    public void wipeOut() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        EZShop.getInstance().reset();
        EZShop.getInstance().createUser("test","test","Administrator");
        EZShop.getInstance().login("test","test");
    }
    @Test
    public void Scenario4_1() throws InvalidCustomerNameException, UnauthorizedException {
        Integer id = EZShop.getInstance().defineCustomer("test1");
        assertNotEquals(-1,id,0);
    }
    @Test
    public void Scenario4_2() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        Integer id = EZShop.getInstance().defineCustomer("test1");
        String card = EZShop.getInstance().createCard();
        assertTrue(EZShop.getInstance().attachCardToCustomer(card,id));
    }
    @Test
    public void Scenario4_3() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        EZShop.getInstance().defineCustomer("test1");
        EZShop.getInstance().defineCustomer("test2");
        String card = EZShop.getInstance().createCard();
        EZShop.getInstance().attachCardToCustomer(card,1);
        EZShop.getInstance().getAllCustomers().get(1).setCustomerCard(null);
        assertNull(EZShop.getInstance().getAllCustomers().get(1).getCustomerCard());
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
