package com.youtube.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.codehaus.jettison.json.JSONArray;

import com.youtube.util.ToJSON;

/**
 * This java class will hold all the SQL queries.
 * 
 * Having all SQL/database code in one package makes it easier to maintain and audit
 * but increase complexity.
 * 
 * Note: we also used the extends Oracle308tube on this java class to inherit all
 * the methods in Oracle308tube.java
 * 
 * Note: The tutorial uses the name 308tube, but my own schema in my Oracle db XE 11.2 is called ORAXEDEV.
 * 
 * @author Jack
 *
 */
public class Schema308tube extends Oracle308tube {
	
	/**
	 * Method to insert one raw of data into the PC_Parts table.
	 * 
	 *  Important: The primary key on PC_PARTS table will auto increment.
	 * 	That means the PC_PARTS_PK column does not need to be apart of the 
	 * 	SQL insert query below.
	 * 
	 * @param PC_PARTS_PK
	 * @param PC_PARTS_TITLE
	 * @param PC_PARTS_CODE
	 * @param PC_PARTS_MAKER
	 * @param PC_PARTS_AVAIL
	 * @param PC_PARTS_DESC
	 * 
	 * @return - HTTP Status Code: 200 if success, 500 if error
	 * @throws Exception
	 */
	public int insertIntoPc_Parts(String PC_PARTS_TITLE,
			String PC_PARTS_CODE,
			String PC_PARTS_MAKER,
			String PC_PARTS_AVAIL,
			String PC_PARTS_DESC) throws Exception {
		
		PreparedStatement query = null;
		Connection conn = null;
		
		try {
			
			/**
			 * In a real application, you should do data validation here
			 * before starting to insert data into the database.
			 */
			
			conn = Oracle308tube.OraclePcPartsConnection();
			
			query = conn.prepareStatement("insert into PC_PARTS " +
					"(PC_PARTS_TITLE, PC_PARTS_CODE, PC_PARTS_MAKER, PC_PARTS_AVAIL, PC_PARTS_DESC) " +
					"VALUES (?, ?, ?, ?, ?)");
			
			query.setString(1, PC_PARTS_TITLE);
			query.setString(2, PC_PARTS_CODE);
			query.setString(3, PC_PARTS_MAKER);
			
			// Convert into int
			int avail = Integer.parseInt(PC_PARTS_AVAIL);
			query.setInt(4, avail);
			
			query.setString(5, PC_PARTS_DESC);
			
			// We use a different command to update
			query.executeUpdate();
			
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			return 500;
		}
		finally {
			if (conn != null) conn.close();
		}
		
		return 200;
	}
	
	/**
	 * This method will search for a specific brand from the PC_PARTS table.
	 * By using prepareStatement and the ?, we are protecting against SQL injection
	 * 
	 * Never add parameter straight into the prepareStatement
	 * 
	 * @param brand - product brand
	 * @return - JSON array of the results from the database
	 * @throws Exception
	 */
	public JSONArray queryReturnBrandParts (String brand) throws Exception {
		
		PreparedStatement query = null;
		Connection conn = null;
		
		ToJSON converter = new ToJSON();
		JSONArray jsonArr = new JSONArray();
		
		try {
			
			// Create a connection to db
			conn = OraclePcPartsConnection();
			
			// Create the pre compiled SQL query.Obs. never use select * in real code
			query = conn.prepareStatement("select PC_PARTS_PK, PC_PARTS_TITLE, PC_PARTS_CODE, PC_PARTS_MAKER, PC_PARTS_AVAIL,PC_PARTS_DESC " +
					"from PC_PARTS " +
					"where UPPER(PC_PARTS_MAKER) = ? ");
			
			// Insert the sought parameter into the query in a secure way, protecting against SQL Injection
			query.setString(1, brand.toUpperCase());
			
			// Execute the SQL query and return a cursor
			ResultSet rs = query.executeQuery();
			
			// Convert the data into a JSONArray of JSON objects
			jsonArr = converter.toJSONArray(rs);
			
			// Very Important!
			conn.close();
		}
		catch (SQLException sqlErr) {
			sqlErr.printStackTrace();
			return jsonArr;
		}
		catch (Exception e) {
			e.printStackTrace();
			return jsonArr;
		}
		finally {
			if (conn != null) {
				conn.close();
			}
		}
		
		return jsonArr;
	}
	
