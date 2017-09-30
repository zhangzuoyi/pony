<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩管理</title>
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
        <div class="my-toolbar-button">
            <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="saveResult()" plain="true">保存</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openUpload()" plain="true">导入</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="exportTemplate()" plain="true">导出模板</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openUploadAll()" plain="true">导入全部</a>
        </div>
        <div class="my-toolbar-search">
        	<input type="hidden" id="examId" value="${vo.examId }" />
        	学年：${vo.schoolYear.name }
        	学期：${vo.term.name }
        	考试名称：${vo.name }
            <label>科目：</label> 
            <select id="subjectSelect" name="subject" class="my-select" panelHeight="auto" style="width:100px">
                <c:forEach items="${vo.subjects }" var="g">
          			<option value="${g.subject.subjectId }">${g.subject.name }</option>
          		</c:forEach>
            </select>
            <label>班级：</label> 
            <select name="schoolClass" class="my-select" panelHeight="auto" style="width:100px">
            	<c:forEach items="${vo.schoolClasses }" var="g">
          			<option value="${g.classId }">${g.name }</option>
          		</c:forEach>
            </select>
            <a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="my-datagrid-2" class="easyui-datagrid" toolbar="#my-toolbar-2" data-options="singleSelect:true,fitColumns:true,fit:true">
    	 <thead> 
            <tr> 
                <th data-options="field:'studentNo',width:100">学号</th> 
                <th data-options="field:'studentName',width:100">姓名</th> 
                <th data-options="field:'subjectName',width:100">科目</th> 
                <th data-options="field:'score',width:80,align:'right',editor:'numberbox'">成绩</th> 
            </tr> 
        </thead> 
    </table>
</div>
<!-- Begin of easyui-dialog -->
<div id="my-dialog-2" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:400px; padding:10px;">
	<form id="my-form-2" method="post" enctype="multipart/form-data">
        <table>
            <tr>
                <td width="60" align="right">考试:</td>
                <td><input type="hidden" name="examId" value="${vo.examId }" /><input type="text" name="examName" class="my-text" value="${vo.name }" readonly="readonly" /></td>
            </tr>
            <tr>
                <td width="60" align="right">科目:</td>
                <td><input type="hidden" name="subjectId" /><input type="text" name="subjectName" class="my-text" readonly="readonly" /></td>
            </tr>
            <tr>
                <td width="60" align="right">班级:</td>
                <td><input type="hidden" name="classId" /><input type="text" name="className" class="my-text" readonly="readonly" /></td>
            </tr>
            <tr>
                <td width="60" align="right">文件:</td>
                <td><input type="file" name="file" /></td>
            </tr>
        </table>
    </form>
</div>
<div id="my-dialog-3" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save',title:'成绩分析'" style="width:800px; padding:10px;">
	<div id="main" style="width: 600px;height:400px;"></div>
</div>
<div id="my-dialog-4" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:400px; padding:10px;">
	<form id="my-form-4" method="post" enctype="multipart/form-data">
        <table>
            <tr>
                <td width="60" align="right">考试:</td>
                <td><input type="hidden" name="examId" value="${vo.examId }" /><input type="text" name="examName" class="my-text" value="${vo.name }" readonly="readonly" /></td>
            </tr>
            <tr>
                <td width="60" align="right">文件:</td>
                <td><input type="file" name="file" /></td>
            </tr>
        </table>
    </form>
