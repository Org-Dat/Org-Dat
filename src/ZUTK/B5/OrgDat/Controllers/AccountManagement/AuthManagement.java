/**
 * This servlet managing authtokens detail
 * 
 * @author : Obeth Samuel
 * 
 * @ version : 1.0
 */
package ZUTK.B5.OrgDat.Controllers.AccountManagement;

import javax.servlet.http.*;
import java.io.*;
import ZUTK.B5.OrgDat.Controllers.Filters.DatabaseConnection;
import ZUTK.B5.OrgDat.Model.AccountManagement.*;

public class AuthManagement extends HttpServlet {
	PrintWriter writer;
	DatabaseConnection dc;

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
			dc = new DatabaseConnection("postgres", "postgres", "");
			String reqURI = path[0];
			if (reqURI.equals("$createAuth") == true) {
				String mail = request.getParameter("email");
				String password = request.getParameter("password");
                if(mail == null || password == null){
                    throw new Exception();
                }
				String authtoken = authManagement.createAuthtoken(mail,
						password);
				if (authtoken == null) {
					throw new Exception();
				}else{
				    writer.write("{'status':200,'authtoken':'"+authtoken+"'}");
				}

			} else if (reqURI.equals("$deleteAuth") == true) {
				String authtoken = request.getParameter("authtoken");
				if (authtoken == null ||authManagement.deleteAuthtoken(authtoken) != true) {
                   	throw new Exception();
				}else{
				    writer.write("{'status':200,'message':'Delete Successfully'}");
				} 
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
            writer.write("{'status':400,'message':'Bad Request'}");
		} finally {
			try {
				dc.conn.close();
			} catch (Exception e) {

			}
		}
	}

}
