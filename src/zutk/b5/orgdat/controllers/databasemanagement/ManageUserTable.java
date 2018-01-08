/**
 * This servlet managing table manage work process
 * 
 * @author : Ponkumar & Obeth Samuel
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.databasemanagement;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import zutk.b5.orgdat.model.orgmanagement.Share;
import com.google.gson.*;
import zutk.b5.orgdat.model.databasemanagement.*;
import zutk.b5.orgdat.controllers.orgmanagement.ManageMember;
import zutk.b5.orgdat.controllers.filters.*;

public class ManageUserTable extends HttpServlet {
	DatabaseConnection dc = null;
	PreparedStatement stmt = null;
	ManageTable templateTable;
	long user_id;
	PrintWriter writer;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) { 
		try {
			writer = response.getWriter();
			if (request.getMethod().toLowerCase().endsWith("post")) {
				doPost(request, response);
			} else if ( request.getRequestURI().substring(1).equals("getTableSharedMembers") && request.getMethod().toLowerCase().equals("get")){
			    doGet(request, response);
			} else {
				writer.write("{'status':405,'message':'this post only url'}");
				return;
			}
		} catch (Exception e) {
			writer.write("{'status':405,'message':'this post only url'}");
			return;
		}
	}

@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		try {
		    out = response.getWriter();
			dc = new DatabaseConnection("postgres", "postgres", "");
			DashboardView dv = new DashboardView(request);
			String path = request.getRequestURI().substring(1);
			long user_id = dv.rc.getUserId(request.getCookies());
			System.out.println("mo user _ get  id :" + user_id);
			String org_name = request.getParameter("org_name");
			String db_name = request.getParameter("db_name");
			String table_name = request.getParameter("table_name");
			ManageTable tableManage = new ManageTable(org_name,org_name+"_"+ db_name, "");
			if (org_name.equals("postgres") || org_name.equals("orgdat")) {
				out.write("{\"status\" : 406 ,\"message\" : \"Organization Name already Exist\"}");
				if (dc != null) {
					dc.close();
				}
				return;
				// throw new Exception();
			}
			long org_id = dc.getOrgId(org_name);
			long db_id = dc.getDBId(org_id,org_name+"_"+ db_name);
			long table_id = dc.getTableId(org_id, db_id, table_name);
			if (dv.getRole(org_name,db_name,table_name,user_id).contains("owner") == false) {
				out.write("{\"status\" : 403,\"message\" : \"Permission denied \"}");
				if (dc != null) {
					dc.close();
				}
				return;
				// throw new Exception();
			}
			System.out.println("out of the check ");
// 			ArrayList<String> ClientResponse = orgManage.getSharedMembers(org_id);
			out.write( tableManage.getSharedMembers(table_id) );
		} catch (Exception e) {
			System.out.println("Manage organi : " + e.getMessage());
			out.write("{\"status\" : 406 ,\"message\" : \"Organization Name already Exist\"}");
		}
	}
// 	 public boolean isVaildOrgForHim(long user_id,long db_id){
// 	     try {
// 			String sql = "select * from db_management where user_id=? and table_id=?";
// 			dc.stmt = dc.conn.prepareStatement(sql);
// 			dc.stmt.setLong(1, user_id);
// 			dc.stmt.setLong(2, db_id);
// // 			dc.stmt.setString(2, db_name);
// 			ResultSet rs = dc.stmt.executeQuery();
// 			while (rs.next()){
// 			    return true;
// 			}
//             return false;
//         } catch (Exception e) {
//              return false;
//         }
//     }
	/**
	 * This Method used to split table manage work to other method.
	 * 
	 * @Params : request HttpServletRequest , response HttpServletResponse
	 * 
	 * @Return : this method return the work response.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		 writer = response.getWriter();
		try { 
		    System.out.println(" anage user table");
			String reqURI = request.getRequestURI();
			RoleChecker rc = new RoleChecker(request);
			if (reqURI.startsWith("/api/")) {
				reqURI = reqURI.substring(5);
				user_id = rc.getUserId(request.getHeader("Authorization"));   
			}else{
			    user_id = rc.getUserId(request.getCookies());
			}
			String org_name = request.getParameter("org_name");
			String db_name = request.getParameter("db_name");
			String table_name = request.getParameter("table_name"); 
			if (isCorrect(table_name) == false ){
			    writer.write("{'status':406,'message':'Invaild table name'}");
				return;
			}else if (isCorrect(org_name) == false){
			    writer.write("{'status':406,'message':'Invaild organization name'}");
				return;
			}else if ( isCorrect(db_name) == false) {
				writer.write("{'status':406,'message':'Invaild database name'}");
				return;
			}
			dc = new DatabaseConnection(org_name + "_" + db_name, org_name, "");
			templateTable = new ManageTable(dc);
			/***************************** Create Table ********************************************/
			if (reqURI.endsWith("createTable") == true) {
			    if(checkCount(org_name,org_name+"_"+db_name) == false){
			        writer.write("{'status':406,'message':'you have reach max count of table  '}");
    				dc.close();
				return;
			    }
			    System.out.println("Before Create");
				String columns = request.getParameter("columnArray");
				writer.write (templateTable.createTable(org_name, db_name, table_name,
						columns, user_id));
			}
			/***************************** Rename Table ********************************************/
			else if (reqURI.endsWith("renameTable") == true) {
				String newTable = request.getParameter("rename");
				if (isCorrect(newTable) == false) {
				    writer.write("{'status':406,'message':'Invaild new table name'}");
				   dc.close();
				return;
				}
				
			System.out.println(reqURI);
				writer.write(templateTable.renameTable(org_name, db_name, table_name,
						newTable));
						dc.close();
				return;
			}
			/***************************** Delete Table ********************************************/
			else if (reqURI.endsWith("deleteTable") == true) {
			    System.out.println("deleteTable");
				writer.write(templateTable.deleteTable(org_name, db_name, table_name) );
				dc.close();
				return;
			}
			/***************************** share Table ********************************************/
			else if (reqURI.endsWith("shareTable") == true) {
				Share share = new Share();
				String role = request.getParameter("role");
				String query = request.getParameter("isRole");
				String email = request.getParameter("email");
				ManageMember mm = new ManageMember();
				System.out.println("EMAIL   ID = "+email);
				user_id = mm.getUser_id(email);
				System.out.println("USER   ID = "+user_id);
				if (share.shareTable(org_name, db_name, table_name, role,
						user_id, query) == true) {
					writer.write("{status:200,response:'share successfully'}");
				} else {
					throw new Exception();
				}
		    if (dc != null ){
		        dc.close();
		    }
			}
			/***************************** Alter Table ********************************************/
			else if (reqURI.endsWith("alterTable") == true) {
				String columnNameArray = request.getParameter("query");
				System.out.println("columnNameArray = "+columnNameArray);
				JsonArray columnArray ;
				if (columnNameArray == null){
				     columnArray = null;
				} else {
				     columnArray = new JsonParser().parse(columnNameArray)
						.getAsJsonArray();
				}
				writer.write(templateTable.alterColumn(org_name,db_name,table_name, columnArray,request.getParameter("wanted")));
				dc.close();
				return;
			}/***************************** edit Column ********************************************/
			else if (reqURI.endsWith("editColumn") == true) {
			    
				String columnNameArray = request.getParameter("query");
				System.out.println("columnNameArray = "+columnNameArray +" for edit column ");
				JsonObject columnArray ;
				if (columnNameArray == null){
				     columnArray = null;
				} else {
				     columnArray = new JsonParser().parse(columnNameArray)
						.getAsJsonObject();
				}
				writer.write(templateTable.editColumn(org_name,db_name,table_name, columnArray));
				dc.close();
				return;
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
		    if (dc != null ){
		        dc.close();
		    }
		    System.out.println("manage user table error : "+e);
		    e.printStackTrace();
			writer.write("{'status':400,'message':'Bad Reuest'}");
			return;
		} finally {
			try {
		    if (dc != null ){
		        dc.close();
		    }
			} catch (Exception e) {
				System.out.println("Connection Not Close");
			}
		}
	}

	/**
	 * This private method user to find user id
	 * 
	 * @Params : Cookie[] cookie
	 * 
	 * @Return : if vaild user return his/her user id ,else return -1
	 */
 
	private long getUserId(Cookie[] cookies) {
		ServletContext context = getServletContext();
		for (Cookie cookie : cookies) {
			if ((cookie.getName()).endsWith("iambdt")) {
				return (long) context.getAttribute(cookie.getValue());
			}
		}
		return -1;
	}  

	/**
	 * This private method used to find table name is correct or wrong
	 * 
	 * @Params : String table_name
	 * 
	 * @Return : if name match to regex return true,else return false
	 */

	private boolean isCorrect(String table_name) {
		return table_name.matches("^[a-z][a-z0-9]{3,30}$");
	}

	public boolean checkCount(String org_name, String db_name) {
		try {
		    dc = new DatabaseConnection("postgres","postgres","");
			String sql = "select count(table_id) from table_details where org_id = (select org_id from org_details where org_name = ?) and db_id = (select db_id from db_details where db_name = ?)";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, org_name);
			dc.stmt.setString(2, db_name);
			
			ResultSet rs = dc.stmt.executeQuery();
			System.out.println(rs.wasNull());
			long count = 0;
			while (rs.next()) {
			    count = rs.getLong(1);
			}
			dc.close();
			if(count > 25){
			    return false;
			}
			
			return true;
		} catch (Exception e) {
		    if (dc != null ){
		        dc.close();
		    }
		    System.out.println("retgutvygv = "+e);
			return false;
		}
	}
}
