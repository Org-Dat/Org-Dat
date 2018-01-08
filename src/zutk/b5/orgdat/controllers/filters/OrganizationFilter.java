/**
 * This Filter is used to Check organization permission
 * 
 * @author : Obeth Samuel & Ponkumar
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class OrganizationFilter extends HttpServlet implements Filter {
	protected DatabaseConnection dc;
	RoleChecker roleFinder;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	    
	}
    /**
     * This method used to check organization permission.
     * 
     * @params : ServletRequest req, ServletResponse res, FilterChain chain
     * 
     * @return type : void 
     * 
     * @return : this method doesn\"t return any thing
     */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		roleFinder = new RoleChecker(request);
		PrintWriter out = response.getWriter();
		try {
			String requri = request.getRequestURI().substring(1);
			long user_id;
			if (requri.startsWith("api/") == true) {
				String authtoken = request.getHeader("Authorization");
				user_id = roleFinder.getUserId(authtoken);
			}else {
			    Cookie[] cookies = request.getCookies();
				user_id = roleFinder.getUserId(cookies);
			}
			System.out.println(user_id);
			if (user_id == -1) {
				throw new Exception();
			}
			dc = new DatabaseConnection("postgres", "postgres", "");

			String org_name = request.getParameter("org_name");
			if (org_name == null || org_name.matches("^[a-z][a-z0-9]{3,30}$") == false) {
				throw new Exception();
			}
			String role = roleFinder.orgRole(org_name, user_id);
			System.out.println("role  = "+role);
			if (role.equals("owner") || request.getRequestURI().endsWith("createOrg")) {
				chain.doFilter(req, res);
				dc.conn.close();
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
		    e.printStackTrace();
		    out.write("{\"status\":403 ,\"message\":\"Forbidden\"}");
		} finally {
		    if (dc != null ){
		        dc.close();
		    }
		}

	}

}
