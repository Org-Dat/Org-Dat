/**
 * This servlet managing authtokens detail
 * 
 * @author : Obeth Samuel
 * 
 * @ version : 1.0
 */
package zutk.b5.orgdat.controllers.accountmanagement;

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
			String requri = request.getRequestURI();
			if (requri.startsWith("/api/")) {
				requri = requri.substring(5);
			}
			String[] path = requri.split("/");
			writer = response.getWriter();
			AuthManage authManagement = new AuthManage();
			String reqURI = path[0];
			if (reqURI.equals("createAuth") == true) {
				String mail = request.getParameter("email");
				String password = request.getParameter("password");
				if (mail == null || password == null) {
					throw new Exception();
				}
				System.out.println("Pass == 1");
				String authtoken = authManagement.createAuthtoken(mail,
						password);
				System.out.println("Pass == 2" + authtoken);
				if (authtoken == null) {
					throw new Exception();
				} else {
					writer.write("{'status':200,'authtoken':'" + authtoken
							+ "'}");
				}
               System.out.println("Pass == 3");
			} else if(reqURI.equals("getAuth") == true){
			    
			}else if (reqURI.equals("deleteAuth") == true) {
				String authtoken = request.getParameter("authtoken");
				if (authtoken == null
						|| authManagement.deleteAuthtoken(authtoken) != true) {
					throw new Exception();
				} else {
					writer.write("{'status':200,'message':'Delete Successfully'}");
				}
			} else {
			    System.out.println("Pass == 4");
				throw new Exception();
			}
		} catch (Exception e) {
		    System.out.println(e);
			writer.write("{'status':400,'message':'Bad Request'}");
		}
	}

}
