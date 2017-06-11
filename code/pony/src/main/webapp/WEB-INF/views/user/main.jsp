<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户管理</title>
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
              <b>用户管理</b>
              </el-col>
              </el-row>
              <!-- <el-row>                            
               <el-col :offset="20" :span="4">
               <el-button type="primary" @click="addUser">新增用户</el-button>
               </el-col>             
              </el-row> -->
            </div>
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    highlight-current-row
                   >               
                 
                <el-table-column
                        prop="userId"
                        label="序号"
                        >
                </el-table-column>
                <el-table-column
                		prop="loginName"
                        label="登录名"
                        >
                </el-table-column>
                <el-table-column
                		inline-template
                        label="用户类型"
                        >
                        <div>{{row.userType | userTypeFilter }}</div>
                </el-table-column>
                <el-table-column
                		prop="lastLoginTime"
                        label="上次登陆时间"
                        >                       
                </el-table-column>                                                             
                <el-table-column                       
                        label="操作"
                        >
                 <template scope="scope">
                 <el-button size="small"  @click="setRole(scope.$index,scope.row)">授权</el-button>                                             
                 <!-- <el-button size="small" @click="handleEdit(scope.$index,scope.row)">编辑</el-button>
                 <el-button size="small" type="danger" @click="handleDelete(scope.$index,scope.row)">删除</el-button> -->               
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
			<%-- <el-dialog  v-model="dialogFormVisible" >
			<div slot="title" class="dialog-title">
                    <b>{{title}}</b>
            </div>
			<el-form :model="user" :rules="rules" ref="ruleForm">						
			 <el-form-item label="登录名" :label-width="formLabelWidth" prop="roleName"> 
			 <el-input v-model="user.loginName" auto-complete="off"   required></el-input> 
			 </el-form-item>
			 <el-form-item label="密码" :label-width="formLabelWidth" prop="psw"> 
			 <el-input v-model="user.psw" auto-complete="off"  v-if="addOrUpdate" required></el-input> 
			 </el-form-item> 
			 <el-form-item label="用户类型" :label-width="formLabelWidth" prop="userType"> 
			 <el-select v-model="user.userType"     placeholder="请选择.." >
               		 <el-option                       
                        :label="'老师'"                   
                        :value="'t'">
                        <span style="float: left">老师</span>
               		 </el-option>
               		 <el-option                       
                        :label="'学生'"                       
                        :value="'s'">
                        <span style="float: left">学生</span>
               		 </el-option>
           			 </el-select> 			 
			 </el-form-item>			 
		    </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="onSubmit('ruleForm')"  >确定</el-button>
				<el-button @click="dialogFormVisible = false">取 消</el-button>				
			</div>
			</el-dialog> --%>
			
			<el-dialog title="设置角色" v-model="dialogFormVisible2" >			
			<el-tree           
            :data="treeData"
            :props="props"
            show-checkbox
            node-key="roleCode"
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
		user:{userId:null,loginName:null,userType:null,roles:[]},
		/* dialogFormVisible:false,
		formLabelWidth:"120px", */
		tableData:[],
		usersUrl:"<s:url value='/user/listPage'/>",
		setRoleUrl:"<s:url value='/user/setRole'/>",										
		/* deleteUrl :"<s:url value='/userAdmin/delete'/>",
		addUrl :"<s:url value='/userAdmin/add'/>",
		updateUrl :"<s:url value='/userAdmin/update'/>", */
		roleTreeUrl : "<s:url value='/roleAdmin/listTree'/>",		
		title:"",
		/* rules :{	
		psw: [{required :true,message:"请填写密码",trigger:"blur"}],	
		loginName: [{required :true,message:"请填写登录名",trigger:"blur"}],
		psw: [{required :true,message:"请填写密码",trigger:"change"}]
		
		}, */
		treeData:[],
		props:{
			children : 'children',
			label : 'label'
		},
		dialogFormVisible2:true,
		addOrUpdate: true,
		currentPage : 1,
		pageSizes :[20,50,100],
		pageSize:[20],
		total:null
	
	
		
	}, 
	filters: {    
    userTypeFilter: function (value) {
      if(value == 't'){return "老师"; }
      if(value == 's'){return "学生"; }
    }
  }	,
	
	mounted : function() { 
		this.getUsers();
		this.getRoleTree();
		this.dialogFormVisible2 = false;//解决el-dialog中的el-tree第一次的动态渲染问题
		
			
	}, 
	methods : {
	
			handleSizeChange :function(val){
			//console.log('每页  ${val}条');
			this.currentPage = 1;
			this.pageSize = val;
			this.getUsers();
			
			},
			handleCurrentChange: function(val){
				this.currentPage = val;
				this.getUsers();
				//console.log('当前页 : ${val}');
			},
	
			 
			setRole:function(index,row){
			if(row.userId == null){
			 this.$message({type:"info",  message:"请选择用户"});
			 return ;
			}						 
			this.user = row;
			this.dialogFormVisible2 = true;	
			//this.$forceUpdate();
			this.$refs.tree.setCheckedKeys(row.roles);
			//this.$set(this.$data,'selectResource',row.resources);   //强制视图刷新 
			},
			submit:function(){
			this.user.roles =  this.$refs.tree.getCheckedKeys();
			this.$refs.tree.setCheckedKeys([]);
			this.dialogFormVisible2 = false;
			this.$http.post(this.setRoleUrl,this.user).then(
					function(response){
						this.$message({message:"授权成功",type:"info"});
						this.dialogFormVisible=false;
						this.user = {userId:null,loginName:null,userType:null,roles:[]};
						this.getUsers();											 												
					 },
					function(response){}  			
					);												
			},
			getRoleTree : function(){
			this.$http.get(this.roleTreeUrl).then(
			function(response){
			var firstNode = {label:'根节点',children : response.data.treeData};			
			this.treeData.push(firstNode); 
			//this.treeData = [{label:'知识库',children : [{"id":1,"resKey":"sys_admin","children":[],"label":"系统管理","presId":0,"resLevel":1}]}];
			},
			function(response){}  			
			); 
			} ,
			getUsers : function(){
			this.$http.get(this.usersUrl,{params:{currentPage:this.currentPage-1,pageSize:this.pageSize}}).then(
			function(response){
			this.tableData=response.data.content;
			this.total = response.data.totalElements;
			
			},
			function(response){}  			
			); 
			} ,	   					
		/* handleEdit : function(index,row){
			this.title="修改用户";
			this.dialogFormVisible = true;
			this.user = row;
			this.addOrUpdate = false;
		},
		handleDelete : function(index,row){
			this.$confirm("确认删除吗？","提示",{
			confirmButtonText:'确认',
			cancleButtonText:'取消',
			type:'warning'			
			}).then(function(){  
			  app.$http.get(app.deleteUrl,{params:{userId:row.userId}}).then(
					function(response){
						app.user = {userId:null,loginName:null,userType:null,roles:[]};
						app.getUsers();
					 },
					function(response){}  			
					);  						
			})
			.catch(function(){ app.$message({ type:'info',message:'已取消删除'})});
			
		},
		addUser:function(){
			this.title="新增用户";
			this.dialogFormVisible = true;
			this.user = {userId:null,loginName:null,userType:null,roles:[]};
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
				app.$http.post(app.updateUrl,app.user).then(
					function(response){
						app.dialogFormVisible=false;
						app.user = {userId:null,loginName:null,userType:null,roles:[]};
						app.getUsers();	
																 												
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
				app.$http.post(app.addUrl,app.user).then(
					function(response){
						
						app.dialogFormVisible=false;
						app.user = {userId:null,loginName:null,userType:null,roles:[]};
						app.getUsers();
						if(response.data == "1"){
							app.$alert("已存在相同的Id","提示",{
		 							confirmButtonText : '确认',		 
								 });
						}	
						
						
																	
					 },
					function(response){}  			
					);
				
				}else{
				}
			});
			
			
			 
			
			
		}, */
		
			
			 
			   	
	  
        }	        
	 
	
});  

</script>
</body>
</html>
