/**
 * This servlet managing user forget password processing work
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.accountmanagement;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
import zutk.b5.orgdat.model.accountmanagement.*;

public class ForgetPassword extends HttpServlet {
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
	 * This void method mnaging forget password processing
	 * 
	 * @params : HttpServletRequest request,HttpServletResponse resnpose
	 * 
	 * @return : if user give incorrect data it return error object .else return
	 *         success object
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			String email = request.getParameter("email");
			if (email != null
					&& email.matches("^[a-z][a-z0-9]{5,30}@[a-z][a-z0-9]{3,25}.com$")) {
				String question = request.getParameter("question");
				String answer = request.getParameter("answer");
				ForgetPass fg = new ForgetPass();
				if (question != null && answer != null
						&& fg.isAnswer(question, answer, email) != -1l) {
					out.write("{'status':200,'message':'answer is correct'}");
				} else {
					throw new Exception();
				}
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			out.write(" {'status':400,'message':'invaild answer'} ");
		}
	}
}