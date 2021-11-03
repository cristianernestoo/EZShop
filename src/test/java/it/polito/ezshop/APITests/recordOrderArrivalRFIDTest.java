package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidRFIDException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mOrder;
import it.polito.ezshop.model.mPosition;
import it.polito.ezshop.model.mProductType;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class recordOrderArrivalRFIDTest {
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
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().recordOrderArrivalRFID(1,"000000000001"));
    }
    @Test
    public void unauthorized2Test(){
        EZShop.getInstance().setLoggedUser(new mUser(1,"User","User","Cashier"));
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().recordOrderArrivalRFID(1,"000000000001"));
    }
    @Test
    public void invalidOrderIDTest(){
        assertThrows(InvalidOrderIdException.class,() -> EZShop.getInstance().recordOrderArrivalRFID(null,"000000000001"));
    }
    @Test
    public void invalidOrderID2Test(){
        assertThrows(InvalidOrderIdException.class,() -> EZShop.getInstance().recordOrderArrivalRFID(-1,"000000000001"));
    }
    @Test
    public void noOrderFoundTest() throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException, InvalidRFIDException{
        assertFalse(EZShop.getInstance().recordOrderArrivalRFID(1,"000000000001"));
    }
    @Test
    public void orderCompletedTest() throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException , InvalidRFIDException{
        mOrder o = new mOrder(1,1,"555555555555",20,1,"COMPLETED");
        EZShop.getInstance().getListOfOrders().add(o);
        assertTrue(EZShop.getInstance().recordOrderArrivalRFID(1,"000000000001"));
    }
    @Test
    public void orderWrongStateTest() throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException, InvalidRFIDException {
        mOrder o = new mOrder(1,1,"555555555555",20,1,"ISSUED");
        EZShop.getInstance().getListOfOrders().add(o);
        assertFalse(EZShop.getInstance().recordOrderArrivalRFID(1,"000000000001"));
    }
    @Test
    public void productNotRegisteredTest() throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException, InvalidRFIDException {
        mOrder o = new mOrder("555555555555",1,20,1,"PAYED");
        EZShop.getInstance().getListOfOrders().add(o);
        o.insert();
        assertFalse(EZShop.getInstance().recordOrderArrivalRFID(1,"000000000001"));
        o.delete();
    }
    @Test
    public void productNotLocatedTest(){
        mOrder o = new mOrder("555555555555",1,20,1,"PAYED");
        EZShop.getInstance().getListOfOrders().add(o);
        mProductType p = new mProductType(3,null,"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        o.insert();
        assertThrows(InvalidLocationException.class, () -> EZShop.getInstance().recordOrderArrivalRFID(1,"000000000001"));
        o.delete();
    }
    @Test
    public void orderCompleteTest() throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException,InvalidRFIDException {
        mOrder o = new mOrder("555555555555",1,20,1,"PAYED");
        EZShop.getInstance().getListOfOrders().add(o);
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        o.insert();
        assertTrue(EZShop.getInstance().recordOrderArrivalRFID(1,"000000000001"));
        o.delete();
        EZShop.getInstance().reset();
    }
    @Test
    public void invalidRFIDTest(){
        assertThrows(InvalidRFIDException.class, () -> EZShop.getInstance().recordOrderArrivalRFID(1,"-000000000001"));
        assertThrows(InvalidRFIDException.class, () -> EZShop.getInstance().recordOrderArrivalRFID(1,"00000001"));
        assertThrows(InvalidRFIDException.class, () -> EZShop.getInstance().recordOrderArrivalRFID(1,"aa0000000001"));
        assertThrows(InvalidRFIDException.class, () -> EZShop.getInstance().recordOrderArrivalRFID(1,null));
    }
    @Test
    public void rfidNotUniqueTest() throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException,InvalidRFIDException {
        mOrder o = new mOrder("555555555555",1,20,1,"PAYED");
        EZShop.getInstance().getListOfOrders().add(o);
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        o.insert();
        EZShop.getInstance().recordOrderArrivalRFID(1,"000000000001");
        o.delete();
        o.setStatus("PAYED");
        assertThrows(InvalidRFIDException.class, () -> EZShop.getInstance().recordOrderArrivalRFID(1,"000000000001"));
    }
    @AfterClass
    public static void clean() {
        EZShop.getInstance().reset();
    }
}
