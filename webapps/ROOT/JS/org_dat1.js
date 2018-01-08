function tab_change(tab) {
    var i;
    var x = document.getElementsByTagName("aside");
   
    for (i = 0; i < x.length; i++) {
       x[i].style.display = "none";
    }
    document.getElementById(tab).style.display = "block";  
}
$(document).ready(function(){
    
    $(document).on("click",".shr,.del_but,.add_fld button,.input>button",function(){
        var msg=$(this).text();
        $(".altMsg").text(msg+"d Organization One");
        $(".alertPop").css("transform","translateY(0px)");
        setTimeout(function(){
            $(".alertPop").css("transform","translateY(-45px)");
        },1000)
    })
    
    
    
	$(document).on("click",".org_box>span",function(){
		$(".whole_org_box").hide();
        // $(".whole_org_box").css("transform","translate(-100%)")
        // $(".whole_database").css("transform","translate(0%)")
        setTimeout(function(){
            $(".whole_org_box").hide();
        },700)
		$(".whole_database").css("display","flex");
	});
	$(document).on("click",".databs>span",function(){
	    console.log("hi");
	    if($(this).attr("contenteditable") != "true"){
	           	$(".whole_database").hide();
        // $(".whole_org_box").css("transform","translate(-100%)")
        // $(".whole_database").css("transform","translate(0%)")
        // setTimeout(function(){
            $(".whole_database").hide();
        // },700)
		$(".whole_table").css("display","flex");      
	    }
	  
	});
	
	$(document).keypress(function(event){
	   // alert(event.keyCode)
	    if(event.keyCode==13){
	        $(".databs>span").attr("contenteditable","false")
	    }
	})
// 	-------------------------------Share page-------------------------------------

    $(document).on("click",".fa-share-alt",function(){
        //$(".share").css("transform","translateY(0)")
        $(".share").css("display","flex");
        $(".org").css("display","none");
    })
    $(document).on("click",".rghtCls",function(){
        $(".share").hide();
        $(".org").css("display","flex");
    })
    
    $(document).on("click",".shr_close",function(){
        $(this).parent().fadeOut();
    })
    
    drop_num=1;
    $(".dropdown").click(function(){
        drop_num++;
        if(drop_num%2==0){
            $(this).css({
                "height":"auto",
                // "z-index":"0"
            });
        }else{
           $(this).css({
                "height":"30px",
                // "z-index":"-1"
            });
        }
        
    });
    $(".dropdown ul li").click(function(){
        $(this).parent().children("li:first-child").text($(this).text())
    })
    share=1;
    $(".mng").click(function(){
        share++;
        if(share%2==0){
            //  $(".shr_fld").css("z-index","-1");
            $(".shr_fld").css({
                "opacity":"0",
                "z-index":"-1"
            });
             $(".co_dev").css({
                 "opacity":"1",
                 "transform":"translateY(0px)",
                 "z-index":"1"
                //  "transition":"0.4s"
             });
        }else{
             //$(".shr_fld").css("opacity","1");
             $(".shr_fld").css({
                "opacity":"1",
                "z-index":"1"
            });
             $(".co_dev").css({
                 "opacity":"0",
                 "transform":"translateY(60px)",
                 "z-index":"-1"
                //  "transition":"0.4s"
             });
        }
    })
	
	/*
    // ---------------------------Org Box Menu-------------------------------------------
    num=1;
   $(document).on("click",".menu",function(){
        num++;
        if(num%2==0){
            $(this).html("<i class='fa fa-times' aria-hidden='true'></i>")
            $(this).prev().css({
                "transform":"rotateZ(0deg)",
                "opacity":"1"
            });
        }else{
            $(this).html("<i class='fa fa-filter' aria-hidden='true'></i>")
            
            $(this).prev().css({
                "transform":"rotateZ(-90deg)",
                "opacity":"0"
            });
        }
    });
    */
    // ----------------------------------Org Box Popup alerts--------------------------
    
    $(document).on("click",".options .fa-trash-o,.fa-users,.fa-upload,.fa-users",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        
        $(".org_popup").css("top","30%")
        $(".whole_org").css("filter","blur(5px)")
        //$(".org_popup .decline").css("display","none")
    })
    $(document).on("click",".options .fa-upload",function(){
        $("input[type=file]").show();
        $(".titH3,.PopUsr").hide();
        $(".TpH1").text("Are You Sure to Upload?")
        $(".del_but").text("Upload")
    })
     $(document).on("click",".options .fa-trash-o",function(){
            $("input[type=file],.PopUsr").hide();
            $(".titH3").show();
            $(".TpH1").text("Are You Sure to Delete?")
            $(".del_but").text("Delete")
     })
     $(document).on("click",".fa-users",function(){
            $("input[type=file]").hide();
            $(".PopUsr,.titH3").show();
            $(".titH3").text("Members")
            $(".TpH1").text("Organization 1")
            $(".decline").text("Close")
            $(".del_but").hide()
     })
     
     $(document).on("click",".options .fa-pencil-square-o",function(){
         $(this).parent().prev("span").css("border-bottom","1px solid");
          $(this).parent().prev("span").attr({
              "contenteditable":"true",
              "autofocus":"true"
          })
     })
    
    $(document).on("click",".org_popup .decline,.del_but",function(){
        $(".org_popup").css("top","-300%")
        $(".whole_org").css("filter","blur(0px)")
    })
    
    // ------------------------------------Home-------------------------------------------
    
    $(document).on("click","header>span",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $("..org_box").css("display","flex")
        $(".dsh_brd,.cmn_tab,.main_tab,.admin,.log").css("display","none")
    })
    //------------------------------------Create Org------------------------------------
    
    /*$(document).on("click",".crt_org>button",function(){
        
        if ($(".whole_org").children(".org").length > 2){
            $("#create_org").css("display","none");
            
        }
        var purpose =  $(this).prev().attr("purpose");
        var source   = document.getElementById("org-template").innerHTML;
        var template = Handlebars.compile(source);
        var content = {};
        content.name = $(this).prev().val();
        console.log($(this).prev());
        $(this).prev().val("");
        content.class = purpose;
        var htmlForOrg = template(content);
        if ( purpose == 'org'){
            if ($(".whole_org").children(".org").length < 3){
                $("#create_org").before(htmlForOrg);
                if ($(".whole_org").children(".org").length == 3){
                    $("#create_org").css("display","none");
                }
            }
        } if ( purpose == 'dat'){
            if ($(".whole_org").children(".dat").length < 6){
                $("#create_dat").before(htmlForOrg);
                if ($(".whole_org").children(".dat").length == 6){
                    $("#create_dat").css("display","none");
                }
            }
        }
    })*/
    
    $(document).on("click","#Name+button",function(){
        var parent = $(this).parents("main");
        if (parent.siblings().length  < 3){
            var source   = $("#org_template").html();
            var template = Handlebars.compile(source);
            var content = {};
            content.org_name = $(this).prev().val();
            $(this).prev().val("");
            var htmlForOrg = template(content);
            parent.before(htmlForOrg);
            if (parent.siblings().length  == 3){
                parent.css("display","none");
            }
        } else {
            parent.css("display","none");
        }
    });
    $(document).on("click",".fa-chevron-circle-left",function(){
        $(this).parent().css("transform","translateX(100%) rotateY(90deg)");
        $(this).parent().next().css("transform","translateX(0) rotateY(0deg)");
    });
    
    $(document).on("click",".org>div:nth-child(2) .fa-chevron-circle-right",function(){
        $(".org>div:nth-child(2)").css("transform-origin","right")
    })
    
    $(document).on("click",".fa-chevron-circle-right",function(){
        $(this).parent().css("transform","translateX(-100%) rotateY(90deg)");
        $(this).parent().prev().css("transform","translateX(0) rotateY(0deg)");
    });
    
    $(document).on("click",".org>div:nth-child(2)>ul>li>span",function(){
        $(".org>div:nth-child(2)").css("transform-origin","left")
        $(this).parents("ul").parent().css("transform","translateX(100%) rotateY(90deg)");
        $(this).parents("ul").parent().next().css("transform","translateX(0) rotateY(0deg)");
    });
    
    
    $(document).on("click","#DB-Name+button",function(){
        var ul = $(this).parent().siblings("ul");
        if (ul.children().length < 5) {
            var name = $(this).prev().val();
            var source   = $("#dat_template").html();
            var template = Handlebars.compile(source);
            var content = {};
            $(this).parent().siblings("span").css("display","none");
            $(this).parent().siblings(".fa-plus-circle").css({
                "top":"11px",
                "right": "-105px",
                "font-size": "20px !important"
            });
            content.name = $(this).prev().val();
            $(this).prev().val("");
            var html = template(content);
            ul.append(html);
            if (ul.children().length == 5) {
                $(this).parent().siblings(".fa-plus-circle").css("display","none");
            }
        }
        
    });
    
    $(document).on("click","#Tbl-name+button",function(){
        var ul = $(this).parent().siblings("ul");
        if (ul.children().length < 25) {
            var name = $(this).prev().val();
            var source   = $("#dat_template").html();
            var template = Handlebars.compile(source);
            var content = {};
            $(this).parent().siblings("span").css("display","none");
            $(this).parent().siblings(".fa-plus-circle").css({
                "top":"11px",
                "right": "-105px",
                "font-size": "20px !important"
            });
            content.name = $(this).prev().val();
            $(this).prev().val("");
            var html = template(content);
            ul.append(html);
            if (ul.children().length == 25) {
                $(this).parent().siblings(".fa-plus-circle").css("display","none");
            }
        }
        
    });
    
    $(document).on("click",".fa-plus-circle",function(){
        $(this).next(".input").css("transform","scale(0.97)")
    })
    $(document).on("click",".input>button,.input>span",function(){
        $(this).parent(".input").css("transform","scale(0)")
    })
    
    // --------------------------------Sidebar--------------------------------------------
    var nu=1;
    $("header>i").click(function(){
        nu++;
        if(nu%2==0){
          $(".sidebar").css({
            "transform":"translateX(0px)"
        });
        $(".sidebar>ul>li:first-child img").css({
            "margin":"0 0 0px 32px",
            "width":"68px"
        })
        
        setTimeout(function(){
          $(".sidebar>ul>li:first-child span").css("display","inline")  
        },400)
             $(this).css("transform","rotate(180deg)"); 
        
                }else{
                    
                $(".sidebar").css("transform","translateX(175px)");
                
                $(".sidebar>ul>li:first-child img").css({
                    "margin":"0 159px 0px -20px",
                    "width":"58px"
                })
                
                $(".sidebar>ul>li:first-child span").css("display","none")
        
                $(this).css("transform","rotate(0deg)"); 
            }
        });
    
    // ------------------------------------Profile Security------------------------------------------
    
    $(document).on("click",".sidebar>ul>li:nth-child(2)",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        
        $(".security_whole").css("display","flex")
        $(".org_box,.cmn_tab,.whole_org,.org_name,.main_tab,.admin,.log,.dsh_brd").css("display","none")
    })
    
     var count=1;var n=1;
