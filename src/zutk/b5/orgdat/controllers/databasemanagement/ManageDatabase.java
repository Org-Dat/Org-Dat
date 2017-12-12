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
import zutk.b5.orgdat.model.orgmanagement.Share;
import zutk.b5.orgdat.model.databasemanagement.*;
import java.sql.SQLException;
import zutk.b5.orgdat.controllers.filters.RoleChecker;
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
			} else {

				out.write("{'status':405,'message':'this post only url'}");
			}
		} catch (Exception e) {
			out.write("{'status':405,'message':'this post only url'}");
		}
	}

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
					    if(checkCount(org_name) == false){
					        out.write("{'status' : 406, 'message' : 'Your Organization Database Count Already reach maximun count. '}");
					        return;
					       // throw new Exception();
			 		    }
						isCorrect = dbManage.createDB(user_id, db_name,
								org_name);
						System.out.println("is correct : " + isCorrect);
					} else if (reqURI.endsWith("renameDB")) {
						String rename = request.getParameter("db_rename");
						if (dbManage.isCorrect(rename) == true) {
							isCorrect = dbManage.renameDB(db_name, org_name,
									rename);
						}else{
						     out.write("{'status' : 400,'message' : 'Your Database Name Is InCorrect'}");
			                 return;
						}
					} else if (reqURI.endsWith("deleteDB")) {
						isCorrect = dbManage.deleteDB(db_name, org_name);
						System.out.println("is correct : " + isCorrect);
					} else if (reqURI.endsWith("shareDB")) {
						Share share = new Share();
						String role = request.getParameter("role");
						String query = request.getParameter("isRole");
						if (share.shareDB(org_name, db_name, role, user_id,
								query) == true) {
							out.write("{'status':200,'message':'Database  successfully shared'}");
							return;
						} else {
							throw new Exception();
						}
					}
					if (isCorrect.equals("200")) {
						out.write("{'status':200,'message' : 'Database process successfully' ,'Organization Name' :'" + org_name
								+ "','Database Name ':'" + db_name + "'}");
						return;
					} else {
						out.write(isCorrect);
					}

				} catch (Exception e) {
					 out.write("{'status' : 400,'message' : 'Your input is InCorrect'}");
					 return;
				} finally {
					try {
						dc.conn.close();
					} catch (SQLException e) {
						System.out.println("Connection Not Close");
					    return;
					}
				}

			}else{
			    out.write("{'status' : 400,'message' : 'Your Database Name Is InCorrect'}");
			    return;
			}
		} catch (Exception e) {
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
			String sql = "select count(db_name) from db_management where org_name=?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, org_name);
			ResultSet rs = dc.stmt.executeQuery();
			
			while (rs.next()) {
			    System.out.println(rs.getLong(1));
				return rs.getLong(1) < 5;
			}
			System.out.println("00000000000");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
