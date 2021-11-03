package it.polito.ezshop.tests;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.model.mOrder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.security.InvalidParameterException;

public class mOrderTest {
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
        mOrder mst = new mOrder(1,1,"555555555555",1,1,"ISSUED");
        new mOrder(5,1,"555555555555",1,1,"ISSUED");
		mOrder.reset();
		assertThrows(InvalidParameterException.class, () -> new mOrder(-1,-1,null,-1,-1,null));
		assertThrows(InvalidParameterException.class, () -> new mOrder(1,1,"555555555555",1,1,"A"));
		assertThrows(InvalidParameterException.class, () -> new mOrder(null,-1,-1,-1,null));
		assertThrows(InvalidParameterException.class, () -> new mOrder("555555555555",1,1,1,"A"));
		assertThrows(InvalidParameterException.class, () -> mst.setBalanceId(-1));
		assertThrows(InvalidParameterException.class, () -> mst.setOrderId(-1));
		assertThrows(InvalidParameterException.class, () -> mst.setPricePerUnit(-1));
		assertThrows(InvalidParameterException.class, () -> mst.setProductCode(null));
		assertThrows(InvalidParameterException.class, () -> mst.setQuantity(-1));
		assertThrows(InvalidParameterException.class, () -> mst.setStatus(null));
		assertThrows(InvalidParameterException.class, () -> mst.setStatus("A"));
		assertThrows(InvalidParameterException.class, () -> mst.update(null, -1, -1, null, -1));
		assertThrows(InvalidParameterException.class, () -> mst.update(null, -1, -1, "A", -1));
	}
	
	@Test
	public void equalsTest() {
        mOrder mst = new mOrder(1,1,"555555555555",1,1,"ISSUED");
        mOrder msta = new mOrder(1,1,"555555555555",1,1,"ISSUED");
        mOrder msf = new mOrder(5,1,"555555555555",1,1,"ISSUED");
        mOrder.reset();
        assertFalse(mst.equals(msf));
        assertTrue(mst.equals(msta));
	}
	
    @Test
    public void getBalanceIdTest(){
        mOrder mo = new mOrder(1,1,"1111111111",1,1,"ISSUED");
        mOrder.reset();
        mo.setBalanceId(2);
        assertEquals(2,mo.getBalanceId(),0);
    }
    @Test
    public void setBalanceIdTest(){
        mOrder mo = new mOrder("1111111111",1,1,1,"ISSUED");
        mOrder.reset();
        mo.setBalanceId(2);
        assertEquals(2,mo.getBalanceId(),0);
    }
    @Test
    public void getProductCodeTest(){
        mOrder mo = new mOrder("1111111111",1,1,1,"ISSUED");
        mOrder.reset();
        mo.setProductCode("2222222222");
        assertEquals("2222222222",mo.getProductCode());
    }
    @Test
    public void setProductCodeTest(){
        mOrder mo = new mOrder("1111111111",1,1,1,"ISSUED");
        mOrder.reset();
        mo.setProductCode("2222222222");
        assertEquals("2222222222",mo.getProductCode());
    }
    @Test
    public void getPricePerUnitTest(){
        mOrder mo = new mOrder("1111111111",1,1,1,"ISSUED");
        mOrder.reset();
        mo.setPricePerUnit(2);
        assertEquals(2,mo.getPricePerUnit(),0);
    }
    @Test
    public void setPricePerUnitTest(){
        mOrder mo = new mOrder("1111111111",1,1,1,"ISSUED");
        mOrder.reset();
        mo.setPricePerUnit(2);
        assertEquals(2,mo.getPricePerUnit(),0);
    }
    @Test
    public void getQuantityTest(){
        mOrder mo = new mOrder("1111111111",1,1,1,"ISSUED");
        mOrder.reset();
        mo.setQuantity(5);
        assertEquals(5,mo.getQuantity(),0);
    }
    @Test
    public void setQuantityTest(){
        mOrder mo = new mOrder("1111111111",1,1,1,"ISSUED");
        mOrder.reset();
        mo.setQuantity(5);
        assertEquals(5,mo.getQuantity(),0);
    }
    @Test
    public void getStatusTest(){
        mOrder mo = new mOrder("1111111111",1,1,1,"ISSUED");
        mOrder.reset();
        mo.setStatus("PAYED");
        assertEquals("PAYED",mo.getStatus());
    }
    @Test
    public void setStatusTest(){
        mOrder mo = new mOrder("1111111111",1,1,1,"ISSUED");
        mOrder.reset();
        mo.setStatus("PAYED");
        assertEquals("PAYED",mo.getStatus());
    }
    @Test
    public void getOrderIdTest(){
        mOrder mo = new mOrder("1111111111",1,1,1,"ISSUED");
        mOrder.reset();
        mo.setOrderId(5);
        assertEquals(5,mo.getOrderId(),0);
    }
    @Test
    public void setOrderIdTest(){
        mOrder mo = new mOrder("1111111111",1,1,1,"ISSUED");
        mOrder.reset();
        mo.setOrderId(5);
        assertEquals(5,mo.getOrderId(),0);
    }
    
    @Test
    public void orderInsertTest(){
        mOrder bo = new mOrder(999999,999999,"555555555555",1,1,"PAYED");
        mOrder bs = new mOrder("1111111111",null,1,1,"ISSUED");
        mOrder.reset();
        assertTrue(bo.insert());
        bo.delete();
        assertTrue(bs.insert());
        bs.delete();
    }
    @Test
    public void orderInsertFailTest(){
        mOrder bo = new mOrder(999999,999999,"555555555555",1,1,"PAYED");
        mOrder.reset();
        bo.insert();
        assertFalse(bo.insert());
        bo.delete();
    }
    

    @Test
    public void orderUpdateTest(){
        mOrder bo = new mOrder(999999,999999,"555555555555",1,1,"PAYED");
        mOrder.reset();
        mOrder.reset();
        bo.insert();
        assertTrue(bo.update(bo.getProductCode(),bo.getQuantity(),bo.getPricePerUnit()+5,bo.getStatus(),bo.getBalanceId()));
        assertTrue(bo.update(bo.getProductCode(),bo.getQuantity(),bo.getPricePerUnit()+5,bo.getStatus(),null));
        bo.delete();
    }
    

    @Test
    public void orderDeleteTest(){
        mOrder bo = new mOrder(999999,999999,"555555555555",1,1,"PAYED");
        mOrder.reset();
        bo.insert();
        assertTrue(bo.delete());
    }
}
