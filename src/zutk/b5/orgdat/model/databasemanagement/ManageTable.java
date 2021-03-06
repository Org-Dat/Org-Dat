package zutk.b5.orgdat.model.databasemanagement;

import java.sql.*;
import com.google.gson.*;
import org.json.simple.*;
import java.util.*;
import zutk.b5.orgdat.controllers.filters.DatabaseConnection;

public class ManageTable {
	Set<String> types;
	PreparedStatement stmt = null;
	DatabaseConnection dc = null;
	ManageRecord mr;

	public void init() {
		types = new HashSet<String>();
		types.add("bigint");
		types.add("text");
		types.add("numeric");
		types.add("time");
		types.add("date");
		types.add("timestamp");
	}

	public ManageTable(String user, String db_name, String password) {
		dc = new DatabaseConnection(user + "_" + db_name, user, "");
		init();
	}

	public ManageTable(DatabaseConnection password) {
		dc = password;
		init();
	}

	public void close() {
		if (dc != null) {
			dc.close();
		}
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
			System.out.println(" e " + columnArray);
			System.out.println(wanted);
			JsonObject columnObj = null;
			// if (wanted.equals("renameColumn") && columnArray.size() > 1) {
			// return
			// "E{'status':406,'message':' column rename does not support multiple in one request '}";
			// }
			System.out.println(" 0 0 ");
			for (JsonElement eachElement : columnArray) {
				System.out.println("fefvv --> 1");

				columnObj = eachElement.getAsJsonObject();
				String columnName = columnObj.get("column").getAsString();
				System.out.println("???????????????????????????????????????");
				System.out.println(wanted);
				if (wanted.equals("addColumn") == false
						&& mr.columnNames.contains(columnName) == false) {
					return "E{ 'status' : 406, 'message' : 'Your Column Name is incorrect' }";
				}
				if (wanted.equals("addColumn") == true) {
					/****************************** Add Column ********************************************/
					String type = columnObj.get("type").getAsString();
					System.out
							.println(" d  s s   d f D F  fd f  d s f  s d  f  s  d  s  d  f  f  ");
					if (types.contains(type.toLowerCase()) == false) {
						return "E{ 'status' : 406, 'message' : 'Your Column Type is doesn't support in myapp ' }";
					}
					query = query + ", ADD COLUMN  " + columnName + " " + type
							+ "  ";
					JsonElement defaul = columnObj.get("default");
					String defaultVal = "";
					if (columnObj.get("isNull") != null) {
						if (columnObj.get("isNull").getAsBoolean() == false) {
							query = query + " NOT ";
						}
						query = query + " NULL ";
					}
					defaultVal = defaul.getAsString();
					System.out.println(defaultVal.length());
					System.out.println(defaultVal != null);
					System.out.println("".equals(defaultVal) == false);
					if (defaultVal != null && "".equals(defaultVal) == false
							&& "\"\"".equals(defaultVal) == false) {
						// defaultVal = defaul.getAsString();
						if (type.matches("^(bigint|numeric)$")) {
							query = query + " default " + defaultVal;
						} else {
							query = query + " default '"
									+ escapeing(defaultVal) + "'";
						}

					}

					System.out.println("process");
				}
				/****************************** deleteColumn ********************************************/
				else if (wanted.equals("deleteColumn") == true) {
					query = query + ", DROP COLUMN  " + columnName + " ";
				}
				/****************************** typeChange ********************************************/
				// else if (wanted.equals("typeChange") == true) {
				// String type = columnObj.get("type").getAsString();
				// if (types.contains(type.toLowerCase()) == false) {
				// return
				// "E{ 'status' : 406, 'message' : 'Your Column Type is doesn't support in myapp ' }"
				// ;
				// }
				// query = query + ", ALTER COLUMN  " + columnName + " TYPE "
				// + type + " ";
				// }
				// /****************************** renameColumn
				// ********************************************/
				// else if (wanted.equals("renameColumn") == true) {
				// String new_name =
				// columnObj.get("newcolumn_name").getAsString();
				// if (mr.columnNames.contains(new_name) ||
				// new_name.matches("^[a-z][a-z0-9_]{3,30}$")==false) {
				// System.out.println("new_name = "+new_name);
				// return
				// "E{ 'status' : 406, 'message' : 'Your Column Name is incorrect' }"
				// ;
				// }
				// query = query + ", RENAME  " + columnName + " TO "
				// + new_name + " ";
				// }
				// /****************************** manageDefault
				// ********************************************/
				// else if (wanted.equals("manageDefault") == true) {
				// String defaul = columnObj.get("default").getAsString();
				// query = query + ", ALTER COLUMN  " + columnName
				// + " SET DEFAULT ";
				// if (defaul == null) {
				// return
				// "E{ 'status' : 406, 'message' : 'You doesn't give a default value,if you wand remove default value you want to use is incorrect' }"
				// ;
				// }
				// if
				// (mr.types.get(mr.columnNames.indexOf(columnName)).matches("^(bigint|numeric)$"))
				// {
				// query = query + " " +defaul+" ";
				// } else {
				// query = query + " '" +escapeing(defaul)+"' ";
				// }
				// }
				// /****************************** deleteDefault
				// ********************************************/
				// else if (wanted.equals("deleteDefault") == true) {
				// query = query + ", ALTER COLUMN  " + columnName
				// + " DROP DEFAULT ";
				// }
				// /****************************** setDefault
				// ********************************************/
				// else if (wanted.equals("setNotNull") == true) {
				// query = query + ", ALTER COLUMN  " + columnName
				// + " SET NOT NULL ";
				// }
				// /****************************** deleteDefault
				// ********************************************/
				// else if (wanted.equals("deleteNotNull") == true) {
				// query = query + ", ALTER COLUMN  " + columnName
				// + " DROP DEFAULT ";
				// }
				/******** for loop end *****/
			}
			return query.substring(1);
		} catch (Exception e) {
			System.out.println("manage table alter build  erroir : " + e);
			return "E{ 'status' : 406, 'message' : 'Invaild input' }";
		}
	}

	public String editColumn(String org_name, String db_name,
			String table_name, JsonObject columnObject) {
		try {
		    System.out.println(" edit column enter = "+columnObject);
			mr = new ManageRecord(dc);
			String columnName = columnObject.get("columnName").getAsString();
			String new_name = columnObject.get("new_name").getAsString();
			String type = columnObject.get("type").getAsString();
			String defaul = columnObject.get("default").getAsString();
			boolean isNull = columnObject.get("isNull").getAsBoolean();
			String query = "";
			mr.defineColumnAndType(org_name, db_name, table_name);
			if (types.contains(type.toLowerCase())) {
				query = query + ", ALTER COLUMN  " + columnName + " TYPE "
						+ type + " ";
			}
			if (defaul != null && defaul.equals("") == false ) {
				query = query + ", ALTER COLUMN  " + columnName
						+ " SET DEFAULT ";
				if (mr.types.get(mr.columnNames.indexOf(columnName)).matches(
						"^(bigint|numeric)$")) {
					query = query + " " + defaul + " ";
				} else {
					query = query + " '" + escapeing(defaul) + "' ";
				}
			}
			if (isNull == false) {
				query = query + ", ALTER COLUMN  " + columnName
						+ " SET NOT NULL ";
			} else {
				query = query + ", ALTER COLUMN  " + columnName
						+ " DROP DEFAULT ";
			}
			if (new_name != null && new_name.matches("^[a-z][a-z0-9]{3,30}$")
					&& columnName.equals(new_name) == false) {
				query = query + ";ALTER TABLE " + table_name + " RENAME  "
						+ columnName + " TO " + new_name + "; ";
			}
			System.out.println("query = "+query);
			mr.dc.stmt = mr.dc.conn.prepareStatement("ALTER TABLE "+table_name
					+ query.substring(1));
			mr.dc.stmt.executeUpdate();
            System.out.println("pfeecly finsihed edit columne");
			mr.dc.close();
			return "{ \"status\" : 200, \"message\" : \" Column edit  sucess fully \" ,  \"Organization Name\" :\""
					+ org_name
					+ "\",  \"Database Name\" : \""
					+ db_name
					+ "\", \"Table Name\" : \"" + table_name + "\" } ";// [[int,null,unique],[],[],[]]

		} catch (Exception e) {
		    e.printStackTrace();
			if (mr.dc != null) {
				mr.dc.close();
			}
			return "{ \"status\" : 406, \"message\" : \"Your Column edit un sucess fully \" }";
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

	public String deleteTable(String org_name, String db_name, String table_name) {
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			long org_id = dc.getOrgId(org_name);
			long db_id = dc.getDBId(org_id, org_name + "_" + db_name);
			long table_id = dc.getTableId(org_id, db_id, table_name);
			String sqlQuery = "delete from table_details where org_id=? and db_id=? and table_id=?";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setLong(1, org_id);
			stmt.setLong(2, db_id);
			stmt.setLong(3, table_id);
			stmt.executeUpdate();
			dc.close();
			System.out.println(" ");
			dc = new DatabaseConnection(org_name + "_" + db_name, org_name, "");
			// dc.dbConnection(org_name + "_" + db_name, org_name, "");
			sqlQuery = "drop table " + table_name;
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.execute();
			stmt.close();
			dc.close();
			return "{  'status' : 200,'message' : 'Your Table Delete successfully ', 'Organization Name' : '"
					+ org_name
					+ "', 'Database Name ' : ' "
					+ db_name
					+ "',  'Table Name' : '" + table_name + "' } ";
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			if (dc != null) {
				dc.close();
			}
			return "{ 'status' : 404, 'message' : 'Your Table Not Found '  } ";
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
	public String renameTable(String org_name, String db_name,
			String table_oldname, String table_newname) {
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			long org_id = dc.getOrgId(org_name);
			long db_id = dc.getDBId(org_id, org_name + "_" + db_name);
			long table_id = dc.getTableId(org_id, db_id, table_oldname);
			String sqlQuery = "update table_management set table_name=? where org_id=? and db_id=? and table_id=?";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(1, table_newname);
			stmt.setLong(2, org_id);
			stmt.setLong(3, db_id);
			stmt.setLong(4, table_id);
			stmt.executeUpdate();
			dc.dbConnection(org_name + "_" + db_name, org_name, "");
			sqlQuery = "alter table " + table_oldname + " rename to "
					+ table_newname + "";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.executeUpdate();
			stmt.close();
			dc.close();
			return "{   'status' : 200,  'message' : 'Table rename successfully !...', 'Organization Name ' : '"
					+ org_name
					+ "', 'Database Name ' : '"
					+ db_name
					+ "',  'Table Old Name' : '"
					+ table_oldname
					+ "',  'Table New Name' : '" + table_newname + "' } ";
		} catch (Exception e) {
			System.out.println("manage table error :" + e);
			if (dc != null) {
				dc.close();
			}
			return "{ 'status' : 404, 'message' : 'Your Table Not Found '  } ";
		}
	}

	/**
	 * This private method used to create new table in database.
	 * 
	 * @Params : String org_name , String db_name , String table_name
	 * 
	 * @Return : if table successfully create return true, else false;
	 */

	public String createTable(String org_name, String db_name,
			String table_name, String columnNames, long user_id) {
		System.out.println(" create table ");
		try {

			System.out.println("Create Table");

			dc = new DatabaseConnection("postgres", "postgres", "");
			long org_id = dc.getOrgId(org_name);
			long db_id = dc.getDBId(org_id, org_name + "_" + db_name);
			if (org_id == -1 || db_id == -1) {
				throw new Exception("Invalid Organization Name & Database");
			}
			String sqlQuery = "insert into table_details ( org_id , db_id ,table_name) values(?,?,?)";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(3, table_name);
			stmt.setLong(2, db_id);
			stmt.setLong(1, org_id);
			stmt.executeUpdate();
			long table_id = dc.getTableId(org_id, db_id, table_name);
			sqlQuery = "insert into table_management (role,table_id,user_id) values(?,?,?)";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(1, "co-owner");
			stmt.setLong(2, table_id);
			stmt.setLong(3, user_id);
			stmt.execute();
			System.out.println("Query Exexcute");
			dc.conn.close();
			JsonArray trade;
			JsonParser parser = new JsonParser();
			if (columnNames != null) {
				JsonElement tradeElement = parser.parse(columnNames);
				trade = tradeElement.getAsJsonArray();
			} else {
				trade = new JsonArray();
			}
			dc.dbConnection(org_name + "_" + db_name, org_name, "");
			System.out.println("Connection Exexcute");
			StringBuilder s = new StringBuilder("");
			for (JsonElement eachColumn : trade) {
				JsonArray column = eachColumn.getAsJsonArray();
				String columnType = "";
				String tem = "";
				for (JsonElement typeAndName : column) {
					tem = typeAndName.getAsString();
					if (tem.startsWith("\"") && tem.endsWith("\"")) {
						tem = tem.substring(1, tem.length() + 1);
					}
					columnType = columnType + tem + " ";
				}
				s.append(columnType + ",");
			}
			System.out.println("Column  :");
			if (s.length() >= 1) {
				sqlQuery = "CREATE table " + table_name
						+ " ( roll_no bigserial primary key , "
						+ s.toString().substring(0, s.length() - 1) + ");";
			} else {
				sqlQuery = "CREATE table " + table_name
						+ " ( roll_no bigserial primary key ) ";
			}
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.executeUpdate();
			dc.close();
			return "{  'status' : 200, 'message' : 'Table Create successfully !...',  'Organization Name' :'"
					+ org_name
					+ "',  'Database Name' : '"
					+ db_name
					+ "', 'Table Name' : '" + table_name + "' } ";// [[int,null,unique],[],[],[]]
		} catch (Exception e) {
			if (dc != null) {
				dc.close();
			}
			System.out.println("Error ;: " + e);
			return "{ 'status' : 404, 'message' : 'Your Table Not Found '  } ";
		}
	}

	/**
	 * This public method used to alter table in database.
	 * 
	 * @Params : String table_name , JsonArray columnArray , String wanted
	 * 
	 * @Return : if table successfully alter return true, else false;
	 */

	public String alterColumn(String org_name, String db_name,
			String table_name, JsonArray columnArray, String wanted) {
		try {
			mr = new ManageRecord(dc);
			mr.defineColumnAndType(org_name, db_name, table_name);
			String sql = bulidAlterQuery(columnArray, wanted);
			if (sql.charAt(0) == 'E') {
				return " {'status':406 , 'message': 'Your Column "
						+ sql.substring(1) + " unsuccess fully '}";
			}
			sql = "ALTER table " + table_name + "  " + sql;
			System.out.println(sql);
			dc.stmt = dc.conn.prepareStatement(sql);
			System.out.println("uyyiyiiyiyiyiyyi");
			dc.stmt.executeUpdate();
			dc.close();
			return "  {'status':200 , 'message': 'Your Column " + wanted
					+ " success fully ','Organizartion Name ' : '" + org_name
					+ "',  'Database Name ' : ' " + db_name
					+ "' ,'Table Name ' : ' " + table_name + " ' }";
		} catch (Exception e) {
			if (dc != null) {
				dc.close();
			}
			System.out.println("alter table error : " + e);
			return " {'status':406 , 'message': 'Your Column " + wanted
					+ " unsuccess fully '}";
		}
	}

	public boolean shareTable(String org_name, String db_name,
			String table_name, String role, long user_id, String query) {
		try {
			init();
			dc = new DatabaseConnection("postgres", "postgres", "");
			String sql = "";
			long org_id = dc.getOrgId(org_name);
			long db_id = dc.getDBId(org_id, org_name + "_" + db_name);
			long table_id = dc.getTableId(org_id, db_id, table_name);
			if (query.equals("createRole")) {
				sql = "insert into table_management (role,user_id,table_id)values(?,?,?)";
			} else if (query.equals("editRole")) {
				sql = "update table_management set role= ? where user_id=? and table_id=?";
			} else {
				sql = "delete from table_management where role=? and user_id=? and table_id=?";
			}
			dc = new DatabaseConnection(org_name + "_" + db_name, org_name, "");
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.setString(1, role);
			dc.stmt.setLong(2, user_id);
			dc.stmt.setLong(3, table_id);
			dc.stmt.executeUpdate();
			dc.close();
			return true;
		} catch (Exception e) {
			if (dc != null) {
				dc.close();
			}
			return false;
		}
	}
	
	
	public String getSharedMembers(long table_id) {
	    try {
	        JSONArray resp = new JSONArray();
	        dc = new DatabaseConnection("postgres","postgres","");
	        JSONObject arg0 = new JSONObject();
	        dc.stmt = dc.conn.prepareStatement(" select signup_detail.user_name,signup_detail.user_email,table_management.role from signup_detail inner join table_management on signup_detail.user_id = table_management.user_id and table_management.table_id=? and not signup_detail.user_email like '%@orgdat.com';");
	        dc.stmt.setLong(1, table_id);
	        ResultSet rs = dc.stmt.executeQuery();
	        while (rs.next()){
	            arg0.put("member",rs.getString(1));
	            arg0.put("member_email",rs.getString(2));
	            arg0.put("role",rs.getString(3));
	            resp.add(arg0.clone());
	            arg0.clear();
	        }
	        dc.close();
	       // return members;
	        return "{\"status\":200,\"data\":"+resp.toJSONString()+",\"message\":\"table\"}";
	    } catch (Exception e){
	        dc.close();
	        e.printStackTrace();
	       // return new ArrayList<String>();
	       return "{\"status\":404,\"message\":\"not founded some error occured \"}";
	    }
	}
}