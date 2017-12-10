package ZUTK.B5.OrgDat.Controllers.Filters;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DownloadFilter implements Filter {
	protected DatabaseConnection dc;
	RoleChecker roleFinder;
	PrintWriter out;

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
			roleFinder = new RoleChecker();
			out = response.getWriter();
			long user_id = roleFinder.getUserId(request.getCookies());
			String[] path = (request.getRequestURI().substring(1)).split("/");
			String db_name = path[1];
			String org_name = path[0];
			String role = roleFinder.orgRole(org_name, user_id);
			if (role.equals("owner") == false) {
				throw new Exception();
			}
			dc.stmt = dc.conn
					.prepareStatement("select db_name from db_management where org_name =?");
			dc.stmt.setString(1, org_name);
			ResultSet rs = dc.stmt.executeQuery();
			boolean valid = false;
			while (rs.next()) {
				if (db_name.equals(rs.getString(1))) {
					valid = true;
					break;
				}
			}
			if (valid == false) {
				throw new Exception();
			}
			if (path.length == 3) {
				chain.doFilter(req, res);
			} else if (path.length == 4) {
				dc.stmt = dc.conn
						.prepareStatement("select table from table_management where org_name =? and db_name=?");
				dc.stmt.setString(1, org_name);
				dc.stmt.setString(2, path[2]);
				rs = dc.stmt.executeQuery();
				valid = false;
				while (rs.next()) {
					if (path[2].equals(rs.getString(1))) {
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

			out.write("{'status':403,'message':'forbetten'}");
		}

	}

	@Override
	public void destroy() {

	}
}