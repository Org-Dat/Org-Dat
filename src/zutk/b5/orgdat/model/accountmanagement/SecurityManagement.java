package zutk.b5.orgdat.model.accountmanagement;

import zutk.b5.orgdat.controllers.filters.*;
import java.sql.*;
import java.util.*;

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
		        dc.close();
				return false;
			}
			while (rs.next()) {
				if (current_password.equals(rs.getString(1)) == false) {
		        dc.close();
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
		        dc.close();
			return true;
		} catch (Exception e) {
		    if (dc != null ){
		        dc.close();
		    }
			return false;
		}
	}
	
	public ArrayList<String> getUserDatail(long user_id){
	    try{
	        ArrayList<String> memberDetail = new  ArrayList<String>();
	        String sql = "select user_name,user_email,user_phone from signup_detail where user_id  = ?";
	        dc = new DatabaseConnection("postgres","postgres","");
	        dc.stmt = dc.conn.prepareStatement(sql);
	        dc.stmt.setLong(1, user_id);
	        ResultSet rs = dc.stmt.executeQuery();
	        while(rs.next()){
	            memberDetail.add(rs.getString("user_name"));
	            memberDetail.add(rs.getString("user_email"));
	            memberDetail.add(rs.getString("user_phone"));
	        }
	        dc.close();
	        return memberDetail;
	    }catch(Exception e){
	        
	        return new  ArrayList<String>();
	    }
	}
	

}