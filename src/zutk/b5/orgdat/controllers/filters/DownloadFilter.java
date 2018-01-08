package zutk.b5.orgdat.controllers.filters;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DownloadFilter implements Filter {
	protected DatabaseConnection dc;
	RoleChecker roleFinder;
	PrintWriter writer;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	/**
	 * This method used to check table permission
	 * 
	 * @params : ServletRequest req, ServletResponse res,FilterChain chain
	 * 
	 * @return type : void
	 * 
	 * @return : This method doesn't return any thing
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
			HttpServletResponse response = (HttpServletResponse) res;
		try {
			HttpServletRequest request = (HttpServletRequest) req;
			roleFinder = new RoleChecker(request);
			long user_id = roleFinder.getUserId(request.getCookies());
			String db_name = request.getParameter("db_name");
			String org_name = request.getParameter("org_name");
			String table_name = request.getParameter("table_name");
			String role = roleFinder.orgRole(org_name, user_id);
			// check owner are not
			if (role.equals("owner") == false) {
				throw new Exception();
			}
			dc = new DatabaseConnection("postgres", "postgres", "");
			long org_id = dc.getOrgId(org_name);
			long db_id = dc.getDBId(org_id, org_name+"_"+db_name);
			if (db_id <= 0) {
				throw new Exception();
			}
			if (request.getRequestURI().endsWith("downloadDB")) {
				System.out.println("downloadDB");
				chain.doFilter(req, res);
			} else if (request.getRequestURI().endsWith("downloadTable")) {
				long table_id = dc.getTableId(org_id, db_id, table_name);
				
				if (table_id <= 0) {
					throw new Exception();
				}
				chain.doFilter(req, res);
			} else {
				throw new Exception();
			}
			chain.doFilter(req, res);
		} catch (Exception e) {
			writer = response.getWriter();
			
			e.printStackTrace();
			
			writer.write("{'status':403,'message':'forbetten'}");
		} finally {
		    if (dc != null ){
		        dc.close();
		    }
		}

	}

	@Override
	public void destroy() {

	}
}

