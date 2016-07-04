<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>上课时间表管理</title>
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
		<li class="active"><a href="${ctx}/school/lessontime/schoolLessonTime/">上课时间表列表</a></li>
		<shiro:hasPermission name="school:lessontime:schoolLessonTime:edit"><li><a href="${ctx}/school/lessontime/schoolLessonTime/form">上课时间表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolLessonTime" action="${ctx}/school/lessontime/schoolLessonTime/" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
  			<th>上级</th>
				<th>所有上级</th>
				<th>序号</th>
				<th>名称</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>排序</th>
				<th>更新时间</th>
				<th>备注</th>
				<shiro:hasPermission name="school:lessontime:schoolLessonTime:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="${ctx}/school/lessontime/schoolLessonTime/form?id={{row.id}}">
				{{row.parent.id}}
			</a></td>
			<td>
				{{row.parentIds}}
			</td>
			<td>
				{{row.count}}
			</td>
			<td>
				{{row.name}}
			</td>
			<td>
				{{row.start}}
			</td>
			<td>
				{{row.end}}
			</td>
			<td>
				{{row.sort}}
			</td>
			<td>
				{{row.updateDate}}
			</td>
			<td>
				{{row.remarks}}
			</td>
			<shiro:hasPermission name="school:lessontime:schoolLessonTime:edit"><td>
   				<a href="${ctx}/school/lessontime/schoolLessonTime/form?id={{row.id}}">修改</a>
				<a href="${ctx}/school/lessontime/schoolLessonTime/delete?id={{row.id}}" onclick="return confirmx('确认要删除该上课时间表及所有子上课时间表吗？', this.href)">删除</a>
				<a href="${ctx}/school/lessontime/schoolLessonTime/form?parent.id={{row.id}}">添加下级上课时间表</a> 
			</td></shiro:hasPermission>
		</tr>
	</script>
</body>
</html>