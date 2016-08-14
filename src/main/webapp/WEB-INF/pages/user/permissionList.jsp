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
	
	//查询
	function doSearch(){
		var params = {};
		$('#grid-tb').find(":input").each(function () {
            var $this = $(this);
            if ($this.val()) {
                if ($this.is(':checked')) {
                    params[$this.attr("name")] = $this.is(':checked');
                } else {
                    params[$this.attr("name")] = $this.val();
                }
            }
        });
        $('#list-grid').datagrid( 'load', params);
	}
	
	//清空
	function doClear(){
		resetClear("grid-tb");
	}
	//进入添加页面
	function saveForm(permissionId){
		$('#updatePermissioninfo-dialog').dialog({
			title: '添加权限信息',
	        closed: false,
	        cache: false,
	        href: g_base_path +'/permission/intoEdit.do?permissionId='+permissionId+'&savetype='+'add',
	        modal: true
		});
	}
	//进入修改页面
	function updateForm(permissionId) {
		 $('#updatePermissioninfo-dialog').dialog({
		       title: '修改权限信息',
		       closed: false,
		       cache: false,
		       href: g_base_path +'/permission/intoEdit.do?permissionId='+permissionId+'&savetype='+'update',
		       modal: true
		 });
	}
	
	//保存
	function permissioninfo_save(permissionId){
		var url=g_base_path+"/permission/update.do";
		$(permissionId).form('submit',{
			url:url,
			onSubmit:function(){
				return $(this).form('validate');
			},
			success : function(result) {
                alertMsg(result);
                $('#updatePermissioninfo-dialog').dialog('close');
                $('#list-grid').datagrid('reload');
	         }
		});
	}
	
	//重置
	function permissioninfo_reset(permissionId){
		$(permissionId).form('reset');
  	}
	
	$(document).ready(function() {
		$('#list-grid').datagrid({
	    	width: 'auto',
	        height:'auto',
	        title: '权限列表',
	        iconCls: 'icon-search',
	        rownumbers: true,
	        singleSelect: false,
	        pagination:true,
	        pageNumber:1,
	        pageSize:10,
	        url: g_base_path+'/permission/list.do',
	        method: 'post',
	        toolbar: '#grid-tb',
	        checkOnSelect: false,
	        columns: [[
	            {field: 'permissionCode', title: '权限编码', align: 'left'},
	            {field: 'permissionName', title: '权限名称', align: 'left'},
	            {field: 'permissionUrl', title: '权限路径', align: 'left'},
	            {field: 'permissionType', title: '权限类型', align: 'left',formatter:function(val, row, index){
   	                return val==1?'权限(菜单)':'权限(功能)';}},
	            {field: 'permission', title: '权限描述符', align: 'left'},
	            {field:'action',title:'操作',width:180,formatter:function(val,row){
	                var str ='<a href="javascript:void(0)" onclick="updateForm('+'\''+row.permissionId+'\''+');">修改</a>&nbsp;';
	                return str;
	            }}
	        ]]
	    });
	});
</script>
</head>
<body>
	<!-- 机构列表 -->
	<table id="list-grid" style="padding: 2px;height:90%"></table>
	<!-- 列表查询条件 -->
	<div id="grid-tb" style="padding:3px">
	    <table class="list">
			<tr>	
		        <td>
		            <a href="#" class="easyui-linkbutton" onclick="saveForm(0)" iconCls="icon-add" style="width:80px" plain="true">新增</a>
					<a href="#" class="easyui-linkbutton" onclick="refreshTab()" iconCls="icon-refresh" style="width:80px" plain="true">刷新</a> 
		        </td>
	        </tr>
	    </table>
	</div>
	
	<!-- 修改 -->
	<div id="updatePermissioninfo-dialog" class="easyui-dialog" title="" style="width:600px;height:350px;padding:10px"
	     data-options="iconCls: 'icon-list',closed:true">
	</div>
</body>
</html>