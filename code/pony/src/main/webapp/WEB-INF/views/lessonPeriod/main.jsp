<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上课时段管理</title>
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
              <b>上课时段管理</b>
              </el-col>
              </el-row>  
              <el-row>             
               <el-col :offset="18" :span="6">
               <el-button type="primary" @click="addLessonPeriod">新增</el-button>       
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
                        prop="seq"
                        label="顺序"
                        >
                </el-table-column>
                <el-table-column
                		prop="startTime"
                        label="开始时间"
                        >
                </el-table-column>
                <el-table-column
                        prop="endTime"
                        label="结束时间"
                        >                      
                </el-table-column>
                <el-table-column
                		prop="yearName"
                        label="学年"
                        >
                </el-table-column>
                <el-table-column
                        prop="termName"
                        label="学期"
                        >                      
                </el-table-column>
               <el-table-column
                        prop="importanceName"
                        label="重要程度"
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
			<el-dialog  v-model="dialogFormVisible" >
			<div slot="title" class="dialog-title">
                    <b>{{title}}</b>
                </div>
			<el-form :model="lessonPeriod" :rules="rules" ref="ruleForm">			
			 <el-form-item label="学年" :label-width="formLabelWidth" prop="yearName" v-if="!addOrUpdate"> 
			 <el-input v-model="lessonPeriod.yearName" auto-complete="off"  readonly ></el-input> 
			 </el-form-item>
			 <el-form-item label="学期" :label-width="formLabelWidth" prop="termName" v-if="!addOrUpdate"> 
			 <el-input v-model="lessonPeriod.termName" auto-complete="off"   readonly></el-input> 
			 </el-form-item>
			 <el-form-item label="顺序" :label-width="formLabelWidth" prop="seq"> 
            	<el-input-number v-model="lessonPeriod.seq"  :min="1" :max="10" required></el-input-number>
			 </el-form-item> 
			 <el-form-item label="开始时间" :label-width="formLabelWidth" prop="startTime"> 
			  	<el-time-select
			  			v-model="lessonPeriod.startTime"
	                    :picker-options="{
	                    start: '06:00',
	                    step: '00:15',
	                    end: '22:30'
	                  }"
	                    placeholder="选择开始时间">
	            </el-time-select>
            </el-form-item>
            <el-form-item label="结束时间" :label-width="formLabelWidth" prop="endTime"> 
			  	<el-time-select
			  			v-model="lessonPeriod.endTime"			  		
	                    :picker-options="{
	                    start: '06:00',
	                    step: '00:15',
	                    end: '22:30'
	                  }"
	                    placeholder="选择结束时间">
	            </el-time-select>
			 </el-form-item>
			 <el-form-item label="重要程度" :label-width="formLabelWidth" prop="importance"> 
			 <el-select v-model="lessonPeriod.importance"  placeholder="请选择.."  > 
				<el-option v-for="x in importances" :label="x.value" :value="parseInt(x.code)">				
				</el-option> 				
				</el-select> 
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
		lessonPeriod:{periodId:null,seq:null,startTime: null,endTime:null,yearName:null,termName:null,importance:null},
		dialogFormVisible:false,
		formLabelWidth:"120px",
		tableData:[],
		importancesUrl:"<s:url value='/commonDict/importances'/>",							
		lessonPeriodsUrl:"<s:url value='/lessonPeriod/findBySchoolYearAndTerm'/>",		
		deleteUrl :"<s:url value='/lessonPeriod/delete'/>",
		addUrl :"<s:url value='/lessonPeriod/add'/>",
		updateUrl :"<s:url value='/lessonPeriod/edit'/>",
		title:"",
		importances:[],
		rules :{
		/* seq: [{required :true,message:"请填写顺序..",trigger:"blur"}]	 */			
		},
		addOrUpdate: true
		
	
		
	}, 
	/* filters: {    
    subjectTypeFilter: function (value) {
      if(value == 0 || value==1){return "上课科目"; }
      if(value == 2){return "选修科目"; }     
    }
    
  }	, */
	
	mounted : function() { 
		this.getLessonPeriods();
		this.getImportances();
		
		
			
	}, 
	methods : { 
			getLessonPeriods : function(){
			this.$http.get(this.lessonPeriodsUrl).then(
			function(response){
			this.tableData=response.data;},
			function(response){}  			
			); 
			} ,			
			getImportances : function(){
			this.$http.get(this.importancesUrl).then(
			function(response){
			this.importances=response.data;},
			function(response){}  			
			); 
			} ,
	   
		 
			
		handleEdit : function(index,row){
			this.title="修改上课时段";
			this.dialogFormVisible = true;
			this.lessonPeriod = row;
			this.addOrUpdate = false;
			
		},
		handleDelete : function(index,row){
			this.$confirm("确认删除吗？","提示",{
			confirmButtonText:'确认',
			cancleButtonText:'取消',
			type:'warning'			
			}).then(function(){  
			  app.$http.get(app.deleteUrl,{params:{id:row.periodId}}).then(
					function(response){
						app.getLessonPeriods();
					 },
					function(response){}  			
					);  						
			})
			.catch(function(){ app.$message({ type:'info',message:'已取消删除'})});
			
		},
		addLessonPeriod:function(){
			this.title="新增上课时段";
			this.dialogFormVisible = true;
			this.lessonPeriod = {periodId:null,seq:null,startTime:null,endTime:null,yearName:null,termName:null,importance:null};
			this.addOrUpdate = true;
			
			
		},		
		onSubmit :function(formName){
			if(this.lessonPeriod.periodId == null){
				this.add(formName);
			}else{
				this.update(formName);
			}
			
		},
		update : function(formName){
			this.$refs[formName].validate(function(valid){
				if(valid){
				app.$http.post(app.updateUrl,app.lessonPeriod).then(
					function(response){
						app.dialogFormVisible=false;
						app.lessonPeriod = {periodId:null,seq:null,startTime:null,endTime:null,yearName:null,termName:null,importance:null};
						app.getLessonPeriods();												 												
					 },
					function(response){}  			
					);
				
				
				
				}else{
				console.log("error submit!");				
				}
			});
			
						
			 
			
			
		},
		add : function(formName){
			this.$refs[formName].validate(function(valid){
				if(valid){
				app.$http.post(app.addUrl,app.lessonPeriod).then(
					function(response){
						app.dialogFormVisible=false;
						app.lessonPeriod = {periodId:null,seq:null,startTime:null,endTime:null,yearName:null,termName:null,importance:null};
						app.getLessonPeriods();										
					 },
					function(response){}  			
					);
				
				}else{
				}
			});									 						
		}							 			   		  
        }	        
	 
	
});  
	
</script>
</body>
</html>