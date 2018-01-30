<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考场考生安排</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<%-- <link rel="stylesheet" type="text/css" href="<s:url value='/static/bootstrap/css/bootstrap.min.css' />" /> --%>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/elementUI/index.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/elementUI/element.css' />" />
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/bootstrap/js/bootstrap.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/dateFormat.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue-resource.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue-validator.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/elementUI/index.js' />"></script>
<style type="text/css">
.el-input {
width:200px;
}
</style>
</head>
<body>
<div id="app">
  <div>   	           	
        	<el-card class="box-card content-margin">
            <div slot="header" class="clearfix">
              <el-row>
              <el-col :span="4">
              <b>考场考生安排</b>
              </el-col>
              </el-row>
              <el-row>                            
               <el-col :span="1" >
                    <b>学年:</b>                                    
            	</el-col> 
               <el-col  :span="2">
               		{{schoolYear.startYear}}——{{schoolYear.endYear}}
               </el-col>
               <el-col :span="1" >
                    <b>学期:</b>                                    
            	</el-col> 
               <el-col  :span="2">
               		{{term.name}}
               </el-col>
             	<el-col :span="1" >
                    <b>考试:</b>                                    
            	</el-col> 
           		 <el-col :span="4" >
            		<div class="grid-content bg-purple">                                     
					<el-select v-model="examId"   filterable clearable placeholder="请选择..">
               		 <el-option
                        v-for="exam in exams" 
                        :label="exam.name"                      
                        :value="exam.examId">
                        <span style="float: left">{{exam.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            	</el-col>
           		 <el-col :span="1" >
                    <b>年级:</b>                                    
            	</el-col> 
            	<el-col :span="4" >
            	<div class="grid-content bg-purple">                                     
					<el-select v-model="gradeId"   filterable clearable placeholder="请选择..">
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
                    <b>考场:</b>                                    
            	</el-col> 
            	<el-col :span="4" >
            	<div class="grid-content bg-purple">                                     
					<el-select v-model="roomId"  filterable clearable placeholder="请选择..">
               		 <el-option
                        v-for="item in examRooms" 
                        :label="item.name"                      
                        :value="item.name">
                        <span style="float: left">{{item.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>        
            	</el-col>
            	
            	<el-col :span="2" >
               		<el-button type="primary" @click="findExamineeRoomArrangeByRoomId" >查询</el-button>
               		<el-button type="primary" @click="exportByRoomId" >导出</el-button>
              	</el-col>                           
              </el-row>             
            </div>                      			
        </el-card>
        
        <el-table         		
                    :data="tableData"
                    border
                    style="width: 100%"				
                   >                             
                <el-table-column
                     v-for="col in cols"
        			:prop="col.prop" 
        			:label="col.label"
                        >
                </el-table-column>
                                                                                                                        
            </el-table> 
              
        
        
        
        			    	          
		</div>
</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		
		
		schoolYearUrl:"<s:url value='/schoolYear/getCurrent'/>",
		termUrl:"<s:url value='/term/getCurrent'/>",
		examUrl:"<s:url value='/exam/list'/>",
		gradesUrl :"<s:url value='/grade/list'/>",
		examRoomsUrl:"<s:url value='/examAdmin/examRoom/list'/>",
		findExamineeRoomArrangeUrl:"<s:url value='/examAdmin/examineeRoomArrange/findExamineeRoomArrangeByRoomId'/>",		          		         
        schoolYear :null,
		term : null,
		examId: ${examId == null ? 'null' : examId},
		exams:[],
		grades : [],	
		gradeId: ${gradeId == null ? 'null' : gradeId},
		examRooms:[],
		roomId : null,
		tableData:[],
		cols:[],
	
	}, 
	filters: {    
    /* timeFilter : function(input){
        var date = new date(input);
        var hour = date.getHours();
        var minutes = date.getMinutes();
        var seconds = date.getSeconds();
        return hour+":"+minutes+":"+seconds;
    } */
  }	,
	
	mounted : function() { 
				this.getCurrentSchoolYear();
				this.getCurrentTerm();
				this.getExams();
				this.getGrades();
				this.getExamRooms();

		
			
	}, 
	methods : {
			getCurrentSchoolYear	:function(){ 			
			this.$http.get(this.schoolYearUrl).then(
			function(response){
			this.schoolYear=response.data;
			 },
			function(response){}  	 			
			);
			},
			getCurrentTerm	:function(){ 			
			this.$http.get(this.termUrl).then(
			function(response){
			this.term=response.data; },
			function(response){}  	 			
			);
			},
			getExams	:function(){ 			
			this.$http.get(this.examUrl).then(
			function(response){
			this.exams=response.data; },
			function(response){}  	 			
			);
			},
			getGrades	:function(){ 
			this.$http.get(this.gradesUrl).then(
			function(response){this.grades=response.data; },
			function(response){}  	 			
			);
			},			
			getExamRooms : function(){ 
			this.$http.get(this.examRoomsUrl).then(
			function(response){this.examRooms=response.data; },
			function(response){}  	 			
			);
			},
			
			findExamineeRoomArrangeByRoomId : function(){
			if(this.examId == null || this.examId == ""){
              	this.$alert("请选择考试","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }			
			if(this.gradeId == null || this.gradeId == ""){
              	this.$alert("请选择年级","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
             if(this.roomId == null || this.roomId == ""){
              	this.$alert("请选择考场","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }                                    
              this.$http.get(this.findExamineeRoomArrangeUrl,{params:{examId: this.examId,gradeId:this.gradeId,roomId:this.roomId}}).then(
                    function(response){                          
                           this.cols= response.data.title;
                   		   this.tableData  = response.data.rows;
                                               
                        });	
			
			
			
			
			},
			exportByRoomId : function(){
			 if(this.examId == null || this.examId == ""){
              	this.$alert("请选择考试","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }			
			if(this.gradeId == null || this.gradeId == ""){
              	this.$alert("请选择年级","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
             /* if(this.roomId == null || this.roomId == ""){
              	this.$alert("请选择考场","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              } */
					var exportParams = {
										examId : this.examId,
										gradeId:this.gradeId
								};


                window.location.href = "<s:url value='/examAdmin/examineeRoomArrange/exportByRoomId?'/>"+jQuery.param(exportParams);

            }
			
			}
			
			
        
            		   		  
        	        
	 
	
});  

</script>
</body>
</html>
