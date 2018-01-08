package zutk.b5.orgdat.controllers.filters;

import java.sql.*;
import java.util.*;

public class DatabaseConnection {
	private String url;
	private String user = "postgres";
	private String password = "";
	public Connection conn = null;
	public PreparedStatement stmt = null;

	public DatabaseConnection(String database_name, String _user,
			String _password) {
		//dbConnection(database_name, _user, _password);
		dbConnection(database_name, _user, "zohouniversity");
	}

	public void dbConnection(String db_name, String _user, String _password) {
		this.url = "jdbc:postgresql://localhost:5432/" + db_name;
		this.user = _user;
		this.password = "zohouniversity";
		close();
		try {
			Class.forName("org.postgresql.Driver");
			this.conn = DriverManager.getConnection(url, user, password);
			System.out.println(conn);
		} catch (Exception sqlEx) {
			sqlEx.printStackTrace();
			return;
		}
	}

	public String createJunk(int count) {
		Random rand = new Random();
		StringBuilder s = new StringBuilder("");
		String characters = "abcdefghijklmnopqrstuvwxyz";
		characters = characters + characters.toUpperCase();
		characters = characters + "_1234567890";
		while (count >= s.length()) {
			int randomNumber = rand.nextInt(63);
			s.append(characters.charAt(randomNumber));
		}
		return s.toString();
	}

	public long getUserId(String mail, String password) {
		try {
			long user_id = -1;
			String sql = "select user_id from signup_detail where user_email=? and user_password=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, mail);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				user_id = rs.getLong(1);
			}
			return user_id;
		} catch (Exception e) {
			return -1;
		}
	}

	public long getOrgId(String org_name) {
		try {
			stmt = conn
					.prepareStatement("select org_id from org_details where org_name=?");
			stmt.setString(1, org_name);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getLong(1);
			}
			return -1;
		} catch (Exception e) {
			return -1;
		}
	}

	public long getDBId(long org_id, String db_name) {
		try {
			stmt = conn
					.prepareStatement("select db_id from db_details where db_name=? and org_id=?");
			stmt.setString(1, db_name);
			stmt.setLong(2, org_id);
			ResultSet rs = stmt.executeQuery();
			long db_id = -1;
			while (rs.next()) {
				db_id =  rs.getLong(1);
				break;
			}
			//stmt.close();
				System.out.println("ORG ID = "+org_id + "DB NAME = "+ db_name +" === DB ID  +==="+db_id);
			return db_id;
		} catch (Exception e) {
		    e.printStackTrace();
			return -1;
		}
	}

	public long getTableId(long org_id, long db_id, String table_name) {
		try {
			stmt = conn
					.prepareStatement("select table_id from table_details where table_name=? and org_id=? and db_id=?");
			stmt.setString(1, table_name);
			stmt.setLong(2, org_id);
			stmt.setLong(3, db_id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getLong(1);
			}
			return -1;
		} catch (Exception e) {
			return -1;
		}
	}

	public void close() {
		try {
			if (stmt != null && stmt.isClosed() == false) {
				stmt.close();
			}
			if (conn != null && conn.isClosed() == false) {
				conn.close();
			}
		} catch (Exception e) {
			System.out.println("connection close has problem = " + e);
		}
	}
}
