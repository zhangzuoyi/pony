<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教师管理</title>
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
<div id="my-dialog-2" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:800px; padding:10px;">
	<form id="my-form-2" method="post">
		<input type="hidden" name="teacherId" />
        <table>
            <tr>
                <td width="60" align="right">教师编号:</td>
                <td><input type="text" name="teacherNo" class="my-text" /></td>
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
                <td width="60" align="right">参加工作日期:</td>
                <td><input type="text" name="startWorkDate"  class="my-text easyui-datebox" data-options="formatter:myformatter,parser:myparser" /></td>
                <td width="60" align="right">工龄:</td>
                <td>
                	<input type="text" name="workLength" class="my-text" />
                </td>
            </tr>
            <tr>
                <td width="60" align="right">毕业日期:</td>
                <td><input type="text" name="graduateDate"  class="my-text easyui-datebox" data-options="formatter:myformatter,parser:myparser" /></td>
                <td width="60" align="right">毕业学校:</td>
                <td>
                	<input type="text" name="graduateSchool" class="my-text" />
                </td>
            </tr>
            <tr>
            	<td width="60" align="right">专业:</td>
                <td>
                	<input type="text" name="major" class="my-text" />
                </td>
                <td width="60" align="right">学历:</td>
                <td colspan="3">
                	<select name="degree" class="my-select">
                		<c:forEach items="${degrees }" var="g">
                			<option value="${g.code }">${g.value }</option>
                		</c:forEach>
                	</select>
                </td>
            </tr>
            <tr>
            	<td width="60" align="right">职称:</td>
                <td>
                	<input type="text" name="title" class="my-text" />
                </td>
                <td width="60" align="right">任教科目:</td>
                <td colspan="3">
                	<select name="subject.subjectId" class="my-select">
                		<c:forEach items="${subjects }" var="g">
                			<option value="${g.subjectId }">${g.name }</option>
                		</c:forEach>
                	</select>
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
			url:"<s:url value='/teacher/add' />",
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
			url:"<s:url value='/teacher/edit' />",
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
					url:"<s:url value='/teacher/delete' />",
					data:{id: item.classId},
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
			url:"<s:url value='/teacher/get' />",
			data:{id: item.teacherId},
			dataType:'json',
			success:function(data){
				if(data){
					//绑定值
					$('#my-form-2').form('load', data);
					$("select[name='subject.subjectId'] option[value='"+data.subject.subjectId+"']").attr("selected","selected");
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
		url:"<s:url value='/teacher/list' />",
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
			{ field:'teacherNo',title:'教师编号',width:100,sortable:true},
			{ field:'name',title:'姓名',width:100,sortable:true},
			{ field:'sex',title:'性别',width:100,sortable:true},
			{ field:'birthday',title:'生日',width:100,sortable:true},
			{ field:'phone',title:'联系电话',width:100,sortable:true},
			{ field:'graduateDate',title:'毕业日期',width:100,sortable:true},
			{ field:'major',title:'专业',width:100,sortable:true},
			{ field:'title',title:'职称',width:100,sortable:true},
			{ field:'subject',title:'任教科目',width:180,sortable:true,
				formatter:function(value,rec){
				   return rec.subject.name;
				}
			}
		]]
	});
	
</script>
</body>
</html>