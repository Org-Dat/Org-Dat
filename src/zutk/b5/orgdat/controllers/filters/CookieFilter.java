/**
  * This filter used to check cookies.
  * 
  * @author : Obeth Samuel & Ponkumar
  * 
  * @version : 1.0
  */
  

package zutk.b5.orgdat.controllers.filters;

import java.io.*;
import java.sql.*;
import javax.servlet.*; 
import javax.servlet.http.*;
import zutk.b5.orgdat.model.accountmanagement.CookieManage; 
public class CookieFilter extends HttpServlet implements Filter {
	public long user_id;
	protected DatabaseConnection dc;
// 	protected PreparedStatement stmt = null;
	ServletContext servletContext ;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();
	}
    /**
      * This method used to check cookies and set user id in context
      * 
      * @params : ServletRequest req, ServletResponse res,FilterChain chain
      * 
      * @return type : void
      * 
      * @return : if user didn't give correct detail it return error object .else redirect to servlet
      */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		Cookie[] cookies = request.getCookies();
		
		String iambdt = "";
		long user_id = 0;
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			String requri = request.getRequestURI();
			if (requri.startsWith("/api/")) {
				String authtoken = request.getHeader("Authorization");
				if(authtoken == null){
				    throw new Exception();
				}
				 user_id = getUserId(authtoken);
				if(user_id == -1){
				    throw new Exception();
				}
				iambdt = authtoken;
			} else {
			    if (cookies == null || cookies.length <= 1) {
        			response.sendRedirect("/JSP/landingPage.jsp");
        		}
			    for (Cookie cookie : cookies) {
					if (cookie.getName().equals("iambdt")) {
						iambdt = cookie.getValue();
						break;
					}
				}
				String ipAddress = request.getRemoteAddr();
				String user_agent = request.getHeader("User-Agent");
				String query = "select user_id from cookie_management where user_agent = ? and ip_address = ? and iambdt_cookie = ?";
				dc.stmt = dc.conn.prepareStatement(query);
				dc.stmt.setString(1, user_agent);
				dc.stmt.setString(2, ipAddress);
				dc.stmt.setString(3, iambdt);
				ResultSet rs = dc.stmt.executeQuery();
				if (rs.wasNull()) {
					throw new Exception();
				}
				while (rs.next()) {
				    user_id = rs.getLong(1);
				}
			} 
			
	        dc.close();
			System.out.println("cookie userid : " +user_id);
			System.out.println("cookie iambdt : " + iambdt);
// 			HttpSession session = request.getSession();
		    req.setAttribute(iambdt, user_id);
			chain.doFilter(req, res);
			if (requri.startsWith("/api/") == false) {
			    CookieManage cm = new CookieManage();
    			Cookie cookie = new Cookie("iambdt",cm.changeCookie(iambdt));
    		//	cookie.setPath("/");
    		//	cookie.setSecure(true);
    		    cookie.setHttpOnly(true);
    		    response.addCookie(cookie);
    			req.removeAttribute(iambdt);
    		}
		} catch (Exception e) {
		    if (dc != null ){
		        dc.close();
		    }
		    System.out.println("cookie Filter error : "+e.getMessage());
		    response.sendRedirect("/JSP/landingPage.jsp");
		}
	}

	
	private long getUserId(String authtoken){
	    	String query = "select user_id from auth_management where auth_token=?";
		try {
			dc.stmt = (dc.conn).prepareStatement(query);
			dc.stmt.setString(1,authtoken);
			ResultSet rs = dc.stmt.executeQuery();
			long user_id = -1l; 
			if(rs.wasNull()){
			    return -1l;
			}
			while(rs.next()){
			    user_id = rs.getLong(1);
			}
			return user_id;
		} catch (Exception e) {
		    return -1l;
		}
	}
}
