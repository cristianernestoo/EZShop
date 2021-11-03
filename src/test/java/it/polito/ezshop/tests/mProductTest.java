package it.polito.ezshop.tests;

import it.polito.ezshop.model.mProduct;
import org.junit.Test;

import java.security.InvalidParameterException;

import static org.junit.Assert.*;

public class mProductTest {
    @Test
    public void equalsTest(){
        mProduct p1 = new mProduct("111111111111");
        mProduct p2 = new mProduct("222222222222");
        mProduct p3 = new mProduct("222222222222");
        assertTrue(p3.equals(p2));
        assertFalse(p1.equals(p2));
    }
    @Test
    public void invalidParameter(){
        assertThrows(InvalidParameterException.class,() -> new mProduct("1"));
    }
    @Test
    public void getRFIDTest(){
        mProduct p = new mProduct("111111111111");
        assertEquals("111111111111",p.getRFID());
    }
}
