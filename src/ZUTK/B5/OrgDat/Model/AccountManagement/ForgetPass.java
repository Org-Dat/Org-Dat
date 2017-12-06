package ZUTK.B5.OrgDat.Model.AccountManagement;

import ZUTK.B5.OrgDat.Controllers.Filters.DatabaseConnection;
import java.sql.*;

public class ForgetPass {
	DatabaseConnection dc;

	public boolean isAnswer(String question, String answer,String email) {
		try {
			dc = new DatabaseConnection("postgres", "postgres", "");
			String sql = "select question,answer from security_management where question=? and answer=? and user_id in (select user_id from sigup_detail where user_email=?)";
            dc.stmt = dc.conn.prepareStatement(sql);
            dc.stmt.setString(1,question);
            dc.stmt.setString(2,answer);
            dc.stmt.setString(3,email);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}