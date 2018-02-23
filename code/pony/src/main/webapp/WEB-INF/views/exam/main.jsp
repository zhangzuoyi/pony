<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试管理</title>
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
	              <b>考试管理</b>
	              </el-col>
              </el-row>  
              <el-row>             
               <el-col :offset="18" :span="6">
               <el-button type="primary" @click="addExam">新增</el-button>       
               </el-col>             
              </el-row>            
            </div>
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    highlight-current-row
                   >               
                <el-table-column
                        prop="schoolYear.name"
                        label="学年"
                        >
                </el-table-column>
                <el-table-column
                        prop="term.name"
                        label="学期"
                        >
                </el-table-column>
                <el-table-column
                		prop="name"
                        label="名称"
                        >
                </el-table-column>
                <el-table-column
                        prop="type.name"
                        label="考试类型"
                        >                      
                </el-table-column>
                <el-table-column
                		prop="subjectsName"
                        label="科目"
                        >
                </el-table-column>
                <el-table-column
                        prop="classesName"
                        label="班级"
                        >                      
                </el-table-column>
               <el-table-column
                        prop="examDate"
                        label="考试日期"
                        >
                </el-table-column>                              
                <el-table-column                       
                        label="操作"
                        >
                 <template scope="scope">
                 <el-button size="small" @click="handleEdit(scope.$index,scope.row)">编辑</el-button>
                 <el-button size="small" type="danger" @click="handleDelete(scope.$index,scope.row)">删除</el-button>               
                 </template>                             
                </el-table-column>
            </el-table> 
        </el-card>
        
		<el-dialog  :visible.sync="dialogFormVisible" >
			<div slot="title" class="dialog-title">
                    <b>{{title}}</b>
                </div>
			<el-form :model="exam" :rules="rules" ref="ruleForm">			
			 <el-form-item label="学年" :label-width="formLabelWidth" > 
			 	{{exam.schoolYear.name }}
			 </el-form-item>
			 <el-form-item label="学期" :label-width="formLabelWidth" > 
			 	{{exam.term.name }}
			 </el-form-item>
			 <el-form-item label="考试类型" :label-width="formLabelWidth" > 
            	<el-select v-model="exam.type.typeId" placeholder="请选择">
                    <el-option v-for="x in types" :label="x.name" :value="x.typeId"></el-option>
                </el-select>
			 </el-form-item> 
			 <el-form-item label="科目" :label-width="formLabelWidth" > 
			 	<el-checkbox :indeterminate="isIndeterminateSubjects" v-model="checkAllSubjects" @change="handleCheckAllSubjects">全选</el-checkbox>
			  	<el-checkbox-group v-model="subjectIds" id="subjectsGroup" @change="handleCheckedSubjectChange">
					<el-checkbox v-for="x in subjects" :label="x.subjectId">{{x.name}}</el-checkbox>
				</el-checkbox-group>
             </el-form-item>
             <el-form-item label="名称" :label-width="formLabelWidth" > 
			  	<el-input v-model="exam.name"></el-input>
			 </el-form-item>
			 <el-form-item label="班级" :label-width="formLabelWidth" > 
			 	<el-checkbox :indeterminate="isIndeterminateClasses" v-model="checkAllClasses" @change="handleCheckAllClasses">全选</el-checkbox>
			  	<el-checkbox-group v-model="classIds" id="classesGroup" @change="handleCheckedClassChange">
					<el-checkbox v-for="x in schoolClasses" :label="x.classId">{{x.name}}</el-checkbox>
				</el-checkbox-group>
             </el-form-item>
			 <el-form-item label="考试日期" :label-width="formLabelWidth" > 
			 	<el-date-picker
                    v-model="exam.examDate"
                    type="date"
                    placeholder="选择日期">
            	</el-date-picker>
			 </el-form-item>
		    </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="onSubmit('ruleForm')"  >确定</el-button>
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				
			</div>
		</el-dialog>


		</div>
 

</div>

