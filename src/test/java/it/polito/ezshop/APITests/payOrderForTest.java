package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class payOrderForTest {
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
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().payOrderFor("555555555555",10,5));
    }
    @Test
    public void unauthorized2Test(){
        EZShop.getInstance().setLoggedUser(new mUser(1,"User","User","Cashier"));
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().payOrderFor("555555555555",10,5));
    }
    @Test
    public void invalidProductCode1Test(){
        assertThrows(InvalidProductCodeException.class,() -> EZShop.getInstance().payOrderFor(null,10,5));
    }
    @Test
    public void invalidProductCode2Test(){
        assertThrows(InvalidProductCodeException.class,() -> EZShop.getInstance().payOrderFor("55555555",10,5));
    }
    @Test
    public void invalidProductCode3Test(){
        assertThrows(InvalidProductCodeException.class,() -> EZShop.getInstance().payOrderFor("",10,5));
    }
    @Test
    public void invalidQuantityTest(){
        assertThrows(InvalidQuantityException.class,() -> EZShop.getInstance().payOrderFor("555555555555",-1,5));
    }
    @Test
    public void invalidPricePerUnitTest(){
        assertThrows(InvalidPricePerUnitException.class,() -> EZShop.getInstance().payOrderFor("555555555555",1,-5));
    }
    @Test
    public void noProductFoundTest() throws InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException {
        mBalanceOperation bo = new mBalanceOperation(1, LocalDate.now(),1000,"CREDIT");
        EZShop.getInstance().getBalanceOperations().add(bo);
        EZShop.getInstance().computeBalance();
        assertSame(-1,EZShop.getInstance().payOrderFor("555555555555",1,5));
    }
    @Test
    public void noEnoughBalanceTest() throws InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException {
        mBalanceOperation bo = new mBalanceOperation(1, LocalDate.now(),1,"CREDIT");
        EZShop.getInstance().getBalanceOperations().add(bo);
        EZShop.getInstance().computeBalance();
        assertSame(-1,EZShop.getInstance().payOrderFor("555555555555",1,5));
    }

    @Test
    public void orderIssuedTest() throws InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException {
        mBalanceOperation bo = new mBalanceOperation(1, LocalDate.now(),1000,"CREDIT");
        EZShop.getInstance().getBalanceOperations().add(bo);
        EZShop.getInstance().computeBalance();
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        assertNotSame(-1,EZShop.getInstance().issueOrder("555555555555",1,5));
        EZShop.getInstance().getListOfOrders().get(0).delete();
    }

    @Test
    public void orderDBProblemTest() throws InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException {
        mBalanceOperation bo = new mBalanceOperation(1, LocalDate.now(),1000,"CREDIT");
        EZShop.getInstance().getBalanceOperations().add(bo);
        EZShop.getInstance().computeBalance();
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        mOrder o = new mOrder(1,1,"555555555555",2,2,"PAYED");
        o.insert();
        mOrder.reset();
        assertSame(-1,EZShop.getInstance().issueOrder("555555555555",1,5));
        EZShop.getInstance().getListOfOrders().get(0).delete();
    }

    @Test
    public void balanceDBProblemTest() throws InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException {
        mBalanceOperation bo = new mBalanceOperation(1, LocalDate.now(),1000,"CREDIT");
        bo.insert();
        mBalanceOperation.reset();
        EZShop.getInstance().getBalanceOperations().add(bo);
        EZShop.getInstance().computeBalance();
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        assertSame(-1,EZShop.getInstance().payOrderFor("555555555555",1,5));
        bo.delete();
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
