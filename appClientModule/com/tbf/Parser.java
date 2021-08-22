package com.tbf;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
@author Aime Nishimwe
 * @Date 02.27.2020
 * Desinged to accomadate the adjustment to reading data from database
 *
 */
public abstract class Parser<T> {
	/**
	 * @param a string that represents path to the file 
	 * return a file object that can be read using scanner
	 */
	public static File fromFile(String path) { //later on, we might have to read fromDatabase()
		File f = new File(path);
		
		return f;
	}
	/**
	 * uses mysql DRIVER_CLASS a connection anishimw database
	 * @return connection that helps to retrieve data from database
	 */
	public static Connection getConnection () {
		//Load the JDBC driver 
		String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
		
		try {
			Class.forName(DRIVER_CLASS).getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} 
		//create a connection to our database
		Connection conn = null;
		String password = "D59wfjtQ";
		String username = "anishimw";
		String url = "jdbc:mysql://cse.unl.edu/anishimw?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return conn;
	}
	/**
	 * Closes all resources including connection, preparedStatments, and resultSets
	 * @param conn
	 * @param prepared
	 * @param rs
	 */
	
	public static void closeResources (Connection conn, PreparedStatement prepared, ResultSet rs) {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}if(rs != null && !rs.isClosed()) {
				rs.close();
			}if(prepared != null && !prepared.isClosed()) {
				prepared.close();
			}
			
		}catch (SQLException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * Closes only preparedStatements and resultSets
	 * Sometimes, I wanted to run multiple queries, and did not to close Connection to the database
	 * Used method overloading to implement this functionlity
	 * @param prepared
	 * @param rs
	 */
	public static void closeResources (PreparedStatement prepared, ResultSet rs) {
		try {
			if(rs != null && !rs.isClosed()) {
				rs.close();
			}if(prepared != null && !prepared.isClosed()) {
				prepared.close();
			}
		}catch (SQLException e){
			throw new RuntimeException(e);
		}
	}
}
