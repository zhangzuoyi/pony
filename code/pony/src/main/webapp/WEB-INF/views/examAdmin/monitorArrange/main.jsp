<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>监考设置</title>
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
              <b>监考设置</b>
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
            	
            	<el-col :span="4" >
               		<el-button type="primary"  @click="listByPage">查询</el-button>
              	</el-col>                           
              </el-row>
              <el-row>                            
               <el-col  :span="3">
               <el-button type="primary" @click="setTeacher">设置监考老师</el-button>
               </el-col>
               <el-col  :span="3">
               <el-button type="primary" @click="deleteExaminee" >删除</el-button>
               </el-col>
                           
              </el-row>
            </div>
            <el-table
            		ref="multipleTable"
                    :data="tableData"
                    border
                    style="width: 100%"
					@selection-change="handleSelectionChange"
                   >               
                <el-table-column
                        type="selection"
                        width="50">
                </el-table-column>
                <el-table-column
							prop="name"
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
            </el-table>
            <el-row>
            <el-col :offset="18" :span="6">
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="pageSizes"
                :page-size="pageSize"
                layout="total,sizes,prev,pager,next,jumper"
                :total="total"
                ></el-pagination>
            </el-col>          
			</el-row>			
        </el-card>
			<el-dialog title="设置监考老师"  v-model="setTeacherDialogFormVisible" >
			<el-row>
			<el-col>
			<b>班级</b>
			</el-col>
			<el-col>
				<el-input placeholder="请输入.." v-model="filterText"></el-input>
			</el-col>
			</el-row>
			<el-row>
				<el-table
						:data="usedTeachers"
						height="250"
						border
						@selection-change="handleSelectionChange2">
					<el-table-column
							type="selection"
							>
					</el-table-column>
					<el-table-column
							prop="name"
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
				</el-table>	
			</el-row>
			<el-row>
			<el-button type="primary" size="small" @click="submitByStudent">确认</el-button>
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
		getTeacherUrl:"<s:url value='/teacherAdmin/listAllVo'/>",
        schoolYear :null,
		term : null,
		examId: null,
		exams:[],
		teachers:[],
		usedTeachers:[],
		filterText:'',
		tableData:[],
		tableData2:[],/*设置考生使用*/
		tableData3:[],/*查看考生使用*/
		currentPage : 1,
		currentPage2:1,
		currentPage3:1,		
		pageSizes :[20,50,100],
		pageSize:[20],
		pageSize2:[20],
		pageSize3:[20],					
		total:null,
		total2:null,
		total3:null,		
		checkedClasses:[],
		classes:[],
        setTeacherDialogFormVisible:false,
        findExamineeDialogFormVisible:false,
        generateExamineeNoDialogFormVisible:false,
        gradeName:null,
        schoolClass:null,
        schoolClass2:null,
        multipleSelection:[],
        multipleSelection2:[],
        prefixNo:null,
        bitNo:null,
        flag:true,//按照班级或者按照考生
        arrangeId:null,//用来记录查看考生时选择的科目
        isGenerateShowFlag:true
	}, 
	mounted : function() { 
				this.getCurrentSchoolYear();
				this.getCurrentTerm();
				this.getExams();
				this.getTeachers();
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
			getTeachers	:function(){
				this.$http.get(this.getTeacherUrl).then(
				function(response){
					this.teachers=response.data; 
					this.usedTeachers=this.teachers;
				},
				function(response){}  	 			
				);
			},
			handleSelectionChange: function(val) {
                this.multipleSelection = val;
            },
            handleSelectionChange2: function(val) {
            this.multipleSelection2 = val;
            },
			handleSizeChange :function(val){
			//console.log('每页  ${val}条');
			this.currentPage = 1;
			this.pageSize = val;

			},
			handleCurrentChange: function(val){
				this.currentPage = val;
				//console.log('当前页 : ${val}');
			},
			handleSizeChange2 :function(val){
			//console.log('每页  ${val}条');
			this.currentPage2 = 1;
			this.pageSize2 = val;

			},
			handleCurrentChange2: function(val){
				this.currentPage2 = val;
				//console.log('当前页 : ${val}');
			},
			handleSizeChange3 :function(val){
			//console.log('每页  ${val}条');
			this.currentPage3 = 1;
			this.pageSize3 = val;

			},
			handleCurrentChange3: function(val){
				this.currentPage3 = val;
				//console.log('当前页 : ${val}');
			},
            setTeacher: function(){
            if(this.examId == null || this.examId==''){
              	this.$alert("请选择考试","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
                this.setTeacherDialogFormVisible = true;
             },
            handleFind :function(index,row){
            	this.tableData3=[];
            	this.schoolClass2 = null;
                this.findExamineeDialogFormVisible = true;
                this.arrangeId = row.arrangeId;
             },
            deleteExaminee: function(){
				if(this.multipleSelection==null||this.multipleSelection.length==0){
				this.$alert("请选择考试科目","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
				}
			  var arrangeIds = [];
              for(var index in this.multipleSelection){
              	arrangeIds.push(this.multipleSelection[index].arrangeId);
              }
				this.$http.get(this.deleteUrl,{params:{arrangeIds:arrangeIds}}).then(
                    function(response){
                        this.$message({type:"info",message:"删除成功"});                   
                        this.multipleSelection=[];
                        });
				
				
            },
            listByPage : function(){
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
              this.$http.get(this.listByPageUrl,{params:{currentPage:this.currentPage-1,pageSize:this.pageSize,examId:this.examId,gradeId:this.gradeId}}).then(
                    function(response){
                        this.tableData=response.data.content;
                        this.total = response.data.totalElements;
                        });
		
            },
            submitByClass:function(){
				
              if(this.checkedClasses == null || this.checkedClasses.length ==0){
              this.$alert("请选择班级","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;				
              }
              
              var arrangeIds = [];
              for(var index in this.multipleSelection){
              	arrangeIds.push(this.multipleSelection[index].arrangeId);
              }
			 this.$http.get(this.submitByClassUrl,{params:{examId:this.examId,classIds:this.checkedClasses,arrangeIds:arrangeIds}}).then(
                    function(response){
                    	this.$message({type:"info",message:"设置成功"});
                        //this.examId = null;
                        //this.gradeId = null;
                        //this.gradeName = null;
                        this.checkedClasses=[];
                        this.multipleSelection=[];
                        this.setExamineeDialogFormVisible=false;
                        this.flag=true;
                        });


            },
            submitByStudent:function(){				
              if(this.multipleSelection2 ==null||this.multipleSelection2.length==0 ){
              this.$alert("请选择考生","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
              var arrangeIds = [];
              for(var index in this.multipleSelection){
              	arrangeIds.push(this.multipleSelection[index].arrangeId);
              }
              var examineeIds = [];
              for(var index in this.multipleSelection2){
              	examineeIds.push(this.multipleSelection2[index].examineeId);        
              }
              
              this.$http.get(this.submitByStudentUrl,{params:{examineeIds:examineeIds,arrangeIds:arrangeIds}}).then(
                    function(response){
                        this.$message({type:"info",message:"设置成功"});                   
                        this.examId = null;
                        this.gradeId = null;
                        this.gradeName = null;
                        this.checkedClasses=[];
                        this.multipleSelection=[];
                        this.multipleSelection2=[];
                        this.setExamineeDialogFormVisible=false;
                        this.flag=true;                        
                        });
              

            }
        }	        
});  
</script>
</body>
</html>
