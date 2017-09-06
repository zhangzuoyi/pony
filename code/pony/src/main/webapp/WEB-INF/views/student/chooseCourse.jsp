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
        <!-- <div class="my-toolbar-button">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAdd()" plain="true">选课</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" onclick="removeItem()" plain="true">删除</a>
        </div> -->
    </div>
    <!-- End of toolbar -->
    <table id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2">
    	 <!-- <thead> 
            <tr> 
                <th data-options="field:'arrangeId',width:100">ID</th> 
                <th data-options="field:'subjectName',width:100">科目</th> 
                <th data-options="field:'teacherName',width:100">老师</th> 
                <th data-options="field:'classroom',width:100">上课地点</th> 
                <th data-options="field:'period',width:100">课时</th> 
                <th data-options="field:'credit',width:100">学分</th> 
                <th data-options="field:'timesStr',width:100">时间安排</th>
                <th data-options="field:'choose',width:100,formatter:isChooseValue">是否已选</th>
                <th data-options="field:'choose',width:100,formatter:chooseOperation">操作</th>
            </tr> 
        </thead>  -->
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
	
	function reload(){
		$('#my-datagrid-2').datagrid('reload');
	}

	$('#my-datagrid-2').datagrid({
		url:"<s:url value='/student/chooseCourseList' />",
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
			{ field:'timesStr',title:'时间安排',width:180,sortable:true},
			{ field:'choose',title:'是否已选',width:180,sortable:true,
				formatter:function(value,rec){
					if(rec.choose == false){
						return '否';
					}
				   return '是';
				}
			},
			{ field:'studentId',title:'操作',width:180,
				formatter:function(value,rec){
					if(rec.choose == false){
						return '<a href="javascript:void(0)" class="easyui-linkbutton" onclick="chooseLesson(\''+rec.arrangeId+'\',\''+rec.studentId+'\')" style="width:80px">选择</a>';
					}
				   return '<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cancelChoose(\''+rec.arrangeId+'\',\''+rec.studentId+'\')" style="width:80px">取消选择</a>';
				}
			}
		]]
	});
	
	/* function chooseOperation(value,rec){
		if(rec.choose == false){
			return '<a href="javascript:void(0)" class="easyui-linkbutton" onclick="chooseLesson(\''+rec.arrangeId+'\',\''+rec.studentId+'\')" style="width:80px">选择</a>';
		}
	   return '<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cancelChoose(\''+rec.arrangeId+'\',\''+rec.studentId+'\')" style="width:80px">取消选择</a>';
	}
	function isChooseValue(value,rec){
		if(rec.choose == false){
			return '否';
		}
	   return '是';
	} */
	function chooseLesson(arrangeId,studentId){
		$.ajax({
			url:"<s:url value='/lessonSelect/selectLesson' />",
			data:{studentId: studentId, arrangeId: arrangeId},
			method:"POST",
			dataType:"json",
			success:function(data){
				if(data){
					reload();
					$.messager.alert('信息提示','选课成功！','info');		
				}
				else
				{
					$.messager.alert('信息提示','选课失败！','info');		
				}
			}	
		});
	}
	function cancelChoose(arrangeId,studentId){
		$.ajax({
			url:"<s:url value='/lessonSelect/deleteSelect' />",
			data:{studentId: studentId, arrangeId: arrangeId},
			method:"POST",
			dataType:"json",
			success:function(data){
				if(data){
					reload();
					$.messager.alert('信息提示','取消选课成功！','info');		
				}
				else
				{
					$.messager.alert('信息提示','取消选课失败！','info');		
				}
			}	
		});
	}
</script>
</body>
</html>