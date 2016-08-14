<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);	
%>
<div id="northDiv">
	<a href="javascript:void(0);"><img alt="logo" src="<%=basePath%>/static/images/logo.png"></a>
	<div class="r person_info">
		<img alt="role" src="<%=basePath%>/static/images/role.png">&nbsp;<span>你好, <shiro:principal /></span>&nbsp;<span>|</span>&nbsp;<span>${LoginUser.userName}</span>
		&nbsp;<a href="javascript:void(0);" id="change_password_link">修改密码</a>
		<shiro:authenticated>
		&nbsp;<a href="<%=basePath%>/logout.do" id="logout_link"><img alt="loginout" src="<%=basePath%>/static/images/loginout.png" style="">&nbsp;退出</a>
		</shiro:authenticated>
	</div>
	<div id="changePassword-dialog-form"  title="&nbsp;修改密码" class="form_1">
		<p class="validateTips">
			<span class="red_star">*</span>为必添项
		</p>
		<form action="<%=basePath%>/user/updatePassword.do" id="changePasswordForm">
			<table class="list2">
				<tr>
					<td>原密码<span class="red_star">*</span></td>
					<td colspan="3">
						<input type="password" name="oldPassword" id="oldPassword" maxlength="45" 
						class="easyui-numberbox easyui-textbox" data-options="missingMessage:'请输入原密码',required:true,validType:'length[1,45]'"/>
					</td>
				</tr>
				<tr>
					<td>新密码<span class="red_star">*</span></td>
					<td><input type="password" name="newPassword" id="newPassword" maxlength="32" class="easyui-numberbox easyui-textbox"
						data-options="missingMessage:'请输入新密码',required:true,validType:'length[6,32]'" /></td>
					<td>确认密码<span class="red_star">*</span></td>
					<td><input type="password" name="retypeNewPassword" id="retypeNewPassword" maxlength="32" class="easyui-numberbox easyui-textbox" 
						data-options="missingMessage:'请再次输入密码',required:true,validType:{length:[6,32],equals:['#newPassword']}" /></td>
				</tr>
			</table>
		</form>
	</div>
	
	<script type="text/javascript">
	$(document).ready(function() {
		$.extend($.fn.validatebox.defaults.rules, {
		    equals: {
		        validator: function(value,param){
		            return value == $(param[0]).val();
		        },
		        message: '确认密码不正确，请重新输入'
		    }
		});
		
		$("#change_password_link").click(function() {
			$("#changePassword-dialog-form").dialog("open");
		});
		
		$('#changePassword-dialog-form').dialog({
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
					if($("#changePasswordForm").form('validate')) {
						$.ajax({
							url: $('#changePasswordForm').attr('action'),
							type: 'post',
							data: $('#changePasswordForm').serialize(),
							beforeSend: ajaxLoading,
							success: function (data) {
								ajaxLoadEnd();
								if ("true" == data) {
									showHintMessage('修改密码成功，请重新登录！', 1000, function () {
										window.location.href = $('#logout_link').attr('href');
									});
									
									$('#changePassword-dialog-form').dialog('close');
								} else {
									showHintMessage('密码不正确，修改密码失败');
								}
							}
						});
					}
				}
			}, {
				text : '取消',
				iconCls : 'icon-cancel',
				handler : function() {
					$('#changePassword-dialog-form').dialog('close');
				}
			} ]
		});
	});
	</script>
</div>