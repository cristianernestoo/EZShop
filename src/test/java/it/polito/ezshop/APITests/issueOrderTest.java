package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mPosition;
import it.polito.ezshop.model.mProductType;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class issueOrderTest {
    @BeforeClass
    public static void wipeout(){
        EZShop.getInstance().reset();
    }
    @Before
    public void preconditions(){
        EZShop.getInstance().setLoggedUser(new mUser(1,"User","User","Administrator"));
        EZShop.getInstance().getInventory().clear();
        mProductType.reset();
    }
    @Test
    public void unauthorizedTest(){
        EZShop.getInstance().setLoggedUser(null);
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().issueOrder("555555555555",10,5));
    }
    @Test
    public void unauthorized2Test(){
        EZShop.getInstance().setLoggedUser(new mUser(1,"User","User","Cashier"));
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().issueOrder("555555555555",10,5));
    }
    @Test
    public void invalidProductCode1Test(){
        assertThrows(InvalidProductCodeException.class,() -> EZShop.getInstance().issueOrder(null,10,5));
    }
    @Test
    public void invalidProductCode2Test(){
        assertThrows(InvalidProductCodeException.class,() -> EZShop.getInstance().issueOrder("55555555",10,5));
    }
    @Test
    public void invalidProductCode3Test(){
        assertThrows(InvalidProductCodeException.class,() -> EZShop.getInstance().issueOrder("",10,5));
    }
    @Test
    public void invalidQuantityTest(){
        assertThrows(InvalidQuantityException.class,() -> EZShop.getInstance().issueOrder("555555555555",-1,5));
    }
    @Test
    public void invalidPricePerUnitTest(){
        assertThrows(InvalidPricePerUnitException.class,() -> EZShop.getInstance().issueOrder("555555555555",1,-5));
    }

    @Test
    public void noProductFoundTest() throws InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException {
        assertSame(-1,EZShop.getInstance().issueOrder("555555555555",1,5));
    }
    @Test
    public void orderIssuedTest() throws InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException {
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        assertNotSame(-1,EZShop.getInstance().issueOrder("555555555555",1,5));
        EZShop.getInstance().getListOfOrders().get(0).delete();
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
