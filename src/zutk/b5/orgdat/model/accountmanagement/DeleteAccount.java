package zutk.b5.orgdat.model.accountmanagement;

import java.util.ArrayList;
import java.sql.*;
import zutk.b5.orgdat.model.databasemanagement.*;

public class DeleteAccount {
	DatabaseConnection dc;
	public boolean deleteAccount(long user_id) {
		try {
		//	rc = new RoleChecker();
			dc = new DatabaseConnection("postgres", "postgres", "");
			
			if (isOwner(user_id) == true) {
				ManageOrg mo = new ManageOrg();
                 ArrayList<String> org_names = getOrgNames(user_id);
                 for(String org_name : org_names){
                   mo.deleteOrg(mo.getDatabases(org_name), org_name);
                   deleteMebmer(org_name);
                 }
			}
			return deleteUser(user_id);
		} catch (Exception e) {
			return false;
		}
	}

	private ArrayList<String> getOrgNames(long user_id) {
		try {
			ArrayList<String> org_names = new ArrayList<String>();
			String sql = "select org_name from org_management where user_id=? and role=?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, "owner");
			ResultSet rs = dc.stmt.executeQuery();
			if (rs == null) {
				return org_names;
			}
			while (rs.next()) {
				org_names.add(rs.getString("org_name"));
			}
			return org_names;
		} catch (Exception e) {
			return new ArrayList<String>();
		}

	}

	public boolean deleteUser(long user_id) {
		try {
			String sql = "delete from signup_management where user_id =?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			dc.stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isOwner(long user_id) {
		try {
			String sql = "select role from signup_management where user_id = ?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				if (rs.getString("role").equals("owner")) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean deleteMebmer(String org_name){
	    try{
	        String sql = "select user_id from signup_datail email like %@"+org_name+".com";
	        dc.stmt = dc.conn.prepareStatement(sql); 
	        ResultSet rs = dc.stmt.executeQuery();
	        if(rs.wasNull()){
	            return true;
	        }
	        while(rs.next()){
	            deleteUser(rs.getLong(1));
	        }
	        return true;
	    }catch(Exception e){
	        return false;
	    }
	}
}