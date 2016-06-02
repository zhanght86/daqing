<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ path;
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBliC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link href="<%=basePath%>/static/css/style.css" rel="stylesheet"
	type="text/css" />
<link href="<%=basePath%>/static/css/select.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="<%=basePath%>/static/js/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/static/js/jquery.idTabs.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/static/js/select-ui.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/static/editor/kindeditor.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/static/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		KE.show({
			id : 'remarks',
		});
		$(".select1").uedSelect({

		});
	});
</script>
</head>
<body>
	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li>
				<a href="<%=basePath%>/to.do?file=index">首页</a>
			</li>
			<%-- <li>
				<a href="<%=basePath%>/dData/trst.do">补录数据</a>
			</li> --%>
		</ul>
	</div>

	<div class="formbody">

		<div class="formtitle">
			<span>补录数据</span>
		</div>



		<form action="<%=basePath%>/standingbook/insert.do">

			<table  >
			<!-- 	<tr  >
					<td><label>编 &nbsp;&nbsp;&nbsp;&nbsp;号 &nbsp;</label></td>
					<td><input name="sid" type="text" class="dfinput" value="" />
					</td>
				</tr> -->
				
				<tr  >
					<td><label>卷 标 号 &nbsp;</label></td>
					<td><input name="volume_label" type="text" class="dfinput"
						value="" /></td>
				</tr>
				
				<tr>
					<td><label>数据类型 &nbsp;</label></td>
					<td><select id="data_type" name="data_type" class="select1">
							<option value="R">原始数据清单</option>
							<option value="T">二维数据</option>
							<option value="D">三维数据</option>
							<option value="M">中间数据</option>

					</select></td>
				</tr>
			
				<tr  >
					<td><label>工 区 &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
					<td><input name="work_area" type="text" class="dfinput"
						value="" /></td>
				</tr>
				
				<tr  >
					<td><label>单 位 &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
					<td><input name="construction_unit" type="text"
						class="dfinput" value="" /></td>
				</tr>
				
				<tr >
					<td><label>日 期 &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
					<td><input name="construction_year" type="text"
						class="dfinput" value="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', errDealMode:'0'})"/></td>
				</tr>
			
				<tr >
					<td><label>状 态 &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
					<td><select id="states" name="states" class="select1">
							<option value="1">有效</option>
							<option value="0">无效</option>
					</select></td>
				</tr>
				<tr  >
					<td><label>数据大小 &nbsp;</label></td>
					<td><input name="data_quantity" type="text" class="dfinput"
						value="" /></td>
				</tr>
				
				<tr  >
					<td><label>刻盘数量 &nbsp;</label></td>
					<td><input name="burn_count" type="text" class="dfinput"
						value="" /></td>
				</tr>
				
				 <tr  >
					<td><label>刻盘结束时间 &nbsp;</label></td>
					<td><input name="update_time" type="text" class="dfinput"
						value="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', errDealMode:'0'})"/></td>
				</tr> 
			




				<tr height=30>
					<td><label>&nbsp;</label></td>
					<td><input type="submit" class="btn" value="保存" /></td>
				</tr>
			</table>

		</form>
	</div>
</body>
</html>