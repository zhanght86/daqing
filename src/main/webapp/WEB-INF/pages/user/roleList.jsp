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
	
	function updateForm(roleId) {
		$.ajax({
			url: g_base_path + '/role/updateYe.do',
			type: 'get',
			data: "roleId=" + roleId,
			dataType: "html",
			beforeSend: ajaxLoading,
			success: function (data) {
				ajaxLoadEnd();
				$("#update-form").html('');
				$("#update-form").html(data);
				$("#update-form").dialog("open");
			}
		});
	};

	$(document).ready(function() {
		<c:if test="${message ne null}">
			showHintMessage('${message}');
		</c:if>

		$("#addBtn").click(function() {
			$("#dialog-form").dialog("open");
		});

		$('#dialog-form').dialog({
			resizable : true,
			width : 550,
			height : 250,
			closed : true,
			mask : true,
			closable : false,
			iconCls : 'icon-addContent',
			modal : true,
			style : {
				padding : '5px'
			},
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler : function() {
					//点击确定做的方法
					if($("#addRoleForm").form('validate')) {
						$.ajax({
							url: g_base_path + '/role/add.do',
							type: 'post',
							data: $('#addRoleForm').serialize(),
							beforeSend: ajaxLoading,
							success: function (data) {
								ajaxLoadEnd();
								if ("true" == data) {
									showHintMessage('新增角色保存成功！', 1000, function () {
										refreshTab();
									});
									
									$('#dialog-form').dialog('close');
								} else {
									showHintMessage('新增角色保存失败，请重新尝试');
								}
							}
						});
					}
				}
			}, {
				text : '取消',
				iconCls : 'icon-cancel',
				handler : function() {
					$('#dialog-form').dialog('close');
					//点击取消按钮作的方法
					$('#addRoleForm').get(0).reset();
				}
			} ]
		});

		$.extend($.fn.validatebox.defaults.rules, {
			halfEnDigital : {
		        validator: function(value, param){
		            return /^[0-9A-Za-z]+$/.test(value);
		        },
		        message: '请输入半角英文数字'
		    }
		});
		
		$.fn.validatebox.defaults.rules.remote.message = "已经存在，请重新输入";
	});
</script>
</head>
<body>
	<div class="easyui-panel" title="角色列表"
		data-options="fit:true,iconCls:'icon-list'" style="padding: 2px;">
		<div id="update-form" title="&nbsp;修改角色" class="form_1"></div>
		<div id="dialog-form" title="&nbsp;新增角色" class="form_1">
			<p class="validateTips">
				<span class="red_star">*</span>为必添项
			</p>
			<form action="<%=basePath%>/role/add.do" id="addRoleForm">
				<table class="list2">
					<tr>
						<td>角色编码<span class="red_star">*</span></td>
						<td><input type="text" name="roleCode" id="roleCode" maxlength="45" class="easyui-validatebox" 
							data-options="required:true,validType:{halfEnDigital:[],length:[1,45],remote:['<%=basePath%>/role/roleCode/validation.do','roleCode']}" /></td>
						<td>角色名称<span class="red_star">*</span></td>
						<td><input type="text" name="roleName" id="" maxlength="50" class="easyui-validatebox" 
							data-options="required:true,validType:{length:[1,50],remote:['<%=basePath%>/role/roleName/validation.do','roleName']}" /></td>
					</tr>
					<tr>
						<td>状态</td>
						<td><select name="deleted" id="deleted" required="true">
								<option value="0" selected="selected">正常</option>
								<option value="1">撤销</option>
						</select></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>备注</td>
						<td colspan="3"><input type="text" name="remark" id="remark" maxlength="400" style="width:250px;"  data-options="validType:'length[0,400]'" /></td>
					</tr>
				</table>
			</form>
		</div>
		<div id="top_body">
			<div id="hintMessage"></div>
			<div class="button_bar">
				<a href="javascript:void(0)" id="addBtn" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增</a>
				<a href="javascript:refreshTab();" id="refreshBtn" class="easyui-linkbutton" plain="true" iconCls="icon-refresh">刷新</a>
			</div>
		</div>

		<table class="list">
			<tr>
				<th>&nbsp;</th>
				<th>角色编码</th>
				<th>角色名称</th>
				<th width="20%">备注</th>
				<th width="10%">操作</th>
			</tr>

			<c:forEach items="${roles }" var="item" varStatus="status">
				<tr class="${status.index % 2 == 0 ? '' : 'brown' }">
					<td>${status.index+1}</td>
					<td>${item.roleCode }</td>
					<td>${item.roleName }</td>
					<td>${item.remark }</td>
					<td>
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'" onclick="updateForm(${item.roleId});">修改</a>
						<a href="<%=basePath%>/role/rolePermission.do?roleId=${item.roleId }" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-qx'">权限</a>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty roles }">
				<tr>
					<td colspan="6">没有查询到任何角色信息</td>
				</tr>
			</c:if>
		</table>
	</div>
</body>
</html>