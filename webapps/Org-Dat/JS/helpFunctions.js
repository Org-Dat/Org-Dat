function listToObject(detailArray){
	var renderObj = {};
	var dataList = [];
	for(let i= 0 ;i <detailArray[0].length ;i++){
		let tmp = {};
		tmp.key = detailArray[0][i];
		tmp.type = detailArray[1][i];
		dataList.push(tmp);
	}
	renderObj.headers = dataList;
	dataList = [];
	for(let i = 2;i < detailArray.length;i++){
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


function niceURL(){
    var currentPath = location.pathname; 
    if (currentPath.endsWith("/")){
        currentPath = currentPath.substring(0,currentPath.length-1);
    }
    var pathList = currentPath.substring(4).split("/");
    console.log(pathList);
    if (currentPath.startsWith("/v1")){
        if((pathList.length == 1) && pathList[0] != ""){
            sendGetRequest("/getDBName?org_name="+pathList[0],showAllDB);
            // return {"ajax" : "getDBNames" , "org_name" : pathList[0]};
        }
        else if(pathList.length == 2  && pathList[0] != ""  && pathList[1] != ""){
            // showAlltable
            sendGetRequest("/getTableName?org_name="+pathList[0]+"&db_name="+pathList[1],showAlltable);
            // return {"ajax" : "getTableNames" , "db_name" : pathList[1], "org_name" : pathList[0] };
        }else if(pathList.length == 3  && pathList[0] != ""  && pathList[1] != ""  && pathList[2] != ""){
           sendGetRequest("/readRecord?org_name="+pathList[0]+"&db_name="+pathList[1]+"&table_name="+pathList[2],showTable);
            // return  {"ajax" : "readRecord" , "table_name" : pathList[2], "db_name" : pathList[1] ,"org_name" : pathList[0] };
        }else{
            history.replaceState(null,null,"/v1");
            sendGetRequest("/getOrgName", showAllOrg);
        }
        
    }else{
         history.replaceState(null,null,"/v1");
         sendGetRequest("/getOrgName", showAllOrg);
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
        niceURL();
        callback(result);
    }});
    
}
 function urlAdd(pathname,purpose){
    var e = {
         "databs"      : "database",
         "org_box"     : "org",
         "databs tble" : "table"
     }
     var path = location.pathname;
        if (path.endsWith("/")){
            path = path +pathname ; 
        } else {
            path = path +"/"+pathname;
        }
        history.pushState(null,null,path);
        console.log("done");
        niceURL();
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

function getColumnDetail(){
    try{
        console.log(" s a d d s a d s a ");
   var columnName = document.getElementsByClassName("columnName");
   var type  = document.getElementsByClassName("type");
   var defaultValue = document.getElementsByClassName("defaultValue");
   var isNull = document.getElementsByClassName("isNull");
    var list = [];
    for(let i = 0;i < columnName.length;i++){
      var tmpObj = {};
      tmpObj.type = type[i].value; 
      tmpObj.column = columnName[i].value;
      tmpObj.default = defaultValue[i].value;
      if(isNull[i].checked == true){
         tmpObj.isNull = false;
       }else{
         tmpObj.isNull = true;
       }
      list.push(tmpObj);
    }
        return list;        
    }catch(err){
        return [];
    }
}

// function getColumnNames(){
//   var columnNames = document.getElementsByClassName("th");
//   var list = [];
//   for(let i = 0;i<columnNames.length;i++){
//      list.push({"column" : columnNames[0].innerText});
//   }
//   return list;
// } 

 function sort(isAsc){
     if(isAsc == true){
        $("#table").tablesorter(
         { sortForce : [[0,0]]  } 
        );
     }else{
         $("#table").tablesorter(
           {sortForce : [[0,1]] }
        );
      }
  }
      
function getColumnNames(){
    var column = document.getElementsByClassName("th");
    var list = [];
    for(let i = 0;i < column.length;i++){
      if(column[i].getAttribute("type") != "bigserial"){
          var tmpObj = {};
           tmpObj.type = column[i].getAttribute("type");
          tmpObj.columnName = column[i].innerText;
          list.push(tmpObj);
      }
      
      
    }  
  return list;
}

function addColumn(){
   var val = document.getElementsByClassName("row_inp");
   var tmpObj = {};
    for(let element of val){
       var column= element.parentNode.innerText;
       tmpObj[column.substring(0,column.indexOf(":"))] = element.value;
    }
  return tmpObj;
}