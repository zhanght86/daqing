<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
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
		$(".refresh").click(function() {
			window.location.reload(true);
		});
		
		$(".click").click(function() {
			window.location.href = '<%=basePath %>/user/addInit.do';
		});
	});
	
	function reset(obj) {
		$.ajax({
			url:"<%=basePath%>/user/resetPassword.do?username="+obj,
			async : false,
			success : function(data) {
				if(data.result) {
					alert("重置密码成功!!!");
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
			<li><a href="<%=basePath%>/user/list.do">用户列表</a></li>
		</ul>
	</div>
	<div class="formbody">
		<div id="usual1" class="usual">
		
			<ul class="toolbar">
				<li class="refresh"><span><img src="<%=basePath%>/static/images/Refresh.gif" /></span>刷新</li>
				<li class="click"><span><img src="<%=basePath%>/static/images/t01.png" /></span>新增用户</li>
			</ul>
		
			<ul class="seachform">
			</ul>
		
			<table class="tablelist">
				<thead>
					<tr>
						<th>编号</th>
						<th>登录名</th>
						<th>昵称</th>
						<th>状态</th>
						<th>创建时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${pageInfo.list }" var="page" varStatus="status">
					<tr class="${status.count % 2 == 0 ? '' : 'brown'}">
						<td>${status.count }</td>
						<td>${page.user_name }</td>
						<td>${page.nick_name }</td>
						<td>
							<c:if test="${page.status eq 1 }">有效</c:if>
							<c:if test="${page.status ne 1 }">无效</c:if>
						</td>
						<td><fmt:formatDate value="${page.created_time }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td><a href="<%=basePath%>/user/delete.do?id=${page.id }">删除</a>&nbsp;|&nbsp;<a href="javascript:reset('${page.user_name }')">重置密码</a>&nbsp;|%nbsp;<a href="<%=basePath%>/user/updateInit.do?id=${page.id }">修改</a></td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			
			<div class="pagin">
		    	<div class="message">共<i class="blue">${pageInfo.total }</i>条记录，当前显示第&nbsp;<i class="blue">${pageInfo.pageNum }&nbsp;</i>页</div>
		        <ul class="paginList">
			        <c:if test="${pageInfo.pageNum == 1 }">
			        	<li class="paginItem"><a href="javascript:alert('没有上一页');"><span class="pagepre"></span></a></li>
			        </c:if>
			        <c:if test="${pageInfo.pageNum != 1 }">
			        	<li class="paginItem"><a href="<%=basePath%>/user/list.do?pageNum=${pageInfo.pageNum - 1}&project_name=${project_name }&filing_unit=${filing_unit }&filing_date=${filing_date }"><span class="pagepre"></span></a></li>
			        </c:if>
			        <c:forEach items="${pageInfo.navigatepageNums }" var="num" varStatus="status">
			        	<c:if test="${status.count < 5 }">
			        		<li class="paginItem"><a charset="utf-8" href="<%=basePath%>/user/list.do?pageNum=${num}&project_name=${project_name }&filing_unit=${filing_unit }&filing_date=${filing_date }">${num }</a></li>
			        	</c:if>
			        </c:forEach>
			        <c:if test="${pageInfo.pages == pageInfo.pageNum}">
			       	 	<li class="paginItem"><a href="javascript:alert('没有下一页');"><span class="pagenxt"></span></a></li>
			        </c:if>
			        <c:if test="${pageInfo.pages > pageInfo.pageNum}">
			       	 	<li class="paginItem"><a href="<%=basePath%>/user/list.do?pageNum=${pageInfo.pageNum + 1}&project_name=${project_name }&filing_unit=${filing_unit }&filing_date=${filing_date }"><span class="pagenxt"></span></a></li>
			        </c:if>
		        </ul>
		    </div>
		</div>
	</div>
</body>
</html>