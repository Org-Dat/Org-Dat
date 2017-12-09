package ZUTK.B5.OrgDat.Model.DatabaseManagement;

import com.google.gson.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.text.*;
import java.sql.*;

public class ManageRecord{
	ArrayList<String> statementTypes;
	ArrayList<String> statementValues;
	ArrayList<String> columnNames;
	ArrayList<String> types ;
	String tableName;
	DatabaseConnection dc;
	public String deleteOrNot;
	public HashMap<String,ArrayList<String>> whereCondition;
	
	public ManageRecord(String db_name,String user_name,String password){
	    dc = new DatabaseConnection(db_name,user_name,password); 
		ArrayList<String> stringCondi = new ArrayList<String>();
		stringCondi.add("startsWith");
		stringCondi.add("endsWith");
		stringCondi.add("contains");
		stringCondi.add("equals");
		//{"contains","startsWith","endsWith"};
		ArrayList<String> intCondi = new ArrayList<String>();
		intCondi.add("=");
		intCondi.add("!=");
		intCondi.add(">");
		intCondi.add("<");
		//String[] intCondi = {"==","!=",">","<"};
		whereCondition = new HashMap<String,ArrayList<String>>();
		whereCondition.put("text",stringCondi);
		whereCondition.put("bigint",intCondi);
		whereCondition.put("numeric",intCondi);
		whereCondition.put("time",intCondi);
		whereCondition.put("date",intCondi);
	}
	
