<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
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
<script type="text/javascript" src="<%=basePath%>/static/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
    KE.show({
        id : 'remarks',
    });
  </script>
</head>
<body>

	
	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="<%=basePath%>/to.do?file=index">首页</a></li>
			<li><a href="<%=basePath%>/rawData/list.do">原始数据</a></li>
		</ul>
	</div>

	<div class="formbody">

		<div class="formtitle">
			<span>原始数据</span>
		</div>
		<form action="<%=basePath%>/data/update.do">
			<input type="hidden" name="dataType" value="R">
			<input type="hidden" name="url" value="rawData/list.do">
			<input type="hidden" name="sid" value="${data.sid}">
			<ul class="forminfo">
				<li style="display:inline;">
					<label>&nbsp;&nbsp;工&nbsp;区&nbsp;&nbsp;&nbsp;&nbsp;</label>
					<input name="work_area" type="text" class="dfinput" value="${data.work_area }" />
				</li>
				&nbsp;&nbsp;&nbsp;
				<li style="display:inline;">
					<label>测线(束)号</label>
					<input name="test_line_number" type="text" class="dfinput" value="${data.test_line_number }" />
				</li>
				<br /><br />
				<li style="display:inline; margin-top: 10px">
					<label>起止序号</label>
					<input name="se_number" type="text" class="dfinput" value="${data.se_number }" />
				</li>&nbsp;&nbsp;&nbsp;
				<li style="display:inline; margin-top: 10px">
					<label>&nbsp;记录长度&nbsp;&nbsp;</label>
					<input name="record_length" type="text" class="dfinput" value="${data.record_length }" />
				</li>
				<br /><br />
				<li style="display:inline; margin-top: 10px">
					<label>采样间隔</label>
					<input name="use_interval" type="text" class="dfinput" value="${data.use_interval }" />
				</li>&nbsp;&nbsp;&nbsp;
				<li style="display:inline; margin-top: 10px">
					<label>&nbsp;磁带编号&nbsp;&nbsp;</label>
					<input name="tape_number" type="text" class="dfinput" value="${data.tape_number }" />
				</li>
				<br /><br />
				<li style="display:inline; margin-top: 10px">
					<label>磁带盘数</label>
					<input name="tape_size" type="text" class="dfinput" value="${data.tape_size }" />
				</li>&nbsp;&nbsp;&nbsp;
				<li style="display:inline; margin-top: 10px">
					<label>数据量(GB)</label>
					<input name="data_quantity" type="text" class="dfinput" value="${data.data_quantity }" />
				</li>
				<br /><br />
				<li style="display:inline; margin-top: 10px">
					<label>施工年度</label>
					<input name="construction_year" type="text" class="dfinput" value="${data.construction_year }" />
				</li>&nbsp;&nbsp;&nbsp;
				<li style="display:inline; margin-top: 10px">
					<label>&nbsp;施工单位&nbsp;&nbsp;</label>
					<input name="construction_unit" type="text" class="dfinput" value="${data.construction_unit }" />
				</li>
				<br /><br />
				<li>
					<label>备注</label>
					 <textarea id="remarks" name="remarks" style="width:700px;height:250px;visibility:hidden;">${data.remarks }</textarea>
				</li>
				<li><label>&nbsp;</label><input type="submit" class="btn" value="修改"/></li>
			</ul>
		</form>
	</div>

</body>
</html>