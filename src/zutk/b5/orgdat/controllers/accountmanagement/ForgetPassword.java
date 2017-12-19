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
			ForgetPass fg = new ForgetPass();
			if (request.getRequestURI().endsWith("setPassword")) {
				String password = request.getParameter("password");
				if(password.matches("^.{6,255}$") == false){
				     out.write("Invalid Password");
				     return;
				}
				HttpSession session = request.getSession();
				String email = (String) session.getAttribute("email");
				System.out.println("EMAIL == " +email) ;
				if(fg.setPassWord(email, password)){
				    out.write("success");
				    return;
				}else{
				    out.write("Invalid Password");
				    return;
				}
			} else {
				String email = request.getParameter("email");
				if (email != null
						&& email.matches("^[a-z][a-z0-9]{5,30}@[a-z][a-z0-9]{3,25}.com$")) {
					
					if (request.getRequestURI().endsWith("getQuestion")) {
						long user_id = fg.getUserId(email);
						String question = fg.getQuestion(user_id);
						System.out.println(question + " =  Question ");
						if (question == null) {
							out.write("Invalid Email Address}");
							return;
						} else {
							out.write(question);
							return;
						}
					} else {
						String question = request.getParameter("question");
						String answer = request.getParameter("answer");
						if (question != null && answer != null
								&& fg.isAnswer(question, answer, email) != -1) {
							HttpSession session = request.getSession();
							session.setAttribute("email",email);
							out.write("success");
							// out.write("{'status':200,'message':'answer is correct'}");
						} else {
							throw new Exception();
						}
					}
				} else {
					throw new Exception();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.write(" {'status':400,'message':'invaild answer'} ");
		}
	}
}