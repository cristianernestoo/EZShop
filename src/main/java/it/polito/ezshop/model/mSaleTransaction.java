package it.polito.ezshop.model;
import it.polito.ezshop.data.*;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class mSaleTransaction implements it.polito.ezshop.data.SaleTransaction {

    private static Integer counter = 0;
    private Integer transactionId;
    private Integer balanceId;
    private double discountRate;
    private double price;
    private LinkedList<mTicketEntry> entries;

    public mSaleTransaction(Integer transactionId, Integer balanceId, double discountRate, double price, LinkedList<mTicketEntry> entries) {
        if(transactionId==null || transactionId<=0 ||  balanceId<=0 || discountRate<0 || discountRate>=1 || price<0 || entries==null) throw new InvalidParameterException();
        this.transactionId = transactionId;
        this.balanceId = balanceId;
        this.discountRate = discountRate;
        this.price = price;
        this.entries = entries;
        if(counter < transactionId) counter=transactionId;
    }

    public mSaleTransaction(double discountRate, double price, LinkedList<mTicketEntry> entries) {
        if(discountRate<0 || discountRate>=1 || price<0 || entries==null) throw new InvalidParameterException();
        counter++;
        this.transactionId = counter;
        this.discountRate = discountRate;
        this.price = price;
        this.entries = entries;
        balanceId = null;
    }

    public void computePrice(){
        price = 0 ;
        for(mTicketEntry t : entries) price += ((t.getPricePerUnit()*t.getAmount())*(1.00-t.getDiscountRate()));
        price = price * (1.00 - discountRate);
    }

    public Integer getBalanceId() {
        return balanceId;
    }

    public static void reset() {counter = 0;}

    @Override
    public Integer getTicketNumber() {
        return transactionId;
    }

    @Override
    public void setTicketNumber(Integer ticketNumber) {
        if(ticketNumber == null || ticketNumber<=0) throw new InvalidParameterException();
        this.transactionId = ticketNumber;
    }

    @Override
    public List<TicketEntry> getEntries() {
        return new LinkedList<>(entries);
    }

    @Override
    public void setEntries(List<TicketEntry> entries) {
        if(entries==null) throw new InvalidParameterException();
        this.entries =  new LinkedList(entries);
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

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        if(price<0) throw new InvalidParameterException();
        this.price = price;
    }

    public void addEntries(mTicketEntry entrytoAdd){
        if(entrytoAdd==null) throw new InvalidParameterException();
        entries.add(entrytoAdd);
    }

    public void removeEntries(mTicketEntry entrytoRemove){
        if(entrytoRemove==null) throw new InvalidParameterException();
        entries.remove(entrytoRemove);
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
        String sql = "INSERT INTO SaleTransaction(transactionId,balanceId,price,discountRate) VALUES (?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, transactionId);
            if(balanceId != null) pstmt.setInt(2, balanceId);
            else pstmt.setNull(2, Types.INTEGER);
            pstmt.setDouble(3, price);
            pstmt.setDouble(4, discountRate);

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        try {
            for (mTicketEntry t : entries) t.insert(transactionId,"SALE");
        }catch (SQLException e){
            for (mTicketEntry t : entries) t.delete();
            delete();
            return false;
        }
        return true;
    }

    public boolean update(double discountRate, Integer balanceId, double price){
        if(discountRate<0 || discountRate>=1 || price<0) throw new InvalidParameterException();
        String sql = "UPDATE SaleTransaction SET price = ? ,"
                + "balanceId = ? ,"
                + "discountRate = ? "
                + "WHERE transactionId = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, price);
            if(balanceId != null) pstmt.setInt(2, balanceId);
            else pstmt.setNull(2, Types.INTEGER);
            pstmt.setDouble(3, discountRate);
            pstmt.setInt(4,transactionId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        this.discountRate = discountRate;
        this.price = price;
        this.balanceId = balanceId;
        return true;
    }

    public boolean delete(){
        String sql = "DELETE FROM SaleTransaction WHERE transactionId = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, transactionId);

            // delete
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        for (mTicketEntry t : entries) t.delete();

        return true;
    }

    public boolean equals(mSaleTransaction s) {
        if (s == null) return false;
        if (!(Objects.equals(s.balanceId, this.balanceId))) return false;
        return (transactionId.equals(s.transactionId) && discountRate==s.discountRate && price==s.price && entries==s.entries);
    }
}