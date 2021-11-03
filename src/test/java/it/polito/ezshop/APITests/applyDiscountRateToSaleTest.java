package it.polito.ezshop.APITests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.InvalidPaymentException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class applyDiscountRateToSaleTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().applyDiscountRateToSale(1,1));
    }
    @Test
    public void testCorrect() throws InvalidTransactionIdException, UnauthorizedException, InvalidDiscountRateException {
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        assertTrue(EZShop.getInstance().applyDiscountRateToSale(saleId,0.1));
    }
    @Test
    public void testCorrect2() throws InvalidTransactionIdException, UnauthorizedException, InvalidDiscountRateException {
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().endSaleTransaction(saleId);
        assertTrue(EZShop.getInstance().applyDiscountRateToSale(saleId,0.1));
    }
    @Test
    public void testAlreadyPayed() throws InvalidTransactionIdException, UnauthorizedException, InvalidDiscountRateException, InvalidPaymentException {
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().endSaleTransaction(saleId);
        Integer saleId2 = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().endSaleTransaction(saleId2);
        EZShop.getInstance().receiveCashPayment(saleId, 1);
        assertFalse(EZShop.getInstance().applyDiscountRateToSale(saleId,0.1));
    }
    @Test
    public void testAbsentTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidDiscountRateException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        assertFalse(EZShop.getInstance().applyDiscountRateToSale(1,0.1));
    }
    @Test
    public void testAbsentTransaction2() throws InvalidTransactionIdException, UnauthorizedException, InvalidDiscountRateException {
        EZShop.getInstance().startSaleTransaction();
        assertFalse(EZShop.getInstance().applyDiscountRateToSale(10,0.1));
    }
    @Test
    public void testNullId() {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Cashier"));
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().applyDiscountRateToSale(null,0.2));
    }
    @Test
    public void testIdEqual0() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().applyDiscountRateToSale(0,0.2));
    }
    @Test
    public void testIdLessThan0() {
        assertThrows(InvalidTransactionIdException.class, () -> EZShop.getInstance().applyDiscountRateToSale(-1, 0.2));
    }
    @Test
    public void testDiscountRateLessThan0() {
        assertThrows(InvalidDiscountRateException.class, () -> EZShop.getInstance().applyDiscountRateToSale(1,-1));
    }
    @Test
    public void testDiscountRateGreaterThan1() {
        assertThrows(InvalidDiscountRateException.class, () -> EZShop.getInstance().applyDiscountRateToSale(1,2));
    }
    @Test
    public void testDiscountRateEqual1() {
        assertThrows(InvalidDiscountRateException.class, () -> EZShop.getInstance().applyDiscountRateToSale(1,2));
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
