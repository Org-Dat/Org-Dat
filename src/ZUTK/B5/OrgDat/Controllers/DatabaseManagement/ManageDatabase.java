package ZUTK.B5.OrgDat.Controllers.DatabaseManagement;

import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ZUTK.B5.OrgDat.Model.DatabaseManagement.*;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ManageDatabase extends HttpServlet {
	PrintWriter out;
	DatabaseConnection dc;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			String requri = request.getRequestURI();
			ManageDB dbManage = new ManageDB();
			if (requri.startsWith("/api")) {
				requri = requri.substring(requri.indexOf("/", 1));
			}
			String[] path = requri.split("/");
			String db_name = path[0];// request.getParameter("db_name");
			dc = new DatabaseConnection("postgres", "postgres", "");
			if (dbManage.isCorrect(db_name) == true) {
				try {
					String reqURI = path[2];// request.getRequestURI();
					String org_name = path[1];// request.getParameter("org_name");
					boolean isCorrect = false;
					if (reqURI.equals("$createDB")) {
						isCorrect = dbManage.createDB(db_name, org_name);
					} else if (reqURI.equals("$renameDB")) {
						String rename = request.getParameter("db_rename");
						if (dbManage.isCorrect(rename) == true) {
							isCorrect = dbManage.renameDB(db_name, org_name,
									rename);
						}

					} else if (reqURI.equals("$deleteDB")) {
						isCorrect = dbManage.deleteDB(db_name, org_name);
					}
					if (isCorrect == true) {
						out.write("{'status':200,'org_name':'" + org_name
								+ "','db_name':'" + db_name + "'}");
					} else {
						out.write("{'status':404,'message':'Not Found'}");
					}

				} catch (Exception e) {
					out.write("{'status':400,'message':'Bad Reuest'}");
				} finally {
					try {
						dc.conn.close();
					} catch (SQLException e) {
						System.out.println("Connection Not Close");
					}
				}

			}
		} catch (Exception e) {

		}
	}

	public boolean checkCount(String org_name) {
		try {
			String sql = "select count(db_name) from db_management where org_name=?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, org_name);
			ResultSet rs = dc.stmt.executeQuery();
			while(rs.next()){
			    return rs.getLong(1) < 5;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
