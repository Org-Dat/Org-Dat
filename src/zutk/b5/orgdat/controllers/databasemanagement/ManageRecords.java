/**
 * This servlet managing records process
 * 
 * @author : Ponkumar
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.databasemanagement;

import javax.servlet.http.*;
import zutk.b5.orgdat.model.databasemanagement.ManageRecord;
import com.google.gson.*;
import java.io.*;

public class ManageRecords extends HttpServlet {
	PrintWriter writer;
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			writer = response.getWriter();
			if (request.getMethod().toLowerCase().equals("post") || request.getRequestURI().endsWith("/readRecord") ) {
				doPost(request, response);
			} else {
				writer.write("{'status':405,'message':'this post only url'}");
				return;
			}
		} catch (Exception e) {
			writer.write("{'status':405,'message':'this post only url'}");
			return;
		}
	}

	/**
	 * This method split record manage work process
	 * 
	 * @params : HttpServletRequest request , HttpServletResponse response
	 * 
	 * @return : If user give correct data it return detail object else return
	 *         error object
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		writer = response.getWriter();
		try {
			String reqURI = request.getRequestURI();
			if (reqURI.startsWith("/api/")) {
				reqURI = reqURI.substring(reqURI.lastIndexOf("/") + 1);
			} else {
			    reqURI = reqURI.substring(1);
			}
			String org_name = request.getParameter("org_name");
			String db_name = request.getParameter("db_name");
			String table_name = request.getParameter("table_name");
			ManageRecord databaseProcess = new ManageRecord(org_name+"_"+db_name, org_name,"");
					System.out.println("manage record");
			JsonParser parser = new JsonParser();
			JsonArray conditionArray;
			JsonArray columnArray;
			String tmparavary = request.getParameter("conditionArray");
			if (tmparavary != null) {
				conditionArray = parser.parse(tmparavary).getAsJsonArray();
			} else {
				conditionArray = null;
			}
            System.out.println(parser);
            System.out.println(request.getParameter("records"));
            System.out.println(request.getQueryString());
			switch (reqURI) {
			case "addRecord":
				JsonObject records = parser.parse(request.getParameter("records")).getAsJsonObject();
				writer.write(databaseProcess.addRecord(org_name,db_name,table_name, records));
				break;
			case "readRecord":
			    System.out.println("readRecord");
				if (request.getParameter("columnNames") != null) {
					columnArray = parser.parse(
							request.getParameter("columnNames"))
							.getAsJsonArray(); 
				} else {
					columnArray = null;
				} 
				writer.write(databaseProcess.selectRecord(org_name,db_name,table_name, columnArray,conditionArray, request.getParameter("andOr")));
				break;
			case "deleteRecord": 
			    writer.write(databaseProcess.deleteRecord(org_name,db_name,table_name, conditionArray,request.getParameter("andOr")));
				break;
			case "updateRecord":
				JsonObject set = parser.parse(request.getParameter("set")).getAsJsonObject();
				writer.write( databaseProcess.updateRecord(org_name,db_name,table_name, set,conditionArray, request.getParameter("andOr")));
				break;
			default:
			    System.out.println("default");
				writer.write("{'status':400,'message':'Bad Request'}");
				return;
			}
		} catch (Exception e) {
		    e.printStackTrace();
		    System.out.println("mag records con = "+e);
		    
			writer.write("{'status':400,'message':'Bad Reuest'}");
			return;
		}

	}
}