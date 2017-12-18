package zutk.b5.orgdat.controllers.databasemanagement;

import java.io.PrintWriter;
import javax.servlet.http.*;
import zutk.b5.orgdat.model.databasemanagement.*;
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
				out.write("{\"status\":405,\"message\":\"this get only url\"}");
				return;
			}
		} catch (Exception e) {
			out.write("{\"status\":405,\"message\":\"this get only url\"}");
			return;
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
			DashboardView dv = new DashboardView(request);
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
			String role = "";
			switch (requri) {
			case "/getOrgName":
				detailList = sd.getOrganization(user_id);
				
				break;
			case "/getDBName":
                System.out.print("org _ "+org_name+"\n");
				if (org_name == null
						|| org_name.matches("^[a-z][a-z0-9]{3,30}") == false) {
						    
                System.out.print("org _ errerre "+org_name+"\n");
					throw new Exception();
				}
				role = dv.getRole(org_name, user_id);
				System.out.println(role);
				detailList = sd.getDatabase(org_name,user_id,role);
				break;
			case "/getTableName":

				if (org_name == null
						|| org_name.matches("^[a-z][a-z0-9]{3,30}") == false
						|| db_name == null
						|| db_name.matches("^[a-z][a-z0-9]{3,30}") == false) {
					throw new Exception();
				}
			    role = dv.getRole(org_name, db_name, user_id);
			    System.out.println(role);
				detailList = sd.getTables(org_name, db_name,user_id,role);

				break;
			}
			System.out.println("Werfwe"+detailList.toString());
			String answer = "";
			if (detailList.size() == 0) {
				answer = "[]";
			} else {
				for (String s : detailList) {
					answer += "," + "\"" + s + "\"";
				}
				answer = "[" + answer.substring(1) + "]";
			}

			System.out.println("Answer   : " + answer);
			out.write("{ \"status\" : 200, \"message\" : \"Get "+requri.substring(1)+"  Successfully !...\", \"Records\" : "+answer+" } ");
		} catch (Exception e) {
		    e.printStackTrace();
			System.out.println(e); 
			out.write("{\"status\":404 , \"message\" : \"Not Found\"}");
			return;
		}
	}
}