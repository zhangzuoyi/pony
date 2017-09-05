<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍卡</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/bootstrap/css/bootstrap.min.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/bootstrap-select/bootstrap-select.min.css' />" />
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/dateFormat.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/datagrid-cellediting.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue-resource.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/bootstrap/js/bootstrap.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/bootstrap-select/bootstrap-select.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/js/moment.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/myfilters.js' />"></script>
</head>
<body>
	<div id="app" >
		<div class="row" style="padding:10px 20px;">
			学年：
			<select name="schoolYear" v-model="condition.schoolYear" v-on:change="changeCondition()">
			<c:forEach items="${years }" var="g">
				<option value="${g.yearId }">${g.name }</option>
			</c:forEach>
			</select>
			年级：
			<select name="grade" v-model="condition.grade" v-on:change="changeCondition()">
				<option value="">请选择</option>
			<c:forEach items="${grades }" var="g">
				<option value="${g.gradeId }">${g.name }</option>
			</c:forEach>
			</select>
			班级：
			<select name="schoolClass"  v-model="condition.schoolClass" v-on:change="changeClass()">
				<option value="">请选择</option>
				<option v-for="sc in schoolClasses" v-bind:value="sc.classId">{{sc.name}}</option>
			</select>
			学生：
			<!-- <input type="text" class="span3" name="student" data-provide="typeahead" data-items="4" /> -->
			<!-- class="selectpicker"  -->
			<select name="studentId" v-model="studentId"><!-- 使用bootstrap-select -->
				<option v-for="s in students" v-bind:value="s.studentId">{{s.name}}</option>
			</select>
			<button type="button" class="btn btn-default btn-sm" v-on:click="getStudent()">查看</button>
		</div>
		<div class="panel panel-default" v-if="card.student">
		  <div class="panel-heading">基本信息</div>
		  <table class="table">
		  	<tr>
		  		<td>姓名</td><td>{{card.student.name}}</td>
		  		<td>学号</td><td>{{card.student.studentNo}}</td>
		  		<td>性别</td><td>{{card.student.birthday}}</td>
		  	</tr>
		  	<tr>
		  		<td>出生日期</td><td>{{card.student.birthday}}</td>
		  		<td>入学日期</td><td>{{card.student.entranceDate}}</td>
		  		<td>学生类别</td><td>{{card.student.entranceType}}</td>
		  	</tr>
		  	<tr>
		  		<td>证件号码</td><td>{{card.student.idNo}}</td>
		  		<td>联系电话</td><td>{{card.student.phone}}</td>
		  		<td>Email</td><td>{{card.student.email}}</td>
		  	</tr>
		  	<tr>
		  		<td>籍贯</td><td>{{card.student.nativePlace}}</td>
		  		<td>户籍地址</td><td>{{card.student.nativeAddr}}</td>
		  		<td></td><td></td>
		  	</tr>
		  	<tr>
		  		<td>邮编</td><td>{{card.student.homeZipcode}}</td>
		  		<td>居住地址</td><td>{{card.student.homeAddr}}</td>
		  		<td></td><td></td>
		  	</tr>
		  	<tr>
		  		<td>状态</td><td>{{card.student.status}}</td>
		  		<td>毕业日期</td><td>{{card.student.graduateDate}}</td>
		  		<td></td><td></td>
		  	</tr>
		  </table>
		</div>
		<!-- <div class="panel panel-default">
		  <div class="panel-heading">变动信息</div>
		  <table class="table">
		  	<tr>
		  		
		  	</tr>
		  </table>
		</div> -->
		<div class="panel panel-default" v-if="card.results">
		  <div class="panel-heading">成绩信息</div>
		  <table class="table">
		  	<tr>
		  		<th>学年</th>
		  		<th>学期</th>
		  		<th>考试类型</th>
		  		<th>成绩</th>
		  		<th>班级排名</th>
		  		<th>年级排名</th>
		  	</tr>
		  	<tr v-for="r in card.results">
		  		<td></td>
		  		<td></td>
		  		<td></td>
		  		<td>{{r.score}}</td>
		  		<td></td>
		  		<td></td>
		  	</tr>
		  </table>
		</div>
		<div class="panel panel-default" v-if="card.pps">
		  <div class="panel-heading">奖惩信息</div>
		  <table class="table">
		  	<tr>
		  		<th>学年</th>
		  		<th>学期</th>
		  		<th>奖惩类型</th>
		  		<th>发生日期</th>
		  		<th>说明</th>
		  		<th>原因</th>
		  	</tr>
		  	<tr v-for="r in card.pps">
		  		<td>{{r.schoolYear.name}}</td>
		  		<td>{{r.term.name}}</td>
		  		<td>{{r.type}}</td>
		  		<td>{{r.occurDate}}</td>
		  		<td>{{r.info}}</td>
		  		<td>{{r.reason}}</td>
		  	</tr>
		  </table>
		</div>
		<div class="panel panel-default" v-if="card.remarks">
		  <div class="panel-heading">评语信息</div>
		  <table class="table">
		  	<tr>
		  		<th>学年</th>
		  		<th>学期</th>
		  		<th>评语级别</th>
		  		<th>评语日期</th>
		  		<th>老师</th>
		  		<th>评语</th>
		  	</tr>
		  	<tr v-for="r in card.remarks">
		  		<td>{{r.schoolYear.name}}</td>
		  		<td>{{r.term.name}}</td>
		  		<td>{{r.remarkLevel}}</td>
		  		<td>{{r.remarkTime | date}}</td>
		  		<td>{{r.teacher.name}}</td>
		  		<td>{{r.remark}}</td>
		  	</tr>
		  </table>
		</div>
	</div>
	<script type="text/javascript"> 
	//$('.selectpicker').selectpicker();//bootstrap-select
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
		condition : {}, 
		card : {}, 
		schoolClasses : [],
		students : [],
		studentId : null,
		getClassesUrl : "<s:url value='/schoolClass/findByYearAndGrade' />", 
		getStudentsUrl : "<s:url value='/studentAdmin/findByClass' />",
		getCardUrl : "<s:url value='/studentCard/getCard' />" 
	}, 
	mounted : function() { 
		//this.getPPs(); 
	}, 
	methods : { 
		changeCondition : function(){
			if(this.condition.schoolYear && this.condition.grade){
				this.$http.get(this.getClassesUrl,{params:{yearId:this.condition.schoolYear, gradeId:this.condition.grade}}).then(
					function(response){
						this.schoolClasses=response.body;
					}		
				);
			}
		},
		changeClass : function(){
			if(this.condition.schoolClass){
				this.$http.get(this.getStudentsUrl,{params:{classId : this.condition.schoolClass}}).then(
					function(response){
						this.students=response.body;
					}		
				);
			}
		},
		getStudent : function(){
			if(this.studentId){
				this.$http.get(this.getCardUrl,{params:{studentId : this.studentId}}).then(
					function(response){
						this.card=response.body;
					}		
				);
			}
		}
	} 
}); 
</script>
</body>
</html>