$(".security_whole>div:nth-child(2)>nav").click(function(){
        count++;
        if(count%2==0){
          $(".security_whole>div:nth-child(2)").css({
              "width":"600px"
          });
       
         $(".security_whole>div:nth-child(1)").css({
             "width":"56px"
         });
         
        }else{
            
        $(".security_whole>div:nth-child(1)").css({
             "width":"600px"
        }); 
         
        $(".security_whole>div:nth-child(2)").css({
              "width":"56px"
         });
    }
        
});
    pf_ed=1;
    $(".pf_ed").click(function(){
        pf_ed++;
        if(pf_ed%2==0){
            $(this).removeClass("fa fa-pencil").addClass("fa fa-check")        
        }else{
            $(this).removeClass("fa fa-check").addClass("fa fa-pencil")        
        }
    
    $(this).parent().children("span").attr("contenteditable","true");
    $(this).parent().children("span").css({
        "cursor":"text",
        "outline":"none",
        "border-bottom":"1px solid"
    });
    
});

$(".security_whole .slide_button div").click(function(){
         
             n++;
        if(n%2==0){
               $(".slide_button div").css({
                    "left":"4px"
                });
        }else{
                $(".slide_button div").css({
                    "left":"24px"
                }); 
        }
    });


// ---------------------------------------Common Table-------------------------------------------------------

    $(document).on("click",".sidebar>ul>li:nth-child(3),.sidebar>ul>li:nth-child(4)",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        
        $(".cmn_tab").css("display","block")
        $(".org_box,.security_whole,.org_name,.whole_org,.admin,.log,.main_tab,.dsh_brd").hide();
        $(".cmn_tab>h2").text("History")
        $(".cm_tb table th:first-child,.cm_tb table td:first-child").hide();
    })
    
    $(document).on("click",".sidebar>ul>li:nth-child(4)",function(){
        $(".cmn_tab>h2").text("Auth Tokens");
        $(".cmn_tab>h2").css("display","inline-block");
        //$(".cm_tb table th:first-child,.cm_tb table td:first-child").show();
    })
    
