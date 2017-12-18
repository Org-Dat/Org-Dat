$(document).ready(function(){
    $(document).on("click", "#Name+button", function() {
        var name = $(this).prev().val();
        var purpose = $(this).attr("purpose");
        var currentPath = location.pathname;
        var pathList = currentPath.substring(4).split("/");
        var data = {};
        data.org_name = pathList[0];
        if (purpose == "Organization") {
            data.org_name = $(this).prev().val();
            sendPostRequest("/createOrg", data, niceURL);
        } else if (purpose == "Database") {
            data.db_name = $(this).prev().val();
            sendPostRequest("/createDB", data, niceURL);
        } else if (purpose == "Table") {
            data.db_name = pathList[1];
            data.table_name = $(this).prev().val();
            sendPostRequest("/createTable", data, niceURL);
        }
    });
    
    $(document).on("click", ".sub", function() {
        //$(this).attr("contenteditable","true")
        var purpose = $(this).parent().attr("purpose");
        var data = {};
        var path = location.pathname.substring(4).split("/");
        data.org_name = path[0];
        data.db_name = path[1];
        data.table_name = path[2];
        if (purpose == "add") {
            data.records = JSON.stringify(addColumn());
            // console.log(addColumn());
            // console.log(data);
            sendPostRequest("/addRecord", data, showTable);
        } else if (purpose == "update") {
            data.set = JSON.stringify(addColumn());
            var tem = {};
            tem.column = "roll_no";
            tem.condition = "=";
            tem.value = $(this).parent().attr("roll_no");
            console.log(JSON.stringify(tem));
            console.log(data);
            data.conditionArray = JSON.stringify([tem]);
            sendPostRequest("/updateRecord", data, showTable);
        } else if (purpose == "filter") {
            data.conditionArray = JSON.stringify(getFilter());
            data.andOr = document.getElementsByClassName("tbl_con")[0].innerText.trim();
            sendPostRequest("/readRecord", data, showTable);
        }
    });


    $(document).on("click", ".can,.sub", function() {
        //$(this).attr("contenteditable","true")
        $(".tbl_srch").css({
            "transform": "translate(-350px)"
        })

    })

    $(document).on("click", ".main_tab td", function() {
         console.log($(this).index())
         if (Number($(this).index()) == 0){
            $(".del_row").css(  "visibility", "visible");var data = {};
            
         } else {
             $('.row_edit').html("");
            $(".filter_box").css("display", "none");
            $(".row_edit").css("display", "block");
            $(".row_edit").html("");
            $(".tbl_srch").css({
             "transform":"translate(0px)"
            });
            var theTemplate, theTemplateScript, context, theCompiledHtml;
            theTemplateScript = $("#addrow-template").html();
            theTemplate = Handlebars.compile(theTemplateScript);
            theCompiledHtml = theTemplate({
                list: getRecordDetail(this.parentElement)
            });
            $('.row_edit').html(theCompiledHtml);
            $(".sub_can").attr("purpose", "update");
            // var jh = $(this).siblings("td : nth-child(2)").text();.querySelectorAll("td")
            //var  we = this.parentElement.querySelector("td : nth-child(2)").innerText;
            var  we = this.parentElement.querySelectorAll("td")[1].innerText;
            // console.log(jh);
            console.log(we)
            $(".sub_can").attr("roll_no", we); 
            $(".filter_box").css("display", "none");
        }//this.parentElement.querySelector("td : nth-child(2)").innerText);
    })

    
    $(".add").click(function() {
        // $(".whole_popup").css("background", "none");
        console.log("add");
        var theTemplateScript, theTemplate, theCompiledHtml;
        theTemplateScript = $("#addcolumn").html();
        theTemplate = Handlebars.compile(theTemplateScript);
        theCompiledHtml = theTemplate({});
        //  $('.whole_org_box').append(theCompiledHtml);
        $(".add_col").append(theCompiledHtml);
        // $(".add_col").append("<div class='col'><ul><li><i class='fa fa-times-circle sel_col_del' aria-hidden='true'></i><label>Column:</label><input></li><li><label>Type</label><select><option selected>--select--</option><option>Text</option><option>Integer</option><option>Boolean</option><option>Float</option><option>Date</option><option>Time</option><option>Time stamp</option></select><input><label>Charc</label></li><li><label>Default Value:</label><input></li><li><input type='checkbox'><label>Mandatory</label></li></ul></div>")
    })


    $(document).on("click", ".submit", function() {
        console.log("submiting");
        var data = {};
        alert(getColumnDetail())
        data.query = JSON.stringify(getColumnDetail());

        data.wanted = "addColumn";
        var path = location.pathname.substring(4).split("/");
        data.org_name = path[0];
        data.db_name = path[1];
        data.table_name = path[2];
        console.log("asdads\"asd = " + data);
        $(".add_col").html("");
        sendPostRequest("/alterTable", data, niceURL);
        console.log("submiting");
        // $(".col").css("display","none")
        $(".whole_popup").css("background", "url(https://assets.materialup.com/uploads/77a5d214-0a8a-4444-a523-db0c4e97b9c0/preview.jpg) center/cover");

    });
    
    $(document).on("click",".row_but",function(){
        $(".tbl_srch").css({
         "transform":"translate(0px)"
        });
        $(".filter_box").css("display","none");
        $(".row_edit").html("");
        var theTemplate,theTemplateScript,context,theCompiledHtml;
        theTemplateScript = $("#addrow-template").html();
        theTemplate = Handlebars.compile(theTemplateScript);
        theCompiledHtml = theTemplate({list:getColumnNames()});
        $('.row_edit').html(theCompiledHtml);
        $(".sub_can").attr("purpose","add");
    });
    $(document).on("click","#addMember",function(){
        alert(" add member wa clicked ");
        sendPostRequest("/createMember",getMemberDetail(),alert);
    })

    $(document).on("click", ".add-column-x", function() { 
        $(this).parents(".col").remove();
    });
     
    $(document).on("click", ".del,.submit,.cancel", function() {
        $(".col").remove();
    });
    $(document).on("click",".sidebar ul li:last-child",function(){
        var org = location.pathname.substring(4);
        $(".add_mail").text("@"+org.substring(0,org.indexOf("/"))+".com");
        $(".share_org").text(org.substring(0,org.indexOf("/")));
    })
    
    $(document).on("click","#add_mem",function(){
       var data = getMemberDetail();
       
    });
    
    
     $(document).on("click", ".del_row", function() {
       // alert(JSON.stringify(getValue("check")))
       var data={};
        var path = location.pathname.substring(4).split("/");
        data.org_name = path[0]; 
        data.db_name = path[1];
        data.table_name = path[2];
        data.andOr ="OR";
        alert(this.parentNode)
        data.conditionArray = JSON.stringify(getValue("check"));
        sendPostRequest("/deleteRecord", data, showTable);
     });
     
    $(document).on("click", ".org_box>span,.databs>span", function() {
        console.log($(this).parent().attr("class"));
        var forPath = ["org_box", "databs", "databs tble"];
        var tem = $(this).parent().attr("class");
        history.pushState({
            title: "xczxczx"
        }, null, getSetPath(forPath.indexOf(tem) + 1, $(this).text()));
        console.log("done");
        niceURL();
    });
    
    $(document).on("click", ".srch_col", function() {
        // $(this).parent().slideToggle();
        // $(".filter_select").css("display","none");

        $(".srch_edit").css("display", "block");
        $(".row_edit").css("display", "none");
        $(".tbl_srch").css({
            "transform": "translate(0px)"
        });
        $(".srch_edit").html(""); 
        var theTemplate, theTemplateScript, context, theCompiledHtml;
        theTemplateScript = $("#filter-template").html();
        theTemplate = Handlebars.compile(theTemplateScript);
        theCompiledHtml = theTemplate({
            list: getColumnNames(),
            condition: [{
                "opareter": "startsWith"
            }, {
                "opareter": "endsWith"
            }, {
                "opareter": "contains"
            }, {
                "opareter": "equals"
            },  {
                "opareter": ">"
            }, {
                "opareter": "<"
            }, {
                "opareter": "!="
            }]
        });
        $('.srch_edit').html(theCompiledHtml);
        $('.srch_edit').append('<div class="tbl_con"><span class="cond">OR</span><i class="fa fa-plus-square-o" aria-hidden="true"></i> </div>');
        $(".sub_can").attr("purpose", "filter");

    })
    
    // $(document).on("click", ".row_but",function(){
    //     $(".row_edit").html("");
    //     var theTemplateScript, theTemplate, theCompiledHtml;
    //     theTemplateScript = $("#addrow-template").html();
    //     theTemplate = Handlebars.compile(theTemplateScript);
    //     theCompiledHtml = theTemplate({list:JSON.stringify(getColumnNames())});
    //     $(".row_edit").html(theCompiledHtml);
    // });
    
    
    
});
function showAllOrg(orgNames){
    var array = JSON.parse(orgNames);
    if (array.status != 200){
        alert(array.message);
        return;
    }
     array = array.Records;
    var theTemplate,theTemplateScript,context,theCompiledHtml;
    $('.whole_org_box').html("");
    
    theTemplateScript = $("#org_template").html();
    theTemplate = Handlebars.compile(theTemplateScript);
    $(".whole_org").css("display","block");
    $(".whole_org_box").css("display","flex");
    $(".whole_database,.whole_table").css("display","none");
    for (var name of array){
         context={"count": ""};
          context.name = name;
         theCompiledHtml = theTemplate(context);
        $('.whole_org_box').append(theCompiledHtml);
    }
    if (array.length < 3) {
        theTemplateScript = $("#create_template").html();
        theTemplate = Handlebars.compile(theTemplateScript);
        context={"cla2": ""};
        context.cla1 = "org_box";
        context.purpose = "Organization";
        theCompiledHtml = theTemplate(context);
        $('.whole_org_box').append(theCompiledHtml);
    }
}


