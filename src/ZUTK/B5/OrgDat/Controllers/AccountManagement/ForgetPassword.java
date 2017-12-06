package ZUTK.B5.OrgDat.Controllers.AccountManagement;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
import ZUTK.B5.OrgDat.Model.AccountManagement.*;

public class ForgetPassword extends HttpServlet {
	PrintWriter out;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			String email = request.getParameter("email");
			if (email.matches("^[a-z][a-z0-9]{5,30}@orgdat.com$")) {
                String question  = request.getParameter("question");
                String answer = request.getParameter("answer");
                ForgetPass fg = new ForgetPass();
                if(question != null && answer != null && fg.isAnswer(question,answer,email)){
                    out.write("");
                }else{
                    out.write("");
                }
			}
		} catch (Exception e) {
            out.write(" invaild answer ");
		}
	}
}