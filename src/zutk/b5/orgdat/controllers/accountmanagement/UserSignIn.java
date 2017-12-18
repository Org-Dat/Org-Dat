/**
 * This servlet managing login detail
 *  
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.accountmanagement;

import javax.servlet.http.*;
import java.io.*;
import zutk.b5.orgdat.model.accountmanagement.*;

public class UserSignIn extends HttpServlet {
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
	 * This void method check login detail
	 * 
	 * @params : HttpServletRequest request,HttpServletResponse response
	 * 
	 * @return type : void
	 * 
	 * @return : if user password is correct it allow user login .else throw
	 *         error
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			SignIn si = new SignIn();
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			if (email.matches("^[a-z][a-z0-9]{5,30}@[a-z][a-z0-9]{3,30}.com$")) {
				if (password.matches("^.{6,255}$")) {
					long user_id = si.checkUserAccount(email, password);
					if (user_id != -1) {
						CookieManage cookie_manage = new CookieManage();
						String ip_address = request.getRemoteAddr();
						String user_agent = request.getHeader("User-Agent");
						String iamdbt = cookie_manage.createCookie(user_id,
								ip_address, user_agent);
						Cookie cookie = new Cookie("iambdt", iamdbt);
						cookie.setPath("/");
						cookie.setHttpOnly(true);
						response.addCookie(cookie);
						out.write("success");
						//response.sendRedirect("http://orgdat.zcodeusers.com/HTML/homepage.html");
					} else {
						out.write("User Name Or Password is incorrect");
					}
				} else {
					out.write("Paaword is incorrect");
				}
			} else {
				out.write("Email Address is incorrect");
			}
		} catch (Exception e) {

		}
	}
}