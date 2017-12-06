package ZUTK.B5.OrgDat.Controllers.DatabaseManagement;

import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ZUTK.B5.OrgDat.Model.DatabaseManagement.*;

public class ManageOrganization extends HttpServlet {
    PrintWriter out;
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {

		try {
            out = response.getWriter();
			ManageOrg orgManage = new ManageOrg();
			String requri = request.getRequestURI();
			String ClientResponse = orgManage.ManageOrg(requri);
			out.write(ClientResponse);
		} catch (Exception e) {
			out.write("{'status':404,'message':'Not Found'}");
		}
	}
}