package ZUTK.B5.OrgDat.Model.DatabaseManagement;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowDetails {
	DatabaseConnection dc =  new DatabaseConnection("postgres","postgres","");

	public ArrayList<String> getOrganization(long user_id) {
		ArrayList<String> organizations = new ArrayList<String>();
		String sqlQuery = "select org_name from where user_id=?";
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
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
	public ArrayList<String> getDatabase(String org_name) {
		ArrayList<String> databases = new ArrayList<String>();
		String sqlQuery = "select db_name from db_manament org_name=?";
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				databases.add(((rs.getString(1)).split("_"))[1]);
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
	public ArrayList<String> getTables(String org_name, String db_name) {
		ArrayList<String> tables = new ArrayList<String>();
		String sqlQuery = "select table_name from table_management where org_name=? and db_name=?";
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name);
			dc.stmt.setString(2, org_name+"_"+db_name);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				tables.add(rs.getString(1));
			}
			return tables;
		} catch (SQLException e) {
			return new ArrayList<String>();
		}
	}
}