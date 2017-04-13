<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>班级综合成绩对比</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/elementUI/index.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/elementUI/element.css' />" />
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/dateFormat.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/echarts/echarts.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue-resource.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/elementUI/index.js' />"></script>

</head>
<body >
<div id="app">
 <div>   	           	
        	<el-card class="box-card content-margin">
            <div slot="header" class="clearfix">
              <el-row>
              
              <el-col :span="1" >
                    <b>学年:</b>                                    
            </el-col> 
            <el-col :span="4" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="conditionVo.yearId"    filterable placeholder="请选择.." >
               		 <el-option
                        v-for="schoolYear in schoolYears"
                        :label="schoolYear.name"                                            
                        :value="schoolYear.yearId">
                        <span style="float: left">{{schoolYear.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            </el-col>
            <el-col :span="1" >
                    <b>学期:</b>                                    
            </el-col> 
            <el-col :span="4" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="conditionVo.termId" @change="getExams(conditionVo.yearId,conditionVo.termId)" filterable placeholder="请选择..">
               		 <el-option
                        v-for="term in terms" 
                        :label="term.name"                      
                        :value="term.termId">
                        <span style="float: left">{{term.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            </el-col>
            <el-col :span="1" >
                    <b>年级:</b>                                    
            </el-col> 
            <el-col :span="4" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="conditionVo.gradeId"  filterable placeholder="请选择..">
               		 <el-option
                        v-for="grade in grades" 
                        :label="grade.name"                      
                        :value="grade.gradeId">
                        <span style="float: left">{{grade.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            </el-col>
            <el-col :span="1" >
                    <b>考试:</b>                                    
            </el-col> 
            <el-col :span="4" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="conditionVo.examId"  @change="getSchoolClasses(conditionVo.examId)"  filterable placeholder="请选择..">
               		 <el-option
                        v-for="exam in exams" 
                        :label="exam.name"                      
                        :value="exam.examId">
                        <span style="float: left">{{exam.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            </el-col>
             
              <el-col :span="2" >
               <el-button type="primary" @click="getListTableData()" >查询</el-button>
              <!--  <el-button type="primary" @click="save()" >保存</el-button>    -->          
              </el-col>
              
              </el-row> 
              <el-row>
              <!-- <el-radio-group v-model="examTypeId">
                <el-radio-button v-for="examType in examTypes" :label="examType.typeId" >{{examType.name}}</el-radio-button>              
            </el-radio-group> -->
            <el-col :span="1">班级:</el-col>
            <el-col :offset="1">
            <el-checkbox-group v-model="conditionVo.schoolClasseIds">
   			 <el-checkbox  v-for="schoolClass in schoolClasses"   :label="schoolClass.classId">{{schoolClass.name}}</el-checkbox>   
  			</el-checkbox-group>
  			</el-col>
              </el-row> 
                                                                      
            </div> 
            <el-row> 
            <el-col :span="16" :offset="4" >                   
            <el-table  
           	 		:data="tableData"                 
                    border
                    style="width: 100%"                  
                    >                            
                           
                  <el-table-column 
        			v-for="col in cols"
        			:prop="col.prop" 
        			:label="col.label"
        			width="150"         			
        			>
     		 </el-table-column> 
     		   
                                               
            </el-table>
            </el-col> 
                                                              
            </el-row> 
            <el-row>
              <el-col :offset="4">           
               <div id="echarts" style="width: 600px;height:400px;"></div> 
               </el-col>
            </el-row>
        </el-card>       	
    </div>
</div> 

<script type="text/javascript">

var app = new Vue({ 
	el : '#app' ,
	data : { 
	
		conditionVo : {gradeId:null,yearId:null,termId:null,examId:null,schoolClasseIds:[]},				
		grades : [],		
		schoolYears:[],		
		terms:[],
		schoolClasses:[],
		exams:[],
		schoolYearsUrl :"<s:url value='/schoolYear/listVo'/>",
		termsUrl :"<s:url value='/term/list'/>",				
		gradesUrl :"<s:url value='/grade/list'/>",
		examsUrl : "<s:url value='/exam/findByYearAndTerm'/>",
		schoolClassesUrl : "<s:url value='/schoolClass/findByExam' />",						
		listTableDataUrl :"<s:url value='/classComprehensiveCompare/findByCondition'/>",		
		tableData: [],		
        cols:[]
		
	
		
	}, 
	mounted : function() { 
		this.getSchoolYears();
		this.getTerms();		
		this.getGrades();
			
	}, 
	methods : { 
		 	
		  getSchoolYears	:function(){ 			
			this.$http.get(this.schoolYearsUrl).then(
			function(response){this.schoolYears=response.data; },
			function(response){}  	 			
			);
			},
			getTerms	:function(){ 			
			this.$http.get(this.termsUrl).then(
			function(response){this.terms=response.data; },
			function(response){}  	 			
			);
			},
		  getGrades	:function(){ 
			this.$http.get(this.gradesUrl).then(
			function(response){this.grades=response.data; },
			function(response){}  	 			
			);
			},
		  getExams	:function(yearId,termId){
		  
		  	this.conditionVo.examId = null;
		   
			this.$http.get(this.examsUrl,{params:{yearId:yearId,termId:termId}}).then(
			function(response){this.exams=response.data; },
			function(response){}  	 			
			);
			},
		getSchoolClasses :function(examId){
		    this.conditionVo.schoolClasses = null; 
			this.$http.get(this.schoolClassesUrl,{params:{examId:examId}}).then(
			function(response){this.schoolClasses=response.data; },
			function(response){}  	 			
			);
			},
		
	
		 getListTableData:function(){
					this.tableData = [];  //清空表格数据
				
					if(this.conditionVo.yearId==null||this.conditionVo.gradeId==null||this.conditionVo.termId==null||this.conditionVo.examId==null){					
						return ;
						}			 			
					 this.$http.post(this.listTableDataUrl,this.conditionVo).then(
							function(response){
                    this.cols= response.data.title;
                   	this.tableData  = response.data.rows;                   
                    this.echartsInit(response.data.echarts);	
				 },
							function(response){}  			
							);   	
				},
				
	echartsInit:function (echartsData){
	
	var myChart = echarts.init(document.getElementById('echarts'));
		var xAxis = {type: 'category',
        		boundaryGap: false};
        xAxis.data=[];
        var sumAverage = [];
        var top = [];
        var bottom=[];
        $.each(echartsData,function(key,value){
        	 xAxis.data.push(key);
        	 var rank = value.split("#");
        	 sumAverage.push(rank[0]);
        	 top.push(rank[1]);
        	 bottom.push(rank[2]);
        });
        	
       	var series = [
       		{
            name:'平均分',
            type:'line',           
            data:sumAverage
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
            title : {text : '班级综合成绩对比图' },
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
   			 
   					 
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        }
	  
        }	        	
});  

	
	

	
</script>
</body>
</html>