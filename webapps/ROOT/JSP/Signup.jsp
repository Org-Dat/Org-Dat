<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="../CSS/Signup.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="../JS/Signup.js"></script>
    </head>
    <body onload = "isSign()">
         <div class="whole">
             <!--...............................sec question...................................-->
                <!--<div class="Sec">-->
                    
                    <div class="ques">
                        <div class="secQues" id ="question" >
                             What makes a good security question? A good security question produces answers that are:
                         </div>
                         <div class="secAus" contenteditable="true" id = "answer"></div>
                        <button class="nxt" onclick = "forgetPassword()">next</button>
                    </div>
                    <div class="fgt" >
                        <h2>Forget Password</h2>
                    
                    <div class="rel">
                        <input id="fpswd" class="sgupInp" style="border-bottom: 1px solid black;" ><label id="label_fpswd" for="mail" >New Password:</label>
                    </div>
                    <div class="rel">
                        <input id="repswd" type="password" class="sgupInp" style="border-bottom: 1px solid black;"><label id="label_frepswd" for="pswd" >Re-Password:</label>
                    </div>
                    
                    <button id="but" onclick="setPassword()">Create</button>
                    </div>
                
                <div class="Signup_form">
                    <h2>SignUp</h2>
                    <div class="rel">
                        <input id="name"  class="sgupInp sgup" onfocusout = "check(this.value , 'name','label_name')"><label id="label_name" for="name" class="sgup">Username:</label>
                    </div>
                    
                    <div class="rel">
                        <input id="mail"  class="sgupInp" onfocusout = "check(this.value , 'email','label_mail')"><label id="label_mail" for="mail">E-mail:</label>
                    </div>
                     
                     <div class="rel">
                        <input id="mble"  class="sgupInp sgup" onfocusout = "check(this.value , 'phone','label_mble')"><label id="label_mble" for="mble" class="sgup">Mobile:</label>
                    </div>
                     <div class="rel">
                         <input id="pswd"  type="password" class="sgupInp" onfocusout = "check(this.value , 'pass','label_pswd')"><label id="label_pswd" for="pswd">Password:</label>
                    </div>
                    <div class="rel">
                        <input id="re_pswd"  type="password" class="sgupInp sgup" onfocusout = "reenterPass(this.value)"><label id="label_re_pswd" for="re_pswd" class="sgup">Re-Enter Password:</label>
                    </div>
                    
                    <button id="but" onclick="signupCheck()">Login</button>   
                    <span class="fgtPsw">forget password</span>
                </div>
                
                <div class="login_form">
                    <h2>LOGIN</h2>
                   <div class="rel">
                       <input id="smail" class="sgupInp" style="border-bottom: 1px solid black;"><label id="label_smail" for="mail">E-mail:</label>
                   </div>
                    
                    <div class="rel">
                        <input id="spswd" type="password" class="sgupInp" style="border-bottom: 1px solid black;"><label id="label_spswd" for="pswd" >Password:</label>
                    </div>
                    
                    
                    <button id="but" onclick="signinCheck()">Login</button> 
                    <span class="fgtPsw" style="display: inline;" onclick = "getQuestion()">forget password</span>
                    
                </div>
        <script>
           var a =  <%= (request.getCookies() !=null && request.getCookies().length  > 1)%>  
              if (a == true){
               location.href = "/v1"
             }
             function isSign(){
             
                 var path = location.pathname;
                 if(path == "/enroll"){
                     document.getElementsByClassName("Signup_form")[0].setAttribute("style","display:block");
                 }else if(path == "/forgot"){
                     
                      document.getElementsByClassName("ques")[0].setAttribute("style","display:block");
                      
                 }else{
                     document.getElementsByClassName("login_form")[0].setAttribute("style","display:block");
                 }
             }  
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
        
        function setPassword(){
            var password = $("#fpswd").val();
            var repass = $("#repswd").val();
            var count = 0;
            if(/^.{6,255}$/.test(password)){
                count++;
                document.getElementById("label_frepswd").setAttribute("style","color:black");
            }else{
                document.getElementById("label_frepswd").setAttribute("style","color:red");
            }
            
            if(repass == password){
                count++;
                document.getElementById("label_fpswd").setAttribute("style","color:black");
            }else{
                document.getElementById("label_fpswd").setAttribute("style","color:red");
            }
            var data = {
                "password" : password   
            }
            if(count == 2){
                sendPostRequest("/setPassword",data, function(res){
                  if(res == "success"){
                      location.href = "/login";
                   } else{
                   alert(res);
                     //  document.getElementById("alert").innerText = res;
                   }
                });
            }
        }
        
        function signinCheck(){
             var email = $("#smail").val();
             var password = $("#spswd").val();
             var count  = 0;
             if((/^[a-z][a-z0-9]{5,255}@[a-z0-9]{4,25}.com$/).test(email) == false){
                 document.getElementById("label_smail").style.color = "red";
             }else{
                 count++;
                  document.getElementById("label_smail").style.color = "black";
             }
             
            if((/^.{5,255}$/).test(password) == false){
                 document.getElementById("label_spswd").style.color = "red";
             }else{
                 count++;
                  document.getElementById("label_spswd").style.color = "black";
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
          if(res == "success"){
              location.href = "/v1"
          }else{
              location.href = "/enroll"
          }
            
        }
        
        function forgetPassword(){
            var question = ($("#question").text()).trim();
            var answer = ($("#answer").text()).trim();
             var email = $("#smail").val().trim();
            var data = {
                "question" : question,
                "answer" : answer,
                "email" : email
            }
            if(question.length == 0 || answer.length == 0){
                
            }else{
                sendPostRequest("/forgetpass", data , checkResponse);   
            }
        }
        
        function printQuestion(res){
	      if(res != null  || res.length >  2){
            document.getElementById("question").innerText = res 
          }
	   }
	   
	   function getQuestion(){
	       var email = $("#smail").val();
	       if((/^[a-z][a-z0-9]{5,255}@[a-z0-9]{4,25}.com$/).test(email) == false){
	           alert(" Invalid ");
	          // document.getElementById("alert").innerText = "Invalid Email Address !...";
	       }else{
	          document.getElementsByClassName("login_form")[0].setAttribute("style","display:none");
	          document.getElementsByClassName("ques")[0].setAttribute("style","display:block");
	          var url = "/getQuestion";
	          sendPostRequest(url ,{"email" : email} , printQuestion);
	       }
	   }
        </script>
    </body>
</html>
