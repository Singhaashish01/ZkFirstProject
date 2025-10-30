package com.operation.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.bg.dbconnection.DBConnection;
import com.operation.model.Mobile;

public class MobileServiceImplements implements MobileService {

    List<Mobile> mobile = new ArrayList<>();

    @Override
    public List<Mobile> findAllMobile() {
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM mobile");

                while (rs.next()) {
                    Mobile mobiles = new Mobile(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("image")
                    );
                    mobile.add(mobiles);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            return mobile;
        }
        return null;
    }

    // Method to fetch mobile by ID
    public Mobile findMobileById(int id) {
        Connection conn = DBConnection.getConnection();
        Mobile mobileObj = null;

        if (conn != null) {
            String sql = "SELECT * FROM mobile WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    mobileObj = new Mobile(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("image")
                    );
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return mobileObj;
    }

    public List<Mobile> findproductsByKey(String keyword) {
        if (keyword.isBlank()) {
            return mobile;
        }
        return null;
    }
    
    public void saveMobile(Mobile m) {
    	String query = "INSERT INTO mobile(brand,model,price,description,image)VALUES(?,?,?,?,?)";
    	try {
    		
    		Connection conn = DBConnection.getConnection();
    		PreparedStatement pstmt = conn.prepareStatement(query);
    		
    		pstmt.setString(1, m.getBrand());
    		pstmt.setString(2, m.getModel());
    		pstmt.setDouble(3, m.getPrice());
    		pstmt.setString(4, m.getDescription());
    		pstmt.setString(5, m.getImage());
    		pstmt.execute();
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
    }

    @Override
    public List<Mobile> findMobiles(String keyword) {
        return null;
    }

    public List<Mobile> search(String text) {
        return null;
    }

    public static void main(String[] args) {
        MobileServiceImplements service = new MobileServiceImplements();

      
        System.out.println("All Mobiles:");
        List<Mobile> all = service.findAllMobile();
        all.forEach(System.out::println);

        
    }
}
