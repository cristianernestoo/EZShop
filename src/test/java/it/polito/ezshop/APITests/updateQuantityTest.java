package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mPosition;
import it.polito.ezshop.model.mProductType;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class updateQuantityTest {
    @BeforeClass
    public static void wipeOut() {
        EZShop.getInstance().reset();
    }
    @Before
    public void preconditions(){
        EZShop.getInstance().setLoggedUser(new mUser(1,"a","a","Administrator"));
        EZShop.getInstance().getInventory().clear();
        mProductType.reset();
    }
    @Test
    public void unauthorizedTest() {
        EZShop.getInstance().setLoggedUser(null);
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().updateQuantity(1,1));
    }
    @Test
    public void invalidProductIDTest() {
        assertThrows(InvalidProductIdException.class,() -> EZShop.getInstance().updateQuantity(-1,1));
    }
    @Test
    public void invalidProductID2Test() {
        assertThrows(InvalidProductIdException.class,() -> EZShop.getInstance().updateQuantity(null,1));
    }
    @Test
    public void noProductFoundTest() throws UnauthorizedException, InvalidProductIdException {
        assertFalse(EZShop.getInstance().updateQuantity(1,1));
    }
    @Test
    public void noLocationTest() throws UnauthorizedException, InvalidProductIdException {
        mProductType p = new mProductType(3,null,"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        assertFalse(EZShop.getInstance().updateQuantity(1,1));
    }
    @Test
    public void notEnoughQuantityTest() throws UnauthorizedException, InvalidProductIdException, InvalidLocationException {
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        p.insert();
        assertFalse(EZShop.getInstance().updateQuantity(1,-4));
        p.delete();
    }
    @Test
    public void addedQuantityTest() throws UnauthorizedException, InvalidProductIdException, InvalidLocationException {
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        p.insert();
        assertTrue(EZShop.getInstance().updateQuantity(1,4));
        assertSame(7,p.getQuantity());
        p.delete();
    }
    @AfterClass
    public static void clean() {
        EZShop.getInstance().reset();
    }
}
