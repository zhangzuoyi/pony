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
           			<option value="${g.yearId }">${g.startYear }-${g.endYear }</option>
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
            <select name="exam" class="my-select" panelHeight="auto" style="width:100px">
            	<option value="">请选择</option>
            	<c:forEach items="${exams }" var="g">
           			<option value="${g.examId }">${g.name }</option>
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
 pagination:true,
 pageSize:10
 " ></table>
</div>
<!-- End of easyui-dialog -->
<script type="text/javascript">

		$(function(){ 
				//默认选择当前学年和学期
				$.ajax({
					url:"<s:url value='/schoolYear/getCurrent' />",
					data:{},
					method:"GET",
					dataType:"json",
					success:function(data){
						$("select[name='schoolYear']").val(data.yearId);
					$.ajax({
					url:"<s:url value='/term/getCurrent' />",
					data:{},
					method:"GET",
					dataType:"json",
					success:function(data){
						$("select[name='terms']").val(data.termId);
						var yearId=$("select[name='schoolYear']").children('option:selected').val();
			   			 var termId=$("select[name='terms']").children('option:selected').val();
					$.ajax({
					url:"<s:url value='/exam/findByYearAndTerm' />",
					data:{yearId: yearId,termId : termId},
					method:"GET",
					dataType:"json",
					success:function(data){
						$("select[name='exam']").empty();
						$("select[name='exam']").append("<option value=''>请选择</option>");
						var len=data.length;
						for(var i=0;i<len;i++){
							var item=data[i];
							$("select[name='exam']").append("<option value='"+item.examId+"'>"+item.name+"</option>");
						}
					}	
				});
						
						
						
						
						}						
				});
						
						}						
				});
				
				
				
				
				
				
		}); 


	$("select[name='terms']").change(function(){
			var yearId=$("select[name='schoolYear']").children('option:selected').val();
			var termId=$("select[name='terms']").children('option:selected').val();
			if(yearId == "" || termId==""){
				$("select[name='exam']").empty();
				
			}else{
				$.ajax({
					url:"<s:url value='/exam/findByYearAndTerm' />",
					data:{yearId: yearId,termId : termId},
					method:"GET",
					dataType:"json",
					success:function(data){
						$("select[name='exam']").empty();
						$("select[name='exam']").append("<option value=''>请选择</option>");
						var len=data.length;
						for(var i=0;i<len;i++){
							var item=data[i];
							$("select[name='exam']").append("<option value='"+item.examId+"'>"+item.name+"</option>");
						}
					}	
				});
			}
		});
		
		$("select[name='grade']").change(function(){
			var gradeId=$("select[name='grade']").children('option:selected').val();
				if(gradeId ==""){
				$("#schoolClasses").empty();
				}else{
				$.ajax({
					url:"<s:url value='/schoolClass/findCurrentByGrade' />",
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
		
		
		$("select[name='exam']").change(function(){
			var examId=$(this).children('option:selected').val();
			if(examId == ""){
				$("#subjects").empty();
				$("#schoolClasses").empty();
				
			}else{
				 $.ajax({
					url:"<s:url value='/subject/findByExam' />",
					data:{examId: examId},
					method:"GET",
					dataType:"json",
					success:function(data){
						$("#subjects").empty();
						var len=data.length;
						for(var i=0;i<len;i++){
							var item=data[i];
							$("#subjects").append(" <input type='checkbox'  name='subjects' value='"+item.subjectId+"'/>"+item.name);
						}
					}	
				}); 
				 /* $.ajax({
					url:"<s:url value='/schoolClass/findByExam' />",
					data:{examId: examId},
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
				});  */
			}
		});
		


		function pagerFilter(data){
			if (typeof data.length == 'number' && typeof data.splice == 'function'){	// is array
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
					pager.pagination('refresh',{
						pageNumber:pageNum,
						pageSize:pageSize
					});
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
		
		

	
	/**
	* Name 载入数据
	*/
	$("#searchButton").click(function(){
	
		//var examId=$("#examId").val();
		var yearId=$("select[name='schoolYear']").children('option:selected').val();
		var termId=$("select[name='terms']").children('option:selected').val();
		var gradeId=$("select[name='grade']").children('option:selected').val();
		var examId=$("select[name='exam']").children('option:selected').val();
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
		if(yearId&&termId&&gradeId&&examId){
		
		//,schoolClasses:JSON.stringify(schoolClasses),subjects:JSON.stringify(subjects)
		$.ajax({
				 headers: {
					'Accept': 'application/json',	               
	                'Content-Type': 'application/json'
	            }, 
				url:"<s:url value='/examResultRank/findByCondition' />",
				data:JSON.stringify({yearId : yearId, termId: termId, gradeId: gradeId, examId: examId ,schoolClasses:schoolClasses ,subjects: subjects}),
				type:"POST",
				dataType:'json',
				success:function(data){
				
					 //var msg = $.parseJSON(data);
                     $("#my-datagrid-2").datagrid({
                         columns: [data.title]    //动态取标题
                     });
                  //   $("#my-datagrid-2").datagrid("loadData", data.rows);  //动态取数据 
				
         			$('#my-datagrid-2').datagrid({loadFilter:pagerFilter}).datagrid('loadData', data.rows);

				
				},
				  error : function(XMLHttpRequest, textStatus, errorThrown) {
		
					/* alert(XMLHttpRequest.responseText); 
					alert(XMLHttpRequest.status);
					alert(XMLHttpRequest.readyState);
					alert(textStatus);   */
					}	
			});		
		}else{
		$.messager.alert('','请输入完整的查询条件!');;
		}		
	});

	
</script>
</body>
</html>