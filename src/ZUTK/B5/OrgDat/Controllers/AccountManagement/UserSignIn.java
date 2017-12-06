package ZUTK.B5.OrgDat.Controllers.AccountManagement;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import ZUTK.B5.OrgDat.Controllers.Filters.DatabaseConnection;
import ZUTK.B5.OrgDat.Model.AccountManagement.*;

public class UserSignIn extends HttpServlet {
	PrintWriter out;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			SignIn si = new SignIn();
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			if (email.matches("^[a-z][a-z0-9]{5,30}@orgdat.com$")) {
				if (password.matches("^.{6,255}$")) {
					if (si.checkUserAccount(email, password)) {
                        out.write("Signin Successfully");
					} else {
                        out.write("User Name Or Password is incorrect");
					}
				}else{
				   out.write("Paaword is incorrect"); 
				}
			}else{
			    out.write("Email Address is incorrect");   
			}
		} catch (Exception e) {

		}
	}
}