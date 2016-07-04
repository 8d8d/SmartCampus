<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>考勤管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				//$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
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
		<li><a href="${ctx}/school/onclass/schoolOnclass/">考勤列表</a></li>
		<li class="active"><a
			href="${ctx}/school/onclass/schoolOnclass/form?id=${schoolOnclass.id}">考勤<shiro:hasPermission
					name="school:onclass:schoolOnclass:edit">${not empty schoolOnclass.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="school:onclass:schoolOnclass:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="schoolOnclass"
		action="${ctx}/school/onclass/schoolOnclass/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="control-group">
			<label class="control-label">学生姓名：</label>
			<div class="controls">
				<sys:treeselect id="studentId" name="studentId"
					value="${schoolOnclass.studentId}" labelName=""
					labelValue="${student}" title="用户"
					url="/school/ctable/schoolCtable/treeDataStudent" cssClass="" allowClear="true"
					notAllowSelectParent="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">考勤结果：</label>
			<div class="controls">
				<form:checkboxes path="resultList"
					items="${fns:getDictList('studentOnclass')}" itemLabel="label"
					itemValue="value" htmlEscape="false" class="required" />
					<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">第几周：</label>
			<div class="controls">
				<form:input path="week" htmlEscape="false" maxlength="255" onkeyup="this.value=this.value.replace(/\D/g,'')"
					 class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">课程名称：</label>
			<div class="controls">
				<sys:treeselect id="lessonId" name="lessonId"
					value="${schoolOnclass.lessonId}" labelName=""
					labelValue="${lesson}" title="课程名称"
					url="/school/ctable/schoolCtable/treeData" cssClass=""
					allowClear="true" notAllowSelectParent="true" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4"
					maxlength="255" class="input-xxlarge " />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="school:onclass:schoolOnclass:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>