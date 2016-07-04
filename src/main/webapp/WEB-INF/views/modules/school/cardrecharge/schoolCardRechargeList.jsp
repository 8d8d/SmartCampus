<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>一卡通充值记录管理</title>
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
		<li class="active"><a href="${ctx}/school/cardrecharge/schoolCardRecharge/">一卡通充值记录列表</a></li>
		<shiro:hasPermission name="school:cardrecharge:schoolCardRecharge:edit"><li><a href="${ctx}/school/cardrecharge/schoolCardRecharge/form">一卡通充值记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolCardRecharge" action="${ctx}/school/cardrecharge/schoolCardRecharge/" method="post" class="breadcrumb form-search">
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
				<th>卡号</th>
				<th>充值金额</th>
				<th>充值机器名</th>
				<th>充值钱包</th>
				<th>充值类型</th>
				<th>update_date</th>
				<th>remarks</th>
				<shiro:hasPermission name="school:cardrecharge:schoolCardRecharge:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolCardRecharge">
			<tr>
				<td><a href="${ctx}/school/cardrecharge/schoolCardRecharge/form?id=${schoolCardRecharge.id}">
					${schoolCardRecharge.cardId}
				</a></td>
				<td>
					${schoolCardRecharge.rechargeMoney}
				</td>
				<td>
					${schoolCardRecharge.rechargeName}
				</td>
				<td>
					${schoolCardRecharge.wallet}
				</td>
				<td>
					${schoolCardRecharge.rechargeType}
				</td>
				<td>
					<fmt:formatDate value="${schoolCardRecharge.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${schoolCardRecharge.remarks}
				</td>
				<shiro:hasPermission name="school:cardrecharge:schoolCardRecharge:edit"><td>
    		<!-- 	<a href="${ctx}/school/cardrecharge/schoolCardRecharge/form?id=${schoolCardRecharge.id}">修改</a> -->	
					<a href="${ctx}/school/cardrecharge/schoolCardRecharge/delete?id=${schoolCardRecharge.id}" onclick="return confirmx('确认要删除该一卡通充值记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>