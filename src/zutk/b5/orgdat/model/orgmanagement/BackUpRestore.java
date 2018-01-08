package zutk.b5.orgdat.model.orgmanagement;

//public class BackUpRestore{

import java.io.*;
import java.sql.*;
import zutk.b5.orgdat.controllers.filters.*;

public class BackUpRestore {
	public static void main(String[] args) {
		// CSVFileBackUp("ponkumar","testdb","werwer","yes");
	}

	public static Connection dbConnection(String db_name, String user) {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
		}
		System.out.println("PostgreSQL JDBC Driver Registered!");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/testdb", "postgres", "");
		} catch (Exception e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		} finally {
			return connection;
		}
	}

	public static String CSVFile(String user, String datName, String tabName,
			String path, String option, String header) {
		/**
		 * CSV File backup Option = to
		 * 
		 * CSV File restore Option = from
		 * 
		 */
		String query = "copy " + tabName + " " + option + " '" + path
				+ "' csv ";
		// String query = "select * from werwer;";
		if ("yes".equals(header)) {
			query = query + " header";
		}
		try {
			System.out.println("start");
			System.out.println(query);
			System.out.println(" datName  = "+datName+" user  = "+user);
			DatabaseConnection dc = new DatabaseConnection(datName,"postgres","");
			//DatabaseConnection dc = new DatabaseConnection(datName,user,"");
			//Connection database = dbConnection(datName, user);
			System.out.println("database = " + dc.conn);
			Statement statement = dc.conn.createStatement();
			System.out.println("preStm = " + statement);
			statement.execute(query);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR Message is : " + e.getMessage());
			return null;
		}
		System.out.println("finish");
		//return "/tmp/" + tabName + ".csv";
		return path;

	}

	public static String SQLFile(String user, String db_name, String path,
			String option) {
		try {
			try {
				/**
				 * SQL File backup Option = -b -v
				 * 
				 * SQL File restore Option = -a -w
				 * 
				 */
				String command = "pg_dump -U " + user + " -d " + db_name + " "
						+ option + " -f " + path + " ";
				Process proc = Runtime.getRuntime().exec(command);
				System.out.println(command);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(proc.getInputStream()));
				String line = reader.readLine();
				while (line != null) {
					System.out.print(line + "\n");
					line = reader.readLine();
				}
				proc.waitFor();
				proc.destroy();
				System.out.println("success backup" + path);
				return path;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return null;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
