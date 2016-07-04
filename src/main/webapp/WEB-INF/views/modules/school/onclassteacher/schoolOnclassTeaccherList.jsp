<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>教师考勤管理</title>
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
		<li class="active"><a href="${ctx}/school/onclassteacher/schoolOnclassTeaccher/">教师考勤列表</a></li>
		<shiro:hasPermission name="school:onclassteacher:schoolOnclassTeaccher:edit"><li><a href="${ctx}/school/onclassteacher/schoolOnclassTeaccher/form">教师考勤添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolOnclassTeaccher" action="${ctx}/school/onclassteacher/schoolOnclassTeaccher/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>考勤状态：</label>
				<form:select path="state" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('school_onclass')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>课程：</label>
				<form:input path="lessonId" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>第几周：</label>
				<form:input path="week" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>用户：</label>
				<sys:treeselect id="user" name="user.id" value="${schoolOnclassTeaccher.user.id}" labelName="user.name" labelValue="${schoolOnclassTeaccher.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>考勤状态</th>
				<th>课程</th>
				<th>第几周</th>
				<th>用户</th>
				<th>update_date</th>
				<th>remarks</th>
				<shiro:hasPermission name="school:onclassteacher:schoolOnclassTeaccher:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolOnclassTeaccher">
			<tr>
				<td><a href="${ctx}/school/onclassteacher/schoolOnclassTeaccher/form?id=${schoolOnclassTeaccher.id}">
					${fns:getDictLabel(schoolOnclassTeaccher.state, 'schoolOnclass', '')}
				</a></td>
				<td>
					${schoolOnclassTeaccher.lessonId}
				</td>
				<td>
					${schoolOnclassTeaccher.week}
				</td>
				<td>
					${schoolOnclassTeaccher.user.name}
				</td>
				<td>
					<fmt:formatDate value="${schoolOnclassTeaccher.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${schoolOnclassTeaccher.remarks}
				</td>
				<shiro:hasPermission name="school:onclassteacher:schoolOnclassTeaccher:edit"><td>
    				<a href="${ctx}/school/onclassteacher/schoolOnclassTeaccher/form?id=${schoolOnclassTeaccher.id}">修改</a>
					<a href="${ctx}/school/onclassteacher/schoolOnclassTeaccher/delete?id=${schoolOnclassTeaccher.id}" onclick="return confirmx('确认要删除该教师考勤吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>