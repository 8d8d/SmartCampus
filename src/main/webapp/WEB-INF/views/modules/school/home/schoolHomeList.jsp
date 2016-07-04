<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>首页管理</title>
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
		<li class="active"><a href="${ctx}/school/home/schoolHome/">首页列表</a></li>
		<shiro:hasPermission name="school:home:schoolHome:edit"><li><a href="${ctx}/school/home/schoolHome/form">首页添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolHome" action="${ctx}/school/home/schoolHome/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>功能：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>功能</th>
				<th>图片</th>
				<th>备注</th>
				<shiro:hasPermission name="school:home:schoolHome:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolHome">
			<tr>
				<td><a href="${ctx}/school/home/schoolHome/form?id=${schoolHome.id}">
					${schoolHome.name}
				</a></td>
				<td>
					${schoolHome.pic}
				</td>
				<td>
					${schoolHome.remarks}
				</td>
				<shiro:hasPermission name="school:home:schoolHome:edit"><td>
    				<a href="${ctx}/school/home/schoolHome/form?id=${schoolHome.id}">修改</a>
					<a href="${ctx}/school/home/schoolHome/delete?id=${schoolHome.id}" onclick="return confirmx('确认要删除该首页吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>