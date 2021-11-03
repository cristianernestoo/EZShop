package it.polito.ezshop.tests;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.model.mReturnTransaction;
import it.polito.ezshop.model.mTicketEntry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class mReturnTransactionTest {
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
        mReturnTransaction mst = new mReturnTransaction(1,1,1,0.9,0,new LinkedList<>());
        new mReturnTransaction(10,1,1,0.9,0,new LinkedList<>());
		mReturnTransaction.reset();
		assertThrows(InvalidParameterException.class, ()->new mReturnTransaction(-1,-1,-1,-1,-1,null));
		assertThrows(InvalidParameterException.class, ()->new mReturnTransaction(-1,-1));
		assertThrows(InvalidParameterException.class, ()->mst.setBalanceId(-1));
		assertThrows(InvalidParameterException.class, ()->mst.addEntries(null));
	}
	
	@Test
	public void equalsTest() {
		mReturnTransaction mst = new mReturnTransaction(1,1,1,0.9,0,new LinkedList<>());
        mReturnTransaction msta = new mReturnTransaction(1,1,1,0.9,0,new LinkedList<>());
        mReturnTransaction msf = new mReturnTransaction(10,1,1,0.9,0,new LinkedList<>());
        mReturnTransaction.reset();
        assertFalse(mst.equals(msf));
        assertTrue(mst.equals(msta));
	}
	
    @Test
    public void getBalanceIdTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mReturnTransaction mrt = new mReturnTransaction(1,1,1,1,0.1,entries);
        mReturnTransaction.reset();
        mrt.setBalanceId(2);
        assertEquals(2,mrt.getBalanceId(),0);
    }
    @Test
    public void setBalanceIdTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mReturnTransaction mrt = new mReturnTransaction(1,1,1,1,0.1,entries);
        mReturnTransaction.reset();
        mrt.setBalanceId(2);
        assertEquals(2,mrt.getBalanceId(),0);
    }
    @Test
    public void getEntriesTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mReturnTransaction mrt = new mReturnTransaction(1,1,1,1,0.1,entries);
        mReturnTransaction.reset();
        assertEquals(entries,mrt.getEntries());
    }
    @Test
    public void getPriceTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mReturnTransaction mrt = new mReturnTransaction(1,1,1,1,0.1,entries);
        mReturnTransaction.reset();
        assertEquals(1,mrt.getPrice(),0);
    }
    @Test
    public void getReturnIdTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mReturnTransaction mrt = new mReturnTransaction(1,1,1,1,0.1,entries);
        mReturnTransaction.reset();
        assertEquals(1,mrt.getReturnId(),0);
    }
    @Test
    public void getSaleIdTest(){
        mReturnTransaction mrt = new mReturnTransaction(1,0.1);
        mReturnTransaction.reset();
        assertEquals(1,mrt.getSaleId(),0);
    }
    @Test
    public void addEntries(){
        mTicketEntry mte = new mTicketEntry("333333333331",1,"test",1,1,0.1);
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mReturnTransaction mrt = new mReturnTransaction(1,1,1,1,0.1,entries);
        mReturnTransaction.reset();
        mrt.addEntries(mte);
        assertEquals(mte,mrt.getEntries().get(0));
    }
    
    @Test
    public void computePriceTest(){
        mTicketEntry mte = new mTicketEntry("333333333331",1,"test",1,1,0);
        mTicketEntry mte2 = new mTicketEntry("333333333331",1,"test",1,1,0);
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        entries.add(mte);
        entries.add(mte2);
        mReturnTransaction mst = new mReturnTransaction(1,1,1,1,0,entries);
        mReturnTransaction.reset();
        mst.computePrice();
        assertEquals(2,mst.getPrice(),0);
    }
    

    @Test
    public void returnInsertTest(){
        mTicketEntry a = new mTicketEntry(999999,999999,"555555555555","Desc",1,1,0);
        mTicketEntry b = new mTicketEntry(1000000,999999,"333333333331","Desc",1,1,0);
        mReturnTransaction bo = new mReturnTransaction(999999,999999,999999,1,0,new LinkedList<>());
        mReturnTransaction bs = new mReturnTransaction(999999,0);
        bo.addEntries(a);
        bo.addEntries(b);
        assertTrue(bo.insert());
        assertTrue(bs.insert());
        bo.delete();
        bs.delete();
        mReturnTransaction.reset();
    }
    @Test
    public void returnFailTest(){
        mReturnTransaction bo = new mReturnTransaction(999999,999999,999999,1,0,new LinkedList<>());
        mReturnTransaction.reset();
        bo.insert();
        assertFalse(bo.insert());
        bo.delete();
    }
    

    @Test
    public void returnUpdateTest(){
        mReturnTransaction bo = new mReturnTransaction(999999,999999,999999,1,0,new LinkedList<>());
        mReturnTransaction.reset();
        bo.insert();
        assertTrue(bo.update(2));
        assertTrue(bo.update(null));
        bo.delete();
    }

    
    @Test
    public void returnDeleteTest(){
        mTicketEntry a = new mTicketEntry(999999,999999,"555555555555","Desc",1,1,0);
        mTicketEntry b = new mTicketEntry(999999,999999,"333333333331","Desc",1,1,0);
        mReturnTransaction bo = new mReturnTransaction(999999,999999,999999,1,0,new LinkedList<>());
        mReturnTransaction.reset();
        bo.addEntries(a);
        bo.addEntries(b);
        bo.insert();
        assertTrue(bo.delete());
    }
}
