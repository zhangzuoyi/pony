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
</head>
<body class="easyui-layout">
<div class="easyui-layout" data-options="fit:true">
    <!-- Begin of toolbar -->
    <div id="my-toolbar-2">
        <div class="my-toolbar-button">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAdd()" plain="true">添加</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-edit" onclick="openEdit()" plain="true">修改</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" onclick="removeItem()" plain="true">删除</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openUpload()" plain="true">导入</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="reload()" plain="true">刷新</a>
        </div>
        <div class="my-toolbar-search">
            <label>班级：</label> 
            <select name="schoolClass" class="my-select" panelHeight="auto" style="width:100px">
            	<c:forEach items="${classes }" var="g">
           			<option value="${g.classId }">${g.name }</option>
           		</c:forEach>
            </select>
            <a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2" data-options="singleSelect:true,fitColumns:true,fit:true">
    <!-- columns:[[
			/* { checkbox:true}, */
			{ field:'studentNo',title:'学号',width:100,sortable:true},
			{ field:'name',title:'姓名',width:100,sortable:true},
			{ field:'sex',title:'性别',width:100,sortable:true},
			{ field:'birthday',title:'生日',width:100,sortable:true},
			{ field:'nativePlace',title:'籍贯',width:100,sortable:true},
			{ field:'phone',title:'联系电话',width:100,sortable:true},
			{ field:'entranceDate',title:'入学日期',width:100,sortable:true},
			{ field:'entranceType',title:'入学类型',width:100,sortable:true},
			{ field:'schoolClass',title:'班级',width:180,sortable:true,
				formatter:function(value,rec){
					if(rec.schoolClass){
						return rec.schoolClass.name;
					}else{
						return "";
					}
				   
				}
			}
		]] -->
    	<thead> 
            <tr> 
                <th data-options="field:'studentNo',width:100">学号</th> 
                <th data-options="field:'name',width:100">姓名</th> 
                <th data-options="field:'sex',width:100,formatter:sexFormatter">性别</th> 
                <th data-options="field:'birthday',width:100">生日</th> 
                <th data-options="field:'nativePlace',width:100">籍贯</th> 
                <th data-options="field:'phone',width:100">联系电话</th> 
                <th data-options="field:'entranceDate',width:100">入学日期</th> 
                <th data-options="field:'entranceType',width:100">入学类型</th> 
                <th data-options="field:'schoolClass',width:100,formatter:classFormatter">班级</th> 
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
                <td width="60" align="right">入学类型:</td>
                <td>
                	<select name="entranceType" class="my-select">
                	</select>
                </td>
            </tr>
            <tr>
                <td width="60" align="right">毕业日期:</td>
                <td><input type="text" name="graduateDate"  class="my-text easyui-datebox" data-options="formatter:myformatter,parser:myparser" /></td>
                <td width="60" align="right">毕业类型:</td>
                <td>
                	<select name="graduateType" class="my-select">
                	</select>
                </td>
            </tr>
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
<!-- End of easyui-dialog -->
<script type="text/javascript">
	/**
	* Name 添加记录
	*/
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
	
	/**
	* Name 修改记录
	*/
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
	
	/**
	* Name 删除记录
	*/
	function removeItem(){
		$.messager.confirm('信息提示','确定要删除该记录？', function(result){
			if(result){
				var item = $('#my-datagrid-2').datagrid('getSelected');
				$.ajax({
					url:"<s:url value='/studentAdmin/delete' />",
					data:{id: item.classId},
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
	
	function reload(){
		//$('#my-datagrid-2').datagrid('reload');
		$("#searchButton").click();
	}
	
	/**
	* Name 载入数据
	*/
	/* $('#my-datagrid-2').datagrid({
		url:"<s:url value='/studentAdmin/list' />",
		method:'get',
		rownumbers:true,
		singleSelect:true,
		fitColumns:true,
		fit:true,
		columns:[[
			{ field:'studentNo',title:'学号',width:100,sortable:true},
			{ field:'name',title:'姓名',width:100,sortable:true},
			{ field:'sex',title:'性别',width:100,sortable:true},
			{ field:'birthday',title:'生日',width:100,sortable:true},
			{ field:'nativePlace',title:'籍贯',width:100,sortable:true},
			{ field:'phone',title:'联系电话',width:100,sortable:true},
			{ field:'entranceDate',title:'入学日期',width:100,sortable:true},
			{ field:'entranceType',title:'入学类型',width:100,sortable:true},
			{ field:'schoolClass',title:'班级',width:180,sortable:true,
				formatter:function(value,rec){
					if(rec.schoolClass){
						return rec.schoolClass.name;
					}else{
						return "";
					}
				   
				}
			}
		]]
	}); */
	
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
		$("#searchButton").click();
	});
	
</script>
</body>
</html>