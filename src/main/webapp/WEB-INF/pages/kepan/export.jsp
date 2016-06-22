<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
	$(document).ready(function() {
		$(".click").click(function() {
			window.location.reload(true);
		});
		$(".history").click(function() {
			window.location.href = history.go(-1);
		})
	});
</script>
</head>
<!-- 电子标签位置查看 -->
<body>
	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="<%=basePath%>/to.do?file=index">首页</a></li>
			<li><a href="<%=basePath%>/burn/mergeList.do?volLabel=${volLabel}">导出管理</a></li>
		</ul>
	</div>

	<div class="formbody">
		<div id="usual1" class="usual">
		
			<ul class="toolbar">
				<li class="click"><span><img
						src="<%=basePath%>/static/images/Refresh.gif" /></span>刷新</li>
						
				<li class="history"><span><img
						src="<%=basePath%>/static/images/left.png" /></span>返回</li>
			</ul>
			<form action="<%=basePath%>/burn/mergeList.do">
			<ul class="seachform">
				<li>
					<label>&nbsp;&nbsp;&nbsp;&nbsp;卷标号</label>
					<input name="volLabel" type="text"class="scinput" value="${volLabel }" />
				</li>
					<li>
					<label>&nbsp;&nbsp;&nbsp;&nbsp;任务名称</label>
					<input name="task_name" type="text"class="scinput" value="${task_name}" />
				</li>
				<li>
					<label>&nbsp;</label>
					<input name="" type="submit" class="scbtn" value="查询" />
				</li>
			</ul>
			</form>
		
			<table class="tablelist">
				<thead>
					<tr>
						<th>序号</th>
						<th>卷标号</th>
						<th>已成功个数</th>
						<th>导出状态</th>
						<th>导出目录</th>
						<th>导出描述</th>
						<th>任务名称</th>
						<th>创建时间</th>
						<th>修改时间</th>
						<!-- 
						<th>操作用户</th>
						 -->
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list }" var="map" varStatus="status">
						<tr class="${status.count % 2 == 0 ? '' : 'brown'}">
							<td>${status.count }</td>
							<td>${map.volume_label }</td>
							<td>${map.number_success }</td>
							<td>
								<c:if test="${map.export_state == 1}">下载数据</c:if>
								<c:if test="${map.export_state == 2}">下载成功</c:if>
								<c:if test="${map.export_state == 3}">下载失败</c:if>
								<c:if test="${map.export_state == 4}">导出成功</c:if>
								<c:if test="${map.export_state == 5}">导出失败</c:if>
								<c:if test="${map.export_state == 6}">关闭</c:if>
								<c:if test="${map.export_state == 7}">导出空间不足</c:if>
							</td>
							<td>${map.export_path }</td>
							<td>${map.export_desc}</td>
								<td>${map.task_name}</td>
							<td><fmt:formatDate value="${map.create_time }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${map.update_time }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<!-- <td>${map.c_user}</td>  -->
							<td>
							<a onclick="return confirm('确定要删除么？');" href="<%=basePath %>/burn/deleteExport.do?eid=${map.eid}">删除</a>
							<c:if test="${map.export_state == 1}">
								<c:if test="${volLabel == null or volLabel == ''}">
									<a href="<%=basePath%>/burn/exportCancel.do?eid=${map.eid}" onclick="return confirm('确定要取消么？');">关闭</a>
								</c:if>
								<c:if test="${volLabel != null and volLabel != ''}">
									<a href="<%=basePath%>/burn/exportCancel.do?eid=${map.eid}&volume_label=${volLabel}" onclick="return confirm('确定要取消么？');">关闭</a>
								</c:if>
							</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

</body>
</html>