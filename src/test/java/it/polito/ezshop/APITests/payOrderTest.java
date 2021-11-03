package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mBalanceOperation;
import it.polito.ezshop.model.mOrder;
import it.polito.ezshop.model.mProductType;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class payOrderTest {
    @BeforeClass
    public static void wipeout(){
        EZShop.getInstance().reset();
    }
    @Before
    public void preconditions(){
        EZShop.getInstance().setLoggedUser(new mUser(1,"User","User","Administrator"));
        EZShop.getInstance().getInventory().clear();
        mProductType.reset();
        EZShop.getInstance().getBalanceOperations().clear();
        mBalanceOperation.reset();
    }
    @Test
    public void unauthorizedTest(){
        EZShop.getInstance().setLoggedUser(null);
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().payOrder(1));
    }
    @Test
    public void unauthorized2Test(){
        EZShop.getInstance().setLoggedUser(new mUser(1,"User","User","Cashier"));
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().payOrder(1));
    }
    @Test
    public void invalidOrderIDTest(){
        assertThrows(InvalidOrderIdException.class,() -> EZShop.getInstance().payOrder(-1));
    }
    @Test
    public void invalidOrderID2Test(){
        assertThrows(InvalidOrderIdException.class,() -> EZShop.getInstance().payOrder(null));
    }
    @Test
    public void noOrderFoundTest() throws UnauthorizedException, InvalidOrderIdException {
        assertFalse(EZShop.getInstance().payOrder(1));
    }
    @Test
    public void orderPayedTest() throws UnauthorizedException, InvalidOrderIdException {
        mOrder o = new mOrder(1,1,"555555555555",20,1,"PAYED");
        EZShop.getInstance().getListOfOrders().add(o);
        assertTrue(EZShop.getInstance().payOrder(1));
    }
    @Test
    public void orderWrongStateTest() throws UnauthorizedException, InvalidOrderIdException {
        mOrder o = new mOrder(1,1,"555555555555",20,1,"COMPLETED");
        EZShop.getInstance().getListOfOrders().add(o);
        assertFalse(EZShop.getInstance().payOrder(1));
    }
    @Test
    public void orderPayTest() throws UnauthorizedException, InvalidOrderIdException {
        mOrder o = new mOrder("555555555555",null,20,1,"ISSUED");
        EZShop.getInstance().getListOfOrders().add(o);
        o.insert();
        mBalanceOperation bo = new mBalanceOperation(1, LocalDate.now(),1000,"CREDIT");
        EZShop.getInstance().getBalanceOperations().add(bo);
        EZShop.getInstance().computeBalance();
        assertFalse(EZShop.getInstance().payOrder(1));
        o.delete();
        EZShop.getInstance().getBalanceOperations().element().delete();
    }
    @Test
    public void notEnoughBalanceTest() throws UnauthorizedException, InvalidOrderIdException {
        mOrder o = new mOrder("555555555555",null,20,11,"ISSUED");
        EZShop.getInstance().getListOfOrders().add(o);
        o.insert();
        mBalanceOperation bo = new mBalanceOperation(1, LocalDate.now(),10,"CREDIT");
        EZShop.getInstance().getBalanceOperations().add(bo);
        EZShop.getInstance().computeBalance();
        assertFalse(EZShop.getInstance().payOrder(1));
        o.delete();
    }
    @Test
    public void failedBalanceInsertTest() throws UnauthorizedException, InvalidOrderIdException {
        mOrder o = new mOrder("555555555555",null,20,11,"ISSUED");
        EZShop.getInstance().getListOfOrders().add(o);
        o.insert();
        mBalanceOperation bo = new mBalanceOperation(1, LocalDate.now(),10,"CREDIT");
        bo.insert();
        mBalanceOperation.reset();
        EZShop.getInstance().getBalanceOperations().add(bo);
        EZShop.getInstance().computeBalance();
        assertFalse(EZShop.getInstance().payOrder(1));
        o.delete();
        bo.delete();
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
