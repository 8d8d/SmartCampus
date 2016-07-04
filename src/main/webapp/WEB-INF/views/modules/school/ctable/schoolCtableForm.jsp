<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>课程表管理</title>
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
		<li><a href="${ctx}/school/ctable/schoolCtable/">课程表列表</a></li>
		<li class="active"><a href="${ctx}/school/ctable/schoolCtable/form?id=${schoolCtable.id}">课程表<shiro:hasPermission name="school:ctable:schoolCtable:edit">${not empty schoolCtable.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="school:ctable:schoolCtable:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="schoolCtable" action="${ctx}/school/ctable/schoolCtable/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">课程名称：</label>
				<sys:treeselect id="courseId" name="courseId" value="${schoolCtable.courseId}" labelName="" labelValue="${schoolCtable.courseId}"
					title="课程种类" url="/school/classtype/schoolCType/treeData" cssClass="" allowClear="true" notAllowSelectParent="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			<div class="controls">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">星期：</label>
			<div class="controls">
				<form:input path="weekday" htmlEscape="false" maxlength="1" onkeyup="this.value=this.value.replace(/\D/g,'')" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">起始周：</label>
			<div class="controls">
				<form:input path="weekNum" htmlEscape="false" maxlength="3" class="input-xlarge required"/>(例如1,4代表第一到第四周)<!--  （开学时间：${ fns:getDictLabel ("2016-03-01", "school_ctable", "2016-03-01")}）-->
							<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">课节：</label>
			<div class="controls">
				<form:input path="courseOrder" onkeyup="value=value.replace(/[^(\d)]/g,'')" htmlEscape="false" maxlength="2" class="input-xlarge required"/><!-- ${schoolCtable.start} -->
			<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">上课时间：</label>
			<div class="controls">
				<sys:treeselect id="start" name="start" value="${schoolCtable.start}" labelName="" labelValue="${schoolCtable.start}"
					title="上课时间" url="/school/lessontime/schoolLessonTime/treeData" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">教室：</label>
			<div class="controls">
				<sys:treeselect id="classroomId" name="classroomId" value="${schoolCtable.classroomId}" labelName="" labelValue="${schoolCtable.classroomId}"
					title="教室名称" url="/school/classroom/schoolClassroom/treeData" cssClass="" allowClear="true" notAllowSelectParent="true"/>
						<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
<!--		<div class="control-group">
			<label class="control-label">班级：</label>
			<div class="controls">
				<sys:treeselect id="office" name="office.id" value="${schoolCtable.office.id}" labelName="office.name" labelValue="${schoolCtable.office.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">日期：</label>
			<div class="controls">
				<input name="courseDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${schoolCtable.courseDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		
		  -->
		<div class="control-group">
			<label class="control-label">任课教师：</label>
			<div class="controls">
				<sys:treeselect id="teacherId" name="teacherId" value="${schoolCtable.teacherId}" labelName="" labelValue="${schoolCtable.teacherId}"
					title="用户" url="/sys/office/treeDataTeacher?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
						<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="school:ctable:schoolCtable:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>