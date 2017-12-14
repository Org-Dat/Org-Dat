/**
 * This servlet managing create new my app account
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */

package zutk.b5.orgdat.controllers.accountmanagement;

import javax.servlet.http.*;
import java.io.*;
import zutk.b5.orgdat.model.accountmanagement.*;
public class UserSignUp extends HttpServlet {
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
	 * This void method check user detail and create a new user account
	 * 
	 * @params : HttpServletRequest request , HttpServletResponse response
	 * 
	 * @return type : void
	 * 
	 * @return : if user give correct datas it will create new user account
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
			    System.out.println("werwerwerwerwer");
		try {
			out = response.getWriter();
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String phoneNumber = request.getParameter("phone_number");
			String password = request.getParameter("password");
			if (name.matches("^[a-z][a-zA-Z0-9]{3,255}$")) {
				if (email.matches("^[a-z][a-z0-9]{5,30}@orgdat.com$")) {
					if (phoneNumber == null
							|| phoneNumber.matches("^[+]?[0-9]{10,30}$")) {
						if (password.matches("^.{6,255}$")) {
							SignUp su = new SignUp();
							if (su.isMember(email) == true) {
								String[] details = new String[5];
								details[0] = name;
								details[1] = email;
								details[2] = phoneNumber;
								details[3] = password;
								details[4] = "owner";
								if (su.addMemeber(details)) {
									SignIn si = new SignIn();
									long user_id = si.checkUserAccount(email,
											password);
									su.setSecurityQuestion(user_id, email);
									CookieManage cookie_manage = new CookieManage();
									String ip_address = request.getRemoteAddr();
									String user_agent = request
											.getHeader("User-Agent");
									String iamdbt = cookie_manage.createCookie(
											user_id, ip_address, user_agent);
									Cookie cookie = new Cookie("iambdt", iamdbt);
									cookie.setPath("/");
									cookie.setHttpOnly(true);
									response.addCookie(cookie);
									response.sendRedirect("http://orgdat.zcodeusers.com/HTML/homepage.html");
									/** which file redirect ? **/
								} else {
									out.write("Invaild Inputs");
								}
							} else {
								out.write("Email Id Already Exists");
							}
						} else {
							out.write("Password is incorrect");
						}
					} else {
						out.write("Phone Number is incorrect");
					}
				} else {
					out.write("Email is incorrect");
				}
			} else {
				out.write("Name is incorrect");
			}
		} catch (Exception e) {
			out.write("{'status':400,'message':'Invaild Request'}");
		}
	}
}