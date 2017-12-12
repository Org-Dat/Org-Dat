package zutk.b5.orgdat.controllers.databasemanagement;

import java.io.PrintWriter;
import javax.servlet.http.*;
import zutk.b5.orgdat.model.databasemanagement.ShowDetails;
import java.util.ArrayList;
import zutk.b5.orgdat.controllers.filters.RoleChecker;

public class ShowDeets extends HttpServlet {
	PrintWriter out;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			if (request.getMethod().toLowerCase().equals("get")) {
				doGet(request, response);
			} else {
				out.write("{'status':405,'message':'this get only url'}");
			}
		} catch (Exception e) {
			out.write("{'status':405,'message':'this get only url'}");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String requri = request.getRequestURI();
			String org_name = request.getParameter("org_name");
			String db_name = request.getParameter("db_name");
			long user_id = 0;
			RoleChecker rc = new RoleChecker(request);
			if (requri.startsWith("/api/")) {
				String authtoken = request.getHeader("Authorization");
				if (authtoken == null) {
					throw new Exception();
				}
				user_id = rc.getUserId(authtoken);
				requri = requri.substring(4);
			} else {
				user_id = rc.getUserId(request.getCookies());
			}
			ShowDetails sd = new ShowDetails();
			ArrayList<String> detailList = new ArrayList<String>();
            System.out.println("usererrer : "+user_id);
            System.out.println(requri +"   ___ "+ org_name);
			switch (requri) {
			case "/getOrgName":
				detailList = sd.getOrganization(user_id);
				System.out.println("Werfwe"+detailList.toString());
				break;
			case "/getDBName":

				if (org_name == null
						|| org_name.matches("^[a-z][a-z0-9]{4,25}") == false) {
					throw new Exception();
				}
				detailList = sd.getDatabase(org_name);
				break;
			case "/getTableName":

				if (org_name == null
						|| org_name.matches("^[a-z][a-z0-9]{4,25}") == false
						|| db_name == null
						|| db_name.matches("^[a-z][a-z0-9]{4,25}") == false) {
					throw new Exception();
				}
				detailList = sd.getTables(org_name, db_name);

				break;
			}
			String answer = "";
			System.out.println(detailList.toString());
			if (detailList.size() == 0) {
				answer = "[]";
			} else {
				for (String s : detailList) {
					answer += "," + "\"" + s + "\"";
				}
				answer = "[" + answer.substring(1) + "]";
			}

			System.out.println("Answer   : " + answer);
			out.write("{ 'status' : 200, 'message' : 'Get Organization Name Successfully !...', 'Data' : "+answer+" } ");
		} catch (Exception e) {
			System.out.println();
			out.write("{'status':404}");
		}
	}
}