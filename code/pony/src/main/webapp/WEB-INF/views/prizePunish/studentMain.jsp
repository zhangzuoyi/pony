<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生奖惩查看</title>
<link rel="stylesheet" type="text/css"
	href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css"
	href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css"
	href="<s:url value='/static/css/icon.css' />" />
<link rel="stylesheet" type="text/css"
	href="<s:url value='/static/bootstrap/css/bootstrap.min.css' />" />
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
<script type="text/javascript"
	src="<s:url value='/static/vue/vue.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/vue/vue-resource.min.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/bootstrap/js/bootstrap.min.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/js/moment.min.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/vue/myfilters.js' />"></script>
</head>
<body class="easyui-layout">
	<div id="app">
		<div class="easyui-layout" data-options="fit:true">
			<!-- Begin of toolbar -->
			<div id="my-toolbar-2">
				<div class="my-toolbar-search">学号：${student.studentNo }
					姓名：${student.name } 性别：${student.sex }</div>
			</div>
			<!-- End of toolbar -->
			<!-- <table id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2" data-options="singleSelect:true,fitColumns:true,fit:true"> -->
			<table id="my-datagrid-2" class="table table-striped">
				<thead>
					<tr>
						<th>学年</th>
						<th>学期</th>
						<th>奖惩日期</th>
						<th>奖惩类型</th>
						<th>奖惩信息</th>
						<th>原因</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="r in pps">
						<td>{{r.schoolYear.name}}</td>
						<td>{{r.term.name}}</td>
						<td>{{r.occurDate | date}}</td>
						<td>{{r.type}}</td>
						<td>{{r.info}}</td>
						<td>{{r.reason}}</td>
						<td>
							<button v-on:click="editPP(r.id)" class="btn btn-primary btn-sm">修改</button>
							<button v-on:click="delPP(r.id)" class="btn btn-primary btn-sm">删除</button>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="my-dialog-2" class="easyui-dialog"
			data-options="closed:true,iconCls:'icon-save'"
			style="width: 400px; padding: 10px;">
			<form id="my-form-2" method="post">
				<input type="hidden" name="id" v-model="currentPP.id" />
				<table>
					<tr>
						<td width="60" align="right">学号:</td>
						<td><input type="text" v-model="currentPP.student.studentNo"
							class="my-text" readonly="readonly" /></td>
					</tr>
					<tr>
						<td width="60" align="right">姓名:</td>
						<td><input type="text" v-model="currentPP.student.name"
							class="my-text" readonly="readonly" /></td>
					</tr>
					<tr>
						<td width="60" align="right">奖惩日期:</td>
						<td><input type="text" name="occurDate"
							v-model="currentPP.occurDate" class="my-text easyui-datebox"
							data-options="formatter:myformatter,parser:myparser" /></td>
					</tr>
					<tr>
						<td width="60" align="right">奖征类型:</td>
						<td><select name="type" class="my-select"
							v-model="currentPP.type">
								<c:forEach items="${ppTypes }" var="g">
									<option value="${g.key }">${g.value }</option>
								</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td width="60" align="right">奖惩信息:</td>
						<td><input type="text" name="info" v-model="currentPP.info"
							class="my-text" /></td>
					</tr>
					<tr>
						<td width="60" align="right">原因:</td>
						<td><textarea rows="5" name="reason"
								v-model="currentPP.reason" style="width: 300px;"></textarea></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<script type="text/javascript"> 

function edit() { 
	$('#my-form-2').form('submit', { 
		url : "<s:url value='/prizePunish/edit' />", 
		success : function(data) { 
			if (data) { 
				$.messager.alert('信息提示', '提交成功！', 'info'); 
				app.getPPs(); 
				$('#my-dialog-2').dialog('close'); 
			} else { 
				$.messager.alert('信息提示', '提交失败！', 'info'); 
			} 
		} 
	}); 
} 

function openEdit() { 
	$('#my-dialog-2').dialog({ 
		closed : false, 
		modal : true, 
		title : "修改信息", 
		buttons : [ { 
				text : '确定', 
				iconCls : 'icon-ok', 
				handler : edit 
			}, { 
				text : '取消', 
				iconCls : 'icon-cancel', 
				handler : function() { 
				$('#my-dialog-2').dialog('close'); 
			} 
		} ] 
	}); 
} 

var app = new Vue({ 
	el : '#app', 
	data : { 
		studentId : "${student.studentId}", 
		pps : [], 
		currentPP : {student:{}}, 
		getUrl : "<s:url value='/prizePunish/findByStudent' />", 
		delUrl : "<s:url value='/prizePunish/delete' />" 
	}, 
	mounted : function() { 
		this.getPPs(); 
	}, 
	methods : { 
		getPPs : function() { 
			this.$http.get(this.getUrl, {params:{studentId : this.studentId}}).then( 
			response => { 
			this.pps=response.data; 
			}, 
			response => {} 
			); 
		}, 
		delPP : function(id) { 
			if (confirm("确认删除吗？")) { 
			/* Vue.http.options.emulateJSON = true; 
			this.$http.post(this.delUrl, {params:{ id : id }}).then(function(data) { 
			this.getRemarks(); 
			});  */ 
			Vue.http.options.emulateJSON = true; 
			this.$http.post(this.delUrl+"?id="+id).then( 
			response => { 
			this.getPPs(); 
			}, 
			response => {} 
			); 
			} 
		}, 
		editPP : function(id) { 
			for(var i=0;i<this.pps.length;i++){ 
				if(id == this.pps[i].id){ 
					this.currentPP = jQuery.extend(true, {}, this.pps[i]); 
					$('input[name="occurDate"]').val(this.currentPP.occurDate);
					break; 
				} 
			} 
			openEdit(); 
		} 
	} 
}); 
</script>
</body>
</html>