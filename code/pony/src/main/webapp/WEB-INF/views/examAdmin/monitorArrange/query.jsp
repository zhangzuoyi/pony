<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>监考查询</title>
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
<script type="text/javascript" src="<s:url value='/static/js/moment.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/myfilters.js' />?20170929"></script>
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
              <b>监考查询</b>
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
               <el-col  :span="1">
               		{{term.name}}
               </el-col>
             	<el-col :span="1" >
                    <b>考试:</b>                                    
            	</el-col> 
           		 <el-col :span="4" >
            		<div class="grid-content bg-purple">                                     
					<el-select v-model="examId" @change="getRoomList()" filterable placeholder="请选择..">
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
					<el-select v-model="gradeId" @change="getRoomList()" filterable placeholder="请选择..">
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
					<el-select v-model="examRoom" filterable clearable placeholder="请选择..">
               		 <el-option
                        v-for="room in roomList"
                        :label="room"                      
                        :value="room">
                        <span style="float: left">{{room}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            	</el-col>
            	
            	<el-col :span="3" >
               		<el-button type="primary"  @click="listByRoom">查询</el-button>
               		<el-button type="primary"  @click="exportAll">导出</el-button>
              	</el-col>                           
              </el-row>
            </div>
            <el-table
            		ref="multipleTable"
                    :data="tableData"
                    border
                    style="width: 100%"
                   >               
                <el-table-column
                			inline-template
							label="日期">
							<div>{{row.examDate | date}}</div>
				</el-table-column>
				<el-table-column
						inline-template
						prop="startTime"
						label="开始时间"
						width="180">
						<div>{{row.startTime | hhmm}}</div>
				</el-table-column>
				<el-table-column
						inline-template
						prop="endTime"
						label="结束时间">
						<div>{{row.endTime | hhmm}}</div>
				</el-table-column>
				<el-table-column
						prop="gradeName"
						label="年级">
				</el-table-column>
				<el-table-column
						prop="subjectName"
						label="考试科目">
				</el-table-column>
				<el-table-column
						prop="teacherName"
						label="监考老师">
				</el-table-column>
            </el-table>
        </el-card>

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
		roomListUrl:"<s:url value='/examAdmin/monitorArrange/roomList'/>",
		listByRoomUrl:"<s:url value='/examAdmin/monitorArrange/arrangeListByRoom'/>",
		exportAllUrl:"<s:url value='/examAdmin/monitorArrange/exportArrangeResult'/>",
        schoolYear :null,
		term : null,
		examId: ${examId == null ? 'null' : examId},
		exams:[],
		grades : [],	
		gradeId: ${gradeId == null ? 'null' : gradeId},
		tableData:[],
        roomList:[],
        examRoom:""
	}, 
	mounted : function() { 
			this.getCurrentSchoolYear();
			this.getCurrentTerm();
			this.getExams();
			this.getGrades();
			this.getRoomList();
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
			getRoomList	:function(){
				if(this.examId && this.gradeId){
					this.$http.get(this.roomListUrl,{params:{examId : this.examId, gradeId : this.gradeId}}).then(
							function(response){
							this.roomList=response.data; },
							function(response){}
						);
				}
				
			},
            listByRoom : function(){
				if(this.examId == null || this.examId==''){
	              	this.$alert("请选择考试","提示",{
						type:"warning",
						confirmButtonText:'确认'
					});
					return;
	              }
				if(this.gradeId == null || this.gradeId==''){
	              	this.$alert("请选择年级","提示",{
						type:"warning",
						confirmButtonText:'确认'
					});
					return;
	              }
				if(this.examRoom == null || this.examRoom==''){
	              	this.$alert("请选择考场","提示",{
						type:"warning",
						confirmButtonText:'确认'
					});
					return;
	              }
              	this.$http.post(this.listByRoomUrl,{examId:this.examId,gradeId : this.gradeId,gradeId : this.gradeId, room : this.examRoom},{emulateJSON:true}).then(
                    function(response){
                        this.tableData=response.data;
                    });
		
            },
            exportAll : function(){
            	if(this.examId == null || this.examId==''){
	              	this.$alert("请选择考试","提示",{
						type:"warning",
						confirmButtonText:'确认'
					});
					return;
	             }
            	if(this.gradeId == null || this.gradeId==''){
	              	this.$alert("请选择年级","提示",{
						type:"warning",
						confirmButtonText:'确认'
					});
					return;
	              }
            	window.location.href=this.exportAllUrl+"?examId="+this.examId+"&gradeId="+this.gradeId;
            }
        }	        
});  
</script>
</body>
</html>
