package it.polito.ezshop.tests;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.model.mTicketEntry;
import it.polito.ezshop.model.mProduct;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.security.InvalidParameterException;
import java.sql.SQLException;

public class mTicketEntryTest {
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
		mTicketEntry mte = new mTicketEntry(1,1,"333333333331","test",15,1,0);
		new mTicketEntry(5,1,"333333333331","test",15,1,0);
		mTicketEntry.reset();
		assertThrows(InvalidParameterException.class, ()->new mTicketEntry(0,0,null,null,0,0,-1));
		assertThrows(InvalidParameterException.class, ()->new mTicketEntry(0,0,"","",0,0,-1));
		assertThrows(InvalidParameterException.class, ()->new mTicketEntry(null,0,null,0,0,-1));
		assertThrows(InvalidParameterException.class, ()->mte.setAmount(-1));
		assertThrows(InvalidParameterException.class, ()->mte.setBarCode(null));
		assertThrows(InvalidParameterException.class, ()->mte.setDiscountRate(1));
		assertThrows(InvalidParameterException.class, ()->mte.setPricePerUnit(-1));
		assertThrows(InvalidParameterException.class, ()->mte.setProductDescription(null));
		assertThrows(InvalidParameterException.class, ()->mte.insert(-1, null));
		assertThrows(InvalidParameterException.class, ()->mte.update(null, -1, -1, -1));
	}
	
    @Test
    public void setQuantityTest() {
        mTicketEntry mte = new mTicketEntry("333333333331",1,"test",1,1,0);
        mTicketEntry.reset();
        mte.setAmount(5);
        assertEquals(5,mte.getAmount(),0);
    }
    @Test
    public void getQuantityTest() {
        mTicketEntry mte = new mTicketEntry("333333333331",1,"test",5,1,0);
        mTicketEntry.reset();
        assertEquals(5,mte.getAmount(),0);
    }
    @Test
    public void getProductDescriptionTest() {
        mTicketEntry mte = new mTicketEntry(1,1,"333333333331","test",15,1,0);
        mTicketEntry.reset();
        assertEquals("test",mte.getProductDescription());
    }
    @Test
    public void setProductDescriptionTest() {
        mTicketEntry mte = new mTicketEntry(1,1,"333333333331","test",15,1,0);
        mTicketEntry.reset();
        mte.setProductDescription("test1");
        assertEquals("test1",mte.getProductDescription());
    }
    @Test
    public void getBarCodeTest() {
        mTicketEntry mte = new mTicketEntry("333333333331",1,"test",1,1,0);
        mTicketEntry.reset();
        assertEquals("333333333331",mte.getBarCode());
    }
    @Test
    public void setBarCodeTest() {
        mTicketEntry mte = new mTicketEntry("333333333331",1,"test",1,1,0);
        mTicketEntry.reset();
        mte.setBarCode("555555555555");
        assertEquals("555555555555",mte.getBarCode());
    }
    @Test
    public void getPricePerUnitTest() {
        mTicketEntry mte = new mTicketEntry(1,1,"333333333331","test",15,1,0);
        mTicketEntry.reset();
        assertEquals(1.0,mte.getPricePerUnit(),0);
    }
    @Test
    public void setPricePerUnitTest() {
        mTicketEntry mte = new mTicketEntry(1,1,"333333333331","test",15,1,0);
        mTicketEntry.reset();
        mte.setPricePerUnit(2.0);
        assertEquals(2.0,mte.getPricePerUnit(),0);
    }
    @Test
    public void getDiscountRateTest() {
        mTicketEntry mte = new mTicketEntry("333333333331",1,"test",1,1,0);
        mTicketEntry.reset();
        assertEquals(0,mte.getDiscountRate(),0);
    }
    @Test
    public void setDiscountRateTest() {
        mTicketEntry mte = new mTicketEntry("333333333331",1,"test",1,1,0);
        mTicketEntry.reset();
        mte.setDiscountRate(0.5);
        assertEquals(0.5,mte.getDiscountRate(),0);
    }
    @Test
    public void equalsTest() {
        mTicketEntry mte = new mTicketEntry(1,1,"333333333331","test",15,1,0);
        mTicketEntry mta = new mTicketEntry(1,1,"333333333331","test",15,1,0);
        mTicketEntry.reset();
        assertTrue(mte.equals(mta));
    }
    @Test
    public void notEqualsTest() {
        mTicketEntry mte = new mTicketEntry(1,1,"333333333331","test",15,1,0);
        mTicketEntry mtg = new mTicketEntry(1,1,"333333333331","test",15,1,0.2);
        mTicketEntry mtf = new mTicketEntry(1,1,"333333333331","test",15,3,0.2);
        mTicketEntry mtd = new mTicketEntry(1,1,"333333333331","test",53,3,0.2);
        mTicketEntry mtc = new mTicketEntry(1,1,"333333333331","roas",53,3,0.2);
        mTicketEntry mtb = new mTicketEntry(1,1,"555555555555","roas",53,3,0.2);
        mTicketEntry mta = new mTicketEntry(1,3,"555555555555","roas",53,3,0.2);
        mTicketEntry mts = new mTicketEntry(2,3,"555555555555","roas",53,3,0.2);
        assertFalse(mte.equals(mts));
        assertFalse(mte.equals(mta));
        assertFalse(mte.equals(mtb));
        assertFalse(mte.equals(mtc));
        assertFalse(mte.equals(mtf));
        assertFalse(mte.equals(mtd));
        assertFalse(mte.equals(mtg));
        mTicketEntry.reset();
    }

    @Test
    public void getProductByRFIDTest(){
        mTicketEntry mte = new mTicketEntry(1,1,"333333333331","test",15,1,0);
        mte.addRFID("111111111111");
        assertEquals("111111111111",mte.getProductByRFID("111111111111").getRFID());

    }
    @Test
    public void addRFIDTest(){
        mTicketEntry mte = new mTicketEntry(1,1,"333333333331","test",15,1,0);
        assertTrue(mte.addRFID("111111111111"));
    }
    @Test
    public void addRFIDFAILTest(){
        mTicketEntry mte = new mTicketEntry(1,1,"333333333331","test",15,1,0);
        assertFalse(mte.addRFID("1111111"));
    }
    
    @Test
    public void ticketInsertTest(){
        mTicketEntry bo = new mTicketEntry(999999,999999,"555555555555","Desc",1,1,0);
        mTicketEntry.reset();
        try{
            assertTrue(bo.insert(999999,"SALE"));
        } catch (SQLException e){
            fail();
        }
        bo.delete();
    }
    @Test
    public void ticketInsertFailTest(){
        mTicketEntry bo = new mTicketEntry(999999,999999,"555555555555","Desc",1,1,0);
        mTicketEntry.reset();
        try{
            assertTrue(bo.insert(999999,"SALE"));
        } catch (SQLException e){
            fail();
        }
        assertThrows(SQLException.class, () -> bo.insert(1,"SALE"));
        
        bo.delete();
    }
    

    @Test
    public void ticketUpdateTest(){
        mTicketEntry bo = new mTicketEntry(999999,999999,"555555555555","Desc",1,1,0);
        mTicketEntry.reset();
        try{
            bo.insert(999999,"SALE");
            assertTrue(bo.update(bo.getBarCode(),bo.getProductId(),bo.getAmount()+10,bo.getDiscountRate()));
        } catch (SQLException e){
            fail();
        }
        bo.delete();
    }
    

    @Test
    public void ticketDeleteTest(){
        mTicketEntry bo = new mTicketEntry(999999,999999,"555555555555","Desc",1,1,0);
        mTicketEntry.reset();
        try{
            bo.insert(999999,"SALE");
        } catch (SQLException e){
            fail();
        }
        assertTrue(bo.delete());
    }
}
