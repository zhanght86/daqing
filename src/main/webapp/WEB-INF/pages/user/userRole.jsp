<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
<head>
<c:import url="../include/headers.jsp"></c:import>
<style type="text/css">
.select_radio {
	position: relative;
	top: -2px;
}
</style>
<script type="text/javascript">

	var g_base_path = '<%=basePath%>';
	
	$(document).ready(function(){
		$("#user_list").panel({
			iconCls:'icon-org'
		});
	
		$("#saveBtn").click(function() {
			$('#updateUserRolesForm').find('input').not('[name="userId"]').remove();
			
			var selectedRoleTrs = $('#selectedUserRoles').children('tr').not('.noRoles');
			if (selectedRoleTrs.length) {
				for (var i = 0; i < selectedRoleTrs.length; i++) {
					var roleId = $(selectedRoleTrs[i]).data('roleid');
					
					var $input = $('<input type="hidden" name="roleId" value="" />');
					$('#updateUserRolesForm').append($input);
					
					$input.val(roleId);
				}
			}
			
			$.ajax({
				url: $('#updateUserRolesForm').attr('action'),
				type: 'post',
				data: $('#updateUserRolesForm').serialize(),
				success: function (data) {
					ajaxLoadEnd();
					if ("true" == data) {
						showHintMessage('用户角色设置成功！', 1500, function () {
							refreshTab();
						});
						
						$('#dialog-form').dialog('close');
					} else {
						showHintMessage('用户角色设置失败，请重新尝试');
					}
				}
			});
			
			return false;
		});
		
		$("#backBtn").click(function() {
			window.location.href= g_base_path + "/user/query.do";
		});
		
		$('#selectAllRolesBtn').click(function () {
			var trList = $('#availableRoles').children('tr').not('.noRoles');
			if (trList.length) {
				$('#availableRoles').hide();
				$('#selectedUserRoles').hide();
				
				for (var i = 0; i < trList.length; i++) {
					selectRoleToUser(trList[i]);
				}
				
				$('#availableRoles').show();
				$('#selectedUserRoles').show();
			}
			
			return false;
		});
		
		$('#removeAllRolesBtn').click(function () {
			var trList = $('#selectedUserRoles').children('tr').not('.noRoles');
			if (trList.length) {
				$('#availableRoles').hide();
				$('#selectedUserRoles').hide();
				for (var i = 0; i < trList.length; i++) {
					removeRoleFromUser(trList[i]);
				}
				
				$('#availableRoles').show();
				$('#selectedUserRoles').show();
			}
			
			return false;
		});
		
		$( document ).on( 'click', '#availableRoles a.select-role', function () {
			var trEl = $(this).parent('td').parent('tr').get(0);
			
			$('#availableRoles').hide();
			$('#selectedUserRoles').hide();
			selectRoleToUser(trEl);
			
			$('#availableRoles').show();
			$('#selectedUserRoles').show();
		} );
		
		$( document ).on( 'click', '#selectedUserRoles a.remove-role', function () {
			var trEl = $(this).parent('td').parent('tr').get(0);
			
			$('#availableRoles').hide();
			$('#selectedUserRoles').hide();
			
			removeRoleFromUser(trEl);
			
			$('#availableRoles').show();
			$('#selectedUserRoles').show();
		} );
		
		function selectRoleToUser(trEl) {
			var $tr = $(trEl),
				roleId = $tr.data('roleid'),
				roleCode = $tr.data('rolecode'),
				roleName = $tr.data('rolename'),
				remark = $tr.data('remark');
			
			// remove it from left
			$tr.remove();
			refreshLeftCss();
			
			// add it into right
			$('#selectedUserRoles tr.noRoles').remove();
			var $newTr = $('<tr><td></td><td></td>' + 
					'<td><a href="javascript:void(0);" class="easyui-linkbutton remove-role" plain="true" iconCls="icon-unselect">删除</a></td></tr>');
			$('#selectedUserRoles').append($newTr);
			
			$newTr.data('roleid', roleId);
			$newTr.data('rolecode', roleCode);
			$newTr.data('rolename', roleName);
			$newTr.data('remark', remark);
			
			$newTr.children('td').eq(0).html(roleCode);
			$newTr.children('td').eq(1).html(roleName);
			$newTr.children('td').eq(2).children('a').linkbutton();
			
			refreshRightCss();
		}
		
		function removeRoleFromUser(trEl) {
			var $tr = $(trEl),
			roleId = $tr.data('roleid'),
			roleCode = $tr.data('rolecode'),
			roleName = $tr.data('rolename'),
			remark = $tr.data('remark');
			
			// remove it from right
			$tr.remove();
			refreshRightCss();
			
			// add it into left
			$('#availableRoles tr.noRoles').remove();
			
			var $newTr = $('<tr><td></td><td></td><td></td><td></td>' + 
					'<td><a href="javascript:void(0);" class="easyui-linkbutton select-role" plain="true" iconCls="icon-plus">选择</a></td></tr>');
			$('#availableRoles').append($newTr);
			
			$newTr.data('roleid', roleId);
			$newTr.data('rolecode', roleCode);
			$newTr.data('rolename', roleName);
			$newTr.data('remark', remark);
			
			$newTr.children('td').eq(1).html(roleCode);
			$newTr.children('td').eq(2).html(roleName);
			$newTr.children('td').eq(3).html(remark);
			$newTr.children('td').eq(4).children('a').linkbutton();
			
			refreshLeftCss();
		}
		
		function refreshLeftCss() {
			var trList = $('#availableRoles').children('tr').not('.noRoles');
			if (trList.length) {
				for (var i = 0; i < trList.length; i++) {
					var $tr = $(trList[i]);
					$tr.children('td').eq(0).html(i+1);
					
					if (i % 2 == 0) {
						if ($tr.hasClass('brown')) $tr.removeClass('brown');
					} else {
						if (!$tr.hasClass('brown')) $tr.addClass('brown');
					}
				}
			} else {
				// no roles can be selected
				if (!$('#availableRoles').children('tr.noRoles').length) {
					$('#availableRoles').append('<tr class="noRoles"><td colspan="5" style="text-align: center;">没有可选的角色</td></tr>');
				}
			}
		}
		
		function refreshRightCss() {
			var trList = $('#selectedUserRoles').children('tr').not('.noRoles');
			if (trList.length) {
				for (var i = 0; i < trList.length; i++) {
					var $tr = $(trList[i]);
					if (i % 2 == 0) {
						if ($tr.hasClass('brown')) $tr.removeClass('brown');
					} else {
						if (!$tr.hasClass('brown')) $tr.addClass('brown');
					}
				}
			} else {
				// no roles can be selected
				if (!$('#selectedUserRoles').children('tr.noRoles').length) {
					$('#selectedUserRoles').append('<tr class="noRoles"><td colspan="3" style="text-align: center;">请选择角色</td></tr>');
				}
			}
		}

	});
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true,iconCls:'icon-set'" style="padding: 2px;margin-bottom: 2px;width: 100%;height: 100%;">
		<div region="north" id="user_list" title="用户-角色设定 | 角色列表" collapsible="false" style="padding: 2px; height: 74px;">
			<div id="top_body">
				<div id="hintMessage"></div>
				<div class="button_bar">
					<a href="javascript:void(0)" id="backBtn" class="easyui-linkbutton" plain="true" iconCls="icon-back">返回</a>&nbsp;&nbsp;&nbsp;&nbsp; 
					<a href="javascript:void(0)" id="saveBtn" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>&nbsp;&nbsp;&nbsp;&nbsp; 
					<a href="javascript:refreshTab();" id="refreshBtn" class="easyui-linkbutton" plain="true" iconCls="icon-refresh">刷新</a>
				</div>
			</div>
		</div>
		<div region="west" title="可选角色列表" collapsible="false" split="true" style="width: 60%; padding: 2px;">
			<table class="list">
				<thead>
					<tr>
						<th width="20px;">&nbsp;</th>
						<th>角色编码</th>
						<th>角色名称</th>
						<th>备注</th>
						<th width="80px;">
							<a id="selectAllRolesBtn" href="javascript:void(0);"
								class="easyui-linkbutton"  iconCls="icon-right" >全选</a>
						</th>
					</tr>
				</thead>
				<tbody id="availableRoles">
					<c:forEach items="${availableRoles }" var="item" varStatus="status">
						<tr class="${status.index % 2 == 0 ? '' : 'brown' }" data-roleid="${item.roleId }" 
							data-rolecode="${item.roleCode }" data-rolename="${item.roleName }" data-remark="${item.remark }">
							<td>${status.index+1 }</td>
							<td>${item.roleCode }</td>
							<td>${item.roleName }</td>
							<td>${item.remark }</td>
							<td>
								<a href="javascript:void(0);"
									class="easyui-linkbutton select-role" plain="true" iconCls="icon-plus">选择</a>
							</td>
						</tr>
					</c:forEach>
					
					<c:if test="${empty availableRoles }">
						<tr class="noRoles">
							<td colspan="5" style="text-align: center;">没有可选的角色</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
		
		<div region="center" title="已选角色列表" collapsible="false" split="true" style="width: 40%; padding: 2px;">
			<table class="list" id="selected_list">
				<thead>
					<tr>
						<th>角色编码</th>
						<th>角色名称</th>
						<th width="80px;">
							<a id="removeAllRolesBtn" href="javascript:void(0);"
								class="easyui-linkbutton"  iconCls="icon-left" >全删</a>
						</th>
					</tr>
				</thead>
				<tbody id="selectedUserRoles">
					<c:forEach items="${userRoles }" var="item" varStatus="status">
						<tr class="${status.index % 2 == 0 ? '' : 'brown' }" data-roleid="${item.roleId }" 
							data-rolecode="${item.roleCode }" data-rolename="${item.roleName }" data-remark="${item.remark }">
							<td>${item.roleCode }</td>
							<td>${item.roleName }</td>
							<td>
								<a href="javascript:void(0);"
									class="easyui-linkbutton remove-role" plain="true" iconCls="icon-unselect">删除</a>
							</td>
						</tr>
					</c:forEach>
					
					<c:if test="${empty userRoles }">
						<tr class="noRoles">
							<td colspan="3" style="text-align: center;">请选择角色</td>
						</tr>
					</c:if>
				</tbody>
			</table>
			
			<form id="updateUserRolesForm" action="<%=basePath%>/user/userRole.do" method="post">
				<input type="hidden" name="userId" value="${userId }" />
			</form>
		</div>
	</div>
</body>
</html>