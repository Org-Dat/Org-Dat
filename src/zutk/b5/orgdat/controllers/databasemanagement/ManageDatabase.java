/**
 * This servlet manage database working process
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.databasemanagement;

import java.io.PrintWriter;
import javax.servlet.http.*;
import zutk.b5.orgdat.model.orgmanagement.*;
import zutk.b5.orgdat.model.databasemanagement.*;
import zutk.b5.orgdat.controllers.orgmanagement.ManageMember;
import java.sql.SQLException;
import zutk.b5.orgdat.controllers.filters.*;
import java.sql.ResultSet;

public class ManageDatabase extends HttpServlet {
	PrintWriter out;
	DatabaseConnection dc;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			if (request.getMethod().toLowerCase().equals("post")) {
				doPost(request, response);
			} else if ( request.getRequestURI().substring(1).equals("getDBSharedMembers") && request.getMethod().toLowerCase().equals("get")){
			    doGet(request, response);
			} else {

				out.write("{'status':405,'message':'this post only url'}");
			}
		} catch (Exception e) {
			out.write("{'status':405,'message':'this post only url'}");
		}
	}

@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			out = response.getWriter();
			ManageDB dbManage = new ManageDB();
// 			RoleChecker rc = new RoleChecker(request);
			DashboardView dv = new DashboardView(request);
			String path = request.getRequestURI().substring(1);
			long user_id = dv.rc.getUserId(request.getCookies());
			System.out.println("mo user _ get  id :" + user_id);
			String org_name = request.getParameter("org_name");
			String db_name = request.getParameter("db_name");
			if (org_name.equals("postgres") || org_name.equals("orgdat")) {
				out.write("{\"status\" : 406 ,\"message\" : \"Organization Name already Exist\"}");
				if (dc != null) {
					dc.close();
				}
				return;
				// throw new Exception();
			}
// 			long org_id = dc.getOrgId(org_name);
			long db_id = dc.getDBId(dc.getOrgId(org_name), org_name+"_"+db_name);
			if (dv.getRole(org_name, db_name, user_id).contains("owner") == false) {
				out.write("{\"status\" : 403,\"message\" : \"Permission denied \"}");
				if (dc != null) {
					dc.close();
				}
				return;
				// throw new Exception();
			}
			System.out.println("out of the check ");
// 			ArrayList<String> ClientResponse = orgManage.getSharedMembers(org_id);
			out.write( dbManage.getSharedMembers(db_id) );
		} catch (Exception e) {
			System.out.println("Manage organi : " + e.getMessage());
			out.write("{\"status\" : 406 ,\"message\" : \"Organization Name already Exist\"}");
		}
	}
// 	 public boolean isVaildOrgForHim(long user_id,long db_id){
// 	     try {
// 			String sql = "select * from db_management where user_id=? and db_id=?";
// 			dc.stmt = dc.conn.prepareStatement(sql);
// 			dc.stmt.setLong(1, user_id);
// 			dc.stmt.setLong(2, db_id);
// // 			dc.stmt.setString(2, db_name);
// 			ResultSet rs = dc.stmt.executeQuery();
// 			    System.out.println("rs.wasNull()1");
// 			System.out.println(rs.wasNull());
// 			while (rs.next()){
// 			    System.out.println("rs.wasNull()2");
// 			    return true;
// 			}
// 			    System.out.println("rs.wasNull()3");
//             return false;
//         } catch (Exception e) {
//             e.printStackTrace();
//              return false;
//         }
//     }
	/**
	 * This method split manage database work process
	 * 
	 * @params : HttpServletRequest request , HttpServletResponse response
	 * 
	 * @return : if user give valid data it return detail object else return
	 *         error object
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			RoleChecker rc = new RoleChecker(request);
			long user_id = -1;
			String requri = request.getRequestURI();
			if (requri.startsWith("/api/")) {
				requri = requri.substring(5);
				user_id = rc.getUserId(request.getHeader("Authorization"));
			} else {
				user_id = rc.getUserId(request.getCookies());
			}
			ManageDB dbManage = new ManageDB();

			String db_name = request.getParameter("db_name");
			dc = new DatabaseConnection("postgres", "postgres", "");
			if (dbManage.isCorrect(db_name) == true) {
				try {
					String reqURI = request.getRequestURI();
					String org_name = request.getParameter("org_name");
					String isCorrect = "";
					if (reqURI.endsWith("createDB")) {
						if (checkCount(org_name) == false) {
							out.write("{'status' : 406, 'message' : 'Your Organization Database Count Already reach maximun count. '}");
							if (dc != null) {
								dc.close();
							}
							return;
							// throw new Exception();
						}
						isCorrect = dbManage.createDB(user_id, db_name,
								org_name);
						System.out.println("is correct : " + isCorrect);
					} else if (reqURI.endsWith("renameDB")) {
						String rename = request.getParameter("rename");
						if (dbManage.isCorrect(rename) == true) {
							isCorrect = dbManage.renameDB(db_name, org_name,
									rename);
						} else {
							out.write("{'status' : 400,'message' : 'Your Database Name Is InCorrect'}");

							if (dc != null) {
								dc.close();
							}
							return;
						}
					} else if (reqURI.endsWith("deleteDB")) {
						isCorrect = dbManage.deleteDB(db_name, org_name);
						System.out.println("is correct : " + isCorrect);
					} else if (reqURI.endsWith("shareDB")) {
						Share share = new Share();
						String role = request.getParameter("role");
						String query = request.getParameter("isRole");
						ManageMember mm = new ManageMember();
				System.out.println("EMAIL   ID = "+request.getParameter("email"));
				user_id = mm.getUser_id(request.getParameter("email"));
				System.out.println("USER   ID = "+user_id);
						if (share.shareDB(org_name, db_name, role, user_id,
								query) == true) {
							out.write("{'status':200,'message':'Database  successfully shared'}");

							if (dc != null) {
								dc.close();
							}
							return;
						} else {
							throw new Exception();
						}
					}
					if (isCorrect.equals("200")) {
						out.write("{'status':200,'message' : 'Database process successfully' ,'Organization Name' :'"
								+ org_name
								+ "','Database Name ':'"
								+ db_name
								+ "'}");

						if (dc != null) {
							dc.close();
						}
						return;
					} else {
						out.write(isCorrect);
					}

				} catch (Exception e) {
					out.write("{'status' : 400,'message' : 'Your input is InCorrect'}");
					if (dc != null) {
						dc.close();
					}
					return;
				} finally {
					try {
						if (dc != null) {
							dc.close();
						}
					} catch (Exception e) {
						System.out.println("Connection Not Close");
						return;
					}
				}

			} else {
				if (dc != null) {
					dc.close();
				}
				out.write("{'status' : 400,'message' : 'Your Database Name Is InCorrect'}");
				return;
			}
		} catch (Exception e) {
			if (dc != null) {
				dc.close();
			}
			out.write("{'status' : 400,'message' : 'Your input is InCorrect'}");
		}
	}

	/**
	 * This method check database count.
	 * 
	 * @params : String org_name
	 * 
	 * @return : if database count less than 5 it return true.else return false
	 */
	public boolean checkCount(String org_name) {
		try {
			String sql = "select count(db_id) from db_details where org_id=(select org_id from org_details where org_name = ?)";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, org_name);
			ResultSet rs = dc.stmt.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getLong(1));
				return rs.getLong(1) < 5;
			}
			System.out.println("00000000000");
			if (dc != null) {
				dc.close();
			}
			return true;
		} catch (Exception e) {
			if (dc != null) {
				dc.close();
			}
			return false;
		}
	}
}
