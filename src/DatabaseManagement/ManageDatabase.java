/**
 * This Servlet is managing the Database
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */

package DatabaseManagement;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.*;
import java.sql.*;
import Filters.*;

public class ManageDatabase extends HttpServlet {

	DatabaseConnection dc;
	private String deleteOrNot;

	/**
	 * This Method Used to split database management work.
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
		String requri = request.getRequestURI();
		if (requri.startsWith("/api")) {
			requri = requri.substring(requri.indexOf("/", 1));
		}
		String[] path = requri.split("/");
		String db_name = path[0];// request.getParameter("db_name");
		dc = new DatabaseConnection("postgres", "postgres", "");
		if (isCorrect(db_name) == true) {
			try {
				String reqURI = path[2];// request.getRequestURI();
				String org_name = path[1];// request.getParameter("org_name");
				boolean isCorrect = false;
				if (reqURI.equals("createDB")) {
					isCorrect = createDB(db_name, org_name);
				} else if (reqURI.equals("renameDB")) {
					String rename = request.getParameter("db_rename");
					if (isCorrect(rename) == true) {
						isCorrect = renameDB(db_name, org_name, rename);
					}

				} else if (reqURI.equals("deleteDB")) {
					isCorrect = deleteDB(db_name, org_name);
				}
				if (isCorrect == true) {
					out.write("{'status':200,'org_name':'" + org_name
							+ "','db_name':'" + db_name + "'}");
				} else {
					out.write("{'status':404,'message':'Not Found'}");
				}

			} catch (Exception e) {
				out.write("{'status':400,'message':'Bad Reuest'}");
			} finally {
				try {
					dc.conn.close();
				} catch (SQLException e) {
					System.out.println("Connection Not Close");
				}
			}

		}
	}

	/**
	 * This private method used to validate database name
	 * 
	 * @params : String db_name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if name match to regex it retur true. else return false
	 */

	private boolean isCorrect(String db_name) {
		boolean correct = db_name.matches("^[a-z][a-z0-9]{2,29}$");
		return correct;
	}

	/**
	 * This private method used to create database.
	 * 
	 * @params : String db_name,String org_name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if database create successfully ,it return true.else return
	 *         false
	 */

	private boolean createDB(String dbName, String org_name) {
		try {
			String sqlQuery = "";
			sqlQuery = "insert into db_mangament(db_name,db_roles,org_name) values(?,?,?)";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, dbName);
			dc.stmt.setString(2, "owner");
			dc.stmt.setString(3, org_name);
			dc.stmt.executeUpdate();
			sqlQuery = "create database " + dbName + "_" + org_name;
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.executeUpdate();
			dc.conn.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * This private method used to rename database.
	 * 
	 * @params : String db_name,String org_name,String newDb_name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if database rename successfully ,it return true.else return
	 *         false
	 */
	private boolean renameDB(String dbName, String org_name, String newDb_name) {

		try {
			String sqlQuery = "";
			sqlQuery = "update db_manament set db_name=? where db_name=? and org_name=?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, newDb_name);
			dc.stmt.setString(2, dbName);
			dc.stmt.setString(3, org_name);
			dc.stmt.executeUpdate();
			sqlQuery = "alter database " + dbName + " rename to " + newDb_name;
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.executeUpdate();
			dc.conn.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * This private method used to delete database.
	 * 
	 * @params : String db_name,String org_name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if database delete successfully ,it return true.else return
	 *         false
	 */

	private boolean deleteDB(String dbName, String org_name) {

		try {
			String sqlQuery = "";
			sqlQuery = "delete from db_mangament where db_name=? and org_name =?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, dbName);
			dc.stmt.setString(2, org_name);
			dc.stmt.executeUpdate();
			sqlQuery = "drop database " + dbName + "_" + org_name;
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.executeUpdate();
			dc.conn.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}