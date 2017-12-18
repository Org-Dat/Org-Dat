package zutk.b5.orgdat.model.orgmanagement;

import zutk.b5.orgdat.controllers.filters.*;

public class Share { 
	DatabaseConnection dc = new DatabaseConnection("postgres","postgres","");
    
	public boolean shareTable(String org_name, String db_name,
			String table_name, String role, long user_id, String query) {
		try {
		    
			String sql = "";
			if (query.equals("createRole")) {
				sql = "insert into table_management (role,user_id,org_name,db_name,table_name)values(?,?,?,?,?)";
			} else if (query.equals("editRole")) {
				sql = "update table_management set role= ? where user_id=? and org_name=? and db_name=? and table_name=?";
			} else if (query.equals("deleteRole")) {
				sql = "delete from table_management where role=? and user_id=? and org_name=? and db_name=? and table_name=?";
			} else {
			    throw new Exception();
			}
			dc = new DatabaseConnection("postgres", "postgres", "");
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, role);
			dc.stmt.setLong(2, user_id);
			dc.stmt.setString(3, org_name);
			dc.stmt.setString(4, db_name); 
			dc.stmt.setString(5, table_name);
			dc.stmt.executeUpdate();
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean shareDB(String org_name, String db_name, String role,
			long user_id, String query) {
		try {
			String sql = "";
			if (query.equals("createRole")) {
				sql = "insert into db_management (role,user_id,org_name,db_name) values(?,?,?,?)";
			} else if (query.equals("EditRole")) {
				sql = "update db_management role=? where user_id=? and org_name=? and db_name=?";
			} else if (query.equals("deleteRole")) {
				sql = "delete from db_management where role=? and user_id=? and org_name = ? and db_name=?";
			} else {
			    throw new Exception();
			}
			dc = new DatabaseConnection("postgres", "postgres", "");
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, role);
			dc.stmt.setLong(2, user_id);
			dc.stmt.setString(3, org_name);
			dc.stmt.setString(4, db_name);
			dc.stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean shareOrg(String org_name,String role,long user_id,String query){
	    try{
	        System.out.println("share");
	        String sql = "";
	        System.out.println("role = "+role);
	        System.out.println("query = "+query);
	        System.out.println(query.equals("createRole"));
	        if(query.equals("createRole")){
	            sql = "insert into org_management (role,user_id,org_name) values(?,?,?)";
	        } else if (query.equals("deleteRole")) {
	           sql = "delete from org_management where role=? and user_id=? and org_name=?";
	        } else {
			    throw new Exception();
			}
	        dc = new DatabaseConnection("postgres", "postgres", "");
	        dc.stmt = dc.conn.prepareStatement(sql);
	        dc.stmt.setString(1, role);
	        dc.stmt.setLong(2, user_id);
	        dc.stmt.setString(3, org_name);
	        dc.stmt.executeUpdate();
	        System.out.println("share perfact");
	        return true;
	    }catch(Exception e){
	        e.printStackTrace();
	        return false;
	    }
	}
}