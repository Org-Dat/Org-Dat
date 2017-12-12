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
		try {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			roleFinder = new RoleChecker(request);
			writer = response.getWriter();
			long user_id = roleFinder.getUserId(request.getCookies());
			String db_name = request.getParameter("db_name");
			String org_name = request.getParameter("org_name");
			String table_name = request.getParameter("table_name");
			String role = roleFinder.orgRole(org_name, user_id);
			// check owner are not
			if (role.equals("owner") == false) {
				throw new Exception();
			}
			dc.stmt = dc.conn
					.prepareStatement("select db_name from db_management where org_name =?");
			dc.stmt.setString(1, org_name);
			ResultSet rs = dc.stmt.executeQuery();
			boolean valid = false;
			// check vaild database are not
			while (rs.next()) {
				if (db_name.equals(rs.getString(1))) {
					valid = true;
					break;
				}
			}
			if (valid == false) {
				throw new Exception();
			}
			if (request.getRequestURI().equals("downloadDB")) {
				chain.doFilter(req, res);
			} else if (request.getRequestURI().equals("downloadTable")) {
				dc.stmt = dc.conn
						.prepareStatement("select table from table_management where org_name =? and db_name=?");
				dc.stmt.setString(1, org_name);
				dc.stmt.setString(2, db_name);
				rs = dc.stmt.executeQuery();
				valid = false;
				while (rs.next()) {
					if (table_name.equals(rs.getString(1))) {
						valid = true;
						break;
					}
				}
				if (valid == false) {       
					throw new Exception();
				}
				chain.doFilter(req, res);
			} else {
				throw new Exception();
			}
			chain.doFilter(req, res);
		} catch (Exception e) {

			writer.write("{'status':403,'message':'forbetten'}");
		}

	}

	@Override
	public void destroy() {

	}
}