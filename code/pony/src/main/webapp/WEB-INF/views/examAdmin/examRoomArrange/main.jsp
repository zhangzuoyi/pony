<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考场设置</title>
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
              <b>考场设置</b>
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
					<el-select v-model="examId" @change="getExamSubjects()"    filterable clearable placeholder="请选择..">
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
               		<el-button type="primary" @click="getExamArranges()" >查询</el-button>
              	</el-col>                           
              </el-row>
              <el-row>                            
                  <b>科目:</b>
              </el-row>
            <el-row>
                <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
                <div style="margin:15px 0;"></div>
                <el-checkbox-group v-model="checkedSubjects" @change="handleCheckedSubjectChange">
                    <el-checkbox v-for="subject in subjects" :label="subject.subjectId">{{subject.name}}</el-checkbox>
                </el-checkbox-group>
            </el-row>
                <el-row>
                    <b>考场:</b>
                </el-row>
                <el-row>
                    <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
                    <div style="margin:15px 0;"></div>
                    <el-checkbox-group v-model="checkedSubjects" @change="handleCheckedSubjectChange">
                        <el-checkbox v-for="subject in subjects" :label="subject.subjectId">{{subject.name}}</el-checkbox>
                    </el-checkbox-group>
                </el-row>
                <el-row>
                    <el-button type="primary" >生成设置</el-button>
                    <el-button type="primary" >保存设置</el-button>
                </el-row>
            </div>
        </el-card>
            <el-row>
                <el-col :span="4">
                    <b>已选科目:</b>
                </el-col>
                <el-col :span="20">


                </el-col>
            </el-row>
            <el-row>
                <el-col :span="4">
                      <b>已选考场:</b>
                 </el-col>
                <el-col :span="20">
                    <el-table
                            :data="tableData"
                            border
                            style="width: 100%">
                        <el-table-column
                                prop="roomName"
                                label="名称"
                                >
                        </el-table-column>
                        <el-table-column
                                prop="roomSeq"
                                label="序号"
                                >
                        </el-table-column>
                        <el-table-column
                                prop="capacity"
                                label="容量">
                        </el-table-column>
                    </el-table>
                </el-col>
             </el-row>
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
		examSubjectUrl:"<s:url value='/subject/findByExam'/>",
		schoolYear :null,
		term : null,
		examId: null,
		exams:[],
		grades : [],	
		gradeId:null,
		tableData:[],						
		checkAll:true,
		isIndeterminate:true,
		checkedSubjects:[],
		subjects:[]

		
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
			getExamSubjects	:function(){ 			
			this.$http.get(this.examSubjectUrl,{params:{examId:this.examId}}).then(
			function(response){
			this.subjects=response.data; },
			function(response){}  	 			
			);
			},
			handleSelectionChange: function(val) {
                this.multipleSelection = val;
            },
			handleCheckAllChange:function(event){
			    var allSubjects = [];
			    for(var index in this.subjects){
                    allSubjects.push(this.subjects[index].subjectId);
                }
				this.checkedSubjects = event.target.checked?allSubjects:[];
				this.isIndeterminate=false;
			},
			handleCheckedSubjectChange:function(value){
				var checkedCount = value.length;
				this.checkAll = checkedCount === this.subjects.length;
				this.isIndeterminate = checkedCount > 0 && checkedCount <this.subjects.length;
			}
        }	        
	 
	
});  

</script>
</body>
</html>
