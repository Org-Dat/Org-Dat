package zutk.b5.orgdat.model.accountmanagement;

import zutk.b5.orgdat.controllers.filters.*;
import java.sql.*;
import java.util.ArrayList;

public class AuthManage {
	DatabaseConnection dc;

	/**
	 * This private method used to create Authtoken
	 * 
	 * @params : long used_id ,String role
	 * 
	 * @return type : String
	 * 
	 *         @ if user give correct dettail this method return his/her
	 *         authtoken.else return empty String
	 */
	public String createAuthtoken(String mail, String password) {
		try {
		    dc = new DatabaseConnection("postgres","postgres","");
			String authtoken = dc.createJunk(10);
			long user_id = dc.getUserId(mail, password);
			if(user_id == -1){
			    return null;
			}
			String sql = "insert into auth_management (user_id,auth_token) values(?,?)";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, authtoken);
			dc.stmt.executeUpdate();
			return authtoken;
		} catch (Exception e) {
			return createAuthtoken(mail,password);
		}
	}

	/**
	 * This  method used to delete authtoken.
	 * 
	 * @params : String authtoken
	 * 
	 * @return type : boolean
	 * 
	 * @return :if Authtoken delete successfully it return true else return
	 *         false.
	 * 
	 */
	public boolean deleteAuthtoken(String authtoken) {
		try {
		    dc = new DatabaseConnection("postgres","postgres","");
			String sql = "delete from auth_management where auth_token="
					+ authtoken;
			Statement dstmt = dc.conn.createStatement();
			dstmt.executeUpdate(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public String getAuthToken(long user_id){
	    try{
	        dc = new DatabaseConnection("postgres","postgres","");
	        ArrayList<String> authtoken = new ArrayList<String>();
	        String sql = "select auth_token from auth_management where user_id=?";
	        dc.stmt = dc.conn.prepareStatement(sql);
	        dc.stmt.setLong(1, user_id);
	        ResultSet rs = dc.stmt.executeQuery();
	        while(rs.next()){
	            authtoken.add(rs.getString(1));
	        }
	        return authtoken.toString();
	    }catch(Exception e){
	        return null;
	    }
	}
}