package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mOrder;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class getAllOrdersTest {
    @Before
    public void preconditions(){
        EZShop.getInstance().setLoggedUser(new mUser(1,"User","User","Administrator"));
        EZShop.getInstance().getListOfOrders().clear();
        mOrder.reset();
    }
    @Test
    public void unauthorizedTest(){
        EZShop.getInstance().setLoggedUser(null);
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().recordOrderArrival(1));
    }
    @Test
    public void unauthorized2Test(){
        EZShop.getInstance().setLoggedUser(new mUser(1,"User","User","Cashier"));
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().recordOrderArrival(1));
    }
    @Test
    public void allOrderMoreTest() throws UnauthorizedException {
        mOrder o = new mOrder(1,1,"555555555555",20,1,"COMPLETED");
        EZShop.getInstance().getListOfOrders().add(o);
        mOrder os = new mOrder(2,1,"555555555555",20,1,"PAYED");
        EZShop.getInstance().getListOfOrders().add(os);
        assertEquals(EZShop.getInstance().getListOfOrders().get(0).getOrderId(),EZShop.getInstance().getAllOrders().get(0).getOrderId());
        assertEquals(EZShop.getInstance().getListOfOrders().get(1).getOrderId(),EZShop.getInstance().getAllOrders().get(1).getOrderId());
    }
    @Test
    public void allOrderNoneTest() throws UnauthorizedException {
        assertTrue(EZShop.getInstance().getAllOrders().isEmpty());
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
