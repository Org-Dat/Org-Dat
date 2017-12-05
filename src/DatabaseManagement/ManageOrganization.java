/**
 * This Servlet is managing the Organization.
 * 
 * @author : Obeth samuel
 * 
 * @version : 1.0
 */
package DatabaseManagement;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

import Filters.*;

public class ManageOrganization extends HttpServlet {

	String deleteOrNot = "";
	DatabaseConnection dc;

	/**
	 * This method used to split organization management work
	 * 
	 * @params : HttpServletRequest request,HttpServletResponse response
	 * 
	 * @return type : void
	 * 
	 * @return : if user give correct detail it return success object.else
	 *         return error object.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();

		try {
			String requri = request.getRequestURI();
			if(requri.startsWith("/api")){
			    requri = requri.substring(requri.indexOf("/", 1));
			}
			String[] path = requri.split("/");
			String orgName = path[0];// request.getParameter("org_name");
			String reqURI = path[1];// request.getRequestURI();
			dc = new DatabaseConnection("postgres", "postgres", "");
			if (isCorrect(orgName) == true) {
				boolean isCorrect = false;
				if (reqURI.equals("createOrg")) {
					isCorrect = createOrg(orgName);
				} else if (reqURI.equals("renameOrg")) {

				} else if (reqURI.equals("deleteOrg")) {
					ArrayList<String> databases = getDatabases(orgName);
					isCorrect = deleteOrg(databases, orgName);
				}
				if (isCorrect == true) {

					out.write("{'status':200 ,'org_name':'" + orgName + "'}");
				} else {
					out.write("{'status':404,'message':'Not Found'}");
				}
			} else {
				out.write("{'status':400,'message':'Bad Request'}");
			}
		} catch (Exception ce) {
			out.write("{'status':400,'message':'Bad Request'}");
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
		boolean correct = orgName.matches("^[a-z0-9][a-z0-9]{2,25}$");
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

	private ArrayList<String> getDatabases(String org_name) {
		ArrayList<String> databaseNameList = new ArrayList<String>();
		String sqlQuery = "selete db_name from db_manament where org_name=?";
		try {
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

	private boolean createOrg(String name) throws ClassNotFoundException {

		try {
			String sqlQuery = "insert into org_mangament(org_name,org_roles) values(?,?)";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, name);
			dc.stmt.setString(2, "owner");
			dc.stmt.executeUpdate();
			if (manageRole("create role " + name + " with login") == true) {

			} else {

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

	private boolean deleteOrg(ArrayList<String> databaseNames, String org_name) {
		try {
			String sqlQuery = "delete from db_mangament where db_name=?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name);
			dc.stmt.executeUpdate();
			Statement stmt = dc.conn.createStatement();
			for (String database : databaseNames) {
				stmt.executeUpdate("drop database " + database);
			}
			if (manageRole("drop role " + org_name) == true) {

			} else {
				return false;
			}
			return true;
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