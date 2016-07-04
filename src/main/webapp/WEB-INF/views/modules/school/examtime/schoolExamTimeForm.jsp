<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>考试时间设置管理</title>
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
		<li><a href="${ctx}/school/examtime/schoolExamTime/">考试时间设置列表</a></li>
		<li class="active"><a href="${ctx}/school/examtime/schoolExamTime/form?id=${schoolExamTime.id}">考试时间设置<shiro:hasPermission name="school:examtime:schoolExamTime:edit">${not empty schoolExamTime.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="school:examtime:schoolExamTime:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="schoolExamTime" action="${ctx}/school/examtime/schoolExamTime/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">考试类型：</label>
			<div class="controls">
				<form:select path="type" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('exam_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
									<span class="help-inline"><font color="red">*</font> </span>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">学期：</label>
			<div class="controls">
				<form:select path="term" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('exam_term')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
									<span class="help-inline"><font color="red">*</font> </span>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">年度：</label>
			<div class="controls">
				<form:select path="year" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('exam_year')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
									<span class="help-inline"><font color="red">*</font> </span>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">remarks：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="school:examtime:schoolExamTime:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>