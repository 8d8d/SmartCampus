<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>设置当前周管理</title>
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
		<li class="active"><a href="${ctx}/school/currentweek/schoolCurrentWeek/">当前周</a></li>
		<shiro:hasPermission name="school:currentweek:schoolCurrentWeek:edit"><li><a href="${ctx}/school/currentweek/schoolCurrentWeek/form?id=${page.list[0].id}">修改当前周</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolCurrentWeek" action="${ctx}/school/currentweek/schoolCurrentWeek/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>week</th>
				<th>update_date</th>
				<th>remarks</th>
				<shiro:hasPermission name="school:currentweek:schoolCurrentWeek:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolCurrentWeek">
			<tr>
				<td><a href="${ctx}/school/currentweek/schoolCurrentWeek/form?id=${schoolCurrentWeek.id}">
					${schoolCurrentWeek.week}
				</a></td>
				<td>
					<fmt:formatDate value="${schoolCurrentWeek.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${schoolCurrentWeek.remarks}
				</td>
				<shiro:hasPermission name="school:currentweek:schoolCurrentWeek:edit"><td>
    				<a href="${ctx}/school/currentweek/schoolCurrentWeek/form?id=${schoolCurrentWeek.id}">修改</a>
					<a href="${ctx}/school/currentweek/schoolCurrentWeek/delete?id=${schoolCurrentWeek.id}" onclick="return confirmx('确认要删除该设置当前周吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>