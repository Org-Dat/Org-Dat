package zutk.b5.orgdat.model.databasemanagement;


import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.ArrayList;
import java.sql.ResultSet;
import zutk.b5.orgdat.controllers.filters.*;
import zutk.b5.orgdat.controllers.databasemanagement.DashboardView;
public class ShowDetails {
	DatabaseConnection dc;
 
    public  ShowDetails(){
        dc =  new DatabaseConnection("postgres","postgres","");
    }
    
	public ArrayList<String> getOrganization(long user_id) {
		ArrayList<String> organizations = new ArrayList<String>();
		String sqlQuery = "select org_name from org_details where owner_id = ? ";
		dc =  new DatabaseConnection("postgres","postgres","");
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				organizations.add(rs.getString(1));
			}
			System.out.println("Sample" +organizations.toString());
		//	dc.close();
			return organizations;
		} catch (Exception e) {
		    if (dc != null ){
    		    dc.close();
		    }
		    System.out.println(e.getMessage());
			return new ArrayList<String>();
		}

	}

	/**
	 * This method used to get database names .
	 * 
	 * @params : String org_name ,long user_id
	 * 
	 * @return type : ArrayList<String>
	 * 
	 * @return : it return database names arraylist
	 */
	public ArrayList<String> getDatabase(String org_name,long user_id,String role) {
	    DashboardView  dv = new DashboardView();  
	    long org_id = dc.getOrgId(org_name); 
	    String s1 = dv.getRole( org_name, user_id);
	    System.out.println(s1);
	    if (s1.endsWith("owner")){
	        return getAllDatabase( org_id);
	    } else {
    	    JSONObject whole = getDashboardViewObject( org_name ,  user_id);
    	    System.out.println(whole);
    	    ArrayList<String> retur = new ArrayList<String>();
    	    for (Object s : whole.keySet()){
    	        retur.add(((String)s).split("_")[1]);
    	        
    	    }
    	    return retur;
	    }
// 		ArrayList<String> databases = new ArrayList<String>();
// 		String sqlQuery = "select db_name from db_management where org_name=?";
// 		if(role.startsWith("org_")  == false || role.endsWith("owner") == false){
// 		    sqlQuery = sqlQuery + "and user_id = "+user_id;
// 		}
// 		try {
// 		    System.out.println("get db detals model org_name : "+org_name);
// 			dc.stmt = dc.conn.prepareStatement(sqlQuery);
// 			dc.stmt.setString(1, org_name);
// 			ResultSet rs = dc.stmt.executeQuery();
// 			while (rs.next()) {
// 				databases.add(((rs.getString(1)).split("_"))[1]);
// 			}
// 			dc.close();
// 			return databases;
// 		} catch (Exception e) {
// 		    if (dc != null ){
//     		    dc.close();
// 		    }
// 		    System.out.println("get db detals model error : "+e.getMessage());
// 			return new ArrayList<String>();
// 		}
	}
