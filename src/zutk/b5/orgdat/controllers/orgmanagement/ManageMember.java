/**
 * This servlet managing organization member 
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.orgmanagement ;

import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import org.json.simple.JSONArray;
import zutk.b5.orgdat.controllers.filters.*; 
import zutk.b5.orgdat.model.accountmanagement.*;
import java.sql.*;

public class ManageMember extends HttpServlet {
	PrintWriter out;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			if (request.getMethod().toLowerCase().equals("post")) {
				doPost(request, response);
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
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			out = response.getWriter();
			boolean isWork = false;
			String requri = request.getRequestURI().substring(1);
			String org_name = request.getParameter("org_name");
			if (requri.endsWith("createMember")) {
				String name = request.getParameter("name");
				if (name.matches("^[a-zA-Z][a-zA-Z0-9]{3,255}$") == false){
				    out.write("User Name is Incorrect");
				    // out.write("{\"status\" :, \"message\" : \"get list successfully \" , \"data\" : "+JSONArray.toJSONString(getMemberList(org_name))+"}");
                    return;
				}
				String email = request.getParameter("email");
				if (email.matches("^[a-z][a-z0-9]{5,30}@"+org_name+".com$") == false){
				    out.write("User email is Incorrect");
				    // out.write("{\"status\" :, \"message\" : \"get list successfully \" , \"data\" : "+JSONArray.toJSONString(getMemberList(org_name))+"}");
                    return;
				}
				String phoneNumber = request.getParameter("phone_number");
				if (phoneNumber != null && phoneNumber.matches("^[+]?[0-9]{10,30}$") == false){
				    out.write("User phone is Incorrect");
				    // out.write("{\"status\" :, \"message\" : \"get list successfully \" , \"data\" : "+JSONArray.toJSONString(getMemberList(org_name))+"}");
                    return;
				}
				String password = request.getParameter("password");
				if (password.matches("^.{6,255}$") == false){
				    out.write("Password is Incorrect");
				    // out.write("{\"status\" :, \"message\" : \"get list successfully \" , \"data\" : "+JSONArray.toJSONString(getMemberList(org_name))+"}");
                    return;
				}
				String[] details = new String[5];
				if (email.endsWith("@" + org_name + ".com") == false) {
					throw new Exception("email address invaild ");
				}
				details[0] = name;
				details[1] = email;
				details[2] = phoneNumber;
				details[3] = password;
				details[4] = "member";
				System.out.println("DETAIL ARRRAY = "
						+ Arrays.toString(details));
				isWork = addMember(details);

			} else if (requri.endsWith("getMember")) {
                 out.write("{\"status\" :200, \"message\" : \"get list successfully \" , \"data\" : "+JSONArray.toJSONString(getMemberList(org_name))+"}");
                 return;
                
			} else if (requri.endsWith("deleteMember")) {
				String email = request.getParameter("email");
				long user_id = getUser_id(email);
				isWork = removeMember(user_id);
			} else {
				throw new Exception();
			}
			if (isWork == true) {
				out.write("{\"status\" : 200 ,\"message\" : \"member detail  managed successfully\" , \"data\" : "
						+ JSONArray.toJSONString(getMemberList(org_name)) + "}");
				return;
			} else {
				throw new Exception(" some error occred");
			}
		} catch (Exception e) {
		    e.printStackTrace();
			out.write("{\"status\" : 406 ,\"message\" : \"member detail  invaild details  un successfully\"}");
			return;
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
			System.out.println("START CREATION !....");
			if (su.addMemeber(details) == true) {
				return true;
			}
			System.out.println("FINISH CREATION !....");
			return false;
		} catch (Exception e) {
			System.out.println("ERROR " + e);
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
		DatabaseConnection dc = null;
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			dc.stmt = dc.conn
					.prepareStatement("select user_id from signup_detail where user_email=?");
			dc.stmt.setString(1, email);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				return rs.getLong(1);
			}
			dc.close();
			return -1;
		} catch (Exception e) {
			if (dc != null) {
				dc.close();
			}
			return -1;
		}
	}
    public ArrayList<ArrayList<String>> getMemberList(String org_name) {
		DatabaseConnection dc = null;
		System.out.println("GET MEMBER LIST ! >..."); 
		try {
			ArrayList<ArrayList<String>> memberList = new ArrayList<ArrayList<String>>();
			dc = new DatabaseConnection("postgres", "postgres", "");
			String sql = "select user_name,user_email from signup_detail where user_email like ?;";
			ArrayList<String> names = new ArrayList<String>();
			ArrayList<String> emails = new ArrayList<String>();
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, "%@"+ org_name + ".com");
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				names.add(rs.getString(1));
				emails.add(rs.getString(2));
			}
			dc.stmt.close();
			dc.stmt = dc.conn.prepareStatement("select user_name,user_email from signup_detail where user_id=(select owner_id from org_details where org_name=?);");
			dc.stmt.setString(1, org_name);
			rs = dc.stmt.executeQuery();
			while (rs.next()) {
				names.add(rs.getString(1));
				emails.add(rs.getString(2));
			}
			memberList.add(names);
			memberList.add(emails);
			dc.close();
			System.out.println(memberList.toString());
			return memberList;
		} catch (Exception e) {
		    e.printStackTrace();
			if (dc != null) {
				dc.close();
			}
			return new ArrayList<ArrayList<String>>();
		}
	}
	/*public ArrayList<ArrayList<String>> getMemberList(String org_name) {
		DatabaseConnection dc = null;
		System.out.println("GET MEMBER LIST ! >...");
		try {
			ArrayList<ArrayList<String>> memberList = new ArrayList<ArrayList<String>>();
			dc = new DatabaseConnection("postgres", "postgres", "");
			String sql = "select user_name,user_email from signup_detail where user_id in (select user_id from org_management where org_id in (select org_id from org_details where org_name=?));";
			ArrayList<String> names = new ArrayList<String>();
			ArrayList<String> emails = new ArrayList<String>();
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, org_name);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
			    System.out.println("one");
				names.add(rs.getString(1));
				emails.add(rs.getString(2));
			}
// 			dc.stmt.close();
// 			dc.stmt = dc.conn.prepareStatement("select user_name,user_email from signup_detail where user_id=(select user_id from org_management where org_name=?);");
// 			dc.stmt.setString(1, org_name);
// 			rs = dc.stmt.executeQuery();
// 			while (rs.next()) {
// 				names.add("\""+rs.getString(1)+"\"");
// 				emails.add("\""+rs.getString(2)+"\"");
// 			}
			memberList.add(names);
			memberList.add(emails);
			dc.close();
			System.out.println(memberList.toString());
			return memberList;
		} catch (Exception e) {
		    e.printStackTrace();
			if (dc != null) {
				dc.close();
			}
			return new ArrayList<ArrayList<String>>();
		}
	}*/
}