/**
 * This servlet is managing the Auth token
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */
package AccountManagement;

import java.util.*;
import java.io.*;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.sql.*;
import Filters.DatabaseConnection;

public class AuthManagement extends HttpServlet {
	DatabaseConnection dc;
	PrintWriter writer;

	/**
	 * This Method used to split authtoken managing work to helper Method
	 * 
	 * @params : HttpServletRequest request,HttpServletResponse response
	 * 
	 * @return type : void
	 * 
	 * @return : if user give correct detail it return success object.else
	 *           return error object.
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			writer = response.getWriter();
			dc = new DatabaseConnection("postgres", "postgres", "");
			HttpSession session = request.getSession();
			String requri = (String) session.getAttribute("reqURI");
			String[] path = requri.split("/");
			String reqURI = path[0];
			if (reqURI.equals("createAuth") == true) {
			    String mail = request.getParameter("email");
			    String password = request.getParameter("password");
				String authtoken = createAuthtoken(mail,password);
				if (authtoken.equals("") == true) {
					throw new Exception();
				}

			} else if (reqURI.equals("deleteAuth") == true) {
				String authtoken = request.getParameter("authtoken");
				if (deleteAuthtoken(authtoken) == true) {

				} else {
					throw new Exception();
				}
			} else {

			}
		} catch (Exception e) {

		} finally {
			try {
				dc.conn.close();
			} catch (Exception e) {

			}
		}
	}
    /**
      * This private method used to create Authtoken 
      * 
      * @params : long used_id ,String role
      * 
      * @return type : String 
      * 
      * @ if user give correct dettail this method return his/her authtoken.else return empty String
      */
	private String createAuthtoken(String mail,String password) {
		try {
			String authtoken = "#" + dc.createJunk(10);
			long user_id = dc.getUserId(mail, password);
			String sql = "insert into auth_management (user_id,auth_token) values(?,?)";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, authtoken);
			return authtoken;
		} catch (Exception e) {
			return "";
		}
	}
    /**
      * This private  method used to delete authtoken.
      * 
      * @params : String authtoken
      * 
      * @return type : boolean
      * 
      * @return :if  Authtoken delete successfully it return true else return false.
      * 
      */
	private boolean deleteAuthtoken(String authtoken) {
		try {
			String sql = "delete from auth_management where auth_token="
					+ authtoken;
			Statement dstmt = dc.conn.createStatement();
			dstmt.executeUpdate(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}