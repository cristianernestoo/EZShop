package it.polito.ezshop.model;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.sql.*;

public class mBalanceOperation implements it.polito.ezshop.data.BalanceOperation{
    private static int counter = 0;
    private int balanceId;
    private LocalDate date;
    private double money;
    private String type;

    public mBalanceOperation(int balanceId, LocalDate date, double money, String type) {
        if( balanceId <= 0 || date == null || type == null || (!type.equals("CREDIT") && !type.equals("DEBIT") && !type.equals("RETURN") && !type.equals("SALE") && !type.equals("ORDER"))) throw new InvalidParameterException();
        this.balanceId = balanceId;
        this.date = date;
        this.money = money;
        this.type = type;
        if(counter < balanceId) counter=balanceId;
    }
    public mBalanceOperation() {
        counter++;
        balanceId = counter;
        this.date = LocalDate.now();
        this.money = 0;
        this.type = "";
    }
    public mBalanceOperation(double money, String type) {
        if(type == null || (!type.equals("CREDIT") && !type.equals("DEBIT") && !type.equals("RETURN") && !type.equals("SALE") && !type.equals("ORDER"))) throw new InvalidParameterException();
        counter++;
        balanceId = counter;
        this.date = LocalDate.now();
        this.money = money;
        this.type = type;
    }

    public static void reset() {counter = 0;}

    @Override
    public int getBalanceId() {
        return balanceId;
    }

    @Override
    public void setBalanceId(int balanceId) {
        if(balanceId <= 0) throw new InvalidParameterException();
        this.balanceId = balanceId;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDate date) throws InvalidParameterException {
        if (date == null) throw new InvalidParameterException();
        this.date = date;
    }

    @Override
    public double getMoney() {
        return money;
    }

    @Override
    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type){
        if(type == null || (!type.equals("CREDIT") && !type.equals("DEBIT") && !type.equals("RETURN") && !type.equals("SALE") && !type.equals("ORDER"))) throw new InvalidParameterException();
        this.type = type;
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
        String sql = "INSERT INTO BalanceOperation(balanceId,date,money,type) VALUES (?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, balanceId);
            pstmt.setString(2, date.toString());
            pstmt.setDouble(3, money);
            pstmt.setString(4, type);

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            counter--;
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean update(){
        String sql = "UPDATE BalanceOperation SET money = ? ,"
                + "type = ? "
                + "WHERE balanceId = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, money);
            pstmt.setString(2, type);
            pstmt.setInt(3, balanceId);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean delete(){
        String sql = "DELETE FROM BalanceOperation WHERE balanceId = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, balanceId);

            // delete
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean equals(mBalanceOperation bo) {
        if (bo == null) return false;
        return (balanceId == bo.getBalanceId() && type.equals(bo.type) && date.equals(bo.date) && money == bo.money);
    }

}
