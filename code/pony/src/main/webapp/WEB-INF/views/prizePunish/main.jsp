<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生奖惩管理</title>
<link rel="stylesheet" type="text/css"
	href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css"
	href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css"
	href="<s:url value='/static/css/icon.css' />" />
<script type="text/javascript"
	src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/easyui/dateFormat.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/easyui/datagrid-cellediting.js' />"></script>
</head>
<body class="easyui-layout">
	<div class="easyui-layout" data-options="fit:true">
		<!-- Begin of toolbar -->
		<div id="my-toolbar-2">
			<div class="my-toolbar-search">
				学年：${year.name } 学期：${term.name } <label>班级：</label> <select
					name="schoolClass" class="my-select" panelHeight="auto"
					style="width: 100px">
					<c:forEach items="${classes }" var="g">
						<option value="${g.classId }">${g.name }</option>
					</c:forEach>
				</select> <a href="#" id="searchButton" class="easyui-linkbutton"
					iconCls="icon-search">查询</a>
			</div>
		</div>
		<!-- End of toolbar -->
		<table id="my-datagrid-2" class="easyui-datagrid"
			toolbar="#my-toolbar-2"
			data-options="singleSelect:true,fitColumns:true,fit:true">
			<thead>
				<tr>
					<th data-options="field:'studentNo',width:100">学号</th>
					<th data-options="field:'name',width:100">姓名</th>
					<th data-options="field:'sex',width:100,formatter:sexFormatter">性别</th>
					<th
						data-options="field:'schoolClass',width:100,formatter:operationFormatter">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<!-- Begin of easyui-dialog -->
	<div id="my-dialog-2" class="easyui-dialog"
		data-options="closed:true,iconCls:'icon-save'"
		style="width: 400px; padding: 10px;">
		<form id="my-form-2" method="post">
			<input type="hidden" name="student.studentId" />
			<table>
				<tr>
					<td width="60" align="right">学号:</td>
					<td><input type="text" name="student.studentNo"
						class="my-text" readonly="readonly" /></td>
				</tr>
				<tr>
					<td width="60" align="right">姓名:</td>
					<td><input type="text" name="student.name" class="my-text"
						readonly="readonly" /></td>
				</tr>
				<tr>
					<td width="60" align="right">奖惩日期:</td>
					<td><input type="text" name="occurDate"
						class="my-text easyui-datebox"
						data-options="formatter:myformatter,parser:myparser" /></td>
				</tr>
				<tr>
					<td width="60" align="right">奖征类型:</td>
					<td><select name="type" class="my-select">
							<c:forEach items="${ppTypes }" var="g">
								<option value="${g.key }">${g.value }</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td width="60" align="right">奖惩信息:</td>
					<td><input type="text" name="info" class="my-text" /></td>
				</tr>
				<tr>
					<td width="60" align="right">原因:</td>
					<td><textarea rows="5" name="reason" style="width: 300px;"></textarea></td>
				</tr>
			</table>
		</form>
	</div>
	<!-- End of easyui-dialog -->
	<script type="text/javascript">
		function add() {
			$('#my-form-2').form('submit', {
				url : "<s:url value='/prizePunish/add' />",
				success : function(data) {
					if (data) {
						$.messager.alert('信息提示', '提交成功！', 'info');
						$('#my-dialog-2').dialog('close');
					} else {
						$.messager.alert('信息提示', '提交失败！', 'info');
					}
				}
			});
		}

		function openAdd() {
			$('#my-dialog-2').dialog({
				closed : false,
				modal : true,
				title : "添加评语",
				buttons : [ {
					text : '确定',
					iconCls : 'icon-ok',
					handler : add
				}, {
					text : '取消',
					iconCls : 'icon-cancel',
					handler : function() {
						$('#my-dialog-2').dialog('close');
					}
				} ]
			});
		}

		function operationFormatter(value, rec, index) {
			return '<a href="javascript:void(0)" class="easyui-linkbutton" onclick="viewPP(\''
					+ index
					+ '\')" style="width:80px">评语查看</a>&nbsp;'
					+ '<a href="javascript:void(0)" class="easyui-linkbutton" onclick="addPP(\''
					+ index + '\')" style="width:80px">新增评语</a>';
			;
		}
		function sexFormatter(value, rec) {
			if (rec.sex == 'M') {
				return "男";
			} else if (rec.sex == 'F') {
				return "女";
			}
			return "";
		}
		function addPP(index) {
			var data = $('#my-datagrid-2').datagrid('getData').rows;
			//data=JSON.stringify(data); 
			//alert(data); 
			var rec = data[index];
			//alert(rec); 
			//$('#my-form-2').form('clear'); 
			//$('#my-form-2').form('load', rec); 
			$('input[name="student.studentNo"]').val(rec.studentNo);
			$('input[name="student.name"]').val(rec.name);
			$('input[name="student.studentId"]').val(rec.studentId);
			$('input[name="info"]').val("");
			$('textarea[name="reason"]').val("");
			openAdd();
		}
		function viewPP(index) {
			var data = $('#my-datagrid-2').datagrid('getData').rows;
			var rec = data[index];
			var url = "<s:url value='/prizePunish/studentMain' />"
					+ "?studentId=" + rec.studentId;
			window.parent.addTab("奖惩查看", url, "icon-users", true);
		}

		$(document)
				.ready(
						function() {
							$("#searchButton")
									.click(
											function() {
												var classId = $(
														"select[name='schoolClass']")
														.children(
																'option:selected')
														.val();
												if (classId) {
													$
															.ajax({
																url : "<s:url value='/studentAdmin/findByClass' />",
																data : {
																	classId : classId
																},
																method : "GET",
																dataType : "json",
																success : function(
																		data) {
																	$(
																			'#my-datagrid-2')
																			.datagrid(
																					{
																						data : data
																					});

																}
															});
												} else {
													alert("empty");
												}

											});
							$("#searchButton").click();
						});
	</script>
</body>
</html>