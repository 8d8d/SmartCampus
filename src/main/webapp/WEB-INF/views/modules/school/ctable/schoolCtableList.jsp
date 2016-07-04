<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>课程表管理</title>
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
		<li class="active"><a href="${ctx}/school/ctable/schoolCtable/">课程表列表</a></li>
		<shiro:hasPermission name="school:ctable:schoolCtable:edit"><li><a href="${ctx}/school/ctable/schoolCtable/form">课程表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="schoolCtable" action="${ctx}/school/ctable/schoolCtable/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>课程名称：</label>
			</li>
			<li><label>第几周：</label>
				<form:input path="weekNum" htmlEscape="false" maxlength="3" class="input-medium"/>
			</li>
			<!--  <li><label>任课教师：</label>
				<sys:treeselect id="teacherId" name="teacherId" value="${schoolCtable.teacherId}" labelName="" labelValue="${schoolCtable.teacherId}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			-->
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed"  style="table-layout:fixed;" >
		<thead>
			<tr>
				<th>课节</th>
				<th>一</th>
				<th>二</th>
				<th>三</th>
				<th>四</th>
				<th>五</th>
				<th>六</th>
				<th>日</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="i" begin="1" end="11">
				<tr><td>${i}</td>
					<c:forEach var="j" begin="1" end="7">
						
						<td>
							<c:forEach items="${page.list}" var="schoolCtable">
								<c:if test="${schoolCtable.weekday==j&&schoolCtable.courseOrder==i}">
									<a href="${ctx}/school/ctable/schoolCtable/form?id=${schoolCtable.id}">${schoolCtable.courseId}</a>
									<br>${schoolCtable.classroomId}
									<br>${schoolCtable.teacherId}
									<br><shiro:hasPermission name="school:ctable:schoolCtable:edit">
					    				<a href="${ctx}/school/ctable/schoolCtable/form?id=${schoolCtable.id}">修改</a>
										<a href="${ctx}/school/ctable/schoolCtable/delete?id=${schoolCtable.id}" onclick="return confirmx('确认要删除该课程表吗？', this.href)">删除</a>
									</shiro:hasPermission>
								</c:if>
							</c:forEach>
						</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>课程名称</th>
				<th>星期</th>
				<th>第几周</th>
				<th>课节</th>
				<th>教室</th>
<!--  				<th>班级</th>
				<th>日期</th>-->
				<th>任课教师</th>
				<th>备注</th>
				<shiro:hasPermission name="school:ctable:schoolCtable:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="schoolCtable">
			<tr>
				<td><a href="${ctx}/school/ctable/schoolCtable/form?id=${schoolCtable.id}">
					${schoolCtable.courseId}
				</a></td>
				<td>
					${schoolCtable.weekday}
				</td>
				<td>
					${schoolCtable.weekNum}
				</td>
				<td>
					${schoolCtable.courseOrder}
				</td>
				<td>
					${schoolCtable.classroomId}
				</td>
				<!--<td>
					${schoolCtable.office.name}
				</td>
				<td>
					<fmt:formatDate value="${schoolCtable.courseDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				
				!-->
				<td>
					${schoolCtable.teacherId}
				</td>
				<td>
					${schoolCtable.remarks}
				</td>
				<shiro:hasPermission name="school:ctable:schoolCtable:edit"><td>
				    <a href="http://10.0.0.100:8080/smartCampus/static/ckfinder/ckfinder.html?type=files&start=files:/${schoolCtable.id}">上传课件</a>
    				<a href="${ctx}/school/ctable/schoolCtable/form?id=${schoolCtable.id}">修改</a>
					<a href="${ctx}/school/ctable/schoolCtable/delete?id=${schoolCtable.id}" onclick="return confirmx('确认要删除该课程表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>