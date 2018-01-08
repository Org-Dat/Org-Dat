package zutk.b5.orgdat.model.orgmanagement;

import zutk.b5.orgdat.controllers.filters.*;

public class Share { 
	DatabaseConnection dc = new DatabaseConnection("postgres","postgres","");
    
	public boolean shareTable(String org_name, String db_name,
			String table_name, String role, long user_id, String query) {
		try {
		    
			String sql = "";
			
			dc = new DatabaseConnection("postgres", "postgres", "");
			long org_id = dc.getOrgId(org_name);
			long db_id = dc.getDBId(org_id,org_name+"_"+db_name);
			long table_id = dc.getTableId(org_id,db_id,table_name);
			System.out.println("ORG ID  = " +org_id + " : DB ID  = "+db_id + "USER ID "+user_id);
			if (query.equals("createRole")) {
				sql = "insert into table_management (role,user_id,table_id)values(?,?,?)";
			} else if (query.equals("editRole")) {
				sql = "update table_management set role= ? where user_id=? and table_id=?";
			} else if (query.equals("deleteRole")) {
				sql = "delete from table_management where role=? and user_id=? and table_id=?";
			} else {
			    throw new Exception();
			}
			
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, (role.trim()).toLowerCase());
			dc.stmt.setLong(2, user_id);
			dc.stmt.setLong(3, table_id);
			dc.stmt.executeUpdate();
			   System.out.println("share perfact");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean shareDB(String org_name, String db_name, String role,
			long user_id, String query) {
		try {
			String sql = "";
			dc = new DatabaseConnection("postgres", "postgres", "");
			long org_id = dc.getOrgId(org_name);
			long db_id = dc.getDBId(org_id,org_name+"_"+db_name);
			System.out.println("ORG ID  = " +org_id + " : DB ID  = "+db_id+ "USER ID "+user_id);
			if (query.equals("createRole")) {
				sql = "insert into db_management (role,user_id,db_id) values(?,?,?)";
			} else if (query.equals("EditRole")) {
				sql = "update db_management role=? where user_id=? and db_id  = ?";
			} else if (query.equals("deleteRole")) {
				sql = "delete from db_management where role=? and user_id=? and  db_id=?";
			} else {
			    throw new Exception();
			}
			
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, (role.trim()).toLowerCase());
			dc.stmt.setLong(2, user_id);
			dc.stmt.setLong(3, db_id);
			dc.stmt.executeUpdate();
			   System.out.println("share perfact");
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
	        dc = new DatabaseConnection("postgres", "postgres", "");
	        long org_id = dc.getOrgId(org_name);
	        System.out.println("ORG ID  = " +org_id + "USER ID "+user_id);
	        if(query.equals("createRole")){
	        
	            sql = "insert into org_management (role,user_id,org_id) values(?,?,?)";
	        } else if (query.equals("deleteRole")) {
	           sql = "delete from org_management where role=? and user_id=? and org_id=?";
	        } else {
			    throw new Exception();
			}
	        
	        dc.stmt = dc.conn.prepareStatement(sql);
	        dc.stmt.setString(1, (role.trim()).toLowerCase());
	        dc.stmt.setLong(2, user_id);
	        dc.stmt.setLong(3, org_id);
	        dc.stmt.executeUpdate();
	        System.out.println("share perfact");
	        return true;
	    }catch(Exception e){
	        e.printStackTrace();
	        return false;
	    }
	}
}
