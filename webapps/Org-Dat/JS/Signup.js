
function check(){
    list=['pswd','mail','mble','name','re_pswd'];
    list1=['label_pswd','label_mail','label_name','label_re_pswd','label_mble'];
    for(i=0;i<=5;i++){
        var value=document.getElementById(list[i]).value;
        if(value==""){
            document.getElementById(list[i]).style.borderBottom = "1px solid red";
            document.getElementById(list1[i]).setAttribute("style","color:red");
        }
    }
}


$(document).ready(function(){
    $("input").focus(function(){
        $("input").css("border-bottom", "1px solid black");
        $("label").css("color","black");
        $(this).next().css("left","18%");
    });
    
    $("h2").click(function(){
        $(".sgup").toggle();
        $(".fgtPsw").toggle();
    });
    
    $(".fgtPsw").click(function(){
        $(".Sec").show();
        $(".forms").hide();
        $(".signup").css({
            "padding":"2% 5%",
            "width":"30%"
        });
    });
    
    $(".nxt").click(function(){
        $(".ques").hide();
        $(".frgt").show();
        $(".signup").css({
            "padding":"2% 14%",
            "width":"20%"
        });
    });
    var regex = {
        "name" : /^[a-z][a-zA-Z0-9]{3,255}$/, 
        "email" : /^[a-z][a-z0-9]{5,30}$/ ,
        "phone" : /^[+]?[0-9]{10,30}$/ ,
        "pass" : /^.{6,255}$/
     }
     
    $(document).on("click","#signup",function(){
        var name = $("#name").val();
        var email = $("#mail").val();
        var password = $("#pswd").val();
        var reenter = $("#re_pswd").val();
        var phone = $("#mble").val();
        var count = 0;
        if((regex.name).test(name)){
            count++;
            $("#label_name").css("color","#000000");
        }else{
            $("#label_name").css("color","#ff0000")
        }
        
        if((regex.email).test(email)){
            count++;
            $("#label_mail").css("color","#000000");
        }else{
            $("#label_mail").css("color","#ff0000");
        }
        
        if((regex.phone).test(phone)){
            count++;
            $("#label_mble").css("color","#000000");
        }else{
            $("#label_mble").css("color","#ff0000");
        }
        
        if((regex.pass).test(password)){
            count++;
            $("#label_pass").css("color","#000000");
        }else{
            $("#label_pass").css("color","#ff0000");
        }
        
        if(password == reenter){
            count++;
            $("#label_re_pass").css("color","#000000");
        }else{
            $("#label_re_pass").css("color","#ff0000");
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
    });

    $("#name").focusout(function(){
        var name = $("#name").val();
        if((regex.name).test(name) == false){
            $("#label_name").css("color","#ff0000");
        }else{
            $("#label_name").css("color","#000000");
        }
    });
    
    $("#mail").focusout(function(){
        var email = $("#mail").val();
        if((regex.email).test(email) == false){
            $("#label_mail").css("color","#ff0000");
        }else{
            $("#label_mail").css("color","#000000");
        }
    });
    
    $("#mble").focusout(function(){
       var phone =  $("#mble").val();
       if((regex.phone).test(phone) == false){
           $("#label_mble").css("color","#ff0000");
       }else{
           $("#label_mble").css("color","#000000");
       }
    });
    
    $("#pswd").focusout(function(){
        var pass = $("#pswd").val();
        if((regex.pass).test(pass) == false){
            $("#label_pswd").css("color" , "#ff0000");
        }else{
            $("#label_pswd").css("color" , "#000000");
        }
    });
    
    $("#re_pswd").focusout(function(){
        var repass = $("#re_pswd").val();
        var pass = $("pswd").val();
        if((regex.pass).test(repass) == false || pass != repass){
            $("#label_re_pswd").css("color", "#ff0000");
        }else{
            $("#label_re_pswd").css("color", "#000000");
        }
    });
    
        // $("#sign_email").focusout(function(){
        //     var name = $("#sign_email").val();
        //   if((regex.email).test(name) == false){
        //       $("#sign_email").css("color","#ff0000");
        //   }else{
        //       $("#sign_email").css("color","#000000");
        //   }
        // });
        
        // $("#sign_pass").focusout(function(){
        //     var pass = $("#sign_pass").val();
        //     if((regex.pass).test(pass) == false){
        //       $("#sign_pass").css("color" , "#ff0000");
        //     }else{
        //       $("#sign_pass").css("color" , "#000000");
        //     }
        // });
    // $(document).on("click","#signin",function(){
    //     var email = $("#sign_email").val();
    //     var password = $("#sign_pass").val();
    //     if((regex.pass).test(password)){
    //         $("#sign_pass").css("color","#000000");
    //     }else{
    //         $("#sign_pass").css("color","#ff0000");
    //     }
        
    //         $("#mail").focusout(function(){
    //     if((/^[a-z][a-z0-9]{}/).test(email) == false){
    //         $("#label_mail").css("color","#ff0000");
    //     }else{
    //         $("#label_mail").css("color","#000000");
    //     }
        
        
    // });
    });
// });


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

//function 
function checkResponse(res){
    if(res != "success"){
        $("#alert").text(res);
    }
}