package ZUTK.B5.OrgDat.Model.AccountManagement;

import ZUTK.B5.OrgDat.Controllers.Filters.DatabaseConnection;
import java.sql.*;

public class Logout{
    DatabaseConnection dc;
    public boolean logout(String iambdt,String ip_address){
        try{
            dc = new DatabaseConnection("postgres","postgres","");
            String sql = "delete from cookie_management where iambdt_cookie=? and ip_address=?";
            dc.stmt = dc.conn.prepareStatement(sql);
            dc.stmt.setString(1, iambdt);
            dc.stmt.setString(2, ip_address);
            dc.stmt.executeUpdate();
            return true;
        }catch(Exception e){
            return false;
        }
    }
}    