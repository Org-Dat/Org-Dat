package ZUTK.B5.OrgDat.Controllers.AccountManagement;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import ZUTK.B5.OrgDat.Model.AccountManagement.*;

public class UserLogout extends HttpServlet {
	PrintWriter out;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Cookie[] cookies = request.getCookies();
			String iambdt = "";
			out = response.getWriter();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("iambdt")) {
					iambdt = cookie.getValue();
					break;
				}
			}
			Logout lo = new Logout();
			String ip_address = "";
			if (lo.logout(iambdt, ip_address) == true) {
                out.write("Logout Successfully");
			} else {
                out.write("Logout Unsuccessfully");
			}
		} catch (Exception e) {
            out.write("Logout Unsuccessfully");
		}
	}
}