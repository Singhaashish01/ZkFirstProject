package com.bg.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	private static Connection conn=null;
	private static final String DB_URL="jdbc:mysql://localhost:3306/Mobiledb";
	private static final String DB_USERNAME="root";
	private static final String DB_PASSWORD="root123";
	private static final String DB_DRIVER="com.mysql.cj.jdbc.Driver";

	private DBConnection() {
		try {
			Class.forName(DB_DRIVER);
			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
		} catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
	}
	
	public static Connection getConnection() {
		try {
			if(conn==null || conn.isClosed()) {
				new DBConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