function showAllDB(dbNames){
    var array = JSON.parse(dbNames); 
    if (array.status != 200){
        alert(array.message);
        return;
    }
     array = array.Records;
    var theTemplate,theTemplateScript,context,theCompiledHtml;
    $('.database').html("");
    if (array.length < 5) {
        theTemplateScript = $("#create_template").html();
        theTemplate = Handlebars.compile(theTemplateScript);
         context={"cla2": ""};
         context.cla1 = "databs";
         context.purpose = "Database";
         theCompiledHtml = theTemplate(context);
         $('.database').append(theCompiledHtml);
    }
    theTemplateScript = $("#dat_template").html();
    theTemplate = Handlebars.compile(theTemplateScript);
    $(".whole_org").css("display","block");
    $(".whole_org_box,.whole_table,.main_tab").css("display","none");
    $(".whole_database").css("display","flex");
    for (var name of array){
         context={"count": ""};
          context.name = name;
         theCompiledHtml = theTemplate(context);
        $('.database').append(theCompiledHtml);
    }
}
function showAlltable(tableNames){
    var array = JSON.parse(tableNames);
    if (array.status != 200){
        alert(array.message);
        return;
    }
     array = array.Records;
    var theTemplate,theTemplateScript,context,theCompiledHtml;
    $('.table').html("");
    if (array.length < 25) {
        theTemplateScript = $("#create_template").html();
        theTemplate = Handlebars.compile(theTemplateScript);
         context={"cla2": ""};
         context.cla1 = "databs";
         context.cla2 = "tble";
         context.purpose = "Table";
         theCompiledHtml = theTemplate(context);
         $('.table').append(theCompiledHtml);
    }
    theTemplateScript = $("#tab_template").html();
    theTemplate = Handlebars.compile(theTemplateScript);
    $(".whole_org").css("display","block");
    $(".whole_org_box,.main_tab,.whole_database").css("display","none");
    $(".whole_table").css("display","flex");
    for (var name of array){
         context={"count": ""};
          context.name = name;
         theCompiledHtml = theTemplate(context);
        $('.table').append(theCompiledHtml);
    }
}

