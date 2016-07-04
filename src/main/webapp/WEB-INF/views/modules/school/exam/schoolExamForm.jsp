<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>成绩查询管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/school/exam/schoolExam/">成绩查询列表</a></li>
		<li class="active"><a href="${ctx}/school/exam/schoolExam/form?id=${schoolExam.id}">成绩查询<shiro:hasPermission name="school:exam:schoolExam:edit">${not empty schoolExam.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="school:exam:schoolExam:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="schoolExam" action="${ctx}/school/exam/schoolExam/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">课程名称：</label>
			<div class="controls">
				<sys:treeselect id="courseId" name="courseId" value="${schoolExam.courseId}" labelName="" labelValue="${lesson}"
					title="课程种类" url="/school/ctable/schoolCtable/treeData" cssClass="" allowClear="true" notAllowSelectParent="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">考试名称：</label>
			<div class="controls">
				<sys:treeselect id="examId" name="examId" value="${schoolExam.examId}" labelName="" labelValue="${exam}"
					title="考试" url="/school/examtime/schoolExamTime/treeData" cssClass="" allowClear="true" notAllowSelectParent="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<!--  <div class="control-group">
			<label class="control-label">班级名称：</label>
			<div class="controls">
				<sys:treeselect id="classId" name="classId" value="${schoolExam.classId}" labelName="" labelValue="${schoolExam.classId}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">教师姓名：</label>
			<div class="controls">
				<sys:treeselect id="teacherId" name="teacherId" value="${schoolExam.teacherId}" labelName="" labelValue="${schoolExam.teacherId}"
					title="用户" url="/sys/office/treeDataTeacher?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		-->
		<div class="control-group">
			<label class="control-label">学生姓名：</label>
			<div class="controls">
				<sys:treeselect id="studentId" name="studentId" value="${schoolExam.studentId}" labelName="" labelValue="${student}"
					title="用户" url="/school/ctable/schoolCtable/treeDataStudent" cssClass="" allowClear="true" notAllowSelectParent="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">分数：</label>
			<div class="controls">
				<form:input path="score" htmlEscape="false" maxlength="64" onkeyup="value=value.replace(/[^\d.]/g,'')" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">考试时间：</label>
			<div class="controls"><!-- 设计数据表时忘记设计考试时间字段，把备注更改为考试时间 -->
				<fmt:parseDate value="${schoolExam.remarks}" var="yearMonth" pattern="yyyy年MM月dd日"/> 
				<input name="remarks" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${yearMonth}" pattern="yyyy年MM月dd日"/>"
					onclick="WdatePicker({dateFmt:'yyyy年MM月dd日',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="school:exam:schoolExam:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>