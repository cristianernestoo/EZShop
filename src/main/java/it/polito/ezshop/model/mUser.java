package it.polito.ezshop.model;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings("ALL")
public class mUser implements it.polito.ezshop.data.User {
    private static int counter = 0;
    private int id;
    private String username;
    private String password;
    private String role;

    public mUser(int id, String username, String password, String role) {
        if(id <= 0 || username==null || password==null || role==null || username.equals("") || password.equals("") || (!role.equals("Administrator") && !role.equals("Cashier") && !role.equals("ShopManager"))) throw new InvalidParameterException();
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        if(counter < id) counter=id;
    }

    public mUser(String username, String password, String role){
        if(username==null || password==null || role==null || username.equals("") || password.equals("") || (!role.equals("Administrator") && !role.equals("Cashier") && !role.equals("ShopManager"))) throw new InvalidParameterException();
        this.username=username;
        this.password=password;
        this.role=role;
        counter++;
        id = counter;
    }

    public static void reset() {counter = 0;}

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
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        if(username == null || username.equals("")) throw new InvalidParameterException();
        this.username=username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        if(password == null || password.equals("")) throw new InvalidParameterException();
        this.password=password;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        if(role==null || role.equals("") || (!role.equals("Administrator") && !role.equals("Cashier") && !role.equals("ShopManager"))) throw new InvalidParameterException();
        this.role=role;
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
        String sql = "INSERT INTO Users(id,username,password,role) VALUES (?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, role);

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            counter--;
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean update(String username, String password, String role){
        if(username==null || password==null || role==null || username.equals("") || password.equals("") || (!role.equals("Administrator") && !role.equals("Cashier") && !role.equals("ShopManager"))) throw new InvalidParameterException();
        String sql = "UPDATE Users SET username = ? ,"
                + "password = ? ,"
                + "role = ? "
                + "WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setInt(4, id);

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        this.username = username;
        this.password = password;
        this.role = role;
        return true;
    }

    public boolean delete(){
        String sql = "DELETE FROM Users WHERE id = ?";

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

    public boolean equals(mUser u) {
        if (u == null) return false;
        return ((this.id == u.id) && (this.username.equals(u.username)) && (this.password.equals(u.password)) && (this.role.equals(u.role)));
    }
}