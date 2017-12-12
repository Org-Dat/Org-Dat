/**
 * This Servlet managing organization process
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.databasemanagement;

import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import zutk.b5.orgdat.model.databasemanagement.*;
import java.sql.ResultSet;
import zutk.b5.orgdat.model.orgmanagement.Share;
import zutk.b5.orgdat.controllers.filters.RoleChecker;

public class ManageOrganization extends HttpServlet {
	DatabaseConnection dc;
	PrintWriter out;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			if (request.getMethod().toLowerCase().equals("post")) {
				doPost(request, response);
			} else {

				out.write("{'status':405,'message':'This URL is post only url'}");
			}
		} catch (Exception e) {
			out.write("{'status':405,'message':'This URL is post only url'}");
		}
	}

	/**
	 * This method split organization mangement work
	 * 
	 * @params : HttpServletRequest request, HttpServletResponse response
	 * 
	 * @return : If user give correct data it return detail object else return
	 *         error object
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			out = response.getWriter();
			ManageOrg orgManage = new ManageOrg();
			RoleChecker rc = new RoleChecker(request);
			String path = request.getRequestURI().substring(1);
			long user_id = -1;
			if (path.startsWith("api/")){
			    String authtoken = request.getHeader("Authorization");
				if(authtoken == null){
				    throw new Exception();
				}
				 user_id = rc.getUserId(authtoken);
				 path = path.substring(4);
			} else {
			     user_id = rc.getUserId(request.getCookies());
			}
			
			System.out.println("mo user _ id :"+user_id);
			String org_name = request.getParameter("org_name");
			if (path.equals("createOrg") || path.equals("deleteOrg")
					|| path.equals("shareOrg")) {
				if (org_name.equals("postgres") || org_name.equals("orgdat")) {
					out.write("{'status' : 406 ,'message' : 'Organization Name already Exist'}");
					return;
					//throw new Exception();
				}
				String email = getMailId(user_id);
				if (email.endsWith("@orgdat.com") == false) {
				    out.write("{'status' : 403,'message' : 'Please Create Owner Account'}");
				    return;
				   //	throw new Exception();
				}
				if (checkOrgCount(user_id) == false && path.equals("createOrg")) {
					out.write("{'status' : 406 ,'message' : 'You have already reach 3 organization'}");
					return;
				}
			}
			String ClientResponse;
			if (path.endsWith("shareOrg")) {
				Share share = new Share();
				String role = request.getParameter("role");
				String query = request.getParameter("isRole");
				if (share.shareOrg(org_name, role, user_id, query) == true) {
					ClientResponse = "{ 'status' : 200 , 'message' : 'share successfully'}";
				} else {
					ClientResponse = "{'status': 403 , 'message' : 'Ask To Your Organization Owner to get Permission'}";
				} 
			} else {
				ClientResponse = orgManage.ManageOrg(user_id , org_name, path);
				out.write(ClientResponse);
			}
		} catch (Exception e) {
		    System.out.println("Manage organi : " +e.getMessage());
			out.write("{'status' : 406 ,'message' : 'Organization Name already Exist'}");
		}
	}

	/**
	 * This method used get mail id
	 * 
	 * @params : long user_id
	 * 
	 * @return type : String
	 * 
	 * @return : if user_id is member of my app it return his/her mail id.else
	 *         reteurn null
	 */
	public String getMailId(long user_id) {
		try {
			String sql = "select user_email from signup_detail where user_id=?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				return rs.getString(1);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * This method check his/her organization count.
	 * 
	 * @params : long user_id
	 * 
	 * @return type : boolean
	 * 
	 * @return : if organization count less than 3 it return true.else return
	 *         false
	 */
	public boolean checkOrgCount(long user_id) {
		try {
			String sql = "select count(org_name) from  org_management where user_id=? and role=?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, "owner");
			ResultSet rs = dc.stmt.executeQuery();
			if (rs == null) {
				return true;
			}
			while (rs.next()) {
				return (rs.getLong(1) < 3);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}