<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生管理</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/dateFormat.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/datagrid-cellediting.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue-resource.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/js/moment.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/myfilters.js' />"></script>
</head>
<body class="easyui-layout">
<div class="easyui-layout" data-options="fit:true" id="app">
    <!-- Begin of toolbar -->
    <div id="my-toolbar-2">
        <div class="my-toolbar-button">
        	<a href="#" class="easyui-menubutton" data-options="menu:'#mm2'">状态变更</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-edit" onclick="openEdit()" plain="true">修改</a>
            <!-- <a href="#" class="easyui-linkbutton" iconCls="icon-remove" onclick="removeItem()" plain="true">删除</a> -->
            <a href="#" class="easyui-linkbutton" iconCls="icon-edit" onclick="openSetSubjects()" plain="true">设置考试科目</a>
        </div>
        <div id="mm2" style="width:100px;">
        	<div onclick="openChangeStatus('在读')">在读</div>
        	<div onclick="openChangeStatus('不参评')">不参评</div>
	        <div onclick="openChangeStatus('开除')">开除</div>
	        <div onclick="openChangeStatus('辍学')">辍学</div>
	        <div onclick="openChangeStatus('退学')">退学</div>
	        <div onclick="openChangeStatus('肄业')">肄业</div>
	        <div onclick="openChangeStatus('休学')">休学</div>
	        <div onclick="openChangeStatus('死亡')">死亡</div>
	        <div onclick="openChangeStatus('转出')">转出</div>
	        <div onclick="openChangeStatus('借出')">借出</div>
	    </div>
        <div class="my-toolbar-search">
        	<label>年级：</label> 
            <select name="gradeId" v-model="gradeId" v-on:change="changeGrade()" class="my-select" panelHeight="auto" style="width:100px">
            	<option v-for="g in grades" v-bind:value="g.gradeId">{{g.name}}</option>
            </select>
            <label>班级：</label> 
            <select name="schoolClass" v-model="classId" class="my-select" panelHeight="auto" style="width:100px">
            	<option v-for="g in schoolClasses" v-bind:value="g.classId">{{g.name}}</option>
            </select>
            <a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2" data-options="fitColumns:true,fit:true">
    	<thead> 
            <tr> 
                <th data-options="field:'studentNo',width:100">学号</th> 
                <th data-options="field:'name',width:100">姓名</th> 
                <th data-options="field:'sex',width:100,formatter:sexFormatter">性别</th> 
                <th data-options="field:'birthday',width:100">生日</th> 
                <th data-options="field:'nativePlace',width:100">籍贯</th> 
                <th data-options="field:'phone',width:100">联系电话</th> 
                <th data-options="field:'entranceDate',width:100">入学日期</th> 
                <th data-options="field:'entranceType',width:100,formatter:entranceTypeFormat">学生类型</th> 
                <th data-options="field:'status',width:100,formatter:statusFormat">状态</th> 
                <th data-options="field:'examSubjects',width:100">考试科目</th> 
                <!-- <th data-options="field:'schoolClass',width:100,formatter:classFormatter">班级</th>  -->
            </tr> 
        </thead>
    </table>