// ----------------------------------Database---------------------------------------------------

$(document).on("click",".org>h2",function(){
    if(nu%2==0){
         document.getElementById("demo").click();   
        }
        
        $(".org_box").css("display","none")
        $(".org_name i").css("display","block")
        $(".whole_org").append("<main id='create_dat' class='org_box dat'><h2>Create Database</h2><i class='fa fa-plus-circle' aria-hidden='true'></i><div class='crt_org'><i class='fa fa-times-circle' aria-hidden='true'></i><p class='clr'></p><input purpose='dat' placeholder='Database name'><button>Create</button></div></main>")

})

    $(document).on("click",".fa-plus-circle",function(){
        $(this).next(".crt_org").css("transform","scale(0.97)")
    })
    $(document).on("click",".crt_org>button,.fa-times-circle",function(){
        $(this).parent(".crt_org").css("transform","scale(0)")
    })
    
$(document).on("click",".org_name i",function(){
        $(".dat,.org_name i").css("display","none")
        $(".org").css("display","flex")
    })

// -----------------------------------------------Main Table---------------------------------------

$(document).on("click",".main_tab td",function(){
    //$(this).attr("contenteditable","true")
     $(".tbl_srch").css({
         "transform":"translate(0px)"
     })
     $(".srch_edit").hide();
    $(".row_edit").show();
})
$(document).on("click",".srch_col",function(){
    $(".srch_edit").show();
    $(".row_edit").hide();
    $(".tbl_srch").css({
         "transform":"translate(0px)"
     })
})
$(document).on("click",".can,.mn_tb td:first-child",function(){
    //$(this).attr("contenteditable","true")
     $(".tbl_srch").css({
         "transform":"translate(-350px)"
     })
    
})

