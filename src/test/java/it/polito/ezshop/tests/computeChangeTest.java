package it.polito.ezshop.tests;

import org.junit.Test;

import static org.junit.Assert.*;

public class computeChangeTest {
    @Test
    public void testCorrectChange(){
        assertEquals(5 ,it.polito.ezshop.data.EZShop.getInstance().computeChange(10,5), 0); //delta is error margin from expected-function
    }
    @Test
    public void testInvalidCost1(){
        assertThrows(IllegalArgumentException.class, () -> it.polito.ezshop.data.EZShop.getInstance().computeChange(10,20));
    }
    @Test
    public void testInvalidCost2(){
        assertThrows(IllegalArgumentException.class, () -> it.polito.ezshop.data.EZShop.getInstance().computeChange(10,-1));
    }
    @Test
    public void testInvalidCash(){
        assertThrows(IllegalArgumentException.class, () -> it.polito.ezshop.data.EZShop.getInstance().computeChange(-5,2));
    }


}