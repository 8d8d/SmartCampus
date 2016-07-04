<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>教师考勤管理</title>
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
		<li><a href="${ctx}/school/onclassteacher/schoolOnclassTeaccher/">教师考勤列表</a></li>
		<li class="active"><a href="${ctx}/school/onclassteacher/schoolOnclassTeaccher/form?id=${schoolOnclassTeaccher.id}">教师考勤<shiro:hasPermission name="school:onclassteacher:schoolOnclassTeaccher:edit">${not empty schoolOnclassTeaccher.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="school:onclassteacher:schoolOnclassTeaccher:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="schoolOnclassTeaccher" action="${ctx}/school/onclassteacher/schoolOnclassTeaccher/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">考勤状态：</label>
			<div class="controls">
				<form:select path="state" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('schoolOnclass')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								<span class="help-inline"><font color="red">*</font> </span>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">课程：</label>
			<div class="controls">
				<sys:treeselect id="lessonId" name="lessonId" value="${schoolOnclassTeaccher.lessonId}" labelName="" labelValue="${lesson}"
					title="课程" url="/school/ctable/schoolCtable/treeData" cssClass="" allowClear="true" notAllowSelectParent="true"/>
								<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">第几周：</label>
			<div class="controls">
				<form:input path="week" onkeyup="this.value=this.value.replace(/\D/g,'')" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
								<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户：</label>
			<div class="controls">
				<sys:treeselect id="user" name="user.id" value="${schoolOnclassTeaccher.user.id}" labelName="user.name" labelValue="${schoolOnclassTeaccher.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">remarks：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="school:onclassteacher:schoolOnclassTeaccher:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>