

$(document).ready(function(){
    $("input").focus(function(){
        $("input").css("border-bottom", "1px solid black");
        //$("label").css("color","black");
       // $(this).next().css("left","1%");
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
    });
