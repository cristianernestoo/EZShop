package it.polito.ezshop.model;

import java.security.InvalidParameterException;
import java.sql.*;

public class mOrder implements it.polito.ezshop.data.Order{
    private static int counter = 0;
    private Integer orderId;
    private Integer balanceId;
    private String productCode;
    private double pricePerUnit;
    private int quantity;
    private String status;

    public mOrder(Integer orderId, Integer balanceId, String productCode, double pricePerUnit, int quantity, String status) {
        if(orderId == null || orderId <= 0 || balanceId == null || balanceId <=0 || productCode == null || productCode.equals("") || pricePerUnit <= 0 || quantity < 0 || status == null || (!status.equals("ISSUED") && !status.equals("PAYED") && !status.equals("COMPLETED"))) throw new InvalidParameterException();
        this.orderId = orderId;
        this.balanceId = balanceId;
        this.productCode = productCode;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.status = status;
        if(counter < orderId) counter=orderId;
    }

    public mOrder(String productCode, Integer balanceId, int quantity, double pricePerUnit, String status){
        if(productCode == null || productCode.equals("") || pricePerUnit <= 0 || quantity < 0 || status == null || (!status.equals("ISSUED") && !status.equals("PAYED") && !status.equals("COMPLETED"))) throw new InvalidParameterException();
        counter++;
        orderId = counter;
        this.balanceId = balanceId;
        this.productCode = productCode;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.status = status;
    }

    public static void reset() {counter = 0;}

    @Override
    public Integer getBalanceId() {
        return balanceId;
    }

    @Override
    public void setBalanceId(Integer balanceId) {
        if(balanceId == null || balanceId <=0) throw new InvalidParameterException();
        this.balanceId = balanceId;
    }

    @Override
    public String getProductCode() {
        return productCode;
    }

    @Override
    public void setProductCode(String productCode) {
        if(productCode == null || productCode.equals("")) throw new InvalidParameterException();
        this.productCode = productCode;
    }

    @Override
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit) {
        if(pricePerUnit <= 0) throw new InvalidParameterException();
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        if(quantity<0) throw new InvalidParameterException();
        this.quantity = quantity;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        if(status == null || (!status.equals("ISSUED") && !status.equals("PAYED") && !status.equals("COMPLETED"))) throw new InvalidParameterException();
        this.status = status;
    }

    @Override
    public Integer getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(Integer orderId) {
        if(orderId == null || orderId <= 0) throw new InvalidParameterException();
        this.orderId = orderId;
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

    public boolean insert(){
        String sql = "INSERT INTO Orders(orderId,balanceId,productCode,pricePerUnit,quantity,status) VALUES (?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            if(balanceId != null) pstmt.setInt(2, balanceId);
            else pstmt.setNull(2, Types.VARCHAR);
            pstmt.setString(3, productCode);
            pstmt.setDouble(4, pricePerUnit);
            pstmt.setInt(5, quantity);
            pstmt.setString(6, status);

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            counter--;
            return false;
        }
        return true;
    }

    public boolean update(String productCode, int quantity, double pricePerUnit, String status,Integer balanceId){
        if(orderId == null || orderId < 0 || productCode == null || productCode.equals("") || pricePerUnit < 0 || quantity < 0 || status == null || (!status.equals("ISSUED") && !status.equals("PAYED") && !status.equals("COMPLETED"))) throw new InvalidParameterException();

        String sql = "UPDATE Orders SET status = ? ,"
                + " balanceId = ? ,"
                + " productCode = ? ,"
                + " pricePerUnit = ? ,"
                + " quantity = ? "
                + " WHERE orderId = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, status);
            if(balanceId != null) pstmt.setInt(2, balanceId);
            else pstmt.setNull(2, Types.VARCHAR);
            pstmt.setString(3, productCode);
            pstmt.setDouble(4, pricePerUnit);
            pstmt.setInt(5, quantity);
            pstmt.setInt(6, orderId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        this.status = status;
        this.productCode = productCode;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.balanceId = balanceId;
        return true;
    }

    public boolean delete(){
        String sql = "DELETE FROM Orders WHERE orderId = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);

            // delete
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public boolean equals(mOrder mo) {
        if (mo == null) return false;
        return ((this.pricePerUnit == mo.pricePerUnit) && (this.status.equals(mo.status)) && (this.orderId.equals(mo.orderId)) && (this.productCode.equals(mo.productCode)) && (this.balanceId.equals(mo.balanceId)) && (this.quantity == mo.quantity));
    }
}
