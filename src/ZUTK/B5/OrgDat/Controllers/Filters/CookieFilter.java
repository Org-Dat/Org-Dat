/**
  * This filter used to check cookies.
  * 
  * @author : Obeth Samuel & Ponkumar
  * 
  * @version : 1.0
  */
  

package ZUTK.B5.OrgDat.Controllers.Filters;

import java.io.*;
import java.sql.*;
import javax.servlet.*; 
import javax.servlet.http.*;
import ZUTK.B5.OrgDat.Model.AccountManagement.CookieManage; 
public class CookieFilter extends HttpServlet implements Filter {
	public long user_id;
	protected DatabaseConnection dc;
	protected PreparedStatement stmt = null;
	protected RoleChecker rc = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

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
		rc = new RoleChecker();
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length <= 1) {
			response.sendRedirect("/JSP/landingPage.jsp");
		}
		String iambdt = "";
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			String requri = request.getRequestURI();
			if (requri.startsWith("/api/")) {
				String authtoken = request.getHeader("Authorization");
				if(authtoken == null){
				    throw new Exception();
				}
				long user_id = getUserId(authtoken);
				if(user_id == -1){
				    throw new Exception();
				}
				iambdt = authtoken;
			} else {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("iambdt")) {
						iambdt = cookie.getValue();
						break;
					}
				}
				String ipAddress = request.getRemoteAddr();
				String user_agent = request.getHeader("User-Agent");
				String query = "select user_id from cookie_management where user_agent = ? and ip_address = ? and iambdt_cookie = ?";
				stmt = dc.conn.prepareStatement(query);
				stmt.setString(1, user_agent);
				stmt.setString(2, ipAddress);
				stmt.setString(3, iambdt);
				ResultSet rs = stmt.executeQuery();
				long user_id = 0;
				if (rs == null) {
					throw new Exception();
				}
				while (rs.next()) {
					user_id = rs.getLong(1);
				}
			}
			ServletContext context = getServletContext();
			context.setAttribute(iambdt, user_id);
			chain.doFilter(req, res);
			if (requri.startsWith("/api/") == false) {
			    CookieManage cm = new CookieManage();
    			Cookie cookie = new Cookie("iambdt",cm.changeCookie(iambdt));
    			cookie.setPath("/");
    		//	cookie.setSecure(true);
    		    cookie.setHttpOnly(true);
    		    response.addCookie(cookie);
    			context.removeAttribute(iambdt);
			}
			dc.conn.close();
		} catch (Exception e) {
			response.sendRedirect("/JSP/landingPage.jsp");
		}
	}

	
	private long getUserId(String authtoken){
	    	String query = "select user_id from auth_management where auth_token=?";
		try {
			stmt = (dc.conn).prepareStatement(query);
			stmt.setString(1,authtoken);
			ResultSet rs = stmt.executeQuery();
			long user_id = -1l;
			if(rs.wasNull()){
			    return -1l;
			}
			while(rs.next()){
			    user_id = rs.getLong(1);
			}
			return user_id;
		} catch (SQLException e) {
		    return -1l;
		}
	}
}
