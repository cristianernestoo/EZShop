package it.polito.ezshop.model;

import it.polito.ezshop.data.TicketEntry;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.LinkedList;


public class mTicketEntry implements TicketEntry {

    private static int counter = 0;
    private int id;
    private int productId;
    private String barCode;
    private String productDescription;
    private int amount;
    private double pricePerUnit;
    private double discountRate;
    private LinkedList<mProduct> products;

    public mTicketEntry(int id, int productId, String barCode, String productDescription, int amount, double pricePerUnit, double discountRate) {
        if(id<=0 || productId<=0 || barCode==null || barCode.equals("") || productDescription==null || productDescription.equals("") || amount<=0 || pricePerUnit<=0 || discountRate<0 || discountRate>=1) throw new InvalidParameterException();
        this.id = id;
        this.productId = productId;
        this.barCode = barCode;
        this.productDescription = productDescription;
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
        this.discountRate = discountRate;
        this.products = new LinkedList<>();
        if(counter < id) counter= id;
    }

    public mTicketEntry(String barCode, int productId, String productDescription, int amount, double pricePerUnit, double discountRate) {
        if(productId<=0 || barCode==null || barCode.equals("") || productDescription==null || productDescription.equals("") || amount<=0 || pricePerUnit<=0 || discountRate<0 || discountRate>=1) throw new InvalidParameterException();
        counter++;
        this.id = counter;
        this.productId = productId;
        this.barCode = barCode;
        this.productDescription = productDescription;
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
        this.discountRate = discountRate;
        this.products = new LinkedList<>();
    }

    public static void reset() {
        counter=0;
    }
    
    @Override
    public String getBarCode() {
        return barCode;
    }

    @Override
    public void setBarCode(String barCode) {
        if(barCode==null || barCode.equals("")) throw new InvalidParameterException();
        this.barCode=barCode;
    }

    public int getProductId() {
        return productId;
    }

    @Override
    public String getProductDescription() {
        return productDescription;
    }

    @Override
    public void setProductDescription(String productDescription) {
        if(productDescription==null || productDescription.equals("")) throw new InvalidParameterException();
        this.productDescription=productDescription;
    }

    @Override
    public int getAmount() { return amount; }

    @Override
    public void setAmount(int amount) {
        if(amount<0) throw new InvalidParameterException();
        this.amount=amount;
    }

    @Override
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit) {
        if(pricePerUnit<=0) throw new InvalidParameterException();
        this.pricePerUnit=pricePerUnit;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
        if(discountRate<0 || discountRate>=1) throw new InvalidParameterException();
        this.discountRate = discountRate;
    }

    public LinkedList<mProduct> getProducts() {return products;}

    public mProduct getProductByRFID(String RFID) {
        for(mProduct p : products){
            if(p.getRFID().equals(RFID)) return p;
        }
        return  null;
    }

    public boolean addRFID(String RFID){
        if (RFID == null || RFID.length() != 12) return  false;
        products.add(new mProduct(RFID));
        return true;
    }

    private Connection connect(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean insert(Integer transactionId,String type) throws SQLException{
        if(transactionId == null || transactionId<=0 || type==null || type.equals("")) throw new InvalidParameterException();

        String sql = "INSERT INTO TicketEntry(id,transactionId,barCode,pricePerUnit,discountRate,productDescription,amount,type,productId) VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setInt(2, transactionId);
            pstmt.setString(3, barCode);
            pstmt.setDouble(4, pricePerUnit);
            pstmt.setDouble(5, discountRate);
            pstmt.setString(6, productDescription);
            pstmt.setInt(7,amount);
            pstmt.setString(8,type);
            pstmt.setInt(9, productId);

            // update
            pstmt.executeUpdate();
        }

        for (mProduct p : products) p.update(id,type);
        return true;
    }

    public boolean update(String barCode, int productId, int amount, double discountRate){
        if(productId<=0 || barCode==null || barCode.equals("") || amount<=0 || discountRate<0 || discountRate>=1) throw new InvalidParameterException();

        String sql = "UPDATE TicketEntry SET amount = ? , "
                + "discountRate = ? "
                + "WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, amount);
            pstmt.setDouble(2, discountRate);
            pstmt.setInt(3, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        this.barCode = barCode;
        this.productId = productId;
        this.amount = amount;
        this.discountRate = discountRate;
        return true;
    }

    public boolean delete(){
        String sql = "DELETE FROM TicketEntry WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            // delete
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean equals(mTicketEntry t) {
        if (t == null) return false;
        return (id==t.id && productId==t.productId && barCode.equals(t.barCode) && productDescription.equals(t.productDescription) && amount==t.amount && pricePerUnit==t.pricePerUnit && discountRate==t.discountRate);
    }
}
