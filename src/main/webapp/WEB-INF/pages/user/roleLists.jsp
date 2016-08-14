<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
<head>
<c:import url="../include/headers.jsp"></c:import>

<script type="text/javascript">
	var g_base_path = '<%=basePath%>';

	
	//重置
	function role_reset(roleId){
		$(roleId).form('reset');
  	}
	$(document).ready(function() {
		$('#list-grid').datagrid({
	    	width: 'auto',
	        height:'auto',
	        title: '角色列表',
	        iconCls: 'icon-search',
	        rownumbers: true,
	        singleSelect: false,
	        pagination:true,
	        pageNumber:1,
	        pageSize:10,
	        url: g_base_path+'/role/list.do',
	        method: 'post',
	        toolbar: '#grid-tb',
	        checkOnSelect: false,
	        columns: [[
	             {field: 'roleId', title: '角色id', align: 'left'},
	            {field: 'roleCode', title: '角色编码', align: 'left'},
	            {field: 'roleName', title: '角色名称', align: 'left'},
	            {field: 'deleted', title: '状态', align: 'left',formatter:function(val, row, index){
   	                return val==0?'正常':'撤销';}},
	            {field: 'remark', title: '备注', align: 'left'},
	            {field:'action',title:'操作',width:180,formatter:function(val,row){
	                var str ='<a href="javascript:void(0)" onclick="updateForm('+'\''+row.roleId+'\''+');">修改</a>&nbsp;'
	                	+'<a href="<%=basePath%>/role/rolePermission.do?roleId='+row.roleId+'">权限</a>';
	                return str;
	            }}
	        ]]
	    });
	});

	//清空
	function doClear(){
		resetClear('grid-tb');
	}
	//进入添加页面
	function saveForm(roleId){
	    $('#updaterole-dialog').dialog({
	        title: '添加角色信息',
	        closed: false,
	        cache: false,
	        href: g_base_path +'/role/intoEdit.do?roleId='+roleId+'&savetype='+'add',
	        modal: true
	    });
	}
	//进入编辑页面
	function updateForm(roleId){
	    $('#updaterole-dialog').dialog({
	        title: '修改角色信息',
	        closed: false,
	        cache: false,
	        href: g_base_path +'/role/intoEdit.do?roleId='+roleId+'&savetype='+'update',
	        modal: true
	    });
	}
	//保存
	function role_save(roleId){
		  var url = g_base_path+'/role/update.do';
		  $(roleId).form('submit', {
				 url:url, 
				 onSubmit : function() {
		             return $(this).form('validate');
		         },
		         success : function(result) {
	                 alertMsg(result);
	                 $('#updaterole-dialog').dialog('close');
	                 $('#list-grid').datagrid('reload');
		         }
		     });
	  }
	
</script>
</head>
<body>
	<!-- 用户列表 -->
	<table id="list-grid" style="padding: 2px;height:90%"></table>
	<!-- 列表查询条件 -->
	<div id="grid-tb" style="padding:3px">
	    <table class="list">
			 <tr>
				<td>	
					<a href="javascript:saveForm(0);" plain="true" class="easyui-linkbutton" iconCls="icon-add">新增</a>
					<a href="javascript:refreshTab();" id="refreshBtn" class="easyui-linkbutton" plain="true" iconCls="icon-refresh">刷新</a>
				</td>
	        </tr> 
	    </table>
	</div>
	<!-- 修改 -->
	<div id="updaterole-dialog" class="easyui-dialog" title="" style="width:600px;height:350px;padding:10px"
	     data-options="iconCls: 'icon-list',closed:true">
	</div>
</body>
</html>