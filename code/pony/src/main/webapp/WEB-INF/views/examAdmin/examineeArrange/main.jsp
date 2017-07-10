<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考生设置</title>
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
              <b>考生设置</b>
              </el-col>
              </el-row>
              <el-row>                            
               <el-col :span="1" >
                    <b>学年:</b>                                    
            	</el-col> 
               <el-col  :span="4">
               		{{schoolYear.startYear}}——{{schoolYear.endYear}}
               </el-col>
               <el-col :span="1" >
                    <b>学期:</b>                                    
            	</el-col> 
               <el-col  :span="4">
               		{{term.name}}
               </el-col>
             	<el-col :span="1" >
                    <b>考试:</b>                                    
            	</el-col> 
           		 <el-col :span="4" >
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
            	<el-col :span="4" >
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
            	
            	<el-col :span="2" >
               		<el-button type="primary"  >查询</el-button>
               		<el-button type="primary"  >生成考生号</el-button>
              	</el-col>                           
              </el-row>
              <el-row>                            
               <el-col  :span="3">
               <el-button type="primary" >设置考生</el-button>
               </el-col>
               <el-col  :span="3">
               <el-button type="primary" >删除考生</el-button>
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
                        prop="subjectName"
                        label="科目"
                        >
                </el-table-column>
                <el-table-column
                		prop="examDate"
                        label="考试日期"
                        >
                </el-table-column>
                <el-table-column
                		prop="startTime"
                        label="开始时间"
                        >
                </el-table-column><el-table-column
                		prop="endTime"
                        label="结束时间"
                        >
                </el-table-column><el-table-column
                		prop="capacity"
                        label="考生人数"
                        >
                </el-table-column>
                <el-table-column                       
                        label="操作"
                        style="width:100px;"  
                        >
                 <template scope="scope">
                 <el-button size="small" @click="handleEdit(scope.$index,scope.row)">查看考生</el-button>                             
                 </template>                             
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
			<el-dialog title="设置考生"  v-model="subjectDialogFormVisible" >		
			<el-row>
			<el-col :span="4">
			<b>按班级:</b>
			</el-col>
			<el-col>
			<b>年级:{{gradeName}}</b>
			</el-col>
			</el-row>
			<el-row>
			<b>班级</b>
			</el-row>
			<el-row>
			<el-checkbox-group v-model="checkedClasses" @change="handleCheckedClassChange">
				<el-checkbox v-for="class in classes" :label="class.classId">{{class.className}}</el-checkbox>
			</el-checkbox-group>
			</el-row>
			<el-row>
			<el-button size="small" >确认</el-button>
			</el-row>
			<el-row>
			<el-col :span="4">
			<b>按考生:</b>
			</el-col>
			<el-col :span="4">
			<b>年级:{{gradeName}}</b>
			</el-col>
			<el-col>
			<el-select>
			</el-select>
			</el-col>
			</el-row>						
			<el-row>
			<el-table>
			</el-table>
			</el-row>
			<el-row>
			<el-button size="small" >确认</el-button>
			</el-row>
		
			</el-dialog>
			
			<el-dialog title="查看考生"  v-model="examDateDialogFormVisible" >		
			<el-row>
			<el-col :span="4">
			<b>年级:{{gradeName}</b>
			</el-col>
			<el-col :span="4">
			<b>班级</b>
			</el-col>
			<el-col :span="8">
			<el-select>
			</el-select>
			</el-col>
			<el-col>
			<el-button size="small" >查询</el-button>
			</el-col>
			</el-row>
			<el-row>
			<el-table>
			</el-table>
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
		examSubjectUrl:"<s:url value='/subject/findByExam'/>",
		/* examRoomUrl: "<s:url value='/examAdmin/examRoom/list'/>", */   
        listPageUrl:"<s:url value='/examAdmin/examArrange/listPage'/>",
        addUrl:"<s:url value='/examAdmin/examArrange/add'/>",
        addExamDateUrl:"<s:url value='/examAdmin/examArrange/addExamDate'/>",
        addExamTimeUrl:"<s:url value='/examAdmin/examArrange/addExamTime'/>",
        addGroupUrl:"<s:url value='/examAdmin/examArrange/addGroup'/>",
        deleteUrl:"<s:url value='/examAdmin/examArrange/delete'/>",
		schoolYear :null,
		term : null,
		examId: null,
		exams:[],
		grades : [],	
		gradeId:null,
		/* rooms : [],
		roomId : null, */	
		tableData:[],						
		currentPage : 1,
		pageSizes :[20,50,100],
		pageSize:[20],
		total:null,
		checkAll:true,
		isIndeterminate:true,
		checkedSubjects:[],
		subjects:[],
		examTime:[new Date(2017, 1, 1, 8, 00), new Date(2050, 1, 1, 10, 00)],
		subjectDialogFormVisible:false,
		examDateDialogFormVisible:false,
		examTimeDialogFormVisible:false,
		examRoomDialogFormVisible:false,
		examGroupDialogFormVisible:false,
		examDate :null,
		groupName:null,
		multipleSelection:[]
		
	
		
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
				/* this.getExamRooms(); */
				this.getExamArranges();
				
		
			
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
			/* getExamRooms : function(){
			this.$http.get(this.examRoomUrl).then(
			function(response){this.examRooms=response.data; },
			function(response){}  	 			
			);
			}, */
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
			},
			pickerOptions: function(time){
                        return time.getTime() < Date.now() - 8.64e7;
                    
                },
			handleSizeChange :function(val){
			//console.log('每页  ${val}条');
			this.currentPage = 1;
			this.pageSize = val;
			//this.getUsers();
			
			},
			handleCurrentChange: function(val){
				this.currentPage = val;
				//this.getUsers();
				//console.log('当前页 : ${val}');
			},
			selectSubject: function(){
                if (this.examId == null || this.examId == '' ||this.gradeId == null||this.gradeId==''){
                    this.$alert("请选择考试和年级","提示",{
                        type:"warning",
                        confirmButtonText:'确认'
                    });
                    return ;
                }
				this.subjectDialogFormVisible = true;
			},
			selectExamDate: function(){
			    if(this.multipleSelection==null||this.multipleSelection.length == 0){
			     this.$alert("请选择科目","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
			    }		    	    
				this.examDateDialogFormVisible = true;
			},
			selectTime: function(){
				if(this.multipleSelection==null||this.multipleSelection.length == 0){
			     this.$alert("请选择科目","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
			    }		    	    
				this.examTimeDialogFormVisible = true;
			},
			selectExamRoom: function(){
			
			},
			joinGroup: function(){
				if(this.multipleSelection==null||this.multipleSelection.length == 0){
			     this.$alert("请选择科目","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
			    }		
			    this.examGroupDialogFormVisible=true;    	    
			},
			
            getExamArranges:function(){
                this.$http.get(this.listPageUrl,{params:{currentPage:this.currentPage-1,pageSize:this.pageSize,examId:this.examId,gradeId:this.gradeId}}).then(
                    function(response){
                        this.tableData=response.data.content;
                        //已选择的科目从备选项中移除掉
                        if(this.subjects.length>0){
                            for(var index in this.subjects){
                                for(var idx in this.tableData){
                                    if(this.subjects[index].subjectId == this.tableData[idx].subjectId){
                                        this.subjects.splice(index,1);
                                    }
                                }
                            }
                        }


                        },
                    function(response){}
                );
            },
            onSubmitSubject:function () {


                this.$http.get(this.addUrl,{params:{subjects:this.checkedSubjects,examId:this.examId,gradeId:this.gradeId}}).then(
                    function(response){
                          this.$message({type:"info",message:"新增成功"});

                          this.getExamArranges();                       
                          this.checkedSubjects = [];
                          this.subjectDialogFormVisible = false;
                          this.isIndeterminate =true;


                    },
                    function(response){}
                );
            },
            onSubmitExamDate : function(){
              if(this.examDate == null || this.examDate==''){
              	this.$alert("请选择时间","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }           
              var examArranges = [];
              for(var index in this.multipleSelection){
               	examArranges.push(this.multipleSelection[index].arrangeId);
              }
              var year = this.examDate.getFullYear();
              var month = this.examDate.getMonth()+1;
              var day = this.examDate.getDate();
              var examDate = year+"-"+month+"-"+day;
              
              this.$http.get(this.addExamDateUrl,{params:{examArranges:examArranges,examDate:examDate}}).then(
                    function(response){
                          this.$message({type:"info",message:"更新成功"});                        
                          this.getExamArranges(); 
                          this.multiplSelection = []; 
                          this.$refs.multipleTable.clearSelection();                     
                          this.examDateDialogFormVisible = false;
                          this.examDate = null;
                    },
                    function(response){}
                );                          
            }	,
            onSubmitExamTime : function(){
              if(this.examTime == null || this.examTime==''){
              	this.$alert("请选择时间","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }           
              var examArranges = [];
              for(var index in this.multipleSelection){
               	examArranges.push(this.multipleSelection[index].arrangeId);
              }
              /* var year = this.examDate.getFullYear();
              var month = this.examDate.getMonth()+1;
              var day = this.examDate.getDate();
              var examDate = year+"-"+month+"-"+day; */
              var startTime = this.examTime[0].getHours()+":"+this.examTime[0].getMinutes()+":"+this.examTime[0].getSeconds();
              var endTime = this.examTime[1].getHours()+":"+this.examTime[1].getMinutes()+":"+this.examTime[1].getSeconds();
              
             
              this.$http.get(this.addExamTimeUrl,{params:{examArranges:examArranges,startTime:startTime,endTime:endTime}}).then(
                    function(response){
                          this.$message({type:"info",message:"更新成功"});                        
                          this.getExamArranges(); 
                          this.multiplSelection = []; 
                          this.$refs.multipleTable.clearSelection();                     
                          this.examTimeDialogFormVisible = false;
                          this.examTime = [];
                    },
                    function(response){}
                );                         
            }	,
            onSubmitExamGroup : function(){
            	if(this.multipleSelection==null||this.multipleSelection.length == 0){
			     this.$alert("请选择科目","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
			    }
			    if(this.groupName == null|| this.groupName ==""){
			     this.$alert("请填写组名","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
			    }
			    var examArranges = [];
              for(var index in this.multipleSelection){
               	examArranges.push(this.multipleSelection[index].arrangeId);
              }
			    this.$http.get(this.addGroupUrl,{params:{examArranges:examArranges,groupName:this.groupName,examId:this.examId,gradeId:this.gradeId}}).then(
                    function(response){
                          this.$message({type:"info",message:"加入组合成功"});                        
                          this.getExamArranges();
                          this.multiplSelection = []; 
                          this.$refs.multipleTable.clearSelection();                                                                                             
                          this.groupName =null;
                          this.examGroupDialogFormVisible=false;
                    },
                    function(response){}
                );			               
            },
            handleDelete: function(){
            if(this.multipleSelection==null||this.multipleSelection.length == 0){
			     this.$alert("请选择科目","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
			    }
                var examArranges = [];
                for(var index in this.multipleSelection){
                    examArranges.push(this.multipleSelection[index].arrangeId);
                }
			   this.$http.get(this.deleteUrl,{params:{examArranges:examArranges}}).then(
                    function(response){
                          this.$message({type:"info",message:"删除成功"});                        
                          this.getExamArranges();
                          this.multiplSelection = []; 
                          this.$refs.multipleTable.clearSelection();                                                                                             
                    },
                    function(response){}
                ); 
			    
			    
            }
            
			
			
            
            		   		  
        }	        
	 
	
});  

</script>
</body>
</html>
