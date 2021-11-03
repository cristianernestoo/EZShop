package it.polito.ezshop.APITests;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class getCreditsAndDebitsTest {
    @Before
    public void wipeOut() {
        EZShop.getInstance().reset();
        EZShop.getInstance().setLoggedUser(new mUser("a","a","Administrator"));
    }
    @Test
    public void testUnauthorized() {
        EZShop.getInstance().logout();
        assertThrows(UnauthorizedException.class, () -> EZShop.getInstance().getCreditsAndDebits(LocalDate.of(2000, 1, 1), LocalDate.now()));
    }
    @Test
    public void testRange() throws UnauthorizedException {
        EZShop.getInstance().recordBalanceUpdate(10);
        List<BalanceOperation> list =  EZShop.getInstance().getCreditsAndDebits(LocalDate.of(2000, 1, 1), LocalDate.now());
        assertEquals(10,list.get(0).getMoney(),0);
    }
    @Test
    public void testSwap() throws UnauthorizedException {
        EZShop.getInstance().recordBalanceUpdate(10);
        List<BalanceOperation> list =  EZShop.getInstance().getCreditsAndDebits(LocalDate.now(), LocalDate.of(2000, 1, 1));
        assertEquals(10,list.get(0).getMoney(),0);
    }
    @Test
    public void testFromNull() throws UnauthorizedException {
        EZShop.getInstance().recordBalanceUpdate(10);
        List<BalanceOperation> list =  EZShop.getInstance().getCreditsAndDebits(null, LocalDate.now());
        assertEquals(10,list.get(0).getMoney(),0);
    }
    @Test
    public void testToNull() throws UnauthorizedException {
        EZShop.getInstance().recordBalanceUpdate(10);
        List<BalanceOperation> list =  EZShop.getInstance().getCreditsAndDebits(LocalDate.of(2000, 1, 1), null);
        assertEquals(10,list.get(0).getMoney(),0);
    }
    @Test
    public void testBothNull() throws UnauthorizedException {
        EZShop.getInstance().recordBalanceUpdate(10);
        List<BalanceOperation> list =  EZShop.getInstance().getCreditsAndDebits(null, null);
        assertEquals(10,list.get(0).getMoney(),0);
    }
    @AfterClass
    public static void clean(){
        EZShop.getInstance().reset();
    }
}