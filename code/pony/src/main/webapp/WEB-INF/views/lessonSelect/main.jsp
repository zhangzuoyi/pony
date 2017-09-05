<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选课管理</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/dateFormat.js' />"></script>
</head>
<body class="easyui-layout">
<div class="easyui-layout" data-options="fit:true">
    <!-- Begin of toolbar -->
    <div id="my-toolbar-2">
        <div class="my-toolbar-button">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAdd()" plain="true">选课</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" onclick="removeItem()" plain="true">删除</a>
        </div>
        <div class="my-toolbar-search">
            <label>年级：</label> 
            <select id="gradeSelect" name="grade" class="my-select" panelHeight="auto" style="width:100px">
                <option value="">请选择</option>
                <c:forEach items="${grades }" var="g">
          			<option value="${g.gradeId }">${g.name }</option>
          		</c:forEach>
            </select>
            <label>班级：</label> 
            <select name="schoolClass" class="my-select" panelHeight="auto" style="width:100px">
            </select>
            <label>学生：</label> 
            <select name="student" class="my-select" panelHeight="auto" style="width:100px">
            </select>
            <a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2">
    	 <thead> 
            <tr> 
                <th data-options="field:'arrangeId',width:100">ID</th> 
                <th data-options="field:'subjectName',width:100">科目</th> 
                <th data-options="field:'teacherName',width:100">老师</th> 
                <th data-options="field:'classroom',width:100">上课地点</th> 
                <th data-options="field:'period',width:100">课时</th> 
                <th data-options="field:'credit',width:100">学分</th> 
                <th data-options="field:'timesStr',width:100">时间安排</th> 
            </tr> 
        </thead> 
    </table>
</div>
<!-- Begin of easyui-dialog -->
<div id="my-dialog-2" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:600px;height:400px; padding:10px;">
	<table id="my-datagrid-3" class="easyui-datagrid"></table>
