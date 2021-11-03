package it.polito.ezshop.tests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.model.mCustomer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.security.InvalidParameterException;

public class mCustomerTest {
    @BeforeClass
    public static void cleanBefore(){
        EZShop.getInstance().reset();
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
	@Test
	public void parameterFailTest() {
		mCustomer mte = new mCustomer(1,"User","1010101010",2);
		new mCustomer(5,"Alme","1010101010",2);
		mCustomer.reset();
		assertThrows(InvalidParameterException.class, ()->new mCustomer(-1,null,null,null));
		assertThrows(InvalidParameterException.class, ()->new mCustomer(""));
		assertThrows(InvalidParameterException.class, ()->mte.setId(-1));
		assertThrows(InvalidParameterException.class, ()->mte.setCustomerName(""));
		assertThrows(InvalidParameterException.class, ()->mte.setPoints(-1));
		assertThrows(InvalidParameterException.class, ()->mte.setCustomerName(null));
		assertThrows(InvalidParameterException.class, ()->mte.update(-1,null,null));
	}
	@Test
	public void equalsTest() {
		mCustomer mte = new mCustomer(1,"User","1010101010",2);
        mCustomer mtae = new mCustomer(1,"User","1010101010",2);
		mCustomer mts = new mCustomer(5,"USD","1010101010",2);
        mCustomer.reset();
		assertFalse(mte.equals(mts));
		assertTrue(mte.equals(mtae));
	}
    @Test
    public void getCustomerNameTest(){
        mCustomer mc = new mCustomer("test");
        mCustomer.reset();
        mc.setCustomerName("tests");
        assertEquals("tests",mc.getCustomerName());
    }
    @Test
    public void setCustomerNameTest(){
        mCustomer mc = new mCustomer("test");
        mCustomer.reset();
        mc.setCustomerName("tests");
        assertEquals("tests",mc.getCustomerName());
    }
    @Test
    public void getCustomerCardTest(){
        mCustomer mc = new mCustomer(1,"test","1111111111",0);
        mCustomer.reset();
        mc.setCustomerCard("2222222222");
        assertEquals("2222222222",mc.getCustomerCard());
    }
    @Test
    public void setCustomerCardTest(){
        mCustomer mc = new mCustomer(1,"test","1111111111",0);
        mCustomer.reset();
        mc.setCustomerCard("2222222222");
        assertEquals("2222222222",mc.getCustomerCard());
    }
    @Test
    public void getIdTest(){
        mCustomer mc = new mCustomer("test");
        mCustomer.reset();
        mc.setId(2);
        assertEquals(2,mc.getId(),0);
    }
    @Test
    public void setIdTest(){
        mCustomer mc = new mCustomer("test");
        mCustomer.reset();
        mc.setId(2);
        assertEquals(2,mc.getId(),0);
    }
    @Test
    public void getPointsTest(){
        mCustomer mc = new mCustomer("test");
        mCustomer.reset();
        mc.setPoints(32);
        assertEquals(32,mc.getPoints(),0);
    }
    @Test
    public void setPointsTest(){
        mCustomer mc = new mCustomer("test");
        mCustomer.reset();
        mc.setPoints(32);
        assertEquals(32,mc.getPoints(),0);
    }
    
    @Test
    public void customerInsertFailTest(){
        mCustomer bo = new mCustomer(999999,"Name","8787339485",10);
        mCustomer.reset();
        bo.insert();
        assertFalse(bo.insert());
        bo.delete();
    }
    @Test
    public void customerInsertTest(){
        mCustomer bo = new mCustomer(999999,"Name","8787339485",10);
        mCustomer.reset();
        assertTrue(bo.insert());
        bo.delete();
    }

    
    @Test
    public void customerUpdateTest(){
        mCustomer bo = new mCustomer(999999,"Name","8787339485",10);
        mCustomer.reset();
        bo.insert();
        assertTrue(bo.update(bo.getPoints()+20,bo.getCustomerName(), bo.getCustomerCard()));
        bo.delete();
    }
    

    @Test
    public void customerDeleteTest(){
        mCustomer bo = new mCustomer(999999,"Name","8787339485",10);
        mCustomer.reset();
        bo.insert();
        assertTrue(bo.delete());
    }
}
