<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>角色管理</title>
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
              <b>角色管理</b>
              </el-col>
              </el-row>
              <el-row>                            
               <el-col :offset="20" :span="4">
               <el-button type="primary" @click="addRole">新增角色</el-button>
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
                        prop="roleCode"
                        label="角色编码"
                        >
                </el-table-column>
                <el-table-column
                		prop="roleName"
                        label="角色名称"
                        >
                </el-table-column>                                              
                <el-table-column                       
                        label="操作"
                        >
                 <template scope="scope">
                 <el-button size="small"  @click="setResource(scope.$index,scope.row)">设置资源</el-button>                              
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
			<el-form :model="role" :rules="rules" ref="ruleForm">
			<el-form-item label="角色编码" :label-width="formLabelWidth" prop="roleCode"> 
			 <el-input v-model="role.roleCode" auto-complete="off"   :disabled="!addOrUpdate"   required></el-input> 
			 </el-form-item>			
			 <el-form-item label="角色名称" :label-width="formLabelWidth" prop="roleName"> 
			 <el-input v-model="role.roleName" auto-complete="off"   required></el-input> 
			 </el-form-item> 			 
		    </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="onSubmit('ruleForm')"  >确定</el-button>
				<el-button @click="dialogFormVisible = false">取 消</el-button>				
			</div>
			</el-dialog>
			
			<el-dialog title="设置资源" v-model="dialogFormVisible2" >			
			<el-tree           
            :data="treeData"
            :props="props"
            show-checkbox
            node-key="resId"
            highlight-current
            default-expand-all           
            ref="tree"
            >           
            </el-tree>						
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submit"  >确定</el-button>
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>				
			</div>
			</el-dialog>
			


		</div>
 

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		role:{roleCode:null,roleName:null,resources:[]},
		dialogFormVisible:false,
		formLabelWidth:"120px",
		tableData:[],
		rolesUrl:"<s:url value='/roleAdmin/list'/>",				
		deleteUrl :"<s:url value='/roleAdmin/delete'/>",
		addUrl :"<s:url value='/roleAdmin/add'/>",
		updateUrl :"<s:url value='/roleAdmin/update'/>",
		resoureTreeUrl : "<s:url value='/resourceAdmin/listTree'/>",		
		title:"",
		rules :{	
		roleCode: [{required :true,message:"请填写角色编码",trigger:"blur"}],	
		roleName: [{required :true,message:"请填写角色名称",trigger:"blur"}]		
		},
		treeData:[],
		props:{
			children : 'children',
			label : 'label'
		},
		dialogFormVisible2:true,
		addOrUpdate: true
	
	
		
	}, 
	
	mounted : function() { 
		this.getRoles();
		this.getResoureTree();
		this.dialogFormVisible2 = false;//解决el-dialog中的el-tree第一次的动态渲染问题
		
			
	}, 
	methods : {
	
			 
			setResource:function(index,row){
			if(row.roleCode == null){
			 this.$message({type:"info",  message:"请选择角色"});
			 return ;
			}						 
			this.role = row;
			this.dialogFormVisible2 = true;	
			//this.$forceUpdate();
			this.$refs.tree.setCheckedKeys(row.resources);
			//this.$set(this.$data,'selectResource',row.resources);   //强制视图刷新 
			},
			submit:function(){
			this.role.resources =  this.$refs.tree.getCheckedKeys();
			this.$refs.tree.setCheckedKeys([]);
			this.dialogFormVisible2 = false;
			this.$http.post(this.updateUrl,this.role).then(
					function(response){
						this.dialogFormVisible=false;
						this.role = {roleCode:null,roleName:null};
						this.getRoles();											 												
					 },
					function(response){}  			
					);												
			},
			getResoureTree : function(){
			this.$http.get(this.resoureTreeUrl).then(
			function(response){
			var firstNode = {label:'知识库',children : response.data.treeData};			
			this.treeData.push(firstNode); 
			//this.treeData = [{label:'知识库',children : [{"id":1,"resKey":"sys_admin","children":[],"label":"系统管理","presId":0,"resLevel":1}]}];
			},
			function(response){}  			
			); 
			} ,
			getRoles : function(){
			this.$http.get(this.rolesUrl).then(
			function(response){
			this.tableData=response.data;},
			function(response){}  			
			); 
			} ,	   					
		handleEdit : function(index,row){
			this.title="修改角色";
			this.dialogFormVisible = true;
			this.role = row;
			this.addOrUpdate = false;
		},
		handleDelete : function(index,row){
			this.$confirm("确认删除吗？","提示",{
			confirmButtonText:'确认',
			cancleButtonText:'取消',
			type:'warning'			
			}).then(function(){  
			  app.$http.get(app.deleteUrl,{params:{roleCode:row.roleCode}}).then(
					function(response){
						app.role = {roleCode:null,roleName:null};
						app.getRoles();
					 },
					function(response){}  			
					);  						
			})
			.catch(function(){ app.$message({ type:'info',message:'已取消删除'})});
			
		},
		addRole:function(){
			this.title="新增角色";
			this.dialogFormVisible = true;
			this.role = {roleCode:null,roleName:null};
			this.addOrUpdate = true;
			
		},		
		onSubmit :function(formName){
			if(this.addOrUpdate){
				this.add(formName);
			}else{
				this.update(formName);
			}
			
		},
		update : function(formName){
			this.$refs[formName].validate(function(valid){
				if(valid){
				app.$http.post(app.updateUrl,app.role).then(
					function(response){
						app.dialogFormVisible=false;
						app.role = {roleCode:null,roleName:null};
						app.getRoles();	
																 												
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
				app.$http.post(app.addUrl,app.role).then(
					function(response){
						
						app.dialogFormVisible=false;
						app.role = {roleCode:null,roleName:null};
						app.getRoles();	
						if(response.data == "1"){
							app.$alert("已存在相同的编码","提示",{
		 							confirmButtonText : '确认',		 
								 });
						}	
						
						
																	
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
