package zutk.b5.orgdat.controllers.filters;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import zutk.b5.orgdat.model.databasemanagement.ShowDetails;

public class LogFilter implements Filter {
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
			HttpServletResponse response = (HttpServletResponse) res;
		try {
			HttpServletRequest request = (HttpServletRequest) req;
			roleFinder = new RoleChecker(request);
			
			String org_name = request.getParameter("org_name");
			if (request.getRequestURI().endsWith("/getOrgName")
					|| request.getRequestURI().endsWith("/createOrg") ||request.getRequestURI().endsWith("/deleteOrg") ||request.getRequestURI().endsWith("/getDashBoardWithSize") ) {
				chain.doFilter(req, res);
			} else if (org_name == null) {
				out = response.getWriter();
				out.write("{\"status\":404,\"message\":\"org_name = "
						+ org_name + " Not Founded\"}");
				if (dc != null) {
					dc.close();
				}
				return;
			} else {
				long user_id = -1;
				if (request.getRequestURI().startsWith("/api/")) {
					user_id = roleFinder.getUserId(request
							.getHeader("Authorization"));
				} else {
					user_id = roleFinder.getUserId(request.getCookies());
				}
				if (user_id <= 0) {
					out = response.getWriter();
					out.write("{\"status\" :401 ,\"message\" : \"Invalid user\"}");
					if (dc != null) {
						dc.close();
					}
					return;
				}
				ShowDetails sh = new ShowDetails();
				System.out.println("log filterr ");
				String email = idToEmail(user_id);
				
				System.out.println("email : " + email);
				System.out.println("Id : " + user_id);
				if (email.endsWith("@" + org_name + ".com") == false
						&& email.endsWith("@orgdat.com") == false) {
					out = response.getWriter();
					out.write("{\"status\" :401 ,\"message\" : \"Invalid user\"}");
					if (dc != null) {
						dc.close();
					}
					return;
				}
				ArrayList<String> orgs = sh.getOrganization(user_id);
				if (orgs.contains(org_name) == false) {
					out = response.getWriter();
					out.write("{\"status\":400,\"message\":\" Your Organization Name (org_name = "
							+ org_name + " ) is incorect.\"}");
					if (dc != null) {
						dc.close();
					}
					return;
				}
				String date = new SimpleDateFormat("dd-MM-yyyy")
						.format(new java.util.Date());
				File logFile = new File("Log/"
						+ org_name + "/log_" + date + ".log");
				logFile.createNewFile();

				Date now = new Date();
				chain.doFilter(req, res);
				SimpleDateFormat dateFormatter = new SimpleDateFormat(
						"[dd/MMM/yyyy:kk:mm:ss Z]");
				FileWriter fw = new FileWriter(logFile, true); // the true will
																// append the
																// new data
				fw.write(email + " - - " + request.getRemoteAddr() + " - - "
						+ dateFormatter.format(now) + " \""
						+ request.getMethod() + " " + request.getRequestURI()
						+ " " + request.getProtocol() + "\" "
						+ response.getStatus() + " " + response.getBufferSize()
						+ "\n");// appends the string to the file
				fw.close();

				if (dc != null) {
					dc.close();
				}
			}
		} catch (Exception e) {
		//	e.printStackTrace();
			System.out.println(e);
			
			out = response.getWriter();
			out.write("{\"status\":403,\"message\":\"forbetten\"}");
			if (dc != null) {
				dc.close();
			}
			return;
			
		}

	}

	public String idToEmail(long user_id) {
		String email = null;
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			String sql = "select user_email from signup_detail where user_id = ?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				email = rs.getString(1);
			}
			if (dc != null) {
				dc.close();
			}
			return email;
		} catch (Exception e) {

			if (dc != null) {
				dc.close();
			}
			return null;
		}
	}
/*
 insert into signup_detail (user_name,user_email,user_password,user_phone,role) values('narayana1','narayana1@google.com','narayana1',9104837784,'member');
 insert into signup_detail (user_name,user_email,user_password,user_phone,role) values('narayana2','narayana2@google.com','narayana2',9204837784,'member');
 insert into signup_detail (user_name,user_email,user_password,user_phone,role) values('narayana3','narayana3@google.com','narayana3',9304837784,'member');
 insert into signup_detail (user_name,user_email,user_password,user_phone,role) values('narayana4','narayana4@google.com','narayana4',9404837784,'member');

*/
	@Override
	public void destroy() {

	}
}
