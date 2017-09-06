<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>消息管理</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/dateFormat.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/nicedit/nicEdit.js' />"></script>
</head>
<body class="easyui-layout">
<div class="easyui-layout" data-options="fit:true">
    <!-- Begin of toolbar -->
    <div id="my-toolbar-2">
        <div class="my-toolbar-button">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAdd()" plain="true">添加</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-edit" onclick="openReceiversSelect()" plain="true">修改</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" onclick="removeItem()" plain="true">删除</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="reload()" plain="true">刷新</a>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2"></table>
</div>
<!-- Begin of easyui-dialog -->
<div id="my-dialog-2" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:800px; padding:10px;">
	<form id="my-form-2" method="post" enctype="multipart/form-data">
		<input type="hidden" name="targetGroups" />
		<input type="hidden" name="targetUsers" />
        <table>
        	<tr>
                <td width="60" align="right">接收人:</td>
                <td>
                	<input type="text" name="receivers" class="my-text" onclick="openReceiversSelect()" />
                </td>
            </tr>
            <tr>
                <td width="60" align="right">标题:</td>
                <td>
                	<input type="text" name="title" class="my-text" />
                </td>
            </tr>
            <tr>
                <td width="60" align="right">内容:</td>
                <td>
                	<textarea id="textarea1" name="content" style="width: 500px; height: 300px;">
                		HTML <b>content</b> <i>default</i> in textarea
                	</textarea>
                </td>
            </tr>
            <tr>
                <td width="60" align="right">附件:</td>
                <td><input type="file" name="file" /></td>
            </tr>
        </table>
    </form>
</div>
<div id="my-dialog-3" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:600px; padding:10px;">
	<div id="p" class="easyui-panel" title="用户组" >
		<input type="checkbox" name="userGroup" value="a" mytext="A" />A
		<input type="checkbox" name="userGroup" value="b" mytext="B" />B
		<input type="checkbox" name="userGroup" value="c" mytext="C" />C
		<input type="checkbox" name="userGroup" value="d" mytext="D" />D
	</div>
	<div style="margin:2px 0;"></div>
	<div id="p" class="easyui-panel" title="用户" >
		<div style="padding:5px;">
			用户组：
			<select></select>
			用户：
			<input type="text" />
			<button type="button">查询</button>
		</div>
		<div style="float:left;">
		    <ul class="easyui-datalist" title="Basic DataList" lines="true" style="width:200px;height:250px">
		        <li value="AL">Alabama</li>
		        <li value="AK">Alaska</li>
		        <li value="AZ">Arizona</li>
		        <li value="AR">Arkansas</li>
		        <li value="CA">California</li>
		        <li value="CO">Colorado</li>
		        <li value="CT">Connecticut</li>
		        <li value="DE">Delaware</li>
		        <li value="FL">Florida</li>
		    </ul>
	    </div>
	    <div style="float:left;text-align:center;width:50px;height:250px;margin-top:50px;">
	    	<div>
	    		<button type="button" class="easyui-linkbutton">》</button>
	    	</div>
	    	<div>
	    		<button type="button" class="easyui-linkbutton">《</button>
	    	</div>
	    	<div>
	    		<button type="button" class="easyui-linkbutton">》》</button>
	    	</div>
	    	<div>
	    		<button type="button" class="easyui-linkbutton">《《</button>
	    	</div>
	    </div>
	    <div style="float:left;">
		    <ul class="easyui-datalist" id="targetUsers" title="Basic DataList" lines="true" style="width:200px;height:250px">
		        <li value="AL">Alabama</li>
		        <li value="AK">Alaska</li>
		        <li value="AZ">Arizona</li>
		        <li value="AR">Arkansas</li>
		        <li value="CA">California</li>
		        <li value="CO">Colorado</li>
		        <li value="CT">Connecticut</li>
		        <li value="DE">Delaware</li>
		        <li value="FL">Florida</li>
		    </ul>
	    </div>
    </div>
</div>
<!-- End of easyui-dialog -->
<script type="text/javascript">
	var targetGroups=[];
	var targetUsers=[];
	var targetNames=[];
	bkLib.onDomLoaded(function() { nicEditors.allTextAreas(); });
	/**
	* Name 添加记录
	*/
	function add(){
		var content=nicEditors.findEditor('textarea1').getContent();
		$("#textarea1").val(content);
		$("input[name='targetGroups']").val(targetGroups.join(","));
		$("input[name='targetUsers']").val(targetUsers.join(","));
		$('#my-form-2').form('submit', {
			url:"<s:url value='/message/admin/add' />",
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
	
	function openReceiversSelect(){
		$('#my-dialog-3').dialog({
			closed: false,
			modal:true,
            title: "修改信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: setReceivers
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#my-dialog-3').dialog('close');
                }
            }]
        });
	}	

	function setReceivers(){
		targetGroups=[];
		targetUsers=[];
		targetNames=[];
		$("input[name='userGroup']").each(function(){
			if($(this).is(':checked')){
				targetGroups.push($(this).val());
				targetNames.push($(this).attr("mytext"));
			}
		});
		var data=$('#targetUsers').datalist('getData');
		for(var i=0;i<data.rows.length;i++){
			targetUsers.push(data.rows[i].value);
			targetNames.push(data.rows[i].text);
		}
		$("input[name='receivers']").val(targetNames.join(","));
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
			{ field:'name',title:'标题',width:180,sortable:true},
			{ field:'subject',title:'发件人',width:180,sortable:true},
			{ field:'schoolYear',title:'发送时间',width:180,sortable:true,
				formatter:function(value,rec){
				   return rec.schoolYear.name;
				}
			}
		]]
	});
	
</script>
</body>
</html>