package AccountManagement;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.json.*;
import redis.clients.jedis.Jedis;

public class Signup extends HttpServlet {
	int correctCount;
	String deta = "";
	static Jedis jedis = new Jedis("localhost");

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		correctCount = 1;
		PrintWriter out = response.getWriter();
		System.out.println(request.getParameter("first_name"));
		System.out.println(request.getQueryString());
		try {
			String[] res = { "User Name is incorrect",
					" Phone Number is incorrect ", "Email Id is incorrect ",
					"Password is incorrect ", "Email Already Exist ",
					"SignUp Successfully " };

			String[] regexs = { "[a-zA-Z]{3,255}", "^[+]?[7-9][0-9]{9,30}$",
					"^[a-z][a-z0-9]{5,30}@orgdat.com$", "^.{6,255}$" };
			String[] userDetail = new String[4];
			String[] detail = { "first_name", "last_name", "phone_number",
					"email", "password" };

			for (int i = 0; i < 4; i++) {
				if (isCorrectValue(request.getParameter(detail[i]), regexs[i]) == false) {
					break;
				}
				userDetail[correctCount - 1] = request
						.getParameter(detail[correctCount - 1]);
			}
			if(correctCount == 5){
			    if(isMember(userDetail[2])){
			        
			    }else{
			        
			    }
			}
			out.write(res[correctCount]);

		} catch (Exception e) {
			out.write("" + e);
		}

	}

	private boolean isMember(String email) {
		String userDetail = jedis.get("user_details");
		JSONObject jsonObj = new JSONObject(userDetail);
		if (jsonObj.get(email) != null) {
			return true;
		}
		return false;
	}

	private boolean isCorrectValue(String name, String regex) {
		try {
			boolean isCorrect = name.matches(regex);
			if (isCorrect == true) {
				correctCount++;
				deta = deta + name;
			}
			return isCorrect;
		} catch (Exception e) {
			return false;
		}
	}

}