$(document).on("click",".fa-sort",function(){
    $(this).parent().next().slideToggle();
})

$(document).on("click",".tble",function(){
    $(".whole_org,.security_whole,.cmn_tab").css("display","none")
    $(".main_tab").css("display","block")
})

    // ------------------------POPUP-------------------------------------------------
    
    $(".col_but,.tb_op .fa-pencil").click(function(){
        
        $(".whole_popup").css({
            "transform":"translateY(50px) scale(0.2)"
        })
        
        
        $(".main_tab").css("filter","blur(3px)")
            
        setTimeout(function(){
            $(".whole_popup").css({
                "transform":"translateY(50px) scale(1)",
                "border-radius":"10px",
                "width":"650px",
                "left":"50%",
                "top":"50%",
                "margin":"-300px 0 0 -325px",
                "color":"black",
                "box-shadow":"0px 0px 10px 2px rgba(0, 0, 0, 0.32)",
                "text-shadow":"0px 0px 1px rgba(255, 255, 255, 0.68), 0px 0px 2px rgba(255, 255, 255, 0.71)",
                "color": "rgba(0, 0, 0, 0.87)"
            })
            
            $(".whole_popup>div,.whole_popup button").css("opacity","1") },500) 
    });
    
    $(".submit,.cancel").click(function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $(".main_tab").css("filter","blur(0px)")
        // $(".main_tab>caption button:nth-child(2)").css("opacity","1")
        
        $(".whole_popup").css({
            "transform":"translateY(50px) scale(0.2)",
            "border-radius":"50%",
            "width":"500px",
            "left":"50%",
            "top":"50%",
            "margin":"-300px 0 0 -325px",
            "color":"black",
            "box-shadow":"0px 0px 10px 7px rgba(0, 0, 0, 0.32)",
            "text-shadow":"0px 0px 1px rgba(255, 255, 255, 0.68), 0px 0px 2px rgba(255, 255, 255, 0.71)",
            "color": "rgba(0, 0, 0, 0.87)"
        })
            
        $(".whole_popup>div,.whole_popup button").css("opacity","0")
                
        setTimeout(function(){
            $(".whole_popup").css("transform","translateY(-100%) scale(0.2)")},500)
        })
    
    $(".add").click(function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $(".whole_popup").css("background","none")
        $(".add_col").append("<div class='col'><ul><li><i class='fa fa-times-circle sel_col_del' aria-hidden='true'></i><label>Column:</label><input></li><li><label>Type</label><select><option selected>--select--</option><option>Text</option><option>Integer</option><option>Boolean</option><option>Float</option><option>Date</option><option>Time</option><option>Time stamp</option></select><input><label>Charc</label></li><li><label>Default Value:</label><input></li><li><input type='checkbox'><label>Mandatory</label></li></ul></div>")
    })
    
    $(document).on("click",".sel_col_del",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $(this).parent().parent().parent().css("display","none")
    })
    
    $(document).on("click",".del,.submit",function(){
        $(".col").css("display","none")
        $(".whole_popup").css("background","url(https://cdn.dribbble.com/users/463734/screenshots/2016792/empty-result_shot.png) center/cover")
    })
    
            // --------------------------------Admin Dashboard---------------------------------------------

    $(document).on("click",".sidebar ul li:last-child",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $(".admin").css("display","block")
        $(".whole_org").css("display","none")
    })
    
    // $(document).on("click",".add_fld button,.add_fld .fa-trash-o",function(){
        
    //     $(this).parent(".add_fld").slideUp();
    // })
    
    // $(document).on("click",",list .fa-pencil-square-o",function(){
    //     $(".list input").attr("disabled","false")
    //     $(".list input").css("border-bottom","1px solid silver")
    // })
    
        // ----------------------------------------Log file--------------------------------
        
    $(document).on("click",".fa-file-text-o",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $(".log").css("display","block")
        $(".whole_org,.cmn_tab,.dsh_brd,.main_tab,.admin").css("display","none")
    })  
    $(document).on("click",".log_list li",function(){
        $(".log_list").hide();
        $(".log_show").show();
        
        $(".log_det").text($(this).children("date").text())
    })
    
    
    // ----------------------------------------Dash board--------------------------------
        
    $(document).on("click",".sidebar ul li:nth-child(5)",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $(".dsh_brd").css("display","block")
        $(".whole_org,.whole_org,.cmn_tab,.main_tab,.admin,.log,.security_whole").hide();
    })  
    
            // -------------------------dashboard overview----------------------
    
    $(document).on("click",".ovrView",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $(".dsh_ovr").css("display","block")
        $(".dsh_brd .box").css("display","none")
    })
    
    $(document).on("click",".fa-hand-o-left",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $(".dsh_ovr").hide();
        $(".dsh_brd .box").show();
    })
    var dt=1;
    $(document).on("click",".dtb li span",function(){
        dt++;
        $(this).next().next(".tbl").slideToggle()
        if(dt%2==0){
            $(this).prev(".dtb .fa-caret-right").css("transform","rotate(90deg)")
        }else{
            $(this).prev(".dtb .fa-caret-right").css("transform","rotate(0deg)")
        }
    })
    
    $(document).on("click",".dsh_ovr .tbl li span i",function(){
        $(".setGrph").css("transform","translate(0px)");
        $(".tbl_srh_box").css("height","60px");
        $(".srch_edit").show();
        $(".b").addClass("grpShow");
        $(".row_edit").css({
            "height": "calc(100% - 135px)",
            "overflow":"auto"
        })
        // $(".whole_org,.whole_org,.cmn_tab,.main_tab,.admin,.log,.dsh_ovr,.box").css("display","none")
    })
    
    $(document).on("click",".grpShow",function(){
        $(".chart").show();
        $(".whole_org,.whole_org,.cmn_tab,.main_tab,.admin,.log,.dsh_ovr,.box").hide();
    })
    
    $(document).on("click",".chart .rghtCls",function(){
        $(".dsh_ovr").show();
        $(".whole_org,.whole_org,.cmn_tab,.main_tab,.admin,.log,.chart,.box").hide();
    })
       
    
});