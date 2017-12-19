function logout(){
    sendGetRequest("/logout",log);
}

function log(res){
    if(res == "Logout Successfully"){
        location.href = "http://orgdat.zcodeusers.com/v1";
    }else{
        document.getElementById("alert").innerText = "Please Try Again";
    }
}

function listToObject(detailArray){
	var renderObj = {};
	var dataList = [];
	for(let i= 0 ;i <detailArray[0].length ;i++){
		let tmp = {};
		tmp.key = detailArray[0][i];
		tmp.type = detailArray[1][i];
		tmp.defalt = detailArray[2][i];
		tmp.isNull = detailArray[3][i];
		dataList.push(tmp);
	}
	renderObj.headers = dataList;
	dataList = [];
	for(let i = 4;i < detailArray.length;i++){
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

function getData(){
    var data={};
    var path = location.pathname.substring(4).split("/");
    data.org_name = path[0]; 
    data.db_name = path[1];
    data.table_name = path[2];
    return data;
}
function niceURL(){
    var currentPath = location.pathname; 
    if (currentPath.endsWith("/")){
        currentPath = currentPath.substring(0,currentPath.length-1);
    }
    var pathList = currentPath.substring(4).split("/");
    // console.log(pathList);
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
    var column_name = document.getElementsByClassName("column_name1");
    var opertor = document.getElementsByClassName("opertor1");
    var values = document.getElementsByClassName("update");
    let i = 0;
    var valList = [];
    while(i < column_name.length){
       valList.push({"column" :column_name[i].innerText , "condition" : opertor[i].innerText , "value" : values[i].value});
       i++;
    }
//   var list = [];
//   list.push({"andOr" :document.getElementsByClassName("tbl_con")[0].innerText.trim()});
//   list.push(valList);
  return valList;
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

 function sorting(isAsc){
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
           tmpObj.type = column[i].getAttribute("type").trim();
          tmpObj.columnName = column[i].innerText.trim();
          list.push(tmpObj);
      }
    }  
  return list;
}

function addColumn(){
   var val = document.getElementsByClassName("row_inp");
   var tmpObj = {};
    for(let element of val){
       var column= element.parentNode.innerText.trim();
       tmpObj[column.substring(0,column.indexOf(":")).trim()] = element.value.trim();
    }
  return tmpObj;
}


function getRecordDetail(element){
   var records = element.querySelectorAll("td");
   var columnName = getColumnNames();
   console.log(JSON.stringify(columnName))
    var list  = [];
    for(let i = 0 ;i < columnName.length;i++){
       var tmpObj = {};
       tmpObj.columnName = columnName[i]["columnName"];
       tmpObj.value = records[i+2].innerText;
       list.push(tmpObj);
    }
  return list;
}

function getValue(classname){
    var values = document.querySelectorAll('.'+classname+':checked');
    var list = [];
    for(eachValue of values){
      list.push({"column" :"roll_no","condition":"=","value": $(eachValue).parent().next().text().trim()});
    }
   return list;
 }

function deleteRow(element){
   var roll_no = element.parentNode.querySelector("td").innerText;
   return [{"column" : "roll_no", "condition" : "=" , "value" : roll_no}];
}

function getColDeets(element){
    var type = element.getAttribute("type");
    var name = element.innerText;
    var defaultVal = element.getAttribute("defaultVal");
    var isNull = element.getAttribute("isNull");
    var tmpObj = {"type" : type , "column" : name , "defaultVal" : defaultVal , "isNull" : isNull}
    return tmpObj;
}

function  getPathConfig(element){
   var name = element.innerText;
   var path = location.pathname.substring(1)
   if(path.endsWith("/") == true){
       path = path.substring(0,path.length-1);
   }
   var path = path.substring(1).split("/");
   var count = 0;
   var  tmpObj = {};
   
   switch(path.length){
      case 3 : 
         tmpObj.db_name  = path[count+2]; 
         if(path.length == 3){
             tmpObj.table_name = name;
          }
      case 2 :  
         tmpObj.org_name  = path[count+1];
         if(path.length == 2){
             tmpObj.db_name = name;
          }
     case 1 : 
         if(path.length == 1){
            tmpObj.org_name = name;
         }
    }
  return tmpObj;
}


function getSetPath(count , names){ 
   var path = (location.pathname).substring(1).split("/");
   var replacePath = "";
   while(count--){
      replacePath = path[count]+"/"+replacePath;
    }
   replacePath = replacePath+ names;
   return "/"+replacePath;
}

function shareConfig(name){
    var email = document.getElementById("email").value;
    var role = document.getElementById("role").value;
    var a = document.createElement("p")
    a.innerText = name;
    var tmpObj = getPathConfig(a);
    tmpObj.role=role;
    tmpObj.email=email;
    tmpObj.isRole="createRole";
   return tmpObj;
}

function editShare(element,name){
    var elementClass  = element.getAttribute("class");
    var name_mail = element.querySelectorAll(elementClass+" > div > p");
    var mail = name_mail[1].innerText;
    var role = element.querySelector("select");
    var a = document.createElement("p")
    a.innerText = name;
    var tmpObj = getPathConfig(a);
    tmpObj.email = mail;
    tmpObj.role = role;
    tmpObj.isRole = "editRole";
    return tmpObj;  
}

function deleteShare(element,name){
   var tmpObj = editShare(element);
   tmpObj.isRole = "deleteRole";
   return tmpObj;
}

function  rename(name){
    var newName = document.getElementById("rename").value;
    var a = document.createElement("p")
    a.innerText = name;
    var tmpObj = getPathConfig(a);
    tmpObj.rename = newName;
    return tmpObj;
}


function getMemberDetail(){
    var org_name = document.getElementsByClassName("share_org")[0].innerText;
   var name = document.getElementById("inv_name").value;
   var email = document.getElementById("inv_mail").value;
   var password = document.getElementById("inv_password").value;
   let nameRex = /^[a-zA-Z]{6,255}$/g;
  let emailRex = /^[a-z][a-z0-9]{5,30}$/g;
  let passRxp = /^.{6,255}$/g
  if(nameRex.test(name)){
     if(emailRex.test(email)){
        if(passRxp.test(password)){
            var a = {};
            a.name = name;
            a.email = email+org_name;
            a.password = password;
            a.org_name = org_name.substring(org_name.indexOf("@")+1,org_name.indexOf("."));
             return a;
        }else{
        //   document.getElementById("alert").innerText = "Password is incorrect";
        alert("Password is incorrect");
       }
     }else{
        //  document.getElementById("alert").innerText = "Email is Incorrect"; 
        alert("Email is Incorrect");
      }
  }else{
    //   document.getElementById("alert").innerText = "User Name is Incorrect";
    alert("User Name is Incorrect");
  }
}

function deleteMember(element){
   var email = (element.querySelectorAll("span"))[2].innerText;
   return {"email" : email};
}

function getPassword(){
    var current_Password = document.getElementById("current_pass").value;
    var new_password = document.getElementById("new_password").value;
    var retype_password = document.getElementById("reenter_pass").value;
    return {"newPassword": new_password , "password" : current_password};
}

function getQuestion(){
    var password = document.getElementById("password").value;
    var question = document.getElementById("question").value;
    var answer = document.getElementById("answer").value;
    return {"password" :password , "question" : question, "answer" : answer};
}
