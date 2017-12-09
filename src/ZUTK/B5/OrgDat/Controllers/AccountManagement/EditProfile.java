/**
 *This servlet managing profile edit details
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */
package ZUTK.B5.OrgDat.Controllers.AccountManagement;

import java.io.*;
import javax.servlet.http.*;
import ZUTK.B5.OrgDat.Model.AccountManagement.*;

public class EditProfile extends HttpServlet {
	PrintWriter out;
    /**
     * This method update user  profile detail
     * 
     * @params : HttpservletRequest request ,HttpServletResponse response
     * 
     * @return : if user give correct detail it return 'update successfully' else return 'invaild input' 
     */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			EditProFile ep = new EditProFile();
			String column_name = request.getParameter("name");
			if(column_name == null || column_name.matches("[a-z][a-z0-9]{3,25}$") == false){
			    throw new Exception();
			}
			String value = request.getParameter("value");
			Cookie[] cookies = request.getCookies();
			long user_id = ep.getUserId(cookies);
			if(user_id == -1l){
			    throw new Exception();
			}
			boolean isSend = ep.updateDeets(column_name, value, user_id);
            if(isSend == true){
                out.write("Update Successfully");
            }else{
                throw new Exception();   
            }
		} catch (Exception e) {
			out.write("Invaild Inputs");
		}
	}
}