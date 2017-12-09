package ZUTK.B5.OrgDat.Controllers.Filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import ZUTK.B5.OrgDat.Model.AccountManagement.SignUp;
 
public class AccountFilter extends HttpServlet implements Filter {
    
	RoleChecker rc;
    DatabaseConnection dc; 
    PrintWriter out;
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
        try{
            rc = new RoleChecker();
            dc = new DatabaseConnection("postgres","postgres","");
            HttpServletRequest request  = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            out = response.getWriter();
            Cookie[] cookies = request.getCookies();
            long user_id = rc.getUserId(cookies);
            String org_name = request.getRequestURI().split("/")[0];
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