</div>
<!-- Begin of easyui-dialog -->
<div id="my-dialog-2" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:800px; padding:10px;">
	<form id="my-form-2" method="post">
		<input type="hidden" name="studentId" />
        <table>
            <tr>
                <td width="60" align="right">学号:</td>
                <td><input type="text" name="studentNo" class="my-text" /></td>
                <td width="60" align="right">姓名:</td>
                <td><input type="text" name="name" class="my-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">性别:</td>
                <td>
                	<select name="sex" class="my-select">
                		<c:forEach items="${sexes }" var="g">
                			<option value="${g.code }">${g.value }</option>
                		</c:forEach>
                	</select>
                </td>
                <td width="60" align="right">出生日期:</td>
                <td><input type="text" name="birthday"  class="my-text easyui-datebox" data-options="formatter:myformatter,parser:myparser" /></td>
            </tr>
            <tr>
                <td width="60" align="right">证件类型:</td>
                <td>
                	<select name="idType" class="my-select">
                		<c:forEach items="${credentials }" var="g">
                			<option value="${g.code }">${g.value }</option>
                		</c:forEach>
                	</select>
                </td>
                <td width="60" align="right">证件号码:</td>
                <td><input type="text" name="idNo" class="my-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">民族:</td>
                <td><input type="text" name="nation" class="my-text" /></td>
                <td width="60" align="right">籍贯:</td>
                <td><input type="text" name="nativePlace" class="my-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">户口地址:</td>
                <td colspan="3"><input type="text" name="nativeAddr" class="my-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">家庭住址:</td>
                <td colspan="3"><input type="text" name="homeAddr" class="my-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">邮编:</td>
                <td><input type="text" name="homeZipcode" class="my-text" /></td>
                <td width="60" align="right">联系电话:</td>
                <td><input type="text" name="phone" class="my-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">Email:</td>
                <td colspan="3"><input type="text" name="email" class="my-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">入学日期:</td>
                <td><input type="text" name="entranceDate"  class="my-text easyui-datebox" data-options="formatter:myformatter,parser:myparser" /></td>
                <td width="60" align="right">学生类型:</td>
                <td>
                	<select name="entranceType" class="my-select">
                		<c:forEach items="${studentTypes }" var="g">
                			<option value="${g.code }">${g.value }</option>
                		</c:forEach>
                	</select>
                </td>
            </tr>
            <%-- <tr>
                <td width="60" align="right">毕业日期:</td>
                <td><input type="text" name="graduateDate"  class="my-text easyui-datebox" data-options="formatter:myformatter,parser:myparser" /></td>
                <td width="60" align="right">毕业类型:</td>
                <td>
                	<select name="graduateType" class="my-select">
                	</select>
                </td>
            </tr> --%>
            <tr>
                <td width="60" align="right">班级:</td>
                <td colspan="3">
                	<select name="schoolClass.classId" class="my-select">
                		<c:forEach items="${classes }" var="g">
                			<option value="${g.classId }">${g.name }</option>
                		</c:forEach>
                	</select>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="my-dialog-3" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:400px; padding:10px;">
	<form id="my-form-3" method="post" enctype="multipart/form-data">
        <table>
            <tr>
                <td width="60" align="right">班级:</td>
                <td><input type="hidden" name="classId" /><input type="text" readonly="readonly" name="className" class="my-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">文件:</td>
                <td><input type="file" name="file" /></td>
            </tr>
        </table>
    </form>
</div>
<div id="my-dialog-4" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:800px; padding:10px;">
	<form id="my-form-4" method="post">
		<!-- <input type="hidden" name="studentId" /> -->
		<input type="hidden" name="studentIds" />
        <table>
            <tr>
                <td width="60" align="right">学号:</td>
                <td colspan="3"><input type="text" name="studentNo" readonly="readonly" class="my-text" style="width:400px;" /></td>
            </tr>
            <tr>
                <td width="60" align="right">姓名:</td>
                <td colspan="3"><input type="text" name="name" readonly="readonly" class="my-text" style="width:400px;" /></td>
            </tr>
            <tr>
                <td width="60" align="right">变动类型:</td>
                <td><input type="text" name="changeType" readonly="readonly" class="my-text" /></td>
                <td width="60" align="right">发生日期:</td>
                <td><input type="text" name="occurDate" class="my-text easyui-datebox" data-options="formatter:myformatter,parser:myparser" /></td>
            </tr>
            <tr>
                <td width="60" align="right">说明:</td>
                <td colspan="3">
                	<textarea rows="3" style="width:500px;" name="description"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="my-dialog-5" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:800px; padding:10px;">
	<form id="my-form-5" method="post" >
        <table>
            <tr>
                <td width="60" align="right">科目:</td>
                <td>
                	<c:forEach items="${subjects }" var="g">
                		<input type="checkbox" name="subjects" value="${g.name }" />${g.name }
                	</c:forEach>
                </td>
            </tr>
            <tr>
                <td width="60" align="right"></td>
                <td><input type="hidden" name="studentIds" /></td>
            </tr>
        </table>
    </form>
