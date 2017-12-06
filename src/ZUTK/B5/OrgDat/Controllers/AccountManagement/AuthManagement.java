package ZUTK.B5.OrgDat.Controllers.AccountManagement;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import ZUTK.B5.OrgDat.Controllers.Filters.DatabaseConnection;
import ZUTK.B5.OrgDat.Model.AccountManagement.*;

public class AuthManagement extends HttpServlet {
	PrintWriter writer;
	DatabaseConnection dc;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			writer = response.getWriter();
			AuthManage authManagement = new AuthManage();
			dc = new DatabaseConnection("postgres", "postgres", "");
			HttpSession session = request.getSession();
			String requri = (String) session.getAttribute("reqURI");
			String[] path = requri.split("/");
			String reqURI = path[0];
			if (reqURI.equals("createAuth") == true) {
				String mail = request.getParameter("email");
				String password = request.getParameter("password");

				String authtoken = authManagement.createAuthtoken(mail,
						password);
				if (authtoken.equals("") == true) {
					throw new Exception();
				}

			} else if (reqURI.equals("deleteAuth") == true) {
				String authtoken = request.getParameter("authtoken");
				if (authManagement.deleteAuthtoken(authtoken) == true) {

				} else {
					throw new Exception();
				}
			} else {
				throw new Exception();
			}
		} catch (Exception e) {

		} finally {
			try {
				dc.conn.close();
			} catch (Exception e) {

			}
		}
	}

}
