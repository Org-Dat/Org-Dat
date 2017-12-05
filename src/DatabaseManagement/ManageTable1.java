/**
 * This Servlet used to manage the table.
 * 
 * @author : Obeth Samuel & PonKumar
 * 
 * @version 1.0
 */
package DatabaseManagement;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import Filters.*;
import com.google.gson.*;
interface QueryBuilder {
    public String  bulidQuery();
}
class AddColumn implements  QueryBuilder {
    public String bulidQuery(){
        
         return "";
    }
}
class DropColumn implements  QueryBuilder {
    public String bulidQuery(){
        
         return "";
    }
}
class TypeChange implements  QueryBuilder {
    public String bulidQuery(){
        
         return "";
    }
}
class ColumnDefault implements  QueryBuilder {
    public String bulidQuery(){
        
         return "";
    }
}

public class ManageTable1 extends HttpServlet {
	DatabaseConnection dc = null;
	PreparedStatement stmt = null;
	Set<String> types;
	HashMap<String, QueryBuilder> queryTypes ;
	long user_id;
    public void init(){
        types = new HashSet<String>();
        types.add("bigint");
        types.add("numeric");
        types.add("time");
        types.add("date");
        types.add("timestamp");
        
    }
	/**
	 * This Method used to split table manage work to other method.
	 * 
	 * @Params : request HttpServletRequest , response HttpServletResponse
	 * 
	 * @Return : this method return the work response.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
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
			/***************************** Create Table ********************************************/
			if (isCorrect(table_name) == false || isCorrect(org_name) == false
					|| isCorrect(db_name) == false) {

				throw new Exception();
			}
			dc = new DatabaseConnection(db_name, org_name, "");
			user_id = getUserId(request.getCookies());
			String reqURI = path[3];
			if (reqURI.equals("createTable") == true) {
				String columns = request.getParameter("columnArray");
				if (createTable(org_name, db_name, table_name, columns) == true) {

					writer.write("{'status':200,'org_name':'" + org_name
							+ "','db_name':'" + db_name + "','table_name':'"
							+ table_name + "'}");
				} else {
					throw new Exception();
				}
			}
			/***************************** Rename Table ********************************************/
			else if (reqURI.equals("renameTable") == true) {
				String newTable = request.getParameter("newtable_name");
				if (isCorrect(newTable) == false) {

					throw new Exception();
				}
				if (renameTable(org_name, db_name, table_name, newTable) == true) {
					writer.write("{'status':200,'org_name':'" + org_name
							+ "','db_name':'" + db_name + "','table_name':'"
							+ table_name + "'}");
				} else {
					throw new Exception();
				}
			}
			/***************************** Delete Table ********************************************/
			else if (reqURI.equals("deleteTable") == true) {
				if (deleteTable(org_name, db_name, table_name) == true) {

					writer.write("{'status':200,'org_name':'" + org_name
							+ "','db_name':'" + db_name + "','table_name':'"
							+ table_name + "'}");
				} else {
					throw new Exception();
				}
			}
			/***************************** Alter Table ********************************************/
			else if (reqURI.equals("alterDatatype") == true
					|| reqURI.equals("alterTable")) {
				String column = request.getParameter("column");
				String constraint = "";
				if (reqURI.equals("alterDatatype")) {
					constraint = request.getParameter("dataType");
					constraint = "type " + constraint;
				} else {
					constraint = request.getParameter("constraint");
					if (request.getParameter("isRemove").equals("true")) {
						constraint = "drop " + constraint;
					} else {
						constraint = "set " + constraint;
					}

				}

				String columnType = request.getParameter("type");
				if (column != null
						&& isCorrect(column) == true
						&& alterColumnType(column + "" + constraint,
								table_name, db_name, org_name) == true) {
					dc.conn.close();
					writer.write("{'status':200,'org_name':'" + org_name
							+ "','db_name':'" + db_name + "','table_name':'"
							+ table_name + "'}");
				} else {
					throw new Exception();
				}
			}
			/****************************** Add Column ********************************************/
			else if (reqURI.equals("addColumn") == true) {
				String columnNameArray = request.getParameter("query");
				String column = "";
				JsonArray columnArray =new JsonParser().parse(columnNameArray).getAsJsonArray();
				JsonObject columnObj = null;
				for (JsonElement eachElement : columnArray) {
				    columnObj = eachElement.getAsJsonObject();
					String colum = columnObj.get("column").getAsString();
					String type = columnObj.get("type").getAsString();
					String defaul = columnObj.get("default").getAsString();
					if (types.contains(type) == false){
					    throw new Exception();
					}
					if (isCorrect(column) == true) {
    					column = column + ", ADD COLUMN  " + colum+ " " + type+ " default " ;
    					if(type.equals("bigint") || type.equals("numeric") ){
    					  column = column + " " +  defaul;
    					} else {
    					    column = column + " '" +escapeing(defaul)+"'";
    					}
					} else {
					    throw new Exception();
					}
				}
				if (alterColumn( column.substring(1), table_name, db_name,org_name) == true) {
					dc.conn.close();
					writer.write("{'status':200,'org_name':'" + org_name
							+ "','db_name':'" + db_name + "','table_name':'"
							+ table_name + "'}");
				} else {
					throw new Exception();
				}
			}
			/****************************** modiy Column dataType********************************************/
			else if (reqURI.equals("typeChange") == true) {
				String columnNameArray = request.getParameter("query");
				String column = "";
				JsonArray columnArray =new JsonParser().parse(columnNameArray).getAsJsonArray();
				JsonObject columnObj = null;
				for (JsonElement eachElement : columnArray) {
				    columnObj = eachElement.getAsJsonObject();
					String columnName = columnObj.get("column").getAsString();
					String type = columnObj.get("type").getAsString();
					if (types.contains(type) == false){
					    throw new Exception();
					}
					if (isCorrect(column) == true) {
    					column = column + ", ALTER COLUMN  " + columnName + " TYPE " + type+ " " ;
					} else {
					    throw new Exception();
					}
					 
				}
				if (alterColumn( column.substring(1), table_name, db_name,org_name) == true) {
					dc.conn.close();
					writer.write("{'status':200,'org_name':'" + org_name
							+ "','db_name':'" + db_name + "','table_name':'"
							+ table_name + "'}");
				} else {
					throw new Exception();
				} 
			}
			/****************************** set Column default ********************************************/
			else if (reqURI.equals("manageDefault") == true  ) {
				String columnNameArray = request.getParameter("query");
				String column = "";
				JsonArray columnArray =new JsonParser().parse(columnNameArray).getAsJsonArray();
				JsonObject columnObj = null;
				for (JsonElement eachElement : columnArray) {
				    columnObj = eachElement.getAsJsonObject();
					String columnName = columnObj.get("column").getAsString();
					String defaul = columnObj.get("default").getAsString();
					if (isCorrect(column) == true) {
    					column = column + ", ALTER COLUMN  " + columnName + " SET DEFAULT " + defaul+ " " ;
					} else {
					    throw new Exception();
					}
				}
				if (alterColumn( column.substring(1), table_name, db_name,org_name) == true) {
					dc.conn.close();
					writer.write("{'status':200,'org_name':'" + org_name
							+ "','db_name':'" + db_name + "','table_name':'"
							+ table_name + "'}");
				} else {
					throw new Exception();
				}
			}
			/***************************** Delete Column ********************************************/
			else if (reqURI.equals("deleteColumn") == true) {
				String columnNameArray = request.getParameter("query");
				String column = "";
				JsonArray columnArray =new JsonParser().parse(columnNameArray).getAsJsonArray();
				for (JsonElement eachElement : columnArray) {
					String colum = eachElement.getAsString();
					if (isCorrect(column) == true) {
    					column = column + ", DROP COLUMN  " + colum+ " " ;
					} else {
					    throw new Exception();
					}
					
				}
				if (alterColumn( column.substring(1), table_name, db_name,org_name) == true) {
					dc.conn.close();
					writer.write("{'status':200,'org_name':'" + org_name
							+ "','db_name':'" + db_name + "','table_name':'"
							+ table_name + "'}");
				} else {
					throw new Exception();
				}
			}
			else if (true) {
				String columnNameArray = request.getParameter("query");
				String column = "";
				JsonArray columnArray =new JsonParser().parse(columnNameArray).getAsJsonArray();
				JsonObject columnObj = null;
				for (JsonElement eachElement : columnArray) {
				    columnObj = eachElement.getAsJsonObject();
					String colum = columnObj.get("column").getAsString();
					String type = columnObj.get("type").getAsString();
					String defaul = columnObj.get("default").getAsString();
					if (types.contains(type) == false){
					    throw new Exception();
					}
					if (isCorrect(column) == true) {
    					column = column + ", ADD COLUMN  " + colum+ " " + type+ " default " ;
    					if(type.equals("bigint") || type.equals("numeric") ){
    					  column = column + " " +  defaul;
    					} else {
    					    column = column + " '" +escapeing(defaul)+"'";
    					}
					    
					} else {
					    throw new Exception();
					}
					 
				}
				if (alterColumn( column.substring(1), table_name, db_name,org_name) == true) {
					dc.conn.close();
					writer.write("{'status':200,'org_name':'" + org_name
							+ "','db_name':'" + db_name + "','table_name':'"
							+ table_name + "'}");
				} else {
					throw new Exception();
				}
			 }
			else {
				throw new Exception();
			}
		} catch (Exception e) {
			writer.write("{'status':400,'message':'Bad Reuest'}"); 
		} finally {
			try {
				dc.conn.close();
			} catch (SQLException e) {
				System.out.println("Connection Not Close");
			}
		}
	}

	/**
	 * This private method used to create new table in database.
	 * 
	 * @Params : String org_name , String db_name , String table_name
	 * 
	 * @Return : if table successfully create return true, else false;
	 */

	private boolean createTable(String org_name, String db_name,
			String table_name, String columnNames) {
		try {
			String sqlQuery = "insert into table_management (roles,table_name,db_name,org_name,user_id) values(?,?,?,?,?)";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(1, "co-owner");
			stmt.setString(2, table_name);
			stmt.setString(3, db_name);
			stmt.setString(4, org_name);
			stmt.setLong(5, user_id);
			stmt.executeUpdate();

			JsonParser parser = new JsonParser();
			JsonElement tradeElement = parser.parse(columnNames);
			JsonArray trade = tradeElement.getAsJsonArray();
			dc.dbConnection(db_name, org_name + "" + db_name, "");

			StringBuilder s = new StringBuilder("");
			for (JsonElement eachColumn : trade) {
				JsonArray column = eachColumn.getAsJsonArray();
				String columnType = "";
				for (JsonElement typeAndName : column) {
					columnType = columnType + typeAndName + " ";
				}
				s.append(columnType + ",");
			}
			sqlQuery = "create table " + table_name + "(" + s.toString() + ")";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.executeUpdate();

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * This private method user to find user id
	 * 
	 * @Params : Cookie[] cookie
	 * 
	 * @Return : if vaild user return his/her user id ,else return -1
	 */

	private long getUserId(Cookie[] cookies) {
		ServletContext context = getServletContext();
		for (Cookie cookie : cookies) {
			if ((cookie.getName()).equals("iambdt")) {
				return (long) context.getAttribute(cookie.getValue());
			}
		}
		return -1;
	}

	/**
	 * This private method used to find table name is correct or wrong
	 * 
	 * @Params : String table_name
	 * 
	 * @Return : if name match to regex return true,else return false
	 */

	private boolean isCorrect(String table_name) {
		return table_name.matches("^[a-z][a-z0-9]{3,30}$");
	}

	/**
	 * This private methos used to delete table from database
	 * 
	 * @Params : String org_name ,String db_name , String table_name
	 * 
	 * @Return : if table delete successfully it's return true, else return
	 *         false
	 */

	private boolean deleteTable(String org_name, String db_name,
			String table_name) {
		try {
			String sqlQuery = "delete from table_manament where org_name=? and db_name=? and table_name=?";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(1, org_name);
			stmt.setString(2, db_name);
			stmt.setString(3, table_name);
			stmt.executeUpdate();
			
			dc.dbConnection(db_name, org_name + "" + db_name, "");
			sqlQuery = "drop table " + table_name;
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(1, table_name);
			stmt.executeUpdate();
			stmt.close();
			dc.conn.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * This private method used to rename the table
	 * 
	 * @Params : String org_name ,String db_name , String oldtable_name
	 * 
	 * @Return : if table name successfully changed it's return true, else
	 *         return false
	 */
	private boolean renameTable(String org_name, String db_name,
			String table_oldname, String table_newname) {
		try {
			String sqlQuery = "update table_manament set table_name=? where org_name=? and db_name=? and table_name=?";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(1, table_newname);
			stmt.setString(2, org_name);
			stmt.setString(3, db_name);
			stmt.setString(4, table_oldname);
			stmt.executeUpdate();
			dc.dbConnection(db_name, org_name + "" + db_name, "");
			sqlQuery = "alter table "+table_oldname+" rename to "+table_newname+"";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean alterColumn( String column,String table_name, String db_name, String org_name) {
		try {
			String sql = "";
			sql = "alter table " + table_name + "  " + column;
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean alterColumnType(String column_name, String org_name,
			String db_name, String table_name) {
		try {
			String sql = "alter table " + table_name + " add column "
					+ column_name;
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private String escapeing(String value) {
	    StringBuilder answer = new StringBuilder("");
	    for (int i = 0 ;i < value.length();i++ ) {
	        if ((value.charAt(i) == '\"') ||  (value.charAt(i) == '\\') || (value.charAt(i) == '\'')){
	            answer.append('\\');
	        }
	        answer.append(value.charAt(i));
	    }
	    return answer.toString();
	}
}
