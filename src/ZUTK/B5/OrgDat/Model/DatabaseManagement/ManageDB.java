package ZUTK.B5.OrgDat.Model.DatabaseManagement;

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
		boolean correct = db_name.matches("^[a-z][a-z0-9]{2,29}$");
		return correct;
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

	public boolean createDB(String dbName, String org_name) {
		try {
			String sqlQuery = "";
			sqlQuery = "insert into db_mangament(db_name,db_roles,org_name) values(?,?,?)";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, dbName);
			dc.stmt.setString(2, "owner");
			dc.stmt.setString(3, org_name);
			dc.stmt.executeUpdate();
			sqlQuery = "create database " + dbName + "_" + org_name;
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.executeUpdate();
			dc.conn.close();
			return true;
		} catch (Exception e) {
			return false;
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
	public boolean renameDB(String dbName, String org_name, String newDb_name) {

		try {
			String sqlQuery = "";
			sqlQuery = "update db_manament set db_name=? where db_name=? and org_name=?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, newDb_name);
			dc.stmt.setString(2, dbName);
			dc.stmt.setString(3, org_name);
			dc.stmt.executeUpdate();
			sqlQuery = "alter database " + dbName + " rename to " + newDb_name;
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.executeUpdate();
			dc.conn.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
    /**
     *  This method crearte JDBC Connection.
     * 
     * @params : String db_name,String user_name,String password
     * 
     * @return type: void
     */
     public void initialiser(String db_name,String user_name,String password){
         this.dc = new DatabaseConnection(db_name,user_name,password);
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

	public boolean deleteDB(String dbName, String org_name) {

		try {
			String sqlQuery = "";
			sqlQuery = "delete from db_mangament where db_name=? and org_name =?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, dbName);
			dc.stmt.setString(2, org_name);
			dc.stmt.executeUpdate();
			sqlQuery = "drop database " + dbName + "_" + org_name;
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.executeUpdate();
			dc.conn.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}