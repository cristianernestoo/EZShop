package it.polito.ezshop.tests;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.model.mSaleTransaction;
import it.polito.ezshop.model.mTicketEntry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.LinkedList;

import static org.junit.Assert.*;
public class mSaleTransactionTest {
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
        mSaleTransaction mst = new mSaleTransaction(1,1,0.9,1,new LinkedList<>());
        new mSaleTransaction(10,1,0.9,1,new LinkedList<>());
		mSaleTransaction.reset();
		assertThrows(InvalidParameterException.class, ()->new mSaleTransaction(-1,-1,-1,-1,null));
		assertThrows(InvalidParameterException.class, ()->new mSaleTransaction(-1,-1,null));
		assertThrows(InvalidParameterException.class, ()->mst.setDiscountRate(-1));
		assertThrows(InvalidParameterException.class, ()->mst.setEntries(null));
		assertThrows(InvalidParameterException.class, ()->mst.setTicketNumber(-1));
		assertThrows(InvalidParameterException.class, ()->mst.setPrice(-1));
		assertThrows(InvalidParameterException.class, ()->mst.addEntries(null));
		assertThrows(InvalidParameterException.class, ()->mst.removeEntries(null));
		assertThrows(InvalidParameterException.class, ()->mst.update(-1, null, -1));
	}
	
	@Test
	public void equalsTest() {
        LinkedList<mTicketEntry> l = new LinkedList<>();
		mSaleTransaction mst = new mSaleTransaction(1,1,0.9,1, l);
        mSaleTransaction msta = new mSaleTransaction(1,1,0.9,1, l);
        mSaleTransaction msf = new mSaleTransaction(10,1,0.9,1, l);
        mSaleTransaction.reset();
        assertFalse(mst.equals(msf));
        assertTrue(mst.equals(msta));
	}
	
    @Test
    public void computePriceTest(){
        mTicketEntry mte = new mTicketEntry("333333333331",1,"test",1,1,0);
        mTicketEntry mte2 = new mTicketEntry("333333333331",1,"test",1,1,0);
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        entries.add(mte);
        entries.add(mte2);
        mSaleTransaction mst = new mSaleTransaction(1,1,0.1,1,entries);
        mSaleTransaction.reset();
        mst.computePrice();
        assertEquals(1.80,mst.getPrice(),0);

    }
    @Test
    public void getBalanceIdTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mSaleTransaction mst = new mSaleTransaction(1,1,0.9,1,entries);
        mSaleTransaction.reset();
        assertEquals(1,mst.getBalanceId(),0);
    }
    @Test
    public void getTicketNumberTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mSaleTransaction mst = new mSaleTransaction(1,1,0.9,1,entries);
        mSaleTransaction.reset();
        assertEquals(1,mst.getTicketNumber(),0);
    }
    @Test
    public void setTicketNumberTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mSaleTransaction mst = new mSaleTransaction(1,1,0.9,1,entries);
        mSaleTransaction.reset();
        mst.setTicketNumber(5);
        assertEquals(5,mst.getTicketNumber(),0);
    }
    @Test
    public void getEntriesTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mSaleTransaction mst = new mSaleTransaction(1,1,0.9,1,entries);
        mSaleTransaction.reset();
        assertEquals(entries,mst.getEntries());
    }
    @Test
    public void setEntriesTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mSaleTransaction mst = new mSaleTransaction(1,1,0.9,1,entries);
        mSaleTransaction.reset();
        LinkedList<TicketEntry> entries_1 = new LinkedList<>();
        mst.setEntries(entries_1);
        assertEquals(entries_1, mst.getEntries());
    }
    @Test
    public void getDiscountRateTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mSaleTransaction mst = new mSaleTransaction(0.1,2,entries);
        mSaleTransaction.reset();
        mst.setDiscountRate(0.2);
        assertEquals(0.2,mst.getDiscountRate(),0);
    }
    @Test
    public void setDiscountRateTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mSaleTransaction mst = new mSaleTransaction(0.1,2,entries);
        mSaleTransaction.reset();
        mst.setDiscountRate(0.2);
        assertEquals(0.2,mst.getDiscountRate(),0);
    }
    @Test
    public void getPriceTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mSaleTransaction mst = new mSaleTransaction(0.1,2,entries);
        mSaleTransaction.reset();
        mst.setPrice(50);
        assertEquals(50,mst.getPrice(),0);
    }
    @Test
    public void setPriceTest(){
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mSaleTransaction mst = new mSaleTransaction(0.1,2,entries);
        mSaleTransaction.reset();
        mst.setPrice(50);
        assertEquals(50,mst.getPrice(),0);
    }
    @Test
    public void addEntriesTest(){
        mTicketEntry mte = new mTicketEntry("333333333331",1,"test",1,1,0.1);
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mSaleTransaction mst = new mSaleTransaction(0.1,2,entries);
        mSaleTransaction.reset();
        mst.addEntries(mte);
        mTicketEntry mtee = (mTicketEntry) mst.getEntries().get(0);
        assertEquals(mte,mtee);
    }
    @Test
    public void removeEntriesTest(){
        mTicketEntry mte = new mTicketEntry("333333333331",1,"test",1,1,0.1);
        LinkedList<mTicketEntry> entries = new LinkedList<>();
        mSaleTransaction mst = new mSaleTransaction(0.1,2,entries);
        mSaleTransaction.reset();
        mst.addEntries(mte);
        mst.removeEntries(mte);
    }


    @Test
    public void saleInsertTest(){
        mTicketEntry a = new mTicketEntry(999999,999999,"555555555555","Desc",1,1,0);
        mTicketEntry b = new mTicketEntry(1000000,999999,"333333333331","Desc",1,1,0);
        mSaleTransaction bo = new mSaleTransaction(999999,999999,0,1,new LinkedList<>());
        mSaleTransaction bs = new mSaleTransaction(0,0,new LinkedList<>());
        bo.addEntries(a);
        bo.addEntries(b);
        assertTrue(bo.insert());
        bo.delete();
        assertTrue(bs.insert());
        bs.delete();
        mSaleTransaction.reset();
    }
    @Test
    public void saleInsertFailTest(){
        mSaleTransaction bo = new mSaleTransaction(999999,999999,0,1,new LinkedList<>());
        mSaleTransaction.reset();
        bo.insert();
        assertFalse(bo.insert());
        bo.delete();
    }


    @Test
    public void saleUpdateTest(){
        mSaleTransaction bo = new mSaleTransaction(999999,999999,0,1,new LinkedList<>());
        mSaleTransaction.reset();
        bo.insert();
        assertTrue(bo.update(0,1,1));
        assertTrue(bo.update(0,null,1));
        bo.delete();
    }

    
    @Test
    public void saleDeleteTest(){
        mTicketEntry a = new mTicketEntry(999999,999999,"555555555555","Desc",1,1,0);
        mTicketEntry b = new mTicketEntry(999999,999999,"333333333331","Desc",1,1,0);
        mSaleTransaction bo = new mSaleTransaction(999999,999999,0,1,new LinkedList<>());
        mSaleTransaction.reset();
        bo.addEntries(a);
        bo.addEntries(b);
        bo.insert();
        assertTrue(bo.delete());
    }
    
}
