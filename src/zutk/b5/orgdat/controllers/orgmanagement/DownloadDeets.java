/**
 * This servlet manage download file process.
 * 
 * @author : Ponkumar 
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.orgmanagement;

import javax.servlet.*;
import java.io.*;
import javax.servlet.http.*;
import zutk.b5.orgdat.model.orgmanagement.BackUpRestore;


public class DownloadDeets extends HttpServlet {
	PrintWriter out;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			
			if (request.getMethod().toLowerCase().equals("get")) {
				doGet(request, response);
			} else {out = response.getWriter();
				out.write("{'status':405,'message':'this get only url'}");
			}
		} catch (Exception e) {
			try {
			out = response.getWriter();
			out.write("{'status':405,'message':'this get only url'}");} catch (Exception e1) {}
		}

	}

	/**
	 * This method check download file detail
	 * 
	 * @params : HttpServletRequest request , HttpServletResponse response
	 * 
	 * @return : this method doesn't return anything.
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
		String filePath = "";
		String tem = "";
		String requri = request.getRequestURI();
		// requri = requri.substring(requri.lastIndexOf("/"));
		String org_name = request.getParameter("org_name");
		String db_name = request.getParameter("db_name");
		String table_name = request.getParameter("table_name");
		new File("Images").mkdir();
		if (requri.endsWith("downloadDB")) {
			filePath = "Images/"
					+ db_name + ".sql";
			tem = BackUpRestore.SQLFile(org_name,org_name+"_"+ db_name, filePath, " -b -v ");
		} else if (requri.endsWith("downloadTable")) {
			filePath = "/tmp/" + table_name + ".csv";
			tem = BackUpRestore.CSVFile(org_name, org_name+"_"+ db_name, table_name,
					filePath, " to ", "yes");
		} else {
			PrintWriter out = response.getWriter();
			System.out.println("downloading");
			
			out.write("{'status':400,'message':'Bad Reuest'}");
			return;
		}
		if (filePath.equals(tem) == false) {
			out = response.getWriter();
			System.out.println("downloading2");
			out.write("{'status':400,'message':'Bad Reuest'}");
			return;
		}
		// reads input file from an absolute path

		System.out.println(filePath);
		File downloadFile = new File(filePath);
		System.out.println(downloadFile.exists());
		System.out.println(downloadFile);
		FileInputStream inStream = new FileInputStream(downloadFile);
		System.out.println("File Input Stream ");
		// if you want to use a relative path to context root:
		String relativePath = getServletContext().getRealPath("");
		System.out.println("relativePath = " + relativePath);
		// obtains ServletContext
		ServletContext context = getServletContext();
		// gets MIME type of the file
		String mimeType = context.getMimeType(filePath);
		if (mimeType == null) {
			// set to binary type if MIME mapping not found
			mimeType = "application/octet-stream";
		}
		System.out.println("MIME type: " + mimeType);
		System.out.println("ContentLength : " + downloadFile.length());
		// modifies response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		// forces download
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				downloadFile.getName());
		response.setHeader(headerKey, headerValue);
		System.out.println(headerKey + " : " + headerValue);
		// obtains response's output stream
		OutputStream outStream = response.getOutputStream();
		byte[] buffer = new byte[4096];
		int bytesRead = inStream.read(buffer);
			while (bytesRead != -1) {
				outStream.write(buffer, 0, bytesRead);
				bytesRead = inStream.read(buffer);
			}
			inStream.close();
			outStream.close();
		System.out.println("download finished");
		} catch (Exception e){
			
			e.printStackTrace();
		}
	}
}
