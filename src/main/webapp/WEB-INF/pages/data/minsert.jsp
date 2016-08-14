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
			<li><a href="<%=basePath%>/mData/list.do">中间数据</a></li>
		</ul>
	</div>

	<div class="formbody">

		<div class="formtitle">
			<span>中间数据</span>
		</div>
		<form action="<%=basePath%>/data/insert.do">
			<input type="hidden" name="dataType" value="M">
			<input type="hidden" name="url" value="mData/list.do">
			<input type="hidden" name="sid" value="${sid}">
			<input type="hidden" name="valLabel" value="${valLabel}">
			<ul class="forminfo">
			
			<li style="display:inline;">
					<label>项目名称　</label>
					<input name="project_name" type="text" class="dfinput" value="" />
				</li>
				&nbsp;&nbsp;&nbsp;
				<li style="display:inline;">
					<label>工区名称</label>
					<input name="area_block_name" type="text" class="dfinput" value="" />
				</li>
				<br /><br />
				<li style="display:inline;">
					<label>负责人</label>
					<input name="project_leader" type="text" class="dfinput" value="" />
				</li>
				&nbsp;&nbsp;&nbsp;
				<li style="display:inline;">
					<label>科室&nbsp;&nbsp;</label>
					<input name="department_name" type="text" class="dfinput" value="" />
				</li>
				
				<br /><br />
				<li style="display:inline;">
					<label>应用软件&nbsp;</label>
					<input name="software" type="text" class="dfinput" value="" />
				</li>
				&nbsp;&nbsp;&nbsp;
					<li style="display:inline;">
					<label>处理步骤　</label>
					<input name="processing_step" type="text" class="dfinput" value="" />
				</li>
				<br /><br />
				<li style="display:inline;">
					<label>数据量(GB)</label>
					<input name="data_quantity" type="text" class="dfinput" value="" />
				</li>
				&nbsp;&nbsp;&nbsp;
				<li style="display:inline;">
					<label>申请类型&nbsp;&nbsp;</label>
					<input name="application_type" type="text" class="dfinput" value="" />
				</li>
				<br /><br />
				<li style="display:inline;">
					<label>磁带编号&nbsp;&nbsp;</label>
					<input name="tape_number" type="text" class="dfinput" value="" />
				</li>
				&nbsp;&nbsp;&nbsp;
					<li style="display:inline;">
					<label>状态&nbsp;&nbsp;</label>
					<input name="state" type="text" class="dfinput" value="" />
				</li>
				<br /><br />
				<li style="display:inline;">
					<label>备份时间　</label>
					<input name="back_date" type="text" class="dfinput" value="${data.back_date }" />
				</li>
			
				<li>
					<label>备注</label>
					 <textarea id="remarks" name="remarks" style="width:700px;height:250px;visibility:hidden;"></textarea>
				</li>
				<li><label>&nbsp;</label><input type="submit" class="btn" value="保存"/></li>
			</ul>
		</form>
	</div>
</body>
</html>