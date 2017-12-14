function tab_change(tab) {
    var i;
    var x = document.getElementsByTagName("aside");
   
    for (i = 0; i < x.length; i++) {
       x[i].style.display = "none";
    }
    document.getElementById(tab).style.display = "block";  
}
$(document).ready(function(){
	$(document).on("click",".org_box>span,.databs>span",function(){
// 		$(".whole_org_box").css("display","none");
// 		$(".whole_org_box").hide();
//         // $(".whole_org_box").css("transform","translate(-100%)")
//         // $(".whole_database").css("transform","translate(0%)")
//         setTimeout(function(){
//             $(".whole_org_box").hide();
// //         },700)
// alert($(this).text());
// alert(location.pathname+$(this).text());
        alert($(this).parent().attr("class"));
        console.log($(this).parent().attr("class"));
        var path = location.pathname;
        if (path.endsWith("/")){
            path = path +$(this).text() ; 
        } else {
            path = path +"/"+$(this).text();
        }
        history.pushState({title:"xczxczx"},null,path);
        console.log("done");
        niceURL();
// alert("org");
//         history.pu
// 		$(".whole_database").css("display","flex");
	});
// 	$(document).on("click",".databs>span",function(){
// 	    console.log("hi");
// 		$(".whole_database").css("display","none");
//         // $(".whole_org_box").css("transform","translate(-100%)")
//         // $(".whole_database").css("transform","translate(0%)")
//         // // setTimeout(function(){
//         //     $(".whole_database").hide();
//         // },700)
//         alert("db");
// 		$(".whole_table").css("display","flex");
// 	});
	
	
// 	-------------------------------Share page-------------------------------------

    $(document).on("click",".fa-share-alt",function(){
        //$(".share").css("transform","translateY(0)")
        $(".share").css("display","flex");
        $(".org").css("display","none");
    });
    
    drop_num=1;
    $(".dropdown").click(function(){
        drop_num++;
        if(drop_num%2==0){
            $(this).css("height","auto");
        }else{
            $(this).css("height","30px");
        }
        
    });
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
    
    $(document).on("click",".options .fa-trash-o,.fa-users",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        
        $(".org_popup").css("top","14%")
        $(".whole_org").css("filter","blur(5px)")
        //$(".org_popup .decline").css("display","none")
    })
    
    $(document).on("click",".org_popup .decline",function(){
        $(".org_popup").css("top","-300%")
        $(".whole_org").css("filter","blur(0px)")
    })
    
    // ------------------------------------Home-------------------------------------------
    
    $(document).on("click","header>span",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $(".org_box").css("display","flex")
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
        var name = $(this).prev().val();
        var purpose =  $(this).attr("purpose");var currentPath = location.pathname; 
        var pathList = currentPath.substring(4).split("/");
        var data = {};
        data.org_name = pathList[0];
        if (purpose == "Organization"){
            data.org_name = $(this).prev().val();
            sendPostRequest( "/createOrg",data,alert);
        } else if (purpose == "Database"){
            data.db_name = $(this).prev().val();
           sendPostRequest(  "/createDB",data,alert);
        }  else if (purpose == "Table"){
            data.db_name = pathList[1];
            data.table_name = $(this).prev().val();
            sendPostRequest(  "/createTable",data,alert);
        } 
    });
    // $(document).on("click","#Name+button",function(){
        
        // var parent = $(this).parents("main");
        // if (parent.siblings().length  < 3){
        //     var source   = $("#org_template").html();
        //     var template = Handlebars.compile(source);
        //     var content = {};
        //     content.org_name = $(this).prev().val();
        //     $(this).prev().val("");
        //     var htmlForOrg = template(content);
        //     parent.before(htmlForOrg);
        //     if (parent.siblings().length  == 3){
        //         parent.css("display","none");
        //     }
        // } else {
        //     parent.css("display","none");
        // }
    // });
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
    
    $(".security_whole li").click(function(){
    $(this).attr("contenteditable","true");
    $(this).css({
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
        $(".org_box,.security_whole,.org_name,.whole_org,.admin,.log,.main_tab").css("display","none")
        $(".cmn_tab>h2").text("History")
    })
    
    $(document).on("click",".sidebar>ul>li:nth-child(4)",function(){
        $(".cmn_tab>h2").text("Auth Tokens")
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
$(document).on("click",".row_but,.main_tab td",function(){
    //$(this).attr("contenteditable","true")
     $(".tbl_srch").css({
         "transform":"translate(0px)"
     })
    
})


$(document).on("click",".sub",function(){
    //$(this).attr("contenteditable","true")
    var purpose = $(this).parent().attr("purpose");
    var data = {};
    var path = location.pathname.substring(4).split("/");
    data.org_name = path[0];
    data.db_name = path[1];
    data.table_name = path[2];
    if (purpose == "add"){
        
        data.records = JSON.stringify(addColumn());
        console.log(data);
        sendPostRequest("/addRecord",data,alert);
        
        
    }
})


$(document).on("click",".can,.sub",function(){
    //$(this).attr("contenteditable","true")
     $(".tbl_srch").css({
         "transform":"translate(-350px)"
     })
    
})

$(document).on("click",".fa-sort",function(){
    $(".sort_box").slideToggle();
})

// $(document).on("click",".tble",function(){
//     $(".whole_org,.security_whole,.cmn_tab").css("display","none")
//     $(".main_tab").css("display","block")
// })

    // ------------------------POPUP-------------------------------------------------
    
    $(".hdr>button:nth-child(1)").click(function(){
        $(".whole_popup").css("background","url(https://cdn.dribbble.com/users/463734/screenshots/2016792/empty-result_shot.png) center/cover")
        $(".whole_popup").css({
            "transform":"translateY(50px) scale(0.2)"
        })
        
        // $(".main_table>caption button:nth-child(2)").css("opacity","0")
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
        // if(nu%2==0){
        //  document.getElementById("demo").click();   
        // }
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
            $(".whole_popup").css("transform","translateY(-300px) scale(0.2)")},500)
        })
    
    $(".add").click(function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $(".whole_popup").css("background","none");
        console.log("add");
        var theTemplateScript,theTemplate,theCompiledHtml;
        theTemplateScript = $("#addcolumn").html();
        theTemplate = Handlebars.compile(theTemplateScript);
         theCompiledHtml = theTemplate({});
        //  $('.whole_org_box').append(theCompiledHtml);
        $(".add_col").append(theCompiledHtml);
        // $(".add_col").append("<div class='col'><ul><li><i class='fa fa-times-circle sel_col_del' aria-hidden='true'></i><label>Column:</label><input></li><li><label>Type</label><select><option selected>--select--</option><option>Text</option><option>Integer</option><option>Boolean</option><option>Float</option><option>Date</option><option>Time</option><option>Time stamp</option></select><input><label>Charc</label></li><li><label>Default Value:</label><input></li><li><input type='checkbox'><label>Mandatory</label></li></ul></div>")
    })
    
    $(document).on("click",".sel_col_del",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
    }) 
    $(document).on("click",".submit",function(){
        console.log("submiting");
        var data = {};
         $(".add_col").html();
        alert(getColumnDetail())
        data.query = JSON.stringify(getColumnDetail());
        
        data.wanted = "addColumn";
        var path = location.pathname.substring(4).split("/");
        data.org_name = path[0];
        data.db_name = path[1];
        data.table_name = path[2];
        console.log("asdads\"asd = " +data);
        sendPostRequest("/alterTable",data,alert);
        console.log("submiting");
        // $(".col").css("display","none")
        $(".whole_popup").css("background","url(https://assets.materialup.com/uploads/77a5d214-0a8a-4444-a523-db0c4e97b9c0/preview.jpg) center/cover");
        
    })
    $(document).on("click",".del,.submit,.cancel",function(){
         $(".col").remove();
        $(".whole_popup").css("background","url(https://assets.materialup.com/uploads/77a5d214-0a8a-4444-a523-db0c4e97b9c0/preview.jpg) center/cover");
        console.log("canceling");
    })
    
            // --------------------------------Admin Dashboard---------------------------------------------
    $(document).on("click",".add-column-x",function(){
        $(this).parents(".col").remove();
    });
    $(document).on("click",".sidebar ul li:last-child",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $(".admin").css("display","block")
        $(".whole_org").css("display","none")
    })
    
    $(document).on("click",".add_fld button,.add_fld .fa-trash-o",function(){
        
        $(this).parent(".add_fld").slideUp();
    })
    
    $(document).on("click",",list .fa-pencil-square-o",function(){
        $(".list input").attr("disabled","false")
        $(".list input").css("border-bottom","1px solid silver")
    })
    
        // ----------------------------------------Log file--------------------------------
        
    $(document).on("click",".fa-file-text-o",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        
        $(".log").css("display","block")
        $(".whole_org,.cmn_tab,.dsh_brd,.main_tab,.admin").css("display","none")
    })  
    
    // ----------------------------------------Dash board--------------------------------
        
    $(document).on("click",".sidebar ul li:nth-child(5)",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $(".dsh_brd").css("display","block")
        $(".whole_org,.whole_org,.cmn_tab,.main_tab,.admin,.log").css("display","none")
    })  
    
            // -------------------------dashboard overview----------------------
    
    $(document).on("click",".dsh_org footer span:first-child",function(){
        if(nu%2==0){
         document.getElementById("demo").click();   
        }
        $(".dsh_ovr").css("display","block")
        $(".dsh_brd .box").css("display","none")
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
        $(".chart").css("display","block")
        $(".whole_org,.whole_org,.cmn_tab,.main_tab,.admin,.log,.dsh_ovr,.box").css("display","none")
    });
     
});
niceURL();