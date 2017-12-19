package zutk.b5.orgdat.controllers.databasemanagement;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.ResultSet;
import zutk.b5.orgdat.controllers.filters.RoleChecker;
import zutk.b5.orgdat.controllers.orgmanagement.ManageMember;
import zutk.b5.orgdat.model.databasemanagement.*;
import zutk.b5.orgdat.controllers.filters.DatabaseConnection;
import com.google.gson.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
public class DashboardView extends HttpServlet{ 
    RoleChecker rc ;
    public DashboardView(HttpServletRequest request){
        rc = new RoleChecker(request);
    }
    
    public DashboardView(){
        rc = new RoleChecker();
    }
    
    @Override
    protected void service(HttpServletRequest request,
			HttpServletResponse response) {
	 try{
	     String role = (String)request.getAttribute("role");
	    // RoleChecker rc = new RoleChecker(request);
	     long user_id = rc.getUserId(request.getCookies());
	     rc = new RoleChecker(request);
	     ShowDetails sd =  new ShowDetails();
	     ArrayList<String> org_names = sd.getOrganization(user_id);
	     JsonObject detail = new JsonObject();
	     for(String name  : org_names){
	          detail = new JsonObject();
	          ManageMember manageMember = new ManageMember();
	          ArrayList<ArrayList<String>> memberList = manageMember.getMemberList(name);

	     }
 	    
	   
	 }catch(Exception e){
	     
	 }
}
    
    public String getRole(String org_name,long user_id){
       String role = rc.orgRole(org_name, user_id);
       return "org_"+role;
    }
    
    public String getRole(String org_name,String db_name, long user_id){
        String role = rc.dbRole(org_name, db_name, user_id);
        if(role == null){
            role = getRole(org_name, user_id);
        }
        return "db_"+role;
    }
    
    public String getRole(String org_name,String db_name,String table_name,long user_id){
        // System.out.println(rc);
        String role = rc.tableRole(org_name, db_name, table_name, user_id);
        if(role == null){
            role = getRole(org_name, db_name, user_id);
        }
        return "table_"+role ;
    }
    
    
    
     public JSONObject getDashboardViewObject(String org_name , long user_id){
        JSONObject json = new JSONObject();
        ManageMember mm = new ManageMember();
        JSONObject json1;
        JSONObject json2;
        ArrayList<String> databases;
        ArrayList<ArrayList<String>> members;
        JSONArray databaseArray;
        JSONArray tableArray;
        String role; 
        ArrayList<String> table_names;
        try {
            members = mm.getMemberList(org_name);
            json.put("Members", members);
            ShowDetails sd = new ShowDetails();
            databases = sd.getDatabase(org_name, user_id, "owner");
            databaseArray = new JSONArray();
            json1 = new JSONObject();
            for (String db_name : databases) {
                tableArray = new JSONArray();
                table_names = sd.getTables(org_name, db_name, user_id,  "owner");
                for (String table_name : table_names) {
                    json2 = new JSONObject();
                    role = getRole( org_name, db_name, table_name, user_id);
                    if (role != null ) { 
                        json2.put("table_name", table_name);
                        json2.put("size", getTableSize( org_name, db_name , table_name ));
                        tableArray.add(json1);
                    }
                }
                if (tableArray.size() > 0){
                    json1.put("db_name", db_name);
                    json1.put("size",tableArray);
                    databaseArray.add(json1);
                }
            }
            json.put("Databases", databaseArray);
            return json;
        } catch (Exception e) {
            System.out.println("dash borad view object Error : "+e.getMessage());
            return new JSONObject();
        }
        
     }
    
    public String getTableSize(String org_name,String db_name ,String table_name ) {
        try {
            rc.dc = new DatabaseConnection(org_name+"_"+db_name,org_name,"");
            rc.dc.stmt = rc.dc.conn.prepareStatement("SELECT pg_size_pretty( pg_total_relation_size(?) );");
            rc.dc.stmt.setString(1, table_name);
            ResultSet rs = rc.dc.stmt.executeQuery();
            String s = rs.getString(1);
            rc.dc.close();
            return s;
        } catch (Exception e ){
            rc.dc.close();
            return null;
        }
    }
}
 
/*

   var total = [
         { 
                   member : [],       
                   databases : {db_name1  : { table_name1  : 12mb,
                                    table_name2 : 13
                                   }
                    db_name2  : {
                        
                    }   }            
        
       
   }]

*/