package it.polito.ezshop.model;

import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.InvalidLocationException;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class mProductType implements ProductType {
    private String barCode;
    private double pricePerUnit;
    private Integer id;
    private Integer quantity;
    private mPosition location;
    private String note;
    private String productDescription;
    private HashMap<String, mProduct> products;
    private static int counter = 0;

    public mProductType(Integer quantity, mPosition location, String note, String productDescription, String barCode, double pricePerUnit, Integer id) {
        if(quantity == null || quantity < 0 || productDescription == null || barCode == null || barCode.equals("") || pricePerUnit <= 0 || id == null || id <= 0) throw new InvalidParameterException();
        this.quantity = quantity;
        this.location = location;
        this.note = note;
        this.productDescription = productDescription;
        this.barCode = barCode;
        this.pricePerUnit = pricePerUnit;
        this.id = id;
        if(counter < id) counter=id;
        products = new HashMap<>();
    }

    public mProductType(Integer quantity, mPosition location, String note, String productDescription, String barCode, double pricePerUnit) {
        if(quantity == null || quantity < 0 || productDescription == null || productDescription.equals("") || barCode == null || barCode.equals("") || pricePerUnit <= 0) throw new InvalidParameterException();
        counter++;
        this.quantity = quantity;
        this.location = location;
        this.note = note;
        this.productDescription = productDescription;
        this.barCode = barCode;
        this.pricePerUnit = pricePerUnit;
        this.id = counter;
        products = new HashMap<>();
    }

    public mProductType(String note, String productDescription, String barCode, double pricePerUnit) {
        if(productDescription == null || productDescription.equals("") || barCode == null || barCode.equals("") || pricePerUnit <= 0) throw new InvalidParameterException();
        this.note = note;
        this.productDescription = productDescription;
        this.barCode = barCode;
        this.pricePerUnit = pricePerUnit;
        this.quantity = 0;
        this.location = null;
        counter++;
        this.id = counter;
        products = new HashMap<>();
    }

    static public void reset () {counter = 0;}

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        if (quantity == null || quantity < 0) throw new InvalidParameterException();
        this.quantity = quantity;
    }

    @Override
    public String getLocation() {
        if(location == null) return null;
        return location.toString();
    }

    public HashMap<String, mProduct> getProducts() {
        return products;
    }

    @Override
    public void setLocation(String location){
        if(location == null || location.equals("")) {
            this.location = null;
            return;
        }
        try {
            this.location = new mPosition(location);
        } catch (InvalidLocationException e){
        	System.out.println(e.getMessage());
        }
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String note) {
    	this.note = note; 
    }

    @Override
    public String getProductDescription() {
        return productDescription;
    }

    @Override
    public void setProductDescription(String productDescription) {
        if (productDescription == null || productDescription.equals("")) throw new InvalidParameterException();
        this.productDescription = productDescription;
    }

    @Override
    public String getBarCode() {
        return barCode;
    }

    @Override
    public void setBarCode(String barCode) {
        if(barCode == null || barCode.equals("")) throw new InvalidParameterException();
        this.barCode = barCode;
    }

    @Override
    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        if(pricePerUnit == null || pricePerUnit < 0) throw new InvalidParameterException();
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        if(id==null || id <= 0) throw new InvalidParameterException();
        this.id = id;
    }

    public void setProducts(LinkedList<mProduct> products) {
        if(products == null) throw new InvalidParameterException();
        this.products.clear();
        for(mProduct p:products){
            this.products.put(p.getRFID(),p);
        }
    }

    public mPosition getPosition()  {
        return location;
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

    public boolean addProduct(mProduct product){
        if(product == null) return false;
        products.put(product.getRFID(),product);
        return true;
    }

    public boolean deleteProduct(String RFID){
        if (RFID == null || RFID.length() != 12) return false;
        products.remove(RFID);
        return true;
    }

    public boolean insert(){
        String sql = "INSERT INTO ProductType(id,barCode,productDescription,location,note,quantity,pricePerUnit) VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, barCode);
            pstmt.setString(3, productDescription);
            if (location != null) {
                pstmt.setString(4, location.toString());
            } else {
                pstmt.setNull(4, Types.VARCHAR);
            }
            pstmt.setString(5, note);
            pstmt.setInt(6, quantity);
            pstmt.setDouble(7, pricePerUnit);

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            counter--;
            return false;
        }
        return true;
    }

    public boolean update(Integer quantity, mPosition location, String note, String productDescription, String barCode, double pricePerUnit){
        if(quantity == null || quantity < 0 || productDescription == null || barCode == null || barCode.equals("") || pricePerUnit < 0) throw new InvalidParameterException();


        String sql = "UPDATE ProductType SET barCode = ?, "
                + "productDescription = ?, "
                + "location = ?, "
                + "note = ?, "
                + "quantity = ?, "
                + "pricePerUnit = ? "
                + "WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, barCode);
            pstmt.setString(2, productDescription);
            if(location != null) pstmt.setString(3, location.toString());
            else pstmt.setNull(3, Types.VARCHAR);
            pstmt.setString(4, note);
            pstmt.setInt(5, quantity);
            pstmt.setDouble(6, pricePerUnit);
            pstmt.setInt(7, id);

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        this.barCode = barCode;
        this.quantity = quantity;
        this.location = location;
        this.note = note;
        this.productDescription = productDescription;
        this.barCode = barCode;
        this.pricePerUnit = pricePerUnit;
        return true;
    }

    // update and also modify the list in local
    public boolean update(LinkedList<mProduct> products, Integer quantity, mPosition location, String note, String productDescription, String barCode, double pricePerUnit){
        if(quantity == null || quantity < 0 || productDescription == null || barCode == null || barCode.equals("") || pricePerUnit < 0) throw new InvalidParameterException();


        String sql = "UPDATE ProductType SET barCode = ?, "
                + "productDescription = ?, "
                + "location = ?, "
                + "note = ?, "
                + "quantity = ?, "
                + "pricePerUnit = ? "
                + "WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, barCode);
            pstmt.setString(2, productDescription);
            if(location != null) pstmt.setString(3, location.toString());
            else pstmt.setNull(3, Types.VARCHAR);
            pstmt.setString(4, note);
            pstmt.setInt(5, quantity);
            pstmt.setDouble(6, pricePerUnit);
            pstmt.setInt(7, id);

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        this.barCode = barCode;
        this.quantity = quantity;
        this.location = location;
        this.note = note;
        this.productDescription = productDescription;
        this.barCode = barCode;
        this.pricePerUnit = pricePerUnit;
        setProducts(products);
        return true;
    }

    public boolean delete(){
        String sql = "DELETE FROM ProductType WHERE id = ?";

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

    public boolean equals(mProductType pt) {
        if (pt == null) return false;
        if (!(Objects.equals(pt.location, this.location))) return false;
        return (productDescription.equals(pt.productDescription) && barCode.equals(pt.barCode) && quantity.equals(pt.quantity) && id.equals(pt.id) && note.equals(pt.note) && pricePerUnit == pt.pricePerUnit);
    }

}
