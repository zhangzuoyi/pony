<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>班级单科成绩对比</title>
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
               <input type="radio"  name="subjects" value="${g.subjectId }"/>${g.name }
            </c:forEach>  
            </div> 
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
		$("select[name='exam']").change(function(){
			var examId=$(this).children('option:selected').val();
			if(examId == ""){				
				$("#schoolClasses").empty();
				$("#subjects").empty();

			}else{				
				$.ajax({
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
				});
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
							$("#subjects").append(" <input type='radio'  name='subjects' value='"+item.subjectId+"'/>"+item.name);
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
		var yearId=$("select[name='schoolYear']").children('option:selected').val();
		var termId=$("select[name='terms']").children('option:selected').val();	
		var gradeId=$("select[name='grade']").children('option:selected').val();				
		var examId=$("select[name='exam']").children('option:selected').val();
		var schoolClasses=new Array();  
		$('input[name="schoolClasses"]:checked').each(function(){  
			 schoolClasses.push($(this).val());//向数组中添加元素  
		});  
		//var idstr=schoolClasses.join(','); 
		var subjectId=$("input[name='subjects']:checked").val();

		
		
		if(yearId&&termId&&gradeId&&examId&&subjectId){				
		$.ajax({
				 headers: {
					'Accept': 'application/json',	               
	                'Content-Type': 'application/json'
	            }, 
				url:"<s:url value='/classSingleCompare/findByCondition' />",
				data:JSON.stringify({yearId: yearId, termId: termId ,gradeId:gradeId ,examId: examId,schoolClasses:schoolClasses,subjectId:subjectId}),
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
        var average = [];
        var top = [];
        var bottom=[];
        $.each(echartsData,function(key,value){
        	 xAxis.data.push(key);
        	 var rank = value.split("#");
        	 average.push(rank[0]);
        	 top.push(rank[1]);
        	 bottom.push(rank[2]);
        });
        	
       	var series = [
       		{
            name:'平均分',
            type:'line',           
            data:average
        },
        {
            name:'最高分',
            type:'line',           
            data:top
        },
        {
            name:'最低分',
            type:'line',           
            data:bottom
        }
       	
       	]; 		
        // 指定图表的配置项和数据
        var option = {
            title : {text : '班级单科成绩对比图' },
            tooltip: {trigger : 'axis'},
             legend: {
        		data:['平均分','最高分','最低分']
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
   			 /*xAxis : {
       			 type: 'category',
        		boundaryGap: false,
       			 data: ['周一','周二','周三','周四','周五','周六','周日']
    				},
    		
    series: [
        {
            name:'邮件营销',
            type:'line',
            stack: '总量',
            data:[120, 132, 101, 134, 90, 230, 210]
        },
        {
            name:'联盟广告',
            type:'line',
            stack: '总量',
            data:[220, 182, 191, 234, 290, 330, 310]
        },
        {
            name:'视频广告',
            type:'line',
            stack: '总量',
            data:[150, 232, 201, 154, 190, 330, 410]
        },
        {
            name:'直接访问',
            type:'line',
            stack: '总量',
            data:[320, 332, 301, 334, 390, 330, 320]
        },
        {
            name:'搜索引擎',
            type:'line',
            stack: '总量',
            data:[820, 932, 901, 934, 1290, 1330, 1320]
        }
    	]	 */	 
   					 
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
	}
	

	
</script>
</body>
</html>