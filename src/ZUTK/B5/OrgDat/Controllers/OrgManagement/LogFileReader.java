package ZUTK.B5.OrgDat.Controllers.OrgManagement;

import javax.servlet.http.*;
import java.io.PrintWriter;
import ZUTK.B5.OrgDat.Model.OrgManagement.LogFile;

public class LogFileReader extends HttpServlet {
	PrintWriter out;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String org_name = request.getParameter("org_name");
			LogFile logFile = new LogFile();
			String fileContents = "";
			if (org_name == null) {
				throw new Exception();
			}
			if (request.getRequestURI().equals("$getLogFile")) {
				String date = request.getParameter("date");
				if (date == null) {
					throw new Exception();
				}

				fileContents = logFile.readFile(org_name, date);
			} else if (request.getRequestURI().equals("$getLogFileNames")) {
				fileContents = logFile.getLogFileNames(org_name);
			} else {
				throw new Exception();
			}

			out.write("{'status':200,'data':'" + fileContents
					+ "','message':'read successfully'}");
		} catch (Exception e) {
			out.write("{'status':404,'message':'File Not Found'}");
		}
	}
}