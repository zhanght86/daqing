<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);	
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>存储平台管理系统</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/main.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/easyui.css">  
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/icon.css"> 
<script type="text/javascript" src="<%=basePath%>/static/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/jquery.easyui.min.js"></script>  
<script type="text/javascript" src="<%=basePath%>/static/js/easyui-lang-zh_CN.js"></script>  
<script type="text/javascript" src="<%=basePath%>/static/js/moment.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/common.js"></script> 
<script type="text/javascript" src="<%=basePath%>/static/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/datagrid-detailview.js"></script>