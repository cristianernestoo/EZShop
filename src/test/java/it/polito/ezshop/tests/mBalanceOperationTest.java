package it.polito.ezshop.tests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.model.mBalanceOperation;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.InvalidParameterException;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class mBalanceOperationTest {
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
        mBalanceOperation mst = new mBalanceOperation(1,LocalDate.now(),1,"ORDER");
        new mBalanceOperation(5,LocalDate.now(),1,"ORDER");
        new mBalanceOperation();
		mBalanceOperation.reset();
		assertThrows(InvalidParameterException.class, () -> new mBalanceOperation(-1,null,-1,null));
		assertThrows(InvalidParameterException.class, () -> new mBalanceOperation(1,LocalDate.now(),1,"A"));
		assertThrows(InvalidParameterException.class, () -> new mBalanceOperation(1,"A"));
		assertThrows(InvalidParameterException.class, () -> new mBalanceOperation(-1,null));
		assertThrows(InvalidParameterException.class, () -> mst.setBalanceId(-1));
		assertThrows(InvalidParameterException.class, () -> mst.setDate(null));
		assertThrows(InvalidParameterException.class, () -> mst.setType("a"));
		assertThrows(InvalidParameterException.class, () -> mst.setType(null));
	}
	
	@Test
	public void equalsTest() {
        mBalanceOperation mst = new mBalanceOperation(1,LocalDate.now(),1,"ORDER");
        mBalanceOperation msta = new mBalanceOperation(1,LocalDate.now(),1,"ORDER");
        mBalanceOperation msf = new mBalanceOperation(5,LocalDate.now(),1,"ORDER");
        mBalanceOperation.reset();
        assertFalse(mst.equals(msf));
        assertTrue(mst.equals(msta));
	}
	
    @Test
    public void getBalanceIdTest(){
        mBalanceOperation mbo = new mBalanceOperation(60,"CREDIT");
        mBalanceOperation.reset();
        mbo.setBalanceId(1);
        assertEquals(1,mbo.getBalanceId());
    }
    @Test
    public void setBalanceIdTest(){
        mBalanceOperation mbo = new mBalanceOperation();
        mBalanceOperation.reset();
        mbo.setBalanceId(2);
        assertEquals(2,mbo.getBalanceId());
    }
    @Test
    public void getDateTest(){
        mBalanceOperation mbo = new mBalanceOperation();
        mBalanceOperation.reset();
        mbo.setDate(LocalDate.now());
        assertEquals(LocalDate.now(),mbo.getDate());
    }
    @Test
    public void setDateTest(){
        mBalanceOperation mbo = new mBalanceOperation();
        mBalanceOperation.reset();
        mbo.setDate(LocalDate.now());
        assertEquals(LocalDate.now(),mbo.getDate());
    }
    @Test
    public void getMoneyTest(){
        mBalanceOperation mbo = new mBalanceOperation();
        mBalanceOperation.reset();
        mbo.setMoney(50);
        assertEquals(50,mbo.getMoney(),0);
    }
    @Test
    public void setMoneyTest(){
        mBalanceOperation mbo = new mBalanceOperation();
        mBalanceOperation.reset();
        mbo.setMoney(50);
        assertEquals(50,mbo.getMoney(),0);
    }
    @Test
    public void getTypeTest(){
        mBalanceOperation mbo = new mBalanceOperation();
        mBalanceOperation.reset();
        mbo.setType("CREDIT");
        assertEquals("CREDIT",mbo.getType());
    }
    @Test
    public void setTypeTest(){
        mBalanceOperation mbo = new mBalanceOperation();
        mBalanceOperation.reset();
        mbo.setType("CREDIT");
        assertEquals("CREDIT",mbo.getType());
    }
    
    @Test
    public void balanceInsertTest(){
        mBalanceOperation bo = new mBalanceOperation(999999,LocalDate.now(),1,"ORDER");
        mBalanceOperation.reset();
        assertTrue(bo.insert());
        bo.delete();
    }
    @Test
    public void balanceInsertFailTest(){
        mBalanceOperation bo = new mBalanceOperation(999999,LocalDate.now(),1,"ORDER");
        mBalanceOperation.reset();
        bo.insert();
        assertFalse(bo.insert());
        bo.delete();
    }
    

    @Test
    public void balanceUpdateTest(){
        mBalanceOperation bo = new mBalanceOperation(999999,LocalDate.now(),1,"ORDER");
        mBalanceOperation.reset();
        bo.insert();
        bo.setMoney(10);
        assertTrue(bo.update());
        bo.delete();
    }
    

    @Test
    public void balanceDeleteTest(){
        mBalanceOperation bo = new mBalanceOperation(999999,LocalDate.now(),1,"ORDER");
        mBalanceOperation.reset();
        bo.insert();
        assertTrue(bo.delete());
    }
}
