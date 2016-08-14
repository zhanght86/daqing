<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path;
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=basePath%>/static/css/style.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>/static/css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/jquery.idTabs.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/select-ui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/editor/kindeditor.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
});
function checkUserName(obj) {
	if(obj.value == '') {
		alert("用户名不能为空");
		obj.focus();
		return;
	}
	$.ajax({
		url:"<%=basePath%>/user/checkUserName.do?username="+obj.value,
		async : false,
		success : function(data) {
			if(data.result) {
				obj.focus();
				alert("重复的用户名, 用户名重新输入");
			}
		}
	});
}
</script>
</head>
	<body>
	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="<%=basePath%>/to.do?file=index">首页</a></li>
			<li><a href="<%=basePath%>/rawData/list.do">新增用户</a></li>
		</ul>
	</div>

	<div class="formbody">

		<div class="formtitle">
			<span>基本信息</span>
		</div>

		<div class="formtext"><b>${msg }</b></div>

		<form action="<%=basePath%>/user/add.do">
			
			<ul class="forminfo">
				<li>
					<label>登录名</label>
					<input name="userName" type="text" class="dfinput" onblur="checkUserName(this)" value="${userName }" /><i>登录名不能超过30个字符</i>
				</li>
				<li>
					<label>昵称</label>
					<input name="nickName" type="text" class="dfinput" value="${nickName }" /><i>昵称不能超过30个字符</i>
				</li>
				<li>
					<label>密码</label>
					<input name="password" type="password" class="dfinput" />
				</li>
				<li>
					<label>确认密码</label>
					<input name="password_confirm" type="password" class="dfinput" />
				</li>
				<li>
					<label>&nbsp;</label>
					<input name="" type="submit" class="btn" value="确认保存" />
				</li>
			</ul>
		</form>

	</div>


</body>
</html>