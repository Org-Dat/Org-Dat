package Filters;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CookieFilter extends HttpServlet implements Filter {
	public long user_id;
	protected DatabaseConnection dc;
	protected PreparedStatement stmt = null;
	protected RoleChecker rc = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		rc = new RoleChecker();

		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 1) {
			response.sendRedirect("/JSP/landingPage.jsp");
		}
		String iambdt = "";
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			String requri = request.getRequestURI();
			if (requri.startsWith("/api")) {
				String authtoken = request.getHeader("Authorization");
				String mail = request.getParameter("mail");
				String password = request.getParameter("password");
				long user_id = dc.getUserId(mail, password);
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
				Integer user_id = 0;
				if (rs == null) {
					throw new Exception();
				}
				while (rs.next()) {
					user_id = rs.getInt(1);
				}
			}
			ServletContext context = getServletContext();
			context.setAttribute(iambdt, user_id);
			chain.doFilter(req, res);
			context.removeAttribute(iambdt);
			dc.conn.close();
		} catch (Exception e) {
			response.sendRedirect("/JSP/landingPage.jsp");
		}
	}

	private void changeCookie(String iambdt) {
		String query = "update cookie_management set cookie=? where cookie=?";
		try {
			stmt = (dc.conn).prepareStatement(query);
			stmt.setString(1, dc.createJunk(20));
			stmt.setString(2, iambdt);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
