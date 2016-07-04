<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>辅导员任教管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/school/leaderclass/schoolLeaderClass/">辅导员任教列表</a></li>
		<shiro:hasPermission name="school:leaderclass:schoolLeaderClass:edit"><li><a href="${ctx}/school/leaderclass/schoolLeaderClass/form">辅导员任教添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolLeaderClass" action="${ctx}/school/leaderclass/schoolLeaderClass/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>辅导员：</label>
				<sys:treeselect id="leaderId" name="leaderId" value="${schoolLeaderClass.leaderId}" labelName="" labelValue="${schoolLeaderClass.leaderId}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>班级：</label>
				<sys:treeselect id="classId" name="classId" value="${schoolLeaderClass.classId}" labelName="" labelValue="${schoolLeaderClass.classId}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>辅导员</th>
				<th>班级</th>
				<th>备注</th>
				<shiro:hasPermission name="school:leaderclass:schoolLeaderClass:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolLeaderClass">
			<tr>
				<td><a href="${ctx}/school/leaderclass/schoolLeaderClass/form?id=${schoolLeaderClass.id}">
					${schoolLeaderClass.leaderId}
				</a></td>
				<td>
					${schoolLeaderClass.classId}
				</td>
				<td>
					${schoolLeaderClass.remarks}
				</td>
				<shiro:hasPermission name="school:leaderclass:schoolLeaderClass:edit"><td>
    				<a href="${ctx}/school/leaderclass/schoolLeaderClass/form?id=${schoolLeaderClass.id}">修改</a>
					<a href="${ctx}/school/leaderclass/schoolLeaderClass/delete?id=${schoolLeaderClass.id}" onclick="return confirmx('确认要删除该辅导员任教吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>