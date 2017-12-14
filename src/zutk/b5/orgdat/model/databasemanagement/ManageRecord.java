package zutk.b5.orgdat.model.databasemanagement;

import com.google.gson.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.text.*;    
import java.sql.*;

public class ManageRecord{
	public ArrayList<String> statementTypes;
	ArrayList<String> statementValues;
	ArrayList<String> columnNames;
	ArrayList<String> types ;
	String tableName;
	DatabaseConnection dc;
	public String deleteOrNot;
	public HashMap<String,ArrayList<String>> whereCondition;
	
	public ManageRecord(String db_name,String user_name,String password){
	    dc = new DatabaseConnection(db_name,user_name,password); 
		 init();
	}
	
	public ManageRecord(DatabaseConnection datcon){
	    dc = datcon;
	     init();
	}
	public void init(){
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
	public String updateRecord(String org_name,String db_name ,String table_name,JsonObject set , JsonArray conditionArray,String andOr){
		try{
		    statementTypes = new ArrayList<String>();
	        statementValues = new ArrayList<String>();
		    defineColumnAndType( table_name);
	        System.out.println("column defined");
		    String condition = jsonToProcess( set ,  conditionArray, andOr );
	        System.out.println("condittiton defined = "+condition);
	        String sql = "update  "+table_name+"  "+ condition;
	        System.out.println("sql defined = "+sql);
	        dc.stmt = statementBuilder(dc.conn.prepareStatement(sql));
	        dc.stmt.executeUpdate();
			dc.stmt = dc.conn.prepareStatement("select * from "+table_name);
			ResultSet resultSet = dc.stmt .executeQuery();
	        return "{  \"status\" : 200, \"message\" : \"Record edit successfully !...\", \"Organization Name\" : \""+org_name+"\", \"Database Name \" : \""+db_name+"\", \"Table Table\"  : \""+table_name+"\",\"Records\" : "+resultToString(resultSet)+"} ";
		}catch(Exception e){
	        System.out.println("update error : "+ e);
	        return "{\"status\" : 400,\"message\":\"update error\"}";
	    }
	}
	/**
	 * defineColumnAndType(String table_name) this method used for  define clounmnames and type it is importent
	 * 
	*/ 
	public boolean defineColumnAndType(String table_name) {
	    tableName = table_name;
		try {
    	    String sql = "select column_name,column_default,data_type from information_schema.columns where table_name=? and table_schema = \"public\" ;";
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
				if (defaul == null){
				    columnNames.add(columnName);
                    types.add(typeTmp);
				} else if(!(typeTmp.equals("bigint") && defaul.matches("^nextval\\(.+$"))){
                    columnNames.add(columnName);
                    types.add(typeTmp);
				} 
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public String selectRecord(String org_name,String db_name ,String table_name, JsonArray columnArray,JsonArray conditionArray,String andOr){
	    try {
	        statementTypes = new ArrayList<String>();
	        statementValues = new ArrayList<String>();
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
    	                 columns = columns + " , "+columnName;
    	             }
    	        }
    	        columns = columns.substring(2);
	        }
	        sql = sql + columns + " from " + tableName+" ";
	        String condition = contitionMaker(conditionArray, andOr);
	        dc.stmt = statementBuilder(dc.conn.prepareStatement(sql + condition));
	        ResultSet resultSet = dc.stmt.executeQuery();
	        
	        dc.stmt.close();
	        dc.conn.close();
	       return "{  \"status\" : 200, \"message\" : \"Get Record Successfully !...\", \"Organization Name\" : \""+org_name+"\", \"Database Name \" : \""+db_name+"\", \"Table Table\"  : \""+table_name+"\",\"Records\" : "+resultToString(resultSet)+"} ";
		// return returnValue.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("select error"+e);
	        return "{\"status\" : 400,\"message\":\"read record error\"}";
	    }
	}
	
	
	private String resultToString(ResultSet resultSet){
	    try {
    	    ResultSetMetaData resultmeta = resultSet.getMetaData();
            JsonArray returnValue = new JsonArray();
            JsonArray tem = new JsonArray();
            for (int i = 1 ; i <= resultmeta.getColumnCount() ; i++) {
                tem.add(resultmeta.getColumnName(i)+"");
            }
            returnValue.add(tem);
            tem = new JsonArray();
            for (int i = 1 ; i <= resultmeta.getColumnCount() ; i++) {
                tem.add(resultmeta.getColumnTypeName(i)+"");
            }
            returnValue.add(tem);
            // returnValue.add(types);    
            tem = new JsonArray();
            while (resultSet.next()){
                for (int i = 1 ; i <= resultmeta.getColumnCount() ; i++) {
    	            tem.add(resultSet.getObject(i)+"");
    	        }
    	        returnValue.add(tem);
    	        tem = new JsonArray();
            } 
            return returnValue.toString();
	    } catch (Exception e ){
	        return null;
	    }
	}

