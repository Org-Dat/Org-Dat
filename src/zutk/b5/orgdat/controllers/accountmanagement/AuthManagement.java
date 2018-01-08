/**
 * This servlet managing authtokens detail
 * 
 * @author : Obeth Samuel
 * 
 * @ version : 1.0
 */
package zutk.b5.orgdat.controllers.accountmanagement;

import zutk.b5.orgdat.controllers.filters.*;
import javax.servlet.http.*;
import java.io.*;
import zutk.b5.orgdat.model.accountmanagement.*;

public class AuthManagement extends HttpServlet {
	PrintWriter writer;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			writer = response.getWriter();
			if (request.getMethod().toLowerCase().equals("post")) {
				doPost(request, response);
			} else {

				writer.write("{'status':405,'message':'this post only url'}");
			}
		} catch (Exception e) {
			writer.write("{'status':405,'message':'this post only url'}");
		}

	}

	/**
	 * This void method used split auth token manage work
	 * 
	 * @params : HttpServletRequest request,HttpServletResponse response
	 * 
	 * @return type : void
	 * 
	 * @return : if user give correct data,it responsed.else it return error
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
		    long user_id = -1;
			String requri = request.getRequestURI();
			if (requri.startsWith("/api/")) {
				requri = requri.substring(5);
				String mail = request.getParameter("email");
				String password = request.getParameter("password");
				DatabaseConnection dc = new DatabaseConnection("postgres","postgres","");
				user_id = dc.getUserId(mail , password);
				dc.close();
			}else{
			    RoleChecker rc = new RoleChecker(request);
			    user_id = rc.getUserId(request.getCookies());
			}

			writer = response.getWriter();
			AuthManage authManagement = new AuthManage();
			if (requri.endsWith("createAuth") == true) {
				System.out.println("Pass == 1");
				String authtoken = authManagement.createAuthtoken(user_id, request.getRemoteAddr());
				System.out.println("Pass == 2" + authtoken);
				if (authtoken == null) {
					throw new Exception();
				} else {
					writer.write(authManagement.getAuthToken(user_id));
				}
				System.out.println("Pass == 3");
			} else if (requri.endsWith("getAuth") == true) {
				System.out.println("USER ID IN AUTH  == " + user_id);
				String authtoken = authManagement.getAuthToken(user_id);
				if (authtoken == null) {
					writer.write("Invalid User");
				} else {
					writer.write(authtoken);
				}
			} else if (requri.endsWith("deleteAuth") == true) {
				String[] authtoken = request.getParameter("authtoken").split(",");
				if (authtoken == null
						|| authManagement.deleteAuthtoken(authtoken,user_id) != true) {
					throw new Exception();
				} else {
					writer.write(authManagement.getAuthToken(user_id));
				}
			} else {
				System.out.println("Pass == 4");
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			writer.write("{'status':400,'message':'Bad Request'}");
		}
	}

}