</div>
<!-- End of easyui-dialog -->
<script type="text/javascript" src="<s:url value='/static/easyui/datagrid-cellediting.js' />"></script>
<script type="text/javascript">
	//基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('main'));
	
	$(document).ready(function(){
		$('#my-datagrid-2').datagrid('enableCellEditing');
		/* $("#subjectSelect").change(function(){
			var subject=$(this).children('option:selected').val();
			if(subject == ""){
				$("select[name='exam']").empty();
				$("select[name='schoolClass']").empty();
			}else{
				$.ajax({
					url:"<s:url value='/exam/findBySubject' />",
					data:{subjectId: subject},
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
		}); */
		/* $("select[name='exam']").change(function(){
			var exam=$(this).children('option:selected').val();
			if(exam == ""){
				$("select[name='schoolClass']").empty();
			}else{
				$.ajax({
					url:"<s:url value='/exam/get' />",
					data:{id: exam},
					method:"GET",
					dataType:"json",
					success:function(data){
						$("select[name='schoolClass']").empty();
						$("select[name='schoolClass']").append("<option value=''>请选择</option>");
						var len=data.schoolClasses.length;
						for(var i=0;i<len;i++){
							var item=data.schoolClasses[i];
							$("select[name='schoolClass']").append("<option value='"+item.classId+"'>"+item.name+"</option>");
						}
					}	
				});
			}
		}); */

        // 指定图表的配置项和数据
        var option = {
            title: {
                text: '成绩分析'
            },
            tooltip: {},
            legend: {
                data:['成绩']
            },
            xAxis: {
                data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
            },
            yAxis: {},
            series: [{
                name: '成绩',
                type: 'bar',
                data: [5, 20, 36, 10, 10, 20]
            }]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
	});
	function upload(){
		$('#my-form-2').form('submit', {
			url:"<s:url value='/examResult/upload' />",
			success:function(data){
				if(data){
					$.messager.alert('信息提示','提交成功！','info');
					$("#searchButton").click();
					$('#my-dialog-2').dialog('close');
				}
				else
				{
					$.messager.alert('信息提示','提交失败！','info');
				}
			}
		});
	}
	function uploadAll(){//会出现超时重复提交问题
		$('#my-form-4').form('submit', {
			url:"<s:url value='/examResult/uploadAll' />",
			success:function(data){
				if(data){
					$.messager.alert('信息提示','正在后台导入，请稍后查看！','info');
					$('#my-dialog-4').dialog('close');
				}
				else
				{
					$.messager.alert('信息提示','提交失败！','info');
				}
			}
		});
		/* var form = new FormData(document.getElementById("my-form-4"));
	     $.ajax({
	         url:"<s:url value='/examResult/uploadAll' />",
	         type:"post",
	         data:form,
	         processData:false,
	         contentType:false,
	         success:function(data){
	        	 $.messager.alert('信息提示','正在后台导入，请稍后查看','info');
				 $('#my-dialog-4').dialog('close');
	         },
	         error:function(e){
	        	 $.messager.alert('信息提示','提交失败！','info');
	         }
	     });  */
	}
	
	function edit(){
		$('#my-form-2').form('submit', {
			url:"<s:url value='/grade/edit' />",
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
					url:"<s:url value='/grade/delete' />",
					data:{id: item.gradeId},
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
	
	function openUploadAll(){
		$("input[name='file']").val("");
		$('#my-dialog-4').dialog({
			closed: false,
			modal:true,
            title: "上传成绩",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: uploadAll
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#my-dialog-4').dialog('close');
                }
            }]
        });
	}
	function openUpload(){
		//$('#my-form-2').form('clear');
		$("input[name='file']").val("");
		var subjectId=$("select[name='subject']").children('option:selected').val();
		var subjectName=$("select[name='subject']").children('option:selected').html();
		var classId=$("select[name='schoolClass']").children('option:selected').val();
		var className=$("select[name='schoolClass']").children('option:selected').html();
		$('#my-form-2').find("input[name='subjectName']").val(subjectName);
		$('#my-form-2').find("input[name='subjectId']").val(subjectId);
		$('#my-form-2').find("input[name='classId']").val(classId);
		$('#my-form-2').find("input[name='className']").val(className);
		$('#my-dialog-2').dialog({
			closed: false,
			modal:true,
            title: "添加信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: upload
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
			url:"<s:url value='/grade/get' />",
			data:{id: item.gradeId},
			dataType:'json',
			success:function(data){
				if(data){
					//绑定值
					$('#my-form-2').form('load', data);
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
	
	function openAnalysis(){
		$('#my-dialog-3').dialog('open');
	}
	
	function reload(){
		$('#my-datagrid-2').datagrid('reload');
	}
	
	/**
	* Name 载入数据
	*/
	/* $('#my-datagrid-2').datagrid({
		url:"<s:url value='/grade/list' />",
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
			{ field:'gradeId',title:'ID',width:100,sortable:true},
			{ field:'name',title:'名称',width:180,sortable:true}
		]]
	}); */
	$("#searchButton").click(function(){
		var cell=$("#my-datagrid-2").datagrid("cell"); 
		if(cell != null)
			$("#my-datagrid-2").datagrid("endEdit",cell.index); 
		var examId=$("#examId").val();
		var subjectId=$("select[name='subject']").children('option:selected').val();
		var classId=$("select[name='schoolClass']").children('option:selected').val();
		if(classId){
			$.ajax({
				url:"<s:url value='/examResult/findByClass' />",
				data:{classId: classId, examId: examId, subjectId: subjectId},
				method:"GET",
				dataType:"json",
				success:function(data){
					var len=data.length;
					var names=[];
					var mydata=[];
					for(var i=0;i<len;i++){
						//mydata[i]={};
						var item=data[i];
						names[i]=item.studentName;
						mydata[i]=item.score;
					}
					myChart.setOption({
				        title: {
				            text: '成绩分析'
				        },
				        tooltip: {},
				        legend: {
				            data:['成绩']
				        },
				        xAxis: {
				            data: names
				        },
				        yAxis: {},
				        series: [{
				            name: '成绩',
				            type: 'bar',
				            data: mydata
				        }]
				    });
					$('#my-datagrid-2').datagrid({
						data: data
					});
					$('#my-datagrid-2').datagrid("reload");
					
				}	
			});
			/* var data=[
				{f1:'value11', f2:'value12'},
				{f1:'value21', f2:'value22'}
			];
			$('#my-datagrid-2').datagrid({
				data: data,
				columns:[[
					{ field:'f1',title:'ID',width:100,sortable:true},
					{ field:'f2',title:'名称',width:180,sortable:true}
				]]
			}); */
		}else{
			alert("empty");
		}
		
	});
	
	function saveResult(){
		var cell=$("#my-datagrid-2").datagrid("cell"); 
		$("#my-datagrid-2").datagrid("endEdit",cell.index); 
		var updated = $("#my-datagrid-2").datagrid('getChanges', "updated"); 
		if(updated.length>0){ 
			/* alert(JSON.stringify(updated)); 
			for(var u in updated){ 
				alert(updated[u].studentId+":"+updated[u].score); 
			}  */
			$.ajax({
				headers: {
	                'Accept': 'application/json',
	                'Content-Type': 'application/json'
	            },
				url:"<s:url value='/examResult/save' />",
				data:JSON.stringify(updated),
				type:"POST",
				dataType:'json',
				success:function(data){
					$.messager.alert('信息提示','保存成功！','info');
				}	
			});
		}
	}
	
	function exportTemplate(){
		var examId=$("#examId").val();
		var subjectId=$("select[name='subject']").children('option:selected').val();
		var classId=$("select[name='schoolClass']").children('option:selected').val();
		if( ! subjectId){
			alert("请选择科目");
			return;
		}
		if( ! classId){
			alert("请选择班级");
			return;
		}
		window.location.href="<s:url value='/examResult/exportTemplate' />"+"?examId="+examId+"&subjectId="+subjectId+"&classId="+classId;
	}
	
</script>
</body>
</html>