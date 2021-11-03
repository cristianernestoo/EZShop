package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class computePointsForSaleTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().computePointsForSale(1));
    }
    @Test
    public void testCorrect() throws InvalidTransactionIdException, UnauthorizedException {
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        assertNotEquals(-1, EZShop.getInstance().computePointsForSale(saleId));
    }
    @Test
    public void testCorrect2() throws InvalidTransactionIdException, UnauthorizedException {
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().endSaleTransaction(saleId);
        Integer saleId2 = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().endSaleTransaction(saleId2);
        assertNotEquals(-1, EZShop.getInstance().computePointsForSale(saleId2));
    }
    @Test
    public void testAbsentTransaction() throws InvalidTransactionIdException, UnauthorizedException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        assertEquals(-1, EZShop.getInstance().computePointsForSale(1));
    }
    @Test
    public void testAbsentTransaction2() throws InvalidTransactionIdException, UnauthorizedException {
        EZShop.getInstance().startSaleTransaction();
        assertEquals(-1, EZShop.getInstance().computePointsForSale(10));
    }
    @Test
    public void testNullId() {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Cashier"));
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().computePointsForSale(null));
    }
    @Test
    public void testIdEqual0() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().computePointsForSale(0));
    }
    @Test
    public void testIdLessThan0() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().computePointsForSale(-1));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
