package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mProductType;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class manageProductsTest {
    @BeforeClass
    public static void clearBeforeClass() {
        EZShop.getInstance().reset();
    }
    @After
    public void clearAfter() {
        EZShop.getInstance().reset();
    }
    @Test
    public void scenario1_1 () throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException {
        //preconditions
        EZShop.getInstance().createUser("C","c","ShopManager");
        EZShop.getInstance().login("C","c");

        EZShop.getInstance().createProductType("product description","333333333331", 1, "note");
        EZShop.getInstance().updatePosition(1, "1-a-1");

        assertEquals("1-a-1", EZShop.getInstance().getInventory().get(1).getLocation());
    }
    @Test
    public void scenario1_2 () throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException {
        //preconditions
        EZShop.getInstance().createUser("C","c","ShopManager");
        EZShop.getInstance().login("C","c");
        EZShop.getInstance().createProductType("product description","333333333331", 1, "note");

        mProductType p = (mProductType) EZShop.getInstance().getProductTypeByBarCode("333333333331");
        EZShop.getInstance().updatePosition(p.getId(), "1-a-1");

        assertEquals("1-a-1", p.getLocation());
    }
    @Test
    public void scenario1_3 () throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {
        //preconditions
        EZShop.getInstance().createUser("C","c","ShopManager");
        EZShop.getInstance().login("C","c");
        EZShop.getInstance().createProductType("product description","333333333331", 1, "note");

        mProductType p = (mProductType) EZShop.getInstance().getProductTypeByBarCode("333333333331");
        EZShop.getInstance().updateProduct(p.getId(),p.getProductDescription(),p.getBarCode(),5,p.getNote());

        assertEquals(5,p.getPricePerUnit(), 0);
    }
}
