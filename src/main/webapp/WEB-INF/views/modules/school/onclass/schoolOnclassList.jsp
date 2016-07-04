<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>考勤管理</title>
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
		<li class="active"><a href="${ctx}/school/onclass/schoolOnclass/">考勤列表</a></li>
		<shiro:hasPermission name="school:onclass:schoolOnclass:edit"><li><a href="${ctx}/school/onclass/schoolOnclass/form">考勤添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolOnclass" action="${ctx}/school/onclass/schoolOnclass/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>学生姓名：</label>
				<sys:treeselect id="studentId" name="studentId" value="${schoolOnclass.studentId}" labelName="" labelValue="${schoolOnclass.studentId}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>第几周：</label>
				<form:input path="week" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>课程名称：</label>
				<form:input path="lessonId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>学生姓名</th>
				<th>考勤结果</th>
				<th>week</th>
				<th>课程名称</th>
				<th>备注</th>
				<shiro:hasPermission name="school:onclass:schoolOnclass:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolOnclass">
			<tr>
				<td><a href="${ctx}/school/onclass/schoolOnclass/form?id=${schoolOnclass.id}">
					${schoolOnclass.studentId}
				</a></td>
				<td>
					${schoolOnclass.result}
				</td>
				<td>
					${schoolOnclass.week}
				</td>
				<td>
					${schoolOnclass.lessonId}
				</td>
				<td>
					${schoolOnclass.remarks}
				</td>
				<shiro:hasPermission name="school:onclass:schoolOnclass:edit"><td>
    				<a href="${ctx}/school/onclass/schoolOnclass/form?id=${schoolOnclass.id}">修改</a>
					<a href="${ctx}/school/onclass/schoolOnclass/delete?id=${schoolOnclass.id}" onclick="return confirmx('确认要删除该考勤吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>