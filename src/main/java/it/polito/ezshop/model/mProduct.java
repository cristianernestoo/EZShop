package it.polito.ezshop.model;

import java.security.InvalidParameterException;
import java.sql.*;

public class mProduct {
    private String RFID;

    public mProduct(String RFID) {
        if (RFID.length() != 12) throw new InvalidParameterException();
        this.RFID = RFID;
    }

    public String getRFID() {
        return RFID;
    }
    public void setRFID(String RFID) {this.RFID = RFID;}

    private Connection connect(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean insert(Integer typeId){
        String sql = "INSERT INTO Products(rfid,productTypeId,ticketEntryId,status) VALUES (?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, RFID);
            pstmt.setInt(2, typeId);
            pstmt.setInt(3, 0);
            pstmt.setString(4, "NEW");

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean update(Integer entryId,String status){
        String sql = "UPDATE Products SET ticketEntryId = ? , status = ? WHERE rfid=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,entryId);
            pstmt.setString(2,status);
            pstmt.setString(3,RFID);

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean delete(){
        String sql = "DELETE FROM Products WHERE rfid = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, RFID);

            // delete
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean equals(mProduct p) {
        if (p == null) return false;
        return RFID.equals(p.RFID);
    }
}
