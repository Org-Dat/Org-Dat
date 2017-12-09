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

	public long getUserId(Cookie[] cookies) {
		try {
			RoleChecker rc = new RoleChecker();
			return rc.getUserId(cookies);
		} catch (Exception e) {
			return -1l;
		}

	}
}