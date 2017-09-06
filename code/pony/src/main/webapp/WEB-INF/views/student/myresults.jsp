<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的成绩</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/dateFormat.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/echarts/echarts.common.min.js' />"></script>
</head>
<body class="easyui-layout">
<div class="easyui-layout" data-options="fit:true">
    <!-- Begin of toolbar -->
    <div id="my-toolbar-2">
        <%-- <div class="my-toolbar-button">
            <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="saveResult()" plain="true">保存</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAdd()" plain="true">导入</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAnalysis()" plain="true">分析</a>
        </div>
        <div class="my-toolbar-search">
            <label>科目：</label> 
            <select id="subjectSelect" name="subject" class="my-select" panelHeight="auto" style="width:100px">
                <option value="">请选择</option>
                <c:forEach items="${subjects }" var="g">
          			<option value="${g.subjectId }">${g.name }</option>
          		</c:forEach>
            </select>
            <label>考试：</label> 
            <select name="exam" class="my-select" panelHeight="auto" style="width:100px">
            </select>
            <label>班级：</label> 
            <select name="schoolClass" class="my-select" panelHeight="auto" style="width:100px">
            </select>
            <a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </div> --%>
    </div>
    <!-- End of toolbar -->
    <table id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2">
    	 <thead> 
            <tr> 
                <th data-options="field:'studentNo',width:100">学号</th> 
                <th data-options="field:'studentName',width:100">姓名</th> 
                <th data-options="field:'subjectName',width:100">科目</th> 
                <th data-options="field:'examName',width:150">考试名称</th> 
                <th data-options="field:'score',width:80,align:'right',editor:'numberbox'">成绩</th> 
            </tr> 
        </thead> 
    </table>
</div>
<script type="text/javascript" src="<s:url value='/static/easyui/datagrid-cellediting.js' />"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$.ajax({
			url:"<s:url value='/student/myresults' />",
			method:"GET",
			dataType:"json",
			success:function(data){
				$('#my-datagrid-2').datagrid({
					data: data
				});
			}	
		});
	});

	
</script>
</body>
</html>