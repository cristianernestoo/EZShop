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

public class updatePositionTest {
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
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().updatePosition(1,"1-a-1"));
    }
    @Test
    public void invalidProductIDTest() {
        assertThrows(InvalidProductIdException.class,() -> EZShop.getInstance().updatePosition(-1,"1-a-1"));
    }
    @Test
    public void invalidProductID2Test() {
        assertThrows(InvalidProductIdException.class,() -> EZShop.getInstance().updatePosition(null,"1-a-1"));
    }
    @Test
    public void noProductFoundTest() throws UnauthorizedException, InvalidProductIdException, InvalidLocationException {
        assertFalse(EZShop.getInstance().updatePosition(1, "1-a-1"));
    }
    @Test
    public void alreadyAssignedLocTest() throws UnauthorizedException, InvalidProductIdException, InvalidLocationException {
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        mProductType pa = new mProductType(3,new mPosition("1-a-12"),"note","DEscripTion","555555555555",2.0,2);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        EZShop.getInstance().getInventory().put(pa.getId(),pa);
        assertFalse(EZShop.getInstance().updatePosition(1,"1-a-12"));
    }
    @Test
    public void changeLocationTest() throws UnauthorizedException, InvalidProductIdException, InvalidLocationException {
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        p.insert();
        assertTrue(EZShop.getInstance().updatePosition(1,"1-a-3"));
        assertEquals("1-a-3",p.getLocation());
        p.delete();
    }
    @Test
    public void removeLocationTest() throws UnauthorizedException, InvalidProductIdException, InvalidLocationException {
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        p.insert();
        assertTrue(EZShop.getInstance().updatePosition(1,null));
        assertNull(p.getLocation());
        p.delete();
    }
    @Test
    public void removeLocation2Test() throws UnauthorizedException, InvalidProductIdException, InvalidLocationException {
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        p.insert();
        assertTrue(EZShop.getInstance().updatePosition(1,""));
        assertNull(p.getLocation());
        p.delete();
    }
    @Test
    public void wrongFormatTest() throws InvalidLocationException {
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        assertThrows(InvalidLocationException.class,() -> EZShop.getInstance().updatePosition(1,"asssa"));
    }
    @AfterClass
    public static void clean() {
        EZShop.getInstance().reset();
    }
}
