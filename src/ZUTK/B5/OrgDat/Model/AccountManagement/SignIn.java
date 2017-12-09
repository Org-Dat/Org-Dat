package ZUTK.B5.OrgDat.Model.AccountManagement;

import ZUTK.B5.OrgDat.Controllers.Filters.DatabaseConnection;
import java.sql.*;

public class SignIn{
    DatabaseConnection dc;
    public long checkUserAccount(String email,String password){
        try{
            dc = new DatabaseConnection("postgres","postgres","");
            String sql = "select user_id from signup_detail where user_email=? and user_password=?";
            dc.stmt = dc.conn.prepareStatement(sql);
            dc.stmt.setString(1, email);
            dc.stmt.setString(2, password);
            ResultSet rs = dc.stmt.executeQuery();
            long user_id = -1l;
            if(rs.wasNull()){
                return user_id;
            }
            while(rs.next()){
                user_id = rs.getLong(1);
            }
            return user_id;
        }catch(Exception e){
            return -1l;
        }
    }
}