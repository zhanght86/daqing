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
		$("#grid-tb :input").val("");
	}
	
	//重置密码
	function resetPassword(userId) {
		
		$.ajax({
			url: g_base_path + '/user/resetPassword.do',
			type: 'post',
			data: "userId=" + userId,
			success: function (data) {
				ajaxLoadEnd();
				if (data == "true") {
					showHintMessage('初始密码成功');
				} else {
					showHintMessage('初始密码失败，请重新尝试');
				}
			}
		});
		alert(g_base_path);
	}
	
	//进入添加页面
	function saveForm(userId){
	    $('#updateUser-dialog').dialog({
	        title: '添加用户信息',
	        closed: false,
	        cache: false,
	        href: g_base_path +'/user/intoEdit.do?userId='+userId+'&savetype='+'add',
	        modal: true
	    });
	}
	
	//进入编辑页面
	function updateForm(userId){
	    $('#updateUser-dialog').dialog({
	        title: '修改用户信息',
	        closed: false,
	        cache: false,
	        href: g_base_path +'/user/intoEdit.do?userId='+userId+'&savetype='+'update',
	        modal: true
	    });
	}
	
	//保存
	function user_save(userId){
		  var url = g_base_path+'/user/update.do';
		  $(userId).form('submit', {
				 url:url, 
				 onSubmit : function() {
		             return $(this).form('validate');
		         },
		         success : function(result) {
	                 alertMsg(result);
	                 $('#updateUser-dialog').dialog('close');
	                 $('#list-grid').datagrid('reload');
		         }
		     });
	  }
	
	//重置
	function user_reset(formId){
		$(formId).form('reset');
  	}
	
	$(document).ready(function() {
		$('#list-grid').datagrid({
	    	width: 'auto',
	        height:'auto',
	        title: '用户列表',
	        iconCls: 'icon-search',
	        rownumbers: true,
	        singleSelect: false,
	        pagination:true,
	        pageNumber:1,
	        pageSize:10,
	        url: g_base_path+'/user/list.do',
	        method: 'post',
	        toolbar: '#grid-tb',
	        checkOnSelect: false,
	        columns: [[
	            {field: 'userName', title: '用户名称', align: 'left'},
	            {field: 'loginName', title: '登录名称', align: 'left'},
	            {field: 'orgName', title: '所属机构', align: 'left'},
	            {field: 'phone', title: '电话号码', align: 'left'},
	            {field: 'email', title: '邮箱地址', align: 'left'},
	            {field:'action',title:'操作',width:180,formatter:function(val,row){
	                var str ='<a href="javascript:void(0)" onclick="updateForm('+row.userId+');">修改</a>&nbsp;'
	                	+'<a href="javascript:void(0)" onclick="resetPassword('+row.userId+');">初始密码</a>&nbsp;'
	                	+'<a href="<%=basePath%>/user/userRole.do?userId='+row.userId+'">角色</a>';
	                return str;
	            }}
	        ]]
	    });
	});
</script>
</head>
<body>
	<!-- 用户列表 -->
	<table id="list-grid" style="padding: 2px;height:90%"></table>
	<!-- 列表查询条件 -->
	<div id="grid-tb" style="padding:3px">
	    <table class="list">
			<tr>	
				<td>所属机构:
					<select class="easyui-combobox" name="dptCode" style="width:120px">
						<option value="" selected="selected"></option>
						<c:forEach items="${orgList}" var="item">
							<option value="${item.dptCode}">${item.dptName}</option>
						</c:forEach>
					</select>
				</td>
		        <td>
		            <a href="#" class="easyui-linkbutton" onclick="doSearch()" iconCls="icon-search" style="width:80px" plain="true">查询</a>
					<a href="#" class="easyui-linkbutton" onclick="doClear()" iconCls="icon-clear" style="width:80px" plain="true">重置</a> 
					<a href="javascript:saveForm(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
		        </td>
	        </tr>
	    </table>
	</div>
	<!-- 修改 -->
	<div id="updateUser-dialog" class="easyui-dialog" title="" style="width:600px;height:350px;padding:10px"
	     data-options="iconCls: 'icon-list',closed:true">
	</div>
</body>
</html>