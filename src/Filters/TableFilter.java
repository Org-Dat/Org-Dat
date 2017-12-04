package Filters;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class TableFilter extends HttpServlet implements Filter {
	protected DatabaseConnection dc;
	RoleChecker roleFinder;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		roleFinder = new RoleChecker();
		PrintWriter out = response.getWriter();
		try {
			String requri = request.getRequestURI();
			String[] path = requri.split("/");
			int count = 0;
			boolean isApi = false;
			if (path[0].equals("api")) {
				count += 1;
				isApi = true;
			}
			HttpSession session = request.getSession();
			session.setAttribute("requestURI",
					requri.substring(requri.indexOf("/") + 1));
			Cookie[] cookies = request.getCookies();
			long user_id;
			if (isApi == true) {
				String authtoken = request.getHeader("authtoken");
				user_id = roleFinder.getUserId(authtoken);
			} else {
				user_id = roleFinder.getUserId(cookies);
			}
			if (user_id == -1) {
				throw new Exception();
			}
			dc = new DatabaseConnection("postgres", "postgres", "");

			String org_name = path[count];// request.getParameter("org_name");
			String db_name = path[count + 1];// request.getParameter("db_name");
			String table_name = path[count + 2];// request.getParameter("table_name");

			if (org_name == null) {
				throw new Exception();
			}
			String role = roleFinder.tableRole(org_name, db_name, table_name,
					user_id);
			if (role == null) {
				role = roleFinder.dbRole(org_name, db_name, user_id);
				if (role == null) {
					role = roleFinder.orgRole(org_name, user_id);
					if (role.contains("owner")) {
						chain.doFilter(req, res);
						dc.conn.close();
					} else {
						throw new Exception();
					}
				} else if (role.contains("owner") || role.equals("can/write")) {
					chain.doFilter(req, res);
					dc.conn.close();
				} else {
					throw new Exception();
				}
			} else if (role.equals("can/write")) {
				if (path[count + 3].contains("renameTable")) {
					chain.doFilter(req, res);
					dc.conn.close();
				} else {

				}
			} else {
				throw new Exception();
			}

		} catch (Exception e) {
			out.write("403.forbidden");
		} finally {
			try {
				dc.conn.close();
			} catch (Exception e) {
				System.out.println("Connection Not Close");
			}
		}

	}

}
