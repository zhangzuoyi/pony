<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>均量值</title>
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
              <b>均量值</b>
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
					<el-select v-model="examId" filterable clearable placeholder="请选择..">
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
					<el-select v-model="gradeId" filterable placeholder="请选择..">
               		 <el-option
                        v-for="grade in grades" 
                        :label="grade.name"                      
                        :value="grade.gradeId">
                        <span style="float: left">{{grade.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>        
            	</el-col>
            	<el-col :span="6" >
               		<!-- <el-button type="primary"  @click="list">查询</el-button> -->
               		<el-button type="primary"  @click="setAverageIndex">设置指标</el-button>
               		<el-button type="primary"  @click="openUploadIndex">导入指标</el-button>
               		<el-button type="primary"  @click="exportAverage">导出</el-button>
               		
              	</el-col>                           
              </el-row>
            </div>
            <!-- <el-table
            		ref="multipleTable"
                    :data="tableData"
                    border
                    style="width: 100%"
                   >               
                <el-table-column
							prop="teacherName"
							label="姓名"
							>
				</el-table-column>
				<el-table-column
						prop="subjectName"
						label="任教科目"
						width="180">
				</el-table-column>
				<el-table-column
						prop="teacherNo"
						label="编号">
				</el-table-column>
				<el-table-column
						prop="monitorCount"
						label="监考次数">
				</el-table-column>
            </el-table> -->
        </el-card>
		<el-dialog title="设置均量值指标"  :visible.sync="setIndexDialogFormVisible" size="large">
			<div slot="title" class="dialog-title">
                  <b>设置均量值指标</b>
            </div>
			<table class="table table-bordered">
            	<thead>
            		<tr>
            			<th>段名</th>
            			<th v-for="s in subjects">{{s.name}}</th>
            		</tr>
            	</thead>
            	<tbody>
            		<tr v-for="item in indexRows">
            			<td>{{item.section}}</td>
            			<td v-for="x in item.indexList"><el-input v-model="x.indexValue" style="width:50px;"></el-input></td>
            		</tr>
            	</tbody>
            </table>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitIndexList()">确定</el-button>
				<el-button @click="setIndexDialogFormVisible = false">取消</el-button>
			</div>
		</el-dialog>
		<el-dialog title="导入均量值指标"  :visible.sync="uploadIndexDialogFormVisible" >
			<el-row>
			<el-col>
				<el-upload					
	  			 ref="upload" 	
	  			 name="file"
	  			 action="<s:url value='/examAdmin/average/uploadIndexList'/>"
	  			:file-list="fileList"
	  			:auto-upload="false"
	  			:data="conditionVo">
	  			<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
				</el-upload>
			</el-col>
			</el-row>
			<el-row>
			<el-button type="primary" size="small" @click="uploadIndex">提交</el-button>
			<el-button type="primary" size="small" @click="downloadTemplate">下载模板</el-button>
			</el-row>
		</el-dialog>
		
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
		findSubjectUrl:"<s:url value='/subject/findByExam'/>",
		getIndexRowsUrl:"<s:url value='/examAdmin/average/getIndexRows'/>",
		saveIndexListUrl:"<s:url value='/examAdmin/average/submitIndexList'/>",
		schoolYear :null,
		term : null,
		examId: ${examId == null ? 'null' : examId},
		exams:[],
		grades : [],	
		gradeId: ${gradeId == null ? 'null' : gradeId},
		setIndexDialogFormVisible : false,
		uploadIndexDialogFormVisible : false,
		uploadResultDialogFormVisible : false,
		subjects : [],
		indexRows : [],
		fileList : [],
        conditionVo : {},
        tableData:[]
		
	}, 
	mounted : function() { 
		this.getCurrentSchoolYear();
		this.getCurrentTerm();
		this.getExams();
		this.getGrades();
	},
	watch:{
		filterText :function(val){
			this.filterTable(val);
		}
	
	},
	methods : {
			filterTable :function(value){
				if(!value){
					this.usedTeachers=this.teachers;
				}
				this.usedTeachers=[];
				for(var i in this.teachers){
					if(this.teachers[i].name.indexOf(value) != -1){
						this.usedTeachers.push(this.teachers[i]);
					}
				}	
			},
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
			setAverageIndex : function(){
				this.$http.get(this.findSubjectUrl,{params:{examId:this.examId}}).then(
                    function(response){
                        this.subjects=response.data;
                });
				this.$http.get(this.getIndexRowsUrl,{params:{examId:this.examId, gradeId : this.gradeId}}).then(
                    function(response){
                        this.indexRows=response.data;
                        this.setIndexDialogFormVisible=true;
                });
			},
			submitIndexList : function(){
				/* var indexList=[];
				for(var i in this.indexRows){
					indexList=indexList.concat(this.indexRows[i].indexList);
				} */
				this.$http.post(this.saveIndexListUrl, this.indexRows).then(
                    function(response){
                    	this.$message({type:"info",message:"保存成功"});
                        this.setIndexDialogFormVisible=false;
                });
			},
			uploadIndex : function(){
          	  this.conditionVo.examId=this.examId;
          	  this.conditionVo.gradeId=this.gradeId;
          	  this.$refs.upload.submit();
          	  this.$refs.upload.clearFiles();
          	  this.uploadIndexDialogFormVisible = false;
          	  this.$message({
						type:"info",
						message: "导入成功"
				  });
            },
            checkExamAndGrade : function(){
          	  if(this.examId == null || this.examId==''){
  	              	this.$alert("请选择考试","提示",{
  						type:"warning",
  						confirmButtonText:'确认'
  					});
  					return false;
  	          }
          	  if(this.gradeId == null || this.gradeId==''){
	              	this.$alert("请选择年级","提示",{
						type:"warning",
						confirmButtonText:'确认'
					});
					return false;
	          	  }
          	  return true;
            },
            openUploadIndex : function(){
          	  if( ! this.checkExamAndGrade()){
  					return;
  	              }
                this.uploadIndexDialogFormVisible = true;
            },
            downloadTemplate : function(){
          	  window.location.href="<s:url value='/examAdmin/average/exportTemplate' />";
            },
            list : function(){
            	
            },
            exportAverage : function(){
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
   					var exportParams = {
   										examId : this.examId,
   										gradeId:this.gradeId
   								};
   					var url = "<s:url value='/examAdmin/average/exportAverage?'/>"+jQuery.param(exportParams);
   					/*  window.location.href = encodeURI(encodeURI(url));*/
   				    window.open(encodeURI(encodeURI(url)));	
   			},
            
   			
            
        }	        
});  
</script>
</body>
</html>
