package ZUTK.B5.OrgDat.Controllers.DatabaseManagement;

import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ZUTK.B5.OrgDat.Model.DatabaseManagement.*;
import java.sql.ResultSet;
import ZUTK.B5.OrgDat.Model.OrgManagement.Share;
import ZUTK.B5.OrgDat.Controllers.Filters.RoleChecker;

public class ManageOrganization extends HttpServlet {
	DatabaseConnection dc;
	PrintWriter out;
    
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {

		try { 
			dc = new DatabaseConnection("postgres", "postgres", "");
			out = response.getWriter();
			ManageOrg orgManage = new ManageOrg();
			String[] path = request.getRequestURI().split("/");
			RoleChecker rc = new RoleChecker();
			long user_id = rc.getUserId(request.getCookies());
			if (path[1].equals("$createOrg") || path[1].equals("$deleteOrg") || path[1].equals( "$shareOrg")) {
			    if(path[0].equals("postgres") || path[0].equals("orgdat")){
			        throw new Exception();
			    }
				String email = getMailId(user_id);
				if (email.endsWith("@orgdat.com") == false) {
					throw new Exception();
				}
				if(checkOrgCount(user_id) == false){
				    throw new  Exception();
				}
			}
			String ClientResponse ;
			if (path[1].equals( "$shareOrg")){
			    Share share = new Share();
                String role = request.getParameter("role");
                String query = request.getParameter("isRole");
				if (share.shareOrg(path[0], role, user_id, query) == true){
				    ClientResponse = "{status:200,response:'share successfully'}";
				} else {
				    ClientResponse = "{'status':403 ,'message':'Forbidden'}";
				}
			} else {
    			 ClientResponse = orgManage.ManageOrg(request.getRequestURI());
    			out.write(ClientResponse);
			}
		} catch (Exception e) {
			out.write("{'status':404,'message':'Not Found'}");
		}
	}

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

	public boolean checkOrgCount(long user_id) {
		try {
			String sql = "select count(org_name) from  org_management where user_id=? and role=?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, "owner");
			ResultSet rs = dc.stmt.executeQuery();
			if(rs == null){
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