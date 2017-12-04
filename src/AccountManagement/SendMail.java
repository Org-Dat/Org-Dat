package AccountManagement;

import java.io.*;
import java.net.*;
import java.util.*;
import redis.clients.jedis.Jedis;

public class SendMail {
	private String userDetail;

	public SendMail(String _userDetail) {
		this.userDetail = _userDetail;
	}
    public SendMail(){
       System.out.println("User Create No Argument Object ");
    }
	public String setMailConf(String email,int randomNumber) {
		try {
		
			String mailHTMLString = "<div style='max-width: 600px;background: #ddd;border: 1px solid gray;box-sizing: border-box;'><div style='background: #333;padding: 30px 25px;color: white; font: 20px sans-serif;border: 1px solid gray;box-sizing: border-box;'>Shadow Info-Deets</div><div style='padding: 23px;'><b>Dear Shadow Info-Deets  User,</b><p class='cnt' style='text-indent: 35px;line-height: 20px;'> This email address is being used to recover a Shadow Info-Deets  Account. If you initiated the recovery process, it is asking you to enter the numeric verification code that appears below.</p><p class='cnt' style='text-indent: 35px;line-height: 20px;'>If you did not initiate an account recovery process and have a Shadow Info-Deets  Account associated with this email address, it is possible that someone else is trying to access your account. Do not forward or give this code to anyone. Please visit your account's sign-in & security settings to ensure that your account is safe .</p><p style='text-align: center;font: 25px sans-serif;'> <b>  ~ </b></p><p style='line-height: 28px;'>Yours sincerely,<br>The Shadow Info-Deets Team</p></div></div>";
			mailHTMLString = mailHTMLString.replace(" ~", "" + randomNumber);
			return send(mailHTMLString, email, randomNumber);
		} catch (Exception e) {
			return "Please Enter The Correct Email Address ";
		}
	}

	public String verifyMail(String email) {
		try {
		    //URL url = new URL("http://");
		    String url = "";
		    String mailHTMLString = "<div style='max-width: 600px;background: #ddd;border: 1px solid gray;box-sizing: border-box;'><div style='background: #333;padding: 30px 25px;color: white; font: 20px sans-serif;border: 1px solid gray;box-sizing: border-box;'>Shadow Info-Deets</div> <div style='padding: 23px;'><b>Dear Shadow Info-Deets  User,</b><p style='text-indent: 35px;line-height: 20px;'>Your Shadow Info-Deets account details are still are not verified. Please click below to verify your email address.</p><br><a href="+url+" target='_blank'><button style='display: block;background-color: #333;height: 40px;width: 200px;outline: none;border : none;color: #fff;margin: auto;border-radius: 5px;'>CONFIRM EMAIL</button></a><br><p style='line-height: 28px;'>Yours sincerely,<br>The Shadow Info-Deets Team</p></div>";
			
			return send(mailHTMLString, email,0);
		} catch (Exception e) {
			return "Please Enter The Correct Email Address ";
		}

	}

	private String send(String content, String email, int otp)
			throws IOException {
		try {
			String url = "https://mail.zoho.com/api/accounts/5330520000000008001/messages";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization",
					"Zoho-authtoken 8642e263a785c7145c38049f4287f034");
			String urlParameters = "{\"fromAddress\":\"shadow_info_deets@zoho.com\",\"toAddress\":\""
					+ email
					+ "\",\"subject\":\"Shadow Info-Deets Verfication Code\",\"mailFormat\":\"html\",\"content\":\""
					+ content + "\"}";

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				Jedis jedis = new Jedis("localhost");
				jedis.set("" + email, "" + otp);
			//	jedis.expire(email + "", 600);
				return "Email Send Successfully";
			} else {
				return "Please Enter The Correct Email Address ";
			}
		} catch (FileNotFoundException e) {
			return "Please Enter The Correct Email Address  ";

		}

	}
}