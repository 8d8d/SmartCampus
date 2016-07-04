<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>校历管理</title>
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
		<li class="active"><a href="${ctx}/school/calendar/schoolCalendar/">校历列表</a></li>
		<shiro:hasPermission name="school:calendar:schoolCalendar:edit"><li><a href="${ctx}/school/calendar/schoolCalendar/form">校历添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolCalendar" action="${ctx}/school/calendar/schoolCalendar/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主题：</label>
				<form:input path="title" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主题</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>节日图标</th>
				<th>备注</th>
				<shiro:hasPermission name="school:calendar:schoolCalendar:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolCalendar">
			<tr>
				<td><a href="${ctx}/school/calendar/schoolCalendar/form?id=${schoolCalendar.id}">
					${schoolCalendar.title}
				</a></td>
				<td>
					<fmt:formatDate value="${schoolCalendar.start}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${schoolCalendar.end}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${schoolCalendar.picture}
				</td>
				<td>
					${schoolCalendar.remarks}
				</td>
				<shiro:hasPermission name="school:calendar:schoolCalendar:edit"><td>
    				<a href="${ctx}/school/calendar/schoolCalendar/form?id=${schoolCalendar.id}">修改</a>
					<a href="${ctx}/school/calendar/schoolCalendar/delete?id=${schoolCalendar.id}" onclick="return confirmx('确认要删除该校历吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>