	/**
	 * This method will search for the specific brand and the brands item number in
	 * the PC_PARTS table.
	 * 
	 * By using prepareStatement and the ?, we are protecting against SQL injection
	 * 
	 * Never add parameter straight into the prepareStatement
	 * 
	 * @param brand - product brand
	 * @param itemNumber - product item number
	 * @return - JSON array of the results from the database
	 * @throws Exception
	 */
	public JSONArray queryReturnBrandItemNumber (String brand, int itemNumber) throws Exception {
		
		PreparedStatement query = null;
		Connection conn = null;
		
		ToJSON converter = new ToJSON();
		JSONArray jsonArr = new JSONArray();
		
		try {
			
			// Create a connection to db
			conn = OraclePcPartsConnection();
			
			// Create the pre compiled SQL query.Obs. never use select * in real code
			query = conn.prepareStatement("select PC_PARTS_PK, PC_PARTS_TITLE, PC_PARTS_CODE, PC_PARTS_MAKER, PC_PARTS_AVAIL,PC_PARTS_DESC " +
					"from PC_PARTS " +
					"where UPPER(PC_PARTS_MAKER) = ? " +
					" and PC_PARTS_CODE = ?");
			
			/*
			 * protect against SQL injection
			 * when you have more than one ?, it will go in chronological
			 * order.
			 */
			query.setString(1, brand.toUpperCase());
			query.setInt(2, itemNumber);
			
			// Execute the SQL query and return a cursor
			ResultSet rs = query.executeQuery();
			
			// Convert the data into a JSONArray of JSON objects
			jsonArr = converter.toJSONArray(rs);
			
			// Very Important!
			conn.close();
		}
		catch (SQLException sqlErr) {
			sqlErr.printStackTrace();
			return jsonArr;
		}
		catch (Exception e) {
			e.printStackTrace();
			return jsonArr;
		}
		finally {
			if (conn != null) {
				conn.close();
			}
		}
		
		return jsonArr;
	}
	
	/**
	 * This method will return all PC parts.
	 * Done for benefit of V1, so that all SQL could fully be in dao package.
	 * 
	 * @return - all PC parts in JSON format
	 * @throws Exception
	 */
	public JSONArray queryAllParts () throws Exception {
		
		PreparedStatement query = null;
		Connection conn = null;
		
		ToJSON converter = new ToJSON();
		JSONArray jsonArr = new JSONArray();
		
		try {
			
			conn = Schema308tube.OraclePcPartsConnection();
			
			query = conn.prepareStatement("select PC_PARTS_PK, PC_PARTS_TITLE, PC_PARTS_CODE, PC_PARTS_MAKER, PC_PARTS_AVAIL,PC_PARTS_DESC " +
					"from PC_PARTS");
			
			ResultSet rs = query.executeQuery();
			
			jsonArr = converter.toJSONArray(rs);
			
			conn.close();
		}
		catch (SQLException sqlErr) {
			sqlErr.printStackTrace();
			return jsonArr;
		}
		catch (Exception e) {
			e.printStackTrace();
			return jsonArr;
		}
		finally {
			if(conn != null) {
				conn.close();
			}
		}
		
		return jsonArr;
	}
	
	/**
	 * This method will return a time/stamp from database.
	 * 
	 * @return - time/stamp in JSON format
	 * @throws Exception
	 */
	public JSONArray queryCheckDbConnection () throws Exception {

		PreparedStatement query = null;
		Connection conn = null;
		
		ToJSON converter = new ToJSON();
		JSONArray jsonArr = new JSONArray();
		
		try {
			
			conn = Schema308tube.OraclePcPartsConnection();
			
			query = conn.prepareStatement("select to_char(sysdate, 'YYYY-MM-DD HH24:MI:SS') DATETIME " + "from sys.dual");
			
			ResultSet rs = query.executeQuery();
			
			jsonArr = converter.toJSONArray(rs);
			
			conn.close();
		}
		catch (SQLException sqlErr) {
			sqlErr.printStackTrace();
			return jsonArr;
		}
		catch (Exception e) {
			e.printStackTrace();
			return jsonArr;
		}
		finally {
			if(conn != null) {
				conn.close();
			}
		}
		
		return jsonArr;
	}
}
