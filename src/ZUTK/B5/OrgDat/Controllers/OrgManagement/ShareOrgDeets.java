package ZUTK.B5.OrgDat.Controllers.OrgManagement;

import javax.servlet.http.*;
import ZUTK.B5.OrgDat.Model.OrgManagement.Share;
import javax.servlet.*;
import java.io.*;
import ZUTK.B5.OrgDat.Controllers.Filters.*;

public class ShareOrgDeets extends HttpServlet {
	PrintWriter out;
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
	   try{
	       out = response.getWriter();
	       Share share = new Share();
            String requri = request.getRequestURI();
            if(requri.startsWith("/api") == true){
                requri = requri.substring(5);
            }
            RoleChecker rc = new RoleChecker();
            String[] path = requri.split("/");
            String org_name = path[0];
            String role = request.getParameter("role");
            long user_id = rc.getUserId(request.getCookies());
            String query = request.getParameter("isRole");
            boolean isAdd = false;
        switch (path[path.length-1]){
            case "$shareTable":
                String db_name = path[1];
                String table_name = path[2];
                isAdd = share.shareTable(org_name, db_name, table_name, role, user_id, query);
                break;
            case "$shareDB" :
                String dbName = path[2];
                isAdd = share.shareDB(org_name, dbName, role, user_id, query);
                break;
            case "$shareOrg" :
                isAdd = share.shareOrg(org_name, role, user_id, query);
                break;
            default :
                throw new Exception();
        }
        if(isAdd == true){
            out.write("{status:200,response:'share successfully'}");
        }else{
            throw new Exception();
        }
	   }catch(Exception e){
	       out.write("{status:400,response:'Bad Request'}");
	   }
    }
}