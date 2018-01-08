package zutk.b5.orgdat.model.databasemanagement;
import org.json.simple.*;
import java.sql.*;
import zutk.b5.orgdat.controllers.filters.DatabaseConnection;

public class ManageDB {
	DatabaseConnection dc;

	/**
	 * This private method used to validate database name
	 * 
	 * @params : String db_name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if name match to regex it retur true. else return false
	 */

	public boolean isCorrect(String db_name) {
		return db_name.matches("^[a-z][a-z0-9]{3,30}$");
	}

	/**
	 * This private method used to create database.
	 * 
	 * @params : String db_name,String org_name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if database create successfully ,it return true.else return
	 *         false
	 */

	public String createDB(long user_id,String dbName, String org_name) {
		try {
		    ResultSet rs  = null;
		    System.out.println(user_id+"  ==== User id");
		    dc = new DatabaseConnection("postgres", "postgres", "");
		    long org_id =dc.getOrgId(org_name);
		    if(org_id == -1){
		        throw new Exception("Invalid Organization Name");
		    }
			String sqlQuery  = "insert into db_details (org_id , db_name) values(?,?)";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, org_id);
			dc.stmt.setString(2, org_name+"_"+dbName);
			dc.stmt.executeUpdate();
			dc.close();
			dc = new DatabaseConnection("postgres","postgres","");
			long db_id = dc.getDBId(org_id, org_name+"_"+dbName);
			System.out.println("ORG ID  = " +org_id + " ; DATABASE NAME = " +dbName + "DB ID = "+ db_id);
			if(db_id == -1){
			    throw new Exception("Invalid Database ");
			}
			String sql = "insert into db_management (user_id  ,db_id ,role) values(?,?,?)";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			//dc.stmt.setLong(2, org_id);
			dc.stmt.setLong(2, db_id);
			dc.stmt.setString(3, "co-owner");
			dc.stmt.executeUpdate();
			dc.close();
			System.out.println("Org Connection");
		
			dc = new DatabaseConnection("postgres", org_name, "");
			sql = "create database " + org_name + "_" + dbName;
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.executeUpdate();
			dc.close();
			System.out.println("Org Connection close");
			return "200";
		} catch (Exception e) {
		    if (dc != null ){
    		    dc.close();
		    }
		    System.out.println(e);
			return "{'status' : 400,'message' : 'Your Database already exist'}";
		}
	}

	/**
	 * This private method used to rename database.
	 * 
	 * @params : String db_name,String org_name,String newDb_name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if database rename successfully ,it return true.else return
	 *         false
	 */
	public String renameDB(String dbName, String org_name, String newDb_name) {

		try {
		    dc = new DatabaseConnection("postgres", "postgres", "");
			String sqlQuery = "update db_details set db_name=? where db_name=? and org_id = (select org_id from org_details where org_name = ?)";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name+"_"+newDb_name);
			dc.stmt.setString(2, org_name + "_" + dbName);
			dc.stmt.setString(3, org_name);
			dc.stmt.executeUpdate();
			dc.close();
			dc = new DatabaseConnection("postgres", org_name, "");
			sqlQuery = "alter database " + org_name + "_" + dbName + " rename to " + org_name+"_"+newDb_name;
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.executeUpdate();
			dc.close();
			return "200";
		} catch (Exception e) {
		    if (dc != null ){
    		    dc.close();
		    }
			return " { 'status' :400, 'message' : 'Bad Request'}";
		}
	}
 
	/**
	 * This private method used to delete database.
	 * 
	 * @params : String db_name,String org_name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if database delete successfully ,it return true.else return
	 *         false
	 */

	public String deleteDB(String dbName, String org_name) {

		try {
		    dc = new DatabaseConnection("postgres", "postgres", "");
			String sqlQuery = "delete from db_details where db_name=? and  org_id = (select org_id from org_details where org_name = ?)";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1,org_name+"_" +dbName);
			dc.stmt.setString(2, org_name);
			dc.stmt.executeUpdate();
			dc.close();
			dc = new DatabaseConnection("postgres", org_name, "");
			sqlQuery = "drop database " + org_name + "_" + dbName;
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.executeUpdate();
			dc.close();
			return "200";
		} catch (Exception e) {
		    if (dc != null ){
    		    dc.close();
		    }
		    System.out.println(e);
			return "{'status' : 400 , 'message'  : 'Bad Request' }";
		}
	}
	
	public String getSharedMembers(long db_id) {
	    try {
	        JSONArray resp = new JSONArray();
	        dc = new DatabaseConnection("postgres","postgres","");
	        
	       JSONObject arg0 = new JSONObject();
	        dc.stmt = dc.conn.prepareStatement(" select signup_detail.user_name,signup_detail.user_email,db_management.role from signup_detail inner join db_management on signup_detail.user_id = db_management.user_id and db_management.db_id=? and not signup_detail.user_email like '%@orgdat.com';");
	        dc.stmt.setLong(1, db_id);
	        ResultSet rs = dc.stmt.executeQuery();
	        while (rs.next()){
	            arg0.put("member",rs.getString(1));
	            arg0.put("member_email",rs.getString(2));
	            arg0.put("role",rs.getString(3));
	            resp.add(arg0.clone());
	            arg0.clear();
	        }
	        dc.close();
	       // return members;
	        return "{\"status\":200,\"data\":"+resp.toJSONString()+",\"message\":\"database\"}";
	    } catch (Exception e){
	        dc.close();
	        e.printStackTrace();
	       // return new ArrayList<String>();
	       return "{\"status\":404,\"message\":\"not founded some error occured \"}";
	    }
	}
}
