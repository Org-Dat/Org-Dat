package zutk.b5.orgdat.model.databasemanagement;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowDetails {
	DatabaseConnection dc =  new DatabaseConnection("postgres","postgres","");

	public ArrayList<String> getOrganization(long user_id) {
		ArrayList<String> organizations = new ArrayList<String>();
		String sqlQuery = "select org_name from org_management where user_id=?";
		dc =  new DatabaseConnection("postgres","postgres","");
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				organizations.add(rs.getString(1));
			}
			System.out.println(organizations.toString());
			dc.stmt.close();
			dc.conn.close();
			return organizations;
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
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
	public ArrayList<String> getDatabase(String org_name) {
		ArrayList<String> databases = new ArrayList<String>();
		String sqlQuery = "select db_name from db_management where org_name=?";
		try {
		    System.out.println("get db detals model org_name : "+org_name);
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				databases.add(((rs.getString(1)).split("_"))[1]);
			}
			return databases;
		} catch (SQLException e) {
		    System.out.println("get db detals model error : "+e.getMessage());
			return new ArrayList<String>();
		}
	}

	/**
	 * This method used to get table names .
	 * 
	 * @params : String db_name,String org_name ,long user_id1ijenkjwfenfewijnew
	 * 
	 * @return type : ArrayList<String>
	 * 
	 * @return : it return table names arraylist
	 */
	public ArrayList<String> getTables(String org_name, String db_name) {
		ArrayList<String> tables = new ArrayList<String>();
		String sqlQuery = "select table_name from table_management where org_name=? and db_name=?";
		try {
		    dc = new DatabaseConnection("postgres","postgres","");
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name);
			dc.stmt.setString(2, org_name+"_"+db_name);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
			    System.out.println(rs.getString(1));
				tables.add(rs.getString(1));
			}
			dc.conn.close();
			System.out.println();
			return tables;
		} catch (Exception e) {
		    System.out.println(e.getMessage());
			return new ArrayList<String>();
		}
	}
}