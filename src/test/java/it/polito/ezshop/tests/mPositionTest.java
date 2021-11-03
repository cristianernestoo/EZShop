package it.polito.ezshop.tests;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.model.mPosition;
import org.junit.Test;
import static org.junit.Assert.*;
public class mPositionTest {
	@Test
    public void equalsTest() throws InvalidLocationException {
        mPosition mpo = new mPosition(0,"a",0);
        mPosition mpoa = new mPosition(0,"a",0);
        mPosition mps = new mPosition(1,"a",1);
        assertTrue(mpoa.equals(mpo));
        assertFalse(mpo.equals(mps));
    }
	@Test
    public void InvalidInputTest() throws InvalidLocationException {
        mPosition mpo = new mPosition("1-1-1");
        assertThrows(InvalidLocationException.class,() -> mpo.setAisleNumber(null));
        assertThrows(InvalidLocationException.class,() -> mpo.setLevelNumber(null));
        assertThrows(InvalidLocationException.class,() -> mpo.setRackAlphabeticIdentifier(null));
        assertThrows(InvalidLocationException.class,() -> new mPosition(null));
        assertThrows(InvalidLocationException.class,() -> new mPosition("aa-aa-aa"));
        assertThrows(InvalidLocationException.class,() -> new mPosition("1--1"));
        assertThrows(InvalidLocationException.class,() -> new mPosition(null,null,null));
    }
    @Test
    public void getAisleNumberTest() throws InvalidLocationException {
        mPosition mpo = new mPosition(0,"a",0);
        mpo.setAisleNumber(2);
        assertEquals(2,mpo.getAisleNumber(),0);
    }
    @Test
    public void setAisleNumberTest() throws InvalidLocationException {
        mPosition mpo = new mPosition(0,"a",0);
        mpo.setAisleNumber(2);
        assertEquals(2,mpo.getAisleNumber(),0);
    }
    @Test
    public void getRackAlphabeticIdentifierTest() throws InvalidLocationException {
        mPosition mpo = new mPosition("0-a-0");
        mpo.setRackAlphabeticIdentifier("b");
        assertEquals("b",mpo.getRackAlphabeticIdentifier());
    }
    @Test
    public void setRackAlphabeticIdentifierTest() throws InvalidLocationException {
        mPosition mpo = new mPosition(0,"a",0);
        mpo.setRackAlphabeticIdentifier("b");
        assertEquals("b",mpo.getRackAlphabeticIdentifier());
    }
    @Test
    public void getLevelNumberTest() throws InvalidLocationException {
        mPosition mpo = new mPosition(0,"a",0);
        mpo.setLevelNumber(2);
        assertEquals(2,mpo.getLevelNumber(),0);
    }
    @Test
    public void setLevelNumberTest() throws InvalidLocationException {
        mPosition mpo = new mPosition(0,"a",0);
        mpo.setLevelNumber(2);
        assertEquals(2,mpo.getLevelNumber(),0);
    }
    @Test
    public void toStringTest() throws InvalidLocationException {
        String position = "1-a-1";
        mPosition mpo = new mPosition(1,"a",1);
        assertEquals(position,mpo.toString());
    }
}
