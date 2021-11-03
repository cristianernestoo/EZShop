package it.polito.ezshop.acceptanceTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import it.polito.ezshop.tests.*;
import it.polito.ezshop.APITests.*;
import it.polito.ezshop.integrationTests.*;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        //API TESTS
        addProductToSaleTest.class,
        applyDiscountRateToProductTest.class,
        applyDiscountRateToSaleTest.class,
        attachCardToCustomerTest.class,
        computeBalanceTest.class,
        computePointsForSaleTest.class,
        createCardTest.class,
        createProductTypeTest.class,
        createUserTest.class,
        defineCustomerTest.class,
        deleteCustomerTest.class,
        deleteProductFromSaleTest.class,
        deleteProductTypeTest.class,
        deleteReturnTransactionTest.class,
        deleteSaleTransactionTest.class,
        deleteUserTest.class,
        endReturnTransactionTest.class,
        endSaleTransactionTest.class,
        getAllCustomersTest.class,
        getAllOrdersTest.class,
        getAllProductTypesTest.class,
        getAllUserTest.class,
        getCreditsAndDebitsTest.class,
        getCustomerTest.class,
        getProductTypeByBarCodeTest.class,
        getProductTypesByDescriptionTest.class,
        getSaleTransactionTest.class,
        getUserTest.class,
        issueOrderTest.class,
        loginTest.class,
        logoutTest.class,
        modifyPointsOnCardTest.class,
        modifyCustomerTest.class,
        payOrderForTest.class,
        payOrderTest.class,
        receiveCashPaymentTest.class,
        receiveCreditCardPaymentTest.class,
        recordBalanceUpdateTest.class,
        recordOrderArrivalTest.class,
        resetTest.class,
        returnCashPaymentTest.class,
        returnCreditCardPaymentTest.class,
        returnProductTest.class,
        startReturnTransactionTest.class,
        startSaleTransactionTest.class,
        updatePositionTest.class,
        updateProductTest.class,
        updateQuantityTest.class,
        updateUserRightsTest.class,

        //INTEGRATION TESTS
        accountingTest.class,
        authenticateTest.class,
        manageCustomersAndCardsTest.class,
        manageInventoryAndOrdersTest.class,
        managePaymentTest.class,
        manageProductsTest.class,
        manageReturnTest.class,
        manageReturnTransactionTest.class,
        manageSaleTest.class,
        manageUserAndRightsTest.class,

        //UNIT TESTS
        attemptPaymentTest.class,
        attemptRefundTest.class,
        barcodeValidatorTest.class,
        checkCreditCardValidityTest.class,
        checkPositionTest.class,
        computeChangeTest.class,
        getReturnTransactionTest.class,
        isNumericTest.class,
        mBalanceOperationTest.class,
        mCustomerTest.class,
        mOrderTest.class,
        mPositionTest.class,
        mProductTypeTest.class,
        mReturnTransactionTest.class,
        mSaleTransactionTest.class,
        mTicketEntryTest.class,
        mUserTest.class,
        setLocationTest.class

})
public class TestEZShop {
}
