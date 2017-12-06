package ZUTK.B5.OrgDat.Model.DatabaseManagement;
import java.sql.*;
import com.google.gson.*;
public class ManageTable{
    	DatabaseConnection dc = null;
        PreparedStatement stmt = null;
    /**
	 * This private method used to create new table in database.
	 * 
	 * @Params : String org_name , String db_name , String table_name
	 * 
	 * @Return : if table successfully create return true, else false;
	 */

	public boolean createTable(String org_name, String db_name,
			String table_name, String columnNames,long user_id) {
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
	 * This private method used to find table name is correct or wrong
	 * 
	 * @Params : String table_name
	 * 
	 * @Return : if name match to regex return true,else return false
	 */

	public  boolean isCorrect(String table_name) {
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
			sqlQuery = "alter table ? rename to ?";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(1, table_oldname);
			stmt.setString(2, table_newname);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean alterColumn(boolean addOrDel, String column,
			String table_name, String db_name, String org_name) {
		try {
			String sql = "";
			if (addOrDel == true) {
				sql = "alter table " + table_name + " add column " + column;
			} else {
				sql = "alter table " + table_name + " drop column " + column;
			}
			dc.stmt = dc.conn.prepareStatement(sql);
			dc.stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean alterColumnType(String column_name, String org_name,
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
}