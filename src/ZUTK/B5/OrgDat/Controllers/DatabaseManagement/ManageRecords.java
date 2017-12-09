package ZUTK.B5.OrgDat.Controllers.DatabaseManagement;

import javax.servlet.http.*;
import ZUTK.B5.OrgDat.Model.DatabaseManagement.ManageRecord;
import com.google.gson.*;
import java.io.*;

public class ManageRecords extends HttpServlet {
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		try {
			String requri = request.getRequestURI();
			if (requri.startsWith("/api")) {
				requri = requri.substring(requri.indexOf("/", 1));
			}
			String[] path = requri.split("/");
			String org_name = path[0];// request.getParameter("org_name");
			String db_name = path[1];// request.getParameter("db_name");
			String table_name = path[2];// request.getParameter("table_name");
			ManageRecord databaseProcess = new ManageRecord(db_name ,org_name ,"");
			JsonParser parser = new JsonParser(); 
			JsonArray conditionArray;
			JsonArray columnArray;
			String resp = "";
			switch (path[3]) {
    			case "$addRecord":
    			    JsonObject records = parser.parse(request.getParameter("records")).getAsJsonObject();
    			    if (databaseProcess.addRecord( table_name,  records) == false){
    			         throw new Exception();
    			    } 
    			    break;
			    case "$readRecord":
			         columnArray = parser.parse(request.getParameter("columnName")).getAsJsonArray();
			         conditionArray = parser.parse(request.getParameter("conditionArray")).getAsJsonArray();
			         resp = databaseProcess.selectRecord( table_name,  columnArray, conditionArray,request.getParameter("andOr"));
			        break;
			    case "$deleteRecord":
			         conditionArray = parser.parse(request.getParameter("conditionArray")).getAsJsonArray();
			        if (databaseProcess.deleteRecord( table_name , conditionArray,request.getParameter("andOr")) == false ){
			             throw new Exception();
			         }
			        break;
			    case "$updateRecord":
			        JsonObject set = parser.parse(request.getParameter("set")).getAsJsonObject();
    			     conditionArray = parser.parse(request.getParameter("conditionArray")).getAsJsonArray();
			         if (databaseProcess.updateRecord( table_name, set ,conditionArray,request.getParameter("andOr")) == false ){
			             throw new Exception();
			         }
			        break;
			    default :
			        throw new Exception();
			
			}
		} catch (Exception e) {
			writer.write("{'status':400,'message':'Bad Reuest'}");
		}
		
	}
	
}