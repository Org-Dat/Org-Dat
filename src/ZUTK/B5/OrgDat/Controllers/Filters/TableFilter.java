/**
 * This filter check table permission.
 * 
 * @author : Obeth & Ponkumar 
 * 
 * @version : 1.0
 */
package ZUTK.B5.OrgDat.Controllers.Filters;
import ZUTK.B5.OrgDat.Model.DatabaseManagement.ShowDetails;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;

public class TableFilter extends DownloadFilter implements Filter {
	protected DatabaseConnection dc;
	RoleChecker roleFinder;
	PrintWriter out;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	/**
	 * This method used to check table permission
	 * 
	 * @params : ServletRequest req, ServletResponse res,FilterChain chain
	 * 
	 * @return type : void
	 * 
	 * @return : This method doesn't return any thing
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		try {
		    HttpServletRequest request = (HttpServletRequest) req;
    		HttpServletResponse response = (HttpServletResponse) res;
    		roleFinder = new RoleChecker();
    		 out = response.getWriter();
			String requri = request.getRequestURI();
			String[] path = requri.split("/");
			int count = 0;
			long user_id;
			if (path[0].equals("api")) {
				count += 1;
				String authtoken = request.getHeader("authtoken");
				user_id = roleFinder.getUserId(authtoken);
			} else {
				Cookie[] cookies = request.getCookies();
				user_id = roleFinder.getUserId(cookies);
			}

			if (user_id == -1) {
				throw new Exception();
			}
			
			if (path[path.length - 1].equals("downloadTable")) {
			    super.doFilter(req, res, chain);
			} else {
    			dc = new DatabaseConnection("postgres", "postgres", "");
    
    			String org_name = path[count];// request.getParameter("org_name");
    			String db_name = path[count + 1];// request.getParameter("db_name");
    			String table_name = path[count + 2];// request.getParameter("table_name");
    			if (vaildCheck( user_id ,  org_name ,  db_name ,  table_name) == false){
    			    throw new Exception();
    			}
    			String role = roleFinder.tableRole(org_name, db_name, table_name,
    					user_id);
    	/**************** it is for rights ********************/
    			boolean rights = false; 
    			if (role == null) {
    				role = roleFinder.dbRole(org_name, db_name, user_id);
    				if (role == null) {
    					role = roleFinder.orgRole(org_name, user_id);
    					if (role.contains("owner")) {
    					    rights = true;
    					}
    				} else if (role.contains("owner") || role.equals("can/write")) {
    				    rights = true;
    				}else if (role.equals("read only")) {
        				if (path[count + 3].equals("$readRecord")) {
        				    rights = true;
        				}
        			} 
    			} else if (role.equals("can/write")) {
    				if (path[count + 3].equals("$renameTable") || path[count + 3].equals("$readRecord")) {
    				    rights = true;
    				}
    			} else if (role.contains("owner")) {
    			    rights = true;
    			}
    			//  $readRecord
    			else if (role.equals("read only")) {
    				if (path[count + 3].equals("$readRecord")) {
    				    rights = true;
    				}
    			}  
    			if (rights == true){
    			    if (path[count + 3].equals("$readRecord")) {
    				    chain.doFilter(req, res);
    				    dc.conn.close();
    				} else {
            	        dc.stmt = dc.conn.prepareStatement("select user_id form lock_management where org_name =? and db_name =? and table_name =?");
            	        dc.stmt.setString(1,org_name);
            	        dc.stmt.setString(2,db_name);
            	        dc.stmt.setString(3,table_name);
            	        ResultSet rs = dc.stmt.executeQuery();
            	        if (rs.next()){
            	            if ( (long) (rs.getLong(1)) == user_id){
            	                chain.doFilter(req, res);
    				            dc.conn.close();
            	            } else {
            	                dc.stmt = dc.conn.prepareStatement("select user_email form signup_detail where user_id =?");
                    	        dc.stmt.setLong(1,user_id);
                    	        rs = dc.stmt.executeQuery();
            	                out.write("{'status':200 ,'message':'"+rs.getString(1)+" was edit this page , so pleace !!'}");
            	            }
            	        } else {
            	            dc.stmt = dc.conn.prepareStatement("insert into lock_management values(?,?,?,?)");
                	        dc.stmt.setLong(1,user_id);
                	        dc.stmt.setString(2,org_name);
                	        dc.stmt.setString(3,db_name);
                	        dc.stmt.setString(4,table_name);
                	        dc.stmt.executeUpdate();
            	        }
    				}
    				
    			} else {
    				throw new Exception();
    			}
			}
			
		} catch (Exception e) {
			out.write("{'status':403 ,'message':'Forbidden'}");
		} finally {
			try {
				dc.conn.close();
			} catch (Exception e) {
				System.out.println("Connection Not Close");
			}
		}

	}
	
	private boolean vaildCheck(long user_id , String org_name , String db_name , String table_name) {
	    try {
	        if (org_name == null || db_name == null || table_name == null) {
				throw new Exception();
			}
		    ShowDetails shower = new ShowDetails();
		    ArrayList<String> tem = shower.getOrganization(user_id);
		    if (tem.contains(org_name) == false){
		        throw new Exception();
		    }
		    tem = shower.getDatabase(db_name);
		    if (tem.contains(org_name) == false){
		        throw new Exception();
		    }
		    tem = shower.getTables(org_name, db_name);
		    if (tem.contains(org_name) == false){
		        throw new Exception();
		    }
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	private boolean editCheck(long user_id , String org_name , String db_name , String table_name) {
	    try {
	        dc.stmt = dc.conn.prepareStatement("select user_id,visible form lock_management where org_name =? and db_name =? and table_name =?");
	        dc.stmt.setString(1,org_name);
	        dc.stmt.setString(2,db_name);
	        dc.stmt.setString(3,table_name);
	        ResultSet rs = dc.stmt.executeQuery();
	        boolean ans = false;
	        while(rs.next()) {
	             ans = (user_id ==((long) rs.getLong(1)) &&  rs.getBoolean(2));
	        }
	        return ans;
	    } catch (Exception e) {
	        return false;
	    }
	}

	@Override
	public void destroy() {

	}

}



/***
 * 
 * extends HttpServlet
 * 
 * 
 */