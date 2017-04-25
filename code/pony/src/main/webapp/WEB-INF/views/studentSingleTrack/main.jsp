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
                    <b>年级:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="conditionVo.gradeId"  @change="getClasses(conditionVo.gradeId)"  filterable placeholder="请选择.." >
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
                    <b>班级:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="conditionVo.classId" @change="getStudents(conditionVo.classId);getSubjects(conditionVo.classId)" filterable placeholder="请选择..">
               		 <el-option
                        v-for="schoolClass in classes" 
                        :label="schoolClass.name"                      
                        :value="schoolClass.classId">
                        <span style="float: left">{{schoolClass.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            </el-col>
            <el-col :span="1" >
                    <b>学生:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="conditionVo.studentId"  filterable placeholder="请选择..">
               		 <el-option
                        v-for="student in students" 
                        :label="student.name"                      
                        :value="student.studentId">
                        <span style="float: left">{{student.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            </el-col>
             
              <el-col :span="4" :offset="2">
               <el-button type="primary" @click="getListTableData()" >查询</el-button>
              <!--  <el-button type="primary" @click="save()" >保存</el-button>    -->          
              </el-col>
              
              </el-row> 
              <el-row>
              <!-- <el-radio-group v-model="examTypeId">
                <el-radio-button v-for="examType in examTypes" :label="examType.typeId" >{{examType.name}}</el-radio-button>              
            </el-radio-group> -->
            <el-col :span="1">类型:</el-col>
            <el-col :offset="1">
            <el-checkbox-group v-model="conditionVo.examTypeIds">
   			 <el-checkbox  v-for="examType in examTypes"    :label="examType.typeId">{{examType.name}}</el-checkbox>
   
  			</el-checkbox-group>
  			</el-col>
              </el-row> 
              <el-row>
              
            <el-col :span="1">科目:</el-col>
            <el-col :offset="1">
            <el-radio-group v-model="conditionVo.subjectId">
                <el-radio-button v-for="subject in subjects" :label="subject.subjectId" >{{subject.name}}</el-radio-button>              
            </el-radio-group>
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
	
		conditionVo : {gradeId:null,classId:null,studentId:null,examTypeIds:[],subjectId:null},				
		grades : [],		
		classes:[],		
		students:[],
		examTypes:[],
		subjects:[],
		currentSchoolYear : {},
		currentSchoolYearUrl: "<s:url value='/schoolYear/getCurrent'/>",		
		gradesUrl :"<s:url value='/grade/list'/>",
		classesUrl :"<s:url value='/schoolClass/findByYearAndGrade'/>",
		studentsUrl :  "<s:url value='/studentAdmin/findByClass'/>",
		examTypesUrl : "<s:url value='/examType/list' />",
		subjectsUrl : "<s:url value='/subject/findByClass' />",						
		listTableDataUrl :"<s:url value='/studentSingleTrack/findByCondition'/>",		
		tableData: [],		
        cols:[]
		
	
		
	}, 
	mounted : function() { 
		this.getCurrentSchoolYear(); 		
		this.getGrades();
		this.getExamTypes();
		//this.getSubjects();
			
	}, 
	methods : { 
		  getCurrentSchoolYear : function(){ 
			this.$http.get(this.currentSchoolYearUrl).then(
			function(response){this.currentSchoolYear=response.data; },
			function(response){}  			
			); 
			} ,	
		  getClasses	:function(gradeId){ 
		 
		 	this.conditionVo.classId = null;
			
			this.$http.get(this.classesUrl,{params:{yearId:this.currentSchoolYear.yearId,gradeId:gradeId}}).then(
			function(response){this.classes=response.data; },
			function(response){}  	 			
			);
			},
		  getGrades	:function(){ 
			this.$http.get(this.gradesUrl).then(
			function(response){this.grades=response.data; },
			function(response){}  	 			
			);
			},
		  getStudents	:function(classId){
		  
		  	this.conditionVo.studentId = null;
		   
			this.$http.get(this.studentsUrl,{params:{classId:classId}}).then(
			function(response){this.students=response.data; },
			function(response){}  	 			
			);
			},
		getExamTypes :function(){ 
			this.$http.get(this.examTypesUrl).then(
			function(response){this.examTypes=response.data; },
			function(response){}  	 			
			);
			},
		getSubjects :function(classId){ 
			this.$http.get(this.subjectsUrl,{params:{classId:classId}}).then(
			function(response){this.subjects=response.data; },
			function(response){}  	 			
			);
			},
	
		 getListTableData:function(){
					this.tableData = [];  //清空表格数据
				
					if(this.conditionVo.classId==null||this.conditionVo.gradeId==null||this.conditionVo.studentId==null||this.conditionVo.subjectId==null){					
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
        myChart.setOption(option);}
	  
        }	        	
});  
	
	

	
</script>
</body>
</html>