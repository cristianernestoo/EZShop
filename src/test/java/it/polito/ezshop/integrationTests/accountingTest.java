package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.mUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class accountingTest {
    @Before
    public void wipeOut() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        EZShop.getInstance().reset();
    }
    @After
    public void clearAfter() {
        EZShop.getInstance().reset();
    }
    @Test
    public void scenario9_1() throws UnauthorizedException {
        EZShop.getInstance().setLoggedUser(new mUser("a","a","ShopManager"));
        EZShop.getInstance().recordBalanceUpdate(10);
        List<BalanceOperation> list =  EZShop.getInstance().getCreditsAndDebits(LocalDate.of(2000, 1, 1), LocalDate.now());
        assertEquals(10,list.get(0).getMoney(),0);
    }
}
