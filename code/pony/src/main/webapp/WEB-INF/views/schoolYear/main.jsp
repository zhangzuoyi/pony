<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学年管理</title>
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
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="setCurrent()" plain="true">设为当前</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="reload()" plain="true">刷新</a>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2"></table>
</div>
<!-- Begin of easyui-dialog -->
<div id="my-dialog-2" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:400px; padding:10px;">
	<form id="my-form-2" method="post">
		<input type="hidden" name="yearId" />
        <table>
            <tr>
                <td align="right">开始年份:</td>
                <td><input type="text" name="startYear" class="my-text" /></td>
            </tr>
            <tr>
                <td align="right">结束年份:</td>
                <td><input type="text" name="endYear" class="my-text" /></td>
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
			url:"<s:url value='/schoolYear/add' />",
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
	
	/**
	* Name 修改记录
	*/
	function edit(){
		$('#my-form-2').form('submit', {
			url:"<s:url value='/schoolYear/edit' />",
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
	
	/**
	* Name 删除记录
	*/
	function removeItem(){
		$.messager.confirm('信息提示','确定要删除该记录？', function(result){
			if(result){
				var item = $('#my-datagrid-2').datagrid('getSelected');
				$.ajax({
					url:"<s:url value='/schoolYear/delete' />",
					data:{id: item.yearId},
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
	
	function setCurrent(){
		$.messager.confirm('信息提示','确定要设置为当前学年吗？', function(result){
			if(result){
				var item = $('#my-datagrid-2').datagrid('getSelected');
				$.ajax({
					url:"<s:url value='/schoolYear/setCurrent' />",
					data:{id: item.yearId},
					method:"POST",
					success:function(data){
						if(data){
							$.messager.alert('信息提示','设置当前成功！','info');		
							 reload();
						}
						else
						{
							$.messager.alert('信息提示','设置当前失败！','info');		
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
			url:"<s:url value='/schoolYear/get' />",
			data:{id: item.yearId},
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
	$('#my-datagrid-2').datagrid({
		url:"<s:url value='/schoolYear/list' />",
		method:'get',
		rownumbers:true,
		singleSelect:true,
		fitColumns:true,
		fit:true,
		columns:[[
			/* { checkbox:true}, */
			{ field:'yearId',title:'ID',width:100,sortable:true},
			{ field:'name',title:'名称',width:180,sortable:true},
			{ field:'startYear',title:'开始年份',width:100},
			{ field:'endYear',title:'结束年份',width:100},
			{ field:'isCurrent',title:'是否当前',width:180,sortable:true,
				formatter:function(value,rec){
					if(rec.isCurrent == '0'){
						return "是";
					}else{
						return "否";
					}
				}
			}
		]]
	});
	
</script>
</body>
</html>