package it.polito.ezshop.tests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.model.mProductType;
import it.polito.ezshop.model.mPosition;
import org.junit.Test;

import static org.junit.Assert.*;

public class checkPositionTest {
    @Test
    public void testCorrectPosition() throws InvalidLocationException {
        String position = "1-1-1";
        mPosition a = new mPosition("0-0-0");
        mProductType p = new mProductType(1,a,"test","test_description","333333333331",2);
        EZShop.getInstance().getInventory().put(1,p);
        assertTrue(it.polito.ezshop.data.EZShop.getInstance().checkPosition(position));
    }
    @Test
    public void testCorrectPosition2items() throws InvalidLocationException {
        String position = "1-1-1";
        mPosition a = new mPosition("0-0-0");
        mPosition b = new mPosition("2-2-2");
        mProductType p = new mProductType(1,a,"test","test_description","333333333331",2);
        mProductType p1 = new mProductType(1,b,"test","test_description","333333333331",2);
        EZShop.getInstance().getInventory().put(1,p);
        EZShop.getInstance().getInventory().put(2,p1);
        assertTrue(it.polito.ezshop.data.EZShop.getInstance().checkPosition(position));
    }
    @Test
    public void testEmptyInventory() throws InvalidLocationException {
        String position = "1-1-1";
        assertTrue(it.polito.ezshop.data.EZShop.getInstance().checkPosition(position));
    }
    @Test
    public void testNotAvaiability() throws InvalidLocationException {
        String position = "1-a-1";
        mPosition a = new mPosition(1,"a",1);
        mProductType p = new mProductType(1,a,"test","test_description","333333333331",2);
        EZShop.getInstance().getInventory().put(1,p);
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().checkPosition(position));
    }
    @Test
    public void testInvalidPosition(){
        String position = "aaa";
        assertThrows(InvalidLocationException.class, () -> it.polito.ezshop.data.EZShop.getInstance().checkPosition(position));
    }
}
