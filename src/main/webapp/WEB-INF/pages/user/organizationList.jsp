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
	function doSearch() {
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
	
	$(document).ready(function() {
		$('#list-grid').datagrid({
	    	width: 'auto',
	        height:'auto',
	        title: '机构列表',
	        iconCls: 'icon-search',
	        rownumbers: true,
	        singleSelect: false,
	        pagination:true,
	        pageNumber:1,
	        pageSize:10,
	        url: g_base_path+'/organization/list.do',
	        method: 'get',
	        toolbar: '#grid-tb',
	        checkOnSelect: false,
	        columns: [[
	            {field: 'dptCode', title: '机构编号', align: 'left',width:'100'},
	            {field: 'dptName', title: '机构名称', align: 'left',width:'100'},
	            {field: 'dptAddress', title: '机构地址', align: 'left',width:'200'},
	            {field: 'parentName', title: '所属机构', align: 'left',width:'100'},
	            {field: 'path', title: '部门路径', align: 'left',width:'200'}
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
				<td>机构编号:</td>
				<td>
					<input type="text" name="dptCode" class="easyui-textbox"></input>
				</td>
								
				<td>机构名称:</td>
				<td>
					<input type="text" name="dptName" class="easyui-textbox"></input>
				</td>
		        <td>
		            <a href="#" class="easyui-linkbutton" onclick="doSearch()" iconCls="icon-search" style="width:80px" plain="true">查询</a>
					<a href="#" class="easyui-linkbutton" onclick="doClear()" iconCls="icon-clear" style="width:80px" plain="true">重置</a> 
		        </td>
	        </tr>
	    </table>
	</div>
</body>
</html>