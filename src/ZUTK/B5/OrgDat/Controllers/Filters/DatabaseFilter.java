/**
 *  This filter used to check database filter permission.
 * 
 * @author : Obeth Samuel & Ponkumar
 * 
 * @version : 1.0
 */
package ZUTK.B5.OrgDat.Controllers.Filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DatabaseFilter extends HttpServlet implements Filter {
	protected DatabaseConnection dc;
	RoleChecker roleFinder;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
    /**
     * This  Method  check database permission .
     * 
     * @params : ServletRequest req, ServletResponse res, FilterChain chain
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
		roleFinder = new RoleChecker();
		PrintWriter out = response.getWriter();
		try {
			int count = 0;
			boolean isApi = false;
			String requri = request.getRequestURI().substring(1);
			String[] path = requri.split("/");
			long user_id;
			if (path[0].equals("api") == true) {
				count += 1;
				String authtoken = request.getHeader("authtoken");
				user_id = roleFinder.getUserId(authtoken);
			}else{
			    Cookie[] cookies = request.getCookies();
			    user_id = roleFinder.getUserId(cookies);
			}
			if (user_id == -1) {
				throw new Exception();
			}
			dc = new DatabaseConnection("postgres", "postgres", "");
			String org_name = path[count];// request.getParameter("org_name");
			String db_name = path[count + 1];// request.getParameter("db_name");
			if (org_name == null || org_name.matches("^[a-z][a-z0-9]{3,25}$")== false || db_name == null || db_name.matches("^[a-z][a-z0-9]{3,25}$") == false) {
				throw new Exception();
			}
			String role = roleFinder.dbRole(org_name, db_name, user_id);
			if (role == null) {
				role = roleFinder.orgRole(org_name, user_id);
				if (role.contains("owner")) {
					chain.doFilter(req, res);
					dc.conn.close();
				} else {
					throw new Exception();
				}
			} else if (role.contains("owner")) {
				chain.doFilter(req, res);
				dc.conn.close();
			} else {
				throw new Exception();
			}

		} catch (Exception e) {
			out.write("{'status':403 ,'message':'Forbidden'}");
		} finally {
			try {
				dc.conn.close();
			} catch (Exception er) {
				System.out.println("Connection Not close");
			}
		}
	}
}
