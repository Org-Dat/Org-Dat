package ZUTK.B5.OrgDat.Model.DatabaseManagement;

import java.sql.*;
import com.google.gson.*;
import java.util.*;

public class ManageTable {
	Set<String> types;
	PreparedStatement stmt = null;
	DatabaseConnection dc = null;

	public void init() {
		types = new HashSet<String>();
		types.add("bigint");
		types.add("numeric");
		types.add("time");
		types.add("date");
		types.add("timestamp");
	}

	public ManageTable(String user, String db_name, String password) {
		dc = new DatabaseConnection(user + "_" + db_name, user, "");
		init();
	}

	/**
	 * This private methos used to bulidAlterQuery for alter table query
	 * 
	 * @Params : JsonArray columnArray , String wanted
	 * 
	 * @Return : This private methos returned bulid alter query String
	 */

	public String bulidAlterQuery(JsonArray columnArray, String wanted) {
		String query = "";
		try {
			JsonObject columnObj = null;
			for (JsonElement eachElement : columnArray) {
				columnObj = eachElement.getAsJsonObject();
				String columnName = columnObj.get("column").getAsString();
				if (columnName.matches("^[a-z][a-z0-9_]{4,30}$") == true) {
					throw new Exception();
				}
				if (wanted.equals("addColumn") == true) {
					/****************************** Add Column ********************************************/
					String type = columnObj.get("type").getAsString();
					String defaul = columnObj.get("default").getAsString();
					if (types.contains(type) == false) {
						throw new Exception();
					}
					query = query + ", ADD COLUMN  " + columnName + " " + type
							+ " default ";
					if (type.equals("bigint") || type.equals("numeric")) {
						query = query + " " + defaul;
					} else {
						query = query + " '" + escapeing(defaul) + "'";
					}
				}
				/****************************** deleteColumn ********************************************/
				else if (wanted.equals("deleteColumn") == true) {
					query = query + ", DROP COLUMN  " + columnName + " ";
				}
				/****************************** typeChange ********************************************/
				else if (wanted.equals("typeChange") == true) {
					String type = columnObj.get("type").getAsString();
					if (types.contains(type) == false) {
						throw new Exception();
					}
					query = query + ", ALTER COLUMN  " + columnName + " TYPE "
							+ type + " ";
				}
				/****************************** manageDefault ********************************************/
				else if (wanted.equals("manageDefault") == true) {
					String defaul = columnObj.get("default").getAsString();
					query = query + ", ALTER COLUMN  " + columnName
							+ " SET DEFAULT " + defaul + " ";
				}
				/****************************** deleteDefault ********************************************/
				else if (wanted.equals("deleteDefault") == true) {
					query = query + ", ALTER COLUMN  " + columnName
							+ " DROP DEFAULT ";
				}
				/****************************** setDefault ********************************************/
				else if (wanted.equals("setNotNull") == true) {
					query = query + ", ALTER COLUMN  " + columnName
							+ " SET NOT NULL ";
				}
				/****************************** deleteDefault ********************************************/
				else if (wanted.equals("deleteNotNull") == true) {
					query = query + ", ALTER COLUMN  " + columnName
							+ " DROP DEFAULT ";
				}
				/******** for loop end *****/
			}
			return query.substring(1);
		} catch (Exception e) {

			return "";
		}
	}

	/**
	 * This private methos used to escapeing String
	 * 
	 * @Params : String text
	 * 
	 * @Return : This private methos returned fully escaped String
	 */

	private String escapeing(String text) {
		StringBuilder answer = new StringBuilder("");
		for (int i = 0; i < text.length(); i++) {
			if ((text.charAt(i) == '\"') || (text.charAt(i) == '\\')
					|| (text.charAt(i) == '\'')) {
				answer.append('\\');
			}
			answer.append(text.charAt(i));
		}
		return answer.toString();
	}

	/**
	 * This private methos used to delete table from database
	 * 
	 * @Params : String org_name ,String db_name , String table_name
	 * 
	 * @Return : if table delete successfully it's return true, else return
	 *         false
	 */

	public boolean deleteTable(String org_name, String db_name,
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
		} finally {
			try {
				dc.conn.close();
			} catch (Exception e) {

			}
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
	public boolean renameTable(String org_name, String db_name,
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
			sqlQuery = "alter table " + table_oldname + " rename to "
					+ table_newname + "";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				dc.conn.close();
			} catch (Exception e) {

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

	public boolean createTable(String org_name, String db_name,
			String table_name, String columnNames, long user_id) {
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
			dc.dbConnection(db_name, org_name + "_" + db_name, "");

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
		} finally {
			try {
				dc.conn.close();
			} catch (Exception e) {

			}
		}
	}

	/**
	 * This public method used to alter table in database.
	 * 
	 * @Params : String table_name , JsonArray columnArray , String wanted
	 * 
	 * @Return : if table successfully alter return true, else false;
	 */

	public boolean alterColumn(String table_name, JsonArray columnArray,
			String wanted) {
		try {
			String sql = "";
			sql = "alter table " + table_name + "  "
					+ bulidAlterQuery(columnArray, wanted);
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				dc.conn.close();
			} catch (Exception e) {

			}
		}
	}

   	public boolean shareTable(String org_name, String db_name,
			String table_name, String role, long user_id, String query) {
		try {
		    dc = new DatabaseConnection("postgres","postgres","");
			String sql = "";
			if (query.equals("createRole")) {
				sql = "insert into table_management (role,user_id,org_name,db_name,table_name)values(?,?,?,?,?)";
			} else if (query.equals("editRole")) {
				sql = "update table_management set role= ? where user_id=? and org_name=? and db_name=? and table_name=?";
			} else {
				sql = "delete from table_management where role=? and user_id=? and org_name=? and db_name=? and table_name=?";
			}
			dc = new DatabaseConnection(org_name + "_" + db_name, org_name, "");
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, role);
			dc.stmt.setLong(2, user_id);
			dc.stmt.setString(3, org_name);
			dc.stmt.setString(4, db_name); 
			dc.stmt.setString(5, table_name);
			dc.stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}