/**
 *This servlet managing profile edit details
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 *///zutk.b5.orgdat.controllers.accountmanagement
package zutk.b5.orgdat.controllers.accountmanagement;
import zutk.b5.orgdat.controllers.filters.RoleChecker;
import java.io.*;
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
			String column_name = request.getParameter("name");
			if (column_name == null
					|| column_name.matches("[a-z][a-z0-9]{3,25}$") == false) {
				throw new Exception();
			}
			String value = request.getParameter("value");
			Cookie[] cookies = request.getCookies();
			long user_id = rc.getUserId(cookies);
			if (user_id == -1l) {
				throw new Exception();
			}
			boolean isSend = ep.updateDeets(column_name, value, user_id);
			if (isSend == true) {
				out.write("Update Successfully");
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			out.write("Invaild Inputs");
		}
	}
}