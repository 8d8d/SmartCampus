<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>教师任课管理管理</title>
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
		<li class="active"><a href="${ctx}/school/lessonteacher/schoolLessonTeacher/">教师任课管理列表</a></li>
		<shiro:hasPermission name="school:lessonteacher:schoolLessonTeacher:edit"><li><a href="${ctx}/school/lessonteacher/schoolLessonTeacher/form">教师任课管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolLessonTeacher" action="${ctx}/school/lessonteacher/schoolLessonTeacher/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>教师：</label>
				<sys:treeselect id="teacherId" name="teacherId" value="${schoolLessonTeacher.teacherId}" labelName="" labelValue="${schoolLessonTeacher.teacherId}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>班级：</label>
				<sys:treeselect id="classId" name="classId" value="${schoolLessonTeacher.classId}" labelName="" labelValue="${schoolLessonTeacher.classId}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>科目：</label>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>教师</th>
				<th>班级</th>
				<th>科目</th>
				<th>备注</th>
				<shiro:hasPermission name="school:lessonteacher:schoolLessonTeacher:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolLessonTeacher">
			<tr>
				<td><a href="${ctx}/school/lessonteacher/schoolLessonTeacher/form?id=${schoolLessonTeacher.id}">
					${schoolLessonTeacher.teacherId}
				</a></td>
				<td>
					${schoolLessonTeacher.classId}
				</td>
				<td>
					${schoolLessonTeacher.lessonId}
				</td>
				<td>
					${schoolLessonTeacher.remarks}
				</td>
				<shiro:hasPermission name="school:lessonteacher:schoolLessonTeacher:edit"><td>
    				<a href="${ctx}/school/lessonteacher/schoolLessonTeacher/form?id=${schoolLessonTeacher.id}">修改</a>
					<a href="${ctx}/school/lessonteacher/schoolLessonTeacher/delete?id=${schoolLessonTeacher.id}" onclick="return confirmx('确认要删除该教师任课管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>