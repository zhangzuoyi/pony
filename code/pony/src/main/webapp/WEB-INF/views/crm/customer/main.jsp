<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户管理</title>
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
<script type="text/javascript" src="<s:url value='/static/vue/myfilters.js' />"></script>
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
	              <b>客户管理</b>
	              </el-col>
              </el-row>               
              <el-row>  
              <el-col :span="2" >
                    <b>学生:</b>                                    
              </el-col>            
                <el-col :span="4">
					<el-select v-model="studentId" clearable filterable placeholder="请选择..">           		 
               		 <el-option
                        v-for="student in students" 
                        :label="student.name"                      
                        :value="student.studentId">
               		 </el-option>
           			 </el-select>				
                </el-col>
                <el-col :span="2" >
                    <b>组合形式:</b>                                    
              </el-col> 
                <el-col :span="4">
					<el-select v-model="group" clearable filterable placeholder="请选择..">           		 
               		 <el-option
                        v-for="x in groups" 
                        :label="x.group"                      
                        :value="x.group">
               		 </el-option>
           			 </el-select>				
                </el-col>
                
                
                <el-col  :span="4" :offset="4">
                <el-button type="primary" @click="list">查询</el-button> 
                </el-col>                                
              </el-row>
            </div>
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    highlight-current-row>               
                <el-table-column
                        prop="studentName"
                        label="学生"
                        >
                </el-table-column>
                <el-table-column
                        prop="group"
                        label="选课结果"
                        >
                </el-table-column>                                           
                <el-table-column                       
                        label="操作"
                        >
                 <template scope="scope">
                 <el-button size="small" @click="handleEdit(scope.$index,scope.row)">编辑</el-button>
                 <!-- <el-button size="small" type="danger" @click="handleDelete(scope.$index,scope.row)">删除</el-button>  -->              
                 </template>                             
                </el-table-column>
            </el-table> 
        </el-card>
        
		<el-dialog  v-model="dialogFormVisible" >
			<div slot="title" class="dialog-title">
                    <b>{{title}}</b>
                </div>
			<el-form :model="rowData" :rules="rules" ref="ruleForm">					 
			 <el-form-item  :label-width="formLabelWidth" >             	          	
            	<el-row > 
            	<el-col :span="2"> 
            	<b>科目:</b> 
            	</el-col> 
            	<el-col :span="22"> 
            	<el-checkbox-group v-model="rowData.selectSubjects" id="subjectsGroup"> 
            	<el-checkbox v-for="x in config.subjectArray" :label="x">{{x}}</el-checkbox> 
            	</el-checkbox-group> 
            	</el-col> 
				</el-row>
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
		config:{configId:null,subjects:null,selectNum:null,startTime:null,endTime:null,isCurrent:null,schoolYear:{}},
		dialogFormVisible:false,
		formLabelWidth:"50px",
		tableData:[],
		studentId : "",
		students : [],
		group : "",
		groups : [],
		rowData :{studentId:"",studentName:"",group:"",selectSubjects:[]},
		currentConfigUrl:"<s:url value='/ss/config/current'/>",
		studentUrl: "<s:url value='/studentAdmin/list'/>",
		groupsUrl: "<s:url value='/ss/statistics/group'/>",
		listUrl: "<s:url value='/ss/admin/list'/>",
		//deleteUrl :"<s:url value='/ss/config/delete'/>",
		//addUrl :"<s:url value='/ss/config/add'/>",
		updateUrl :"<s:url value='/ss/admin/edit'/>",
		title:"",
		rules :{
		/* seq: [{required :true,message:"请填写顺序..",trigger:"blur"}]	 */			
		}
		
	}, 
	
	mounted : function() { 
		this.getCurrentConfig();
		this.getStudents();
		this.getGroups();
	
	}, 
	methods : { 
		getCurrentConfig : function(){
				this.$http.get(this.currentConfigUrl).then(
				function(response){
					this.config=response.data;
				},
				function(response){}  			
				); 
			},
			getStudents : function(){
				this.$http.get(this.studentUrl).then(
				function(response){
					this.students=response.data;
				},
				function(response){}  			
				); 
			},
			getGroups : function(){
				this.$http.get(this.groupsUrl).then(
						function(response){
							this.groups=response.data;
						},
						function(response){}  			
						); 
					},
									
		handleEdit : function(index,row){
			this.title="修改选课";
			this.dialogFormVisible = true;
			this.rowData = row;
			
		},
		/* handleDelete : function(index,row){
			this.$confirm("确认删除吗？","提示",{
			confirmButtonText:'确认',
			cancleButtonText:'取消',
			type:'warning'			
			}).then(function(){  
			  app.$http.post(app.deleteUrl+"/"+row.configId).then(
					function(response){
						app.getConfigList();
					 },
					function(response){}  			
					);  						
			})
			.catch(function(){ app.$message({ type:'info',message:'已取消删除'})});
			
		}, */
			
		onSubmit :function(formName){		
				this.update(formName);				
		},
		update : function(formName){
			app.$http.post(app.updateUrl,app.rowData).then(
					function(response){
						app.dialogFormVisible=false;
						this.$message({type:"info",  message:"更新成功"});
						app.studentId="";
						app.list();
					 },
					function(response){console.log("error submit!");}
					);
		},
		
		
		list : function(){
			this.$http.get(this.listUrl,{params:{studentId:this.studentId,group:this.group}}).then(
					function(response){
						this.tableData=response.data;
					},
					function(response){}  			
					);
		}
		
      }
	 
	
});  
	
</script>
</body>
</html>