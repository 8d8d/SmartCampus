<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>一卡通信息设置管理</title>
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
		<li class="active"><a href="${ctx}/school/card/schoolCard/">一卡通信息设置列表</a></li>
		<shiro:hasPermission name="school:card:schoolCard:edit"><li><a href="${ctx}/school/card/schoolCard/form">一卡通信息设置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolCard" action="${ctx}/school/card/schoolCard/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>状态</th>
				<th>余额</th>
				<th>用户</th>
				<th>卡号</th>
				<th>update_date</th>
				<th>remarks</th>
				<shiro:hasPermission name="school:card:schoolCard:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolCard">
			<tr>
				<td><a href="${ctx}/school/card/schoolCard/form?id=${schoolCard.id}">
					${schoolCard.state}
				</a></td>
				<td>
					${schoolCard.balance}
				</td>
				<td>
					${schoolCard.user.name}
				</td>
				<td>
					${schoolCard.cardId}
				</td>
				<td>
					<fmt:formatDate value="${schoolCard.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${schoolCard.remarks}
				</td>
				<shiro:hasPermission name="school:card:schoolCard:edit"><td>
    				<a href="${ctx}/school/card/schoolCard/form?id=${schoolCard.id}">修改</a>
					<a href="${ctx}/school/card/schoolCard/delete?id=${schoolCard.id}" onclick="return confirmx('确认要删除该一卡通信息设置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>