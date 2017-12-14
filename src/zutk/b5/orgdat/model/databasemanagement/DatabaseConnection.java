package zutk.b5.orgdat.model.databasemanagement;

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
		dbConnection(database_name, _user, _password);
	}

	public void dbConnection(String db_name, String _user, String _password) {
		this.url = "jdbc:postgresql://localhost:5432/" + db_name;
		this.user = _user;
		this.password = _password;
		try {
			Class.forName("org.postgresql.Driver");
			this.conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException sqlEx) {
			return;
		} catch (ClassNotFoundException e) {
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
			String sql = "select user_id from signup_detail where user_mail=? and user_password=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, mail);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
			    user_id = rs.getLong(1);
			}
			return user_id;
		} catch (Exception e) {
			return -1;
		}
	}

	public void close() {
		
	}
}
