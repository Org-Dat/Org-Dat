/**
 *  This filter used to check database filter permission.
 * 
 * @author : Obeth Samuel & Ponkumar
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DatabaseFilter extends DownloadFilter implements Filter {
	protected DatabaseConnection dc;
	RoleChecker roleFinder;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
	PrintWriter out ;
    /**
     * This  Method  check database permission .
     * 
     * @params : ServletRequest req, ServletResponse res, FilterChain chain
     * 
     * @return type : void
     * 
     * @return : if user didn't give correct detail it return error object .else redirect to servlet
     */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		try {
		    System.out.println("Database Filter ");
		    HttpServletRequest request = (HttpServletRequest) req;
    		HttpServletResponse response = (HttpServletResponse) res;
    		roleFinder = new RoleChecker(request);
    		out = response.getWriter();
			String requri = request.getRequestURI().substring(1);
			
			long user_id;
			if (requri.startsWith("api") == true) {
				String authtoken = request.getHeader("Authorization");
				System.out.println("api stsrst "+authtoken);
				user_id = roleFinder.getUserId(authtoken);
				System.out.println("*"+user_id);
			}else{
			    Cookie[] cookies = request.getCookies();
			    user_id = roleFinder.getUserId(cookies);
			}
			System.out.println("User id :"+user_id);
			if (user_id == -1) {
				throw new Exception();
			}
			if (requri.endsWith("downloadDB")) {
			    super.doFilter(req, res, chain);
			} else {
    			dc = new DatabaseConnection("postgres", "postgres", "");
    			String org_name =  request.getParameter("org_name");
    			String db_name =  request.getParameter("db_name");
    			System.out.println("ORG : "+org_name+" ;DB  : "+db_name+"  ;user_id : "+user_id);
    			if (org_name == null || org_name.matches("^[a-z][a-z0-9]{3,30}$")== false || db_name == null || db_name.matches("^[a-z][a-z0-9]{3,30}$") == false) {
    				throw new Exception();
    			}
    			String role = roleFinder.dbRole(org_name, db_name, user_id);
    			System.out.println("Role  : "+ role);
    			if (role == null) {
    				role = roleFinder.orgRole(org_name, user_id);
    				System.out.println("if Role  : "+ role);
    				if (role.contains("owner")) {
    					chain.doFilter(req, res);
    					dc.conn.close();
    				} else {
    					throw new Exception();
    				}
    			} else if (role.contains("owner")) {
    				chain.doFilter(req, res);
    				dc.conn.close();
    			} else if(role.equals("can/write")){
    			     request.setAttribute("role", "db_"+role);
    			    if(request.getRequestURI().endsWith("renameDB")){
    			        chain.doFilter(req,res);
    			    }else{
    			        throw new Exception();
    			    }
    			}else {
    				throw new Exception();
    			}
			}

		} catch (Exception e) {
		    System.out.println(e);
			out.write("{'status':403 ,'message':'Forbidden'}");
		} finally {
			try {
				dc.conn.close();
			} catch (Exception er) {
				System.out.println("Connection Not close");
			}
		}
	}
	
	@Override
	public void destroy() {

	}
}

/**
 * 
 * extends HttpServlet
 * 
 */
