package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class recordOrderArrivalTest {
    @BeforeClass
    public static void wipeout(){
        EZShop.getInstance().reset();
    }
    @Before
    public void preconditions(){
        EZShop.getInstance().setLoggedUser(new mUser(1,"User","User","Administrator"));
        EZShop.getInstance().getListOfOrders().clear();
        mOrder.reset();
        EZShop.getInstance().getInventory().clear();
        mProductType.reset();
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
    public void invalidOrderIDTest(){
        assertThrows(InvalidOrderIdException.class,() -> EZShop.getInstance().recordOrderArrival(-1));
    }
    @Test
    public void invalidOrderID2Test(){
        assertThrows(InvalidOrderIdException.class,() -> EZShop.getInstance().recordOrderArrival(null));
    }
    @Test
    public void noOrderFoundTest() throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException {
        assertFalse(EZShop.getInstance().recordOrderArrival(1));
    }
    @Test
    public void orderCompletedTest() throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException {
        mOrder o = new mOrder(1,1,"555555555555",20,1,"COMPLETED");
        EZShop.getInstance().getListOfOrders().add(o);
        assertTrue(EZShop.getInstance().recordOrderArrival(1));
    }
    @Test
    public void orderWrongStateTest() throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException {
        mOrder o = new mOrder(1,1,"555555555555",20,1,"ISSUED");
        EZShop.getInstance().getListOfOrders().add(o);
        assertFalse(EZShop.getInstance().recordOrderArrival(1));
    }
    @Test
    public void productNotRegisteredTest() throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException {
        mOrder o = new mOrder("555555555555",1,20,1,"PAYED");
        EZShop.getInstance().getListOfOrders().add(o);
        o.insert();
        assertFalse(EZShop.getInstance().recordOrderArrival(1));
        o.delete();
    }
    @Test
    public void productNotLocatedTest(){
        mOrder o = new mOrder("555555555555",1,20,1,"PAYED");
        EZShop.getInstance().getListOfOrders().add(o);
        mProductType p = new mProductType(3,null,"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        o.insert();
        assertThrows(InvalidLocationException.class, () -> EZShop.getInstance().recordOrderArrival(1));
        o.delete();
    }
    @Test
    public void orderCompleteTest() throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException {
        mOrder o = new mOrder("555555555555",1,20,1,"PAYED");
        EZShop.getInstance().getListOfOrders().add(o);
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        o.insert();
        assertTrue(EZShop.getInstance().recordOrderArrival(1));
        o.delete();
    }
    @AfterClass
    public static void clean() {
        EZShop.getInstance().reset();
    }
}
