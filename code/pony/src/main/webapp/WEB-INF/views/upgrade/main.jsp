<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>升级管理</title>
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
<body>
	<div id="app" >
		<div class="row" style="padding:10px 20px;">
			上一学年：${lastYear.name }
			当前学年：${currentYear.name }
			年级：
			<c:forEach items="${grades }" var="g">
				<input type="checkbox" name="grade" value="${g.gradeId }" />${g.name }
			</c:forEach>
			<button type="button" class="btn btn-default btn-sm" v-on:click="genPreview()">生成预览</button>
			<button type="button" class="btn btn-default btn-sm" v-on:click="submitUpgrade()">确认升级</button>
		</div>
		<div class="panel panel-default" style="margin:5px;" v-for="vo in upgradeVos">
		  <div class="panel-body">
		    <p>原年级：{{vo.srcGrade.name}}</p>
		    <p>目标年级：<span v-if="vo.targetGrade != null">{{vo.targetGrade.name}}</span><span v-else>无</span></p>
		    <p>升级类型：<span v-if="vo.targetGrade != null">升级</span><span v-else>毕业</span></p>
		  </div>
		  <table class="table">
		  	<tr>
		  		<th>原班级</th>
		  		<th>目标班级</th>
		  	</tr>
		    <tr v-for="(sc,index) in vo.srcClasses">
		    	<td>{{sc.seq}}</td>
		    	<td>
		    		<span v-if="vo.targetGrade != null"><input type="text" v-model="vo.targetClasses[index].seq" /></span>
		    		<span v-else>无</span>
		    	</td>
		    </tr>
		  </table>
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
		studentId : "", 
		upgradeVos : [], 
		currentPP : {student:{}}, 
		previewUrl : "<s:url value='/upgrade/preview' />", 
		upgradeUrl : "<s:url value='/upgrade/upgrade' />" 
	}, 
	mounted : function() { 
		//this.getPPs(); 
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
		} ,
		genPreview : function(){
			var gradeIds=[];
			$("input[name='grade']").each(function(){
				if($(this).is(':checked')){
					gradeIds.push($(this).val());
				}
			});
			this.$http.get(this.previewUrl, {params:{ gradeIds : gradeIds.join(",") }}).then(function(response) { 
				//alert(JSON.stringify(response.body));
				this.upgradeVos=response.body; 
			});
		},
		submitUpgrade : function(){
			//alert(JSON.stringify(this.upgradeVos));
			/* this.$http.post(this.upgradeUrl, {params: this.upgradeVos,emulateJSON: true}).then(function(response) { 
				alert("success");
			}); */
			/* this.$http({
	            method:'POST',
	            url:this.upgradeUrl,
	            data:JSON.stringify(this.upgradeVos),
	            emulateJSON: true
	        }).then(function(data){
	            alert(data);
	        },function(error){
	         }) */
			$.ajax({ 
	            type:"POST", 
	            url:this.upgradeUrl, 
	            dataType:"json",      
	            contentType:"application/json",               
	            data:JSON.stringify(this.upgradeVos), 
	            success:function(data){ 
	            	alert(data);               
	            } 
	         }); 
		}
	} 
}); 
</script>
</body>
</html>