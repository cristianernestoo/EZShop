package it.polito.ezshop.tests;

import org.junit.Test;

import static org.junit.Assert.*;

public class isNumericTest {
    @Test
    public void testCorrectIsANumber(){
        String a = "32";
        assertTrue(it.polito.ezshop.data.EZShop.getInstance().isNumeric(a));
    }
    @Test
    public void testIsNotANumber(){
        String a = "aa";
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().isNumeric(a));
    }
    @Test
    public void testNullNumber(){
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().isNumeric(null));
    }

}
