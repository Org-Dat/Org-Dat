package ZUTK.B5.OrgDat.Controllers.OrgManagement;

import javax.servlet.*;
import java.io.*;
import javax.servlet.http.*;
import ZUTK.B5.OrgDat.Model.OrgManagement.BackUpRestore;
/**
 * this servlet only for download file 
 * this servlet only  download request.getAttribute("filepath") that path file ;
 * so we want to set filepart in fillter , we didn't set or change the filepath;  
 */
 
public class DownloadDeets extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filePath = "";
        String tem = "";
        String[] path = request.getRequestURI().split("/");
        if ((path[path.length-1].equals("$downloadDB")) && (path.length == 3)) {
            filePath = " /home/workspace/JavaWepApps/webapps/JavaWepApps/images/"+path[1]+".sql";
            tem = BackUpRestore.SQLFile(path[0],path[1],filePath," -b -v "); 
        } else  if ((path[path.length-1].equals("$downloadTable")) && (path.length == 4)) {
            filePath = " /tmp/"+path[2]+".sql";
            tem = BackUpRestore.CSVFile(path[0],path[1],path[2],filePath," to ","yes"); 
        } else {
            PrintWriter out = response.getWriter();
		    out.write("{'status':400,'message':'Bad Reuest'}"); 
		    return ;
        }
        if (filePath.equals(tem) == false){
            PrintWriter out = response.getWriter();
		    out.write("{'status':400,'message':'Bad Reuest'}"); 
		    return ;
        }
        // reads input file from an absolute path
        
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
        System.out.println("ContentLength : "+ downloadFile.length());
        // modifies response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);
        System.out.println(headerKey +" : " +headerValue);
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