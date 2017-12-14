package zutk.b5.orgdat.model.databasemanagement;

import java.util.ArrayList;
import java.sql.*;
import java.io.File; 
public class ManageOrg {
	DatabaseConnection dc;

	public String ManageOrg(long user_id ,String orgName,String reqURI) {
		try {
// 			if (requri.startsWith("/api/")) {
// 				requri = requri.substring(5);
// 			}
			dc = new DatabaseConnection("postgres", "postgres", "");
			if (isCorrect(orgName) == true) {
				boolean isCorrect = false;
				if (reqURI.equals("createOrg")) {
					isCorrect = createOrg(user_id,orgName);
				} else if (reqURI.equals("deleteOrg")) {
					ArrayList<String> databases = getDatabases(orgName);
					isCorrect = deleteOrg(databases, orgName);
					System.out.println("delete org is "+isCorrect);
				}
				if (isCorrect == true) {

					return "{  'status' : 200,  'message' : 'Organizatin create successfully',  'Organization Name' : '"+ orgName +" '}";
				} else {
					return "{'status': 403 , 'message' : 'Please Create Owner Account'}";
				}
			} else { 
				return "{'status':404,'message':'Your Organization Name is incorrect'}";
			}
		} catch (Exception ce) {
		    System.out.println("Show Deets : " +ce.getMessage());
			return "{'status' : 406 ,'message' : 'Organization already exist'}";
		} finally {
			try {
				dc.conn.close();
			} catch (SQLException e) {
				System.out.println("Connection Not Close");
			}
		}
	}

	/**
	 * This private method used to validate organization name
	 * 
	 * @params : String orgName
	 * 
	 * @return type : boolean
	 * 
	 * @return : if name match to regex it return true.else it return false
	 */
	private boolean isCorrect(String orgName) {
		boolean correct = orgName.matches("^[a-z0-9][a-z0-9]{3,30}");
		return correct;
	}

	/**
	 * This private method used to get one organization databases
	 * 
	 * @params : String org_name
	 * 
	 * @return type : ArratList<String>
	 * 
	 * @return : it return org databases name arraylist.
	 */

	public ArrayList<String> getDatabases(String org_name) {
		ArrayList<String> databaseNameList = new ArrayList<String>();
		String sqlQuery = "selete db_name from db_management where org_name=?";
		try {// db_management org_management
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name);
			ResultSet db_names = dc.stmt.executeQuery();
			while (db_names.next()) {
				databaseNameList.add(db_names.getString(1));
			}
		} catch (SQLException e) {
			return new ArrayList<String>();
		}
		return databaseNameList;
	}

	/**
	 * This private method used to create new organization.
	 * 
	 * @params : String name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if organization successfully create it return true.else false.
	 */

	private boolean createOrg(long user_id ,String name) throws ClassNotFoundException {

		try {
			String sqlQuery = "insert into org_management values(?,?,?)";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, name);
			dc.stmt.setString(3, "owner");
			dc.stmt.executeUpdate(); 
			File f = new File("/home/workspace/OrgDat/webapps/Log/"+name);
			if (f.mkdirs() == false || manageRole("create role " + name + " with login createdb") == false) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * This private method used to delete organization.
	 * 
	 * @params : String org_name,ArrayList<String> databaseNames
	 * 
	 * @return type : boolean
	 * 
	 * @return : if organization successfully delete it return true.else false.
	 */

	public boolean deleteOrg(ArrayList<String> databaseNames, String org_name) {
		try {
			String sqlQuery = "delete from org_management where org_name=?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name);
			dc.stmt.executeUpdate();
			Statement stmt = dc.conn.createStatement();
			for (String database : databaseNames) {
				stmt.executeUpdate("drop database " + database);
			}
			Runtime.getRuntime().exec("rm -rf /home/workspace/OrgDat/webapps/Log/"+org_name);
			boolean  bool = manageRole("drop role " + org_name) ;
			System.out.println("some "+bool);
			return bool;
			
		} catch (Exception e) {
			return false;
		}
	}

	/**
	  *
	  */

	private boolean manageRole(String sqlQuery) {
		try {
			Statement dstmt = dc.conn.createStatement();
			dstmt.executeUpdate(sqlQuery);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
