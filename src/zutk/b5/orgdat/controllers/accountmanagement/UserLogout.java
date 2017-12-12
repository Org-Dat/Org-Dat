/**
 * This  Servlet managing user logout process 
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */

package zutk.b5.orgdat.controllers.accountmanagement;

import zutk.b5.orgdat.controllers.filters.RoleChecker;
import javax.servlet.http.*;
import java.io.*;
import zutk.b5.orgdat.model.accountmanagement.*;

public class UserLogout extends HttpServlet {
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

	/**
	 * This void method used to manage user logout processing
	 * 
	 * @params : HttpServletRequest request , HttpServletResponse response
	 * 
	 * @return type : void
	 * 
	 * @return : this method do logout user account
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Cookie[] cookies = request.getCookies();
			String iambdt = ""; 
			out = response.getWriter();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("iambdt")) {
					iambdt = cookie.getValue();
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					break;
				}
			}  
			RoleChecker rc  = new RoleChecker(request);
			CookieManage cookie_manage = new CookieManage();
			if (cookie_manage.deleteCookie(iambdt,rc.getUserId(request.getCookies()),request.getRemoteAddr(),request.getHeader("User-Agent")) == true) {
				out.write("Logout Successfully");
			} else {
				out.write("Logout Unsuccessfully");
			}
		} catch (Exception e) {
			out.write("Logout Unsuccessfully");
		}
	}
}