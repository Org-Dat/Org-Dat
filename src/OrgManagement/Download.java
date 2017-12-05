package OrgManagement;


import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class Download extends HttpServlet {
 
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // reads input file from an absolute path
        String filePath ="";
        if (request.getRequestURI().equals("/down")){
             filePath = "/tmp/"+request.getParameter("tabName")+".csv";
        } else if (request.getRequestURI().equals("/downsql")){
             filePath = "/home/workspace/deployment/Default_Server/sql/"+request.getParameter("datName")+".sql";
        } 
        
        System.out.println(filePath);
        File downloadFile = new File(filePath);
        FileInputStream inStream = new FileInputStream(downloadFile);
         
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
         
        // modifies response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
         
        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);
         
        // obtains response's output stream
        OutputStream outStream = response.getOutputStream();
         
        byte[] buffer = new byte[4096];
        int bytesRead= inStream.read(buffer);
         
        while (bytesRead  != -1) {
            outStream.write(buffer, 0, bytesRead);
            bytesRead= inStream.read(buffer);
        }
         
        inStream.close();
        outStream.close();     
    }
}