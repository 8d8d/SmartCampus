<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>学生选课管理</title>
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
		<li class="active"><a href="${ctx}/school/lessonstudent/schoolLessonStudent/">学生选课列表</a></li>
		<shiro:hasPermission name="school:lessonstudent:schoolLessonStudent:edit"><li><a href="${ctx}/school/lessonstudent/schoolLessonStudent/form">学生选课添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolLessonStudent" action="${ctx}/school/lessonstudent/schoolLessonStudent/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>studentid：</label>
				<form:input path="studentid" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>lessonid：</label>
				<form:input path="lessonid" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>学生</th>
				<th>课程</th>
				<th>更新日期</th>
				<th>备注</th>
				<shiro:hasPermission name="school:lessonstudent:schoolLessonStudent:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolLessonStudent">
			<tr>
				<td><a href="${ctx}/school/lessonstudent/schoolLessonStudent/form?id=${schoolLessonStudent.id}">
					${schoolLessonStudent.studentid}
				</a></td>
				<td>
					${schoolLessonStudent.lessonid}
				</td>
				<td>
					<fmt:formatDate value="${schoolLessonStudent.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${schoolLessonStudent.remarks}
				</td>
				<shiro:hasPermission name="school:lessonstudent:schoolLessonStudent:edit"><td>
    				<a href="${ctx}/school/lessonstudent/schoolLessonStudent/form?id=${schoolLessonStudent.id}">修改</a>
					<a href="${ctx}/school/lessonstudent/schoolLessonStudent/delete?id=${schoolLessonStudent.id}" onclick="return confirmx('确认要删除该学生选课吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>