package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class endSaleTransactionTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().endSaleTransaction(1));
    }
    @Test
    public void testCorrect() throws UnauthorizedException, InvalidTransactionIdException {
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        assertTrue(EZShop.getInstance().endSaleTransaction(saleId));
    }
    @Test
    public void testAbsentSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        EZShop.getInstance().startSaleTransaction();
        assertFalse(EZShop.getInstance().endSaleTransaction(10));
    }
    @Test
    public void testSaleTransactionAlreadyClosed() throws InvalidTransactionIdException, UnauthorizedException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Cashier"));
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().endSaleTransaction(saleId);
        assertFalse(EZShop.getInstance().endSaleTransaction(saleId));
    }
    @Test
    public void testNullId() {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Cashier"));
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().endSaleTransaction(null));
    }
    @Test
    public void testIdEqual0() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().endSaleTransaction(0));
    }
    @Test
    public void testIdLessThan0() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().endSaleTransaction(-1));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
