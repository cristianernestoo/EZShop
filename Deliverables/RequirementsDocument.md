# Requirements Document 

Authors: Semeraro Lorenzo, Ernesto Cristian, Marino Vincenzo, Matees Mihai Alexandru

Date: 21/04/2021

Version: 1.0

# Contents

- [Requirements Document](#requirements-document)
- [Contents](#contents)
- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
  - [Context Diagram](#context-diagram)
  - [Interfaces](#interfaces)
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
  - [Functional Requirements](#functional-requirements)
  - [Non Functional Requirements](#non-functional-requirements)
- [Access type](#access-type)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
  - [Use case diagram](#use-case-diagram)
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
    - [Use case 9, UC9 ADD SUPPLY](#use-case-9-uc9-add-supply)
        - [Scenario 9.1](#scenario-91)
- [Glossary](#glossary)
- [System Design](#system-design)
- [Deployment Diagram](#deployment-diagram)


<br />

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting

<br />
 
# Stakeholders

| Stakeholder name  | Description | 
| ----------------- |:-----------|
|   Shop Company     | The Company for which the application is developed.            | 
|   Owner     | Owner of the Shop Company. They are the first administrator(s) of the application, and they can register new Administrators.            | 
|    Administrator   |    Manager, Accountant or whoever is registered by a Owner in order to: <br /> &bull; view, edit and delete sales 	<br /> &bull; view, add, edit, and delete products in the inventory and fidelity cards <br /> &bull; add returns <br /> &bull;	use the support accounting functionality<br /> &bull;	authenticate themselves<br /> &bull; logout<br /> &bull; reset password. |
|   Manager	|   Person who is responsible for a part of the Shop Company, endowed with decision-making powers. They can have the role of Administrator.  |
|   Accountant	|   Person that keep, inspect, and analyse financial accounts. They can have the role of Administrator.  |
|   Cashier   | The employee that interacts directly with the Cash Register.|
|   Cash Register (Square Register)   | The system that records sales and is connected to the application. It is in charge to send to the application the data received from the PointOfSale and the Barcode Reader.|
|   PointOfSale (Square Terminal)   | The system that carries out credit card transactions and is connected to the Cash Register.|
|  Barcode Reader (Zebra Symbol LS2208)  | The hardware that scans the barcode of products and is connected to the Cash Register to update the inventory.|
|   Guest   | Whoever has access to the application. They can:<br /> &bull; View the catalogue<br /> &bull;	Search products. |
|  Product  |	Item in the inventory. |
|   Customer   |	Person that buys a product from the shop. |
|  Fidelity Card  |  A plastic card that identifies customers who partecipate in a loyalty program in order to obtain discounts and advantages.  |
|   Developer Company	| System administrator, DB administrator, Security administrator, IT administrator, code developers. |
|   SquareDeveloper	| Company that supplies the Square API to retrieve data from the Cash Register. |
|   Google Firebase	| Company that supplies the Firebase DBMS to manage the shop's data. |
|   Analyst   | 	Person that carries out the elicitation; formalize and inspect the requirements document. |
|   Wholesaler   |	Company that supplies products for the inventory.|


<br />
 

# Context Diagram and interfaces

## Context Diagram

<br />

```plantuml
:Administrator:

:Guest:
:Owner:
:CashRegister:

rectangle System{
    (EZShop)
}

Owner<-->EZShop
Owner-|>Administrator
Administrator-|>Guest
Administrator<-->EZShop
CashRegister-->EZShop
Guest<--EZShop
```


<br />

## Interfaces


| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|   Administrator | GUI | Desktop, mouse and keyboard | 
|   Guest   | GUI | Desktop, mouse and keyboard |
|  Cash Register | Square API | LAN |


<br />


# Stories and personas

Mario Rossi is a 48 year-old business owner. He manages a little food shop, registering all its sales and inventory on paper. He is always very busy and can not afford to spend time on these documents. He would like to automate and easily manage sale procedures and inventory directly through his computer. He also wants analyze sales in order to understand the trend of his customers.


Luca Verdi is a young manager that overviews the accounting of a dress shop. He would like to istantly monitor incomes and expenses from his cellphone. He further wants to easily calculate the shop's taxes without the risk to make mistakes. The shop he manages offers fidelity cards to the customers; he finds uncomfortable to consistently keep track of changes by paper in case of refunds or unexpected situations.


Maria Bianchi is a 30 year-old english teacher. She is always looking for the best offer and always checks on the internet the price of products; usually they don't reflect the ones in a physical shop. She would like her favourite shops to publish their catalogue with prices online: this would allow her to save time and easily find offers.

<br />
 
# Functional and non functional requirements

## Functional Requirements

| ID        | Description  |
| ------------- |:-------------| 
|  FR1     | Manage sales |
|  &nbsp; FR1.1   | &nbsp; Store sale   |
| &nbsp; FR1.2 |&nbsp; Delete sale |
| &nbsp; FR1.3 |&nbsp; Edit sale |
| &nbsp; FR1.4 |&nbsp; Manage return |
| FR2 | Manage inventory |
| &nbsp; FR2.1 |&nbsp;  Update inventory |
| &nbsp; FR2.2 |&nbsp;  Update catalogue |
| &nbsp; FR2.3 |&nbsp; Show inventory |
| &nbsp; FR2.4 |&nbsp; Show catalogue |
| &nbsp; FR2.5 |&nbsp; Search product |
| FR3 | Manage customers |
| &nbsp; FR3.1 |&nbsp; Create fidelity card |
|	&nbsp; FR3.2 |&nbsp; Show fidelity card points|
|	&nbsp; FR3.3 |&nbsp; Delete fidelity card |
|	&nbsp; FR3.4 |&nbsp; Update fidelity card |
| &nbsp; &nbsp;  FR3.4.1 |&nbsp; &nbsp; Add points |
| &nbsp; &nbsp;  FR3.4.2 |&nbsp; &nbsp; Reset points |
| &nbsp; &nbsp;  FR3.4.3 |&nbsp; &nbsp; Update customer information |
|	&nbsp; FR3.5 |&nbsp; Search fidelity card |
| FR4 | Support accounting |
|	&nbsp; FR4.1 |&nbsp; Register supply details |
|	&nbsp; FR4.2 |&nbsp; Delete supply details |
|	&nbsp; FR4.3 |&nbsp; Show cashflow statement |
| &nbsp; &nbsp;  FR4.2.1 |&nbsp; &nbsp; Show balance |
| &nbsp; &nbsp;  FR4.2.2 |&nbsp; &nbsp; Show incomes |
| &nbsp; &nbsp;  FR4.2.3 |&nbsp; &nbsp; Show expenses |
|FR5 | Authentication manager |
|	&nbsp; FR5.1 |&nbsp; Login |
| &nbsp; FR5.2 |&nbsp; Logout |
| &nbsp; FR5.3 |&nbsp; Reset password |
| &nbsp; FR5.4 |&nbsp; Register account |
| &nbsp; FR5.5 |&nbsp;  Delete an account |
| &nbsp; &nbsp;  FR5.5.1 |&nbsp; &nbsp; Delete own account |
| &nbsp; &nbsp;  FR5.5.2 |&nbsp; &nbsp; Delete other account |

	
<br />


## Non Functional Requirements

| ID        | Type            | Description  | Refers to |
| ------------- |:-------------:| :-----| -----:|
|NFR1|	Usability|	Time to learn the interface < 5 min|	All FRs|
|NFR2|	Efficiency|	Response time for catalogue queries < 1 s|	FR2|
|NFR3|	Efficiency|	Response time for generic query < 5 s|	All FRs|
|NFR4|	Security|	Encrypted password|FR5|
|NFR5|	Security|	Password must contain at least a number and an uppercase character|	FR5|
|NFR6|	Availability|	Available at least for the 99% of the time|	All FRs|
|NFR7|	Correctness	|Database size capable of storing 2000 different items|	FR2|
|NFR8|	Reliability|	Log table for the last 1000 queries|	All FRs|
|NFR9|	Maintainability|	Time to fix issues < 1 hrs|	All FRs|
|Domain1|	|	Currency is EURO|	FR1, FR2, FR4|
|Domain2|	|	VAT is computed as a percentage of the product price (4-10-22%)| 	FR1, FR2, FR4|
|Domain3| |		Cash register and Inventory system produce a .json file as output|	FR1, FR2|
|Domain4| |		Unique ID for persons is the italian formatted fiscal code|	FR3|


<br />

# Access type

| Function      | Owner | Administrator | Guest|
| ------------- |:-------------:| :-----| -----:|
|FR1|	Yes|	Yes|	No|
|FR2.1|	Yes|	Yes|	No|
|FR2.2|	Yes|	Yes|	No|
|FR2.3|	Yes|	Yes|	No|
|FR2.4|	Yes|	Yes|	Yes|
|FR2.5|	Yes|	Yes|	Yes|
|FR3|	Yes|	Yes|	No|
|FR4|	Yes|	Yes|	No|
|FR5.1|	Yes|	Yes|	No|
|FR5.2|	Yes|	Yes|	No|
|FR5.3|	Yes|	Yes|	No|
|FR5.4|	Yes|	 No|	No|
|FR5.5.1|	Yes|	 Yes|	No|
|FR5.5.2|	Yes|	 No|	No|




<br />

# Use case diagram and use cases

## Use case diagram


<br />

```plantuml

:Administrator:
:CashRegister:
:Guest:
:Owner:

rectangle System{
    (ManageSale)
    (ManageInventory)
    (ShowCatalogue)
    (ManageCustomers)
    (SupportAccounting)
    (Authentication)
    (RegisterAdministrator)
    (ManageReturn)
    (AddProduct)
    (AddSupply)
}

CashRegister<-->ManageSale
Administrator<-->ManageSale
Administrator<-->ManageInventory
Administrator<-->ManageCustomers
Owner-|>Administrator
Owner-->RegisterAdministrator
Administrator<--SupportAccounting
Administrator-|>Guest
ManageInventory ..> AddProduct : <<include>>
ManageSale ..> ManageReturn : <<include>>
SupportAccounting ..> AddSupply : <<include>>
AddSupply-->ManageInventory
Authentication ..> RegisterAdministrator : <<extend>>
Guest<--ShowCatalogue
ManageSale<-->ManageInventory
Administrator<-->Authentication

/'
(FR1_ManageSale) ..> (FR1.1_StoreSale) : <<include>>
(FR1_ManageSale) ..> (FR1.3_EditSale) : <<include>>

(FR1_ManageInventory) ..> (FR2.1_UpdateInventory) : <<include>>
(FR1_ManageInventory) ..> (FR2.2_ShowCatalogue) : <<include>>
'/

```


<br />

### Use case 1, UC1 SALE STORING

| Actors Involved | Cash Register |
| ------------- |:-------------| 
|  Precondition     | &bull;	A sale has taken place <br /> &bull;	The customer has not a fidelity card and does not request one|  
|  Postcondition     |  |
|  Nominal Scenario     | Sale correctly registered|
|  Variants     | Connection error, Product not in the inventory. |

<br />
 
##### Scenario 1.1 

| Scenario 1.1 | (Nominal) Sale correctly registered |
| ------------- |:-------------| 
|  Precondition |	 &bull; A sale has taken place <br /> &bull; Sale not yet registered <br /> &bull;	The customer has not a fidelity card and does not request one <br /> &bull; Cash Register connected to the system <br /> &bull; The sold product is in the inventory <br />  &bull; Successful file reception |
|  Postcondition     | Sale registered. |
| Step#        | Description  |
|1|	Cash Register sends sale data to the system.|
|2|	System reads data.|
|3|	System updates inventory.|
|4|	System registers sale.|

<br />
 
##### Scenario 1.2

| Scenario 1.2 | (Exception) Connection error  |
| ------------- |:-------------| 
|  Precondition     |  &bull; A sale has taken place  <br /> &bull;	The customer has not a fidelity card and does not request one<br /> &bull; Sale not yet registered <br /> &bull; Cash Register not connected to the system <br /> &bull; The sold product is in the inventory |
|  Postcondition     | Sale registered. |
| Step#        | Description  |
|1|	Cash Register tries to send sale data to the system.|
|2|Cash Register fails to send sale data to the system.|
|3|	Administrator adds the sale manually using the system.|
|4|	Administrator updates inventory accordingly.|

<br />
 
##### Scenario 1.3

| Scenario 1.3 | (Exception) Product not in the inventory  |
| ------------- |:-------------| 
|  Precondition     | &bull;	A sale has taken place <br /> &bull;	The customer has not a fidelity card and does not request one <br /> &bull;  Sale not yet registered <br /> &bull;  Cash Register connected to the system <br /> &bull;  The sold product is not in the inventory <br /> &bull;  Successful file reception <br /> &bull;  application not connected to internet |
|  Postcondition     |  |
| Step#        | Description  |
|1|	Cash Register sends sale data to the system.|
|2|	System reads data.|
|3|	System fails to update inventory (Server backend error).|
|4|	System registers sale.|
|5|	System registers error.|

<br />
 
 
### Use case 2, UC2 PRODUCT RETURN

| Actors Involved | CashRegister |
| ------------- |:-------------| 
|  Precondition     | &bull;	The sale is registered <br /> &bull;	A customer returns a product|  
|  Postcondition     |  |
|  Nominal Scenario     | Sale correctly returned |
|  Variants     | Connection error, Administrator starts return procedure  |

<br />
 
 ##### Scenario 2.1 

| Scenario 2.1 | (Nominal) Sale correctly returned |
| ------------- |:-------------| 
|  Precondition     | &bull;	The sale is registered <br /> &bull; Cash Register connected to application <br />  &bull;	A customer returns a product |
|  Postcondition     |  &bull; The sale is deleted correctly <br /> &bull; The return is registered <br /> &bull; The inventory is consequently updated. |
| Step#        | Description  |
|1|	Cash Register sends data about the return to the system.|
|2|	System reads the data about the return.|
|3|	System updates inventory.|
|4|	System registers return.|

<br />
 
 ##### Scenario 2.2 

| Scenario 2.2 | (Exception) Administrator starts return procedure |
| ------------- |:-------------| 
|  Precondition     | &bull;	The sale is registered <br /> &bull;	A customer returns a product <br /> &bull; Administrator is in the Manage Sales page|
|  Postcondition     |  &bull; The sale is deleted correctly <br /> &bull; The return is registered <br /> &bull; The inventory is consequently updated. |
| Step#        | Description  |
|1|	Administrator clicks on the show button.|
|2|	Administrator clicks on the return button of the corresponding product.|
|3|	Administrator selects the quantity to return.|
|4|	Administrator clicks on the confirm button.|
|5| System updates inventory.|
|6| System registers return.|

<br />


### Use case 3, UC3 ADD PRODUCT INVENTORY

| Actors Involved | Administrator |
| ------------- |:-------------| 
|  Precondition     | &bull; The administrator is logged in	 <br /> &bull; The administrator is on the Manage Inventory page|  
|  Postcondition     |  |
|  Nominal Scenario     | Product added in the inventory |
|  Variants     | Connection error. |

<br />
 
 ##### Scenario 3.1 

| Scenario 3.1 | (Nominal) Product added in the inventory |
| ------------- |:-------------| 
|  Precondition     | &bull; The administrator is logged in	 <br /> &bull; The administrator is on the Manage Inventory page|
|  Postcondition     | &bull; A product is added correctly <br /> &bull; The inventory is consequently updated. |
| Step#        | Description  |
|1|	Administrator clicks on the add button of the corresponding row.|
|2|	Administrator inserts details of the product in the form and uploads relative image from their local memory.|
|3|	Administrator clicks confirm.|

<br />

<br />

### Use case 4, UC4 CUSTOMER SEARCHES PRODUCT IN THE CATALOGUE

| Actors Involved | Guest |
| ------------- |:-------------| 
|  Precondition     | The guest is on the catalogue screen |   
|  Postcondition     | Catalogue screen updated |
|  Nominal Scenario     | Product found.|
|  Variants     | Connection error, Connection timeout, Product not in the catalogue. |

<br />
 
##### Scenario 4.1 

| Scenario 4.1 | (Nominal)  Product found |
| ------------- |:-------------| 
|  Precondition |	 &bull;	The guest is on the catalogue screen <br /> &bull; Product is in the catalogue <br /> &bull; Guest device is connected to internet |
|  Postcondition     |&bull;	Catalogue screen updated <br /> &bull; Product shown  |
| Step#        | Description  |
|1| Guest types the product name in the search text field.|
|2|	System filters inventory.|
|3|	System updates catalogue screen.|

<br /> 

### Use case 5, UC5 CREATE FIDELITY CARD

| Actors Involved | Administrator |
| ------------- |:-------------| 
|  Precondition     | &bull;	A customer requires a fidelity card <br /> &bull; Administrator is logged in|   
|  Postcondition     |  |
|  Nominal Scenario     | Fidelity card created |
|  Variants     | Connection error, Connection timeout, Customer already has one. |

<br />
 
##### Scenario 5.1 

| Scenario 5.1 | (Nominal)  Fidelity card created |
| ------------- |:-------------| 
|  Precondition |	&bull;	A customer requires a fidelity card <br /> &bull; Administrator is logged in <br /> &bull;	The customer does not have a fidelity card |
|  Postcondition     |&bull;	Fidelity card registered <br /> &bull; Fidelity card points set to 0  |
| Step#        | Description  |
|1| Administrator clicks on the add fidelity card button.|
|2|	Administrator types in the name, surname, phone number, birth date, address and unique ID.|
|3|	System checks if the ID is already registered|
|4|	System confirms that the ID is not registered and enables confirm button.|
|5|	Administrator clicks on confirm button.|
|6| System registers fidelity card|

<br />


### Use case 6, UC6 SHOW BALANCE

| Actors Involved | Administrator |
| ------------- |:-------------| 
|  Precondition     | Administrator is logged in|   
|  Postcondition     | Support accounting screen updated |
|  Nominal Scenario     | Balance shown for the current day |
|  Variants     | Balance shown for the last month, Balance shown for the last year, Balance shown for the last trimester, Balance shown for the company lifetime, Connection error. |

<br />

##### Scenario 6.1 

| Scenario 6.1 | (Nominal)  Balance shown for the current day |
| ------------- |:-------------| 
|  Precondition |&bull;  Administrator is logged in <br /> &bull;  Administrator is on the support accounting screen  |
|  Postcondition     | The daily balance is shown |
| Step#        | Description  |
|1| Administrator clicks on the show balance button.|
|2|	System computes daily balance.|
|3|	System updates support accounting screen.|

<br />

### Use case 7, UC7 ADMINISTRATOR AUTHENTICATION

| Actors Involved        | Administrator|
| ------------- |:-------------| 
|  Precondition     |  Administrator is not logged in |
|  Postcondition     |  |
|  Nominal Scenario     | Administrator logs in correctly |
|  Variants     | Connection error, Connection timeout, Wrong credentials, Forgot credentials  |

<br />

##### Scenario 7.1 

| Scenario 7.1 | (Nominal)  Administrator logs in correctly |
| ------------- |:-------------| 
|  Precondition |&bull;  Administrator is not logged in <br />&bull;  Administrator is in the login screen |
|  Postcondition     |&bull; Administrator is logged in and the default page is Manage Sales |
| Step#        | Description  |
|1|	Administrator inserts email|
|2|	System checks if the email is already registered|
|3|	System confirms that the email is registered|
|4|	Administrator inserts password|
|5|	Administrator presses login button|
|6|	System logs the Administrator in|

<br />

##### Scenario 7.2 

| Scenario 7.2 | (Exception)  Forgot credentials |
| ------------- |:-------------| 
|  Precondition |&bull;  Administrator is not logged in <br />&bull;  Administrator is in the login screen |
|  Postcondition     |&bull; Administrator changes the password |
| Step#        | Description  |
|1|	Administrator clicks on "Forgot your password" link|
|2|	Administrator inserts their email on the reset form|
|3|	System sends the email to reset the password|
|4|	Administrator inserts new password |
|5|	Administrator presses confirm button|
|6|	System shows the Login page|

<br />

### Use case 8, UC8 OWNER REGISTERS ADMINISTRATOR

| Actors Involved        | Owner, Administrator|
| ------------- |:-------------| 
|  Precondition     |  &bull;	Owner is logged in <br /> &bull;  Owner device is connected to internet <br /> &bull;  Administrator owns an e-mail <br /> &bull; Owner is in the Manage Administrator page |
|  Postcondition     |  |
|  Nominal Scenario     | Owner registers an account for the Administrator |
|  Variants     | Connection error.  Administrator has already an account. |

<br />
 
##### Scenario 8.1

| Scenario 8.1 | (Nominal) Owner register an account for the Administrator  |
| ------------- |:-------------| 
|  Precondition     | &bull;	Owner is logged in <br /> &bull; Administrator is not registered yet <br /> &bull;   Owner device is connected to internet <br /> &bull;  Administrator owns an e-mail <br /> &bull; Owner is in the Manage Administrator page |
|  Postcondition     | Administrator registered |
| Step#        | Description  |
|1| Owner clicks on add administrator button |
|2|	Owner inserts e-mail, name, surname, unique id, phone number, birth date, address|
|3|    System checks if the e-mail is already registered|
|4|    System confirms that the email is not registered|
|5|    Owner inserts password (Administrator can change it at the first login)|
|6|    System checks password format|
|7|    Password format is correct: System enables “register” button|
|8|    Owner press ”register” button|
|9|    System registers the Administrator|

<br />
 
### Use case 9, UC9 ADD SUPPLY

| Actors Involved| Administrator|
| ------------- |:-------------| 
|  Precondition     |  &bull;	Administrator is logged in <br /> &bull;  Administrator is on the Manage Supply page <br /> &bull; A supply has taken place |
|  Postcondition     |  |
|  Nominal Scenario     | Supply added correctly |
|  Variants     | Connection error. |

<br />
 
##### Scenario 9.1

| Scenario 9.1 | (Nominal) Supply added correctly  |
| ------------- |:-------------| 
|  Precondition     |  &bull;	Administrator is logged in <br /> &bull;  Administrator is on the Manage Supply page <br /> &bull; A supply has taken place |
|  Postcondition     |  &bull;  Supply added correctly<br /> &bull;  The inventory is consequently updated  |
| Step#        | Description  |
|1|	Administrator clicks on add supply button|
|2| Administrator clicks n times add products (n = number of products related to the supply) in order to add n rows |
|3| Administrator inserts products details in the form|
|4| Administrator clicks on confirm button |
|5| System updates the inventory accordingly to the supply|


<br />

# Glossary


<br />

```plantuml
class Guest {
}

class Administrator {
    account_e-mail
    account_password
}

class ProductDescriptor {
    barcode
    name
    product_attribute
    picture
}

class Product {
    id
    instance_attribute
    retail_price
    wholesale_price
    VAT
    certification
}

class Shop {
    name
    domain
    VAT_number
}

class Supply {
    id
    date
    total_cost
}

class WholeSaler {
    name
    VAT_number
}

class Sale {
    id
    date
    time
    total_price
}

class CashRegister {
    id
}

class POSSystem {
    id
} 

class BarcodeReader {
    id
} 

note left of Product
VAT is the
computed
value, not
a percentage
endnote

class FidelityCard {
    card_id
    creation_date
    N°points
}

class Person {
    name
    surname
    unique_id
    phone_number
    birth_date
}


class Address {
    nation
    city
    street
    civic_number
    zip_code
}

Product "*" -- ProductDescriptor
Catalogue -- "*" ProductDescriptor
Product "*" - Inventory

CashRegister -- BarcodeReader
CashRegister -- POSSystem

CashRegister "1..*" --- Shop

Administrator <|- Owner

Customer -- "0..1" FidelityCard

Customer --|> Person
Guest <|- Person
Administrator -|> Person

Inventory - Shop
Shop --- Catalogue

Administrator "1..*" -- Shop
Shop -- "*" Customer

Sale "*" -- CashRegister
Sale "1..*" ----- Customer
Sale --- "1..*" Product

WholeSaler -- "*" Supply
Shop --- "*" Supply
Supply "*" -- "1..*" ProductDescriptor

CashflowStatement -- "1..*" Administrator
Shop --- CashflowStatement

Address --- "*" Person
Shop --- Address:registered
Shop --- Address:operational

```
<br />
 
# System Design

Not really meaningful in this case.  Only software components are needed.

<br />
 
# Deployment Diagram 

<br />

```plantuml

node EZShopServer
node PC

File EZShopApplication
File EZShopBackend

EZShopApplication ..> PC : <<deploy>>
EZShopBackend ..> EZShopServer : <<deploy>>
PC - EZShopServer : Internet Link

```

