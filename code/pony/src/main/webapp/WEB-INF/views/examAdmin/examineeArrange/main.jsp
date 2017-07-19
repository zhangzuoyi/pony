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
					<el-select v-model="gradeId" @change="getSchoolClasses()"  filterable clearable placeholder="请选择..">
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
               		<el-button type="primary"  @click="listByPage">查询</el-button>
               		<el-button type="primary"  @click="generateExamineeNo">生成考生号</el-button>
              	</el-col>                           
              </el-row>
              <el-row>                            
               <el-col  :span="3">
               <el-button type="primary" @click="setExaminee">设置考生</el-button>
               </el-col>
               <el-col  :span="3">
               <el-button type="primary" @click="deleteExaminee" >删除考生</el-button>
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
                 <el-button size="small" @click="handleFind(scope.$index,scope.row)">查看考生</el-button>
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
			<el-dialog title="设置考生"  v-model="setExamineeDialogFormVisible" >
                <el-row>
                    <el-select v-model="flag">
                        <el-option :label="'按班级'" :value="true"><span style="float: left">按班级</span></el-option>
                        <el-option :label="'按考生'" :value="false"><span style="float: left">按考生</span></el-option>
                    </el-select>
                </el-row>
                <div v-if="flag">
            <el-row>
			<el-col>
			<b>年级:{{gradeName}}</b>
			</el-col>
			</el-row>
			<el-row>
			<b>班级</b>
			</el-row>
			<el-row>
			<el-checkbox-group v-model="checkedClasses" >
				<el-checkbox v-for="item in classes" :label="item.classId">{{item.name}}</el-checkbox>
			</el-checkbox-group>
			</el-row>
			<el-row>
			<el-button type="primary" size="small" @click="submitByClass">确认</el-button>
			</el-row>>
            </div>
                <div v-if="!flag">
			<el-row>
			<el-col>
			<b>年级:{{gradeName}}</b>
			</el-col>
			</el-row>
			<el-row>
			<el-col>
			<b>班级</b>
			</el-col>
			<el-col>
				<el-select v-model="schoolClass" @change="getExaminee()" filterable  clearable placeholder="请选择">
					<el-option
							v-for="item in classes"
							:label="item.name"
                          	:value="item.classId">
					</el-option>
				</el-select>
			</el-col>
			</el-row>
			<el-row>
				<el-table
						:data="tableData2"
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
							prop="sex"
							label="性别"
							width="180">
					</el-table-column>
					<el-table-column
							prop="studentNo"
							label="学生号">
					</el-table-column>
					<el-table-column
							prop="examineeNo"
							label="考生号">
					</el-table-column>
				</el-table>	
				<el-row>	
				<el-col :offset="15" :span="9">	
				<el-pagination
                @size-change="handleSizeChange2"
                @current-change="handleCurrentChange2"
                :current-page="currentPage2"
                :page-sizes="pageSizes"
                :page-size="pageSize2"
                layout="total,sizes,prev,pager,next,jumper"
                :total="total2"
                ></el-pagination>
                </el-col>
                </el-row>
			</el-row>
			<el-row>
			<el-button type="primary" size="small" @click="submitByStudent">确认</el-button>
			</el-row>
             </div>
			</el-dialog>

      	<el-dialog title="查看考生"  v-model="findExamineeDialogFormVisible" >
          <el-row>
          <el-col :span="4">
          <b>年级:{{gradeName}}</b>
          </el-col>
          <el-col :span="4">
          <b>班级</b>
          </el-col>
          <el-col :span="8">
              <el-select v-model="schoolClass2" filterable  clearable placeholder="请选择">
                  <el-option
                          v-for="item in classes"
                          :label="item.name"
                          :value="item.classId">
                  </el-option>
              </el-select>
          </el-col>
          <el-col>
          <el-button type="primary" size="small" @click="findExaminee">查询</el-button>
          </el-col>
          </el-row>
          <el-row>
              <el-table
                      :data="tableData3"
                      highlight-current-row
                      border
                      height="250"
                      >
                  <el-table-column
                          type="index"
                  >
                  </el-table-column>
                  <el-table-column
                          prop="name"
                          label="姓名"
                  >
                  </el-table-column>
                  <el-table-column
                          prop="sex"
                          label="性别"
                          width="180">
                  </el-table-column>
                  <el-table-column
                          prop="studentNo"
                          label="学生号">
                  </el-table-column>
                  <el-table-column
                          prop="examineeNo"
                          label="考生号">
                  </el-table-column>
              </el-table>
          </el-row>
          <el-row>
          <el-col :offset="15" :span="9">
          <el-pagination
                @size-change="handleSizeChange3"
                @current-change="handleCurrentChange3"
                :current-page="currentPage3"
                :page-sizes="pageSizes"
                :page-size="pageSize3"
                layout="total,sizes,prev,pager,next,jumper"
                :total="total3"
                ></el-pagination>
          </el-col>
          </el-row>
          </el-dialog>
          <el-dialog title="设置考生号"  v-model="generateExamineeNoDialogFormVisible" >
          <el-row>
          <el-col :span="4">
          <b>考生号前缀:</b>
          </el-col>
          <el-col :span="8">
          <el-input v-model="prefixNo"></el-input>
          </el-col>
          </el-row>
          <el-row>
          <el-col :span="4">
          <b>考生序号位数::</b>
          </el-col>         
          <el-col :span="8">  
          <el-input-number v-model="bitNo"></el-input-number>          
          </el-col>      
          </el-row>
          <div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="onSubmitGenerateNo()"  >确定</el-button>
				<el-button @click="generateExamineeNoDialogFormVisible = false">取 消</el-button>				
			</div>
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
		listByPageUrl:"<s:url value='/examAdmin/examArrange/listPage'/>",
		schoolClassesUrl:"<s:url value='/schoolClass/findByGrade'/>",
		gradeUrl:"<s:url value='/grade/get'/>",
		generateNoUrl:"<s:url value='/examAdmin/examinee/generateNo'/>",
        submitByClassUrl:"<s:url value='/examAdmin/examineeArrange/submitByClass'/>",
        submitByStudentUrl:"<s:url value='/examAdmin/examineeArrange/submitByStudent'/>",
        getExamineeUrl:"<s:url value='/examAdmin/examinee/listPageByClass'/>",
        findExamineeUrl:"<s:url value='/examAdmin/examinee/listPageByClassAndArrange'/>", 
        deleteUrl:"<s:url value='/examAdmin/examineeArrange/delete'/>",             
        schoolYear :null,
		term : null,
		examId: null,
		exams:[],
		grades : [],	
		gradeId:null,
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
        setExamineeDialogFormVisible:false,
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
        arrangeId:null//用来记录查看考生时选择的科目





	
		
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
            getExamSubjects	:function(){this.$http.get(this.examSubjectUrl,{params:{examId:this.examId}}).then(
                function(response){
                    this.subjects=response.data; },
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
            setExaminee: function(){
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
              if(this.multipleSelection ==null||this.multipleSelection.length==0 ){
              this.$alert("请选择考试科目","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
                this.setExamineeDialogFormVisible = true;
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
            getSchoolClasses:function(){        
             this.$http.get(this.schoolClassesUrl,{params:{gradeId:this.gradeId}}).then(
                    function(response){
                        this.classes=response.data;
                        });
             this.$http.get(this.gradeUrl,{params:{id:this.gradeId}}).then(
                    function(response){
                        this.gradeName=response.data.name;
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
            generateExamineeNo :function(){
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
				this.generateExamineeNoDialogFormVisible = true;
            },
            onSubmitGenerateNo : function(){
            
            if(this.prefixNo == null || this.examId==''){
              	this.$alert("请选择前缀","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
              if(this.bitNo == null || this.examId==''){
              	this.$alert("请选择位数","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
              this.$http.get(this.generateNoUrl,{params:{examId:this.examId,gradeId:this.gradeId,prefixNo:this.prefixNo,bitNo:this.bitNo}}).then(
                    function(response){
                        this.examId = null;
                        this.gradeId= null;
                        this.prefixNo= null;
                        this.bitNo=null;
                        this.generateExamineeNoDialogFormVisible=false;
                        this.$message({type:"info",message:"生成成功"});                       
                        
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
                        this.examId = null;
                        this.gradeId = null;
                        this.gradeName = null;
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
              

            },
            getExaminee :function(){
            if(this.examId == null || this.examId==''){
              	this.$alert("请选择考试","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
              if(this.schoolClass == null || this.schoolClass==''){
              	this.$alert("请选择班级","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
             this.$http.get(this.getExamineeUrl,{params:{currentPage:this.currentPage2-1,pageSize:this.pageSize2,examId:this.examId,classId:this.schoolClass}}).then(
                    function(response){
                        this.tableData2=response.data.content;
                        this.total2 = response.data.totalElements;                       
                        });
            
            },
            findExaminee :function(){
            if(this.examId == null || this.examId==''){
              	this.$alert("请选择考试","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
              if(this.schoolClass2 == null || this.schoolClass2==''){
              	this.$alert("请选择班级","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
              if(this.arrangeId == null || this.arrangeId==''){
              	this.$alert("请选择考试科目","提示",{
					type:"warning",
					confirmButtonText:'确认'
				});
				return;
              }
             this.$http.get(this.findExamineeUrl,{params:{currentPage:this.currentPage3-1,pageSize:this.pageSize3,examId:this.examId,classId:this.schoolClass2,arrangeId:this.arrangeId}}).then(
                    function(response){
                        this.tableData3=response.data.content;
                        this.total3 = response.data.totalElements;                       
                        });
            
            }





			






            
			
			
            
            		   		  
        }	        
	 
	
});  

</script>
</body>
</html>
