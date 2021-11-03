package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mSaleTransaction;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class getSaleTransactionTest {
    @BeforeClass
    public static void wipeOut() {
        EZShop.getInstance().reset();
    }
    @Before
    public void preconditions(){
        EZShop.getInstance().setLoggedUser(new mUser(1,"a","a","Administrator"));
    }
    @Test
    public void unauthorizedTest() {
        EZShop.getInstance().setLoggedUser(null);
        assertThrows(UnauthorizedException.class,() -> EZShop.getInstance().getSaleTransaction(1));
    }
    @Test
    public void invalidTransactionNumTest() {
        assertThrows(InvalidTransactionIdException.class,() -> EZShop.getInstance().getSaleTransaction(null));
    }
    @Test
    public void invalidTransactionNum2Test() {
        assertThrows(InvalidTransactionIdException.class,() -> EZShop.getInstance().getSaleTransaction(-1));
    }
    @Test
    public void noSaleFoundTest() throws InvalidTransactionIdException, UnauthorizedException {
        EZShop.getInstance().getSaleTransactions().clear();
        assertNull(EZShop.getInstance().getSaleTransaction(1));
    }
    @Test
    public void saleNotPayedTest() throws InvalidTransactionIdException, UnauthorizedException {
        mSaleTransaction sale = new mSaleTransaction(0,3,new LinkedList<>());
        EZShop.getInstance().getSaleTransactions().add(sale);
        assertEquals(sale,EZShop.getInstance().getSaleTransaction(1));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
