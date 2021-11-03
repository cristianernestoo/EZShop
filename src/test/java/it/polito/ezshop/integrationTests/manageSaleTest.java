package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mCustomer;
import it.polito.ezshop.model.mProductType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class manageSaleTest {
    @BeforeClass
    public static void clearBeforeClass() {
        EZShop.getInstance().reset();
    }
    @After
    public void clearAfter() {
        EZShop.getInstance().reset();
    }
    @Before
    public void preconditions () throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException {
        EZShop.getInstance().createUser("C","c","ShopManager");
        EZShop.getInstance().login("C","c");

        EZShop.getInstance().createProductType("product description","333333333331", 1, "note");
        EZShop.getInstance().updatePosition(1, "1-a-1");
        EZShop.getInstance().updateQuantity(1,20);
    }
    @Test
    public void scenario6_1 () throws UnauthorizedException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidCreditCardException {
        Integer saleid = EZShop.getInstance().startSaleTransaction();
        mProductType p = (mProductType) EZShop.getInstance().getProductTypeByBarCode("333333333331");
        EZShop.getInstance().addProductToSale(saleid, p.getBarCode(), 10);
        EZShop.getInstance().endSaleTransaction(saleid);
        //UC7 MANAGE PAYMENT
        assertTrue(EZShop.getInstance().receiveCreditCardPayment(1, "4485370086510891"));
        EZShop.getInstance().computeBalance();
        assertEquals(10, EZShop.getInstance().getBalance(), 0);
        assertEquals(10, EZShop.getInstance().getInventory().get(1).getQuantity(), 0);
    }
    @Test
    public void scenario6_2 () throws UnauthorizedException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidDiscountRateException, InvalidCreditCardException {
        Integer saleid = EZShop.getInstance().startSaleTransaction();
        mProductType p = (mProductType) EZShop.getInstance().getProductTypeByBarCode("333333333331");
        EZShop.getInstance().addProductToSale(saleid, p.getBarCode(), 10);
        EZShop.getInstance().applyDiscountRateToProduct(saleid, p.getBarCode(), 0.5);
        EZShop.getInstance().endSaleTransaction(saleid);
        //UC7 MANAGE PAYMENT
        assertTrue(EZShop.getInstance().receiveCreditCardPayment(1, "4485370086510891"));
        EZShop.getInstance().computeBalance();
        assertEquals(5, EZShop.getInstance().getBalance(), 0);
        assertEquals(10, EZShop.getInstance().getInventory().get(1).getQuantity(), 0);
    }
    @Test
    public void scenario6_3 () throws UnauthorizedException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidDiscountRateException, InvalidCreditCardException {
        Integer saleid = EZShop.getInstance().startSaleTransaction();
        mProductType p = (mProductType) EZShop.getInstance().getProductTypeByBarCode("333333333331");
        EZShop.getInstance().addProductToSale(saleid, p.getBarCode(), 10);
        EZShop.getInstance().applyDiscountRateToSale(saleid, 0.5);
        EZShop.getInstance().endSaleTransaction(saleid);
        //UC7 MANAGE PAYMENT
        assertTrue(EZShop.getInstance().receiveCreditCardPayment(1, "4485370086510891"));
        EZShop.getInstance().computeBalance();
        assertEquals(5, EZShop.getInstance().getBalance(), 0);
        assertEquals(10, EZShop.getInstance().getInventory().get(1).getQuantity(), 0);
    }
    @Test
    public void scenario6_4 () throws UnauthorizedException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidCustomerNameException, InvalidCustomerIdException, InvalidCustomerCardException, InvalidCreditCardException {
        //preconditions
        EZShop.getInstance().defineCustomer("Cu");
        mCustomer Cu = (mCustomer) EZShop.getInstance().getCustomer(1);
        String card = EZShop.getInstance().createCard();
        EZShop.getInstance().attachCardToCustomer(card, Cu.getId());

        Integer saleid = EZShop.getInstance().startSaleTransaction();
        mProductType p = (mProductType) EZShop.getInstance().getProductTypeByBarCode("333333333331");
        EZShop.getInstance().addProductToSale(saleid, p.getBarCode(), 10);
        EZShop.getInstance().endSaleTransaction(saleid);
        EZShop.getInstance().modifyPointsOnCard(card, 1);
        //UC7 MANAGE PAYMENT
        assertTrue(EZShop.getInstance().receiveCreditCardPayment(1, "4485370086510891"));
        EZShop.getInstance().computeBalance();
        assertEquals(10, EZShop.getInstance().getBalance(), 0);
        assertEquals(10, EZShop.getInstance().getInventory().get(1).getQuantity(), 0);
        assertEquals(1, Cu.getPoints(), 0);
    }
}
