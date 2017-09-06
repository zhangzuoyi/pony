<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩管理</title>
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
            <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="saveResult()" plain="true">保存</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAdd()" plain="true">导入</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="exportResult()" plain="true">导出</a>
        </div>
        <div class="my-toolbar-search">
            <label>科目：</label> 
            <select id="subjectSelect" name="subject" class="my-select" panelHeight="auto" style="width:100px">
                <option value="">请选择</option>
                <c:forEach items="${subjects }" var="g">
          			<option value="${g.subjectId }">${g.name }</option>
          		</c:forEach>
            </select>
            <label>班级：</label> 
            <select name="schoolClass" class="my-select" panelHeight="auto" style="width:100px">
            </select>
            <label>考试：</label> 
            <select name="exam" class="my-select" panelHeight="auto" style="width:100px">
            </select>
            <a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2">
    	 <thead> 
            <tr> 
                <th data-options="field:'studentNo',width:100">学号</th> 
                <th data-options="field:'studentName',width:100">姓名</th> 
                <th data-options="field:'subjectName',width:100">科目</th> 
                <th data-options="field:'score',width:80,align:'right',editor:'numberbox'">成绩</th> 
            </tr> 
        </thead> 
    </table>
</div>
<!-- Begin of easyui-dialog -->
<div id="my-dialog-2" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:400px; padding:10px;">
	<form id="my-form-2" method="post" enctype="multipart/form-data">
        <table>
            <tr>
                <td width="60" align="right">科目:</td>
                <td><input type="hidden" name="subjectId" /><input type="text" name="subjectName" class="my-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">考试:</td>
                <td><input type="hidden" name="examId" /><input type="text" name="examName" class="my-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">班级:</td>
                <td><input type="hidden" name="classId" /><input type="text" name="className" class="my-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">文件:</td>
                <td><input type="file" name="file" /></td>
            </tr>
        </table>
    </form>
</div>
<!-- End of easyui-dialog -->
<script type="text/javascript" src="<s:url value='/static/easyui/datagrid-cellediting.js' />"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#subjectSelect").change(function(){
			var subject=$(this).children('option:selected').val();
			if(subject == ""){
				$("select[name='exam']").empty();
				$("select[name='schoolClass']").empty();
			}else{
				$.ajax({
					url:"<s:url value='/teacher/findClassBySubject' />",
					data:{subjectId: subject},
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
			var subject=$("#subjectSelect").children('option:selected').val();
			if(schoolClass == ""){
				$("select[name='exam']").empty();
			}else{
				$.ajax({
					url:"<s:url value='/exam/findBySubjectAndClass' />",
					data:{classId: schoolClass, subjectId: subject},
					method:"GET",
					dataType:"json",
					success:function(data){
						$("select[name='exam']").empty();
						$("select[name='exam']").append("<option value=''>请选择</option>");
						var len=data.length;
						for(var i=0;i<len;i++){
							var item=data[i];
							$("select[name='exam']").append("<option value='"+item.examId+"'>"+item.name+"</option>");
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
					url:"<s:url value='/grade/delete' />",
					data:{id: item.gradeId},
					method:"POST",
					success:function(data){
						if(data){
							$.messager.alert('信息提示','删除成功！','info');		
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
		$('#my-form-2').form('clear');
		var subjectId=$("select[name='subject']").children('option:selected').val();
		var subjectName=$("select[name='subject']").children('option:selected').html();
		var examId=$("select[name='exam']").children('option:selected').val();
		var examName=$("select[name='exam']").children('option:selected').html();
		var classId=$("select[name='schoolClass']").children('option:selected').val();
		var className=$("select[name='schoolClass']").children('option:selected').html();
		$('#my-form-2').find("input[name='subjectName']").val(subjectName);
		$('#my-form-2').find("input[name='subjectId']").val(subjectId);
		$('#my-form-2').find("input[name='examId']").val(examId);
		$('#my-form-2').find("input[name='examName']").val(examName);
		$('#my-form-2').find("input[name='classId']").val(classId);
		$('#my-form-2').find("input[name='className']").val(className);
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
	* 导出成绩
	*/
	function exportResult(){
		var examId=$("select[name='exam']").children('option:selected').val();
		var classId=$("select[name='schoolClass']").children('option:selected').val();
		window.open("<s:url value='/examResult/export' />?examId="+examId+"&classId="+classId);
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
	
	/**
	* Name 载入数据
	*/
	/* $('#my-datagrid-2').datagrid({
		url:"<s:url value='/grade/list' />",
		method:'get',
		loadFilter:pagerFilter,		
		rownumbers:true,
		singleSelect:true,
		pageSize:20,           
		pagination:true,
		multiSort:true,
		fitColumns:true,
		fit:true,
		columns:[[
			{ field:'gradeId',title:'ID',width:100,sortable:true},
			{ field:'name',title:'名称',width:180,sortable:true}
		]]
	}); */
	$("#searchButton").click(function(){
		var examId=$("select[name='exam']").children('option:selected').val();
		var classId=$("select[name='schoolClass']").children('option:selected').val();
		if(classId){
			$.ajax({
				url:"<s:url value='/examResult/findByClass' />",
				data:{classId: classId, examId: examId},
				method:"GET",
				dataType:"json",
				success:function(data){
					/* var len=data.length;
					var mydata=[];
					for(var i=0;i<len;i++){
						mydata[i]={};
						var item=data[i];
						mydata[i].studentId=item.studentId;
						mydata[i].studentNo=item.studentNo;
						mydata[i].name=item.name;
						//mydata[i].score=0;
					} */
					$('#my-datagrid-2').datagrid({
						data: data
					});
					$('#my-datagrid-2').datagrid('enableCellEditing');
				}	
			});
			/* var data=[
				{f1:'value11', f2:'value12'},
				{f1:'value21', f2:'value22'}
			];
			$('#my-datagrid-2').datagrid({
				data: data,
				columns:[[
					{ field:'f1',title:'ID',width:100,sortable:true},
					{ field:'f2',title:'名称',width:180,sortable:true}
				]]
			}); */
		}else{
			alert("empty");
		}
		
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