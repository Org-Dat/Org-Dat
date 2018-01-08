/**
 * This filter check table permission.
 * 
 * @author : Obeth & Ponkumar 
 * 
 * @version : 1.0 
 */
package zutk.b5.orgdat.controllers.filters;

import zutk.b5.orgdat.model.databasemanagement.ShowDetails;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;

public class TableFilter extends DownloadFilter {
	protected DatabaseConnection dc;
	RoleChecker roleFinder;
	PrintWriter out;

	/**
	 * This method used to check table permission
	 * 
	 * @params : ServletRequest req, ServletResponse res,FilterChain chain
	 * 
	 * @return type : void
	 * 
	 * @return : This method doesn\"t return any thing
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		try {
			System.out.println("table filter ");
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			
			if (request.getRequestURI().endsWith("downloadDB")) {
				System.out.println("downloadDB");
				chain.doFilter(req, res);
				//return;
			} else {
			roleFinder = new RoleChecker(request);
			out = response.getWriter();
			String requri = request.getRequestURI();
			String org_name = request.getParameter("org_name");
			String db_name = request.getParameter("db_name");
			String table_name = request.getParameter("table_name");
			long user_id;
			if (requri.contains("/api/")) {
				String authtoken = request.getHeader("Authorization");
				user_id = roleFinder.getUserId(authtoken);
			} else {
				Cookie[] cookies = request.getCookies();
				user_id = roleFinder.getUserId(cookies);
			}

			if (user_id == -1) {
				throw new Exception();
			}
			System.out.println(" table filter user_id :  " + user_id);
			if (requri.endsWith("/downloadTable")) {
				super.doFilter(req, res, chain);
			} else {
				dc = new DatabaseConnection("postgres", "postgres", "");
				System.out.println(" t db connected");
				// if (request.getRequestURI().endsWith("/createTable") == false) {
				// 	if (vaildCheck(user_id, org_name, db_name, table_name, "owner") == false) {
				// 		throw new Exception();
				// 	}
				// } else {
				// 	if (vaildCheck(user_id, org_name, db_name, "owner") == false
				// 			|| table_name.matches("^[a-z][a-z0-9]{3,30}$") == false) {
				// 		throw new Exception();
				// 	}
				// }

				System.out.println("Before Role Checker");
				String role = roleFinder.tableRole(org_name, db_name,
						table_name, user_id);
				/**************** it is for rights ********************/
				System.out.println("Table Role  : " + role);
				boolean rights = false;
				if (role == null) {
					role = roleFinder.dbRole(org_name, db_name, user_id);
					System.out.println("DB Role  : " + role);
					if (role == null) {
						role = roleFinder.orgRole(org_name, user_id);
						System.out.println("Org Role  : " + role);
						if (role.contains("owner")) {
					//		request.setAttribute("role", "org_" + role);
							rights = true;
						}
					} else if (role.contains("owner")
							|| role.equals("can/write")) {

					//	request.setAttribute("role", "db_" + role);
						rights = true;
					} else if (role.equals("read only")) {
			//			request.setAttribute("role", "db_" + role);
						if (requri.endsWith("/readRecord")) {
							rights = true;
						}
					}
				} else if (role.equals("can/write")) {
			//		request.setAttribute("role", "table_" + role);
					if (requri.endsWith("/renameTable")
							|| requri.endsWith("/readRecord") ) {
						rights = true;
					}
				} else if (role.contains("owner")) {
			//		request.setAttribute("role", "table_" + role);
					rights = true;
				}
				// readRecord
				else if (role.equals("read only")) {
			//		request.setAttribute("role", "table_" + role);
					if (requri.endsWith("/readRecord")) {
						rights = true;
					}
				}
				if (rights == true) {
					System.out.println();
					if (requri.endsWith("/readRecord")
							|| requri.endsWith("/shareTable") || requri.endsWith("/downloadTable")
							|| requri.endsWith("/createTable")) {
						chain.doFilter(req, res);
						dc.conn.close();
					} else if (requri.endsWith("/deleteTable")) {
						System.out.println("table dfgdg");
						chain.doFilter(req, res);
						dc.conn.close();
					} else {
					    long org_id = dc.getOrgId(org_name);
					    long db_id  = dc.getDBId(org_id,org_name + "_" + db_name);
					    long table_id = dc.getTableId(org_id,  db_id, table_name);
						System.out.println("lock ender");
						dc.stmt = dc.conn
								.prepareStatement("select user_id from lock_management where org_id =? and db_id =? and table_id =?");
						dc.stmt.setLong(1, org_id);
						dc.stmt.setLong(2, db_id);
						dc.stmt.setLong(3, table_id);
						ResultSet rs = dc.stmt.executeQuery();
						System.out.println("lock user_id : ");
						if (rs.next()) {
							System.out.println("lock user_id : " + user_id);
							if ((long) (rs.getLong(1)) == user_id) {
								chain.doFilter(req, res);
								dc.conn.close();
							} else {
								dc.stmt = dc.conn
										.prepareStatement("select user_email form signup_detail where user_id =?");
								dc.stmt.setLong(1, user_id);
								rs = dc.stmt.executeQuery();
								out.write("{\"status\":200 ,\"message\":\""
										+ rs.getString(1)
										+ " was edit this page , so pleace !!\"}");
								if (dc != null) {
									dc.close();
								}
								return;
							}
						} else {
							if (requri.endsWith("/renameTable") == false) {
								dc.stmt = dc.conn
										.prepareStatement("insert into lock_management values(?,?,?,?,?)");
								dc.stmt.setLong(1, user_id);
								dc.stmt.setBoolean(2, false);
								dc.stmt.setLong(3, org_id);
								dc.stmt.setLong(4, db_id);
								dc.stmt.setLong(5, table_id);
								dc.stmt.executeUpdate();
							} else {
								System.out.println("table dfgdg");
								dc.close();
							}
							chain.doFilter(req, res);
						}
					}

				} else {
					out.write("{\"status\":403 ,\"message\":\"Permission denied\"}");
					if (dc != null) {
						dc.close();
					}
					return;
					// throw new Exception();
				}
			}}

		} catch (Exception e) {
			System.out.println("lock errror : " + e.getMessage());
			out.write("{\"status\":403 ,\"message\":\"Forbidden\"}");
			if (dc != null) {
				dc.close();
			}
			return;
		}

	}

	private boolean vaildCheck(long user_id, String org_name, String db_name,
			String role) {
		try {
			if (db_name == null) {
				throw new Exception();
			}
			ShowDetails shower = new ShowDetails();
			ArrayList<String> tem;
			tem = shower.getDatabase(org_name, user_id, role);
			System.out.println("GET DATABASE NAMES");
			System.out.println("databases 1 " + tem.toString());
			System.out.println("DATABASE ATT  " + db_name);
			if (tem.contains(db_name) == false) {
				System.out.println("some");
				throw new Exception();
			}
			if (dc != null) {
				dc.close();
			}
			return true;
		} catch (Exception e) {
			System.out.println(e);
			if (dc != null) {
				dc.close();
			}
			return false;
		}
	}

	private boolean vaildCheck(long user_id, String org_name, String db_name,
			String table_name, String role) {
		try {
			if (db_name == null || table_name == null) {
				throw new Exception();
			}
			ShowDetails shower = new ShowDetails();
			ArrayList<String> tem;
			tem = shower.getDatabase(org_name, user_id, role);
			System.out.println("databases 1 " + tem.toString());
			if (tem.contains(db_name) == false) {
				System.out.println("some");
				throw new Exception();
			}
			tem = shower.getTables(org_name, db_name, user_id, "");
			System.out.println("some1");
			System.out.println("tables " + tem);
			if (tem.contains(table_name) == false) {
				throw new Exception();
			}
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	private boolean editCheck(long user_id, String org_name, String db_name,
			String table_name) {
		try {
			dc.stmt = dc.conn
					.prepareStatement("select user_id,visible form lock_management where org_name =? and db_name =? and table_name =?");
			dc.stmt.setString(1, org_name);
			dc.stmt.setString(2, db_name);
			dc.stmt.setString(3, table_name);
			ResultSet rs = dc.stmt.executeQuery();
			boolean ans = false;
			while (rs.next()) {
				ans = (user_id == ((long) rs.getLong(1)) && rs.getBoolean(2));
			}
			if (dc != null) {
				dc.close();
			}
			return ans;
		} catch (Exception e) {
			if (dc != null) {
				dc.close();
			}
			return false;
		}
	}

}

/***
 * 
 * extends HttpServlet
 * 
 * 
 */