/*
    public ArrayList<String> getAllOrg(long user_id) {
		ArrayList<String> orgs = new ArrayList<String>();
		String sqlQuery = "select org_name from org_details where user_id=?";
		try {
		    System.out.println("get all org details model user_id : "+user_id);
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				orgs.add(((rs.getString(1)).split("_"))[1]);
			}
			dc.close();
			return orgs;
		} catch (Exception e) {
		    if (dc != null ){
    		    dc.close();
		    }
		    System.out.println("get all org detals model error : "+e.getMessage());
			return new ArrayList<String>();
		}
	}*/
    public ArrayList<String> getAllDatabase(long org_id) {
		ArrayList<String> databases = new ArrayList<String>();
		String sqlQuery = "select db_name from db_details where org_id=?";
		try {
		    System.out.println("get all db detals model org_id : "+org_id);
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, org_id);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				databases.add(((rs.getString(1)).split("_"))[1]);
			}
		//	dc.close();
			return databases;
		} catch (Exception e) {
		    if (dc != null ){
    		    dc.close();
		    }
		    System.out.println("get all db detals model error : "+e.getMessage());
			return new ArrayList<String>();
		}
	}

    public ArrayList<String> getAllTable(long org_id,long db_id) {
		ArrayList<String> tables = new ArrayList<String>();
		String sqlQuery = "select table_name from table_details where org_id=? and db_id=?";
		try {
		    System.out.println("get db detals model org_id : "+org_id);
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, org_id);
			dc.stmt.setLong(2, db_id);
			ResultSet rs = dc.stmt.executeQuery();
			System.out.println("org_id = "+org_id+" db_id = "+db_id);
			System.out.println(rs.getFetchSize());
			while (rs.next()) {
			    System.out.println(" --- -- -- "); 
			    
				tables.add(rs.getString(1));
			}
		//	dc.close();
		System.out.println("tables = "+tables);
			return tables;
		} catch (Exception e) {
		    e.printStackTrace();
		    if (dc != null ){
    		    dc.close();
		    }
		    System.out.println("get db detals model error : "+e.getMessage());
			return new ArrayList<String>();
		}
	}

	/**
	 * This method used to get table names .
	 * 
	 * @params : String db_name,String org_name ,long user_id1ijenkjwfenfewijnew
	 * 
	 * @return type : ArrayList<String>
	 * 
	 * @return : it return table names arraylist
	 */
	public ArrayList<String> getTables(String org_name, String db_name,long user_id ,String role) {
	    System.out.println("get table ewfwafwefwaeeafwaefwef ");
	    DashboardView  dv = new DashboardView();  
	    dc = new DatabaseConnection("postgres","postgres","");
	    long org_id = dc.getOrgId(org_name); 
	    long db_id = dc.getDBId(org_id,org_name+"_"+db_name); 
	    String df = dv.getRole( org_name,db_name, user_id);
	    System.out.println(df);
	    if (df.endsWith("owner")){
	       // dc.close();
	        return getAllTable( org_id,db_id);
	    } else {
    		JSONObject whole = getDashboardViewObject( org_name ,  user_id);
    		if (whole.get(org_name+"_"+db_name) == null){
    		  //  dc.close();
    		    return new ArrayList<String>();
    		} 
    // 		dc.close();
    	    return ((ArrayList<String>) whole.get(org_name+"_"+db_name));
	    }
// 		String sqlQuery = "select table_name from table_management where org_name=? and db_name=?";
// 		if(role.endsWith("owner") == false){
// 		    sqlQuery = sqlQuery + "and user_id = "+user_id;
// 		}
// 		try {
// 		    dc = new DatabaseConnection("postgres","postgres","");
// 			dc.stmt = dc.conn.prepareStatement(sqlQuery);
// 			dc.stmt.setString(1, org_name);
// 			dc.stmt.setString(2, org_name+"_"+db_name);
// 			ResultSet rs = dc.stmt.executeQuery();
// 			while (rs.next()) {
// 			    System.out.println(rs.getString(1));
// 				tables.add(rs.getString(1));
// 			}
// 			dc.close();
// 			System.out.println();
// 			return tables;
// 		} catch (Exception e) {
// 		    if (dc != null ){
//     		    dc.close();
// 		    }
// 		    System.out.println(e.getMessage());
// 			return new ArrayList<String>();
// 		}
	}
	
	
     public JSONObject getDashboardViewObject(String org_name , long user_id){
        DashboardView  dv = new DashboardView();  
        JSONObject json = new JSONObject();
        ArrayList<String> tableArray;
        String role;
        long org_id = dc.getOrgId(org_name);
        long db_id  = 0l;
        try {
            for (String db_name : getAllDatabase(org_id)) {
                db_id = dc. getDBId(org_id, org_name+"_"+db_name);
                tableArray = new ArrayList<String>();
                ArrayList<String> tem = getAllTable(org_id, db_id);
                for (String table_name : tem) {
                    role = dv.getRole( org_name, db_name, table_name, user_id);
                    System.out.println("role = " +role);
                    if (role != null && role.endsWith("null")==false) { 
                        tableArray.add(table_name);
                    }
                }
                if (tableArray.size() > 0 || tem.size() == 0) {
                    json.put(db_name, tableArray);
                }
            }
            return json;
        } catch (Exception e) {
            System.out.println("dash borad view object Error : "+e.getMessage());
            return new JSONObject();
        }
        
     }
    
}