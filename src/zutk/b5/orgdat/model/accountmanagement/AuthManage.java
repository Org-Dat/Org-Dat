package zutk.b5.orgdat.model.accountmanagement;

import zutk.b5.orgdat.controllers.filters.*;
import java.sql.*;
import java.util.ArrayList;
import org.json.simple.*;
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
	public String createAuthtoken(long user_id , String ip_address) {
		try {
		    dc = new DatabaseConnection("postgres","postgres","");
			String authtoken = dc.createJunk(10);
// 			long user_id = dc.getUserId(mail, password);
// 			if(user_id == -1){
// 			    return null;
// 			}
			String sql = "insert into auth_management (user_id,auth_token,ip_address) values(?,?,?)";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, authtoken);
			dc.stmt.setString(3, ip_address);
			dc.stmt.executeUpdate();
		    dc.close();
			return authtoken;
		} catch (Exception e) {
		    if (dc != null ){
    		    dc.close();
		    }
			return createAuthtoken(user_id,ip_address);
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
	public boolean deleteAuthtoken(String[] authTokens,long user_id) {
		try {
		    dc = new DatabaseConnection("postgres","postgres","");
		    for(String authtoken : authTokens){
			String sql = "delete from auth_management where auth_token='"
					+ authtoken+"' and user_id = "+user_id;
			Statement dstmt = dc.conn.createStatement();
			dstmt.executeUpdate(sql);
		    }
			dc.close();
			return true;
		} catch (Exception e) {
		    System.out.println("NNJNJNJNJNJNJJNJSNJNJSNJS");
		    e.printStackTrace();
		    if (dc != null ){
    		    dc.close();
		    }
			return false;
		}
	}
	
	public String getAuthToken(long user_id){
	    try{
	        System.out.println("USER ID IN GET AUTH  = " + user_id);
	        dc = new DatabaseConnection("postgres","postgres","");
	        ArrayList<String> authtoken = new ArrayList<String>();
	        String sql = "select ip_address,auth_token,createtime from auth_management where user_id=? order by createtime asc";
	        dc.stmt = dc.conn.prepareStatement(sql);
	        dc.stmt.setLong(1, user_id);
	        ResultSet rs = dc.stmt.executeQuery();
	        while(rs.next()){
	            JSONObject tmpObj = new JSONObject();
	            tmpObj.put("ip_address", rs.getString(1));
	            tmpObj.put("auth_token", rs.getString(2));
	            tmpObj.put("createTime", rs.getString(3).substring(0,rs.getString(3).lastIndexOf(":")));
	            authtoken.add(tmpObj.toJSONString());
	        }
	        dc.close();
	        System.out.println("AUTH TOKEN S = " + authtoken.toString());
	        return authtoken.toString();
	    }catch(Exception e){
	            e.printStackTrace();
		    if (dc != null ){
    		    dc.close();
		    }
	        return null;
	    }
	}
	public void close(){
	    if (dc != null){
	        dc.close();
	    }
	}
}
