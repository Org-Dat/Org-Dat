package ZUTK.B5.OrgDat.Controllers.DatabaseManagement;

import java.io.PrintWriter;
import javax.servlet.http.*;
import ZUTK.B5.OrgDat.Model.DatabaseManagement.ShowDetails;
import java.util.ArrayList;
import ZUTK.B5.OrgDat.Controllers.Filters.RoleChecker;

public class ShowDeets extends HttpServlet {
	PrintWriter out;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String requri = request.getRequestURI().substring(1);
			long user_id = 0;
			RoleChecker rc = new RoleChecker();
			if (requri.startsWith("/api/")) {
				String authtoken = request.getHeader("Authorization");
				if (authtoken == null) {
					throw new Exception();
				}
				user_id = rc.getUserId(authtoken);
				requri = requri.substring(5);
			} else {
				user_id = rc.getUserId(request.getCookies());
			}
			String[] path = requri.split("/");
			ShowDetails sd = new ShowDetails();
			ArrayList<String> detailList = new ArrayList<String>();

			switch (path[path.length - 1]) {
			case "$getOrgName":
				detailList = sd.getOrganization(user_id);
				break;
			case "$getDBName":
				String org_name = request.getParameter("org_name");
				if (org_name == null
						|| org_name.matches("^[a-z][a-z0-9]{4,25}$")) {
					throw new Exception();
				}
				detailList = sd.getDatabase(org_name);
				break;
			case "$getTableName":
				String orgName = request.getParameter("org_name");
				String db_name = request.getParameter("db_name");
				if (orgName == null
						|| orgName.matches("^[a-z][a-z0-9]{4,25}$") == false
						|| db_name == null
						|| db_name.matches("^[a-z][a-z0-9]{4,25}$") == false) {
					throw new Exception();
				}
				detailList = sd.getTables(orgName, db_name);
				break;
			}
		} catch (Exception e) {

		}
	}
}