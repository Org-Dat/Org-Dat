package ZUTK.B5.OrgDat.Model.AccountManagement;

import ZUTK.B5.OrgDat.Controllers.Filters.*;
import java.sql.Statement;

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
			String authtoken = "#" + dc.createJunk(10);
			long user_id = dc.getUserId(mail, password);
			String sql = "insert into auth_management (user_id,auth_token) values(?,?)";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, authtoken);
			return authtoken;
		} catch (Exception e) {
			return "";
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
			String sql = "delete from auth_management where auth_token="
					+ authtoken;
			Statement dstmt = dc.conn.createStatement();
			dstmt.executeUpdate(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}