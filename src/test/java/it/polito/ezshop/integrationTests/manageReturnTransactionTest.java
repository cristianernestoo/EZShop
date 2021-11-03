package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import org.junit.*;

import static org.junit.Assert.*;

public class manageReturnTransactionTest {
    @Before
    public void preConditions() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidCreditCardException {
        EZShop.getInstance().reset();
        EZShop.getInstance().createUser("test","test","Administrator");
        EZShop.getInstance().login("test","test");
        EZShop.getInstance().createProductType("Test","333333333331",1.0,null);
        EZShop.getInstance().getInventory().get(1).setQuantity(1);
        EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().addProductToSale(1,"333333333331",1);
        EZShop.getInstance().endSaleTransaction(1);
        EZShop.getInstance().receiveCreditCardPayment(1, "4485370086510891");
    }
    @Test
    public void Scenario8_1() throws InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException {
        Integer id = EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().returnProduct(id,"333333333331",1);
        assertTrue(EZShop.getInstance().endReturnTransaction(id,true));
    }
    @Test
    public void Scenario8_2() throws InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException {
        Integer id = EZShop.getInstance().startReturnTransaction(1);
        EZShop.getInstance().returnProduct(id,"333333333331",1);
        assertTrue(EZShop.getInstance().endReturnTransaction(id,true));
    }
    @AfterClass
    public static void clearAll(){
        EZShop.getInstance().reset();
    }
}
