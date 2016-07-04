<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>校园公告管理</title>
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
		<li class="active"><a href="${ctx}/school/notice/schoolNotice/">校园公告列表</a></li>
		<shiro:hasPermission name="school:notice:schoolNotice:edit"><li><a href="${ctx}/school/notice/schoolNotice/form">校园公告添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolNotice" action="${ctx}/school/notice/schoolNotice/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>创建者：</label>
				<form:input path="createBy.id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>更新者：</label>
				<form:input path="updateBy.id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>公告ID</th>
				<th>标题</th>
				<th>内容</th>
				<th>地址</th>
				<th>创建者</th>
				<th>更新者</th>
				<th>更新时间</th>
				<th>备注</th>
				<shiro:hasPermission name="school:notice:schoolNotice:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolNotice">
			<tr>
				<td><a href="${ctx}/school/notice/schoolNotice/form?id=${schoolNotice.id}">
					${schoolNotice.noticeId}
				</a></td>
				<td>
					${schoolNotice.title}
				</td>
				<td>
					${schoolNotice.content}
				</td>
				<td>
					${schoolNotice.address}
				</td>
				<td>
					${schoolNotice.createBy.id}
				</td>
				<td>
					${schoolNotice.updateBy.id}
				</td>
				<td>
					<fmt:formatDate value="${schoolNotice.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${schoolNotice.remarks}
				</td>
				<shiro:hasPermission name="school:notice:schoolNotice:edit"><td>
    				<a href="${ctx}/school/notice/schoolNotice/form?id=${schoolNotice.id}">修改</a>
					<a href="${ctx}/school/notice/schoolNotice/delete?id=${schoolNotice.id}" onclick="return confirmx('确认要删除该校园公告吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>