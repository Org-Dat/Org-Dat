

function showAllOrg(orgNames){
    var array = JSON.parse(orgNames);
    if (array.status != 200){
        alert("some error occrence");
        return;
    }
     array = array.Records;
    var theTemplate,theTemplateScript,context,theCompiledHtml;
    $('.whole_org_box').html();
    if (tem.length < 3) {
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
    $(".whole_org_box").css("display","block");
    $(".whole_database").css("display","none");
    $(".whole_table").css("display","none");
    $(".main_tab").css("display","none");
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
        alert("some error occrence");
        return;
    }
     array = array.Records;
    var theTemplate,theTemplateScript,context,theCompiledHtml;
    $('.database').html();
    if (tem.length < 5) {
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
    $(".whole_org_box").css("display","none");
    $(".whole_database").css("display","flex");
    $(".whole_table").css("display","none");
    $(".main_tab").css("display","none");
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
        alert("some error occrence");
        return;
    }
     array = array.Records;
    var theTemplate,theTemplateScript,context,theCompiledHtml;
    $('.table').html();
    if (tem.length < 25) {
        theTemplateScript = $("#create_template").html();
        theTemplate = Handlebars.compile(theTemplateScript);
         context={"cla2": ""};
         context.cla1 = "databs";
         context.cla2 = "tble";
         context.purpose = "Table";
         theCompiledHtml = theTemplate(context);
         $('.database').append(theCompiledHtml);
    }
    theTemplateScript = $("#tab_template").html();
    theTemplate = Handlebars.compile(theTemplateScript);
    $(".whole_org_box").css("display","none");
    $(".whole_table").css("display","flex");
    $(".main_tab").css("display","none");
    $(".whole_database").css("display","none");
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
        alert("some error occrence");
        return;
    }
     array = array.Records;
    var theTemplate,theTemplateScript,theCompiledHtml;
    $('.mn_tb').html("");
    theTemplateScript = $("#table-template").html();
    theTemplate = Handlebars.compile(theTemplateScript);
    $(".whole_org_box").css("display","none");
    $(".whole_table").css("display","none");
    $(".main_tab").css("display","flex");
    $(".whole_database").css("display","none");
    theCompiledHtml = theTemplate(listToObject(array));
    $('.mn_tb').html(theCompiledHtml);
}

function listToObject(detailArray){
	var renderObj = {};
	var dataList = [];
	for(let key of detailArray[0]){
		let tmp = {};
		tmp.key = key;
		dataList.push(tmp);
	}
	renderObj.headers = dataList;
	dataList = [];
	for(let i = 1;i < detailArray.length;i++){
		let list  = [];
		for(let j =0 ;j < detailArray[i].length;j++ ){
			
			let tmpObj = {};
			tmpObj.values = detailArray[i][j];
			list.push(tmpObj);
		}
		let obj ={};
		obj.value = list;
		dataList.push(obj);
	}
	renderObj.values  = dataList;
	return renderObj;
}
/*
$(document).ready(function(){
	$(document).on("click",".org_box>span",function(){
		$(".whole_org_box").hide();
        // $(".whole_org_box").css("transform","translate(-100%)")
        // $(".whole_database").css("transform","translate(0%)")
        setTimeout(function(){
            $(".whole_org_box").hide();
        },700);
		$(".whole_database").css("display","flex");
	});
	
    $(document).on("click",".tble",function(){
        $(".whole_org,.security_whole,.cmn_tab").css("display","none")
        $(".main_tab").css("display","block")
    })
	$(document).on("click",".databs>span",function(){
	    console.log("hi");
		$(".whole_database").hide();
        // $(".whole_org_box").css("transform","translate(-100%)")
        // $(".whole_database").css("transform","translate(0%)")
        // setTimeout(function(){
            $(".whole_database").hide();
        // },700)
		$(".whole_table").css("display","flex");
	});
	
    $(document).on("click","#Name+button",function(){
        alert($(this).prev().val());
        $.post({url: "/createOrg?org_name="+$(this).prev().val(), success: function(result){
            alert(result);
        }});
    });
});
*/
 
function niceURL(){
    var currentPath = location.pathname; 
    var pathList = currentPath.split("/");
    if(pathList.length = 1){
        sendGetRequest("/getDBNames?org_name="+pathList[0],showAllDB);
        // return {"ajax" : "getDBNames" , "org_name" : pathList[0]};
    }
    else if(pathList.length == 2){
        showAlltable
        sendGetRequest("/getTableNames?org_name="+pathList[0]+"&db_name="+pathList[1],showAlltable);
        // return {"ajax" : "getTableNames" , "db_name" : pathList[1], "org_name" : pathList[0] };
    }else if(pathList.length == 3){
       sendGetRequest("/readRecord?org_name="+pathList[0]+"&db_name="+pathList[1]+"&table_name="+pathList[2],showAllDB);
        // return  {"ajax" : "readRecord" , "table_name" : pathList[2], "db_name" : pathList[1] ,"org_name" : pathList[0] };
    }else{
         history.replaceState(null,null,"/");
         sendGetRequest("/getOrgNames", showAllOrg);
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

function getColumnName(){
    var element = document.getElementsByClassName("columnName");
    var updateValues =  document.getElementsByClassName("values");
    var count = element.length;
    let i = 0;
    var tmpObj = {};
    var list = [];
    while(i < count){
          tmpObj[element[i].innerText] = updateValues[i].value;
          i++;
    }
   return tmpObj;
}

function getFilter(){
    var column_name = document.getElementsByClassName("columnName");
    var opertor = document.getElementsByClassName("opertor");
    var values = document.getElementsByClassName("update");
    let i = 0;
    var valList = [];
    while(i < column_name.length){
       valList.push({"column" :column_name[i].innerText , "condition" : opertor[i].innerText , "value" : values[i].innerText});
    }
  var list = [];
  list.push(document.getElementsByClassName("conditionOpertor")[0].innerText);
  list.push(valList);
  return list;
}
