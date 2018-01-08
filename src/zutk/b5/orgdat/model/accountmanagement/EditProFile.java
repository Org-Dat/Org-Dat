package zutk.b5.orgdat.model.accountmanagement;
import java.sql.*;
import java.util.*;
import zutk.b5.orgdat.controllers.filters.*;
public class EditProFile {
	DatabaseConnection dc;

	public boolean updateDeets(String column_name, String value, long user_id) {
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			
			String sql = "update signup_detail set " + column_name
					+ "=? where user_id=?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, value);
			dc.stmt.setLong(2, user_id);
			dc.stmt.executeUpdate();
		    if (dc != null ){
		        dc.close();
		    }
			return true;
		} catch (Exception e) {
		    if (dc != null ){
		        dc.close();
		    }
			return false;
		}
	}
    public ArrayList<String> getDeets(long user_id) {
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			ArrayList<String> details = new ArrayList<String>();
			String sql = "select user_name,user_email,user_phone,country from signup_detail where user_id=?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()){
			    details.add(rs.getString(1));
			    details.add(rs.getString(2));
			    details.add(rs.getString(3));
			    details.add(rs.getString(4));
			}
		    if (dc != null ){
		        dc.close();
		    }
			return details;
		} catch (Exception e) {
		    e.printStackTrace();
		    if (dc != null ){
		        dc.close();
		    }
			return new ArrayList<String>();
		}
	}
// 	public long getUserId(Cookie[] cookies) {
// 		try {
// 			RoleChecker rc = new RoleChecker();
// 			return rc.getUserId(cookies);
// 		} catch (Exception e) {
// 			return -1l;
// 		}

// 	}
}