	/**
	 * this method used if create set and where contition 
	 */
	public String jsonToProcess(JsonObject set , JsonArray conditionArray,String andOr ){
	    try {
	        
			System.out.println(set);
	        String setString = "";
	        String columnName;
			 if (set != null){
				for (int i = 0 ; i < columnNames.size();i++){
					columnName = columnNames.get(i);
					System.out.println(columnName);
					if (set.get(columnName) != null) {
					    
					System.out.println("columnName = "+columnName);
						statementTypes.add(types.get(i));
						statementValues.add(set.get(columnName).getAsString());
						setString += "," + columnName + " = ? ";
					}
				}
			}
			return " set " + setString.substring(1)  + " " + contitionMaker(conditionArray,andOr);
			
 	    } catch (Exception e) {
	        return null ;
	    }
	    
	    
	}
	/**
	 * this method used  where contition 
	 */
	public String contitionMaker(JsonArray conditionArray,String andOr) {
	    System.out.println("conditionArray = "+conditionArray);
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
				    System.out.println("and or not");
					andOr = "";
				}
			}
			for (int i = 0 ; i < conditionArray.size();i++) {
				JsonObject conti = (JsonObject)conditionArray.get(i);
				System.out.println("conti = "+conti);
				columnName = conti.get("column").getAsString();
				index = columnNames.indexOf(columnName);
				if (index > -1) {
					condition = conti.get("condition").getAsString();
					if (types.get(index).equals("text") && whereCondition.get("text").contains(condition )) {
						statementTypes.add(condition);
						
						statementValues.add(conti.get("value").getAsString());
						whereStr += andOr+" " + columnName +" like  ? ";
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
		    System.out.println("statementTypes = "+statementTypes);
		    System.out.println("statementValues = "+statementValues);
			for (int i = 0; i < statementTypes.size(); i++) {
				if (statementTypes.get(i).equals("bigint")) {
				    value =  statementValues.get(i);
					int tmp = Integer.parseInt(value);
					statement.setInt(i+1, tmp);
				}else if (statementTypes.get(i).equals("numeric")) {
				    value =  statementValues.get(i);
					Double tmp = Double.parseDouble(value);
					statement.setDouble(i+1, tmp);
					
				} else if (statementTypes.get(i).equals("time")) {
				    value =   statementValues.get(i);
					Time time = (Time) new SimpleDateFormat("hh:mm:ss")
							.parse(value);
					statement.setTime(i, time);
				} else if (statementTypes.get(i).equals("equals")) {
				    value =  statementValues.get(i);
					statement.setString(i+1, value);
					
				} else if (statementTypes.get(i).equals("contains")) {
				    value = "%" + statementValues.get(i) + "%" ;
					statement.setString(i+1, value);
					
				} else if (statementTypes.get(i).equals("text")) {
				    value =  statementValues.get(i) ;
					statement.setString(i+1, value);
					
				} else if (statementTypes.get(i).equals("startsWith")) {
				    value =  statementValues.get(i)+"%" ;
					statement.setString(i+1, value);
					
				} else if (statementTypes.get(i).equals("endsWith")) {
				    value = "%" + statementValues.get(i);
					statement.setString(i+1, value);
					
				} else if (statementTypes.get(i).equals("date")) {
				    value =  statementValues.get(i);
					Date date = (Date) new SimpleDateFormat("dd/MM/yyyy")
							.parse(value);
					statement.setDate(i+1, date);
				}
			}
			return statement;
		} catch (Exception e ) {
		    System.out.println("prepar builder errror : "+e);
			return null ;
		}
	}
	public String deleteRecord(String org_name,String db_name ,String table_name, JsonArray conditionArray,String andOr){
	    try{
	        defineColumnAndType( table_name);
	        statementTypes = new ArrayList<String>();
	        statementValues = new ArrayList<String>();
	        System.out.println("column defined");
		    String condition = contitionMaker(conditionArray, andOr );
	        System.out.println("condittiton defined = "+condition);
	        String sql = "delete from "+table_name+"  "+ condition;
	        System.out.println("sql defined = "+sql);
	        dc.stmt = statementBuilder(dc.conn.prepareStatement(sql));
	        dc.stmt.executeUpdate();
			dc.stmt = dc.conn.prepareStatement("select * from "+table_name);
			ResultSet resultSet = dc.stmt .executeQuery();
			return "{\"status\":200,\"message\":\"get record success fully\" ,\"Records\":"+resultToString(resultSet)+"}";
	    }catch(Exception e){ 
	        System.out.println("delete record error : "+e);
	        return "{\"status\" : 400,\"message\":\"delete record error\"}";
	    }
	}
	public String addRecord(String org_name,String db_name ,String table_name, JsonObject records) {
		try {
		    System.out.println(records.get("werwer"));
			String sql = "select column_name,column_default,data_type,is_nullable from information_schema.columns where table_name=? ;";
			dc.stmt = dc.conn.prepareStatement(sql);
			
			dc.stmt.setString(1, table_name);
			ResultSet rs = dc.stmt.executeQuery();
			System.out.println("addrecord");
			ArrayList<String> columnNames = new ArrayList<String>();
			ArrayList<String> types = new ArrayList<String>();
			String fields = "";
			String values = "";
			while (rs.next()) {
				String columnName = rs.getString(1);
				String defaul = rs.getString(2);
				String typeTmp = rs.getString(3);
				boolean nullIsAllow  = rs.getBoolean(4);
				if(defaul == null || (typeTmp.equals("bigint") && defaul.matches("^nextval\\(.+")) == false){
                    if (records.get(columnName) != null){
                        fields = fields + ", " + columnName;
                        values = values + ", ?";
                        columnNames.add(columnName);
                        types.add(typeTmp);
                    } else if ( nullIsAllow == false){
                        return "{\"status\" : 400,\"message\":\"" +columnName +" is Mandatory but you did not put value \"}";
                    }
				}  
			}
			sql = "insert into " + table_name + " (" + fields.substring(1) + ") values (" + values.substring(1) + ")";
			dc.stmt = dc.conn.prepareStatement(sql);
			System.out.println("sql = " +sql);
			System.out.println("columnNames  = "+columnNames);
			System.out.println("types " +types);
			String value ="";
			for (int i = 0; i < types.size(); i++) {
				if (types.get(i).equals("bigint")) {
				    value =  records.get(columnNames.get(i)).getAsString();
					int tmp = Integer.parseInt(value);
					dc.stmt.setInt(i+1, tmp);
				}else if (types.get(i).equals("numeric")) {
				    value =  records.get(columnNames.get(i)).getAsString();
					Double tmp = Double.parseDouble(value);
					dc.stmt.setDouble(i+1, tmp);
					
				} else if (types.get(i).equals("time")) {
				    value =  records.get(columnNames.get(i)).getAsString();
					Time time = (Time) new SimpleDateFormat("hh:mm:ss")
							.parse(value);
					dc.stmt.setTime(i+1, time);
				} else if (types.get(i).equals("text")) {
				    value = records.get(columnNames.get(i)).getAsString();
					dc.stmt.setString(i+1, value);
					
				} else if (types.get(i).equals("date")) {
				    value =  records.get(columnNames.get(i)).getAsString();
					Date date = (Date) new SimpleDateFormat("dd/MM/yyyy")
							.parse(value);
					dc.stmt.setDate(i+1, date);
				}
			}
			dc.stmt.executeUpdate();
			
			dc.stmt = dc.conn.prepareStatement("select * from "+table_name);
			ResultSet resultSet = dc.stmt .executeQuery();
			return "{  \"status\" : 200, \"message\" : \"record successfully added\", \"Organization Name\" : \""+org_name+"\", \"Database Name \" : \""+db_name+"\", \"Table Table\"  : \""+table_name+"\",\"Records\" : "+resultToString(resultSet)+"} ";
		} catch (Exception e) { 
		    e.printStackTrace();
		    System.out.println("addrecord error = "+e);
	        return "{\"status\" : 400,\"message\":\"add record unsuccess fully\"}";
		} 
	} 
	
}