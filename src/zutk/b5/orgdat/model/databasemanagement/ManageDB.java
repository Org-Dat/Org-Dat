package zutk.b5.orgdat.model.databasemanagement;

public class ManageDB {
	DatabaseConnection dc;

	/**
	 * This private method used to validate database name
	 * 
	 * @params : String db_name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if name match to regex it retur true. else return false
	 */

	public boolean isCorrect(String db_name) {
		return db_name.matches("^[a-z][a-z0-9]{3,30}$");
	}

	/**
	 * This private method used to create database.
	 * 
	 * @params : String db_name,String org_name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if database create successfully ,it return true.else return
	 *         false
	 */

	public String createDB(long user_id,String dbName, String org_name) {
		try {
		    System.out.println(user_id+"  ==== User id");
		    dc = new DatabaseConnection("postgres", "postgres", "");
			String sqlQuery  = "insert into db_management values(?,?,?,?)";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, org_name);
			dc.stmt.setString(3, org_name + "_" + dbName);
			dc.stmt.setString(4, "co-owner");
			dc.stmt.executeUpdate();
			dc.conn.close();
			System.out.println("Org Connection");
			dc = new DatabaseConnection("postgres", org_name, "");
			sqlQuery = "create database " + org_name + "_" + dbName;
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.executeUpdate();
			dc.conn.close();
			System.out.println("Org Connection close");
			return "200";
		} catch (Exception e) {
		    System.out.println(e);
			return "{'status' : 400,'message' : 'Your Database already exist'}";
		}
	}

	/**
	 * This private method used to rename database.
	 * 
	 * @params : String db_name,String org_name,String newDb_name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if database rename successfully ,it return true.else return
	 *         false
	 */
	public String renameDB(String dbName, String org_name, String newDb_name) {

		try {
		    dc = new DatabaseConnection("postgres", "postgres", "");
			String sqlQuery = "update db_management set db_name=? where db_name=? and org_name=?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name+"_"+newDb_name);
			dc.stmt.setString(2, org_name + "_" + dbName);
			dc.stmt.setString(3, org_name);
			dc.stmt.executeUpdate();
			dc.conn.close();
			dc = new DatabaseConnection("postgres", org_name, "");
			sqlQuery = "alter database " + org_name + "_" + dbName + " rename to " + org_name+"_"+newDb_name;
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.executeUpdate();
			dc.conn.close();
			return "200";
		} catch (Exception e) {
			return " { 'status' :400, 'message' : 'Bad Request'}";
		}
	}

	/**
	 * This private method used to delete database.
	 * 
	 * @params : String db_name,String org_name
	 * 
	 * @return type : boolean
	 * 
	 * @return : if database delete successfully ,it return true.else return
	 *         false
	 */

	public String deleteDB(String dbName, String org_name) {

		try {
		    dc = new DatabaseConnection("postgres", "postgres", "");
			String sqlQuery = "delete from db_management where db_name=? and org_name =?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1,org_name+"_" +dbName);
			dc.stmt.setString(2, org_name);
			dc.stmt.executeUpdate();
			dc.conn.close();
			dc = new DatabaseConnection("postgres", org_name, "");
			sqlQuery = "drop database " + org_name + "_" + dbName;
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.executeUpdate();
			dc.conn.close();
			return "200";
		} catch (Exception e) {
		    System.out.println(e);
			return "{'status' : 400 , 'message'  : 'Bad Request' }";
		}
	}
}