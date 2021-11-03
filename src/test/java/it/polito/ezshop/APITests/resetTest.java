package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.model.*;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class resetTest {
    @Test
    public void testEmptyListCorrect(){
      EZShop.getInstance().reset();
        assertEquals(0, EZShop.getInstance().getInventory().size());
        assertEquals(0,EZShop.getInstance().getSaleTransactions().size());
        assertEquals(0,EZShop.getInstance().getBalanceOperations().size());
        assertEquals(0,EZShop.getInstance().getReturnList().size());
        assertEquals(0,EZShop.getInstance().getListOfOrders().size());
        assertNull(EZShop.getInstance().getOpenReturnTransaction());
        assertNull(EZShop.getInstance().getOpenSaleTransaction());
    }
    @Test
    public void testCorrect() throws InvalidLocationException {
        mProductType p = new mProductType(3,new mPosition("1-a-1"),"note","DEscripTion","555555555555",2.0,1);
        mProductType pl = new mProductType(3,null,"note","DEscripTion","555555555555",2.0,10);
        mSaleTransaction s = new mSaleTransaction(1,1,0,3,new LinkedList<>());
        mOrder o = new mOrder(1,1,"555555555555",20,1,"PAYED");
        mBalanceOperation b = new mBalanceOperation(100,"RETURN");
        mReturnTransaction r = new mReturnTransaction(1,1,1,2, 0, new LinkedList<mTicketEntry>());
        mCustomer c = new mCustomer("Alex");
        mUser user = new mUser("PAss","Pass","Administrator");
        r.addEntries(new mTicketEntry("555555555555",1,"test",10,2,0));
        r.addEntries(new mTicketEntry("555555555555",1,"test",10,2,0));
        s.addEntries(new mTicketEntry("555555555555",1,"test",10,2,0));
        s.addEntries(new mTicketEntry("555555555555",1,"test",10,2,0));
        c.insert();
        user.insert();
        p.insert();
        s.insert();
        o.insert();
        b.insert();
        r.insert();
        pl.insert();
        EZShop ez = new EZShop();
        if(ez.getListOfOrders().isEmpty() && ez.getReturnList().isEmpty() && ez.getListOfCustomers().isEmpty() && ez.getSaleTransactions().isEmpty() && ez.getInventory().isEmpty() && ez.getBalanceOperations().isEmpty() && ez.getListOfUsers().isEmpty()) fail();
      ez.reset();
        assertEquals(0, ez.getInventory().size());
        assertEquals(0,ez.getSaleTransactions().size());
        assertEquals(0,ez.getBalanceOperations().size());
        assertEquals(0,ez.getReturnList().size());
        assertEquals(0,ez.getListOfOrders().size());
        assertNull(ez.getOpenReturnTransaction());
        assertNull(ez.getOpenSaleTransaction());
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
