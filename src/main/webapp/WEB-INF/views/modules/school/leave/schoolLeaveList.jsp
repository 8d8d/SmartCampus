<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假审批管理</title>
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
		<li class="active"><a href="${ctx}/school/leave/schoolLeave/">请假审批列表</a></li>
		<shiro:hasPermission name="school:leave:schoolLeave:edit"><li><a href="${ctx}/school/leave/schoolLeave/form">请假审批添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolLeave" action="${ctx}/school/leave/schoolLeave/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>申请人：</label>
				<form:input path="applyId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>审批人：</label>
				<form:input path="acceptId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>缘由：</label>
				<form:input path="reason" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>开始时间：</label>
				<input name="start" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${schoolLeave.start}" pattern="yyyy-MM-dd a HH:mm"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd a HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>结束时间：</label>
				<input name="end" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${schoolLeave.end}" pattern="yyyy-MM-dd HH:mm"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>处理结果：</label>
				<form:input path="dealId" htmlEscape="false" maxlength="1" class="input-medium"/>
			</li>
			<li><label>处理内容：</label>
				<form:input path="dealName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>处理缘由：</label>
				<form:input path="dealReason" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>申请人</th>
				<th>审批人</th>
				<th>缘由</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>处理结果</th>
				<th>处理内容</th>
				<th>处理缘由</th>
				<th>更新日期</th>
				<th>备注</th>
				<shiro:hasPermission name="school:leave:schoolLeave:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolLeave">
			<tr>
				<td><a href="${ctx}/school/leave/schoolLeave/form?id=${schoolLeave.id}">
					${schoolLeave.applyId}
				</a></td>
				<td>
					${schoolLeave.acceptId}
				</td>
				<td>
					${schoolLeave.reason}
				</td>
				<td>
					<fmt:formatDate value="${schoolLeave.start}" pattern="yyyy-MM-dd a"/>
				</td>
				<td>
					<fmt:formatDate value="${schoolLeave.end}" pattern="yyyy-MM-dd a"/>
				</td>
				<td>
					${schoolLeave.dealId}
				</td>
				<td>
					${schoolLeave.dealName}
				</td>
				<td>
					${schoolLeave.dealReason}
				</td>
				<td>
					<fmt:formatDate value="${schoolLeave.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${schoolLeave.remarks}
				</td>
				<shiro:hasPermission name="school:leave:schoolLeave:edit"><td>
    				<a href="${ctx}/school/leave/schoolLeave/form?id=${schoolLeave.id}">修改</a>
					<a href="${ctx}/school/leave/schoolLeave/delete?id=${schoolLeave.id}" onclick="return confirmx('确认要删除该请假审批吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>