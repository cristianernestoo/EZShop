package it.polito.ezshop.tests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.model.mReturnTransaction;
import org.junit.Test;


import static org.junit.Assert.*;

public class getReturnTransactionTest {
    @Test
    public void testFoundId() throws InvalidTransactionIdException {
        mReturnTransaction mrt = new mReturnTransaction(1,0.1);
        EZShop.getInstance().getReturnList().add(mrt);
        mReturnTransaction eq = it.polito.ezshop.data.EZShop.getInstance().getReturnTransaction(mrt.getReturnId());
        assertSame(mrt, eq);
    }
    @Test
    public void testFoundId2items() throws InvalidTransactionIdException {
        mReturnTransaction mrt = new mReturnTransaction(1,0.1);
        mReturnTransaction mrt1 = new mReturnTransaction(1,0.1);
        EZShop.getInstance().getReturnList().add(mrt);
        EZShop.getInstance().getReturnList().add(mrt1);
        mReturnTransaction eq = it.polito.ezshop.data.EZShop.getInstance().getReturnTransaction(mrt.getReturnId());
        assertSame(mrt, eq);
    }
    @Test
    public void testMissingId() throws InvalidTransactionIdException {
        Integer id = 100;
        mReturnTransaction mrt = new mReturnTransaction(1,0.1);
        EZShop.getInstance().getReturnList().add(mrt);
        mReturnTransaction eq = it.polito.ezshop.data.EZShop.getInstance().getReturnTransaction(id);
        assertNull(eq);
    }
    @Test
    public void testInvalidId(){
        Integer id = -1;
        assertThrows(InvalidTransactionIdException.class,() -> it.polito.ezshop.data.EZShop.getInstance().getReturnTransaction(id));
    }
    @Test
    public void testEmptyReturns() throws InvalidTransactionIdException{
        Integer id = 100;
        mReturnTransaction eq = it.polito.ezshop.data.EZShop.getInstance().getReturnTransaction(id);
        assertNull(eq);
    }
    @Test
    public void testNullId(){
        Integer id = null;
        assertThrows(InvalidTransactionIdException.class,() -> it.polito.ezshop.data.EZShop.getInstance().getReturnTransaction(id));
    }
}
