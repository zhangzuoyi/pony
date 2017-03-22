<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩排名管理</title>
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
        <div class="my-toolbar-search">
       	    <div>
       	    <label>学年：</label> 
            <select name="schoolYear" class="my-select" panelHeight="auto" style="width:100px">
            	<option value="">请选择</option>
            	<c:forEach items="${schoolYears }" var="g">
           			<option value="${g.yearId }">${g.name }</option>
           		</c:forEach>
            </select>
            <label>学期：</label> 
            <select name="terms" class="my-select" panelHeight="auto" style="width:100px">
            	<option value="">请选择</option>            	
            	<c:forEach items="${terms }" var="g">
           			<option value="${g.termId }">${g.name }</option>
           		</c:forEach>
            </select>
            <label>年级：</label> 
            <select name="grade" class="my-select" panelHeight="auto" style="width:100px">
            	<option value="">请选择</option>
            	<c:forEach items="${grades }" var="g">
           			<option value="${g.gradeId }">${g.name }</option>
           		</c:forEach>
            </select>
            <label>考试：</label> 
            <select name="examType" class="my-select" panelHeight="auto" style="width:100px">
            	<option value="">请选择</option>
            	<c:forEach items="${examTypes }" var="g">
           			<option value="${g.typeId }">${g.name }</option>
           		</c:forEach>
            </select>
            <a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查询</a>                      
            </div>
            <div>
            <label>班级：</label> 
            <div id="schoolClasses">           
            <c:forEach items="${schoolClasses }" var="g">
               <input type="checkbox"  name="schoolClasses" value="${g.classId }"/>${g.name }
            </c:forEach>
            </div>
            </div>
            <div>
            <label>科目：</label>
            <div id="subjects"> 
            <c:forEach items="${subjects }" var="g">
               <input type="checkbox"  name="subjects" value="${g.subjectId }"/>${g.name }
            </c:forEach>
            </div>
            </div>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2" data-options="
 rownumbers:true,
 singleSelect:true,
 fitColumns:true,
		fit:true,
 pagination:true
 " ></table>
</div>
<!-- End of easyui-dialog -->
<script type="text/javascript">

	$("select[name='grade']").change(function(){
			var gradeId=$(this).children('option:selected').val();
			if(gradeId == ""){
				$("#schoolClasses").empty();
				
			}else{
				$.ajax({
					url:"<s:url value='/schoolClass/findByGrade' />",
					data:{gradeId: gradeId},
					method:"GET",
					dataType:"json",
					success:function(data){
						$("#schoolClasses").empty();
						var len=data.length;
						for(var i=0;i<len;i++){
							var item=data[i];
							$("#schoolClasses").append(" <input type='checkbox'  name='schoolClasses' value='"+item.classId+"'/>"+item.name);
						}
					}	
				});
			}
		});

	
	/**
	* Name 载入数据
	*/
	$("#searchButton").click(function(){
	
		//var examId=$("#examId").val();
		var yearId=$("select[name='schoolYear']").children('option:selected').val();
		var termId=$("select[name='terms']").children('option:selected').val();
		var gradeId=$("select[name='grade']").children('option:selected').val();
		var examTypeId=$("select[name='examType']").children('option:selected').val();
		var schoolClasses=new Array();  
			$('input[name="schoolClasses"]:checked').each(function(){  
   			 schoolClasses.push($(this).val());//向数组中添加元素  
			});  
		//var idstr=schoolClasses.join(','); 
		var subjects=new Array();  
			$('input[name="subjects"]:checked').each(function(){  
   			 subjects.push($(this).val());//向数组中添加元素  
			});    
		//var idstr=subjects.join(',');
		if(yearId&&termId&&gradeId&&examTypeId){
		
		//,schoolClasses:JSON.stringify(schoolClasses),subjects:JSON.stringify(subjects)
		$.ajax({
				 headers: {
					'Accept': 'application/json',	               
	                'Content-Type': 'application/json'
	            }, 
				url:"<s:url value='/examResultRank/findByCondition' />",
				data:JSON.stringify({yearId : yearId, termId: termId, gradeId: gradeId, examTypeId: examTypeId ,schoolClasses:schoolClasses ,subjects: subjects}),
				type:"POST",
				dataType:'json',
				success:function(data){
				
					 //var msg = $.parseJSON(data);
                     $("#my-datagrid-2").datagrid({
                         columns: [data.title]    //动态取标题
                     });
                     $("#my-datagrid-2").datagrid("loadData", data.rows);  //动态取数据 
				},
				  error : function(XMLHttpRequest, textStatus, errorThrown) {
		
					alert(XMLHttpRequest.responseText); 
					alert(XMLHttpRequest.status);
					alert(XMLHttpRequest.readyState);
					alert(textStatus);  
					}	
			});		
		}else{
		$.messager.alert('','请输入完整的查询条件!');;
		}		
	});

	
</script>
</body>
</html>