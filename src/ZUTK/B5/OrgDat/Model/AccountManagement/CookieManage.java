package ZUTK.B5.OrgDat.Model.AccountManagement;

import ZUTK.B5.OrgDat.Model.DatabaseManagement.*;
import java.sql.*;

public class CookieManage{ 
    DatabaseConnection dc; 
      	/**
	  * This  method used to change response  cookie
	  * 
	  * @params : String iambdt
	  * 
	  * @return type : void
	  * 
	  * @return : this method doesn't return any thing
	  */

	public String changeCookie(String iambdt) {
		String query = "update cookie_management set iambdt_cookie=? where iambdt_cookie=?";
		try {
		    dc = new DatabaseConnection("postgres","postgres","");
			dc.stmt = (dc.conn).prepareStatement(query);
			String cookie =  dc.createJunk(20);
			dc.stmt.setString(1,cookie);
			dc.stmt.setString(2, iambdt);
			dc.stmt.executeUpdate();
			return cookie;
		} catch (SQLException e) {
		    return changeCookie(iambdt);
		}
	}
	
	public String createCookie(long user_id,String ip_address,String user_agent){
	    String query = "insert into cookie_management (user_id,ip_address,user_agent,iambdt_cookie) values(?,?,?,?)";
	    try{
	        dc = new DatabaseConnection("postgres","postgres","");
			dc.stmt = (dc.conn).prepareStatement(query);
	        String cookie =  dc.createJunk(20);
	        dc.stmt.setLong(1, user_id);
	        dc.stmt.setString(2, ip_address);
	        dc.stmt.setString(3, user_agent);
	        dc.stmt.setString(4, cookie);
	        dc.stmt.executeUpdate();
	        return cookie;
	    }catch(Exception e){
	        return createCookie(user_id,ip_address,user_agent);
	    }
	}
	
	public boolean deleteCookie(String iambdt){
	    String sql = "delete from cookie_management where iambdt_cookie=?";
	    try{
	        dc = new DatabaseConnection("postgres","postgres","");
			dc.stmt = (dc.conn).prepareStatement(sql);
			dc.stmt.setString(1, iambdt);
			dc.stmt.executeUpdate();
	        return true;
	    }catch(Exception e){
	        return false;
	    }
	}
}