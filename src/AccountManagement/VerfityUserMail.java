package AccountManagement;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.http.Cookie;
import java.io.*;
import java.net.*;
import java.util.*;
import redis.clients.jedis.Jedis;
import org.json.*;
import java.sql.*;
public class VerfityUserMail extends HttpServlet {
	private final String url = "jdbc:postgresql://localhost/user_detail";
	private final String user = "postgres";
	private final String password = "";
	private JSONObject userDetail;
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		Jedis jedis = new Jedis("localhost");
		try {
			String email = request.getParameter("email");
			String otp = jedis.get(email);
			userDetail = new JSONObject(jedis.get(otp));
			userDetail = (JSONObject)userDetail.get(otp);
			String userInputOTP = request.getParameter("OTP");
			if (otp.equals(userInputOTP) &&(userDetail.get("email")).equals(email)) {
				jedis.expire(jedis.get(otp),-1);
			//	jedis.expire(); 
				Random rand = new Random();
				int randomNumber = rand.nextInt(1000000000);
				Cookie cookies = new Cookie("sid_id", randomNumber + "");
				cookies.setPath("/");
				response.addCookie(cookies);			
				RequestDispatcher rd = request.getRequestDispatcher("/JSP/userHomePage.jsp");
				rd.forward(request, response);
	        	} else {
		        	out.write("Incorrect Verfication Code. If You not receive the code Click Resend Button and Get the Verfication Code");
	        	}
		}catch(Exception e){
 			out.write("Incorrect OTP Code ");
        	 }
	}

	private boolean addUserToDB() throws ClassNotFoundException{
		Connection conn = null;
		PreparedStatement stmt = null;
       		 try {
		    Class.forName("org.postgresql.Driver");
            	    conn = DriverManager.getConnection(url, user, password);
		}catch(SQLException sqlEx){
		    return false;   
		}catch(ClassNotFoundException e){
		    return false;
		}
		try {
		 //  stmt = conn.createStatement();
		   stmt=conn.prepareStatement("insert into user_detail(user_name,email,phone_number,password) values(?,?,?,?)");
		   stmt.setString(3,(String) userDetail.get("phone_number"));
		   stmt.setString(1,(String) userDetail.get("user_name"));
		   stmt.setString(2,(String) userDetail.get("email"));
		   stmt.setString(4,(String) userDetail.get("password")); 
		   stmt.executeUpdate();  
		   conn.close();   
		}catch(Exception e){
		    return false;
		}
		return true;
	}
}