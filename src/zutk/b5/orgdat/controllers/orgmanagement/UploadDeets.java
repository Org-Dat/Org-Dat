/**
 * This servlet manage upload detail
 * 
 * @author : Ponkumar
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.orgmanagement;

import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import zutk.b5.orgdat.controllers.filters.DatabaseConnection;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import zutk.b5.orgdat.model.orgmanagement.BackUpRestore;

public class UploadDeets extends HttpServlet {
	DatabaseConnection dc;
	java.io.PrintWriter out;
	private boolean isMultipart;
    private int maxFileSize = 500 * 1024;
	private int maxMemSize = 4 * 1024;
    @Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			out = response.getWriter();
			if (request.getMethod().toLowerCase().equals("post")) {
				doPost(request, response);
			} else {
				out.write("{'status':405,'message':'this post only url'}");
			}
		} catch (Exception e) {
			out.write("{'status':405,'message':'this post only url'}");
		}

	}

	/**
	 * This method check upload file process
	 * 
	 * @params : HttpServletRequest request , HttpServletResponse response
	 * 
	 * @return : this method return process report object
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		String requri = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/"));
		// Check that we have a file upload request
		isMultipart = ServletFileUpload.isMultipartContent(request);
		response.setContentType("text/html");
		out = response.getWriter();
		if (isMultipart == false) {
			out.write("{'status':400,'message':'file upload have sum error'}");
			return;
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File("/tmp/"));
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize);
		try {
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(request);
			// Process the uploaded file items
			Iterator i = fileItems.iterator();
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				if (!fi.isFormField()) {
					// Get the uploaded file parameters
					String forTmp = (String) fi.toString();
					String filePath = forTmp.substring(
							forTmp.indexOf("StoreLocation=")
									+ "StoreLocation=".length(),
							forTmp.indexOf(", size="));
					String tem = "";
					String org_name = request.getParameter("org_name");
					String db_name = request.getParameter("db_name");
					String table_name = request.getParameter("table_name");
					if (requri.equals("uploadDB")) {
						tem = BackUpRestore.SQLFile(org_name, db_name,
								filePath, " -a -w  ");
					} else if (requri.equals("uploadTable")) {
						tem = BackUpRestore.CSVFile(org_name, db_name,
								table_name, filePath, " from ", "yes");
					} else {
						throw new Exception();
					}
					if (filePath.equals(tem) == false) {
						throw new Exception();
					}
					System.out.println(tem + " was deleted is "
							+ new File(tem).delete());
				}
			}
		} catch (Exception ex) {
			out.write("{'status':400,'message':'file upload have sum error'}");
			return;
		}
	}
}