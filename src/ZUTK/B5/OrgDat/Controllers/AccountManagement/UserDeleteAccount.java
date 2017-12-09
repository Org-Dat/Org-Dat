/**
 * This servlet manage user account delete processing work
 * 
 * @author : Obeth Samuel
 * 
 * @version: 1.0
 */
package ZUTK.B5.OrgDat.Controllers.AccountManagement;

import java.io.*;
import javax.servlet.http.*;
import ZUTK.B5.OrgDat.Model.AccountManagement.*;
import ZUTK.B5.OrgDat.Controllers.Filters.*;

public class UserDeleteAccount extends HttpServlet {
	RoleChecker rc;
	PrintWriter out;

	/**
	 * This method delete user account
	 * 
	 * @params : HttpServletRequest request , HttpServletResponse response
	 * 
	 * @return type : void
	 * 
	 * @return : if user give password correct password this method delete user
	 *         account
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			rc = new RoleChecker();
			Cookie[] cookies = request.getCookies();
			long user_id = rc.getUserId(cookies);
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			SignIn si = new SignIn();
			if (si.checkUserAccount(email, password) != -1) {
				DeleteAccount da = new DeleteAccount();
				if (da.deleteAccount(user_id)) {
					out.write("{'status'}");
				} else {
					out.write("");
				}
			}
		} catch (Exception e) {
			out.write("");
		}
	}
}