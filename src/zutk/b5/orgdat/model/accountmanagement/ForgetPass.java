package zutk.b5.orgdat.model.accountmanagement;

import zutk.b5.orgdat.controllers.filters.DatabaseConnection;
import java.sql.*;

public class ForgetPass {
	DatabaseConnection dc;

	public long isAnswer(String question, String answer,String email) {
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			String sql = "select user_id from security_management where question=? and answer=? and user_id in (select user_id from sigup_detail where user_email=?)";
            dc.stmt = dc.conn.prepareStatement(sql);
            dc.stmt.setString(1,question);
            dc.stmt.setString(2,answer);
            dc.stmt.setString(3,email);
            ResultSet rs = dc.stmt.executeQuery();
            long user_id = -1;
            while(rs.next()){
                user_id = rs.getLong(1);
            }
			return user_id;
		} catch (Exception e) {
			return -1l;
		}
	}
}