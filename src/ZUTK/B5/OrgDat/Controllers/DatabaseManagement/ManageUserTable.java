package ZUTK.B5.OrgDat.Controllers.DatabaseManagement;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import com.google.gson.*;
import ZUTK.B5.OrgDat.Model.DatabaseManagement.*;

public class ManageUserTable extends HttpServlet {
	DatabaseConnection dc = null;
	PreparedStatement stmt = null;
	ManageTable templateTable;
	long user_id;
	/**
	 * This Method used to split table manage work to other method.
	 * 
	 * @Params : request HttpServletRequest , response HttpServletResponse
	 * 
	 * @Return : this method return the work response.
	 */
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		try {
			String requri = request.getRequestURI();
			if (requri.startsWith("/api")) {
				requri = requri.substring(requri.indexOf("/", 1));
			}
			String[] path = requri.split("/");
			String org_name = path[0];// request.getParameter("org_name");
			String db_name = path[1];// request.getParameter("db_name");
			String table_name = path[2];// request.getParameter("table_name");
			if (isCorrect(table_name) == false || isCorrect(org_name) == false|| isCorrect(db_name) == false) {
				throw new Exception();
			}
			dc = new DatabaseConnection(org_name+"_"+db_name, org_name, "");
			templateTable = new ManageTable(org_name+"_"+db_name, org_name, ""); 
			user_id = getUserId(request.getCookies()); 
			String reqURI = path[3];
			/***************************** Create Table ********************************************/
			if (reqURI.equals("$createTable") == true) {
				String columns = request.getParameter("columnArray");
				if (templateTable.createTable(org_name, db_name, table_name, columns,user_id) == true) {
					writer.write("{'status':200,'org_name':'" + org_name+ "','db_name':'" + db_name + "','table_name':'"+ table_name + "'}");
				} else {
					throw new Exception();
				}
			}
			/***************************** Rename Table ********************************************/
			else if (reqURI.equals("$renameTable") == true) {
				String newTable = request.getParameter("newtable_name");
				if (isCorrect(newTable) == false) {
					throw new Exception();
				}
				if (templateTable.renameTable(org_name, db_name, table_name, newTable) == true) {
					writer.write("{'status':200,'org_name':'" + org_name+ "','db_name':'" + db_name + "','table_name':'"+ table_name + "'}");
				} else {
					throw new Exception();
				}
			}
			/***************************** Delete Table ********************************************/
			else if (reqURI.equals("$deleteTable") == true) {
				if (templateTable.deleteTable(org_name, db_name, table_name) == true) {
					writer.write("{'status':200,'org_name':'" + org_name+ "','db_name':'" + db_name + "','table_name':'"+ table_name + "'}");
				} else {
					throw new Exception();
				}
			}
			/***************************** Alter Table ********************************************/
			else {
				String columnNameArray = request.getParameter("query");
				JsonArray columnArray =new JsonParser().parse(columnNameArray).getAsJsonArray();
				if (templateTable.alterColumn( table_name,columnArray, reqURI.substring(1)) == true) {
                    dc.conn.close();
                    writer.write("{'status':200,'org_name':'" + org_name+ "','db_name':'" + db_name + "','table_name':'"+ table_name + "'}");
                } else {
                    throw new Exception();
                }
            }
		} catch (Exception e) {
			writer.write("{'status':400,'message':'Bad Reuest'}"); 
		} finally {
			try {
				dc.conn.close();
			} catch (SQLException e) {
				System.out.println("Connection Not Close");
			}
		}
	}
    

	/**
	 * This private method user to find user id
	 * 
	 * @Params : Cookie[] cookie
	 * 
	 * @Return : if vaild user return his/her user id ,else return -1
	 */

	private long getUserId(Cookie[] cookies) {
		ServletContext context = getServletContext();
		for (Cookie cookie : cookies) {
			if ((cookie.getName()).equals("iambdt")) {
				return (long) context.getAttribute(cookie.getValue());
			}
		}
		return -1;
	}

	/**
	 * This private method used to find table name is correct or wrong
	 * 
	 * @Params : String table_name
	 * 
	 * @Return : if name match to regex return true,else return false
	 */

	private boolean isCorrect(String table_name) {
		return table_name.matches("^[a-z][a-z0-9_]{4,30}$");
	}

	public boolean checkCount(String org_name,String db_name) {
		try {
			String sql = "select count(table_name) from table_management where org_name=? and db_name=?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, org_name);
			dc.stmt.setString(2, db_name);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				return rs.getLong(1) < 25;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}