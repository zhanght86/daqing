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
	$(".select1").uedSelect({
		width : 100			  
	});
});
</script>
</head>
	<body>
		<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="<%=basePath%>/to.do?file=index">首页</a></li>
			<li><a href="<%=basePath%>/rawData/list.do">更新用户信息</a></li>
		</ul>
	</div>

	<div class="formbody">

		<div class="formtitle">
			<span>基本信息</span>
		</div>

		<div class="formtext"><b>${msg }</b></div>

		<form action="<%=basePath%>/user/update.do">
			<ul class="forminfo">
				<li>
					<label>登录名</label>
					<input name="userName" type="text" class="dfinput" value="${user.user_name }" readonly="readonly" /><i>登录名不能超过30个字符</i>
				</li>
				<li>
					<label>昵称</label>
					<input name="nickName" type="text" class="dfinput" value="${nick_name }" /><i>昵称不能超过30个字符</i>
				</li>
				<li>
					<label>状态</label>
					<select class="select1" name="status">
							<option value="1" <c:if test="${user.status == 1 }" >selected='selected'</c:if> >有效</option>
							<option value="0" <c:if test="${user.status == 0 }" >selected='selected'</c:if> >无效</option>
					</select>
				</li>
				<li>
					<label>&nbsp;</label>
					<input name="" type="submit" class="btn" value="确认修改" />
				</li>
			</ul>
		</form>

	</div>


</body>
</html>