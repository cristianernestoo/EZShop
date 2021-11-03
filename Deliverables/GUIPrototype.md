# Graphical User Interface Prototype  

Authors: Semeraro Lorenzo, Ernesto Cristian, Marino Vincenzo, Matees Mihai Alexandru

Date: 21/04/2021

Version: 1.0


- [Graphical User Interface Prototype](#graphical-user-interface-prototype)
- [Graphical User Interface with Use Cases](#graphical-user-interface-with-use-cases)
  - [Use case 1, UC1 SALE STORING](#use-case-1-uc1-sale-storing)
    - [Scenario 1.1](#scenario-11)
    - [Scenario 1.2](#scenario-12)
    - [Scenario 1.3](#scenario-13)
  - [Use case 2, UC2 PRODUCT RETURN](#use-case-2-uc2-product-return)
    - [Scenario 2.1](#scenario-21)
    - [Scenario 2.2](#scenario-22)
  - [Use case 3, UC3 ADD PRODUCT INVENTORY](#use-case-3-uc3-add-product-inventory)
    - [Scenario 3.1](#scenario-31)
  - [Use case 4, UC4 CUSTOMER SEARCHES PRODUCT IN THE CATALOGUE](#use-case-4-uc4-customer-searches-product-in-the-catalogue)
    - [Scenario 4.1](#scenario-41)
  - [Use case 5, UC5 CREATE FIDELITY CARD](#use-case-5-uc5-create-fidelity-card)
    - [Scenario 5.1](#scenario-51)
  - [Use case 6, UC6 SHOW BALANCE](#use-case-6-uc6-show-balance)
    - [Scenario 6.1](#scenario-61)
  - [Use case 7, UC7 ADMINISTRATOR AUTHENTICATION](#use-case-7-uc7-administrator-authentication)
    - [Scenario 7.1](#scenario-71)
    - [Scenario 7.2](#scenario-72)
  - [Use case 8, UC8 OWNER REGISTERS ADMINISTRATOR](#use-case-8-uc8-owner-registers-administrator)
    - [Scenario 8.1](#scenario-81)
  - [Use case 9, UC9 MANAGE SUPPLY](#use-case-9-uc9-manage-supply)
    - [Scenario 9.1](#scenario-91)
    



# Graphical User Interface with Use Cases

<br />



## Use case 1, UC1 SALE STORING

<br />

 
### Scenario 1.1 (Nominal) Sale correctly registered 

No GUI involved in this scenario.



<br />
 
### Scenario 1.2 (Exception) Connection error

In this case the Administrator can add the sale and update the inventory manually using the application.


<br />
 
 
![ManageSales](resources/Gui/ManageSales_ClickAdd_.png) 
![RegisterSale](resources/Gui/RegisterSale.png)
![ManageInventoryEditing](resources/Gui/ManageInventory_Editing_.png)


<br />

### Scenario 1.3 (Exception) Product not in the inventory

<br />

No GUI involved in this scenario.


<br />

## Use case 2, UC2 PRODUCT RETURN

<br />



<br />

### Scenario 2.1 (Nominal) Sale correctly returned

<br />

No GUI involved in this scenario.

<br />

### Scenario 2.2 (Exception) Administrator starts return procedure

<br />

![ManageSales](resources/Gui/ManageSales_ClickView_.png)
![SaleDetails](resources/Gui/SaleDetails.png)
![ReturnConfirmation](resources/Gui/ReturnConfirmation.png)


<br />

## Use case 3, UC3 ADD PRODUCT INVENTORY 

### Scenario 3.1 (Nominal) Product added in the inventory

<br />

![ManageInventory](resources/Gui/ManageInventory_ClickAdd_.png)
![AddProductInventory](resources/Gui/AddProductInventory.png)


<br />


## Use case 4, UC4 CUSTOMER SEARCHES PRODUCT IN THE CATALOGUE

<br /> 

### Scenario 4.1 (Nominal) Product found 

<br /> 

![LandingPage](resources/Gui/LandingPage.png)
![LandingPageSearch](resources/Gui/LandingPage_Search_.png)

## Use case 5, UC5 CREATE FIDELITY CARD

<br />

 
### Scenario 5.1 (Nominal) Fidelity card created

<br />

![ManageCustomer](resources/Gui/ManageCustomers.png)
![AddFidelityCard](resources/Gui/AddFidelityCard.png)
<br />


## Use case 6, UC6 SHOW BALANCE


<br />

### Scenario 6.1 (Nominal) Balance shown for the current day

<br />

![Accounting](resources/Gui/Accounting.png)
![BalanceSheet](resources/Gui/BalanceSheet.png)
<br />

## Use case 7, UC7 ADMINISTRATOR AUTHENTICATION

<br />

### Scenario 7.1 (Nominal) Administrator logs in correctly

![Login](resources/Gui/Login_ClickLogin_.png)


### Scenario 7.2 (Exception) Forgot credentials


<br />

![LoginForgot](resources/Gui/Login_ClickForgot_.png)
![ResetPassword](resources/Gui/ResetPassword.png)
![ResetPassForm](resources/Gui/ResetPassForm.png)

<br />

## Use case 8, UC8 OWNER REGISTERS ADMINISTRATOR




<br />
 
### Scenario 8.1 (Nominal) Owner registers an account for the Administrator

<br />

![ManageAdmins](resources/Gui/ManageAdmins.png)

![AdminRegister](resources/Gui/AdminRegister.png)

<br />

Administrator can change his password here.

<br />

![AdminDetails](resources/Gui/AccountSettings.png)

<br />

## Use case 9, UC9 MANAGE SUPPLY

<br />

### Scenario 9.1 (Nominal) Supply added correctly

<br />

![ManageSupply](resources/Gui/ManageSupply.png)
![RegisterSupplyAdd](resources/Gui/RegisterSupply_ClickAdd_.png)

![RegisterSupplyFinal](resources/Gui/RegisterSupply.png)
