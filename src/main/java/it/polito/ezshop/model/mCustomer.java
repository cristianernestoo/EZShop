package it.polito.ezshop.model;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.Objects;

public class mCustomer implements it.polito.ezshop.data.Customer {
    static private Integer counter = 0;
    private Integer id;
    private String customerName;
    private String customerCard;
    private Integer points;

    public mCustomer(Integer id, String customerName, String customerCard, Integer points) {
        if(id==null || id<=0 || customerName == null || customerName.equals("") || points == null || points<0) throw new InvalidParameterException();
        this.id = id;
        this.customerName = customerName;
        this.customerCard = customerCard;
        this.points = points;
        if(counter < id) counter=id;
    }

    public mCustomer(String customerName) {
        if(customerName == null || customerName.equals("")) throw new InvalidParameterException();
        counter++;
        id = counter;
        this.customerName = customerName;
        this.customerCard = null;
        this.points = 0;
    }

    public static void reset() {counter = 0;}

    @Override
    public String getCustomerName() {
        return customerName;
    }

    @Override
    public void setCustomerName(String customerName) {
        if(customerName == null || customerName.equals("")) throw new InvalidParameterException();
        this.customerName = customerName;
    }

    @Override
    public String getCustomerCard() {
        return customerCard;
    }

    @Override
    public void setCustomerCard(String customerCard) {
        this.customerCard = customerCard;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        if(id == null || id<=0) throw new InvalidParameterException();
        this.id = id;
    }

    @Override
    public Integer getPoints() {
        return points;
    }

    @Override
    public void setPoints(Integer points) {
        if(points == null || points<0) throw new InvalidParameterException();
        this.points = points;
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
        String sql = "INSERT INTO Customer(id,customerName,customerCard,points) VALUES (?,?,?,?)";

        try (Connection conn = this.connect();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, customerName);
            pstmt.setString(3, customerCard);
            pstmt.setInt(4, 0);

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            counter--;
            return false;
        }
        return true;
    }

    public boolean update(int points, String customerName, String customerCard){
        if(customerName == null || customerCard==null || customerName.equals("")) throw new InvalidParameterException();
        String sql = "UPDATE Customer SET points = ? ,"
                + " customerName = ?,"
                + " customerCard = ?"
                + " WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, points);
            pstmt.setString(2, customerName);
            pstmt.setString(3, customerCard);
            pstmt.setInt(4, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        this.points = points;
        this.customerName = customerName;
        this.customerCard = customerCard;
        return true;
    }

    public boolean delete(){
        String sql = "DELETE FROM Customer WHERE id = ?";

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
    public boolean equals(mCustomer mc) {
        if (mc == null) return false;
        if (!(Objects.equals(mc.customerCard, this.customerCard))) return false;
        return ((this.points.equals(mc.points)) && (this.customerName.equals(mc.customerName)));
    }

}
