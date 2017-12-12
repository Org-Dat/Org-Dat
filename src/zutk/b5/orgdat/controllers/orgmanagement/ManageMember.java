/**
 * This servlet managing organization member 
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.orgmanagement;

import java.util.ArrayList;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import zutk.b5.orgdat.model.accountmanagement.*;
import zutk.b5.orgdat.model.databasemanagement.DatabaseConnection;
import java.sql.*;

public class ManageMember extends HttpServlet {
	PrintWriter out;
    @Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			if (request.getMethod().toLowerCase().equals("post")) {
				doGet(request, response);
			} else {
				out.write("{'status':405,'message':'this get only url'}");
			}
		} catch (Exception e) {
			out.write("{'status':405,'message':'this get only url'}");
		}

	}

	/**
	 * This method split manage member work
	 * 
	 * @params : HttpServletRequest request , HttpServletResponse response
	 * 
	 * @return : this method return process work report object
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			out = response.getWriter();
			boolean isWork = false;
			String requri = request.getRequestURI();
			if (requri.equals("createMember")) {
				String name = request.getParameter("name");
				String email = request.getParameter("email");
				String phoneNumber = request.getParameter("phone_number");
				String password = request.getParameter("password");
				String[] details = new String[4];
				details[0] = name;
				details[1] = email;
				details[2] = phoneNumber;
				details[3] = password;
				isWork = addMember(details);
			} else if (requri.equals("deleteMember")) {
				long user_id = 0;
				isWork = removeMember(user_id);
			} else {
				throw new Exception();
			}
			if (isWork == true) {
				out.write("");
			} else {
				throw new Exception();
			}

		} catch (Exception e) {
			out.write("");
		}
	}

	/**
	 * This method add new orgmember
	 * 
	 * @params : String[] details
	 * 
	 * @return type : boolean
	 * 
	 * @return : if member add successfully it return true.else return false;
	 */
	public boolean addMember(String[] details) {
		try {
			SignUp su = new SignUp();
			if (su.addMemeber(details) == true) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * This method remove organization member
	 * 
	 * @params : long user_id
	 * 
	 * @return-type : boolean
	 * 
	 * @return : if member remove successfully it return true else return false
	 */
	public boolean removeMember(long user_id) {
		try {
			DeleteAccount da = new DeleteAccount();
			if (da.deleteUser(user_id)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * This method used to get user_id
	 * 
	 * @params : String email
	 * 
	 * @return : if user give valid mail id it return their email id else return
	 *         -1;
	 */
	public long getUser_id(String email) {
		try {
			DatabaseConnection dc = new DatabaseConnection("postgres",
					"postgres", "");
			dc.stmt = dc.conn
					.prepareStatement("select user_id from signup_detail where user_email=?");
			dc.stmt.setString(1, email);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				return rs.getLong(1);
			}
			return -1;
		} catch (Exception e) {
			return -1;
		}
	}
	
	
	public ArrayList<ArrayList<String>> getMemberList(String org_name){
	    try{
	        ArrayList<ArrayList<String>> memberList  = new ArrayList<ArrayList<String>>();
	        DatabaseConnection dc = new DatabaseConnection("postgres","postgres","");
	        String sql = "select user_name,user_email from signup_detail where user_id in (select user_id from org_management where org_name=?);";
	        ArrayList<String> names = new ArrayList<String>();
	        ArrayList<String> emails = new ArrayList<String>();
	        dc.stmt = dc.conn.prepareStatement(sql);
	        dc.stmt.setString(1,org_name);
	        ResultSet rs = dc.stmt.executeQuery();
	        while (rs.next()) {
	            names.add(rs.getString(1));
	            emails.add(rs.getString(2));
	        }
	        memberList.add(names);
	        memberList.add(emails);
	        return memberList;
 	    }catch(Exception e){
	        return new ArrayList<ArrayList<String>>();
	    }
	}
}