package ZUTK.B5.OrgDat.Model.AccountManagement;

import ZUTK.B5.OrgDat.Model.DatabaseManagement.DatabaseConnection;
import java.sql.*;

public class SecurityManagement{
       DatabaseConnection dc;
    	public boolean updateSecurity(String current_password, String arg1,
			String arg2, long user_id) {
		try {
			String sql = "select user_password from signup_detail where user_id=?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			if (rs == null) {
				return false;
			}
			while (rs.next()) {
				if (current_password.equals(rs.getString(1)) == false) {
					return false;
				}
			}
			if (arg1 == null) {
				sql = "update signup_detail set password=? where user_id=?";
			} else {
				sql = "update security_management set question=?,answer=? where user_id=?";
			}
			dc.stmt = dc.conn.prepareStatement(sql);
			if (arg1 == null) {
				dc.stmt.setString(1, arg2);
				dc.stmt.setLong(2, user_id);
			} else {
				dc.stmt.setString(1, arg1);
				dc.stmt.setString(2, arg2);
				dc.stmt.setLong(3, user_id);
			}
			dc.stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}