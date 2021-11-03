package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class managePaymentTest {
    @Before
    public void wipeOut() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        EZShop.getInstance().reset();
        EZShop.getInstance().createUser("test","test","Administrator");
        EZShop.getInstance().login("test","test");
    }
    @Test
    public void Scenario7_1() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidCreditCardException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().addProductToSale(1,"333333333331",1);
        EZShop.getInstance().endSaleTransaction(1);
        assertTrue(EZShop.getInstance().receiveCreditCardPayment(1, "4485370086510891"));
    }
    @Test
    public void Scenario7_2() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException{
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",1,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().addProductToSale(1,"333333333331",1);
        EZShop.getInstance().endSaleTransaction(1);
        assertThrows(InvalidCreditCardException.class, () -> EZShop.getInstance().receiveCreditCardPayment(1, "448537008651089876"));
    }
    @Test
    public void Scenario7_3() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidCreditCardException {
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().createProductType("Test","333333333331",5000,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().addProductToSale(1,"333333333331",1);
        EZShop.getInstance().endSaleTransaction(1);
        assertFalse(EZShop.getInstance().receiveCreditCardPayment(1, "4485370086510891"));
    }
    @Test
    public void Scenario7_4(){
        assertEquals(5 ,it.polito.ezshop.data.EZShop.getInstance().computeChange(10,5), 0);
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
