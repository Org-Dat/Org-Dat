$(document).ready(function(){ /// .hdr>button:nth-child(1)
    $(document).on("click",".row_but",function(){
        $(".tbl_srch").css({
         "transform":"translate(0px)"
        });
        $(".tbl_srh_box,.tbl_con").css("display","none");
        $(".row_edit").html("");
        var theTemplate,theTemplateScript,context,theCompiledHtml;
        theTemplateScript = $("#addrow-template").html();
        theTemplate = Handlebars.compile(theTemplateScript);
         theCompiledHtml = theTemplate({list:getColumnNames()});
         $('.row_edit').html(theCompiledHtml);
         $(".sub_can").attr("purpose","add");
        
    });
    $(document).on("click",".sub.b",function(){
       var purpose = $(this).parent().attr("purpose");
       if (purpose == "add" ) {
           
       } else if (purpose == "fillter" ) {
           
       }
    });
});


function showHide(colType){
    if(colType == "bigserial"){
        document.getElementBy("").setAttribute("style","visiblitity :hidden");
    }
}
function showAllOrg(orgNames){
    var array = JSON.parse(orgNames);
    if (array.status != 200){
        alert(array.message);
        return;
    }
     array = array.Records;
    var theTemplate,theTemplateScript,context,theCompiledHtml;
    $('.whole_org_box').html("");
    if (array.length < 3) {
        theTemplateScript = $("#create_template").html();
        theTemplate = Handlebars.compile(theTemplateScript);
        context={"cla2": ""};
        context.cla1 = "org_box";
        context.purpose = "Organization";
        theCompiledHtml = theTemplate(context);
        $('.whole_org_box').append(theCompiledHtml);
    }
    theTemplateScript = $("#org_template").html();
    theTemplate = Handlebars.compile(theTemplateScript);
    $(".whole_org").css("display","block");
    $(".whole_org_box").css("display","flex");
    $(".whole_database,.whole_table,.main_tab").css("display","none");
    for (var name of array){
         context={"count": ""};
          context.name = name;
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

