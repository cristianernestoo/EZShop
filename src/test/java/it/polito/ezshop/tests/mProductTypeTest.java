package it.polito.ezshop.tests;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.model.mPosition;
import it.polito.ezshop.model.mProduct;
import it.polito.ezshop.model.mProductType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.security.InvalidParameterException;
import java.util.LinkedList;

public class mProductTypeTest {
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
		mPosition p = null;
		mPosition mps = null;
    	try {
    		p = new mPosition("1-a-1");
    		mps = new mPosition(1,"a",2);
    	} catch (InvalidLocationException e) {
    		fail();
    	}
        mProductType mte = new mProductType(1,p,"testa","testa","555555555555",1,1);
        new mProductType(5,mps,"tests","tests","333333333331",1,1);
		mProductType.reset();
		assertThrows(InvalidParameterException.class, ()->new mProductType(-1,null,null,null,null,-1.1,-1));
		assertThrows(InvalidParameterException.class, ()->new mProductType(-1,null,null,null,null,-1.1));
		assertThrows(InvalidParameterException.class, ()->new mProductType(null,null,null,-1.1));
		assertThrows(InvalidParameterException.class, ()->mte.setId(-1));
		assertThrows(InvalidParameterException.class, ()->mte.setBarCode(null));
		assertThrows(InvalidParameterException.class, ()->mte.setPricePerUnit(-1.1));
		assertThrows(InvalidParameterException.class, ()->mte.setProductDescription(null));
		assertThrows(InvalidParameterException.class, ()->mte.setQuantity(-1));
		assertThrows(InvalidParameterException.class, ()->mte.update(null,null, null, null, null, 0));
	}
	@Test
	public void equalsTest() {
		mPosition p = null;
		mPosition mps = null;
    	try {
    		p = new mPosition("1-a-1");
    		mps = new mPosition(1,"a",2);
    	} catch (InvalidLocationException e) {
    		fail();
    	}
        mProductType mte = new mProductType(1,p,"testa","testa","555555555555",1,1);
        mProductType e = new mProductType(1,p,"testa","testa","555555555555",1,1);
        mProductType mts = new mProductType(5,mps,"tests","tests","333333333331",1,1);
        mProductType.reset();
		assertFalse(mte.equals(mts));
		assertTrue(mte.equals(e));
	}
	
    @Test
    public void getQuantityTest() throws InvalidLocationException {
        mPosition mpo = new mPosition(1,"a",1);
        mProductType mpt = new mProductType(1,mpo,"test","test","333333333331",1,1);
        mProductType.reset();
        mpt.setQuantity(5);
        assertEquals(5,mpt.getQuantity(),0);
    }
    @Test
    public void setQuantityTest() throws InvalidLocationException{
        mPosition mpo = new mPosition(1,"a",1);
        mProductType mpt = new mProductType(1,mpo,"test","test","333333333331",1,1);
        mProductType.reset();
        mpt.setQuantity(5);
        assertEquals(5,mpt.getQuantity(),0);
    }
    @Test
    public void getLocationTest() throws InvalidLocationException{
       String location = "1-a-1";
        mPosition mpo = new mPosition(1,"a",1);
        mProductType mpt = new mProductType(1,mpo,"test","test","333333333331",1,1);
        mProductType mps = new mProductType("test","test","333333333331",1);
        mProductType.reset();
        assertEquals(location,mpt.getLocation());
        assertNull(mps.getLocation());
    }
    @Test
    public void setLocationTest() throws InvalidLocationException{
        String location = "0-a-10";
        mPosition mpo = new mPosition(1,"a",1);
        mProductType mpt = new mProductType(1,mpo,"test","test","333333333331",1,1);
        mProductType.reset();
        mpt.setLocation(null);
        assertNull(mpt.getLocation());
        mpt.setLocation("0-a-10");
        assertEquals(location,mpt.getLocation());
        mpt.setLocation("aaaa");
        assertEquals("0-a-10",mpt.getLocation());
    }
    @Test
    public void getNoteTest() throws InvalidLocationException{
        mPosition mpo = new mPosition(1,"a",1);
        mProductType mpt = new mProductType(1,mpo,"test","test","333333333331",2);
        mProductType.reset();
        mpt.setNote("test");
        assertEquals("test",mpt.getNote());
    }
    @Test
    public void setNoteTest() throws InvalidLocationException{
        mPosition mpo = new mPosition(1,"a",1);
        mProductType mpt = new mProductType(1,mpo,"test","test","333333333331",2);
        mProductType.reset();
        mpt.setNote("test");
        assertEquals("test",mpt.getNote());
    }
    @Test
    public void getProductDescriptionTest(){
        mProductType mpt = new mProductType("test","test","333333333331",1);
        mProductType.reset();
        mpt.setProductDescription("test1");
        assertEquals("test1",mpt.getProductDescription());
    }
    @Test
    public void setProductDescriptionTest(){
        mProductType mpt = new mProductType("test","test","333333333331",1);
        mProductType.reset();
        mpt.setProductDescription("test1");
        assertEquals("test1",mpt.getProductDescription());
    }
    @Test
    public void getBarCodeTest(){
        mProductType mpt = new mProductType("test","test","333333333331",1);
        mProductType.reset();
        mpt.setBarCode("5555555555555");
        assertEquals("5555555555555",mpt.getBarCode());
    }
    @Test
    public void setBarCodeTest(){
        mProductType mpt = new mProductType("test","test","333333333331",1);
        mProductType.reset();
        mpt.setBarCode("5555555555555");
        assertEquals("5555555555555",mpt.getBarCode());
    }
    @Test
    public void getPricePerUnitTest(){
        mProductType mpt = new mProductType("test","test","333333333331",1);
        mProductType.reset();
        mpt.setPricePerUnit(5.0);
        assertEquals(5.0,mpt.getPricePerUnit(),0);
    }
    @Test
    public void setPricePerUnitTest(){
        mProductType mpt = new mProductType("test","test","333333333331",1);
        mProductType.reset();
        mpt.setPricePerUnit(5.0);
        assertEquals(5.0,mpt.getPricePerUnit(),0);
    }
    @Test
    public void getIdTest() throws InvalidLocationException{
        mPosition mpo = new mPosition(1,"a",1);
        mProductType mpt = new mProductType(1,mpo,"test","test","333333333331",1,1);
        mProductType.reset();
        mpt.setId(2);
        assertEquals(2,mpt.getId(),0);
    }
    @Test
    public void setIdTest() throws InvalidLocationException{
        mPosition mpo = new mPosition(1,"a",1);
        mProductType mpt = new mProductType(1,mpo,"test","test","333333333331",1,1);
        mProductType.reset();
        mpt.setId(2);
        assertEquals(2,mpt.getId(),0);
    }
    @Test
    public void getPositionTest()throws InvalidLocationException{
        mPosition mpo = new mPosition(1,"a",1);
        mProductType mpt = new mProductType(1,mpo,"test","test","333333333331",1,1);
        mProductType.reset();
        assertEquals(mpo,mpt.getPosition());
    }
    @Test
    public void testSetProductsIncorrect(){
        mProductType pt = new mProductType("","description","55555555555555",2);
        assertThrows(InvalidParameterException.class, () -> pt.setProducts(null));
    }
    @Test
    public void testSetProductsCorrect(){
        mProductType pt = new mProductType("","description","55555555555555",2);
        mProduct p1 = new mProduct("111111111111");
        mProduct p2 = new mProduct("222222222222");
        LinkedList<mProduct> list = new LinkedList<>();
        list.add(p1);
        list.add(p2);
        pt.setProducts(list);
        for(mProduct i:list)
        {
            assertEquals(i, pt.getProducts().get(i.getRFID())) ;
        }
    }
    @Test
    public void testAddProductsIncorrect(){
        mProductType pt = new mProductType("","description","55555555555555",2);
        assertFalse(pt.addProduct(null));
    }
    @Test
    public void testAddProductsCorrect(){
        mProductType pt = new mProductType("","description","55555555555555",2);
        mProduct p = new mProduct("111111111111");
        assertTrue(pt.addProduct(p));
    }
    @Test
    public void testDeleteProductsCorrect(){
        mProductType pt = new mProductType("","description","55555555555555",2);
        mProduct p = new mProduct("111111111111");
        pt.addProduct(p);
        assertTrue(pt.deleteProduct(p.getRFID()));
    }
    @Test
    public void testDeleteProductsIncorrect(){
        mProductType pt = new mProductType("","description","55555555555555",2);
        mProduct p = new mProduct("111111111111");
        pt.addProduct(p);
        assertFalse(pt.deleteProduct("123"));
    }
    @Test
    public void productInsertFailTest(){
    	mPosition p = null;
    	try {
    		p = new mPosition("1-a-1");
    	} catch (InvalidLocationException e) {
    		fail();
    	}
        mProductType bo = new mProductType(999999,p,"Note","Desc","555555555555",1,1);
        mProductType.reset();
        bo.insert();
        assertFalse(bo.insert());
        bo.delete();
    }
    @Test
    public void productInsertTest(){
    	mPosition p = null;
    	try {
    		p = new mPosition("1-a-1");
    	} catch (InvalidLocationException e) {
    		fail();
    	}
        mProductType bo = new mProductType(999999,p,"Note","Desc","555555555555",1,1);
        mProductType mps = new mProductType("test","test","333333333331",1);
        mProductType.reset();
        assertTrue(bo.insert());
        bo.delete();
        assertTrue(mps.insert());
        mps.delete();
    }
    

    @Test
    public void productUpdateTest(){
    	mPosition p = null;
    	try {
    		p = new mPosition("1-a-1");
    	} catch (InvalidLocationException e) {
    		fail();
    	}
        mProductType bo = new mProductType(999999,p,"Note","Desc","555555555555",1,1);
        mProductType.reset();
        bo.insert();
        assertTrue(bo.update(bo.getQuantity()+10,bo.getPosition(),bo.getNote(),bo.getProductDescription(),bo.getBarCode(),bo.getPricePerUnit()));
        assertTrue(bo.update(bo.getQuantity()+10,null,bo.getNote(),bo.getProductDescription(),bo.getBarCode(),bo.getPricePerUnit()));
        bo.delete();
    }
    

    @Test
    public void productDeleteTest(){
    	mPosition p = null;
    	try {
    		p = new mPosition("1-a-1");
    	} catch (InvalidLocationException e) {
    		fail();
    	}
        mProductType bo = new mProductType(999999,p,"Note","Desc","555555555555",1,1);
        mProductType.reset();
        bo.insert();
        assertTrue(bo.delete());
    }
}
