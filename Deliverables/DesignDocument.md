# Design Document 

Authors: Semeraro Lorenzo, Ernesto Cristian, Marino Vincenzo, Matees Mihai Alexandru

Date: 09/06/2021

Version: 3.0


# Contents

- [Design Document](#design-document)
- [Contents](#contents)
- [High level design](#high-level-design)
- [Low level design](#low-level-design)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)
  - [Scenario 1.1](#scenario-11)
  - [Scenario 1.3](#scenario-13)
  - [Scenario 2.1](#scenario-21)
  - [Scenario 2.2](#scenario-22)
  - [Scenario 2.3](#scenario-23)
  - [Scenario 3.1](#scenario-31)
  - [Scenario 3.3](#scenario-33)
  - [Scenario 4.3](#scenario-43)
  - [Scenario 5.1](#scenario-51)
  - [Scenario 6.2](#scenario-62)
  - [Scenario 6.4](#scenario-64)
  - [Scenario 6.5](#scenario-65)
  - [Scenario 7.1](#scenario-71)
  - [Scenario 7.3](#scenario-73)
  - [Scenario 7.4](#scenario-74)
  - [Scenario 8.1](#scenario-81)
  - [Scenario 9.1](#scenario-91)
  - [Scenario 10.1](#scenario-101)


# High level design

```plantuml
package it.polito.ezshop.gui 

package it.polito.ezshop.data 

package it.polito.ezshop.model 

package it.polito.ezshop.exceptions

it.polito.ezshop.gui -- it.polito.ezshop.model

it.polito.ezshop.data --> it.polito.ezshop.exceptions
it.polito.ezshop.data <-- it.polito.ezshop.model

it.polito.ezshop.model -> it.polito.ezshop.exceptions
```
Architectural patterns: <br>
- Façade
- MVC

# Low level design

```plantuml

package it.polito.ezshop.model 
{
    class EZShop{
    - static Shop instance
    - final ArrayList<mUser> listOfUsers
    - final ArrayList<mCustomer> listOfCustomers
    - final LinkedList<mBalanceOperation> balanceOperations
    - final LinkedList<mSaleTransaction> openTransactions
    - final HashMap<String,Double> listOfCreditCards
    - double balance
    - User loggedUser
    - Connection conn
    - HashMap<Integer, mProductType> inventory
    - boolean checkPosition(String)
    - boolean checkCreditCardValidity(String)
    - static boolean isNumeric(String)
    - static boolean barCodeValidator(String)
    - boolean attemptPayment(String, double)
    - boolean attemptRefund(String, double)
    - double computeChange(double,double)
    - mReturnTransaction getReturnTransaction(Integer)
    - static Shop getInstance()
    - mProductType getProductTypeByRFID(String RFID)
    }

    class mUser <<persistent>> {
        - static int counter
        - int ID
        - String username
        - String password
        - String role
        + void reset()
        + boolean update(String, String, String)
        + boolean insert()
        + boolean delete()
    }

    class mBalanceOperation <<persistent>> {
        - static int counter
        - int balanceId
        - String type
        - double money
        - LocalDate date
        + void reset()
        + boolean update()
        + boolean insert()
        + boolean delete()
    }

    class mProductType <<persistent>> {
        - static int counter
        - Integer id
        - String barCode
        - String productDescription
        - double pricePerUnit
        - Integer quantity
        - String note
        - mPosition position
        - HashMap<String, mProduct> products
        + void reset()
        + boolean update (Integer, mPosition, String, String, String, double)
        + boolean insert()
        + boolean delete()  
    }

    class mProduct <<persistent>> {
        - String RFID
        + boolean update(Integer,String)
        + boolean insert(Integer)
        + boolean delete()  
    }
    class mTicketEntry <<persistent>> {
        - static int counter
        - Integer id
        - String productId
        - String barCode
        - String productDescription
        - double pricePerUnit
        - int amount
        - double discountRate
        - LinkedList<String> RFIDList
        + void reset()
        + boolean update (String, int, int, double)
        + boolean insert(Integer, String)
        + boolean delete()  
    }

    class mSaleTransaction <<persistent>> {
        - static int counter
        - Integer transactionId
        - Integer balanceId
        - double discountRate
        - double price
        - LinkedList<mTicketEntry> entries
        + void computePrice()
        + void reset()
        + boolean update(double, Integer, double)
        + boolean insert()
        + boolean delete()  
    }


    class mCustomer <<persistent>> {
        - static int counter
        - Integer id
        - String customerName
        - String customerCard
        - Integer points
        + boolean update(int, String, String)
        + boolean delete()
        + boolean insert()
    }

    class mPosition {
        - Integer aisleID
        - Integer rackID
        - Integer levelID
        + String toString()
    }

    class mOrder <<persistent>> {
    - static int counter
    - Integer orderId
    - Integer balanceId
    - String productCode
    - double pricePerUnit
    - int quantity
    - String status
    + boolean update(String, int, double, String, Integer)
    + boolean delete()
    + boolean insert()
    }

    class mReturnTransaction <<persistent>> {
    - static int counter
    - Integer returnId
    - Integer balanceId
    - Integer saleId
    - double price
    - double discountRate
    - LinkedList<mTicketEntry> entries;
    + reset()
    + boolean update(Integer, double, String, Date, LocalTime, double, SaleTransaction, ProductType)
    + boolean delete()
    + boolean insert()
    }


    note top of EZShop
    Singleton
    Application logic layer
    Controller
    endnote


    EZShop -> "*" mBalanceOperation

    mUser "1..*" <-  EZShop

    EZShop -> "*" mProductType

    mOrder "*" ---> mProductType

    mReturnTransaction --> mBalanceOperation

    mReturnTransaction --> mSaleTransaction

    mReturnTransaction  --> "*" mTicketEntry

    mSaleTransaction -> "*" mTicketEntry

    mPosition "0..1" <-  mProductType

    mProductType -> "*" mProduct 

    EZShop -> "*" mCustomer

    EZShop ---> "*" mSaleTransaction

    EZShop --> "*" mReturnTransaction

    EZShop --> "*" mOrder

    mTicketEntry -> "*" mProduct

    mOrder --> mBalanceOperation

    mSaleTransaction -> mBalanceOperation

    mReturnTransaction -[hidden] mOrder
    /'
    note "ID is a number on 10 digits " as N1  
    N1 .. LoyaltyCard
    note "bar code is a number on 12 to 14  digits, compliant to GTIN specifications, see  https://www.gs1.org/services/how-calculate-check-digit-manually " as N2  
    N2 .. ProductType
    note "ID is a unique identifier of a transaction,  printed on the receipt (ticket number) " as N3
    N3 .. SaleTransaction '/
}

package it.polito.ezshop.data
{
    interface EZShopInterface
    {
        + void reset()
        + int CreateUser(String, String, String )
        + boolean deleteUser(Integer)
        + List<User> getAllUsers()
        + User getUser(Integer)
        + boolean updateUserRights(Integer, String)
        + User login(String, String)
        + boolean logout()
        + Integer createProductType(String, String, double, String)
        + boolean updateProduct(Integer, String, String, double, String)
        + boolean deleteProductType(Integer)
        + List<ProductType> getAllProductTypes()
        + ProductType getProductTypeByBarCode(String)
        + List<ProductType> getProductTypesByDescription(String)
        + boolean updateQuantity(Integer, int)
        + boolean updatePosition(Integer, String)
        + Integer issueOrder(String, int, double)
        + Integer payOrderFor(String, int, double)
        + boolean payOrder(Integer)D
        + boolean recordOrderArrival(Integer)
        + List<Order> getAllOrders()
        + Integer defineCustomer(String)
        + boolean modifyCustomer(Integer, String, String)
        + boolean deleteCustomer(Integer)
        + Customer getCustomer(Integer)
        + List<Customer> getAllCustomers()
        + String createCard()
        + boolean attachCardToCustomer(String, Integer)
        + boolean modifyPointsOnCard(String, int)
        + Integer startSaleTransaction()
        + boolean addProductToSale(Integer, String, int)
        + boolean deleteProductFromSale(Integer, String, int)
        + boolean applyDiscountRateToProduct(Integer, String, double)
        + boolean applyDiscountRateToSale(Integer, double)
        + int computePointsForSale(Integer)
        + boolean endSaleTransaction(Integer)
        + boolean deleteSaleTransaction(Integer)
        + SaleTransaction getSaleTransaction(Integer)
        + Integer startReturnTransaction(Integer)
        + boolean returnProduct(Integer, String, int)
        + boolean endReturnTransaction(Integer, boolean)
        + boolean deleteReturnTransaction(Integer)
        + double receiveCashPayment(Integer, double)
        + boolean receiveCreditCardPayment(Integer, String)
        + double returnCashPayment(Integer)
        + double returnCreditCardPayment(Integer, String)
        + boolean recordBalanceUpdate(double)
        + List<BalanceOperation> getCreditsAndDebits(LocalDate, LocalDate)
        + double computeBalance()
        + boolean recordOrderArrivalRFID(Integer, String)
        + boolean addProductToSaleRFID(Integer, String)
        + boolean deleteProductFromSaleRFID(Integer, String)
        + boolean returnProductRFID(Integer, String)
    }
}


EZShop ..|> EZShopInterface: <implements>

```

# Verification traceability matrix

|FRID|EZShop|mSaleTransaction|mReturnTransaction|mUser|mBalanceOperation|mOrder|mProductType|mPosition|mCustomer|mTicketEntry|mProduct|
|--- |:----:|:--------------:|:----------------:|:---:|:---------------:|:----:|:----------:|:-------:|:-------:|:----------:|:------:|
|FR1 |   X  |                |                  |  X  |                 |      |            |         |         |            |        |
|FR3 |   X  |                |                  |  X  |                 |      |     X      |   X     |         |            |        |
|FR4 |   X  |                |                  |  X  |        X        |  X   |     X      |   X     |         |            |    X   |
|FR5 |   X  |                |                  |  X  |                 |      |            |         |    X    |            |        |
|FR6 |   X  |        X       |        X         |  X  |        X        |      |     X      |         |    X    |      X     |    X   |
|FR7 |   X  |        X       |        X         |  X  |        X        |      |            |         |         |      X     |    X   |
|FR8 |   X  |        X       |        X         |  X  |        X        |  X   |            |         |         |            |        |

# Verification sequence diagrams 

## Scenario 1.1

```plantuml
actor User
User -> EZShop : createProductType(description, productCode, pricePerUnit, note) 
note over User : User must be an Administrator\n or a ShopManager
EZShop -> mProductType : ProductType(description, barCode, sellPrice, notes)
EZShop <-- mProductType : return ProductType p 
User <-- EZShop : return p.getID()
User -> EZShop : updatePosition(productId,  newPos)
EZShop -> mPosition : Position(newPos)
mPosition --> EZShop : return pos
EZShop -> EZShop : checkPosition (pos)
EZShop -> mProductType : p.update(pos)
EZShop <-- mProductType : return true
User <-- EZShop : return true
```

## Scenario 1.3

```plantuml 
actor User
User -> EZShop : updateProduct(newCode, newPrice)
note over User : User must be an Administrator\n or ShopManager
EZShop -> EZShop : getProductTypeByBarCode(newCode)
EZShop -> mProductType : p.update(newPrice)
EZShop <-- mProductType : return true
User <-- EZShop : return true
```


## Scenario 2.1


```plantuml
actor Administrator
Administrator -> EZShop : createUser(username, password, role)
EZShop -> mUser : User(username, password, role)
EZShop <-- mUser : return User u
EZShop -> mUser : u.insert()
EZShop <-- mUser : return true
Administrator <-- EZShop : return u.getID()
```


## Scenario 2.2


```plantuml
actor Administrator

Administrator -> EZShop : deleteUser(id)
EZShop -> EZShop : getUser(id)
EZShop -> mUser : u.delete()
EZShop <-- mUser : return true
Administrator <-- EZShop : return true

```

## Scenario 2.3


```plantuml
actor Administrator
Administrator -> EZShop : updateUserRights(id, role) 
EZShop -> EZShop : getUser(id)
EZShop -> mUser : u.update(role)
EZShop <-- mUser : return true
Administrator <-- EZShop : return true

```

## Scenario 3.1 

```plantuml
actor User
User -> EZShop : issueOrder(productCode, quantity, pricePerUnit)
note over User : User must be an Administrator\n or a ShopManager
EZShop -> EZShop : getProductTypeByBarCode(productCode)
EZShop -> mOrder : Order(pricePerUnit,quantity,status)
EZShop <-- mOrder : return Order o
EZShop -> mOrder : o.insert()
EZShop <-- mOrder : return true
User <-- EZShop : return o.getOrderId()
```

## Scenario 3.3

```plantuml
actor User
User -> EZShop : recordOrderArrival(orderId)
note over User : User must be an Administrator\n or a ShopManager
EZShop -> mOrder : o.update(productCode, quantity,status)
mOrder --> EZShop : return true
User <-- EZShop : return true
```

## Scenario 4.3

```plantuml
actor User
User -> EZShop : modifyCustomer(id, customerName, newCustomerCard)
Note right: newCustomerCard is an empty string
EZShop -> EZShop : getCustomer(id)
EZShop -> mCustomer: c.update(customerName,newCustomerCard)
mCustomer --> EZShop : return true
User <-- EZShop : return true
```

## Scenario 5.1

```plantuml
actor user
user -> EZShop : login(username, password)
EZShop -> mUser : u.getUsername()
EZShop <-- mUser : return String username
EZShop -> mUser : u.getPassword()
EZShop <-- mUser : return String password
EZShop --> user : return User u
```

## Scenario 6.2

```plantuml
actor Cashier
actor Cashier
Cashier -> EZShop : startSaleTransaction()
    EZShop -> SaleTransaction : SaleTransaction()
    SaleTransaction --> EZShop : return SaleTransaction st
EZShop --> Cashier : return st.getTicketNumber()

Cashier -> EZShop : addProductToSale(transactionId, productCode, amount)
EZShop -> EZShop : getProductTypeByBarcode(barcode)
EZShop -> mSaleTransaction : ost.getTicketNumber()
EZShop -> mProductType : p.setQuantity(quantity)
EZShop -> mTicketEntry : te1.getBarCode()
    mTicketEntry --> EZShop : return barcode    
EZShop -> mTicketEntry : te1.setAmount()
EZShop -> mSaleTransaction : st.computePrice()
EZShop -> mTicketEntry : mTicketEntry()
    mTicketEntry --> EZShop : return te2
EZShop -> mSaleTransaction : st.addEntries(entry)
EZShop -> mSaleTransaction : st.computePrice()
mSaleTransaction --> EZShop : return true

Cashier -> EZShop : applyDiscountRateToProduct(transactionId, productCode, discountRate)
EZShop -> mSaleTransaction : openst.getEntries()
    mSaleTransaction --> EZShop : return entries
EZShop -> mSaleTransaction : t.setDiscountRate(discountRate)
EZShop -> mSaleTransaction : openst.computePrice()
Cashier <-- EZShop : return true

Cashier -> EZShop : endSaleTransaction(transactionId)
    EZShop -> mSaleTransaction : openSt.insert()
    mSaleTransaction --> EZShop : return true
EZShop --> Cashier : return true

Cashier -> Cashier : ManagePayment (Scenario 7.1)
```

## Scenario 6.4

```plantuml
actor Cashier
Cashier -> EZShop : startSaleTransaction()
    EZShop -> mSaleTransaction : SaleTransaction()
    mSaleTransaction --> EZShop : return SaleTransaction st
EZShop --> Cashier : return st.getTicketNumber()

Cashier -> EZShop : addProductToSale(transactionId, productCode, amount)
EZShop -> EZShop : getProductTypeByBarcode(barcode)
EZShop -> mSaleTransaction : ost.getTicketNumber()
EZShop -> mProductType : p.setQuantity(quantity)
EZShop -> mTicketEntry : te1.getBarCode()
    mTicketEntry --> EZShop : return barcode    
EZShop -> mTicketEntry : te1.setAmount()
EZShop -> mSaleTransaction : st.computePrice()
EZShop -> mTicketEntry : mTicketEntry()
    mTicketEntry --> EZShop : return te2
EZShop -> mSaleTransaction : st.addEntries(entry)
EZShop -> mSaleTransaction : st.computePrice()
mSaleTransaction --> EZShop : return true

Cashier -> EZShop : endSaleTransaction(transactionId)
    EZShop -> mSaleTransaction : openSt.insert()
    mSaleTransaction --> EZShop : return true
EZShop --> Cashier : return true

Cashier -> Cashier : ManagePayment (Scenario 7.1)

Cashier -> EZShop : computePointsForSale(transactionId)
EZShop -> mSaleTransaction : s.getPrice()
Cashier <-- EZShop : return points

Cashier -> EZShop : modifyPointsOnCard(customerCard, int pointsToBeAdded)
    EZShop -> mCustomer : cu.getPoints()
        mCustomer --> EZShop : return oldPoints
    EZShop -> mCustomer : cu.update(points)
    mCustomer --> EZShop : return true
Cashier <-- EZShop : return true

```

## Scenario 6.5

```plantuml
actor Cashier
Cashier -> EZShop : startSaleTransaction()
    EZShop -> SaleTransaction : SaleTransaction()
    SaleTransaction --> EZShop : return SaleTransaction st
EZShop --> Cashier : return st.getTicketNumber()

Cashier -> EZShop : addProductToSale(transactionId, productCode, amount)
EZShop -> EZShop : getProductTypeByBarcode(barcode)
EZShop -> mSaleTransaction : ost.getTicketNumber()
EZShop -> mProductType : p.setQuantity(quantity)
EZShop -> mTicketEntry : te1.getBarCode()
    mTicketEntry --> EZShop : return barcode    
EZShop -> mTicketEntry : te1.setAmount()
EZShop -> mSaleTransaction : st.computePrice()
EZShop -> mTicketEntry : mTicketEntry()
    mTicketEntry --> EZShop : return te2
EZShop -> mSaleTransaction : st.addEntries(entry)
EZShop -> mSaleTransaction : st.computePrice()
mSaleTransaction --> EZShop : return true
Cashier -> Cashier : Customer cancels payment
Cashier -> EZShop : deleteSaleTransaction(transactionId)
EZShop -> EZShop : getSaleTransaction(transactionId)
EZShop -> SaleTransaction : st.getBalanceId()
EZShop -> SaleTransaction : st.Delete()
SaleTransaction --> EZShop : return true
EZShop --> Cashier : return true
```

## Scenario 7.1

```plantuml
actor User
User -> EZShop : receiveCreditCardPayment(transactionId, creditCard)
    EZShop -> EZShop : checkCreditCardValidity(creditCard)    
    EZShop -> EZShop : getSaleTransaction(transactionId)
    EZShop -> EZShop : checkCreditCardValidity(creditCard)
        EZShop -> EZShop : attemptPayment(creditcard, cost)
        EZShop -> mProductType : p.update()
            mProductType --> EZShop : return true
        EZShop -> mBalanceOperation : mBalanceOperation(money, type)
            mBalanceOperation --> EZShop : return bo
        EZShop -> mBalanceOperation : bo.insert()
            mBalanceOperation --> EZShop : return true
        EZShop -> mSaleTransaction : st.update()
            mSaleTransaction --> EZShop : return true
EZShop --> User : return true
```

## Scenario 7.3

```plantuml
actor User
User -> EZShop : receiveCreditCardPayment(transactionId, creditCard)
    EZShop -> EZShop : checkCreditCardValidity(creditCard)    
    EZShop -> EZShop : getSaleTransaction(transactionId)
    EZShop -> EZShop : checkCreditCardValidity(creditCard)
        EZShop -> EZShop : attemptPayment(creditcard, cost)
    Note left: Payment failed
EZShop --> User : return false
```

## Scenario 7.4

```plantuml
actor User
mUser -> EZShop : receiveCashPayment(transactionId, total)
    EZShop -> EZShop : getSaleTransaction(transactionId)
EZShop -> mProductType : p.update()
    mProductType --> EZShop : return true
EZShop -> mBalanceOperation : mBalanceOperation(money, type)
    mBalanceOperation --> EZShop : return bo
EZShop -> mBalanceOperation : bo.insert()
    mBalanceOperation --> EZShop : return true
EZShop -> mSaleTransaction : st.update()
    mSaleTransaction --> EZShop : return true
EZShop -> EZShop : computeChange(cash,price)
EZShop --> mUser : return change 
```

## Scenario 8.1 

```plantuml
actor User
mUser -> EZShop : startReturnTransaction(transactionId)
EZShop -> EZShop : getSaleTransaction(transactionId)
EZShop -> mReturnTransaction : mReturnTransaction(transactionId, discountRate)
mReturnTransaction --> EZShop : return rt
EZShop --> mUser : return rt.getId()
mUser -> EZShop : returnProduct(returnId, productCode, amount)
EZShop -> EZShop : getProductTypeByBarCode(barCode)
EZShop -> EZShop : getReturnTransaction(transactionId)
EZShop -> mReturnTransaction : mTicketEntry()
EZShop -> mTicketEntry : rt.addEntries(ticketEntry)
EZShop -> EZShop : rt.computePrice()
EZShop --> mUser : return true
mUser -> mUser : ManageReturnPayment (Scenario 10.1)
mUser -> EZShop : endReturnTransaction(returnId, commit)
EZShop -> EZShop : getSaleTransaction(transactionId)
EZShop -> mReturnTransaction : rt.insert()
mReturnTransaction --> EZShop : return true
EZShop -> mProductType : p.update()
mProductType --> EZShop : return true 
EZShop -> mSaleTransaction : st.removeEntries(entry)
EZShop -> mTicketEntry : stEntry.update()
mTicketEntry --> EZShop : return true 
EZShop --> mUser : return true 
```

## Scenario 9.1 

```plantuml
actor User
User -> EZShop : getCreditsAndDebits(from, to)
EZShop --> User : return list
```

## Scenario 10.1 

```plantuml
actor User
User -> EZShop : returnCreditCardPayment(returnId, creditCard)
    EZShop -> EZShop : checkCreditCardValidity(creditCard)
    EZShop -> EZShop : getReturnTransaction(returnId)
    EZShop -> mReturnTransaction : rt.getPrice()
        EZShop <-- mReturnTransaction : return true
    EZShop -> EZShop : attemptRefund(creditcard, cost)
    EZShop -> mBalanceOperation : mBalanceOperation(money, type)
        EZShop <-- mBalanceOperation : return bo
    EZShop -> mReturnTransaction : rt.update()
        EZShop <-- mReturnTransaction : return true
    EZShop -> mBalanceOperation : bo.insert()
        EZShop <-- mBalanceOperation : return true
EZShop --> User : return price 
```
