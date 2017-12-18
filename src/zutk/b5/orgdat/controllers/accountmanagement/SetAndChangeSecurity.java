/**
 * This Servlet manage set and change password & security question
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.accountmanagement;

import zutk.b5.orgdat.model.accountmanagement.SecurityManagement;
import zutk.b5.orgdat.controllers.filters.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.*;
import javax.servlet.*;

public class SetAndChangeSecurity extends HttpServlet {
	PrintWriter out;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			if (request.getMethod().toLowerCase().equals("post")) {
				doPost(request, response);
			} else {
				out.write("{'status':405,'message':'this post only url'}");
			}
		} catch (Exception e) {
			out.write("{'status':405,'message':'this post only url'}");
		}
	}

	/**
	 * This method split change security system
	 * 
	 * @params : HttpServletRequest request, HttpServletResponse response
	 * 
	 * @return : if user data is valid data it return detail object else return
	 *         error object
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			RoleChecker rc = new RoleChecker(request);
			out = response.getWriter();
			String requri = request.getRequestURI();
			SecurityManagement sm = new SecurityManagement();
			long user_id = rc.getUserId(request.getCookies());
			String password = request.getParameter("password");
			if (user_id == -1 || password == null) {
				throw new Exception();
			}
			if (requri.equals("changePassword")) {
				String new_password = request.getParameter("newPassword");
				if (new_password == null) {
					throw new Exception();
				}
				sm.updateSecurity(password, null, new_password, user_id);
			} else if (requri.equals("changeQuestion")) {
				String question = request.getParameter("question");
				String answer = request.getParameter("answer");
				if (question == null || answer == null) {
					throw new Exception();
				}
				sm.updateSecurity(password, question, answer, user_id);
			}else if(requri.endsWith("getUserDetail")){
			     
			}
			out.write("{'status':200,'message':'update successfully'}");

		} catch (Exception e) {
			out.write("{'status':400,'message':'invalid inputs'}");
		}
	}
}