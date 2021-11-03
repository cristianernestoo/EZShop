package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class EZShop implements it.polito.ezshop.data.EZShopInterface {
    private static EZShop instance = null;

    private final ArrayList<mUser> listOfUsers = new ArrayList<>();
    private final HashMap<Integer, mProductType> inventory = new HashMap<>();
    private final ArrayList<mCustomer> listOfCustomers = new ArrayList<>();
    private final HashMap<String,Double> listOfCreditCards = new HashMap<>();
    private final LinkedList<mOrder> listOfOrders = new LinkedList<>();
    private final LinkedList<mBalanceOperation> balanceOperations = new LinkedList<>();
    private final LinkedList<mReturnTransaction> returnTransactions = new LinkedList<>();
    private final LinkedList<mSaleTransaction> saleTransactions = new LinkedList<>();
    private mSaleTransaction openSaleTransaction = null;
    private mReturnTransaction openReturnTransaction = null;

    private User loggedUser = null;
    private double balance = 0;
    Connection conn;

    public EZShop(){

        String [] sqltables = {"CREATE TABLE IF NOT EXISTS Customer ( \n id INTEGER PRIMARY KEY NOT NULL UNIQUE, \n customerName TEXT NOT NULL UNIQUE, \n	customerCard TEXT , \n	points INTEGER NOT NULL \n);",
                "CREATE TABLE IF NOT EXISTS BalanceOperation (\n	balanceId INTEGER PRIMARY KEY NOT NULL UNIQUE,\n date TEXT NOT NULL,\n money REAL NOT NULL,\n 	type TEXT NOT NULL\n );",
                "CREATE TABLE IF NOT EXISTS Orders (\n orderId INTEGER PRIMARY KEY NOT NULL UNIQUE,\n balanceId INTEGER UNIQUE,\n productCode TEXT NOT NULL,\n pricePerUnit REAL NOT NULL,\n quantity INTEGER NOT NULL,\n status TEXT NOT NULL\n );",
                "CREATE TABLE IF NOT EXISTS ProductType (\n id INTEGER PRIMARY KEY NOT NULL UNIQUE,\n barCode TEXT NOT NULL,\n productDescription TEXT,\n location TEXT,\n note TEXT,\n quantity INTEGER NOT NULL,\n pricePerUnit REAL NOT NULL\n );",
                "CREATE TABLE IF NOT EXISTS ReturnTransaction (\n returnId INTEGER PRIMARY KEY NOT NULL UNIQUE,\nbalanceId INTEGER UNIQUE,\n saleId INTEGER NOT NULL,\n price REAL NOT NULL,\n discountRate REAL NOT NULL\n );",
                "CREATE TABLE IF NOT EXISTS SaleTransaction (\n transactionId INTEGER PRIMARY KEY NOT NULL UNIQUE,\n balanceId INTEGER UNIQUE,\n price REAL NOT NULL,\n discountRate REAL NOT NULL\n );",
                "CREATE TABLE IF NOT EXISTS TicketEntry (\n id INTEGER NOT NULL PRIMARY KEY UNIQUE,\n transactionId INTEGER NOT NULL,\n barCode TEXT NOT NULL,\n productId INTEGER NOT NULL,\n pricePerUnit REAL NOT NULL,\n discountRate REAL NOT NULL,\n productDescription TEXT NOT NULL,\n amount INTEGER NOT NULL,\n type TEXT NOT NULL );",
                "CREATE TABLE IF NOT EXISTS Users (\n id INTEGER PRIMARY KEY NOT NULL UNIQUE,\n username TEXT NOT NULL UNIQUE,\n password TEXT NOT NULL,\n role TEXT NOT NULL\n );",
                "CREATE TABLE IF NOT EXISTS Products (\n rfid TEXT PRIMARY KEY NOT NULL,\n productTypeId INTEGER NOT NULL,\n ticketEntryId INTEGER,\n status TEXT\n );" };

        conn = this.connect();
        for(String s : sqltables) {
            dbSetupTable(s);
        }

        loadCreditCards();

        try(ResultSet res = load("Users")){
            while(res.next()) {
                listOfUsers.add(new mUser(res.getInt("id"),res.getString("username"),res.getString("password"),res.getString("role")));
            }
        } catch (SQLException e) {
            System.out.println("SQLException Users");
            e.printStackTrace();
        }

        try(ResultSet res = load("ProductType")){
            while(res.next()) {
                mPosition pos = null;
                if (res.getString("location")!=null) {
                    try {
                        pos = new mPosition(res.getString("location"));
                    }catch (InvalidLocationException e) {
                        e.printStackTrace();
                    }
                }
                inventory.put(res.getInt("id"), new mProductType(res.getInt("quantity"),pos,res.getString("note"),res.getString("productDescription"),res.getString("barCode"),res.getDouble("pricePerUnit"),res.getInt("id")));
                try (ResultSet ent = loadProdRFID(res.getInt("id"),"productTypeId")){
                    while(ent.next()){
                        if(!ent.getString("status").equals("SALE")) {
                            inventory.get(res.getInt("id")).addProduct(new mProduct(ent.getString("rfid")));
                        }
                    }
                }catch (SQLException e) {
                    System.out.println("SQLException TicketEntry");
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException ProductType");
            e.printStackTrace();
        }

        try(ResultSet res = load("Customer")){
            while(res.next()) {
                listOfCustomers.add( new mCustomer(res.getInt("id"),res.getString("customerName"),res.getString("customerCard"),res.getInt("points")));
            }
        } catch (SQLException e) {
            System.out.println("SQLException Customer");
            e.printStackTrace();
        }

        try(ResultSet res = load("BalanceOperation")){
            while(res.next()) {
                balanceOperations.add( new mBalanceOperation(res.getInt("balanceId"),LocalDate.parse(res.getString("Date")),res.getDouble("money"),res.getString("type")));
                if(res.getString("type").equals("CREDIT") || res.getString("type").equals("SALE")){
                    balance += res.getInt("money");
                } else {
                    balance -= res.getInt("money");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException BalanceOperation");
            e.printStackTrace();
        }

        try(ResultSet res = load("Orders")){
            while(res.next()) {
                listOfOrders.add( new mOrder(res.getInt("orderId"),res.getInt("balanceId"),res.getString("productCode"),res.getDouble("pricePerUnit"),res.getInt("quantity"),res.getString("status")));
            }
        } catch (SQLException e) {
            System.out.println("SQLException Orders");
            e.printStackTrace();
        }

        try(ResultSet res = load("ReturnTransaction")){
            while(res.next()) {
                LinkedList<mTicketEntry> entrylist = new LinkedList<>();
                try(ResultSet ent = loadEntries(res.getInt("returnId"),"RETURN")){
                    while(ent.next()) {
                        mTicketEntry t = new mTicketEntry(ent.getString("barCode"),ent.getInt("productId"),ent.getString("productDescription"),ent.getInt("amount"),ent.getDouble("pricePerUnit"),ent.getDouble("discountRate"));
                        try (ResultSet rf = loadProdRFID(ent.getInt("id"),"ticketEntryId")){
                            while(rf.next()){
                                t.addRFID(rf.getString("rfid"));
                            }
                        }catch (SQLException e) {
                            System.out.println("SQLException RFID/Ticket");
                            e.printStackTrace();
                        }
                        entrylist.add(t);
                    }
                } catch (SQLException e) {
                    System.out.println("SQLException TicketEntry");
                    e.printStackTrace();
                }
                returnTransactions.add( new mReturnTransaction(res.getInt("returnId"), res.getInt("balanceId"),res.getInt("saleId"),res.getDouble("price"),res.getDouble("discountRate"),entrylist));
            }
        } catch (SQLException e) {
            System.out.println("SQLException ReturnTransaction");
        }

        try(ResultSet res = load("SaleTransaction")){
            while(res.next()) {
                LinkedList<mTicketEntry> entrylist = new LinkedList<>();
                try(ResultSet ent = loadEntries(res.getInt("transactionId"),"SALE")){
                    while(ent.next()) {
                        mTicketEntry t = new mTicketEntry(ent.getString("barCode"),ent.getInt("productId"),ent.getString("productDescription"),ent.getInt("amount"),ent.getDouble("pricePerUnit"),ent.getDouble("discountRate"));
                        try (ResultSet rf = loadProdRFID(ent.getInt("id"),"ticketEntryId")){
                            while(rf.next()){
                                t.addRFID(rf.getString("rfid"));
                            }
                        }catch (SQLException e) {
                            System.out.println("SQLException RFID/Ticket");
                            e.printStackTrace();
                        }
                        entrylist.add(t);                    }
                } catch (SQLException e) {
                    System.out.println("SQLException TicketEntry");
                    e.printStackTrace();
                }
                saleTransactions.add( new mSaleTransaction(res.getInt("transactionId"),res.getInt("balanceId"),res.getDouble("discountRate"),res.getDouble("price"),entrylist));
            }
        } catch (SQLException e) {
            System.out.println("SQLException SaleTransaction");
            e.printStackTrace();
        }
        try{
            conn.close();
        }catch(SQLException e){
            System.out.println("SQLException Connection close\n");
            e.printStackTrace();
        }
    }

    private Connection connect(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private void dbSetupTable(String sql) {
        try (Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private ResultSet load(String table) throws SQLException{
        String sql = "SELECT * FROM " + table;
        Statement pstmt = conn.createStatement();
        return pstmt.executeQuery(sql);
    }

    private void loadCreditCards(){
        try {
            File f = new File("src/main/resources/creditcards.txt");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                if(line.charAt(0) !='#') listOfCreditCards.put(line.split("[;]")[0],Double.parseDouble(line.split("[;]")[1]));
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private ResultSet loadEntries(Integer saleId,String type) throws SQLException{
        String sql = "SELECT * FROM TicketEntry AS T "
                + "WHERE T.transactionId=" + saleId + " AND T.type='" + type+"'";
        Statement pstmt = conn.createStatement();
        return pstmt.executeQuery(sql);
    }

    private ResultSet loadProdRFID(Integer id,String type) throws SQLException{
        String sql = "SELECT * FROM Products AS P "
                + "WHERE P." + type + "=" + id;
        Statement pstmt = conn.createStatement();
        return pstmt.executeQuery(sql);
    }

    private void deleteContents(String table) throws SQLException{
        String sql = "DELETE FROM " + table;
        Statement pstmt = conn.createStatement();
        pstmt.executeUpdate(sql);
    }

    @Override
    public void reset() {
        conn = connect();
        String [] sql = {"BalanceOperation","SaleTransaction","ReturnTransaction","ProductType","Orders","TicketEntry","Customer","Users","Products"};
        try {
            for (String t : sql) deleteContents(t);
            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage() +"\n");
            e.printStackTrace();
        }
        openReturnTransaction = null;
        openSaleTransaction = null;

        mOrder.reset();
        listOfOrders.clear();

        mReturnTransaction.reset();
        returnTransactions.clear();

        mBalanceOperation.reset();
        balanceOperations.clear();

        mSaleTransaction.reset();
        saleTransactions.clear();

        mProductType.reset();
        inventory.clear();
        
        mCustomer.reset();
        listOfCustomers.clear();

        mUser.reset();
        listOfUsers.clear();

        listOfCreditCards.clear();
        loadCreditCards();

        loggedUser = null;

        balance = 0;
    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        if ((username == null) || username.equals("")) throw new InvalidUsernameException("Username is empty");
        if ((password == null) || password.equals("")) throw new InvalidPasswordException("Password is empty");
        if ((role == null) || (!role.equals("Administrator") && !role.equals("Cashier") && !role.equals("ShopManager"))) throw new InvalidRoleException("Role MUST BE Administrator or Cashier or ShopManager");

        for (mUser us : listOfUsers) {
            if (us.getUsername().equals(username)) {
                System.out.println("Username already used");
                return -1;
            }
        }
        try {
            mUser u = new mUser(username, password, role);
            if(u.insert()) {
                listOfUsers.add(u);
                return u.getId();
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;

    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        if(loggedUser == null || !loggedUser.getRole().equals("Administrator")) throw new UnauthorizedException();
        if(id == null || id <= 0) throw new InvalidUserIdException("Invalid user ID");

        mUser us = (mUser) getUser(id);
        if(us == null) return false;

        if(us.delete()) { //remove from db
            listOfUsers.remove(us);
            return true;
        }

        return false;
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        if(loggedUser == null || !loggedUser.getRole().equals("Administrator")) throw new UnauthorizedException();
        return new ArrayList<>(listOfUsers);
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        if(loggedUser == null || !loggedUser.getRole().equals("Administrator")) throw new UnauthorizedException();
        if(id == null || id <= 0) throw new InvalidUserIdException("Invalid user ID");

        for(mUser us : listOfUsers){
            if(us.getId().equals(id)){
                return us;
            }
        }
        return null;
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        if(loggedUser == null || !loggedUser.getRole().equals("Administrator")) throw new UnauthorizedException();
        if(id == null || id <= 0) throw new InvalidUserIdException("Invalid user ID");
        if(role == null || (!role.equals("Administrator") && !role.equals("Cashier") && !role.equals("ShopManager"))) throw new InvalidRoleException("Invalid user Role");

        mUser us = (mUser) getUser(id);
        if(us == null) return false;

        return us.update(us.getUsername(),us.getPassword(),role);

    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        if((username == null) || username.equals("")) throw new InvalidUsernameException("Username is empty");
        if((password == null) || password.equals("")) throw  new InvalidPasswordException("Password is empty");

        for(mUser user : listOfUsers){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedUser = user;
                return user;
            }
        }


        return null;
    }

    @Override
    public boolean logout() {
        if(loggedUser != null){
            loggedUser = null;
            return true;
        }
        return false;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(description == null || description.equals("")) throw new InvalidProductDescriptionException("Invalid product Description");
        if(productCode == null || productCode.equals("") || !isNumeric(productCode) || !barCodeValidator(productCode)) throw new InvalidProductCodeException("Invalid product Code");
        if(pricePerUnit <= 0) throw new InvalidPricePerUnitException("Price must be a positive number");

        if (getProductTypeByBarCode(productCode) == null) {
            mProductType p = new mProductType(note, description, productCode, pricePerUnit);
            if(p.insert()) {
                inventory.put(p.getId(),p);
                return p.getId();
            }
        }
        return -1;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(id == null || id <= 0) throw new InvalidProductIdException("Invalid product ID");
        if(newDescription == null || newDescription.equals("")) throw new InvalidProductDescriptionException("Invalid product Description");
        if(newCode == null || newCode.equals("") || !isNumeric(newCode) || !barCodeValidator(newCode)) throw new InvalidProductCodeException("Invalid product Code");
        if(newPrice <= 0) throw new InvalidPricePerUnitException("Price must be a positive number");

        mProductType p = inventory.get(id);

        // no products with given product id or another product already has the same barCode
        if((p == null) || (getProductTypeByBarCode(newCode) != null && !getProductTypeByBarCode(newCode).equals(p))) return false;
        if(p.update(p.getQuantity(), p.getPosition(), newNote, newDescription, newCode, newPrice)) {
            inventory.put(id, p);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(id == null || id <= 0) throw new InvalidProductIdException();
        if(inventory.get(id) == null) return false;
        if(inventory.get(id).delete()) {
            inventory.remove(id);
            return true;
        }

        return false;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        return new ArrayList<>(inventory.values());
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if (barCode == null || barCode.equals("") || !barCodeValidator(barCode)) throw new InvalidProductCodeException();

        for(mProductType p : inventory.values()){
            if(p.getBarCode().equals(barCode)) return p;
        }
        return null;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();

        LinkedList<ProductType> list = new LinkedList<>();

        if (description == null) {
            description = "";
        }

        for (Integer key : inventory.keySet()) {
            if(inventory.get(key).getProductDescription().toLowerCase().contains(description.toLowerCase())) list.add(inventory.get(key));
        }

        return list;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(productId == null || productId <= 0) throw new InvalidProductIdException();

        mProductType p = inventory.get(productId);
        if(p == null || p.getLocation()==null) return false;

        if(p.getQuantity() + toBeAdded > 0 && p.getLocation() != null ) {
            return p.update(p.getQuantity() + toBeAdded, p.getPosition(), p.getNote(), p.getProductDescription(), p.getBarCode(), p.getPricePerUnit());
        }
        return false;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(productId == null || productId <= 0) throw new InvalidProductIdException();
        mPosition newP = null;
        if(newPos != null && !newPos.equals("")) newP = new mPosition(newPos);

        if(inventory.containsKey(productId)) {
            mProductType p = inventory.get(productId);

            if (!checkPosition(newPos)) return false;

            return p.update(p.getQuantity() ,newP, p.getNote(), p.getProductDescription(), p.getBarCode(), p.getPricePerUnit());
        }
        return false;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(productCode==null || !barCodeValidator(productCode) || productCode.equals("")) throw new InvalidProductCodeException("Product code is invalid");
        if(quantity <=0) throw new InvalidQuantityException("Quantity must be greater than 0");
        if(pricePerUnit <= 0) throw new InvalidPricePerUnitException("Price per unit must be greater than 0");

        if(getProductTypeByBarCode(productCode) == null) return -1;
        mOrder o = new mOrder(productCode, null , quantity, pricePerUnit,"ISSUED");
        if(!o.insert()) return -1;
        listOfOrders.add(o);
        return o.getOrderId();
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(productCode==null || !barCodeValidator(productCode) || productCode.equals("")) throw new InvalidProductCodeException("Product code is invalid");
        if(quantity <=0) throw new InvalidQuantityException("Quantity must be greater than 0");
        if(pricePerUnit <= 0) throw new InvalidPricePerUnitException("Price per unit must be greater than 0");

        mBalanceOperation bO = new mBalanceOperation(quantity*pricePerUnit,"ORDER");
        if((balance - bO.getMoney()<0.00)) return -1;
        mOrder o = new mOrder(productCode, bO.getBalanceId(), quantity, pricePerUnit,"PAYED");
        if(getProductTypeByBarCode(productCode) == null) return -1;
        if(!o.insert()) return -1;
        if(!bO.insert()) {o.delete(); return -1;}
        balanceOperations.add(bO);
        listOfOrders.add(o);
        computeBalance();
        return o.getOrderId();
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(orderId == null || orderId <=0) throw new InvalidOrderIdException("Order Id is Invalid");

        for(mOrder o : listOfOrders){
            if(o.getOrderId().equals(orderId)){
                if(o.getStatus().equals("PAYED")) return true;
                if(o.getStatus().equals("ISSUED")) {
                    mBalanceOperation bO = new mBalanceOperation(o.getQuantity()*o.getPricePerUnit(),"ORDER");
                    if((balance - bO.getMoney()<0.00)) return false;
                    if(!o.update(o.getProductCode(),o.getQuantity(),o.getPricePerUnit(),"PAYED",bO.getBalanceId())) return false;
                    if(!bO.insert()){ o.update(o.getProductCode(),o.getQuantity(),o.getPricePerUnit(),"ISSUED",null); return false;}
                    balanceOperations.add(bO);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(orderId==null || orderId <=0) throw new InvalidOrderIdException("Order Id is Invalid");

        for(mOrder o : listOfOrders){
            if(o.getOrderId().equals(orderId)){
                if(o.getStatus().equals("COMPLETED")) return true;
                if(o.getStatus().equals("PAYED")) {
                    for(mProductType p: inventory.values()){
                        if(p.getBarCode().equals(o.getProductCode())){
                            if(p.getPosition()==null) throw new InvalidLocationException();
                            if(!o.update(o.getProductCode(),o.getQuantity(),o.getPricePerUnit(),"COMPLETED",o.getBalanceId())) return false;
                            if(!p.update(p.getQuantity()+o.getQuantity(),p.getPosition(),p.getNote(),p.getProductDescription(),p.getBarCode(),p.getPricePerUnit())) {
                                o.update(o.getProductCode(),o.getQuantity(),o.getPricePerUnit(),"PAYED",o.getBalanceId());
                                return false;
                            }
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom) throws InvalidOrderIdException, UnauthorizedException, 
InvalidLocationException, InvalidRFIDException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(orderId==null || orderId <=0) throw new InvalidOrderIdException("Order Id is Invalid");
        if(!isNumeric(RFIDfrom) || RFIDfrom.length()!=12 || RFIDfrom==null) throw new InvalidRFIDException();

        for(mOrder o : listOfOrders){
            if(o.getOrderId().equals(orderId)){
                if(o.getStatus().equals("COMPLETED")) return true;

                for(mProductType pe : inventory.values()){
                    for(String pr : pe.getProducts().keySet()){
                        if(RFIDfrom.compareTo(pr)<=0 && String.format("%012d",Long.parseLong(RFIDfrom)+o.getQuantity()-1).compareTo(pr)>=0) throw new InvalidRFIDException();
                    }
                }

                if(o.getStatus().equals("PAYED")) {
                    for(mProductType p: inventory.values()){
                        if(p.getBarCode().equals(o.getProductCode())){
                            if(p.getPosition()==null) throw new InvalidLocationException();
                            for(int i = 0 ; i < o.getQuantity(); i++){
                                mProduct prod = new mProduct(String.format("%012d",Long.parseLong(RFIDfrom)+i));
                                p.addProduct(prod);
                                prod.insert(p.getId());
                            }
                            if(!o.update(o.getProductCode(),o.getQuantity(),o.getPricePerUnit(),"COMPLETED",o.getBalanceId())) return false;
                            if(!p.update(p.getQuantity()+o.getQuantity(),p.getPosition(),p.getNote(),p.getProductDescription(),p.getBarCode(),p.getPricePerUnit())) {
                                o.update(o.getProductCode(),o.getQuantity(),o.getPricePerUnit(),"PAYED",o.getBalanceId());
                                return false;
                            }
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            }
        }
        return false;
    }
    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();

        return new ArrayList<>(listOfOrders);
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(customerName == null || customerName.equals("")) throw new InvalidCustomerNameException("Customer name is invalid");
        for(mCustomer a : listOfCustomers) if(a.getCustomerName().equals(customerName)) return -1;

        mCustomer c = new mCustomer(customerName);
        if(c.insert())
        {
            listOfCustomers.add(c);
            return c.getId();
        }
        return -1;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if (id == null || id <= 0) throw new InvalidCustomerIdException("Invalid id: it is MUST BE > 0");
        if (newCustomerName == null || newCustomerName.equals("")) throw new InvalidCustomerNameException("Customer name is invalid");
        for(mCustomer a : listOfCustomers) if( a.getCustomerName().equals(newCustomerName) && !a.getId().equals(id)) return false;

        if (newCustomerCard == null) {
            for (mCustomer cu : listOfCustomers) {
                if (cu.getId().equals(id)) {
                    return cu.update(cu.getPoints(),newCustomerName,cu.getCustomerCard());
                }
            }
        } else if (newCustomerCard.equals("")) {
            for (mCustomer cu : listOfCustomers) {
                if (cu.getId().equals(id)) {
                    return cu.update(cu.getPoints(),newCustomerName,newCustomerCard);
                }
            }
        } else if(newCustomerCard.length() != 10 || !isNumeric(newCustomerCard))  throw new InvalidCustomerCardException("Customer card MUST BE of 10 characters");


        for (Customer cu: listOfCustomers){
            if(cu.getCustomerCard() != null && cu.getCustomerCard().equals(newCustomerCard)) return false;
        }

        for (mCustomer cu : listOfCustomers) {
            if (cu.getId().equals(id)) {
                return cu.update(cu.getPoints(),newCustomerName,newCustomerCard);
            }
        }
        return false;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if (id == null || id <= 0) throw new InvalidCustomerIdException("Invalid id: it is MUST BE > 0");

        for (mCustomer cu : listOfCustomers) {
            if (cu.getId().equals(id)) {
                if(cu.delete()) {
                    listOfCustomers.remove(cu);
                    return true;
                }
                else return false;
            }
        }

        return false;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if (id == null || id <= 0) throw new InvalidCustomerIdException("Invalid id: it MUST BE > 0");

        for (Customer cu : listOfCustomers) {
            if (cu.getId().equals(id)) return cu;
        }

        return null;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        return new ArrayList<>(listOfCustomers);
    }

    @Override
    public String createCard() throws UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();

        String cardCode;
        long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        cardCode = Long.toString(number);

        return cardCode;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(customerId == null || customerId<=0) throw new InvalidCustomerIdException();
        if(customerCard == null || customerCard.equals("") || customerCard.length()!=10) throw new InvalidCustomerCardException();

        mCustomer cu = (mCustomer) getCustomer(customerId);

        if(cu == null) return false;
        for(mCustomer c : listOfCustomers) {
            if(c.getCustomerCard() != null)
                if(c.getCustomerCard().equals(customerCard)) return false;
        }

        return cu.update(0, cu.getCustomerName(), customerCard);
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        if(loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(customerCard == null || customerCard.equals(""))  throw new InvalidCustomerCardException();
        if(customerCard.length() != 10 || !isNumeric(customerCard)) throw new InvalidCustomerCardException("Customer card must be of 10 digits");

        for (mCustomer cu : listOfCustomers){
            if(cu.getCustomerCard() !=null)
                if(cu.getCustomerCard().equals(customerCard)){
                    if(cu.getPoints()+pointsToBeAdded < 0) return false;
                    return cu.update(cu.getPoints()+pointsToBeAdded,cu.getCustomerName(), cu.getCustomerCard());
                }
        }
        return false;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        mSaleTransaction st = new mSaleTransaction(0,0, new LinkedList<>());
        openSaleTransaction = st;
        return st.getTicketNumber();
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();
        if(productCode == null || productCode.equals("") || !barCodeValidator(productCode)) throw new InvalidProductCodeException();
        if(amount < 0) throw new InvalidQuantityException();

        mProductType p = null;
        for(mProductType item : inventory.values()){
            if(item.getBarCode().equals(productCode))
                p = item;
        }
        if(p == null) return false;
        if (p.getQuantity() < amount) return false;

        if(openSaleTransaction != null)
            if (openSaleTransaction.getTicketNumber().equals(transactionId)) {
                p.setQuantity(p.getQuantity()-amount);
                for(TicketEntry t : openSaleTransaction.getEntries()) {
                    if(t.getBarCode().equals(productCode)){
                        t.setAmount(t.getAmount()+amount);
                        openSaleTransaction.computePrice();
                        return true;
                    }
                }
                mTicketEntry t = new mTicketEntry(p.getBarCode(),p.getId(), p.getProductDescription(), amount, p.getPricePerUnit(), 0);
                openSaleTransaction.addEntries(t);
                openSaleTransaction.computePrice();
                return true;
            }

        return false;
    }

    @Override
    public boolean addProductToSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException{
        if (openSaleTransaction == null || transactionId == null || transactionId<1) throw new InvalidTransactionIdException();
        if (RFID == null || !isNumeric(RFID) || RFID.length() != 12) throw new InvalidRFIDException();
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        mProductType pt = getProductTypeByRFID(RFID);
        if (pt == null || !openSaleTransaction.getTicketNumber().equals(transactionId)) return false;

        pt.setQuantity(pt.getQuantity()-1);
        pt.deleteProduct(RFID);
        for(TicketEntry t : openSaleTransaction.getEntries()) {
            mTicketEntry mt = (mTicketEntry) t;
            if(mt.getProductId() == pt.getId()){
                mt.setAmount(t.getAmount()+1);
                mt.addRFID(RFID);
                openSaleTransaction.computePrice();
                return true;
            }
        }
        mTicketEntry mt = new mTicketEntry(pt.getBarCode(),pt.getId(), pt.getProductDescription(), 1, pt.getPricePerUnit(), 0);
        mt.addRFID(RFID);
        openSaleTransaction.addEntries(mt);
        openSaleTransaction.computePrice();
        return true;
    }
    
    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();
        if(productCode == null || productCode.equals("") || !barCodeValidator(productCode)) throw new InvalidProductCodeException();
        if(amount < 0) throw new InvalidQuantityException();

        mProductType p = null;
        for(mProductType item : inventory.values()){
            if(item.getBarCode().equals(productCode))
                p = item;
        }
        if(p == null) return false;
        if(openSaleTransaction != null)
            if (openSaleTransaction.getTicketNumber().equals(transactionId)) {
                for (TicketEntry entry : openSaleTransaction.getEntries()) {
                    if (entry.getBarCode().equals(productCode)) {
                        if (entry.getAmount() < amount) return false;
                        p.setQuantity(p.getQuantity()+amount);
                        if (entry.getAmount() == amount) {
                            openSaleTransaction.removeEntries((mTicketEntry) entry);
                            openSaleTransaction.computePrice();
                            return true;
                        }
                        entry.setAmount(entry.getAmount() - amount);
                        openSaleTransaction.computePrice();
                        return true;
                    }
                }
            }
        return false;
    }

    @Override
    public boolean deleteProductFromSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException{
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if (openSaleTransaction == null || transactionId == null || transactionId<1) throw new InvalidTransactionIdException();
        if (RFID == null || !isNumeric(RFID) || RFID.length() != 12) throw new InvalidRFIDException();
        if (!openSaleTransaction.getTicketNumber().equals(transactionId)) return false;

        for ( TicketEntry ticketEntry : openSaleTransaction.getEntries() ){
            mTicketEntry mTicketEntry = (mTicketEntry) ticketEntry;
            if(mTicketEntry.getProductByRFID(RFID).getRFID().equals(RFID)){
                mTicketEntry.setAmount(mTicketEntry.getAmount()-1);
                for (mProduct product : mTicketEntry.getProducts()) {
                    if(product.getRFID().equals(RFID)) {
                        getProductTypeByRFID(RFID).setQuantity(getProductTypeByRFID(RFID).getQuantity()+1);
                        mTicketEntry.getProducts().remove(product);
                    }
                }
                openSaleTransaction.removeEntries((mTicketEntry) ticketEntry);
                openSaleTransaction.computePrice();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();
        if(productCode == null || productCode.equals("") || !barCodeValidator(productCode)) throw new InvalidProductCodeException();
        if(discountRate < 0.00 || discountRate >= 1.00) throw new InvalidDiscountRateException();

        if(openSaleTransaction != null)
            if (openSaleTransaction.getTicketNumber().equals(transactionId)) {
                for (TicketEntry t : openSaleTransaction.getEntries()) {
                    if (t.getBarCode().equals(productCode)) {
                        t.setDiscountRate(discountRate);
                        openSaleTransaction.computePrice();
                        return true;
                    }
                }
            }
        return false;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();
        if(discountRate < 0 || discountRate >= 1.00) throw new InvalidDiscountRateException();

        if(openSaleTransaction != null)
            if (openSaleTransaction.getTicketNumber().equals(transactionId)) {
                openSaleTransaction.setDiscountRate(discountRate);
                openSaleTransaction.computePrice();
                openSaleTransaction.computePrice();
                return true;
            }

        for (mSaleTransaction saleTransaction : saleTransactions) {
            if (saleTransaction.getTicketNumber().equals(transactionId) && saleTransaction.getBalanceId() == null) {
                saleTransaction.setDiscountRate(discountRate);
                saleTransaction.computePrice();
                saleTransaction.computePrice();
                return true;
            }
        }
        return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();
        for(mSaleTransaction s : saleTransactions){
            if(s.getTicketNumber().equals(transactionId)) return (int) (s.getPrice()/10.00);
        }

        if(openSaleTransaction != null)
            if(openSaleTransaction.getTicketNumber().equals(transactionId)) return (int) (openSaleTransaction.getPrice()/10.00);

        return -1;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();

        if(openSaleTransaction != null)
            if(openSaleTransaction.getTicketNumber().equals(transactionId)) {
                if(!openSaleTransaction.insert()) return false;
                saleTransactions.add(openSaleTransaction);
                openSaleTransaction = null;
                return true;
            }

        return false;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();

        mSaleTransaction st = (mSaleTransaction) getSaleTransaction(saleNumber);
        if (st == null) {
            if (openSaleTransaction == null)
                return false;
            if (!openSaleTransaction.getTicketNumber().equals(saleNumber))
                return false;
            st = openSaleTransaction;
        }
        if (st.getBalanceId() != null) return false;
        if (st.delete()) {
            for (TicketEntry t : st.getEntries())
                for(mProductType p : inventory.values())
                    if(p.getBarCode().equals(t.getBarCode()))
                        p.setQuantity(p.getQuantity()+t.getAmount());
            saleTransactions.remove(st);
            return true;
        }
        return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();

        for (mSaleTransaction st:saleTransactions) {
            if (st.getTicketNumber().equals(transactionId)) {
                return st;
            }
        }
        return null;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if (saleNumber == null || saleNumber <= 0) throw new InvalidTransactionIdException();

        mSaleTransaction mst = (mSaleTransaction) getSaleTransaction(saleNumber);
        if(mst == null || mst.getBalanceId() == null) return -1;

        mReturnTransaction rt = new mReturnTransaction(saleNumber,mst.getDiscountRate());
        openReturnTransaction = rt;
        return rt.getReturnId();
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if (returnId == null || returnId <= 0) throw new InvalidTransactionIdException();
        if (productCode == null || productCode.equals("") || !barCodeValidator(productCode)) throw new InvalidProductCodeException();
        if (amount <= 0) throw new InvalidQuantityException();

        mProductType p = (mProductType) getProductTypeByBarCode(productCode);
        if (p == null || openReturnTransaction == null) return false;

        if (openReturnTransaction.getReturnId().equals(returnId)) {
            mSaleTransaction saleTransaction = (mSaleTransaction) getSaleTransaction(openReturnTransaction.getSaleId());
            if (saleTransaction == null) return false;

            for (TicketEntry ent : saleTransaction.getEntries()) {
                mTicketEntry entry = (mTicketEntry) ent;
                if (entry.getBarCode().equals(productCode)) {
                    if (amount>entry.getAmount()) return false;
                    mTicketEntry t = new mTicketEntry(entry.getBarCode(),entry.getProductId(),entry.getProductDescription(),amount,entry.getPricePerUnit(),entry.getDiscountRate());
                    openReturnTransaction.addEntries(t);
                    openReturnTransaction.computePrice();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean returnProductRFID(Integer returnId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException 
    {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if (returnId == null || returnId <= 0) throw new InvalidTransactionIdException();
        if (RFID == null || !isNumeric(RFID) || RFID.length() != 12) throw new InvalidRFIDException();

        if (openReturnTransaction == null) return false;

        if (openReturnTransaction.getReturnId().equals(returnId)) {
            mSaleTransaction saleTransaction = (mSaleTransaction) getSaleTransaction(openReturnTransaction.getSaleId());
            if (saleTransaction == null) return false;

            for (TicketEntry ent : saleTransaction.getEntries()) {
                mTicketEntry entry = (mTicketEntry) ent;
                for (mProduct rf : entry.getProducts()) {
                    if (rf.equals(new mProduct(RFID))) {
                        for(mProductType p : inventory.values()) {
                            if (p.getBarCode().equals(entry.getBarCode())) {
                                for (TicketEntry e : openReturnTransaction.getEntries()) {
                                    mTicketEntry oRTEntry = (mTicketEntry) e;
                                    if(oRTEntry.getBarCode().equals(entry.getBarCode())) {
                                        oRTEntry.addRFID(RFID);
                                        entry.getProducts().remove(rf);
                                        openReturnTransaction.computePrice();
                                        return true;
                                    }
                                }
                                mTicketEntry t = new mTicketEntry(entry.getBarCode(), entry.getProductId(), entry.getProductDescription(), 1, entry.getPricePerUnit(), entry.getDiscountRate());
                                t.addRFID(RFID);
                                openReturnTransaction.addEntries(t);
                                entry.getProducts().remove(rf);
                                entry.setAmount(entry.getAmount()-1);
                                if(entry.getAmount() == 0)
                                    saleTransaction.removeEntries(entry);
                                openReturnTransaction.computePrice();
                                return true;
                            }
                        }
                        return false;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if(returnId == null || returnId <= 0) throw new InvalidTransactionIdException();


        if(openReturnTransaction!= null && openReturnTransaction.getReturnId().equals(returnId)) {
            if(commit) {
                mSaleTransaction st = (mSaleTransaction) getSaleTransaction(openReturnTransaction.getSaleId());
                if (st == null)
                    return false;

                if(!openReturnTransaction.insert()) return false;
                for(int i=0; i<openReturnTransaction.getEntries().size(); i++) {
                    mTicketEntry rtEntry = (mTicketEntry) openReturnTransaction.getEntries().get(i);
                    mProductType p = inventory.get(rtEntry.getProductId());
                    if (p == null)
                        return false;

                    LinkedList<mProduct> returnedProducts = rtEntry.getProducts();
                    LinkedList<mProduct> currentProducts = new LinkedList<>(p.getProducts().values()) ;

                    currentProducts.addAll(returnedProducts);

                    //Modifying inventory
                    if(!p.update(p.getQuantity() + rtEntry.getAmount(), p.getPosition(), p.getNote(), p.getProductDescription(), p.getBarCode(), p.getPricePerUnit()))
                        return false;

                    //Modifying sale transaction entry
                    for(int j=0; j<st.getEntries().size(); j++) {
                        mTicketEntry stEntry = (mTicketEntry) st.getEntries().get(j);
                        if (rtEntry.getBarCode().equals(stEntry.getBarCode())) {
                            if (stEntry.getAmount() == rtEntry.getAmount()){
                                st.removeEntries(stEntry);
                                stEntry.delete();
                            } else if(stEntry.getAmount() > rtEntry.getAmount()){
                                if (!(stEntry.update(stEntry.getBarCode(), st.getTicketNumber(), stEntry.getAmount() - rtEntry.getAmount(), stEntry.getDiscountRate())))
                                    return false;
                            } else return false;
                        }
                    }
                }
                st.computePrice();
                returnTransactions.add(openReturnTransaction);
            }
            openReturnTransaction = null;
            return true;

        }
        return false;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if (returnId == null || returnId <= 0) throw new InvalidTransactionIdException();

        mReturnTransaction rt = getReturnTransaction(returnId);
        if(rt == null) return false;
        mSaleTransaction st= (mSaleTransaction) getSaleTransaction(rt.getSaleId());
        if(st == null) return false;
        if (rt.getBalanceId() != null) return false;
        if (rt.delete()) {
            for (TicketEntry t : rt.getEntries()) {
                for(mProductType p : inventory.values()) if(p.getBarCode().equals(t.getBarCode())) p.update(p.getQuantity()-t.getAmount(),p.getPosition(),p.getNote(),p.getProductDescription(),p.getBarCode(),p.getPricePerUnit());
                for(TicketEntry stE : st.getEntries()) {
                    if(stE.getBarCode().equals(t.getBarCode())) {
                        stE.setAmount(t.getAmount()+stE.getAmount());
                    } else {
                        st.addEntries((mTicketEntry) t);
                    }
                }
            }
            returnTransactions.remove(rt);
            return true;
        }
        return false;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if (cash <= 0.0) throw new InvalidPaymentException();
        if (ticketNumber == null || ticketNumber <= 0) throw new InvalidTransactionIdException();

        mSaleTransaction st = (mSaleTransaction) getSaleTransaction(ticketNumber);
        for(mProductType p : inventory.values()) if(!p.update(p.getQuantity(),p.getPosition(),p.getNote(),p.getProductDescription(),p.getBarCode(),p.getPricePerUnit())) return -1;
        if(st != null && cash >= st.getPrice()) {
            mBalanceOperation bo = new mBalanceOperation(getSaleTransaction(ticketNumber).getPrice(),"SALE");
            if(!bo.insert()) return -1;
            if(!st.update(st.getDiscountRate(),bo.getBalanceId(),st.getPrice())){
                bo.delete();
                return -1;
            }
            balanceOperations.add(bo);
            return computeChange(cash,st.getPrice());
        }
        return -1;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if (ticketNumber == null || ticketNumber <= 0) throw new InvalidTransactionIdException();
        if (creditCard == null || creditCard.equals("") || !checkCreditCardValidity(creditCard)) throw new InvalidCreditCardException();

        mSaleTransaction st = (mSaleTransaction) getSaleTransaction(ticketNumber);
        if(st != null && attemptPayment(creditCard,st.getPrice())) {
            for(mProductType p : inventory.values()) if(!p.update(p.getQuantity(),p.getPosition(),p.getNote(),p.getProductDescription(),p.getBarCode(),p.getPricePerUnit())) return false;
            mBalanceOperation bo = new mBalanceOperation(getSaleTransaction(ticketNumber).getPrice(),"SALE");
            if(!bo.insert()) return false;
            if(!st.update(st.getDiscountRate(),bo.getBalanceId(),st.getPrice())){
                bo.delete();
                attemptPayment(creditCard,-st.getPrice());
                return false;
            }
            balanceOperations.add(bo);
            return true;
        }
        return false;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if (returnId == null || returnId <= 0) throw new InvalidTransactionIdException();

        mReturnTransaction rt = getReturnTransaction(returnId);
        if(rt != null && balance >= rt.getPrice()) {
            mBalanceOperation bo = new mBalanceOperation(getReturnTransaction(returnId).getPrice(),"RETURN");
            if(!bo.insert()) return -1;
            if(!rt.update(bo.getBalanceId())){
                bo.delete();
                return -1;
            }
            balanceOperations.add(bo);
            return rt.getPrice();
        }
        return -1;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();
        if (returnId == null || returnId <= 0) throw new InvalidTransactionIdException();
        if (creditCard == null || creditCard.equals("") || !checkCreditCardValidity(creditCard)) throw new InvalidCreditCardException();

        mReturnTransaction rt = getReturnTransaction(returnId);
        if(rt!= null && attemptRefund(creditCard,rt.getPrice()) && balance >= rt.getPrice()) {
            mBalanceOperation bo = new mBalanceOperation(getReturnTransaction(returnId).getPrice(),"RETURN");
            if(!bo.insert()) return -1;
            if(!rt.update(bo.getBalanceId())){
                bo.delete();
                attemptRefund(creditCard,-rt.getPrice());
                return -1;
            }
            balanceOperations.add(bo);
            return rt.getPrice();
        }
        return -1;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();

        String type;
        if (balance + toBeAdded < 0) return false;
        if (toBeAdded > 0) {
            type = "CREDIT";
        }
        else {
            toBeAdded = toBeAdded * (-1);
            type = "DEBIT";
        }

        mBalanceOperation bo = new mBalanceOperation(toBeAdded, type);
        if(!bo.insert()) return false;
        balanceOperations.add(bo);
        computeBalance();
        return true;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();

        LinkedList<mBalanceOperation> list = new LinkedList<>();
        if (from!=null && to!=null && from.isAfter(to)) {
            LocalDate tmp = to;
            to = from;
            from = tmp;
        }
        for (mBalanceOperation t : balanceOperations) {
            LocalDate date = t.getDate();
            if(from != null) {
                if (date.isAfter(from) || date.isEqual(from)) {
                    if(to !=null) {
                        if (date.isBefore(to) || date.isEqual(to)) {
                            list.add(t);
                        }
                    }
                    else list.add(t);
                }
            }
            else if(to != null) {
                if (date.isBefore(to) || date.isEqual(to)) {
                    list.add(t);
                }
            }
            else list.add(t);
        }
        return new LinkedList<> (list);
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        if (loggedUser == null || !(loggedUser.getRole().equals("Cashier") || loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) throw new UnauthorizedException();

        balance = 0;
        for (mBalanceOperation t : balanceOperations) {
            if (t.getType().equals("CREDIT") || t.getType().equals("SALE")) balance += t.getMoney();
            else balance -= t.getMoney();
        }
        return balance;
    }


    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double num = Double.parseDouble(strNum);
            if (num < 0) return false;
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean barCodeValidator(String barCode) {
        if(barCode == null) return false;
        switch (barCode.length()) {
            case 12:
                barCode = "00" + barCode;
                break;
            case 13:
                barCode = "0" + barCode;
                break;
            case 14:
                break;
            default:
                //wrong number of digits
                return false;
        }

        int sum = 0;
        for (int i = 0; i < barCode.length() - 1; i++) {
            if (i % 2 == 0) {
                sum += Character.getNumericValue(barCode.charAt(i)) * 3;
            } else {
                sum += Character.getNumericValue(barCode.charAt(i));
            }
        }

        int last = Character.getNumericValue(barCode.charAt(barCode.length() - 1));
        int check = (10 - (sum % 10)) % 10;

        return check == last;
    }


    public boolean checkPosition(String position) throws NullPointerException, InvalidLocationException {
        if (position == null || position.equals("")) return true;
        new mPosition(position);
        for(mProductType p : inventory.values()) {
            if(p.getLocation()!=null) {
                if (p.getLocation().equals(position)) return false;
            }
        }
        return true;
    }


    public static boolean checkCreditCardValidity(String creditcard){
        if(creditcard == null || creditcard.equals("")) return false;
        int len = creditcard.length();
        int sum = 0;
        boolean isSecond = false;
        for (int i = len - 1; i >= 0; i--)
        {
            int digit = creditcard.charAt(i) - '0';
            if (isSecond)
                digit = digit * 2;

            sum += digit / 10;
            sum += digit % 10;

            isSecond = !isSecond;
        }
        return (sum % 10 == 0);
    }

    public boolean attemptPayment(String creditcard, double cost) throws IllegalArgumentException{
        if(!checkCreditCardValidity(creditcard)) return false;
        if(cost<0) throw new IllegalArgumentException();
        Double credit = listOfCreditCards.get(creditcard);
        if ( credit != null && credit > cost) {
            listOfCreditCards.put(creditcard,credit - cost);
            return true;
        }
        return false;
    }

    public boolean attemptRefund(String creditcard, double refund) throws IllegalArgumentException{
        if(refund < 0) throw new IllegalArgumentException();
        Double credit = listOfCreditCards.get(creditcard);
        if ( credit != null ) {
            listOfCreditCards.put(creditcard,credit + refund);
            return true;
        }
        return false;
    }

    public double computeChange(double cash,double cost) throws IllegalArgumentException {
        if(cash >=0 &&  cost >=0 && (cash-cost)>=0) return cash - cost;
        else throw new IllegalArgumentException();
    }

    public mReturnTransaction getReturnTransaction(Integer id)  throws InvalidTransactionIdException{

        if(id == null || id <= 0) throw new InvalidTransactionIdException();
        for (mReturnTransaction rt : returnTransactions) {
            if (id.equals(rt.getReturnId())) {
                return rt;
            }
        }
        return null;
    }

    public static EZShop getInstance(){
        if (instance == null) {
            instance = new EZShop();
        }
        return  instance;
    }

    public HashMap<Integer, mProductType> getInventory() {
        return inventory;
    }

    public LinkedList<mReturnTransaction> getReturnList(){
        return  returnTransactions;
    }
    public HashMap<String,Double> getListofCreditCards() {return listOfCreditCards;}

    public mProductType getProductTypeByRFID(String RFID){
        for (mProductType pt:inventory.values()){
            for (mProduct p:pt.getProducts().values()){
                if (RFID.equals(p.getRFID())) return pt;
            }
        }
        return null;
    }

    /// GETTERS ///

    public ArrayList<mUser> getListOfUsers() {
        return listOfUsers;
    }

    public ArrayList<mCustomer> getListOfCustomers() {
        return listOfCustomers;
    }

    public LinkedList<mOrder> getListOfOrders() {
        return listOfOrders;
    }

    public LinkedList<mBalanceOperation> getBalanceOperations() {
        return balanceOperations;
    }

    public LinkedList<mSaleTransaction> getSaleTransactions() {
        return saleTransactions;
    }

    public mSaleTransaction getOpenSaleTransaction() {
        return openSaleTransaction;
    }

    public mReturnTransaction getOpenReturnTransaction() {
        return openReturnTransaction;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(mUser mUser) {this.loggedUser = mUser;}

    public double getBalance() {
        return balance;
    }

}
