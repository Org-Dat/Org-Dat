package ZUTK.B5.OrgDat.Model.AccountManagement;

import ZUTK.B5.OrgDat.Controllers.Filters.*;
import java.sql.*;
import javax.servlet.http.Cookie;

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
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean updateSecurity(String current_password, String arg1,
			String arg2, long user_id) {
		try {
			String sql = "select user_password from signup_detail where user_id=? and ";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, current_password);
			ResultSet rs = dc.stmt.executeQuery();
			if (rs == null) {
				return false;
			}
			if (current_password.equals(rs.getString("user_password")) == false) {
				return false;
			}
			if (arg2.equals("signup_detail")) {
				sql = "update signup_detail set password=? where user_id=?";
			} else {
				sql = "update security_management set question=?,answer=? where user_id=?";
			}
			dc.stmt = dc.conn.prepareStatement(sql);
			if (arg2.equals("signup_detail") == false) {
				dc.stmt.setString(1, arg1);
				dc.stmt.setString(2, arg2);
				dc.stmt.setLong(3, user_id);
			} else {
				dc.stmt.setString(1, arg2);
				dc.stmt.setLong(2, user_id);
			}
			dc.stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public long getUserId(Cookie[] cookies) {
		try {
			RoleChecker rc = new RoleChecker();
			return rc.getUserId(cookies);
		} catch (Exception e) {
			return -1;
		}

	}
}