package zutk.b5.orgdat.model.accountmanagement;

import zutk.b5.orgdat.controllers.filters.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CookieManage {
	DatabaseConnection dc;
	SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"dd / MMM / yyyy kk:mm:ss Z ");

	/**
	 * This method used to change response cookie
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
			dc = new DatabaseConnection("postgres", "postgres", "");
			dc.stmt = dc.conn.prepareStatement(query);
			String cookie = dc.createJunk(20);
			dc.stmt.setString(1, cookie);
			dc.stmt.setString(2, iambdt);
			dc.stmt.executeUpdate();
			dc.close();
			return cookie;
		} catch (Exception e) {
			if (dc != null) {
				dc.close();
			}
			return changeCookie(iambdt);
		}
	}

	public String createCookie(long user_id, String ip_address,
			String user_agent) {
		String query = "insert into cookie_management (user_id,ip_address,user_agent,iambdt_cookie) values(?,?,?,?)";
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			dc.stmt = (dc.conn).prepareStatement(query);
			String cookie = dc.createJunk(20);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, ip_address);
			dc.stmt.setString(3, user_agent);
			dc.stmt.setString(4, cookie);
			dc.stmt.executeUpdate();
			Date now = new Date();
			dc.stmt = (dc.conn)
					.prepareStatement("insert into history (user_id,ip_address,user_agent,signin_time,signout_time) values(?,?,?,?,?)");
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, ip_address);
			dc.stmt.setString(3, user_agent);
			dc.stmt.setString(4, dateFormatter.format(now));
			dc.stmt.setString(5, "login");
			dc.stmt.executeUpdate();
			if (dc != null) {
				dc.close();
			}
			return cookie;
		} catch (Exception e) {
			if (dc != null) {
				dc.close();
			}
			return createCookie(user_id, ip_address, user_agent);
		}
	}

	public boolean deleteCookie(String iambdt, long user_id, String ip_address,
			String user_agent) {
		String sql = "delete from cookie_management where iambdt_cookie=?";
		try {
		    System.out.println("iambdt == " +iambdt +"USER_ID  +=== "+user_id +"  ; ip_address == " + ip_address+" =  user_agent" +user_agent);
			dc = new DatabaseConnection("postgres", "postgres", "");
			dc.stmt = (dc.conn).prepareStatement(sql);
			dc.stmt.setString(1, iambdt);
			dc.stmt.executeUpdate();
			System.out.println("ASDFGHJKLQWERTYUIOPZXCVBNM ==== cookie_management");
			Date now = new Date();
			dc.stmt = (dc.conn)
					.prepareStatement("update  history set signout_time=?  where user_id=? and ip_address = ? and user_agent = ? and signout_time='login' ");
			dc.stmt.setString(1, dateFormatter.format(now));
			dc.stmt.setLong(2, user_id);
			dc.stmt.setString(3, ip_address);
			dc.stmt.setString(4, user_agent);
			dc.stmt.executeUpdate();
			if (dc != null) {
				dc.close();
			}
				System.out.println("ASDFGHJKLQWERTYUIOPZXCVBNM ==== history ");
			return true;
		} catch (Exception e) {
		    e.printStackTrace();
			if (dc != null) {
				dc.close();
			}
			return false;
		}
	}
}