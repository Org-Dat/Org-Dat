/**
  * This Servlet is managing the show name (Organization , Database , Table).
  * 
  * @author : Obeth Samuel
  * 
  * @version : 1.0
  */

package DatabaseManagement;

import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import Filters.*;
import java.io.*;

public class ShowDetail extends HttpServlet{
	protected DatabaseConnection dc;
	/**
	  * This method used to split work to helper method
	  * 
	  * @params : HttpServletRequest request,HttpServletResponse response
	  * 
	  * @return type : void 
	  * 
	  * @return : if user give correct detail it return success object.else return error object.
	  */
	
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException {
		dc = new DatabaseConnection("postgres","postgres","");
	}
	
	/**
	  * This method used to get organization names .
	  * 
	  * @params : long user_id
	  * 
	  * @return type : ArrayList<String> 
	  * 
	  * @return : it return organization names arraylist
	  */
	  
	public ArrayList<String> getOrganization(long user_id){
		ArrayList<String> organizations = new ArrayList<String>();
		String sqlQuery = "select org_name from where user_id=?";
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while(rs.next()) {
				organizations.add(rs.getString(1));
			}
			return organizations;
		} catch (SQLException e) {
			return new ArrayList<String>();
		}
		
	}

	/**
	  * This method used to get database names .
	  * 
	  * @params : String org_name ,long user_id
	  * 
	  * @return type : ArrayList<String> 
	  * 
	  * @return : it return database names arraylist
	  */	
	public ArrayList<String> getDatabase(String org_name,long user_id){
		ArrayList<String> databases = new ArrayList<String>();
		String sqlQuery = "select db_name from db_manament org_name=? and user_id=?";
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name);
			dc.stmt.setLong(2, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while(rs.next()) {
				databases.add(rs.getString(1));
			}
			return databases;
		} catch (SQLException e) {
			return new ArrayList<String>();
		}
	}
	
	/**
	  * This method used to get table names .
	  * 
	  * @params : String db_name,String org_name ,long user_id
	  * 
	  * @return type : ArrayList<String> 
	  * 
	  * @return : it return table names arraylist
	  */		
	public ArrayList<String> getTables(String org_name,String db_name,long user_id){
		ArrayList<String> tables = new ArrayList<String>();
		String sqlQuery = "select table_name from table_management where org_name=? and db_name=? and user_id=?";
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1,org_name);
			dc.stmt.setString(2,db_name);
			dc.stmt.setLong(3, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while(rs.next()) {
				tables.add(rs.getString(1));
			}
			return tables;
		} catch (SQLException e) {
			return new ArrayList<String>();
		}
	}
	
}
