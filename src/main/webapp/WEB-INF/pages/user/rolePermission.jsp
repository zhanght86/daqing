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
		
		initSelectBtnsStatus();
		
		// set up the select buttons
		function initSelectBtnsStatus() {
			var trList = $('#selectedRolePermissions').children('tr').not('.noPermissions');
			if (trList.length) {
				for (var i = 0; i < trList.length; i++) {
					var permissionId = $(trList[i]).data('permissionid');
					var $availablePermission = $('#availablePermissions').children('tr[data-permissionid=' + permissionId + ']');
					if ($availablePermission.length) {
						$availablePermission.find('a.select-permission').hide();
					}
				}
			}
			
			$('#availablePermissions').show();
		}
	
		$("#saveBtn").click(function() {
			$('#updateRolePermissionsForm').find('input').not('[name="roleId"]').remove();
			
			var trList = $('#selectedRolePermissions').children('tr').not('.noPermissions');
			if (trList.length) {
				for (var i = 0; i < trList.length; i++) {
					var $item = $(trList[i]);
					var permissionId = $item.data('permissionid');
					
					var $input = $('<input type="hidden" name="permissionId" value="" />');
					$('#updateRolePermissionsForm').append($input);
					
					$input.val(permissionId);
				}
			}
			
			$.ajax({
				url: $('#updateRolePermissionsForm').attr('action'),
				type: 'post',
				data: $('#updateRolePermissionsForm').serialize(),
				success: function (data) {
					ajaxLoadEnd();
					if ("true" == data) {
						showHintMessage('角色的权限设置成功！', 1500, function () {
							refreshTab();
						});
						
						$('#dialog-form').dialog('close');
					} else {
						showHintMessage('用角色的权限设置失败，请重新尝试');
					}
				}
			});
			
			return false;
		});
		
		$("#backBtn").click(function() {
			window.location.href = g_base_path + "/role/query.do";
		});
		
		$('#selectAllPermissionsBtn').click(function () {
			var trList = $('#availablePermissions').children('tr').not('.noPermissions');
			if (trList.length) {
				$('#selectedRolePermissions').hide();
				
				for (var i = 0; i < trList.length; i++) {
					var $item = $(trList[i]);
					if ($item.data('permissionid') > 0) {
						selectPermissionToRole(trList[i]);
					}
				}
				
				$('#selectedRolePermissions').show();
			}
			
			return false;
		});
		
		$('#removeAllPermissionsBtn').click(function () {
			var trList = $('#selectedRolePermissions').children('tr').not('.noPermissions');
			if (trList.length) {
				$('#selectedRolePermissions').hide();
				for (var i = 0; i < trList.length; i++) {
					removePermissionFromRole(trList[i]);
				}
				$('#selectedRolePermissions').show();
			}
			
			return false;
		});
		
		$( document ).on( 'click', '#availablePermissions a.select-permission', function () {
			var trEl = $(this).parent('td').parent('tr').get(0);
			
			$('#selectedRolePermissions').hide();
			selectPermissionToRole(trEl);
			$('#selectedRolePermissions').show();
		} );
		
		$( document ).on( 'click', '#selectedRolePermissions a.remove-permission', function () {
			var trEl = $(this).parent('td').parent('tr').get(0);

			$('#selectedRolePermissions').hide();
			removePermissionFromRole(trEl);
			$('#selectedRolePermissions').show();
		} );
		
		function selectPermissionToRole(trEl) {
			var $tr = $(trEl),
				permissionId = $tr.data('permissionid'),
				permissionCode = $tr.data('fpcode'),
				permissionType = $tr.data('fptype'),
				permissionName = $tr.data('fpname'),
				permissionUrl = $tr.data('fpurl'),
				permission = $tr.data('permission'),
				parentId = $tr.data('parentid');
				
			// hide select button
			$tr.find('a.select-permission').hide();
			
			// add it into right
			$('#selectedRolePermissions tr.noPermissions').remove();
			var $selectedPermission = null;
			var trList = $('#selectedRolePermissions').children('tr').not('.noPermissions');
			if (trList.length) {
				for (var i = 0; i < trList.length; i++) {
					var $item = $(trList[i]);
					if ($item.data('permissionid') == permissionId) {
						$selectedPermission = $item;
						
						break;
					}
				}
			}
			
			
			if ($selectedPermission == null) {
				var $newTr = $('<tr><td></td><td></td>' + 
				'<td><a href="javascript:void(0);" class="easyui-linkbutton remove-permission" plain="true" iconCls="icon-remove">删除</a></td></tr>');
				$('#selectedRolePermissions').append($newTr);
				
				$newTr.data('permissionid', permissionId);
				$newTr.data('fpcode', permissionCode);
				$newTr.data('fptype', permissionType);
				$newTr.data('fpname', permissionName);
				$newTr.data('fpurl', permissionUrl);
				$newTr.data('permission', permission);
				$newTr.data('parentid', parentId);
				
				$newTr.children('td').eq(0).html(permissionCode);
				$newTr.children('td').eq(1).html(permissionName);
				$newTr.children('td').eq(2).children('a').linkbutton();
			}
			
			refreshRightCss();
		}
		
		function removePermissionFromRole(trEl) {
			var $tr = $(trEl),
				permissionId = $tr.data('permissionid'),
				permissionCode = $tr.data('fpcode'),
				permissionType = $tr.data('fptype'),
				permissionName = $tr.data('fpname'),
				permissionUrl = $tr.data('fpurl'),
				permission = $tr.data('permission'),
				parentId = $tr.data('parentid');
			
			// remove it from right
			$tr.remove();
			refreshRightCss();
			
			// enable the select button in left
			var $availablePermission = $('#availablePermissions').children('tr[data-permissionid=' + permissionId + ']');
			if ($availablePermission.length) {
				$availablePermission.find('a.select-permission').show();
			} else {
				console.log("Can not find the permission element");
			}
		}
		
		function refreshRightCss() {
			var trList = $('#selectedRolePermissions').children('tr').not('.noPermissions');
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
				if (!$('#selectedRolePermissions').children('tr.noPermissions').length) {
					$('#selectedRolePermissions').append('<tr class="noPermissions"><td colspan="3" style="text-align: center;">请选择权限</td></tr>');
				}
			}
		}

	});
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true,iconCls:'icon-set'" style="padding: 2px;margin-bottom: 2px;width: 100%;height: 100%;">
		<div region="north" id="user_list" title="角色: ${role.roleName } - 权限设定 | 权限列表" collapsible="false" style="padding: 2px; height: 74px;">
			<div id="top_body">
				<div id="hintMessage"></div>
				<div class="button_bar">
					<a href="javascript:void(0)" id="backBtn" class="easyui-linkbutton" plain="true" iconCls="icon-back">返回</a>&nbsp;&nbsp;&nbsp;&nbsp; 
					<a href="javascript:void(0)" id="saveBtn" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>&nbsp;&nbsp;&nbsp;&nbsp; 
					<a href="javascript:refreshTab();" id="refreshBtn" class="easyui-linkbutton" plain="true" iconCls="icon-refresh">刷新</a>
				</div>
			</div>
		</div>
		<div region="west" title="权限列表" collapsible="false" split="true" style="width: 60%; padding: 2px;">
			<table class="list">
				<thead>
					<tr>
						<th width="20px;">&nbsp;</th>
						<th width="100px;">权限编码</th>
						<th>权限名称</th>
						<th>访问路径</th>
						<th>描述符</th>
						<th width="80px;">
							<a id="selectAllPermissionsBtn" href="javascript:void(0);"
								class="easyui-linkbutton" plain="true"  iconCls="icon-add">全选</a>
						</th>
					</tr>
				</thead>
				<tbody id="availablePermissions" style="display:none;">
					<c:set var="count" value="0" />
					<c:forEach items="${availablePermissions }" var="item" varStatus="status">
						<tr class="${status.index % 2 == 0 ? '' : 'brown' }" data-permissionid="${item.permissionId }" 
						data-fpcode="${item.permissionCode }" data-fptype="${item.permissionType }" data-fpname="${item.permissionName }" 
						data-fpurl="${item.permissionUrl }" data-permission="${item.permission }" data-parentid="${item.parentId }">
							<td>${status.index+1}</td>
							<td>${item.permissionCode }</td>
							<td>${item.permissionName }</td>
							<td>${item.permissionUrl }</td>
							<td>${item.permission }</td>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton select-permission" plain="true" iconCls="icon-add">选择</a>
							</td>
						</tr>
					</c:forEach>
					
					<c:if test="${empty availablePermissions }">
						<tr class="noPermissions">
							<td colspan="6" style="text-align: center;">没有可选的权限</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
		
		<div region="center" title="已选权限列表" collapsible="false" split="true" style="width: 40%; padding: 2px;">
			<table class="list" id="selected_list">
				<thead>
					<tr>
						<th>权限编码</th>
						<th>权限名称</th>
						<th width="80px;">
							<a id="removeAllPermissionsBtn" href="javascript:void(0);"
								class="easyui-linkbutton" plain="true" iconCls="icon-remove">全删</a>
						</th>
					</tr>
				</thead>
				<tbody id="selectedRolePermissions">
					<c:forEach items="${rolePermissions }" var="item" varStatus="status">
						<tr class="${status.index % 2 == 0 ? '' : 'brown' }" data-permissionid="${item.permissionId }" 
									data-fpcode="${item.permissionCode }" data-fptype="${item.permissionType }" data-fpname="${item.permissionName }" 
									data-fpurl="${item.permissionUrl }" data-permission="${item.permission }" data-parentid="${item.parentId }">
							<td>${item.permissionCode }</td>
							<td>${item.permissionName }</td>
							<td>
								<a href="javascript:void(0);"
									class="easyui-linkbutton remove-permission" plain="true" iconCls="icon-remove">删除</a>
							</td>
						</tr>
					</c:forEach>
					
					<c:if test="${empty rolePermissions }">
						<tr class="noPermissions">
							<td colspan="3" style="text-align: center;">请选择权限</td>
						</tr>
					</c:if>
				</tbody>
			</table>
			
			<form id="updateRolePermissionsForm" action="<%=basePath%>/role/rolePermission.do" method="post">
				<input type="hidden" name="roleId" value="${role.roleId }" />
			</form>
		</div>
	</div>
</body>
</html>