package ZUTK.B5.OrgDat.Controllers.AccountManagement;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import ZUTK.B5.OrgDat.Model.AccountManagement.*;

public class EditProfile extends HttpServlet {
	PrintWriter out;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			EditProFile ep = new EditProFile();
			String column_name = request.getParameter("name");
			String value = request.getParameter("value");
			Cookie[] cookies = request.getCookies();
			long user_id = ep.getUserId(cookies);
			boolean isSend = ep.updateDeets(column_name, value, user_id);
            if(isSend == true){
                out.write("Update Successfully");
            }else{
                out.write("Invaild Inputs");   
            }
		} catch (Exception e) {
			out.write("");
		}
	}
}