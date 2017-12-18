/**/
package zutk.b5.orgdat.controllers.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import zutk.b5.orgdat.model.accountmanagement.SignUp;
 
public class AccountFilter extends HttpServlet implements Filter {
    
	RoleChecker rc;
    DatabaseConnection dc; 
    PrintWriter out;
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
        try{
            HttpServletRequest request  = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            rc = new RoleChecker(request);
            // dc = new DatabaseConnection("postgres","postgres","");
            out = response.getWriter();
            String requri = request.getRequestURI().substring(1);
            Cookie[] cookies = request.getCookies();
            long user_id = rc.getUserId(cookies);
            String org_name = request.getParameter("org_name");
            String db_name = request.getParameter("db_name");
            String table_name = request.getParameter("table_name");
            String email = request.getParameter("email");
            boolean isCorrect = false ;
            if (requri.startsWith("share")){
                isCorrect = shareCheck( requri ,  user_id ,  org_name ,  db_name ,  table_name , email ) ;
            } else if (requri.endsWith("deleteMember")){
                isCorrect = memberCheck( org_name, email,1);
            } else if (requri.endsWith("createMember")){
                isCorrect = memberCheck( org_name, email,0);
            } else if (requri.endsWith("getMember")){
                isCorrect = true;
            }
            if (isCorrect){
                chain.doFilter(req, res);
            }else {
                throw new Exception();
            }
            // SignUp su = new SignUp();
            // if(su.isMember(email) == false){
            //     throw new Exception("This mail Already");
            // }
            // if(email.endsWith("@"+org_name+".com") == false){
            //     throw new Exception("Email Id unauthroized");
            // } 
            
            // if(requri.endsWith("Member")){
            //     chain.doFilter(req, res);
            // }else if(requri.startsWith("get")){
            //     chain.doFilter(req, res);
            // }else{
            //     throw new Exception();
            // }
            //chain.doFilter(req, res);
        }catch(Exception e){
            out.write("{\"status\" : 403 ,\"message\"  : \"Invalid Request \"}");
        }
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	} 
	 
	 public ArrayList<String> getShareAbleMemberList(String org_name , String db_name ,String table_name) {
	     ArrayList<String> membersList = new ArrayList<String>();
	     try {
	         dc = new DatabaseConnection("postgres","postgres","");
	         String sql;
	         if (db_name == null){
    	         sql = "select user_email from (select user_email from signup_detail where user_email like '%@"+org_name+".com')as v where not user_id in (select user_id from org_management where org_name =?);";
    	         dc.stmt = dc.conn.prepareStatement(sql);
	         } else if (table_name == null) {
    	         sql = "select user_email from (select user_email from signup_detail where user_email like '%@"+org_name+".com')as v where not user_id in (select user_id from db_management where org_name =? and db_name=?);";
    	         dc.stmt = dc.conn.prepareStatement(sql);
    	         dc.stmt.setString(2, db_name);
	         } else {
    	         sql = "select user_email from (select user_email from signup_detail where user_email like '%@"+org_name+".com')as v where not user_id in (select user_id from table_management where org_name =? and db_name=? and table_name=?)";
    	         dc.stmt = dc.conn.prepareStatement(sql);
    	         dc.stmt.setString(2, db_name);
    	         dc.stmt.setString(3, table_name);
	         }
	         dc.stmt.setString(1, org_name);
	         ResultSet rs = dc.stmt.executeQuery();
	         while (rs.next()) {
	             membersList.add(rs.getString(1));
	         }
	         dc.close();
	         return membersList;
	     } catch (Exception e) {
	         if (dc != null){
	             dc.close();
	         }
	         return new ArrayList<String>();
	     }
	 }
	 
	 public ArrayList<String> getAllMemberInOrg(String org_name ){
	     ArrayList<String> membersList = new ArrayList<String>();
	     try {
	         dc = new DatabaseConnection("postgres","postgres","");
	         String sql = "select user_email from signup_detail where user_email like '%@"+org_name+".com' ;";
    	     dc.stmt = dc.conn.prepareStatement(sql);
	         ResultSet rs = dc.stmt.executeQuery();
	         while (rs.next()) {
	             membersList.add(rs.getString(1));
	         }
	         dc.close();
	         return membersList;
	     } catch (Exception e) {
	         if (dc != null){
	             dc.close();
	         }
	         return new ArrayList<String>();
	     }
	 }
	 
	 public boolean shareCheck(String requri , long user_id , String org_name , String db_name , String table_name ,String email ) {
	     try {
	        String role = rc.orgRole(org_name, user_id);
            if(role.contains("owner") == false){
                if (requri.endsWith("shareOrg")){
                    throw new Exception("You have not got Permission");
                } else {
                    role = rc.dbRole(org_name,db_name, user_id);
                    if(role.contains("owner") == false){
                        if (requri.endsWith("shareDB")){
                            throw new Exception("You have not got Permission");
                        } else {
                            role = rc.tableRole(org_name,db_name,table_name, user_id);
                            if(role.contains("owner") == false){
                                throw new Exception("You have not got Permission");
                            }
                        }
                    }
                }
            }
            ArrayList<String> orgShareAbleMember = new ArrayList<String>();
            ArrayList<String> dbShareAbleMember = new ArrayList<String>();
            ArrayList<String> tableShareAbleMember = new ArrayList<String>(); 
            switch (requri){
                case "/shareTable" :
                    tableShareAbleMember = getShareAbleMemberList( org_name , db_name , table_name);
                case "/shareDB" :
                    dbShareAbleMember = getShareAbleMemberList( org_name , db_name , table_name);
                case "/shareOrg" :
                    orgShareAbleMember = getShareAbleMemberList( org_name , db_name , table_name);
                    break;
            }
            ArrayList<String> allMember = getAllMemberInOrg( org_name );
            String member;
            for (int i = allMember.size() - 1 ;i > -1;i-- ){
                member = allMember.get(i);
                if (tableShareAbleMember.contains(member) || dbShareAbleMember.contains(member) || orgShareAbleMember.contains(member) ) {
                    allMember.remove(i);
                }
            }
	         return allMember.contains(email);
	     }catch (Exception e ) {
	         return false;
	     }
	 }
	 
	 public boolean memberCheck(String org_name,String email,int i){
	     try {
	         ArrayList<String> allMember = getAllMemberInOrg( org_name );
	         return allMember.contains(email) == false && allMember.size() < 25 +i;
	     } catch (Exception e ){
	         return false;
	     }
	 }
	 
	 
	 
}