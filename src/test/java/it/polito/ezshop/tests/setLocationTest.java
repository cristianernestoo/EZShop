package it.polito.ezshop.tests;

import it.polito.ezshop.model.mProductType;
import org.junit.Test;

import static org.junit.Assert.*;

public class setLocationTest {
    @Test
    public void testNullLocation(){
        String location = null;
        mProductType p = new mProductType("note","description", "33333333331", 2);
        String loc = p.getLocation();
        p.setLocation(location);
        assertEquals(loc,p.getLocation());
    }

    @Test
    public void testSetLocation(){
        String location = "1-1-1";
        mProductType p = new mProductType("note","description", "33333333331", 2);
        p.setLocation(location);
        String a = p.getLocation();
        assertEquals(location,a);
    }

}
