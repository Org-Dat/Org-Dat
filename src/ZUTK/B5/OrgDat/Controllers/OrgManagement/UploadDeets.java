package ZUTK.B5.OrgDat.Controllers.OrgManagement;

import java.util.ArrayList;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import java.io.*;
import ZUTK.B5.OrgDat.Controllers.Filters.DatabaseConnection;
import java.sql.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;
import ZUTK.B5.OrgDat.Model.OrgManagement.BackUpRestore;

public class UploadDeets extends HttpServlet {
	DatabaseConnection dc;
	 
   private boolean isMultipart;
//   private String filePath;
   private int maxFileSize = 500 * 1024;
   private int maxMemSize = 4 * 1024;
//   private File file ;
   public void init( ){
      // Get the file location where it would be stored.
    //   filePath = "/home/workspace/JavaWepApps/webapps/JavaWepApps/images/" ;
   }

   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
   
      // Check that we have a file upload request
      isMultipart = ServletFileUpload.isMultipartContent(request);
      response.setContentType("text/html");
      java.io.PrintWriter out = response.getWriter( );
   
         out.println("<html>"); 
         out.println("<head>");
         out.println("<title>Servlet upload</title>");  
         out.println("</head>");
         out.println("<body>");
   
      if( isMultipart == false) {
         out.println("<p>No file uploaded</p>"); 
         out.println("</body>");
         out.println("</html>");
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
      upload.setSizeMax( maxFileSize );
      try { 
         // Parse the request to get file items.
         List fileItems = upload.parseRequest(request); 
	
         // Process the uploaded file items
         Iterator i = fileItems.iterator();

         while ( i.hasNext () ) {
            FileItem fi = (FileItem)i.next();
            if ( !fi.isFormField () ) {
               // Get the uploaded file parameters
                String forTmp = (String)fi.toString();
                String filePath = forTmp.substring(forTmp.indexOf("StoreLocation=")+"StoreLocation=".length(), forTmp.indexOf(", size="));
                String tem = "";
                String[] path = request.getRequestURI().split("/");
                if (path[2].equals("$uploadDB")) {
                    tem = BackUpRestore.SQLFile(path[0],path[1],filePath," -a -w  ");  
                } else  if (path[3].equals("$uploadTable")) {
                    tem = BackUpRestore.CSVFile(path[0],path[1],path[2],filePath," from ","yes"); 
                } else {
                    throw  new Exception();
                }
                if (filePath.equals(tem) == false) {
                    throw  new Exception();
                }
                System.out.println(tem +" was deleted is "+new File(tem).delete() );
            }
         }
         out.println("</body>");
         out.println("</html>");
         } catch(Exception ex) {
            System.out.println(ex);
		    out.write("{'status':400,'message':'Bad Reuest'}"); 
		    return ;
         }
      }
      
      public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, java.io.IOException {

         throw new ServletException("GET method used but  POST method required.");
      }
  

}