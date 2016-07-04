<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>一卡通消费记录管理</title>
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
		<li class="active"><a href="${ctx}/school/cardconsum/schoolCardConsum/">一卡通消费记录列表</a></li>
		<shiro:hasPermission name="school:cardconsum:schoolCardConsum:edit"><li><a href="${ctx}/school/cardconsum/schoolCardConsum/form">一卡通消费记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolCardConsum" action="${ctx}/school/cardconsum/schoolCardConsum/" method="post" class="breadcrumb form-search">
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
				<th>消费金额</th>
				<th>消费名字</th>
				<th>消费类型</th>
				<th>卡号</th>
				<th>update_date</th>
				<th>remarks</th>
				<shiro:hasPermission name="school:cardconsum:schoolCardConsum:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolCardConsum">
			<tr>
				<td><a href="${ctx}/school/cardconsum/schoolCardConsum/form?id=${schoolCardConsum.id}">
					${schoolCardConsum.consumMoney}
				</a></td>
				<td>
					${schoolCardConsum.consumName}
				</td>
				<td>
					${schoolCardConsum.consumType}
				</td>
				<td>
					${schoolCardConsum.cardId}
				</td>
				<td>
					<fmt:formatDate value="${schoolCardConsum.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${schoolCardConsum.remarks}
				</td>
				<shiro:hasPermission name="school:cardconsum:schoolCardConsum:edit"><td>
<!-- 				<a href="${ctx}/school/cardconsum/schoolCardConsum/form?id=${schoolCardConsum.id}">修改</a>  -->   
					<a href="${ctx}/school/cardconsum/schoolCardConsum/delete?id=${schoolCardConsum.id}" onclick="return confirmx('确认要删除该一卡通消费记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>