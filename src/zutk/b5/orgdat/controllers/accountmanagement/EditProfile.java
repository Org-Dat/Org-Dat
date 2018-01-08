/**
 *This servlet managing profile edit details
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */
//zutk.b5.orgdat.controllers.accountmanagement
package zutk.b5.orgdat.controllers.accountmanagement;
import zutk.b5.orgdat.controllers.filters.RoleChecker;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import zutk.b5.orgdat.model.accountmanagement.*;

public class EditProfile extends HttpServlet {
	PrintWriter out;

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
	 * This method update user profile detail
	 * 
	 * @params : HttpservletRequest request ,HttpServletResponse response
	 * 
	 * @return : if user give correct detail it return 'update successfully'
	 *         else return 'invaild input'
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			EditProFile ep = new EditProFile();
			RoleChecker rc = new RoleChecker(request);
			Cookie[] cookies = request.getCookies();
			long user_id = rc.getUserId(cookies);
			if (user_id == -1l) {
				throw new Exception();
			}
			if (request.getRequestURI().endsWith("/getProFile")) {
				ArrayList<String> detailsArray = ep
						.getDeets(user_id);
				String details = "";
				for (String value : detailsArray) {
					details += ",\"" + value + "\"";
				}
				if (detailsArray.size() < 3) {
					out.write("some error occrence ");
					return;
				} else {
					out.write("[" + details.substring(1)
							+ "]");
				}
			} else if (request.getRequestURI().endsWith(
					"/editProFile")) {
				String column_name = request
						.getParameter("name");
				ArrayList<String> columns = new ArrayList<String>();
				columns.add("user_name");
				columns.add("user_phone");
				columns.add("country");
				String value = request.getParameter("value");
				if (columns.contains(column_name) == false
						|| value == null
						|| value.equals("")
						|| ("user_phone".equals(column_name) && value
								.matches("^[+]?[0-9]{10,30}$") == false)
						|| ("user_phone".equals(column_name) == false && value
								.matches("^[a-zA-Z][a-zA-Z0-9]{3,255}$") == false)) {
					throw new Exception();
				}
				boolean isSend = ep.updateDeets(column_name,
						value, user_id);
				if (isSend == true) {
					out.write("Update Successfully");
				} else {
					throw new Exception();
				}
			}
		} catch (Exception e) {
			out.write("Invaild Inputs");
		}
	}
}