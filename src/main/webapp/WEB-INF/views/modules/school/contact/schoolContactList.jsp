<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>联系人设置管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, ids = [], rootIds = [];
			for (var i=0; i<data.length; i++){
				ids.push(data[i].id);
			}
			ids = ',' + ids.join(',') + ',';
			for (var i=0; i<data.length; i++){
				if (ids.indexOf(','+data[i].parentId+',') == -1){
					if ((','+rootIds.join(',')+',').indexOf(','+data[i].parentId+',') == -1){
						rootIds.push(data[i].parentId);
					}
				}
			}
			for (var i=0; i<rootIds.length; i++){
				addRow("#treeTableList", tpl, data, rootIds[i], true);
			}
			$("#treeTable").treeTable({expandLevel : 5});
		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if ((${fns:jsGetVal('row.parentId')}) == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
						blank123:0}, pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/school/contact/schoolContact/">联系人设置列表</a></li>
		<shiro:hasPermission name="school:contact:schoolContact:edit"><li><a href="${ctx}/school/contact/schoolContact/form">联系人设置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolContact" action="${ctx}/school/contact/schoolContact/" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>parent_id：</label>
			</li>
			<li><label>用户：</label>
				<sys:treeselect id="user" name="user.id" value="${schoolContact.user.id}" labelName="user.name" labelValue="${schoolContact.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>name：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>parent_id</th>
				<th>用户</th>
				<th>name</th>
				<th>update_date</th>
				<th>remarks</th>
				<shiro:hasPermission name="school:contact:schoolContact:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="${ctx}/school/contact/schoolContact/form?id={{row.id}}">
				{{row.parent.id}}
			</a></td>
			<td>
				{{row.user.name}}
			</td>
			<td>
				{{row.name}}
			</td>
			<td>
				{{row.updateDate}}
			</td>
			<td>
				{{row.remarks}}
			</td>
			<shiro:hasPermission name="school:contact:schoolContact:edit"><td>
   				<a href="${ctx}/school/contact/schoolContact/form?id={{row.id}}">修改</a>
				<a href="${ctx}/school/contact/schoolContact/delete?id={{row.id}}" onclick="return confirmx('确认要删除该联系人设置及所有子联系人设置吗？', this.href)">删除</a>
				<a href="${ctx}/school/contact/schoolContact/form?parent.id={{row.id}}">添加下级联系人设置</a> 
			</td></shiro:hasPermission>
		</tr>
	</script>
</body>
</html>