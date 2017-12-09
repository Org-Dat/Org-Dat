package ZUTK.B5.OrgDat.Controllers.AccountManagement;

import ZUTK.B5.OrgDat.Model.AccountManagement.SecurityManagement;
import ZUTK.B5.OrgDat.Controllers.Filters.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.*;
import javax.servlet.*;

public class SetAndChangeSecurity extends HttpServlet {
	PrintWriter out;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			RoleChecker rc = new RoleChecker();
			out = response.getWriter();
			String requri = request.getRequestURI();
			SecurityManagement sm = new SecurityManagement();
			long user_id = rc.getUserId(request.getCookies());
			String password = request.getParameter("password");
			if (user_id == -1 || password == null) {
				throw new Exception();
			}
			if (requri.equals("$changePassword")) {
				String new_password = request.getParameter("newPassword");
				if (new_password == null) {
					throw new Exception();
				}
				sm.updateSecurity(password, null, new_password, user_id);
			} else if (requri.equals("$changeQuestion")) {
				String question = request.getParameter("question");
				String answer = request.getParameter("answer");
				if (question == null || answer == null) {
					throw new Exception();
				}
				sm.updateSecurity(password, question, answer, user_id);
			}/**
			  * the response desn't writen    <---- 
			  */
            
		} catch (Exception e) {
			out.write("");
		}
	}
}