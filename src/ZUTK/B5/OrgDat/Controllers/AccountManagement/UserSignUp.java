package ZUTK.B5.OrgDat.Controllers.AccountManagement;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import ZUTK.B5.OrgDat.Model.AccountManagement.SignUp;

public class UserSignUp extends HttpServlet{
    PrintWriter out; 
    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response){
        try{
            out = response.getWriter();
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phone_number");
            String password = request.getParameter("password");
            if(name.matches("[a-zA-Z]{3,255}$")){
                if(email.matches("^[a-z][a-z0-9]{5,30}@orgdat.com$") ){
                    if(phoneNumber.matches("^[+]?[7-9][0-9]{9,30}$")){
                        if( password.matches("^.{6,255}$")){
                            SignUp su = new SignUp();
                            if(su.isMember(email) == true){
                                String[] details = new String[4];
                                details[0] = name;
                                details[1] = email;
                                details[2] = phoneNumber;
                                details[3] = password;
                                if(su.addMemeber(details)){
                                    out.write("SignUp Successfully");
                                }else{
                                    out.write("Invaild Inputs");
                                }
                            }else{
                                out.write("Email Id Already Exists");
                            }
                        }else{
                            out.write("Password is incorrect");
                        }
                    }else{
                         out.write("Phone Number is incorrect");
                    }
                }else{
                     out.write("Email is incorrect");
                }
            }else{
                 out.write("Name is incorrect");
            }
        }catch(Exception e){
            out.write("Invaild Request");
        }
    }
}