function showTable(resoponse){
    var array = JSON.parse(resoponse);
    if (array.status != 200){
        alert(array.message);
        return; 
    } 
    alert( array["TableTable"]); 
    $(".tbl_name").text( array["TableTable"]);
     array = array.Records;
    var theTemplate,theTemplateScript,theCompiledHtml;
    $('.mn_tb').html("");
    theTemplateScript = $("#table-template").html();
    theTemplate = Handlebars.compile(theTemplateScript);
    $(".whole_org").css("display","none");
    // $(".whole_table").css("display","none");
    $(".main_tab").css("display","block");
    // $(".whole_database").css("display","none");
    var s = listToObject(array);
    console.log("s = " + s);
    theCompiledHtml = theTemplate(s);
    $('.mn_tb').html(theCompiledHtml);
}
// $(document).ready(function(){ /// .hdr>button:nth-child(1)
//     $(document).on("click",".row_but",function(){
//         $(".tbl_srch").css({
//          "transform":"translate(0px)"
//         });
//         $(".filter_box").css("display","none");
//         $(".row_edit").html("");
//         var theTemplate,theTemplateScript,context,theCompiledHtml;
//         theTemplateScript = $("#addrow-template").html();
//         theTemplate = Handlebars.compile(theTemplateScript);
//         theCompiledHtml = theTemplate({list:getColumnNames()});
//         $('.row_edit').html(theCompiledHtml);
//         $(".sub_can").attr("purpose","add");
//     });
//     $(document).on("click","#addMember",function(){
//         alert(" add member wa clicked ");
//         sendPostRequest("/createMember",getMemberDetail(),alert);
//     })
// });


function showHide(colType){
    if(colType == "bigserial"){
        document.getElementBy("").setAttribute("style","visiblitity :hidden");
    }
}
$(document).ready(function(){
    niceURL();
})