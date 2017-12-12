/**/
package zutk.b5.orgdat.controllers.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
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
            dc = new DatabaseConnection("postgres","postgres","");
            out = response.getWriter();
            String requri = request.getRequestURI();
            if(requri.startsWith("addMember")){
                chain.doFilter(req, res);
            }else if(requri.startsWith("get")){
                chain.doFilter(req, res);
            }else{
                throw new Exception();
            }
            Cookie[] cookies = request.getCookies();
            long user_id = rc.getUserId(cookies);
            String org_name = request.getParameter("org_name");
            String role = rc.orgRole(org_name, user_id);
            if(role.equals("owner") == false){
                throw new Exception();
            }
            String email = request.getParameter("email");
            SignUp su = new SignUp();
            if(su.isMember(email) == true){
                throw new Exception();
            }
            if(email.endsWith("@"+org_name+".com") == false){
                throw new Exception();
            }
            chain.doFilter(req, res);
        }catch(Exception e){
            out.write("");
        }
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
}