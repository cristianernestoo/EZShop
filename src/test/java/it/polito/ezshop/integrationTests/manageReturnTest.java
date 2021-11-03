package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class manageReturnTest {
    @Before
    public void preconditions() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
    }
    @After
    public void clearAfter(){
        EZShop.getInstance().reset();
    }
    @BeforeClass
    public static void clearBeforeClass() {
        EZShop.getInstance().reset();
    }
    @Test
    public void scenario10_1() throws UnauthorizedException, InvalidCreditCardException, InvalidTransactionIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(2);
        EZShop.getInstance().addProductToSale(1,"333333333331",2);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 2);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().returnProduct(1,"333333333331",1);
        EZShop.getInstance().endReturnTransaction(1, true);
        EZShop.getInstance().recordBalanceUpdate(2);
        assertEquals(1, EZShop.getInstance().returnCreditCardPayment(1, "4485370086510891"), 0);
    }
    @Test
    public void scenario10_2() throws UnauthorizedException, InvalidTransactionIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidPaymentException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(2);
        EZShop.getInstance().addProductToSale(1,"333333333331",2);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCashPayment(1, 2);
        EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().returnProduct(1,"333333333331",1);
        EZShop.getInstance().endReturnTransaction(1, true);
        EZShop.getInstance().recordBalanceUpdate(2);
        assertEquals(1, EZShop.getInstance().receiveCashPayment(1, 2), 0);
    }
}
