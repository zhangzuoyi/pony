<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考生考场设置</title>
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
              <b>考生考场设置</b>
              </el-col>
              </el-row>
              <el-row>                            
               <el-col :span="1" >
                    <b>学年:</b>                                    
            	</el-col> 
               <el-col  :span="3">
               		{{schoolYear.startYear}}——{{schoolYear.endYear}}
               </el-col>
               <el-col :span="1" >
                    <b>学期:</b>                                    
            	</el-col> 
               <el-col  :span="3">
               		{{term.name}}
               </el-col>
             	<el-col :span="1" >
                    <b>考试:</b>                                    
            	</el-col> 
           		 <el-col :span="5" >
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
            	<el-col :span="5" >
            	<div class="grid-content bg-purple">                                     
					<el-select v-model="gradeId"  filterable clearable placeholder="请选择..">
               		 <el-option
                        v-for="grade in grades" 
                        :label="grade.name"                      
                        :value="grade.gradeId">
                        <span style="float: left">{{grade.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>        
            	</el-col>
            	
            	<el-col :span="4" >
               		<el-button type="success" @click="autoExamineeRoomArrange" >自动考场安排</el-button>
              	</el-col>                           
              </el-row>             
            </div>                      			
        </el-card>
        <el-dialog     close-on-click-modal="false" close-on-press-escape="false" v-model="dialogFormVisible" >			
				
				 <el-table 
				    v-loading="loading" 
				    element-loading-text="自动安排中..."            	 		                
                    border
                    style="width: 100%"
                    empty-text=""
                    >                            
                  </el-table>         
				
		</el-dialog>
        
        <%-- <el-row style="margin-top:10px;">
        	<el-col :span="1" >
                    <b>考试:</b>                                    
            	</el-col> 
           		 <el-col :span="5" >
            		<div class="grid-content bg-purple">                                     
					<el-select v-model="examId2"   filterable clearable placeholder="请选择..">
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
                    <b>类型:</b>                                    
            	</el-col> 
               <el-col  :span="3">
               		<el-select  v-model="type"   clearable placeholder="请选择..">
               		 <el-option                   
                        label="班级"                      
                        value="1">
                        <span style="float: left">班级</span>
               		 </el-option>
               		 <el-option                   
                        label="考场"                      
                        value="2">
                        <span style="float: left">考场</span>
               		 </el-option>
           			 </el-select>
               </el-col>              
             	<el-col :span="1" v-if="type==1" >
                    <b>班级:</b>                                    
            	</el-col> 
            	<el-col :span="5"  v-if="type==1" >
            		<div class="grid-content bg-purple">                                     
					<el-select v-model="classId"   filterable clearable placeholder="请选择..">
               		 <el-option
                        v-for="item in schoolClasses" 
                        :label="item.name"                      
                        :value="item.classId">
                        <span style="float: left">{{item.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            	</el-col>
            	<el-col :span="1" v-if="type==2" >
                    <b>考场:</b>                                    
            	</el-col>
           		 <el-col :span="5" v-if="type==2" >
            		<div class="grid-content bg-purple">                                     
					<el-select v-model="roomId"   filterable clearable placeholder="请选择..">
               		 <el-option
                        v-for="item in examRooms" 
                        :label="item.name"                      
                        :value="item.id">
                        <span style="float: left">{{item.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            	</el-col>
           		
            	
            	<el-col :span="4" >
               		<el-button type="primary" @click="findExamineeRoomArrange()" >查询</el-button>
              	</el-col>                           
              </el-row>
              <el-table         		
                    :data="tableData"
                    border
                    style="width: 100%"				
                   >                             
                <el-table-column
                        prop="roomName"
                        label="考场名"
                        >
                </el-table-column>
                <el-table-column
                		prop="studentName"
                        label="考生名"
                        >
                </el-table-column>
                <el-table-column
                		prop="regNo"
                        label="考试号"
                        >
                </el-table-column>
                <el-table-column
                		prop="className"
                        label="班级"
                        >
                </el-table-column>
                <el-table-column
                		prop="subjectName"
                        label="考试科目"
                        >
                </el-table-column>
                <el-table-column
                		prop="seq"
                        label="序号"
                        >
                </el-table-column>                                                                                                         
            </el-table> --%> 
        
        
        
        			    	          
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
		schoolClassesUrl:"<s:url value='/schoolClass/listVo'/>",
		examRoomsUrl:"<s:url value='/examAdmin/examRoom/list'/>",				
		//gradeUrl:"<s:url value='/grade/get'/>",  
		autoExamineeRoomArrangeUrl:"<s:url value='/examAdmin/examineeRoomArrange/autoExamineeRoomArrange'/>",
		findExamineeRoomArrangeUrl:"<s:url value='/examAdmin/examineeRoomArrange/findExamineeRoomArrange'/>",		          		         
        schoolYear :null,
		term : null,
		examId: ${examId == null ? 'null' : examId},
		exams:[],
		grades : [],	
		gradeId: ${gradeId == null ? 'null' : gradeId},
		schoolClasses:[],
		examRooms:[],
		type : null,
		classId : null,
		roomId : null,
		examId2 : null,
		tableData:[],
		dialogFormVisible: false,
		loading:false

	
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
				//this.getSchoolClasses();
				//this.getExamRooms();

		
			
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
			getSchoolClasses : function(){ 
			this.$http.get(this.schoolClassesUrl).then(
			function(response){this.schoolClasses=response.data; },
			function(response){}  	 			
			);
			},
			getExamRooms : function(){ 
			this.$http.get(this.examRoomsUrl).then(
			function(response){this.examRooms=response.data; },
			function(response){}  	 			
			);
			},
			autoExamineeRoomArrange :function(){
			if(this.examId == null || this.examId==''){
              	this.$alert("请选择考试","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
              if(this.gradeId == null || this.greadeId==''){
              	this.$alert("请选择年级","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
              app.dialogFormVisible=true;   app.loading = true;
              this.$http.get(this.autoExamineeRoomArrangeUrl,{params:{examId:this.examId,gradeId:this.gradeId}}).then(
                    function(response){
                    	app.dialogFormVisible=false;   app.loading = false;
                           this.examId = null;
                           this.gradeId = null;
                           this.$message({type:"info",message:"操作成功"});                    
                        });
			
			},
			findExamineeRoomArrange : function(){
			if(this.examId2 == null || this.examId2 == ""){
              	this.$alert("请选择考试","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
			if(this.type == null || this.type == ""){
              	this.$alert("请选择类型","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
			if(this.type == 1 &&  (this.classId == null || this.classId == "")){
              	this.$alert("请选择班级","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
             if(this.type == 2 &&  (this.roomId == null || this.roomId == "")){
              	this.$alert("请选择考场","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
            
              
              
              this.$http.get(this.findExamineeRoomArrangeUrl,{params:{examId: this.examId2,type:this.type,classId:this.classId,roomId:this.roomId}}).then(
                    function(response){                          
                           this.tableData = response.data;
                                               
                        });
			
			
			
			
			}
			
			
        
            		   		  
        }	        
	 
	
});  

</script>
</body>
</html>
