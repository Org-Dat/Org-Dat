package ZUTK.B5.OrgDat.Model.AccountManagement;

import ZUTK.B5.OrgDat.Controllers.Filters.DatabaseConnection;
import java.sql.*;

public class SignIn{
    DatabaseConnection dc;
    public boolean checkUserAccount(String email,String password){
        try{
            String sql = "select user_password from signup_detail where user_email=? and user_password=?";
            dc.stmt = dc.conn.prepareStatement(sql);
            dc.stmt.setString(1, email);
            dc.stmt.setString(2, password);
            ResultSet rs = dc.stmt.executeQuery();
            String _password = "";
            while(rs.next()){
                _password = rs.getString("user_password");
            }
            if(_password.equals(password)){
                return true;
            }else{
               return false;    
            }
        }catch(Exception e){
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
}