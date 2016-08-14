<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
<script type="text/javascript" src="<%=basePath%>/static/js/common.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$(".click").click(function() {
		window.location.reload(true);
	});
	$(".history").click(function() {
		window.location.href = history.go(-1);
	})
	$(".select1").uedSelect({
			width : 150			  
	});
	$(".tiptop a").click(function() {
		$(".tip").fadeOut(200);
	});
	$(".sure").click(function() {
		$(".tip").fadeOut(100);
	});
	$(".exportHistoryClick").click(function() {
		window.location.href = '<%=basePath%>/burn/mergeList.do?volLabel=' + $("#volLabel").val();
	});
	$(".exportClick").click(function() {
		
		var checkFiles=getallcheckedvalue('box');
		var ischeck=splitCheck(checkFiles);	
	     if(!ischeck)
			{
			alert('您选择的带有split结尾文件,可能不属于同一个源文件,请重新选择同一个编号文件进行导出合并,谢谢!')
			return false;
			} 
		$(".tip").fadeIn(200);
				
		var sourcePath = document.getElementById('sourcePath');
		sourcePath.value=checkFiles;	
		
		 var dirPath=document.getElementById('dirPath');		    
	     dirPath.value=document.getElementById('volLabel').value;
		
			
	
	});
	
	
$(document).on('click','.button',function(){ 
	/* 		$(".aaa").click(function(){ */
	 
		  var currentPath=$("#currentPath").val() ;
		 // alert($("#currentPath").val());
		/*   var currentPath1= currentPath.substring(0, currentPath.lastIndexOf("/"));
		  alert(currentPath.split('/')[3]); */
		  var projectName=currentPath.substring(0,currentPath.substr(1).lastIndexOf('/')+1);
		 // alert(projectName);
		    var arg = {};
		    var arg1="path";
		    arg[arg1]=projectName;
		  
		  
							  $.post("<%=basePath%>/burn/openDir.do", arg,function(data,status){
							  
							  var json =eval(data); 
						      
							  $("#info").html(""); 
								 var table="<table>"
							  $.each(data, function(i, item) {
								  table+="<tr> <td><input type='radio' name='dir' value='"+item+"' onchange='getDataPath1(this)'></input></td> <td><a href='#' class='aaa' name='button' value='"+item+"'>"+item+"</a></td></tr>";
								
						        });
						
							table+=("</table>");
							 
							  /*  $("#info").append("<table> <tr> <td> "+item+"</td></tr></table>");*/
							  document.getElementById("info").innerHTML = table;
							
							/*  alert("Data: " + data + "\nStatus: " + status); */ 
						  });
				});
				
				
			$(document).on('click','.aaa',function(){ 
				/* 		$(".aaa").click(function(){ */
				 
					 
										  $.post("<%=basePath%>/burn/openDir.do", {path:$(this).attr("value")},function(data,status){
										  
										  var json =eval(data); 
							 
										  $("#info").html(""); 
											 var table="<table>"
										  $.each(data, function(i, item) {
											  table+="<tr> <td><input type='radio' name='dir' value='"+item+"' onchange='getDataPath1(this)'></input></td> <td><a href='#' class='aaa' name='button' value='"+item+"' onclick='setCurrent(\""+item+"\")'>"+item+"</a></td></tr>";
											
									        });
									
										table+=("</table>");
										 
										  /*  $("#info").append("<table> <tr> <td> "+item+"</td></tr></table>");*/
										  document.getElementById("info").innerHTML = table;
										
										/*  alert("Data: " + data + "\nStatus: " + status); */ 
									  });
							});
		});

             
	
   
	
	function isDictory(checkfile) {
		var filename = checkfile.substring(checkfile.lastIndexOf('/'));
		if (filename.lastIndexOf('.') < 0) {

			return confirm('您选择的文件可能是一个目录,目录将无法导出,您确定要选择吗?');
		}

		/* if (filename.indexOf('.split') > 0) {
			var split_filename_temp = filename.substring(0, filename.indexOf('('));
			if (split_filename_temp != split_filename) {
				return confirm('您选择的分割文件可能不属于同一个文件,,目录导出文件可能不可用,您确定要选择吗?');
			}
		} */
	}
	

	function splitCheck(sourcePath) {
		var sourcelist = sourcePath.split(',');
		var volumelabel = '';
		for (var int = 0; int < sourcelist.length; int++) {
			var filename = sourcelist[int].substring(sourcelist[int].lastIndexOf('/')+1);
			if (filename.indexOf('.split') > 0) {
				var split_filename_temp = filename.substring(0, filename
						.indexOf('('));
				if (volumelabel == '') {
					volumelabel = split_filename_temp;
				} else {
					if (volumelabel != split_filename_temp)
						return false;
				}
			}

		}
		return true;
	}
	
	function getDataPath1(obj)
	{
	 document.getElementById("exportPath").value =obj.value;  //显示获取结果
	}

	
	 function setCurrent(obj){
		 document.getElementById("currentPath").value=obj;
	 }
	 

