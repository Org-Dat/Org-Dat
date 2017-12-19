package zutk.b5.orgdat.model.accountmanagement;

import zutk.b5.orgdat.controllers.filters.DatabaseConnection;
import java.sql.*;

public class ForgetPass {
	DatabaseConnection dc;

	public long isAnswer(String question, String answer, String email) {
		try {
		    System.out.println("QUESTION   == " + question);
		    
		    System.out.println("ANSWER  == "+answer +" EMAIL   == "+email);
			String sql = "select user_id from security_management where question=? and answer=? and user_id = (select user_id from signup_detail where user_email=?)";
			dc = new DatabaseConnection("postgres", "postgres", "");
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, question);
			dc.stmt.setString(2, answer);
			dc.stmt.setString(3, email);
			ResultSet rs = dc.stmt.executeQuery();
			long user_id = -1;
			while (rs.next()) {
				user_id = rs.getLong(1);
			}
			if (dc != null) {
				dc.close();
			}
			 System.out.println(user_id +" == USER ID "); 
			return user_id;
		} catch (Exception e) {
		    System.out.println("USER OF FOR GT ");
		    e.printStackTrace();
			if (dc != null) {
				dc.close();
			}
			return -1l;
		}
	}
	
	public String getQuestion(long user_id){
	    try{
	        System.out.println("USER ID IN GET QUESTION     =   " + user_id);
	        String question = "";
	        String sql = "select question from  security_management where user_id = ?";
	        dc = new DatabaseConnection("postgres","postgres","");
	        dc.stmt = dc.conn.prepareStatement(sql);
	        dc.stmt.setLong(1, user_id);
	        ResultSet rs = dc.stmt.executeQuery();
	        while(rs.next()){
	            question = rs.getString(1);
	        }
	        System.out.println("GET QUESTION     =   " + question);
	        if(question.equals("")){
	            return null;
	        }
	        
	        return question;
	    }catch(Exception e){
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public long getUserId(String email){
	    try{
	        long user_id = -1;
	        String sql = "select user_id from signup_detail where user_email=?";
	        dc = new DatabaseConnection("postgres","postgres","");
	        dc.stmt = dc.conn.prepareStatement(sql);
	        dc.stmt.setString(1, email);
	        ResultSet rs = dc.stmt.executeQuery();
	        while(rs.next()){
	            user_id = rs.getLong(1);
	        }
	        dc.close();
	        return user_id;
	    }catch(Exception e){
	        return -1;
	    }
	}
	
	public boolean setPassWord(String email,String password){
	    try{
	        String sql = "update signup_detail set user_password =? where user_email = ?";
	        dc = new DatabaseConnection("postgres","postgres","");
	        dc.stmt = dc.conn.prepareStatement(sql);
	        dc.stmt.setString(1, password);
	        dc.stmt.setString(2, email);
	        dc.stmt.executeUpdate();
	        dc.close();
	        return true;
	    }catch(Exception e){
	        System.out.println("SET PASSWORD  ===");
	        e.printStackTrace();
	        return false;
	    }
	}
}