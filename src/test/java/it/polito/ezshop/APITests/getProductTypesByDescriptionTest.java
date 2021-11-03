package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mPosition;
import it.polito.ezshop.model.mProductType;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class getProductTypesByDescriptionTest {
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
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().getProductTypesByDescription("Hry"));
    }
    @Test
    public void returnEmptyTest() throws InvalidLocationException, UnauthorizedException {
        mProductType p = new mProductType(3,new mPosition(1,"a",1),"note","","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        assertEquals(p.getBarCode(),EZShop.getInstance().getProductTypesByDescription("").get(0).getBarCode());
    }
    @Test
    public void returnContainsTest() throws InvalidLocationException, UnauthorizedException {
        mProductType p = new mProductType(3,new mPosition(1,"a",1),"note","DEscripTion","555555555555",2.0,1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        assertEquals(p.getBarCode(),EZShop.getInstance().getProductTypesByDescription("deSC").get(0).getBarCode());
        assertEquals(p.getBarCode(),EZShop.getInstance().getProductTypesByDescription("ptioN").get(0).getBarCode());
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
