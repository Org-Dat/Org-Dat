package zutk.b5.orgdat.model.accountmanagement;

import zutk.b5.orgdat.controllers.filters.DatabaseConnection;
import java.sql.*;

public class SignUp {
	DatabaseConnection dc;

	public boolean isMember(String email) {
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			String sql = "select user_id from signup_detail where user_email=?";
	System.out.println("dc.conn  =  "+dc.conn);
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, email);
			ResultSet rs = dc.stmt.executeQuery();
			if (rs == null) {
				dc.close();
				return true;
			}
			long user_id = -1;
			while (rs.next()) {
				user_id = rs.getLong(1);
			}
			if (user_id == -1) {
				dc.close();
				return true;
			}
			dc.close();
			return false;
		} catch (Exception e) {
			if (dc != null) {
				dc.close();
			}
			return false;
		}
	}

	public boolean addMemeber(String[] details) {
		dc = new DatabaseConnection("postgres", "postgres", "");
		String sql = "insert into signup_detail (user_name,user_email,user_phone,user_password,role) values(?,?,?,?,?)";
		try {
			dc.stmt = dc.conn.prepareStatement(sql);
			for (int i = 0; i < 5; i++) {
				dc.stmt.setString(i + 1, details[i]);
			}
			dc.stmt.executeUpdate();
			dc.close();
			return true;
		} catch (Exception e) {
			if (dc != null) {
				dc.close();
			}
			System.out.println(e.getMessage());
			return false;
		}
	}

	public boolean setSecurityQuestion(long user_id, String answer) {
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			String question = "What is Your Orgdat email-id name ?";
			String sql = "insert into security_management (user_id,question,answer) values(?,?,?)";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, question);
			dc.stmt.setString(3, answer);
			dc.stmt.executeUpdate();
			dc.close();
			return true;
		} catch (Exception e) {
			if (dc != null) {
				dc.close();
			}
			return false;
		}
	}
}
