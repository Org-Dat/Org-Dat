package ZUTK.B5.OrgDat.Controllers.Filters;
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
			roleFinder = new RoleChecker();
			out = response.getWriter();
			long user_id = roleFinder.getUserId(request.getCookies());
			String[] path = request.getRequestURI().substring(1).split("/");
			String org_name;
			if (path[0].equals("api")) {
			    org_name = path[1];
			} else { 
			    org_name = path[0];
			}
			 String date = new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
			File logFile = new File("/home/workspace/OrgDat/src/"+org_name+"/log_"+date);
			logFile.createNewFile();
			String email = idToEmail( user_id);
			Date now = new Date();
	        chain.doFilter(req, res);
	        SimpleDateFormat dateFormatter = new SimpleDateFormat("[dd/MMM/yyyy:kk:mm:ss Z]");
            FileWriter fw = new FileWriter(logFile,true); //the true will append the new data
            fw.write(email + " " +request.getRemoteAddr()+" - - " + dateFormatter.format(now)+" \""+request.getMethod()+" "+request.getRequestURI()+" "+request.getProtocol()+"\" "+response.getStatus()+" "+response.getBufferSize()+"\n");//appends the string to the file
            fw.close();
		} catch (Exception e) {
			out.write("{'status':403,'message':'forbetten'}");
		}

	}
	
	
	private String idToEmail(long user_id) {
	    String email = null;
	    try {
	        String sql = "select user_email from signup_management where user_id = ?";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setLong(1, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while (rs.next()) {
				email = rs.getString("user_email") ;
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