package it.polito.ezshop.APITests;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mProductType;
import it.polito.ezshop.model.mUser;
import it.polito.ezshop.model.mProduct;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
public class deleteProductFromSaleRFIDTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().deleteProductFromSaleRFID(1,"0000010000"));
    }
    @Test
    public void testCorrect() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidRFIDException{
        mProductType pt = new mProductType("","description","55555555555555",2);
        mProduct p = new mProduct("111111111111");
        pt.addProduct(p);
        pt.setQuantity(pt.getQuantity() + 1);
        EZShop.getInstance().getInventory().put(pt.getId(),pt);
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().addProductToSaleRFID(saleId,"111111111111");
        pt.addProduct(p);
        assertTrue(EZShop.getInstance().deleteProductFromSaleRFID(saleId,"111111111111"));
    }
    @Test
    public void testRFIDNotFound() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException{
        mProductType pt = new mProductType("","description","55555555555555",2);
        mProduct p = new mProduct("111111111111");
        pt.addProduct(p);
        pt.setQuantity(pt.getQuantity() + 1);
        EZShop.getInstance().getInventory().put(pt.getId(),pt);
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().addProductToSaleRFID(saleId,"111111111111");
        pt.addProduct(p);
        assertThrows(NullPointerException.class, ()-> EZShop.getInstance().deleteProductFromSaleRFID(saleId,"000001000122"));
    }
    @Test
    public void testInvalidRFID() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException{
        mProductType p = new mProductType("","description","55555555555555",2);
        p.setQuantity(10);
        mProduct p1 = new mProduct("000001000022");
        p.addProduct(p1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().addProductToSaleRFID(saleId,"000001000022");
        assertThrows(InvalidRFIDException.class, ()-> EZShop.getInstance().deleteProductFromSaleRFID(saleId,"2"));
    }
    @Test
    public void testInvalidTransactionID() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException{
        mProductType p = new mProductType("","description","55555555555555",2);
        p.setQuantity(10);
        mProduct p1 = new mProduct("000001000022");
        p.addProduct(p1);
        EZShop.getInstance().getInventory().put(p.getId(),p);
        Integer saleId = EZShop.getInstance().startSaleTransaction();
        EZShop.getInstance().addProductToSaleRFID(saleId,"000001000022");
        assertThrows(InvalidTransactionIdException.class, ()-> EZShop.getInstance().deleteProductFromSaleRFID(0,"2") );
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}
