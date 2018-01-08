package zutk.b5.orgdat.controllers.orgmanagement;

import javax.servlet.http.*;
import zutk.b5.orgdat.model.orgmanagement.Share;
import javax.servlet.*;
import java.io.*;
import zutk.b5.orgdat.controllers.filters.*;

public class ShareOrgDeets extends HttpServlet {
    PrintWriter out;
	protected void service(HttpServletRequest request,
			HttpServletResponse response) {
		try {
	     out = response.getWriter();
			if (request.getMethod().toLowerCase().equals("post")) {
				doPost(request, response);
			}
		} catch (Exception e) {
            out.write("permission denied ");
		}

	}

	//PrintWriter out;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			out = response.getWriter();
			Share share = new Share();
			String requri = request.getRequestURI();
			if (requri.startsWith("/api") == true) {
				requri = requri.substring(5);
			}
			RoleChecker rc = new RoleChecker(request);
			String[] path = requri.split("/");
			String org_name = request.getParameter("org_name");
			String role = request.getParameter("role");
			long user_id = rc.getUserId(request.getCookies());
			String query = request.getParameter("isRole");
			boolean isAdd = false;
			switch (path[path.length - 1]) {
			case "shareTable":
				String db_name = request.getParameter("db_name");
				String table_name = request.getParameter("table_name");
				isAdd = share.shareTable(org_name, db_name, table_name, role,
						user_id, query);
				break;
			case "shareDB":
				String dbName = request.getParameter("db_name");
				isAdd = share.shareDB(org_name, dbName, role, user_id, query);
				break;
			case "shareOrg":
				isAdd = share.shareOrg(org_name, role, user_id, query);
				break;
			default:
				throw new Exception();
			}
			if (isAdd == true) {
				out.write("{status:200,response:'share successfully'}");
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			out.write("{status:400,response:'Bad Request'}");
		}
	}
}