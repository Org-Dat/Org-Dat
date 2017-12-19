/**
 *
 */ 
package zutk.b5.orgdat.controllers.filters;

import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class RoleChecker extends HttpServlet {
	public DatabaseConnection dc;
    HttpServletRequest request;
	/**
      *
      */
    public RoleChecker(HttpServletRequest requ) {
      request = requ;
    }
    
    
    public RoleChecker() {
    }
	public String orgRole(String org_name, long user_id) {
		dc = new DatabaseConnection("postgres", "postgres", "");
		String role = null;
		
		try {
		    role = "";
		    long org_id = dc.getOrgId(org_name);
		    dc.close();
		    dc = new DatabaseConnection("postgres","postgres","");
		    String sqlQuery = "select role from org_management where user_id=? and org_id=?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setLong(2,org_id );
			ResultSet roles = dc.stmt.executeQuery();
			while (roles.next()) {
				role = roles.getString("role");
			}
			dc.close();
			return role;
		} catch (Exception e) {
		    e.printStackTrace();
		    if (dc != null ){
		        dc.close();
		    }
			return role;
		}
	}
	
    /**
      *
      */
	public String dbRole(String org_name, String db_name, long user_id) {
		dc = new DatabaseConnection("postgres", "postgres", "");
		String role = null;
		String sqlQuery = "select role from db_management where user_id=? and db_id=?";
		try {
		    long org_id = dc.getOrgId( org_name );
		    long db_id = dc.getDBId( org_id , db_name);
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setLong(2, db_id);
			ResultSet roles = dc.stmt.executeQuery();
			while (roles.next()) {
				role = roles.getString(1);
			}
		        dc.close();
			return role;
		} catch (Exception e) {
		    e.printStackTrace();
		    if (dc != null ){
		        dc.close();
		    }
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
		String sqlQuery = "select role from table_management where user_id=? and table_id=?)";
		try {
		    long org_id = dc.getOrgId( org_name );
		    long db_id = dc.getDBId( org_id , db_name);
		    long table_id = dc.getTableId( org_id ,db_id, table_name);
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setLong(2, table_id);
			ResultSet roles = dc.stmt.executeQuery();
			while (roles.next()) {
				role = roles.getString(1);
			}
		    dc.close();
			return role;
		} catch (Exception e) {
		    if (dc != null ){
		        dc.close();
		    }
			return role;
		}
	}
	
    /**
      *
      */
      
	public long getUserId(Cookie[] cookies) {
	    
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("iambdt")) {
			    System.out.println("iambdt" + cookie.getValue());
			    System.out.println(request);
			    long user_id = (long) ((ServletRequest) request).getAttribute(cookie.getValue());
			    System.out.println(user_id);
				return user_id;
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
			return user_id;
		} catch (Exception e) {
			return -1;
		}
	}
}
