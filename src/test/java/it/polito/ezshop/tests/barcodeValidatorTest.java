package it.polito.ezshop.tests;

import org.junit.Test;

import static org.junit.Assert.*;

public class barcodeValidatorTest {
    @Test
    public void testCorrectBarcode(){
        String barcode = "333333333331";
        assertTrue(it.polito.ezshop.data.EZShop.getInstance().barCodeValidator(barcode));
    }
    @Test
    public void testCorrectBarcode13(){
        String barcode = "5555555555550";
        assertTrue(it.polito.ezshop.data.EZShop.getInstance().barCodeValidator(barcode));
    }
    @Test
    public void testCorrectBarcode14(){
        String barcode = "55555555555555";
        assertTrue(it.polito.ezshop.data.EZShop.getInstance().barCodeValidator(barcode));
    }
    @Test
    public void testInvalidBarcode(){
        String barcode = "333333333333";
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().barCodeValidator(barcode));
    }
    @Test
    public void testInvalidBarcodeOutOfBoundaries1(){
        String barcode = "0123";
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().barCodeValidator(barcode));
    }

    @Test
    public void testInvalidBarcodeOutOfBoundaries2(){
        String barcode = "012390192301301831088";
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().barCodeValidator(barcode));
    }
    @Test
    public void testNullBarcode(){
        assertFalse(it.polito.ezshop.data.EZShop.getInstance().barCodeValidator(null));
    }
}
