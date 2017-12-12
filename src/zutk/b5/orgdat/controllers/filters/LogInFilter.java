package zutk.b5.orgdat.controllers.filters;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.http.Cookie;
import java.io.IOException;
public class LogInFilter implements Filter {
    @Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {
	    try {
	        HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			Cookie[] cookies = request.getCookies();
			for (Cookie c : cookies) {
			    if (c.getName().equals("iambdt")){
			        response.sendRedirect("arg0");
			        return;
			    }
			}
			chain.doFilter(req, res);
	    } catch (Exception e){
	        
	    }
	    
	}
	
	@Override
	public void destroy() {

	}
}