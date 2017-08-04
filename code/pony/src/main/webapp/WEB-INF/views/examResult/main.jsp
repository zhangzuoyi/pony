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
            学年：${year.name }  学期：${term.name }
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2"></table>
</div>
<!-- End of easyui-dialog -->
<script type="text/javascript">
	
	/**
	* Name 载入数据
	*/
	$('#my-datagrid-2').datagrid({
		url:"<s:url value='/exam/list' />",
		method:'get',
		rownumbers:true,
		singleSelect:true,
		fitColumns:true,
		fit:true,
		columns:[[
			{ field:'name',title:'名称',width:180,sortable:true},
			{ field:'type',title:'考试类型',width:180,sortable:true,
				formatter:function(value,rec){
				   return rec.type.name;
				}
			},
			{ field:'subjectsName',title:'科目',width:180,sortable:true},
			{ field:'classesName',title:'班级',width:180,sortable:true},
			{ field:'examDate',title:'考试日期',width:100,sortable:true},
			{ field:'examId',title:'操作',width:180,
				formatter:function(value,rec){
				   return '<a href="javascript:void(0)" class="easyui-linkbutton" onclick="examResult(\''+rec.examId+'\')" style="width:80px">成绩管理</a>';
				}
			}
		]]
	});
	
	function examResult(examId){
		//alert(examId);
		var url="<s:url value='/examResult/admin' />"+"?examId="+examId;
		window.parent.addTab("考试成绩管理",url,"icon-users",true);
	}
</script>
</body>
</html>