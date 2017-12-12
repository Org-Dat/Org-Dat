/**
 * This Servlet read and get logfile name and content
 * 
 * @author : Obeth Samuel
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.orgmanagement;

import javax.servlet.http.*;
import java.io.PrintWriter;
import zutk.b5.orgdat.model.orgmanagement.LogFile;

public class LogFileReader extends HttpServlet {
	PrintWriter out;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			if (request.getMethod().toLowerCase().equals("post")) {
				doGet(request, response);
			} else {
				out.write("{'status':405,'message':'this get only url'}");
			}
		} catch (Exception e) {
			out.write("{'status':405,'message':'this get only url'}");
		}

	}

	/**
	 * This method check logfile detail
	 * 
	 * @params : HttpServletRequest request , HttpServletResponse response
	 * 
	 * @return : it logfile detail correct it return logfile content or logfile
	 *         names else return error object
	 */
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
			if (request.getRequestURI().equals("getLogFile")) {
				String date = request.getParameter("date");
				if (date == null) {
					throw new Exception();
				}

				fileContents = logFile.readFile(org_name, date);
			} else if (request.getRequestURI().equals("getLogFileNames")) {
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