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
<script type="text/javascript">
	$(document).ready(function() {
		$(".click").click(function() {
			window.location.reload(true);
		});
		$(".history").click(function() {
			window.location.href = history.go(-1);
		})
		$(".select1").uedSelect({
				width : 100			  
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
			var fileselect="";
			$("input[name='fileselect']:checkbox:checked").each(function(){
				if(fileselect==""){
					fileselect=$(this).val();
				}else{
					fileselect+=","+$(this).val()
				}
			
			})
			
			if(fileselect==""){
				alert("请选择文件");
				return;
			}
			$("#fileselecttmp").val(fileselect);
			alert($("#fileselecttmp").val());
			
			$.ajax({url:"<%=basePath%>/burn/mergefileCheck.do",
				type : 'GET',
				data : {
					'volLabel' : $("#volLabel").val()
				},
				async : false,
				dataType : 'json',
				success : function(data) {
					if(data.result) {
						//$("#desc").html("已发起导出指令, 操作需要一定时间, 请等待");
						$(".tip").fadeIn(200);
					} else {
						//$("#desc").html("导出失败: 错误原因如下: ["+data.desc+"]");
						alert("合并失败: 错误原因如下: ["+data.desc+"]");
					}
					//$(".tip").fadeIn(200);
				},
				error : function() {
					//$("#desc").html("发起导出指令失败, 请稍后再试");
					alert("发起导出指令失败, 请稍后再试");
				}
			});
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
	
	/*  */
	
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
			<li><a href="<%=basePath%>/download/list.do">电子标签</a></li>
		</ul>
	</div>


	<div class="formbody">
		<div id="usual1" class="usual">

			<ul class="toolbar">
				<li class="click"><span><img
						src="<%=basePath%>/static/images/Refresh.gif" /></span>刷新</li>
				<li class="history"><span><img src="<%=basePath%>/static/images/left.png" /></span>返回</li>
				<li class="exportClick"><span><img
						src="<%=basePath%>/static/images/t01.png" /></span>导出</li>
			 
			</ul>

	<form action="<%=basePath%>/burn/downloadfile.do" method="post" width="600">
			<input type="hidden" value="${volLabel }" name="volLabel" />
 			<input type="hidden" value="${server }" name="server" />
 			<input type="hidden" value="${findstr}" name="findstr" />
			<ul class="seachform">
				<li>
					<label>&nbsp;&nbsp;&nbsp;&nbsp;文件名</label>
					<input name="filename" type="text" class="scinput" value="" />
				</li>
						<li>
					<label>&nbsp;&nbsp;&nbsp;&nbsp;盘槽号码</label>
					<div class="vocation">
					<select name="slot" class="select1">
					<option value="">空</option>
					<c:forEach items="${slotlist}" var="slotno" varStatus="status">
						<option value="${slotno.value}">${slotno.key}</option>
					</c:forEach>
					</select>
					</div>
				</li>
				 
			 
				 
				<li>
					<label>&nbsp;</label>
					<input type="submit" class="scbtn" value="查询" />
				</li>
		 
			</ul>
			</form>

			<table class="tablelist">
				<thead>
					<tr>
						<th>序号</th>
						<th>选择</th>
						<th>文件</th>
						<th>是否分割</th>
						<th>分割份数</th>
						<th>卷标名</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${fileList }" var="map" varStatus="status">
						<tr class="${status.count % 2 == 0 ? '' : 'brown'}">
							<td>${status.count }</td>
							<td><input type="checkbox" value="${map.id}" name="fileselect" class="checkbox1"> </td>
							<td>${map.filepath}</td>
								<td><c:if test="${map.splitstatus == '1'}">已分割</c:if>
									<c:if test="${map.splitstatus == '0'}">未分割</c:if></td>
									<td>${map.splitcount}</td>
										<td>${map.vol}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	
	<div class="tip">
		<div class="tiptop">
			<span>提示</span><a></a>
		</div>
			<form action="<%=basePath%>/burn/mergefile.do">
			<input type="hidden" name="volLabel" value="${volLabel}">
			<input type="hidden" value="${server }" name="server" />
 			<input type="hidden" value="${findstr}" name="findstr" />
 			<input type="hidden" value="" name="fileselecttmp" id="fileselecttmp"/>
 			
			<input type="hidden" name="currentPath" id="currentPath"   value="">
			
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
		
			<div style="position:absolute; height:200px; overflow:auto">
				  输出数据目录:<input type="text" id="exportPath" name="exportPath" class="scinput" value="/jukebox">     	<input type="submit" class="sure" value="确定" />
				<input type="button"  name="goback" value="返回根目录" class="button"> 
				<table class="info" id="info">
				
							<c:forEach var="dirList" items="${dirLists}">
								<tr  >
									<td><input type="radio" name="dir" value="${dirList}" onchange="getDataPath1(this)"></input></td>
										 
									<td><a href="#" class="aaa" name="button" value="${dirList}"  onclick="setCurrent('${dirList}')">${dirList}</a></td>
								</tr>
							</c:forEach>
						</table>
				</div>
			
			
				
			</div>
			 
				
				
				
				<div class="tipbtn">
			
				</div>
			 
			
		
			</form>
	</div>
	
</body>
</html>