<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>上课时间表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")){
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
		<li><a href="${ctx}/school/lessontime/schoolLessonTime/">上课时间表列表</a></li>
		<li class="active"><a href="${ctx}/school/lessontime/schoolLessonTime/form?id=${schoolLessonTime.id}&parent.id=${schoolLessonTimeparent.id}">上课时间表<shiro:hasPermission name="school:lessontime:schoolLessonTime:edit">${not empty schoolLessonTime.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="school:lessontime:schoolLessonTime:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="schoolLessonTime" action="${ctx}/school/lessontime/schoolLessonTime/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
	   <div class="control-group">
			<label class="control-label">上级上级:</label>
			<div class="controls">
				<sys:treeselect id="parent" name="parent.id" value="${schoolLessonTime.parent.id}" labelName="parent.name" labelValue="${schoolLessonTime.parent.name}"
					title="上级" url="/school/lessontime/schoolLessonTime/treeData" extId="${schoolLessonTime.id}" cssClass="" allowClear="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">序号：</label>
			<div class="controls">
				<form:input path="count" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
								<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
								<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间：</label>
			<div class="controls">
			    <fmt:parseDate value="${schoolLessonTime.start}" var="datestart" pattern="HH:mm:ss"/>
				<input name="start" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${datestart}" pattern="HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});"/>
									<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间：</label>
			<div class="controls">
				 <fmt:parseDate value="${schoolLessonTime.end}" var="dateend" pattern="HH:mm:ss"/>
				<input name="end" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${dateend}" pattern="HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});"/>
									<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="11" class="input-xlarge  digits required"/>
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
			<shiro:hasPermission name="school:lessontime:schoolLessonTime:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>