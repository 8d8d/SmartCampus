<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>日程安排管理</title>
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
		<li class="active"><a href="${ctx}/school/agenda/schoolAgenda/">日程安排列表</a></li>
		<shiro:hasPermission name="school:agenda:schoolAgenda:edit"><li><a href="${ctx}/school/agenda/schoolAgenda/form">日程安排添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolAgenda" action="${ctx}/school/agenda/schoolAgenda/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>重复方式：</label>
				<form:select path="repeats" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('schoolRepeat')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>开始时间：</label>
				<input name="start" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${schoolAgenda.start}" pattern="yyyy-MM-dd HH:mm"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>结束时间：</label>
				<input name="end" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${schoolAgenda.end}" pattern="yyyy-MM-dd HH:mm"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>提醒方式：</label>
				<form:select path="remind" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('schoolRemind')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>内容：</label>
				<form:input path="content" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>用户：</label>
				<sys:treeselect id="user" name="user.id" value="${schoolAgenda.user.id}" labelName="user.name" labelValue="${schoolAgenda.user.name}"
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
				<th>重复方式</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>提醒方式</th>
				<th>内容</th>
				<th>用户</th>
				<th>update_date</th>
				<th>remarks</th>
				<shiro:hasPermission name="school:agenda:schoolAgenda:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolAgenda">
			<tr>
				<td><a href="${ctx}/school/agenda/schoolAgenda/form?id=${schoolAgenda.id}">
					${fns:getDictLabel(schoolAgenda.repeats, 'schoolRepeat', '')}
				</a></td>
				<td>
					<fmt:formatDate value="${schoolAgenda.start}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				<td>
					<fmt:formatDate value="${schoolAgenda.end}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				<td>
					${fns:getDictLabel(schoolAgenda.remind, 'schoolRemind', '')}
				</td>
				<td>
					${schoolAgenda.content}
				</td>
				<td>
					${schoolAgenda.user.name}
				</td>
				<td>
					<fmt:formatDate value="${schoolAgenda.updateDate}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				<td>
					${schoolAgenda.remarks}
				</td>
				<shiro:hasPermission name="school:agenda:schoolAgenda:edit"><td>
    				<a href="${ctx}/school/agenda/schoolAgenda/form?id=${schoolAgenda.id}">修改</a>
					<a href="${ctx}/school/agenda/schoolAgenda/delete?id=${schoolAgenda.id}" onclick="return confirmx('确认要删除该日程安排吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>