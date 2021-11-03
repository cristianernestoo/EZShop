package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class manageInventoryAndOrdersTest {
    @Before
    public void preconditions() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        EZShop.getInstance().createProductType("a","555555555555",5,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(100);
    }
    @After
    public void clearAfter() {
        EZShop.getInstance().reset();
    }
    @BeforeClass
    public static void clearBeforeClass() {
        EZShop.getInstance().reset();
    }
    @Test
    public void scenario3_1() throws UnauthorizedException, InvalidQuantityException, InvalidPricePerUnitException, InvalidProductCodeException {
        assertNotEquals(Integer.valueOf(-1), EZShop.getInstance().issueOrder("555555555555",100,5));
    }
    @Test
    public void scenario3_2() throws UnauthorizedException, InvalidQuantityException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidTransactionIdException, InvalidPaymentException {
        int units = 10;
        int pricePerUnits = 5;
        String productCode = "555555555555";

        //Precondition
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().addProductToSale(saleId,productCode,80);
        EZShop.getInstance().endSaleTransaction(saleId);
        EZShop.getInstance().receiveCashPayment(1,pricePerUnits*80);
        Integer orderId = EZShop.getInstance().issueOrder(productCode,units,pricePerUnits);
        EZShop.getInstance().computeBalance();

        assertNotEquals(Integer.valueOf(-1), EZShop.getInstance().payOrderFor(productCode,units,orderId));
    }
    @Test
    public void scenario3_3() throws UnauthorizedException, InvalidQuantityException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidTransactionIdException, InvalidPaymentException, InvalidOrderIdException, InvalidLocationException {
        int units = 10;
        int pricePerUnits = 5;
        String productCode = "555555555555";

        //Precondition
        EZShop.getInstance().getProductTypeByBarCode(productCode).setLocation("1-a-1");
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().addProductToSale(saleId,productCode,80);
        EZShop.getInstance().endSaleTransaction(saleId);
        EZShop.getInstance().receiveCashPayment(1,pricePerUnits*80);
        Integer orderId = EZShop.getInstance().issueOrder(productCode,units,pricePerUnits);
        EZShop.getInstance().computeBalance();
        EZShop.getInstance().payOrder(orderId);

        assertTrue(EZShop.getInstance().recordOrderArrival(orderId));
    }



}
