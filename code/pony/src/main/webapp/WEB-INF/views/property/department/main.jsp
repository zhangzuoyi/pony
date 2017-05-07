<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>部门管理</title>
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
              <b>部门管理</b>
              </el-col>
              </el-row>
              <el-row>
               <el-col :offset="16">
                <el-button type="primary" @click="addDepartment">新增部门</el-button>                                
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
                        prop="deptId"
                        label="ID"
                        >
                </el-table-column>
                <el-table-column
                		prop="name"
                        label="部门"
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
			<el-form :model="department">			 
			 <el-form-item label="部门名称" :label-width="formLabelWidth"> 
			  <el-input v-model="department.name" auto-complete="off"></el-input>
			  </el-form-item>
		    </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="onSubmit">确定</el-button>
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				
			</div>
			</el-dialog>


		</div>
 

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		
		department:{name:null},
		dialogFormVisible:false,
		formLabelWidth:"120px",
		tableData:[],
		departmentUrl:"<s:url value='/property/department/list'/>",
		deleteUrl :"<s:url value='/property/department/delete'/>",
		addUrl :"<s:url value='/property/department/add'/>",
		updateUrl :"<s:url value='/property/department/update'/>",
		title:"",
	
	
		
	}, 
	
	mounted : function() { 
		this.getDepartment();
			
	}, 
	methods : { 
	   
		getDepartment : function(){
			
			 
			this.$http.get(this.departmentUrl).then(
			function(response){
			this.tableData=response.data;},
			function(response){}  			
			); 
			} ,
			
		handleEdit : function(index,row){
			this.title="新增部门";
			this.dialogFormVisible = true;
			this.department = row;
		},
		handleDelete : function(index,row){
			this.$http.get(this.deleteUrl,{params:{deptId:row.deptId}}).then(
					function(response){
						this.getDepartment();
					 },
					function(response){}  			
					);  
		},
		addDepartment :function(){
			this.title="新增部门";
			this.dialogFormVisible = true;
			this.department = {name:null};
			
		},		
		onSubmit :function(){
			if(this.department.deptId == null){
				this.add();
			}else{
				this.update();
			}
			
		},
		update : function(){
			if(this.department.name == null || this.department.name == "" ){
				alert("不能为空");
				this.dialogFormVisible=false;
				this.getDepartment();
				return ;
			}			
			this.$http.post(this.updateUrl,this.department).then(
					function(response){
						this.dialogFormVisible=false;
						this.department={name:null};
						this.getDepartment();
						if(response.data == "1"){
							alert("已存在相同的部门");
						}						 
						
						
					 },
					function(response){}  			
					); 
			
			
		},
		add : function(){
			if(this.department.name == null || this.department.name == "" ){
				alert("不能为空");
				this.dialogFormVisible=false;
				this.getDepartment();
				return ;
			}
			this.$http.post(this.addUrl,this.department).then(
					function(response){
						this.dialogFormVisible=false;
						this.department={name:null}; 
						this.getDepartment();
						if(response.data == "1"){
							alert("已存在相同的部门");
						}
						
						
						
						
					 },
					function(response){}  			
					); 
			
			
		},
		
			
			 
			   	
	  
        }	        
	 
	
});  

</script>
</body>
</html>
