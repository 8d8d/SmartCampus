<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>成绩查询管理</title>
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
		<li class="active"><a href="${ctx}/school/exam/schoolExam/">成绩查询列表</a></li>
		<shiro:hasPermission name="school:exam:schoolExam:edit"><li><a href="${ctx}/school/exam/schoolExam/form">成绩查询添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolExam" action="${ctx}/school/exam/schoolExam/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>课程名称：</label>
			</li>
			<li><label>考试名称：</label>
				<form:select path="examId" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('school_exam')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>班级名称：</label>
				<sys:treeselect id="classId" name="classId" value="${schoolExam.classId}" labelName="" labelValue="${schoolExam.classId}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>教师姓名：</label>
				<sys:treeselect id="teacherId" name="teacherId" value="${schoolExam.teacherId}" labelName="" labelValue="${schoolExam.teacherId}"
					title="用户" url="/sys/office/treeDataTeacher?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>学生姓名：</label>
				<sys:treeselect id="studentId" name="studentId" value="${schoolExam.studentId}" labelName="" labelValue="${schoolExam.studentId}"
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
				<th>课程名称</th>
				<th>考试名称</th>
	<!--  		<th>班级名称</th>
				<th>教师姓名</th>
				-->
				<th>学生姓名</th>
				<th>分数</th>
				<th>考试时间</th>
				<shiro:hasPermission name="school:exam:schoolExam:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolExam">
			<tr>
				<td><a href="${ctx}/school/exam/schoolExam/form?id=${schoolExam.id}">
					${schoolExam.courseId}
				</a></td>
				<td>
					${schoolExam.examId}
				</td>
				<!--  <td>
					${schoolExam.classId}
				</td>
				<td>
					${schoolExam.teacherId}
				</td>
				-->
				<td>
					${schoolExam.studentId}
				</td>
				<td>
					${schoolExam.score}
				</td>
				<td>
					${schoolExam.remarks}
				</td>
				<shiro:hasPermission name="school:exam:schoolExam:edit"><td>
    				<a href="${ctx}/school/exam/schoolExam/form?id=${schoolExam.id}">修改</a>
					<a href="${ctx}/school/exam/schoolExam/delete?id=${schoolExam.id}" onclick="return confirmx('确认要删除该成绩查询吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>