</div>
<!-- End of easyui-dialog -->
<script type="text/javascript" src="<s:url value='/static/easyui/datagrid-cellediting.js' />"></script>
<script type="text/javascript">
	var selectStudentId;//保存当前选中学生ID
	$(document).ready(function(){
		$("#gradeSelect").change(function(){
			var grade=$(this).children('option:selected').val();
			if(grade == ""){
				$("select[name='schoolClass']").empty();
				$("select[name='student']").empty();
			}else{
				$.ajax({
					url:"<s:url value='/schoolClass/findByGrade' />",
					data:{gradeId: grade},
					method:"GET",
					dataType:"json",
					success:function(data){
						$("select[name='schoolClass']").empty();
						$("select[name='schoolClass']").append("<option value=''>请选择</option>");
						var len=data.length;
						for(var i=0;i<len;i++){
							var item=data[i];
							$("select[name='schoolClass']").append("<option value='"+item.classId+"'>"+item.name+"</option>");
						}
					}	
				});
			}
		});
		$("select[name='schoolClass']").change(function(){
			var schoolClass=$(this).children('option:selected').val();
			if(schoolClass == ""){
				$("select[name='student']").empty();
			}else{
				$.ajax({
					url:"<s:url value='/student/findByClass' />",
					data:{classId: schoolClass},
					method:"GET",
					dataType:"json",
					success:function(data){
						$("select[name='student']").empty();
						$("select[name='student']").append("<option value=''>请选择</option>");
						var len=data.length;
						for(var i=0;i<len;i++){
							var item=data[i];
							$("select[name='student']").append("<option value='"+item.studentId+"'>"+item.name+"</option>");
						}
					}	
				});
			}
		});
	});
	/**
	* Name 添加记录
	*/
	function add(){
		$('#my-form-2').form('submit', {
			url:"<s:url value='/examResult/upload' />",
			success:function(data){
				if(data){
					$.messager.alert('信息提示','提交成功！','info');
					$("#searchButton").click();
					$('#my-dialog-2').dialog('close');
				}
				else
				{
					$.messager.alert('信息提示','提交失败！','info');
				}
			}
		});
	}
	
	/**
	* Name 修改记录
	*/
	function edit(){
		$('#my-form-2').form('submit', {
			url:"<s:url value='/grade/edit' />",
			success:function(data){
				if(data){
					$.messager.alert('信息提示','提交成功！','info');
					$('#my-dialog-2').dialog('close');
				}
				else
				{
					$.messager.alert('信息提示','提交失败！','info');
				}
			}
		});
	}
	
	/**
	* Name 删除记录
	*/
	function removeItem(){
		$.messager.confirm('信息提示','确定要删除该记录？', function(result){
			if(result){
				var item = $('#my-datagrid-2').datagrid('getSelected');
				$.ajax({
					url:"<s:url value='/lessonSelect/deleteSelect' />",
					data:{studentId:selectStudentId, arrangeId: item.arrangeId},
					method:"POST",
					dataType:"json",
					success:function(data){
						if(data){
							$.messager.alert('信息提示','删除成功！','info');		
							$('#my-datagrid-2').datagrid({
								data: data
							});
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
	
	/**
	* Name 打开添加窗口
	*/
	function openAdd(){
		var studentId=$("select[name='student']").children('option:selected').val();
		if(studentId==null || studentId == ""){
			alert("请选择学生");
			return;
		}
		$('#my-datagrid-3').datagrid({
			url:"<s:url value='/lessonSelect/lessonForSelect' />",
			queryParams:{studentId: studentId},
			method:'get',
			rownumbers:true,
			singleSelect:true,
			multiSort:true,
			fitColumns:true,
			fit:true,
			columns:[[
				{ field:'arrangeId',title:'ID',width:100,sortable:true},
				{ field:'subjectName',title:'科目',width:180,sortable:true},
				{ field:'teacherName',title:'老师',width:180,sortable:true},
				{ field:'classroom',title:'上课地点',width:180,sortable:true},
				{ field:'period',title:'课时',width:180,sortable:true},
				{ field:'credit',title:'学分',width:180,sortable:true},
				{ field:'gradesStr',title:'可选年级',width:180,sortable:true},
				{ field:'timesStr',title:'时间安排',width:180,sortable:true}
			]],
			onDblClickRow:function(rowIndex, rowData){
				$.ajax({
					url:"<s:url value='/lessonSelect/selectLesson' />",
					data:{studentId: studentId, arrangeId: rowData.arrangeId},
					method:"POST",
					dataType:"json",
					success:function(data){
						if(data){
							$('#my-datagrid-2').datagrid({
								data: data
							});
							$.messager.alert('信息提示','选课成功！','info');		
						}
						else
						{
							$.messager.alert('信息提示','选课失败！','info');		
						}
					}	
				});
				$('#my-dialog-2').dialog('close');
			}
		});
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
	
	/**
	* Name 打开修改窗口
	*/
	function openEdit(){
		$('#my-form-2').form('clear');
		var item = $('#my-datagrid-2').datagrid('getSelected');
		//alert(item.productid);return;
		$.ajax({
			url:"<s:url value='/grade/get' />",
			data:{id: item.gradeId},
			dataType:'json',
			success:function(data){
				if(data){
					//绑定值
					$('#my-form-2').form('load', data);
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
	
	function reload(){
		$('#my-datagrid-2').datagrid('reload');
	}
	
	$("#searchButton").click(function(){
		var studentId=$("select[name='student']").children('option:selected').val();
		if(studentId==null || studentId == ""){
			alert("请选择学生");
			return;
		}
		selectStudentId=studentId;
		$.ajax({
			url:"<s:url value='/lessonSelect/lessonSelected' />",
			data:{studentId: studentId},
			method:"GET",
			dataType:"json",
			success:function(data){
				$('#my-datagrid-2').datagrid({
					data: data
				});
			}	
		});
		
	});
	
	function saveResult(){
		var cell=$("#my-datagrid-2").datagrid("cell"); 
		$("#my-datagrid-2").datagrid("endEdit",cell.index); 
		var updated = $("#my-datagrid-2").datagrid('getChanges', "updated"); 
		if(updated.length>0){ 
			/* alert(JSON.stringify(updated)); 
			for(var u in updated){ 
				alert(updated[u].studentId+":"+updated[u].score); 
			}  */
			$.ajax({
				headers: {
	                'Accept': 'application/json',
	                'Content-Type': 'application/json'
	            },
				url:"<s:url value='/examResult/save' />",
				data:JSON.stringify(updated),
				type:"POST",
				dataType:'json',
				success:function(data){
					alert(data);
				}	
			});
		}
	}
	
</script>
</body>
</html>