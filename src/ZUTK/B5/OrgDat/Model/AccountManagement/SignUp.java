package ZUTK.B5.OrgDat.Model.AccountManagement;

import ZUTK.B5.OrgDat.Controllers.Filters.DatabaseConnection;
import java.sql.*;

public class SignUp {
	DatabaseConnection dc;

	public boolean isMember(String email) {
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			String sql = "select user_id from signup_detail where user_email=?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, email);
			ResultSet rs = dc.stmt.executeQuery();
			if (rs == null) {
                return true;
			}
			long user_id = -1;
			while (rs.next()) {
				user_id = rs.getLong(1);
			}
			if (user_id == -1) {
                return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean addMemeber(String[] details){
	    String sql = "insert into sigup_detail (user_name,user_email,user_phone,user_passsword) values(?,?,?,?)";
	    try{
	        dc.stmt = dc.conn.prepareStatement(sql);
	        dc.stmt.setString(1,details[0]);
	        dc.stmt.setString(2,details[1]);
	        dc.stmt.setString(3,details[2]);
	        dc.stmt.setString(4,details[3]);
	        dc.stmt.executeUpdate();
	        return true;
	    }catch(Exception e){
	        return false;
	    }
	}
}