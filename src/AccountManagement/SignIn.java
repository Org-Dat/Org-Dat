package AccountManagement;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
public class SignIn extends HttpServlet{
	int correctCount = 0;
	static Jedis jedis = new Jedis("localhost");
	final String charecters = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        try{
            String[] res = {"Email Id is incorrect ", "Password is incorrect ","Email doesn't Exists ","incorrect Email (or) Password","SignIn Successfully " };
            String[] regexs = {"^[a-z]([\\._][a-z0-9]{3,50})@[a-z0-9]([\\._][a-z]{3,25})\\.[a-z]{2,5}$","^.{6,255}$" };
            String[] detail = {	"email", "password" };
            String[] userDetail = new String[2];
            for (String key : detail) {
				if (isCorrectValue(request.getParameter(detail[correctCount]),regexs[correctCount]) == false) {
					break;
				}
				userDetail[correctCount - 1] = request.getParameter(detail[correctCount]);
			}
			if (correctCount == 2) {
			    String email = userDetail[0];
			    if (isMember(email) == true){
			        correctCount++;
			        String password = jedis.hget(email,"password");
			        if (password.equals(userDetail[1]) == true ){
			            String junkCharecters  = getJunkCharecters();
			            Cookie c = new Cookie("",junkCharecters) ;
			            response.addCookie(c);
			            jedis.hset("cookie-details",junkCharecters,email);
			            correctCount++;
			        }
			    }
			}
			writer.write(res[correctCount]);
           
       } catch (Exception error){
           
           
       }
	}
	String getJunkCharecters(){
	    Random rand = new Random();
	    StringBuilder junkCharecters ;
	    String tmp;
	    while (true){
	        junkCharecters =new StringBuilder("");
	        for (int i = 0;i < 15;i++){
	            junkCharecters.append(charecters.charAt( rand.nextInt(charecters.length())));
	        }
	        tmp = jedis.hget("cookie-details",junkCharecters.toString());
	        if (tmp == null){
    	        return junkCharecters.toString();
	        }
	    }
	    
	}
	protected boolean isCorrectValue(String name, String regex) {
		try {
			boolean isCorrect = name.matches(regex);
			if (isCorrect) {
				correctCount++;
			}
			return isCorrect;
		} catch (Exception e) {
			return false;
		}
	}
	
	boolean isMember(String email) {
	    
		String userDetail = jedis.get("user_details");
		JSONObject jsonObj = new JSONObject(userDetail);
		Iterator<String> objectKeys = jsonObj.keys();
		while (objectKeys.hasNext()) {
			String storedMail = (String) objectKeys.next();
			if (storedMail.equals(email) == true){
			    return true;
			}
		}
		return false;

	}

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException {
	    
	}
}