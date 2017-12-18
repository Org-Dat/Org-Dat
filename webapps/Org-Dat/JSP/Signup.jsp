<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="../CSS/Signup.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="../JS/Signup.js"></script>
    </head>
    <body>
        <div class="whole">
            <div class="signup">
                <h2>Login</h2>
                <div class="Sec">
                    <!-- Forget password Question -->
                    <div class="ques">
                        <div class="secQues">
                             What makes a good security question? A good security question produces answers that are:
                         </div>
                         <div class="secAus" contenteditable="true"></div>
                        <button class="nxt">next</button>
                    </div>
                    
                   
                   <!-- Forget New Password -->
                   <div class="frgt">
                       <input id="nqame"  class="sgupInp"><label id="label_name" for="name" >New-Pswd</label>
                       <input id="psqwd"  type="password" class="sgupInp"><label id="label_pswd" for="pswd">Re-enter:</label>
                       <button>Create</button>
                   </div>
                </div>
                
                <!-- Sign Up-->
                    <div class="forms">
                        <input id="name"  class="sgupInp sgup"><label id="label_name" for="name" class="sgup">Username:</label>
                        <input id="mail"  class="sgupInp"><label id="label_mail" for="mail">E-mail:</label>
                        <input id="mble"  class="sgupInp sgup"><label id="label_mble" for="mble" class="sgup">Mobile:</label>
                        <input id="pswd"  type="password" class="sgupInp"><label id="label_pswd" for="pswd">Password:</label>
                        <input id="re_pswd"  type="password" class="sgupInp sgup"><label id="label_re_pswd" for="re_pswd" class="sgup">Re-Enter Password:</label>
                        <button id="signup">Login</button> 
                        <span class="fgtPsw">forget password</span>
                    </div>
        </div>
        </div>
        
    </body>
</html>