</script>
</head>
<!-- 电子标签位置查看 -->
<body>
	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="<%=basePath%>/to.do?file=index">首页</a></li>
			<li><a
				href="<%=basePath%>/burn/mergeList.do?volLabel=${volLabel}">导出管理</a></li>
		</ul>
	</div>

	<div class="formbody">
		<div id="usual1" class="usual">

			<ul class="toolbar">
				<li class="click"><span><img
						src="<%=basePath%>/static/images/Refresh.gif" /></span>刷新</li>

				<li class="history"><span><img
						src="<%=basePath%>/static/images/left.png" /></span>返回</li>
						<shiro:hasPermission name="exp:fileExpOper">
				<li class="exportClick"><span><img
						src="<%=basePath%>/static/images/left.png" /></span>导出</li>
						</shiro:hasPermission>
						
			</ul>

			<form action="<%=basePath%>/burn/exportFileList.do">
				<ul class="seachform">
					<li><label>&nbsp;&nbsp;&nbsp;&nbsp;文件名</label> <input
						id="volLabel" name="volLabel" type="text" class="scinput"
						style="width: 400px" value="${volLabel}"/></li>

					<li><label>&nbsp;</label> <input name="" style="width: 100px" type="submit"
						class="scbtn"   value="在线文件查询" /></li>
					 	<li><label>&nbsp;</label> 
					 	<%-- <input name="" type="submit" style="width: 100px" class="scbtn" value="离线查询"  onclick="javascript:this.form.action='<%=basePath%>/burn/exportFileListOffine.do';"/></li> 
						<li><label>&nbsp;</label>  --%>
						<input name="" type="submit" style="width: 100px"
						class="scbtn" value="离线文件查询"  onclick="javascript:this.form.action='<%=basePath%>/burn/positionBySearch.do';"/></li> 
						
				</ul>
			</form>

			<table class="tablelist">
				<thead>
					<tr>
						<th width="5%">序号</th>
						
						<th width="5%">服务器名</th>
	
						<th width="75%">文件路径</th>
						<!-- 
						<th>操作用户</th>
						 -->
						<th width="5%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list }" var="map" varStatus="status">
						<tr class="${status.count % 2 == 0 ? '' : 'brown'}">
							<td width="50px">${status.count }</td>
							

							<%--<td>
								<c:if test="${map.export_state == 1}">下载数据</c:if>
								<c:if test="${map.export_state == 2}">下载成功</c:if>
								<c:if test="${map.export_state == 3}">下载失败</c:if>
								<c:if test="${map.export_state == 4}">导出成功</c:if>
								<c:if test="${map.export_state == 5}">导出失败</c:if>
								<c:if test="${map.export_state == 6}">关闭</c:if>
							</td> --%>
							
							<td >${map.server }</td>
							
							<td >${map.filePath}</td>
							<!-- <td>${map.c_user}</td>  -->
							<td><input id="box" name="box" type="checkbox"
								value="${map.server}:${map.filePath}" onclick="return isDictory('${map.server}:${map.filePath}');checkonebox('checkall','box');" />
							</td>
						</tr>
					</c:forEach>
				</tbody>

			</table class="tablelist">
			<input id="checkall" type="checkbox" value=""
				onclick="checkboxall('checkall','box')" /> 全选/全不选
			</td>


		</div>
	</div>
	<div class="tip">
		<div class="tiptop">
			<span>提示</span><a></a>
		</div>
		<form action="<%=basePath%>/burn/exportFile.do">
			<input type="hidden" id="sourcePath" name="sourcePath"> <input
				type="hidden" name="currentPath" id="currentPath" value="">
				 <input
				type="hidden" name="dirPath" id="dirPath" >

			<div class="tipinfo">
				<span><img src="<%=basePath%>/static/images/ticon.png" /></span>


				<%-- 	<td><div class="tipright">
					<p>合并文件</p>
					<cite>
						<li>
						  输出数据目录
						</li>
						<li>
							
						</li>
					</cite>
				</div></td>
			<td> --%>

				<div style="position: absolute; height: 200px; overflow: auto">
					输出数据目录:<input type="text" id="exportPath" name="exportPath"
						class="scinput" width="120px" value="/jukebox"> <input type="submit"
						class="sure" onclick="return confirm('请确认导出目录没有任何其它文件存在,尤其是split分割文件,否则导出任务将无法获取正确结果')" value="确定" /> <input type="button" name="goback"
						value="返回根目录" class="button">
					<table class="info" id="info">

						<c:forEach var="dirList" items="${dirLists}">
							<tr>
								<td><input type="radio" name="dir" value="${dirList}"
									onchange="getDataPath1(this)"></input></td>

								<td><a href="#" class="aaa" name="button"
									value="${dirList}" onclick="setCurrent('${dirList}')">${dirList}</a></td>
							</tr>
						</c:forEach>
					</table>
				</div>



			</div>




			<div class="tipbtn"></div>



		</form>
	</div>
</body>
</html>