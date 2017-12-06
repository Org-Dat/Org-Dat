package ZUTK.B5.OrgDat.Controllers.OrgManagement;

import java.util.ArrayList;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import java.io.*;
import  ZUTK.B5.OrgDat.Controllers.Filters.DatabaseConnection;
import java.sql.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

public class UploadDeets extends HttpServlet {
	DatabaseConnection dc;
	 
   private boolean isMultipart;
   private String filePath;
   private int maxFileSize = 500 * 1024;
   private int maxMemSize = 4 * 1024;
   private File file ;
   public void init( ){
      // Get the file location where it would be stored.
      filePath = "/home/workspace/JavaWepApps/webapps/JavaWepApps/images/" ;
   }
   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
   
      // Check that we have a file upload request
      isMultipart = ServletFileUpload.isMultipartContent(request);
      response.setContentType("text/html");
      java.io.PrintWriter out = response.getWriter( );
   
      if( isMultipart == false) {
         out.println("<html>");
         out.println("<head>");
         out.println("<title>Servlet upload</title>");  
         out.println("</head>");
         out.println("<body>");
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

         out.println("<html>");
         out.println("<head>");
         out.println("<title>Servlet upload</title>");  
         out.println("</head>");
         out.println("<body>");
   
         while ( i.hasNext () ) {
            FileItem fi = (FileItem)i.next();
            if ( !fi.isFormField () ) {
               // Get the uploaded file parameters
               String fieldName = fi.getFieldName();
               String fileName = fi.getName();
               String contentType = fi.getContentType();
               /**
                * under three line writen by me for find and tmp file
                */
               String forTmp = (String)fi.toString();
               forTmp = forTmp.substring(forTmp.indexOf("StoreLocation=")+"StoreLocation=".length(), forTmp.indexOf(", size="));
               File tmp = new File(forTmp);
               /* * * * * * * *  * * * * * * * * * * * * * * * * * * * * * * * ** * * * ** * * * * *******/
               boolean isInMemory = fi.isInMemory();
               long sizeInBytes = fi.getSize();
                System.out.println("size = "+sizeInBytes);
                
               // Write the file
               if( fileName.lastIndexOf("\\") >= 0 ) {
                  file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
               } else {
                  file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
               }
               System.out.println(file);
               fi.write( file ) ;
               out.println("Uploaded Filename: " + fileName + "<br>");
               System.out.println(tmp +" was deleted is "+tmp.delete() );
            }
         }
         out.println("</body>");
         out.println("</html>");
         } catch(Exception ex) {
            System.out.println(ex);
         }
      }
      
      public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, java.io.IOException {

         throw new ServletException("GET method used but  POST method required.");
      }
  

}