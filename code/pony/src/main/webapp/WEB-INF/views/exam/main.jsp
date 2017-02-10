<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试管理</title>
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
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAdd()" plain="true">添加</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-edit" onclick="openEdit()" plain="true">修改</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" onclick="removeItem()" plain="true">删除</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="reload()" plain="true">刷新</a>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2"></table>
</div>
<!-- Begin of easyui-dialog -->
<div id="my-dialog-2" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:400px; padding:10px;">
	<form id="my-form-2" method="post">
		<input type="hidden" name="examId" />
        <table>
        	<tr>
                <td width="60" align="right">学年:</td>
                <td>
                	<select name="schoolYear.yearId" class="my-select">
                		<c:forEach items="${years }" var="g">
                			<option value="${g.yearId }">${g.name }</option>
                		</c:forEach>
                	</select>
                </td>
            </tr>
            <tr>
                <td width="60" align="right">学期:</td>
                <td>
                	<select name="term.termId" class="my-select">
                		<c:forEach items="${terms }" var="g">
                			<option value="${g.termId }">${g.name }</option>
                		</c:forEach>
                	</select>
                </td>
            </tr>
            <tr>
                <td width="60" align="right">科目:</td>
                <td>
                	<select name="subject.subjectId" class="my-select">
                		<c:forEach items="${subjects }" var="g">
                			<option value="${g.subjectId }">${g.name }</option>
                		</c:forEach>
                	</select>
                </td>
            </tr>
            <tr>
                <td width="60" align="right">名称:</td>
                <td><input type="text" name="name" class="my-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">班级:</td>
                <td>
                	<input class="easyui-combobox" 
						name="classIds"
						data-options="
								url:'<s:url value='/schoolClass/list' />',
								method:'get',
								valueField:'classId',
								textField:'name',
								multiple:true,
								panelHeight:'auto'
						" />
                </td>
            </tr>
        </table>
    </form>
</div>
<!-- End of easyui-dialog -->
<script type="text/javascript">
	/**
	* Name 添加记录
	*/
	function add(){
		$('#my-form-2').form('submit', {
			url:"<s:url value='/exam/add' />",
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
	* Name 修改记录
	*/
	function edit(){
		$('#my-form-2').form('submit', {
			url:"<s:url value='/exam/edit' />",
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
					url:"<s:url value='/exam/delete' />",
					data:{id: item.examId},
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
			url:"<s:url value='/exam/get' />",
			data:{id: item.examId},
			dataType:'json',
			success:function(data){
				if(data){
					//绑定值
					var formdata={};
					formdata["examId"]=data.examId;
					formdata["name"]=data.name;
					formdata["schoolYear.yearId"]=data.schoolYear.yearId;
					formdata["term.termId"]=data.term.termId;
					formdata["subject.subjectId"]=data.subject.subjectId;
					var classes=data.schoolClasses;
					var clsIds=[];
					for(var i=0;i<classes.length;i++){
						clsIds[i]=classes[i].classId;
					}
					formdata["classIds"]=clsIds.join(",");
					$('#my-form-2').form('load', formdata);
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
	
	/**
	* Name 分页过滤器
	*/
	function pagerFilter(data){            
		if (typeof data.length == 'number' && typeof data.splice == 'function'){// is array                
			data = {                   
				total: data.length,                   
				rows: data               
			}            
		}        
		var dg = $(this);         
		var opts = dg.datagrid('options');          
		var pager = dg.datagrid('getPager');          
		pager.pagination({                
			onSelectPage:function(pageNum, pageSize){                 
				opts.pageNumber = pageNum;                   
				opts.pageSize = pageSize;                
				pager.pagination('refresh',{pageNumber:pageNum,pageSize:pageSize});                  
				dg.datagrid('loadData',data);                
			}          
		});           
		if (!data.originalRows){               
			data.originalRows = (data.rows);       
		}         
		var start = (opts.pageNumber-1)*parseInt(opts.pageSize);          
		var end = start + parseInt(opts.pageSize);        
		data.rows = (data.originalRows.slice(start, end));         
		return data;       
	}
	
	function reload(){
		$('#my-datagrid-2').datagrid('reload');
	}
	
	/**
	* Name 载入数据
	*/
	$('#my-datagrid-2').datagrid({
		url:"<s:url value='/exam/list' />",
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
			/* { checkbox:true}, */
			{ field:'examId',title:'ID',width:100,sortable:true},
			{ field:'name',title:'名称',width:180,sortable:true},
			{ field:'subject',title:'科目',width:180,sortable:true,
				formatter:function(value,rec){
				   return rec.subject.name;
				}
			},
			{ field:'schoolYear',title:'学年',width:180,sortable:true,
				formatter:function(value,rec){
				   return rec.schoolYear.name;
				}
			},
			{ field:'term',title:'学期',width:180,sortable:true,
				formatter:function(value,rec){
				   return rec.term.name;
				}
			},
			{ field:'schoolClasses',title:'班级',width:180,sortable:true,
				formatter:function(value,rec){
					var len=rec.schoolClasses.length;
					var results=[];
					for(var i=0;i<len;i++){
						results[i]=rec.schoolClasses[i].name;
					}
				   return results.join(",");
				}
			}
		]]
	});
	
</script>
</body>
</html>