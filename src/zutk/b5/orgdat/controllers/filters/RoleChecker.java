/**
 *
 */ 

package zutk.b5.orgdat.controllers.filters;

import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class RoleChecker extends HttpServlet {
	DatabaseConnection dc;
    HttpServletRequest request;
	/**
      *
      */
      public RoleChecker(HttpServletRequest requ) {
          request = requ;
      }
	public String orgRole(String org_name, long user_id) {
		dc = new DatabaseConnection("postgres", "postgres", "");
		String role = null;
		String sqlQuery = "select role from org_management where user_id=? and org_name=?";
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, org_name);
			ResultSet roles = dc.stmt.executeQuery();
			while (roles.next()) {
				role = roles.getString(1);
			}
			return role;
		} catch (SQLException e) {
			return role;
		}
	}
	
    /**
      *
      */
	public String dbRole(String org_name, String db_name, long user_id) {
		dc = new DatabaseConnection("postgres", "postgres", "");
		String role = null;
		String sqlQuery = "select role from db_management where user_id=? and org_name=? and db_name=?";
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, org_name);
			dc.stmt.setString(3, db_name);
			ResultSet roles = dc.stmt.executeQuery();
			while (roles.next()) {
				role = roles.getString(1);
			}
			return role;
		} catch (SQLException e) {
			return role;
		}
	}
	
    /**
      *
      */
      
	public String tableRole(String org_name, String db_name, String table_name,
			long user_id) {
		dc = new DatabaseConnection("postgres", "postgres", "");
		String role = null;
		String sqlQuery = "select role from table_management where user_id=? and org_name=? and db_name=? and table_name=?";
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, org_name);
			dc.stmt.setString(3, db_name);
			dc.stmt.setString(4, table_name);
			ResultSet roles = dc.stmt.executeQuery();
			while (roles.next()) {
				role = roles.getString(1);
			}
			return role;
		} catch (SQLException e) {
			return role;
		}
	}
	
    /**
      *
      */
      
	public long getUserId(Cookie[] cookies) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("iambdt")) {
				return ((long) ((ServletRequest) request).getAttribute(cookie.getValue()));
			}
		}
		return -1;
	}
	
    /**
      *
      */
      
	public long getUserId(String authtoken) {
		try {
			long user_id = -1;
			user_id = ((long) ((ServletRequest) request).getAttribute(authtoken));
			System.out.println("role checher user_id : "+user_id);
			return user_id;
		} catch (Exception e) {
			return -1;
		}
	}
}
