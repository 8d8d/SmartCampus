<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>教师任课管理管理</title>
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
		<li><a href="${ctx}/school/lessonteacher/schoolLessonTeacher/">教师任课管理列表</a></li>
		<li class="active"><a href="${ctx}/school/lessonteacher/schoolLessonTeacher/form?id=${schoolLessonTeacher.id}">教师任课管理<shiro:hasPermission name="school:lessonteacher:schoolLessonTeacher:edit">${not empty schoolLessonTeacher.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="school:lessonteacher:schoolLessonTeacher:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="schoolLessonTeacher" action="${ctx}/school/lessonteacher/schoolLessonTeacher/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">教师：</label>
			<div class="controls">
				<sys:treeselect id="teacherId" name="teacherId" value="${schoolLessonTeacher.teacherId}" labelName="" labelValue="${teacher}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
							<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">班级：</label>
			<div class="controls">
				<sys:treeselect id="classId" name="classId" value="${schoolLessonTeacher.classId}" labelName="" labelValue="${className}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="" allowClear="true" notAllowSelectParent="true"/>
							<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">课程名称：</label>
				<sys:treeselect id="lessonId" name="lessonId" value="${schoolCtable.lessonId}" labelName="" labelValue="${lesson}"
					title="课程种类" url="/school/classtype/schoolCType/treeData" cssClass="" allowClear="true" notAllowSelectParent="true"/>
							<span class="help-inline"><font color="red">*</font> </span>
			<div class="controls">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="school:lessonteacher:schoolLessonTeacher:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>