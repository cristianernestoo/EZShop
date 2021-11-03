package it.polito.ezshop.model;

import it.polito.ezshop.data.TicketEntry;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class mReturnTransaction{
    private static int counter = 0;
    private Integer returnId;
    private Integer balanceId;
    private Integer saleId;
    private double price;
    private double discountRate;
    LinkedList<mTicketEntry> entries;

    public mReturnTransaction(Integer returnId,Integer balanceId, Integer saleId, double price, double discountRate , LinkedList<mTicketEntry> entries) {
        if(returnId==null || returnId<=0 || balanceId==null || balanceId<=0 || discountRate<0 || discountRate>=1 || price<0 || entries==null) throw new InvalidParameterException();
        this.returnId = returnId;
        this.balanceId=balanceId;
        this.saleId = saleId;
        this.price = price;
        this.entries = entries;
        this.discountRate = discountRate ;
        if(counter < returnId) counter=returnId;
    }

    public mReturnTransaction(Integer saleId, double discountRate){
        if(saleId == null || saleId<0 || discountRate>=1) throw new InvalidParameterException();
        counter++ ;
        returnId = counter;
        this.saleId = saleId;
        this.price = 0;
        this.discountRate = discountRate ;
        this.entries = new LinkedList<>();
    }

    public static void reset() {counter = 0;}

    public void setBalanceId(Integer balanceId) {
        if(balanceId == null || balanceId <= 0) throw new InvalidParameterException();
        this.balanceId = balanceId;
    }

    public void setSaleId(Integer saleId) {
        if(saleId==null || saleId<=0) throw new InvalidParameterException();
        this.saleId = saleId;
    }

    public Integer getBalanceId() { return balanceId;}

    public List<TicketEntry> getEntries(){ return new LinkedList<>(entries); }

    public double getPrice() { return price; }

    public Integer getReturnId(){
        return returnId;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void addEntries(mTicketEntry entrytoAdd){
        if(entrytoAdd==null) throw new InvalidParameterException();
        entries.add(entrytoAdd);
    }

    public void computePrice(){
        price = 0 ;
        for(mTicketEntry t : entries) price += ((t.getPricePerUnit()*t.getAmount())*(1.00-t.getDiscountRate()));
        price = price * (1.00 - discountRate);
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
        String sql = "INSERT INTO ReturnTransaction(returnId,balanceId,saleId,price,discountRate) VALUES (?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, returnId);
            if(balanceId != null) pstmt.setInt(2, balanceId);
                else pstmt.setNull(2, Types.INTEGER);
            pstmt.setInt(3, saleId);
            pstmt.setDouble(4, price);
            pstmt.setDouble(5, discountRate);

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        try {
            for (mTicketEntry t : entries) t.insert(returnId,"RETURN");
        }catch (SQLException e){
            for (mTicketEntry t : entries) t.delete();
            delete();
            return false;
        }
        return true;
    }

    public boolean update(Integer balanceId) {
        String sql = "UPDATE ReturnTransaction SET balanceId = ? "
                + "WHERE returnId = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if(balanceId != null) pstmt.setInt(1, balanceId);
            else pstmt.setNull(1, Types.INTEGER);
            pstmt.setInt(2,returnId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        this.balanceId = balanceId;
        return true;
    }

    public boolean delete(){
        String sql = "DELETE FROM ReturnTransaction WHERE returnId = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, returnId);

            // delete
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        for (mTicketEntry t : entries) t.delete();

        return true;
    }

    public boolean equals(mReturnTransaction rt) {
        if (rt == null) return false;
        if (!(Objects.equals(rt.balanceId, this.balanceId))) return false;
        return (returnId.equals(rt.returnId) && discountRate == rt.discountRate && saleId.equals(rt.saleId) && price == rt.price);
    }
}
