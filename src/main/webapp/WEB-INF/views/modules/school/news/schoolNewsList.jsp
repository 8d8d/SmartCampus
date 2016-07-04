<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>校园新闻管理</title>
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
		<li class="active"><a href="${ctx}/school/news/schoolNews/">校园新闻列表</a></li>
		<shiro:hasPermission name="school:news:schoolNews:edit"><li><a href="${ctx}/school/news/schoolNews/form">校园新闻添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolNews" action="${ctx}/school/news/schoolNews/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>新闻ID</th>
				<th>地址</th>
				<th>标题</th>
				<th>内容</th>
				<th>封面图片地址</th>
				<th>创建者</th>
				<th>创建日期</th>
				<th>备注</th>
				<shiro:hasPermission name="school:news:schoolNews:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolNews">
			<tr>
				<td><a href="${ctx}/school/news/schoolNews/form?id=${schoolNews.id}">
					${schoolNews.newId}
				</a></td>
				<td>
					${schoolNews.address}
				</td>
				<td>
					${schoolNews.title}
				</td>
				<td>
					${schoolNews.content}
				</td>
				<td>
					${schoolNews.cover}
				</td>
				<td>
					${schoolNews.createBy.id}
				</td>
				<td>
					<fmt:formatDate value="${schoolNews.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${schoolNews.remarks}
				</td>
				<shiro:hasPermission name="school:news:schoolNews:edit"><td>
    				<a href="${ctx}/school/news/schoolNews/form?id=${schoolNews.id}">修改</a>
					<a href="${ctx}/school/news/schoolNews/delete?id=${schoolNews.id}" onclick="return confirmx('确认要删除该校园新闻吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>