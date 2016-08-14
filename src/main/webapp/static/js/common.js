function checkboxall( Allname,name) {  
    var ischecked = document.getElementById(Allname).checked;  
    if(ischecked) {  
        checkallbox(name);  
    }else {  
        discheckallbox(name);  
    }  
}  
//选中全部复选框  
function checkallbox(name) {  
    var boxarray = document.getElementsByName(name);  
    for(var i = 0; i < boxarray.length; i++) {  
        boxarray[i].checked = true;  
    }  
}  
//取消选中全部复选框  
function discheckallbox(name) {  
    var boxarray = document.getElementsByName(name);  
    for(var i = 0; i < boxarray.length; i++) {  
        boxarray[i].checked = false;  
    }  
}  
  
//点击某个复选框，如果所有复选框都选中，“全选/全不选”复选框也选中  
//否则如果所有复选框都取消选中，“全选/全不选”复选框也取消选中  
function checkonebox(Allname,name) {  
    if(isallchecked(name)) {  
        document.getElementById(Allname).checked = true;  
    }  
    if(isalldischecked(name)) {  
        document.getElementById(Allname).checked = false;  
    }  
}  
  
//是否全部选中  
function isallchecked(name) {  
    var boxarray = document.getElementsByName(name);  
    for(var i = 0; i < boxarray.length; i++) {  
        if(!boxarray[i].checked) {  
            return false;  
        }  
    }  
    return true;  
}  
//是否全部没有选中  
function isalldischecked(name) {  
    var boxarray = document.getElementsByName(name);  
    for(var i = 0; i < boxarray.length; i++) {  
        if(boxarray[i].checked) {  
            return false;  
        }  
    }  
    return true;  
}  
  
//得到选中项的值的集合，结果为“1|小明,2|小王,3|小李”的形式  
function getallcheckedvalue(name) {  
    var boxvalues = "";  
    var boxarray = document.getElementsByName(name);  
    for(var i = 0; i < boxarray.length; i++) {  
        if(boxarray[i].checked) {  
            var boxvalue = boxarray[i].value;  
            if(boxvalues == "") {  
                boxvalues = boxvalue;  
            }else {  
                boxvalues = boxvalues + "," + boxvalue;  
            }  
        }  
    }  
    return boxvalues;  
} 

function getallcheckedvalue_old(name) {  
    var boxvalues = "";  
    var boxarray = document.getElementsByName(name);  
    for(var i = 0; i < boxarray.length; i++) {  
        if(boxarray[i].checked) {  
            var boxvalue = boxarray[i].value;  
            if(boxvalues == "") {  
                boxvalues = boxvalue;  
            }else {  
                boxvalues = boxvalues + "," + boxvalue;  
            }  
        }  
    }  
    return boxvalues;  
} 
//如果只需要得到其中选中项的id值的集合，方法如下，得到的值为（1,2,3,…）  
function getIds() {  
        var boxvalues = getallcheckedvalue();  
        var boxvaluesArray = boxvalues.split(",");  
        var ids = "";  
        for(var i = 0; i < boxvaluesArray.length; i++) {  
            var boxvalue = boxvaluesArray[i];  
            var boxvalueArray = boxvalue.split("|");  
            var id = boxvalueArray[0];  
            var username = boxvalueArray[1];  
            if(ids == "") {  
                ids = id;  
            }else {  
                ids = ids + "," + id;  
            }  
        }  
        return ids;  
}  


//打开标签页
function openTab(tabId,title,url,iconClass){
	var e =$('#centerTab').tabs('exists', title);
	if(e) {
		// 已存在则被选中
		$("#centerTab").tabs("select", title);
	}
	if($("#"+tabId).html()==null){
		var name = 'iframe_'+tabId; 
		$('#centerTab').tabs('add',{
			title:title,
			closable:true,
			cache:false,
			iconCls:iconClass,
			id:tabId,
			style:{padding:"2px"},
			content:  '<iframe name="'+name+'"id="'+tabId+'"src="'+url+'" width="100%" height="100%" frameborder="0" scrolling="auto" ></iframe>' 
		});
	}	
}
function refreshTab(){
	window.location.reload();
}