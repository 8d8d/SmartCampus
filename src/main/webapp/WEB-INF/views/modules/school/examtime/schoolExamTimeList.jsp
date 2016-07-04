<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>考试时间设置管理</title>
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
		<li class="active"><a href="${ctx}/school/examtime/schoolExamTime/">考试时间设置列表</a></li>
		<shiro:hasPermission name="school:examtime:schoolExamTime:edit"><li><a href="${ctx}/school/examtime/schoolExamTime/form">考试时间设置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolExamTime" action="${ctx}/school/examtime/schoolExamTime/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>type：</label>
				<form:input path="type" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>term：</label>
				<form:input path="term" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>year：</label>
				<form:input path="year" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>考试类型</th>
				<th>学期</th>
				<th>年度</th>
				<th>update_date</th>
				<th>remarks</th>
				<shiro:hasPermission name="school:examtime:schoolExamTime:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolExamTime">
			<tr>
				<td><a href="${ctx}/school/examtime/schoolExamTime/form?id=${schoolExamTime.id}">
					${fns:getDictLabel(schoolExamTime.type, 'exam_type', '')}
				</a></td>
				<td>
					${fns:getDictLabel(schoolExamTime.term, 'exam_term', '')}
				</td>
				<td>
					${fns:getDictLabel(schoolExamTime.year, 'exam_year', '')}
				</td>
				<td>
					<fmt:formatDate value="${schoolExamTime.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${schoolExamTime.remarks}
				</td>
				<shiro:hasPermission name="school:examtime:schoolExamTime:edit"><td>
    				<a href="${ctx}/school/examtime/schoolExamTime/form?id=${schoolExamTime.id}">修改</a>
					<a href="${ctx}/school/examtime/schoolExamTime/delete?id=${schoolExamTime.id}" onclick="return confirmx('确认要删除该考试时间设置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>