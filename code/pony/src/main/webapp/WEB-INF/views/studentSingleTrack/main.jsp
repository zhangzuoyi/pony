<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生单科成绩追踪</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/dateFormat.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/echarts/echarts.min.js' />"></script>

</head>
<body class="easyui-layout">
<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center'" style="border:0;">	
     <!-- Begin of toolbar -->
    <div id="my-toolbar-2">       
        <div class="my-toolbar-search">
       	    <div>    	    
            <label>年级：</label> 
            <select name="grade" class="my-select" panelHeight="auto" style="width:100px">
            	<option value="">请选择</option>
            	<c:forEach items="${grades }" var="g">
           			<option value="${g.gradeId }">${g.name }</option>
           		</c:forEach>
            </select>           
            <label>班级：</label> 
            <select name="schoolClasses" class="my-select" panelHeight="auto" style="width:100px">
            	<option value="">请选择</option>
            	<c:forEach items="${schoolClasses }" var="g">
           			<option value="${g.classId }">${g.name }</option>
           		</c:forEach>
            </select>
            
            <label>学生：</label> 
            <select name="students" class="my-select" panelHeight="auto" style="width:100px">
            	<option value="">请选择</option>
            	<c:forEach items="${students }" var="g">
           			<option value="${g.studentId }">${g.name }</option>
           		</c:forEach>
            </select>
             <a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查询</a>   
            </div>                       
            <div>
            <label>考试类型：</label>                       
            <c:forEach items="${examTypes }" var="g">
               <input type="checkbox"  name="examTypes" value="${g.typeId }"/>${g.name }
            </c:forEach>  
            &nbsp;&nbsp;         
            <label>科目：</label>                       
            <c:forEach items="${subjects }" var="g">
               <input type="radio"  name="subjects" value="${g.subjectId }"/>${g.name }
            </c:forEach>  
                                
            </div>                                                  
            </div>                   
    </div>
    
    <!-- End of toolbar -->
    <table    id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2" data-options="
 rownumbers:true,
 singleSelect:true,
 fitColumns:true,
		fit:true,
 pagination:true
 " ></table> 
    </div>
	<div data-options="region:'south'" style="border:0;" >
	 <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="echarts" style="width: 400px;height:300px;"></div>
	</div>

</div>
<!-- End of easyui-dialog -->
<script type="text/javascript">

	$("select[name='grade']").change(function(){
			var gradeId=$(this).children('option:selected').val();
			if(gradeId == ""){
				$("select[name='schoolClasses']").empty();
				
			}else{
				$.ajax({
					url:"<s:url value='/schoolClass/findByGrade' />",
					data:{gradeId: gradeId},
					method:"GET",
					dataType:"json",
					success:function(data){
						$("select[name='schoolClasses']").empty();
						$("select[name='schoolClasses']").append("<option value=''>请选择</option>");
						var len=data.length;
						for(var i=0;i<len;i++){
							var item=data[i];
							$("select[name='schoolClasses']").append("<option value='"+item.classId+"'>"+item.name+"</option>");
						}
					}	
				});
			}
		});
		$("select[name='schoolClasses']").change(function(){
			var classId=$(this).children('option:selected').val();
			if(classId == ""){
				$("select[name='students']").empty();
				
			}else{
				$.ajax({
					url:"<s:url value='/studentAdmin/findByClass' />",
					data:{classId: classId},
					method:"GET",
					dataType:"json",
					success:function(data){
						$("select[name='students']").empty();
						$("select[name='students']").append("<option value=''>请选择</option>");
						var len=data.length;
						for(var i=0;i<len;i++){
							var item=data[i];
							$("select[name='students']").append("<option value='"+item.studentId+"'>"+item.name+"</option>");
						}
					}	
				});
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
			
		var gradeId=$("select[name='grade']").children('option:selected').val();		
		var classId=$("select[name='schoolClasses']").children('option:selected').val();
		var studentId=$("select[name='students']").children('option:selected').val();
		var subjectId=$("input[name='subjects']:checked").val();
		
		var examTypeIds=new Array();  
			$('input[name="examTypes"]:checked').each(function(){  
   			 examTypeIds.push($(this).val());//向数组中添加元素  
			});  
		//var idstr=schoolClasses.join(','); 
		
		
		if(gradeId&&classId&&studentId&&subjectId){				
		$.ajax({
				 headers: {
					'Accept': 'application/json',	               
	                'Content-Type': 'application/json'
	            }, 
				url:"<s:url value='/studentSingleTrack/findByCondition' />",
				data:JSON.stringify({gradeId: gradeId, classId: classId ,studentId:studentId ,examTypeIds: examTypeIds,subjectId:subjectId}),
				type:"POST",
				dataType:'json',
				success:function(data){
				
					
                     $("#my-datagrid-2").datagrid({
                         columns: [data.title]    //动态取标题
                     });
                    // $("#my-datagrid-2").datagrid("loadData", data.rows);  //动态取数据 
                    $('#my-datagrid-2').datagrid({loadFilter:pagerFilter}).datagrid('loadData', data.rows);

                    echartsInit(data.echarts);
                     
                     
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
	 function  echartsInit(echartsData){
	
	var myChart = echarts.init(document.getElementById('echarts'));
		var xAxis = {type: 'category',
        		boundaryGap: false};
        xAxis.data=[];
        var classRank = [];
        var gradeRank = [];
        $.each(echartsData,function(key,value){
        	 xAxis.data.push(key);
        	 var rank = value.split("#");
        	 classRank.push(rank[0]);
        	 gradeRank.push(rank[1]);
        });
        	
       	var series = [
       		{
            name:'班级排名',
            type:'line',           
            data:classRank
        },
        {
            name:'年级排名',
            type:'line',           
            data:gradeRank
        }
       	
       	]; 		
        // 指定图表的配置项和数据
        var option = {
            title : {text : '学生单科成绩追踪图' },
            tooltip: {trigger : 'axis'},
             legend: {
        		data:['班级排名','年级排名']
   			 }, 
            grid: {
        			 left: '3%',
       				 right: '4%',
       				 bottom: '3%',
       				 containLabel: true
        
    				},
    		toolbox: {
        			feature: {
            		saveAsImage: {}
        					}
   					 },
   					 yAxis: {
       			type: 'value'
    			},	
    			xAxis :xAxis,
    			series :series  			 
   					 
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
	} 
	

	
</script>
</body>
</html>