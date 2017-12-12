package zutk.b5.orgdat.controllers.filters;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
public class LogFilter implements Filter {
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
			roleFinder = new RoleChecker(request);
			out = response.getWriter();
			String org_name = request.getParameter("org_name");
			if (org_name != null && request.getRequestURI().endsWith("/createOrg") == false  && request.getRequestURI().endsWith("/deleteOrg") == false ){
			    long user_id = -1 ; 
			    if (request.getRequestURI().startsWith("/api/")){
			         user_id = roleFinder.getUserId(request.getHeader("Authorization"));
			    } else {
			         user_id = roleFinder.getUserId(request.getCookies());
			    }
			    
    			String date = new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
    			File logFile = new File("/home/workspace/OrgDat/webapps/Log/"+org_name+"/log_"+date+".log");
    			logFile.createNewFile();
    			String email = idToEmail(user_id);
    			Date now = new Date();
    			
    			chain.doFilter(req, res);
    	        SimpleDateFormat dateFormatter = new SimpleDateFormat("[dd/MMM/yyyy:kk:mm:ss Z]");
                FileWriter fw = new FileWriter(logFile,true); //the true will append the new data
                fw.write(email + " " +request.getRemoteAddr()+" - - " + dateFormatter.format(now)+" \""+request.getMethod()+" "+request.getRequestURI()+" "+request.getProtocol()+"\" "+response.getStatus()+" "+response.getBufferSize()+"\n");//appends the string to the file
                fw.close();
			} else {
			    chain.doFilter(req, res);
			}
			
		} catch (Exception e) {
		    e.printStackTrace();
		   System.out.println(e);
			out.write("{'status':403,'message':'forbetten'}");
		}

	}
	
	
	private String idToEmail(long user_id) {
	    String email = null;
	    try {
	        dc = new DatabaseConnection("postgres","postgres","");
	        String sql = "select user_email from signup_detail where user_id = ?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				email = rs.getString(1) ;
			}
	        return email;
	    } catch (Exception e) {
	        return null;
	    }
	}
	

	@Override
	public void destroy() {

	}
}