</div>
<!-- End of easyui-dialog -->
<script type="text/javascript">
	function add(){
		$('#my-form-2').form('submit', {
			url:"<s:url value='/studentAdmin/add' />",
			success:function(data){
				if(data){
					$.messager.alert('信息提示','提交成功！','info');
					reload();
					$('#my-dialog-2').dialog('close');
				}
				else
				{
					$.messager.alert('信息提示','提交失败！','info');
				}
			}
		});
	}
	
	function edit(){
		$('#my-form-2').form('submit', {
			url:"<s:url value='/studentAdmin/edit' />",
			success:function(data){
				if(data){
					$.messager.alert('信息提示','提交成功！','info');
					reload();
					$('#my-dialog-2').dialog('close');
				}
				else
				{
					$.messager.alert('信息提示','提交失败！','info');
				}
			}
		});
	}
	
	function upload(){
		$('#my-form-3').form('submit', {
			url:"<s:url value='/studentAdmin/upload' />",
			success:function(data){
				if(data){
					$.messager.alert('信息提示','提交成功！','info');
					$("#searchButton").click();
					$('#my-dialog-3').dialog('close');
				}
				else
				{
					$.messager.alert('信息提示','提交失败！','info');
				}
			}
		});
	}
	
	function removeItem(){
		$.messager.confirm('信息提示','确定要删除该记录？', function(result){
			if(result){
				var item = $('#my-datagrid-2').datagrid('getSelected');
				$.ajax({
					url:"<s:url value='/studentAdmin/delete' />",
					data:{id: item.studentId},
					method:"POST",
					success:function(data){
						if(data){
							$.messager.alert('信息提示','删除成功！','info');		
							reload();
						}
						else
						{
							$.messager.alert('信息提示','删除失败！','info');		
						}
					}	
				});
			}	
		});
	}
	
	function openAdd(){
		$('#my-form-2').form('clear');
		$('#my-dialog-2').dialog({
			closed: false,
			modal:true,
            title: "添加信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: add
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#my-dialog-2').dialog('close');                    
                }
            }]
        });
	}
	
	function openEdit(){
		$('#my-form-2').form('clear');
		var item = $('#my-datagrid-2').datagrid('getSelected');
		//alert(item.productid);return;
		$.ajax({
			url:"<s:url value='/studentAdmin/get' />",
			data:{id: item.studentId},
			dataType:'json',
			success:function(data){
				if(data){
					//绑定值
					data['schoolClass.classId']=data.schoolClass.classId;
					$('#my-form-2').form('load', data);
					//$("select[name='schoolClass.classId'] option[value='"+data.schoolClass.classId+"']").attr("selected","selected");
				}
				else{
					$('#my-dialog-2').dialog('close');
				}
			}	
		});
		$('#my-dialog-2').dialog({
			closed: false,
			modal:true,
            title: "修改信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: edit
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#my-dialog-2').dialog('close');                    
                }
            }]
        });
	}	
	
	function openUpload(){
		$('#my-form-3').form('clear');
		var classId=$("select[name='schoolClass']").children('option:selected').val();
		var className=$("select[name='schoolClass']").children('option:selected').html();
		$('#my-form-3').find("input[name='classId']").val(classId);
		$('#my-form-3').find("input[name='className']").val(className);
		$('#my-dialog-3').dialog({
			closed: false,
			modal:true,
            title: "导入学生",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: upload
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#my-dialog-3').dialog('close');                    
                }
            }]
        });
	}
	
	function openSetSubjects(){
		$('#my-form-5').form('clear');
		var items = $('#my-datagrid-2').datagrid('getSelections');
		if(items.length == 0){
			$.messager.alert('信息提示','请选择学生！','info');
			return;
		}
		var studentIds=[];
		for(var i=0;i<items.length;i++){
			studentIds.push(items[i].studentId);
		}
		$("input[name='studentIds']").val(studentIds.join(","));
		$('#my-dialog-5').dialog({
			closed: false,
			modal:true,
            title: "设置考试科目",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: submitSetSubjects
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#my-dialog-5').dialog('close');                    
                }
            }]
        });
	}
	function submitSetSubjects(){
		$('#my-form-5').form('submit', {
			url:"<s:url value='/studentAdmin/setSubjects' />",
			success:function(data){
				if(data){
					$.messager.alert('信息提示','提交成功！','info');
					reload();
					$('#my-dialog-5').dialog('close');
				}
				else
				{
					$.messager.alert('信息提示','提交失败！','info');
				}
			}
		});
	}
	function openChangeStatus(ct){
		//var item = $('#my-datagrid-2').datagrid('getSelected');
		var rows = $('#my-datagrid-2').datagrid('getSelections');
		if(rows.length == 0){
			$.messager.alert('信息提示','请选择一条或多条学生记录！','info');
			return;
		}
		var studentIds=[];
		var studentNos=[];
		var studentNames=[];
		for(var i=0; i<rows.length; i++){
            var row = rows[i];
            studentIds.push(row.studentId);
            studentNos.push(row.studentNo);
            studentNames.push(row.name);
        }
		$('#my-form-4').form('clear');
		//$('#my-form-4').find("input[name='studentId']").val(item.studentId);
		//$('#my-form-4').find("input[name='studentNo']").val(item.studentNo);
		//$('#my-form-4').find("input[name='name']").val(item.name);
		$('#my-form-4').find("input[name='studentIds']").val(studentIds.join());
		$('#my-form-4').find("input[name='studentNo']").val(studentNos.join());
		$('#my-form-4').find("input[name='name']").val(studentNames.join());
		$('#my-form-4').find("input[name='changeType']").val(ct);
		$('#my-dialog-4').dialog({
			closed: false,
			modal:true,
            title: "添加信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: changeStatus
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#my-dialog-4').dialog('close');                    
                }
            }]
        });
	}
	
	function changeStatus(){
		if( ! $("input[name='occurDate']").val()){
			$.messager.alert('信息提示','请填写发生日期！','info');
			return;
		}
		$('#my-form-4').form('submit', {
			url:"<s:url value='/studentAdmin/changeStatus' />",
			success:function(data){
				if(data){
					$.messager.alert('信息提示','提交成功！','info');
					reload();
					$('#my-dialog-4').dialog('close');
				}
				else
				{
					$.messager.alert('信息提示','提交失败！','info');
				}
			}
		});
	}
	
	function reload(){
		//$('#my-datagrid-2').datagrid('reload');
		$("#searchButton").click();
	}
	
	function classFormatter(value,rec){
		if(rec.schoolClass){
			return rec.schoolClass.name;
		}else{
			return "";
		}
	}
	function sexFormatter(value,rec){
		if(rec.sex == 'M'){
			return "男";
		}else if(rec.sex == 'F'){
			return "女";
		}
		return "";
	}
	var entranceTypes={'0':'统招','1':'转入','2':'借入'};
	function entranceTypeFormat(value,rec){
		
		return entranceTypes[rec.entranceType];
	}

	var statusList={'0':'在读','1':'毕业','2':'开除','3':'退学','4':'辍学','5':'肄业','6':'转出','7':'借出','8':'休学','9':'死亡','10':'不参评'};
	function statusFormat(value,rec){
		
		return statusList[rec.status];
	}
	
	$(document).ready(function(){
		$("#searchButton").click(function(){
			var classId=$("select[name='schoolClass']").children('option:selected').val();
			if(classId){
				$.ajax({
					url:"<s:url value='/studentAdmin/findByClass' />",
					data:{classId: classId},
					method:"GET",
					dataType:"json",
					success:function(data){
						$('#my-datagrid-2').datagrid({
							data: data
						});
						
					}	
				});
			}else{
				alert("empty");
			}
			
		});
		//$("#searchButton").click();
	});
	var app = new Vue({ 
		el : '#app', 
		data : { 
			condition : {}, 
			grades : [],
			allClasses : [],
			schoolClasses : [],
			gradeId : null,
			classId : null,
			getGradesUrl : "<s:url value='/grade/list' />", 
			getAllClassUrl : "<s:url value='/schoolClass/list' />"
		}, 
		mounted : function() { 
			this.getGrades();
			//this.gradeId=this.grades[0].gradeId;
			this.getAllClasses();
		}, 
		methods : { 
			getGrades : function(){
				this.$http.get(this.getGradesUrl).then(
					function(response){
						this.grades=response.body;
						//this.gradeId=this.grades[0].gradeId;
					}		
				);
			},
			getAllClasses : function(){
				this.$http.get(this.getAllClassUrl).then(
					function(response){
						this.allClasses=response.body;
					}		
				);
			},
			changeGrade : function(){
				this.schoolClasses=[];
				for(var i=0;i<this.allClasses.length;i++){
					if(this.gradeId == this.allClasses[i].grade.gradeId){
						this.schoolClasses.push(this.allClasses[i]);
					}
				}
			},
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