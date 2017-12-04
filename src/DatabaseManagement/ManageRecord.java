package DatabaseManagement;

import javax.servlet.http.*;
import Filters.DatabaseConnection;
import com.google.gson.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.text.*;
import java.sql.Date;
import java.sql.Time;

public class ManageRecord extends HttpServlet {
	DatabaseConnection dc;
	private String deleteOrNot;

	protected void doPost(HttpServletRequest request, HttpServlet response) {
		dc = new DatabaseConnection("postgres","","");
	}

	private String addRecord(String table_name, JsonObject records) {
		try {
			String sql = "select column_name,column_default,data_type,is_nullable from information_schema.columns where table_name=? AND table_schema = 'public' ;";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, table_name);
			ResultSet rs = dc.stmt.executeQuery();
			ArrayList<String> columnNames = new ArrayList<String>();
			ArrayList<String> types = new ArrayList<String>();
			String fields = "";
			String values = "";
			while (rs.next()) {
				String columnName = rs.getString(1);
				String defaul = rs.getString(2);
				String typeTmp = rs.getString(3);
				boolean nullIsAllow  = rs.getBoolean(4);
				if((typeTmp.equals("bigint") && defaul.matches("^nextval\\(.+")) == false){
                    if (records.get(columnName) != null){
                        fields = fields + "," + columnName;
                        values = values + ",?";
                        columnNames.add(columnName);
                        types.add(typeTmp);
                    } else if ( nullIsAllow == false){
                        return columnName+" was mandatory but value not included";
                    }
				} 
			}
			sql = "insert into " + table_name + " " + fields.substring(1) + " values (" + values.substring(1) + ")";
			dc.stmt = dc.conn.prepareStatement(sql);
			String value ="";
			for (int i = 0; i < types.size(); i++) {
				if (types.get(i).equals("bigint")) {
				    value =  records.get(columnNames.get(i)).getAsString();
					int tmp = Integer.parseInt(value);
					dc.stmt.setInt(i, tmp);
				}else if (types.get(i).equals("numeric")) {
				    value =  records.get(columnNames.get(i)).getAsString();
					Double tmp = Double.parseDouble(value);
					dc.stmt.setDouble(i, tmp);
					
				} else if (types.get(i).equals("time")) {
				    value =  records.get(columnNames.get(i)).getAsString();
					Time time = (Time) new SimpleDateFormat("hh:mm:ss")
							.parse(value);
					dc.stmt.setTime(i, time);
				} else if (types.get(i).equals("text")) {
				    value = records.get(columnNames.get(i)).getAsString();
					dc.stmt.setString(i, value);
					
				} else if (types.get(i).equals("date")) {
				    value =  records.get(columnNames.get(i)).getAsString();
					Date date = (Date) new SimpleDateFormat("dd/MM/yyyy")
							.parse(value);
					dc.stmt.setDate(i, date);
				}
			}
			dc.stmt.executeUpdate();
			return "record added success fully";
		} catch (Exception e) {
			return e.getMessage();
		}

	}
	
	
	private boolean updateRecord(String table_name,String set,String condition){
	    
	    
	    return true;
	}
	
	private String[]  jsonToProcess(JsonObject set,JsonObject condition , String join){
	    String[] stringify = new String[2]; 
	    try {
    	    String sql = "select column_name,data_type from information_schema.columns where table_name=? AND table_schema = 'public' ;";
			dc.stmt = dc.conn.prepareStatement(sql);
			ResultSet rs = dc.stmt.executeQuery();
			ArrayList<String> columnNames = new ArrayList<String>();
			ArrayList<String> types = new ArrayList<String>();
			String setString = " ";
			String whereString = "";
			while (rs.next()) {
				String columnName = rs.getString(1);
				String defaul = rs.getString(2);
				String typeTmp = rs.getString(3);
				boolean nullIsAllow  = rs.getBoolean(4);
				if((typeTmp.equals("bigint") && defaul.matches("^nextval\\(.+")) == false){
			        columnNames.add(columnName);
                    types.add(typeTmp);
				} 
			}
			ArrayList<String> statementTypes = new ArrayList<String>();
			String columnName;
			for (int columnNum = 0 ;columnNum < columnNames.size();columnNum++){
			    columnName = columnNames.get(columnNum);
			    if (set.get(columnName) != null ) {
			        setString += columnName + " = ? , ";
			        statementTypes.add(types.get(columnNum));
			    }
			}
			for (int columnNum = 0 ;columnNum < columnNames.size();columnNum++){
			    columnName = columnNames.get(columnNum);
			    if (condition.get(columnName) != null ) {
			        whereString += columnName + join;
			        statementTypes.add(types.get(columnNum));
			    }
			}
        } catch (Exception e) {
            
        }
        
        return stringify ;
	}
	
	private boolean deleteRecord(String table_name,String condition){
	    try{
	        String sql = "delete from "+table_name+" where "+ condition;
	        dc.stmt = dc.conn.prepareStatement(sql);
	       // dc.stmt.setString(1, table_name);
	       // dc.stmt.setString(2, condition);
	        dc.stmt.executeUpdate();
	        return true;
	    }catch(Exception e){
	        return false;
	    }
	}
}
