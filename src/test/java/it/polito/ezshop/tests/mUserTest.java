package it.polito.ezshop.tests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.security.InvalidParameterException;

public class mUserTest {
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
		mUser mte = new mUser(1,"User","user","Cashier");
		new mUser(5,"User","user","Cashier");
		mUser.reset();
		assertThrows(InvalidParameterException.class, ()->new mUser(-1,null,null,null));
		assertThrows(InvalidParameterException.class, ()->new mUser(1,"A","A","A"));
		assertThrows(InvalidParameterException.class, ()->new mUser("","",""));
		assertThrows(InvalidParameterException.class, ()->new mUser("A","A","A"));
		assertThrows(InvalidParameterException.class, ()->new mUser(null,null,null));
		assertThrows(InvalidParameterException.class, ()->mte.setId(-1));
		assertThrows(InvalidParameterException.class, ()->mte.setPassword(null));
		assertThrows(InvalidParameterException.class, ()->mte.setRole(null));
		assertThrows(InvalidParameterException.class, ()->mte.setUsername(null));
		assertThrows(InvalidParameterException.class, ()->mte.update(null, null,null));
		assertThrows(InvalidParameterException.class, ()->mte.update("","",""));
	}
	@Test
	public void equalsTest() {
		mUser mte = new mUser(1,"User","user","Cashier");
        mUser mta = new mUser(1,"User","user","Cashier");
		mUser mts = new mUser(5,"User","user","Cashier");
        mUser.reset();
		assertFalse(mte.equals(mts));
		assertTrue(mte.equals(mta));
	}
	
    @Test
    public void getIdTest(){
        mUser mu = new mUser(1,"user","pass","Administrator");
        mUser.reset();
        assertEquals(1,mu.getId(),0);
    }
    @Test
    public void setIdTest(){
        mUser mu = new mUser("user","pass","Cashier");
        mUser.reset();
        mu.setId(2);
        assertEquals(2,mu.getId(),0);
    }
    @Test
    public void getUsernameTest(){
        mUser mu = new mUser("user","pass","Administrator");
        mUser.reset();
        assertEquals("user",mu.getUsername());
    }
    @Test
    public void setUsernameTest(){
        mUser mu = new mUser("user2","pass2","Cashier");
        mUser.reset();
        mu.setUsername("user1");
        assertEquals("user1",mu.getUsername());
    }
    @Test
    public void getPasswordTest(){
        mUser mu = new mUser("user2","pass2","Administrator");
        mUser.reset();
        assertEquals("pass2",mu.getPassword());
    }
    @Test
    public void setPasswordTest(){
        mUser mu = new mUser("user2","pass2","Administrator");
        mUser.reset();
        mu.setPassword("pass1");
        assertEquals("pass1",mu.getPassword());
    }
    @Test
    public void getRoleTest(){
        mUser mu = new mUser("user","pass","Administrator");
        mUser.reset();
        assertEquals("Administrator",mu.getRole());
    }
    @Test
    public void setRoleTest(){
        mUser mu = new mUser("user","pass","Administrator");
        mUser.reset();
        mu.setRole("ShopManager");
        assertEquals("ShopManager",mu.getRole());
    }


    @Test
    public void userInsertFailTest(){
        mUser bo = new mUser(999999,"Username","user","Cashier");
        mUser.reset();
        bo.insert();
        assertFalse(bo.insert());
        bo.delete();
    }
    @Test
    public void userInsertTest(){
        mUser bo = new mUser(999999,"Username","user","Cashier");
        mUser.reset();
        assertTrue(bo.insert());
        bo.delete();
    }
    

    @Test
    public void userUpdateTest(){
        mUser bo = new mUser(999999,"Username","user","Cashier");
        mUser.reset();
        bo.insert();
        assertTrue(bo.update(bo.getUsername(),bo.getPassword(),"Administrator"));
        bo.delete();
    }
    

    @Test
    public void userDeleteTest(){
        mUser bo = new mUser(999999,"Username","user","Cashier");
        mUser.reset();
        bo.insert();
        assertTrue(bo.delete());
    }
}