<script type="text/javascript">
	var app = new Vue({ 
	el : '#app' ,
	data : { 		
		exam:{examId:null,name:null,examDate:null,schoolYear:{},term:{},type:{}},
		subjectIds:[],
		classIds:[],
		dialogFormVisible:false,
		formLabelWidth:"120px",
		tableData:[],
		schoolYearUrl:"<s:url value='/schoolYear/getCurrent'/>",
		termUrl:"<s:url value='/term/getCurrent'/>",
		listUrl:"<s:url value='/exam/list'/>",
		examTypesUrl:"<s:url value='/exam/examTypes'/>",
		deleteUrl :"<s:url value='/exam/delete'/>",
		addUrl :"<s:url value='/exam/add2'/>",
		updateUrl :"<s:url value='/exam/edit2'/>",
		subjectListUrl :"<s:url value='/subject/list'/>",
		schoolClassListUrl :"<s:url value='/schoolClass/list'/>",
		title:"",
		types:[],
		schoolClasses:[],
		subjects:[],
		rules :{
		/* seq: [{required :true,message:"请填写顺序..",trigger:"blur"}]	 */			
		},
		isIndeterminateSubjects:true,
		checkAllSubjects:false,
		isIndeterminateClasses:true,
		checkAllClasses:false
		
	}, 
	
	mounted : function() { 
		this.getExamList();
		this.getCurrentSchoolYear();
		this.getCurrentTerm();
		this.getExamTypes();
		this.getSubjects();
		this.getSchoolClasses();
	}, 
	methods : { 
			getExamList : function(){
				this.$http.get(this.listUrl).then(
				function(response){
					this.tableData=response.data;
				},
				function(response){}  			
				); 
			},
			getCurrentSchoolYear	:function(){ 			
				this.$http.get(this.schoolYearUrl).then(
				function(response){
				this.schoolYear=response.data;
				 },
				function(response){}  	 			
				);
			},
			getCurrentTerm :function(){ 			
				this.$http.get(this.termUrl).then(
				function(response){
				this.term=response.data; },
				function(response){}  	 			
				);
			},
			getExamTypes :function(){ 			
				this.$http.get(this.examTypesUrl).then(
				function(response){
				this.types=response.data; },
				function(response){}  	 			
				);
			},
			getSubjects :function(){ 			
				this.$http.get(this.subjectListUrl).then(
				function(response){
				this.subjects=response.data; },
				function(response){}  	 			
				);
			},
			getSchoolClasses :function(){ 			
				this.$http.get(this.schoolClassListUrl).then(
				function(response){
				this.schoolClasses=response.data; },
				function(response){}  	 			
				);
			},
		handleEdit : function(index,row){
			this.title="修改上课时段";
			this.dialogFormVisible = true;
			this.exam = row;
			this.subjectIds=this.exam.subjectIds;
			this.classIds=this.exam.classIds;//['31','32','33','34','35','36'];
			
		},
		handleDelete : function(index,row){
			this.$confirm("确认删除吗？","提示",{
			confirmButtonText:'确认',
			cancleButtonText:'取消',
			type:'warning'			
			}).then(function(){  
			  app.$http.post(app.deleteUrl,{id:row.examId},{emulateJSON:true}).then(
					function(response){
						app.getExamList();
					 },
					function(response){}  			
					);  						
			})
			.catch(function(){ app.$message({ type:'info',message:'已取消删除'})});
			
		},
		addExam:function(){
			this.title="新增考试";
			this.dialogFormVisible = true;
			this.exam = {examId:null,name:null,examDate:null,schoolYear:this.schoolYear,term:this.term,type:{typeId: null}};
		},		
		onSubmit :function(formName){
			if(this.exam.examId == null ){
				this.add(formName);
			}else{
				this.update(formName);
			}
			
		},
		update : function(formName){
			app.exam.subjectIds=app.subjectIds;
			app.exam.classIds=app.classIds;
			app.$http.post(app.updateUrl,app.exam).then(
					function(response){
						app.dialogFormVisible=false;
						app.getExamList();
					 },
					function(response){console.log("error submit!");}
					);
		},
		add : function(formName){
			app.exam.subjectIds=app.subjectIds;
			app.exam.classIds=app.classIds;
			app.$http.post(app.addUrl,app.exam).then(
					function(response){
						app.dialogFormVisible=false;
						app.getExamList();
					 },
					function(response){console.log("error submit!");}
					);
		},
		handleCheckAllSubjects:function(event){
		    var allSubjects = [];
		    for(var i in this.subjects){
		    	allSubjects.push(this.subjects[i].subjectId);
		    }
			this.subjectIds = event.target.checked?allSubjects:[];
			this.isIndeterminateSubjects=false;
		},
		handleCheckedSubjectChange:function(value){
			var checkedCount = value.length;
			this.checkAllSubjects = checkedCount === this.subjects.length;
			this.isIndeterminateSubjects = checkedCount > 0 && checkedCount <this.subjects.length;
		},
		handleCheckAllClasses:function(event){
		    var allSubjects = [];
		    for(var i in this.schoolClasses){
		    	allSubjects.push(this.schoolClasses[i].classId);
		    }
			this.classIds = event.target.checked?allSubjects:[];
			this.isIndeterminateClasses=false;
		},
		handleCheckedClassChange:function(value){
			var checkedCount = value.length;
			this.checkAllClasses = checkedCount === this.schoolClasses.length;
			this.isIndeterminateClasses = checkedCount > 0 && checkedCount <this.schoolClasses.length;
		}
      }
	 
	
});  
	
</script>
</body>
</html>