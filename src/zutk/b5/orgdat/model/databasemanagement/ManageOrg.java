package zutk.b5.orgdat.model.databasemanagement;

import java.util.ArrayList;
import java.sql.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.File; 
import zutk.b5.orgdat.controllers.filters.DatabaseConnection;

public class ManageOrg {
	DatabaseConnection dc;
	public String ManageOrg(long user_id ,String orgName,String reqURI) {
		try {
		    
// 			if (requri.startsWith("/api/")) {
// 				requri = requri.substring(5);
// 			}
			dc = new DatabaseConnection("postgres", "postgres", "");
			if (isCorrect(orgName) == true) {
				boolean isCorrect = false;
				if (reqURI.equals("createOrg")) {
					isCorrect = createOrg(user_id,orgName);
				} else if (reqURI.equals("deleteOrg")) {
					ArrayList<String> databases = getDatabases(orgName);
					isCorrect = deleteOrg(databases, orgName);
					System.out.println("delete org is "+isCorrect);
				}
				if (isCorrect == true) {
                    dc.close();
					return "{  'status' : 200,  'message' : 'Organizatin create successfully',  'Organization Name' : '"+ orgName +" '}";
				} else {
				    dc.close();
					return "{'status': 403 , 'message' : 'Permission defined '}";
				}
			} else { 
			    dc.close();
				return "{'status':404,'message':'Your Organization Name is incorrect'}";
			}
		} catch (Exception ce) {
		    if (dc != null ){
    		    dc.close();
		    }
		    System.out.println("Show Deets : " +ce.getMessage());
			return "{'status' : 406 ,'message' : 'Organization already exist'}";
		}
	}

	/**
	 * This private method used to validate organization name
	 * 
	 * @params : String orgName
	 * 
	 * @return type : boolean
	 * 
	 * @return : if name match to regex it return true.else it return false
	 */
	private boolean isCorrect(String orgName) {
		boolean correct = orgName.matches("^[a-z0-9][a-z0-9]{3,30}");
		return correct;
	}

	/**
	 * This private method used to get one organization databases
	 * 
	 * @params : String org_name
	 * 
	 * @return type : ArratList<String>
	 * 
	 * @return : it return org databases name arraylist.
	 */
    
	public ArrayList<String> getDatabases(String org_name) {
	        dc = new DatabaseConnection("postgres","postgres","");
		ArrayList<String> databaseNameList = new ArrayList<String>();
		String sqlQuery = "select db_name from db_details where org_id in (select org_id from org_details where org_name = ?)";
		try {// db_management org_management
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name);
			ResultSet db_names = dc.stmt.executeQuery();
			while (db_names.next()) {
				databaseNameList.add(db_names.getString(1));
			}
			System.out.println("JKDNWJNDKJWLKJDLKJWLKJDLKJWDLKWLKDWLKDNLKWNDWDNKLWNLK"+databaseNameList.toString());
			//dc.close();
		} catch (Exception e) {
		    System.out.println("ERRRRRR" + e);
			return new ArrayList<String>();
		}
		return databaseNameList;
	}

	/**
	 * This private method used to create new organization.
	 * 
	 * @params : String name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if organization successfully create it return true.else false.
	 */

	private boolean createOrg(long user_id ,String name) throws ClassNotFoundException {

		try {
		    dc  = new DatabaseConnection("postgres","postgres","");
		    String sqlQuery = "insert into org_details (owner_id,org_name) values(?,?)";
		    dc.stmt = dc.conn.prepareStatement(sqlQuery);
		    dc.stmt.setLong(1, user_id);
		    dc.stmt.setString(2,name);
		    dc.stmt.executeUpdate();
		    dc.stmt.close();
		    System.out.println("Creare Org idr");
		    System.out.println(name  + "ORG NAME ");
		    long org_id = dc.getOrgId(name);
		    if(org_id == -1){
		        throw new Exception();
		    }
		    System.out.println("Creare Org Folder");
			sqlQuery = "insert into org_management (user_id,org_id,role) values(?,?,?)";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setLong(2, org_id);
			dc.stmt.setString(3, "owner");
			dc.stmt.executeUpdate(); 
			System.out.println("Creare Org Folder");
			File f = new File("Log/"+name);
			if (f.mkdirs() == false || manageRole("create role " + name + " with login createdb PASSWORD 'zohouniversity' ") == false) {
				return false;
			}
			return true;
		} catch (Exception e) {
		    e.printStackTrace();
			return false;
		}
	}

	/**
	 * This private method used to delete organization.
	 * 
	 * @params : String org_name,ArrayList<String> databaseNames
	 * 
	 * @return type : boolean
	 * 
	 * @return : if organization successfully delete it return true.else false.
	 */

	public boolean deleteOrg(ArrayList<String> databaseNames, String org_name) {
		try {
		    dc  = new DatabaseConnection("postgres","postgres","");
			String sqlQuery = "delete from org_details where org_name=?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name);
			dc.stmt.executeUpdate();
			Statement stmt = dc.conn.createStatement();
			//dc.close();
	  	//dc = new DatabaseConnection("",org_name,"");
	  	    System.out.println(databaseNames.toString());
			for (String database : databaseNames) {
				stmt.execute("drop database " + database);
			//	stmt = dc.conn.createStatement();
			}
			Runtime.getRuntime().exec("rm -rf Log/"+org_name);
			boolean  bool = manageRole("drop role " + org_name) ;
			System.out.println("some "+bool);
			dc.close();
			return bool;
			
		} catch (Exception e) {
		    System.out.println(e);
			return false;
		}
	}

	/**
	  *
	  */

	private boolean manageRole(String sqlQuery) {
		try {
		    DatabaseConnection dataconn = new DatabaseConnection("postgres","postgres","");
			Statement dstmt = dataconn.conn.createStatement();
			dstmt.execute(sqlQuery);
			System.out.println("QWERTYUIOPASDFG");
			dataconn.close();
			//dc.close();
			return true;
		} catch (Exception e) {
		    e.printStackTrace();
		    System.out.println(e);
			return false;
		}
	}
	
	public String getSharedMembers(long org_id) {
	    try {
	        JSONArray resp = new JSONArray();
	        dc = new DatabaseConnection("postgres","postgres","");
	       // long org_id = dc.getOrgId(org_name);
	       JSONObject arg0 = new JSONObject();
	        dc.stmt = dc.conn.prepareStatement(" select signup_detail.user_name,signup_detail.user_email,org_management.role from signup_detail inner join org_management on signup_detail.user_id = org_management.user_id and org_management.org_id=? and not signup_detail.user_email like '%@orgdat.com';");
	        dc.stmt.setLong(1, org_id);
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
	        return "{\"status\":200,\"data\":"+resp.toJSONString()+",\"message\":\"org\"}";
	    } catch (Exception e){
	        dc.close();
	        e.printStackTrace();
	       // return new ArrayList<String>();
	       return "{\"status\":404,\"message\":\"not founded some error occured \"}";
	    }
	}
}
