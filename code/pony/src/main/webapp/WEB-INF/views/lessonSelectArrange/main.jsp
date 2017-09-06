<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>可选课程管理</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/dateFormat.js' />"></script>
<style type="text/css">
div.timeArrange{margin:5px;}
</style>
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
<div id="my-dialog-2" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:600px; padding:10px;">
	<form id="my-form-2" method="post">
		<input type="hidden" name="arrangeId" />
        <table>
        	<tr>
                <td width="60" align="right">学年:</td>
                <td>${currentYear.name }<input type="hidden" name="yearId" value="${currentYear.yearId }" /></td>
                <td width="60" align="right">学期:</td>
                <td>${currentTerm.name }<input type="hidden" name="termId" value="${currentTerm.termId }" /></td>
            </tr>
            <tr>
                <td width="60" align="right">科目:</td>
                <td>
                	<select name="subjectId" class="tiny-select">
                		<c:forEach items="${subjects }" var="g">
                			<option value="${g.subjectId }">${g.name }</option>
                		</c:forEach>
                	</select>
                </td>
                <td width="60" align="right">教师:</td>
                <td>
                	<select name="teacherId" class="tiny-select">
                		<c:forEach items="${teachers }" var="g">
                			<option value="${g.teacherId }">${g.name }</option>
                		</c:forEach>
                	</select>
                </td>
            </tr>
            <tr>
                <td width="60" align="right">课时:</td>
                <td><input type="text" name="period" class="tiny-text" /></td>
                <td width="60" align="right">学分:</td>
                <td><input type="text" name="credit" class="tiny-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">人数上限:</td>
                <td><input type="text" name="upperLimit" class="tiny-text" /></td>
                <td width="60" align="right">人数下限:</td>
                <td><input type="text" name="lowerLimit" class="tiny-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">上课地点:</td>
                <td colspan="3"><input type="text" name="classroom" class="tiny-text" /></td>
            </tr>
            <tr>
                <td width="60" align="right">可选年级:</td>
                <td colspan="3">
                	<input type="hidden" name="gradeIds" />
                	<c:forEach items="${grades }" var="g">
               			<input type="checkbox" class="gradeCheck" value="${g.gradeId }" />${g.name }
               		</c:forEach>
                </td>
            </tr>
            <tr>
                <td width="60" align="right">时间安排:</td>
                <td colspan="3" id="timeArrangeTd">
                	<div class="timeArrange">
	                	<select name="lessonSelectTimes[].weekday" class="tiny-select">
	                		<c:forEach items="${weekdays }" var="g">
	                			<option value="${g.key }">${g.value }</option>
	                		</c:forEach>
	                	</select>
	                	<select name="lessonSelectTimes[].periodId" class="tiny-select">
	                		<c:forEach items="${periods }" var="g">
	                			<option value="${g.periodId }">第${g.seq }节</option>
	                		</c:forEach>
	                	</select>
	                	<button type="button" onclick="addTimeArrange();">新增</button>
                	</div>
                </td>
            </tr>
        </table>
    </form>
</div>
<!-- End of easyui-dialog -->
<script type="text/javascript">
	/**
	* 新增时间安排
	*/
	function addTimeArrange(){
		var div=$("<div class='timeArrange'></div>");
		var weekday=$("#timeArrangeTd").children("div:first").children("select[name='lessonSelectTimes[].weekday']").clone();
		var lessonPeriod=$("#timeArrangeTd").children("div:first").children("select[name='lessonSelectTimes[].periodId']").clone();
		div.append(weekday);
		div.append(lessonPeriod);
		div.append("<button type='button' onclick='delTimeArrange(this)' >删除</button>");
		$("#timeArrangeTd").append(div);
	}
	function delTimeArrange(ele){
		$(ele).parent().remove();
	}
	/**
	* Name 添加记录
	*/
	function add(){
		var gradeIds="";
		$("input.gradeCheck").each(function(){
			if($(this).is(':checked')) {
				if(gradeIds==""){
					gradeIds=gradeIds+$(this).val();
				}else{
					gradeIds=gradeIds+","+$(this).val();
				}
			}
		});
		$("#timeArrangeTd").children("div").each(function(index){
			$(this).find("select[name='lessonSelectTimes[].weekday']").attr("name","lessonSelectTimes["+index+"].weekday");
			$(this).find("select[name='lessonSelectTimes[].periodId']").attr("name","lessonSelectTimes["+index+"].periodId");
		});
		$("input[name='gradeIds']").val(gradeIds);
		$('#my-form-2').form('submit', {
			url:"<s:url value='/lessonSelectArrange/add' />",
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
			url:"<s:url value='/subject/edit' />",
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
					url:"<s:url value='/lessonSelectArrange/delete' />",
					data:{id: item.arrangeId},
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
		var yearId=$("input[name='yearId']").val();
		var termId=$("input[name='termId']").val();
		$('#my-form-2').form('clear');
		$("input[name='yearId']").val(yearId);
		$("input[name='termId']").val(termId);
		$("#timeArrangeTd").children("div").each(function(index){
			if(index>0){
				$(this).remove();
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
			url:"<s:url value='/lessonSelectArrange/get' />",
			data:{id: item.arrangeId},
			dataType:'json',
			success:function(data){
				if(data){
					//绑定值
					$('#my-form-2').form('load', data);
					for(var i=0;i<data.gradeIds.length;i++){
						//alert(data.gradeIds[i]);
						$("input.gradeCheck").each(function(){
							if($(this).val() == data.gradeIds[i]){
								$(this).attr("checked","true");
							}
						});
					}
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
		url:"<s:url value='/lessonSelectArrange/list' />",
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
		]]
	});
	
</script>
</body>
</html>