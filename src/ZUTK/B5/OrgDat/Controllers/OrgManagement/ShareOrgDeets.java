package ZUTK.B5.OrgDat.Controllers.OrgManagement;

import java.util.ArrayList;
import javax.servlet.http.*;
import ZUTK.B5.OrgDat.Model.OrgManagement.Share;
import javax.servlet.*;
import java.io.*;
import  ZUTK.B5.OrgDat.Controllers.Filters.DatabaseConnection;
import java.sql.*;

public class ShareOrgDeets extends HttpServlet {
	DatabaseConnection dc;
    PrintWriter out;
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
	   try{
	       out = response.getWriter();
	       Share share = new Share();
	   }catch(Exception e){
	       
	   }
    }
}