	public boolean updateRecord(String table_name,JsonObject set , JsonArray conditionArray,String andOr){
		try{
		    defineColumnAndType( table_name);
		    String condition = jsonToProcess( set ,  conditionArray, andOr );
	        String sql = "update  "+table_name+"  "+ condition;
	        dc.stmt = statementBuilder(dc.conn.prepareStatement(sql));
	       dc.stmt.executeUpdate();
	        return true;
	    }catch(Exception e){
	        return false;
	    }
	}
	/**
	 * defineColumnAndType(String table_name) this method used for  define clounmnames and type it is importent
	 * 
	*/ 
	private boolean defineColumnAndType(String table_name) {
	    tableName = table_name;
		try {
    	    String sql = "select column_name,column_default,data_type from information_schema.columns where table_name=? AND table_schema = 'public' ;";
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1,table_name);
			ResultSet rs = dc.stmt.executeQuery();
			columnNames = new ArrayList<String>();
			types = new ArrayList<String>();
			String columnName = "";
			while (rs.next()) {
				 columnName = rs.getString(1);
				String defaul = rs.getString(2);
				String typeTmp = rs.getString(3);
				if((typeTmp.equals("bigint") && defaul.matches("^nextval\\(.+")) == false){
                    columnNames.add(columnName);
                    types.add(typeTmp);
				} 
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public String selectRecord(String table_name, JsonArray columnArray,JsonArray conditionArray,String andOr){
	    try {
	        defineColumnAndType(table_name);
	        String columnName;
	        String sql = "SELECT ";
	        String columns = "";
	        if (columnArray == null ){
	            columns = " * ";
	        } else {

    	        for (JsonElement e : columnArray){
    	             columnName =  e.getAsString();
    	             if (columnNames.contains(columnName)){
    	                 columns = " , "+columnName;
    	             }
    	        }
    	        columns = columns.substring(2);
	        }
	        sql = sql + columns + " from " + tableName+" ";
	        String cointiton = contitionMaker(conditionArray, andOr);
	        dc.stmt = statementBuilder(dc.conn.prepareStatement(sql + cointiton));
	        ResultSet resultSet = dc.stmt.executeQuery();
	        ResultSetMetaData resultmeta = resultSet.getMetaData();
	        JsonArray returnValue = new JsonArray();
	        JsonArray tem = new JsonArray();
	        for (int i = 1 ; i <= resultmeta.getColumnCount() ; i++) {
	            tem.add(resultmeta.getColumnName(i)+"");
	        }
	        returnValue.add(tem);
	        tem = new JsonArray();
	        while (resultSet.next()){
	            for (int i = 1 ; i <= resultmeta.getColumnCount() ; i++) {
    	            tem.add(resultSet.getObject(i)+"");
    	        }
    	        returnValue.add(tem);
    	        tem = new JsonArray();
	        }
	        return returnValue.getAsString();
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	        return null;
	    }
	}
	/**
	 * this method used if create set and where contition 
	 */
	public String jsonToProcess(JsonObject set , JsonArray conditionArray,String andOr ){
	    try {
	        String setString = "";
	        String columnName;
			 if (set != null){
				for (int i = 0 ; i < columnNames.size();i++){
					columnName = columnNames.get(i);
					if (set.get(columnName) != null) {
						statementTypes.add(types.get(i));
						statementValues.add(set.get(columnName).getAsString());
						setString += "," + columnName + " = ? ";
					}
				}
			}
			String whereContition = contitionMaker(conditionArray,andOr);
			if (whereContition.contains("where") || whereContition.equals("") ){
				 return " set " + setString  + " " + whereContition;
			} else{
				 return  whereContition;
			}
 	    } catch (Exception e) {
	        return null ;
	    }
	    
	    
	}
	/**
	 * this method used  where contition 
	 */
	public String contitionMaker(JsonArray conditionArray,String andOr) {
    		if (conditionArray == null || conditionArray.size() == 0){
    			return "";
    		}
			String whereStr = "";
			
			String columnName;
			int index ; 
			String condition = "";
			if ((andOr == null ) || (! "OR".equals(andOr.toUpperCase()) && ! "AND".equals(andOr.toUpperCase()))){
				if (conditionArray.size() > 1) {
					return " and (or) or was not used ";
				} else {
					andOr = "";
				}
			}
			for (int i = 0 ; i < conditionArray.size();i++) {
				JsonObject conti = (JsonObject)conditionArray.get(i);
				columnName = conti.get("column").getAsString();
				index = columnNames.indexOf(columnName);
				if (index > -1) {
					condition = conti.get("condition").getAsString();
					if (types.get(index).equals("text") && whereCondition.get("text").contains(condition )) {
						statementTypes.add(condition);
						statementValues.add(conti.get("value").getAsString());
						whereStr += "," + columnName +" like  ? ";
					}  else if ( whereCondition.get(types.get(i)).contains(condition )){
						statementTypes.add(types.get(i));
						statementValues.add(conti.get("value").getAsString());
						whereStr += andOr+" " + columnName +" "+ condition + " ? ";
					} else { 
						return " opareter not definded";
					}
				}
			}
			if (whereStr.length() == 0){
				return "";
			}
			return " where "+whereStr.substring(andOr.length());
		
	}
	/**
	 * this method used  initialization  PreparedStatement
	 * but you already hava crete where
	 */
	private PreparedStatement statementBuilder(PreparedStatement statement){
		try {
		    String value;
			for (int i = 0; i < statementTypes.size(); i++) {
				if (statementTypes.get(i).equals("bigint")) {
				    value =  statementValues.get(i);
					int tmp = Integer.parseInt(value);
					statement.setInt(i, tmp);
				}else if (statementTypes.get(i).equals("numeric")) {
				    value =  statementValues.get(i);
					Double tmp = Double.parseDouble(value);
					statement.setDouble(i, tmp);
					
				} else if (statementTypes.get(i).equals("time")) {
				    value =   statementValues.get(i);
					Time time = (Time) new SimpleDateFormat("hh:mm:ss")
							.parse(value);
					statement.setTime(i, time);
				} else if (statementTypes.get(i).equals("equals")) {
				    value =  statementValues.get(i);
					statement.setString(i, value);
					
				} else if (statementTypes.get(i).equals("contains")) {
				    value = "%" + statementValues.get(i) + "%" ;
					statement.setString(i, value);
					
				} else if (statementTypes.get(i).equals("startsWith")) {
				    value =  statementValues.get(i)+"%" ;
					statement.setString(i, value);
					
				} else if (statementTypes.get(i).equals("endsWith")) {
				    value = "%" + statementValues.get(i);
					statement.setString(i, value);
					
				} else if (statementTypes.get(i).equals("date")) {
				    value =  statementValues.get(i);
					Date date = (Date) new SimpleDateFormat("dd/MM/yyyy")
							.parse(value);
					statement.setDate(i, date);
				}
			}
			return statement;
		} catch (Exception e ) {
			return null ;
		}
	}
	public boolean deleteRecord(String table_name, JsonArray conditionArray,String andOr){
	    try{
	        defineColumnAndType( table_name);
		    String condition = contitionMaker(conditionArray, andOr );
	        String sql = "delete from "+table_name+"  "+ condition;
	        dc.stmt = statementBuilder(dc.conn.prepareStatement(sql));
	        dc.stmt.executeUpdate();
	        return true;
	    }catch(Exception e){
	        return false;
	    }
	}
	public boolean addRecord(String table_name, JsonObject records) {
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
                        fields = fields + ", " + columnName;
                        values = values + ", ?";
                        columnNames.add(columnName);
                        types.add(typeTmp);
                    } else if ( nullIsAllow == false){
                        return false;
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
			return true;
		} catch (Exception e) {
		    System.out.println(e.getMessage());
			return false;
		}

	} 
	
}