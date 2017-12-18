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
                       <input id="nqame"  class="sgupInp"><label id="label_fname" for="name" >New-Pswd</label>
                       <input id="psqwd"  type="password" class="sgupInp"><label id="label_fpswd" for="pswd">Re-enter:</label>
                       <button>Create</button>
                   </div>
                </div>
                
                <!-- Sign Up-->
                    <div class="forms">
                        <input id="name"  class="sgupInp sgup" onfocusout = "check(this.value , 'name','label_name')"><label id="label_name" for="name" class="sgup">Username:</label>
                        <input id="mail"  class="sgupInp" onfocusout = "check(this.value , 'email','label_mail')"><label id="label_mail" for="mail">E-mail:</label>
                        <input id="mble"  class="sgupInp sgup" onfocusout = "check(this.value , 'phone','label_mble')"><label id="label_mble" for="mble" class="sgup">Mobile:</label>
                        <input id="pswd"  type="password" class="sgupInp" onfocusout = "check(this.value , 'pass','label_pswd')"><label id="label_pswd" for="pswd">Password:</label>
                        <input id="re_pswd"  type="password" class="sgupInp sgup" onfocusout = "reenterPass(this.value)"><label id="label_re_pswd" for="re_pswd" class="sgup">Re-Enter Password:</label>
                        <button id="signup" onclick = "signinCheck()">Login</button> 
                        <span class="fgtPsw">forget password</span>
                    </div>
        </div>
        </div>
        <script>
            
     var regex = {
        "name" : /^[a-z][a-zA-Z0-9]{3,255}$/, 
        "email" : /^[a-z][a-z0-9]{5,30}$/ ,
        "phone" : /^[+]?[0-9]{10,30}$/ ,
        "pass" : /^.{6,255}$/
     }
function check(val , check,label_id){
    var reg = regex[check];
    if(reg.test(val)){
        document.getElementById(label_id).style.color = "black";
    }else{
        document.getElementById(label_id).style.color = "red";
    }
}

function reenterPass(value)  {
    var pass = document.getElementById('pswd').value;
    if (value != pass) {
        document.getElementById('label_re_pswd').setAttribute('style', 'color : red');
    } else {
        document.getElementById('label_re_pswd').setAttribute('style', 'color : black')
    }
}


function signupCheck(){
     var name = $("#name").val();
     var email = $("#mail").val();
     var password = $("#pswd").val();
     var reenter = $("#re_pswd").val();
     var phone = $("#mble").val();
     var count = 0;
  
  
     if(regex.name.test(name) == false){
         document.getElementById("label_name").style.color = "red";
     }else{
         count++;
          document.getElementById("label_name").style.color = "black";
     }
     
     
     if(regex.email.test(email) == false){
         document.getElementById("label_mail").style.color = "red";
     }else{
         count++;
          document.getElementById("label_mail").style.color = "black";
     }
     
     
     if(regex.pass.test(password) == false){
         document.getElementById("label_pswd").style.color = "red";
     }else{
         count++;
          document.getElementById("label_pswd").style.color = "black";
     }
     
     
     if(regex.phone.test(phone) == false){
         document.getElementById("label_mble").style.color = "red";
     }else{
         count++;
          document.getElementById("label_mble").style.color = "black";
     }


     if(reenter != password){
         document.getElementById("label_re_pswd").style.color = "red";
     }else{
         count++;
          document.getElementById("label_re_pswd").style.color = "black";
     }
     
     if(count == 5){
            var data = {
            "name" : name,
            "email" : email+"@orgdat.com",
            "phone_number" : phone,
            "password" : password
        }
     sendPostRequest("/signup", data , checkResponse);
  }
    
}


function sendGetRequest(url1,callback){
    $.ajax({url:url1,success : function(result){
        console.log(result);
        callback(result);
    }});
    
}

function sendPostRequest(url1,data,callback){
    $.post({url:url1,data,success : function(result){
        console.log(result);
        callback(result);
    }});
    
}

function signinCheck(){
     var email = $("#mail").val();
     var password = $("#pswd").val();
     var count  = 0;
     if((/^[a-z][a-z0-9]{5,255}@[a-z0-9]{4,25}.com$/).test(email) == false){
         document.getElementById("label_mail").style.color = "red";
     }else{
         count++;
          document.getElementById("label_mail").style.color = "black";
     }
     
    if((/^.{5,255}$/).test(password) == false){
         document.getElementById("label_pswd").style.color = "red";
     }else{
         count++;
          document.getElementById("label_pswd").style.color = "black";
     }
     
     if(count == 2){
         var data = {
             "email" : email,
             "password" : password
         }
         sendPostRequest("/signin", data , checkResponse);
     }
}


function checkResponse(res){
  if(res != "success"){
      alert(res);
  }else{
      location.href = "http://orgdat.zcodeusers.com/v1"
  }
    
}
        </script>
    </body>
</html>