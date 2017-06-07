<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>科目管理</title>
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
              <b>科目管理</b>
              </el-col>
              </el-row>  
              <el-row>             
               <el-col :offset="18" :span="6">
               <el-button type="primary" @click="addSubject">新增</el-button>       
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
                        prop="subjectId"
                        label="ID"
                        >
                </el-table-column>
                <el-table-column
                		prop="name"
                        label="科目名称"
                        >
                </el-table-column>
                <el-table-column
                        inline-template
                        label="科目类型"
                        >
                        <div>{{row.type | subjectTypeFilter }}</div>
                </el-table-column>
               <el-table-column
                        inline-template
                        label="重要程度"
                        >
                        <div>{{row.importanceName}}</div>
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
			<el-form :model="subject" :rules="rules" ref="ruleForm">			
			 <el-form-item label="科目名称" :label-width="formLabelWidth" prop="name"> 
			 <el-input v-model="subject.name" auto-complete="off"   required></el-input> 
			 </el-form-item> 
			 <el-form-item label="科目类型" :label-width="formLabelWidth" prop="type"> 
			 <el-select v-model="subject.type" placeholder="请选择.."  > 
				<el-option  
						:label="'上课科目'"                   
                        :value="0">
                        <span style="float: left">上课科目</span>
               		 </el-option>
               		<el-option  
						:label="'上课科目'"                   
                        :value="1">
                        <span style="float: left">上课科目</span>
               		 </el-option>
               		 <el-option                       
                        :label="'选修科目'"                       
                        :value="2">
                        <span style="float: left">选修科目</span>
               		 </el-option>				
				</el-select> 
			 </el-form-item>
			 <el-form-item label="重要程度" :label-width="formLabelWidth" prop="importance"> 
			 <el-select v-model="subject.importance" placeholder="请选择.."  > 
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
		subject:{subjectId:null,name:null,type:null,importance:null},
		dialogFormVisible:false,
		formLabelWidth:"120px",
		tableData:[],
		subjectTypesUrl:"<s:url value='/commonDict/subjectTypes'/>",	
		importancesUrl:"<s:url value='/commonDict/importances'/>",							
		subjectsUrl:"<s:url value='/subject/list'/>",		
		deleteUrl :"<s:url value='/subject/delete'/>",
		addUrl :"<s:url value='/subject/add'/>",
		updateUrl :"<s:url value='/subject/edit'/>",
		title:"",
		subjectTypes:[],
		importances:[],
		rules :{
		name: [{required :true,message:"请填写科目名称..",trigger:"blur"}]				
		}
	
	
		
	}, 
	filters: {    
    subjectTypeFilter: function (value) {
      if(value == 0 || value==1){return "上课科目"; }
      if(value == 2){return "选修科目"; }     
    }
    
  }	,
	
	mounted : function() { 
		this.getSujects();
		this.getSujectTypes();
		this.getImportances();
		
		
			
	}, 
	methods : { 
			getSujects : function(){
			this.$http.get(this.subjectsUrl).then(
			function(response){
			this.tableData=response.data;},
			function(response){}  			
			); 
			} ,
			getSujectTypes : function(){
			this.$http.get(this.subjectTypesUrl).then(
			function(response){
			this.subjectTypes=response.data;},
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
			this.title="修改科目";
			this.dialogFormVisible = true;
			this.subject = row;
			
		},
		handleDelete : function(index,row){
			this.$confirm("确认删除吗？","提示",{
			confirmButtonText:'确认',
			cancleButtonText:'取消',
			type:'warning'			
			}).then(function(){  
			  app.$http.get(app.deleteUrl,{params:{id:row.subjectId}}).then(
					function(response){
						app.getSujects();
					 },
					function(response){}  			
					);  						
			})
			.catch(function(){ app.$message({ type:'info',message:'已取消删除'})});
			
		},
		addSubject:function(){
			this.title="新增科目";
			this.dialogFormVisible = true;
			this.subject = {subjectId:null,name:null,type:null,importance:null};
			
		},		
		onSubmit :function(formName){
			if(this.subject.subjectId == null){
				this.add(formName);
			}else{
				this.update(formName);
			}
			
		},
		update : function(formName){
			this.$refs[formName].validate(function(valid){
				if(valid){
				app.$http.post(app.updateUrl,app.subject).then(
					function(response){
						app.dialogFormVisible=false;
						app.subject = {subjectId:null,name:null,type:null,importance:null};
						app.getSujects();												 												
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
				app.$http.post(app.addUrl,app.subject).then(
					function(response){
						app.dialogFormVisible=false;
						app.subject = {subjectId:null,name:null,type:null,importance:null};
						app.getSujects();										
					 },
					function(response){}  			
					);
				
				}else{
				}
			});
			
			
			 
			
			
		},
		
			
			 
			   	
	  
        }	        
	 
	
});  
	
</script>
</body>
</html>