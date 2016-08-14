$(function() {
	getGoods();
	fnaddGoodsDialog();
	fnGoods();
	fn();
	fnaddGoodss();
	fnaddGoodsDialo();
});

function getGoods() {
	$("#dg").datagrid({
		url : "person.json",
		width : "100%", // 表格宽度
		height : "auto",
		pageSize : 3,// 默认选择的分页是每页5行数据
		pageList : [ 3, 6, 9 ],// 可以选择的分页集合
		nowrap : true,// 设置为true，当数据长度超出列宽时将会自动截取
		striped : true,// 设置为true将交替显示行背景。
		collapsible : false,// 显示可折叠按钮
		loadMsg : '数据装载中......',
		fitColumns : true,// 允许表格自动缩放，以适应父容器
		pagination : true,// 分页
		rownumbers : true,
		remoteSort : false, // 允许服务器排序
		sortName : "id",
		sortOrder : "desc",
		singleSelect : false, // 设置复选框属性
		
		toolbar : [ {// 正上方工具栏
			text : '添加商铺',
			iconCls : 'icon-add',
			handler : function() {
				//点击此图表调用的方法
				openpersonAdd_dialog();	
				//alert("dfgh");
			}
		},'-',{
			text : '批量导出',
			iconCls : 'icon-cancel',
			handler : function() {
				alert("导出");
			}
				
			}, '-' , {
				text : '下载模板',
				iconCls : 'icon-cancel',
				handler : function() {
					alert("下载");
				}
			}, '-', {
				text : '导入数据',
				iconCls : 'icon-cancel',
				handler : function() {
					alert("导入");
				}
		} ]
	});
}
/**
 * 设置操作列的信息 参数说明 value 这个可以不管，
 * 但是要使用后面 row 和index 这个参数是必须的 row 当前行的数据 index
 * 当前行的索引 从0 开始
 */
function optFormater(value, row, index) {
	var bookId = row.id;
	var opt = '';
	var edit = '<a href="#" onclick="fnfindGoods(' + bookId
			+ ')">编辑</a> | ';
	
	var del = '<a href="#" onclick="fndelGoods(' + bookId + ')">删除</a>';
	return opt  + edit + del;
}

/**
 * 设置操作列的查看详情信息
 */
function optDetailFormater(value, row, index){
	var bookId = row.id;
	var opt = '';
	var detail='<a href="#" >查看商品详情</a>';
	return opt + detail;
}
/**
 * 转化性别
 * 
 * @param value
 * @param row
 * @param index
 */
function formatSex(value, row, index) {
	var ob = row.sex; // 得到当前列的时间值
	if (ob == "1") {
		return "男";
	} else if (ob == "0") {
		return "女";
	}
}
/**
 * 添加商铺信息
*/
function fnaddGoods(){
	var str = $("#addfm").serialize();
	alert(str);
	$.ajax({
		url:'addGoodsAction',
		type:'post',
		cache:false,
		data:str,
		success:function(data){
			if(data.count==1){
				 alert("添加成功!");
				$("#dlg").dialog("close");
			}
		}
	});
}
/**
*初始化添加的对话框
*/
function fnaddGoodsDialog(){
	$("#dlg").dialog({    
	    title: '添加商铺信息',    
	    width: 400,    
	    height: 300,    
	    closed: true,    
	    width : "600", // 表格宽度
		height : "305"
	});    
}
/**
 * 打开对话框
 */
function openpersonAdd_dialog(){
	$("#dlg").dialog("open");
}

/**
 * 删除商品
 */
function fndelGoods(id){
	alert(id);
	$.messager.confirm('删除提示', '你确定永久删除该数据吗?', function(rs) {
		if (rs) {
			//执行的是删除的方法
			$.ajax({
				url:'delGoodsAction.action',
				type:'post',
				data:{"pid":id},
				success:function(data){
				if(data.count==1){
					alert("删除成功！");
				}
				}
			});
		}
	});
}

/**
 * 初始化修改商品的对话框
 */
function fnupdate(){
	var str = $("#fmupdate").serialize();
	$.ajax({
		type:'post',
		url:'PersonInfo.json',
		data:str,
		success:function(data){
			if(data.count==1){
				alert("修改成功！");
				$("#updateDialog").dialog("close");
			}
		}
	});
}
/**
 * 查询单个对象
 * @param pid
 */
function fnfind(pid){
	$.ajax({
		type:"post",
		url:'PersonInfo.json',
		data:{"sid":pid},
		success:function(data){     
			alert(data);     
			$("input[name='caseId']").val(data.caseId);
			$("input[name='caseName']").val(data.caseName);
			$("input[name='caseSuspect']").val(data.caseSuspect);
			$("input[name='caseDeptNo']").val(data.caseDeptNo);
			$("input[name='caseDate']").val(data.caseDate);
			$("input[name='caseEndDate']").val(data.caseEndDate);
			$("input[name='caseStatus']").val(data.caseStatus);
			$("#updateDialog").dialog("open");
		}
	});
}
function fnGoods(){
	$("#updateDialog").dialog({    
	    title: '修改商品信息',    
	    width: 600,    
	    height: 400,    
	    closed: true,    
	    width : "600", // 表格宽度
		height : "305"
	});
}
function fnupdateGoods(){
	var str = $("#fmupdate").serialize();
	$.ajax({
		type:'post',
		url:'PersonInfo.json',
		data:str,
		success:function(data){
			if(data.count==1){
				alert("修改成功！");
				$("#updateDialog").dialog("close");
			}
		}
	});
}


