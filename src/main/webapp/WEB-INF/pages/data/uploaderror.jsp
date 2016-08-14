<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>405</title>
<link href="<%=basePath %>/static/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath %>/static/js/jquery.js"></script>

<script language="javascript">
	$(function() {
		$('.inforesult').css({
			'position' : 'absolute',
			'left' : ($(window).width() - 490) / 2
		});
		$(window).resize(function() {
			$('.inforesult').css({
				'position' : 'absolute',
				'left' : ($(window).width() - 490) / 2
			});
		})
	});
</script>


</head>


<body style="background: #edf6fa;">

	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="<%=basePath %>/to.do?file=index">首页</a></li>
			<li><a href="#">上传数据失败</a></li>
		</ul>
	</div>

	<div class="inforesult">
		<h2>上传数据失败</h2>
		<p>${info }</p>
		<div class="reindex">
			<a href="<%=basePath %>/to.do?file=index">返回首页</a>
		</div>
		<div class="reindex">
			<a href="<%=basePath %>/index.do" target="_parent">返回登录</a>
		</div>
	</div>
</body>
</html>