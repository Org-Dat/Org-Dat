/**
 * This  Servlet managing user logout process 
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */

package zutk.b5.orgdat.controllers.accountmanagement;

import zutk.b5.orgdat.controllers.filters.*;
import javax.servlet.http.*;
import java.sql.ResultSet;
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
		    System.out.println("HAIIIIIIIIIIIIIIIIIIIIIII");
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
			System.out.println("HEKLLLLLELELLELELE");
			RoleChecker rc  = new RoleChecker(request);
			CookieManage cookie_manage = new CookieManage();
			long user_id = getUserId(request.getCookies());
			System.out.println("USER  ID  = "+user_id );
			removeCheck( user_id);
			if (cookie_manage.deleteCookie(iambdt,user_id,request.getRemoteAddr(),request.getHeader("User-Agent")) == true) {
				out.write("Logout Successfully");
			} else {
				out.write("Logout Unsuccessfully");
			}
		} catch (Exception e) {
		    e.printStackTrace();
			out.write("Logout Unsuccessfully");
		}
	}
	
	public long getUserId(Cookie[] cookies){
	    try{
	        long user_id = -1;
	        String iambdt = "";
	        for(Cookie cookie : cookies){
            	if (cookie.getName().equals("iambdt")) {
            	   iambdt = cookie.getValue();   
            	}
	        }
	        String sql = "select user_id from cookie_management where iambdt_cookie=?";
	        DatabaseConnection dc = new DatabaseConnection("postgres","postgres","");
	        dc.stmt = dc.conn.prepareStatement(sql);
	        dc.stmt.setString(1, iambdt);
	        ResultSet rs = dc.stmt.executeQuery();
	        while(rs.next()){
	            user_id = rs.getLong(1);
	        }
	        dc.close();
	        return user_id;
	    }catch(Exception e){
	        return -1;
	    }
	}
	
	
	private boolean removeCheck(long user_id) {
		
		try {
			DatabaseConnection dc = new DatabaseConnection("postgres","postgres","");
			System.out.println("lock ender");
			dc.stmt = dc.conn
					.prepareStatement("delete user_id from lock_management where org_id =? ");
			dc.stmt.setLong(1, user_id);
			dc.stmt.executeUpdate();
			dc.close();
			return true;
		} catch (Exception e) {
			
			return false;
		}
	}
	
	
}
