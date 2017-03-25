<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年级管理</title>
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
		<input type="hidden" name="gradeId" />
        <table>
            <tr>
                <td width="60" align="right">名称:</td>
                <td><input type="text" name="name" class="my-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">序号:</td>
                <td><input type="text" name="seq" class="my-text" /></td>
            </tr>
        </table>
    </form>
</div>
<!-- End of easyui-dialog -->
<script type="text/javascript">
	function add(){
		$('#my-form-2').form('submit', {
			url:"<s:url value='/grade/add' />",
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
			url:"<s:url value='/grade/edit' />",
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
	
	$('#my-datagrid-2').datagrid({
		url:"<s:url value='/grade/list' />",
		method:'get',
		rownumbers:true,
		singleSelect:true,
		pageSize:20,           
		pagination:true,
		multiSort:true,
		fitColumns:true,
		fit:true,
		columns:[[
			/* { checkbox:true}, */
			{ field:'gradeId',title:'ID',width:100,sortable:true},
			{ field:'name',title:'名称',width:180,sortable:true},
			{ field:'seq',title:'序号',width:180,sortable:true}
		]]
	});
	
</script>
</body>
</html>