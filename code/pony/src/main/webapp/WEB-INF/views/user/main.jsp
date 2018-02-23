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
              <el-row>                            
            	<el-col :span="2" >
                    <b>用户类型:</b>
            	</el-col> 
            	<el-col :span="4" >
                    <el-select v-model="userType" placeholder="请选择" clearable>
	                    <el-option v-for="x in userTypes" :label="x.name" :value="x.type"></el-option>
	                </el-select>
            	</el-col> 
            	<el-col :span="2" >
                    <b>姓名:</b>
            	</el-col> 
            	<el-col :span="4" >
                    <el-input v-model="userName" />
            	</el-col> 
            	<el-col :span="4" >
               		<el-button type="primary"  @click="search">查询</el-button>
               		<!-- <el-button type="primary"  @click="showAdd">新增</el-button> -->
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
                        prop="userId"
                        label="序号"
                        >
                </el-table-column>
                <el-table-column
                        prop="showName"
                        label="用户名"
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
                 <el-button size="small"  @click="resetPsw(scope.$index,scope.row)">重置密码</el-button>
                     <!-- <el-button size="small" @click="handleEdit(scope.$index,scope.row)">编辑</el-button>
                     <el-button size="small" type="danger" @click="handleDelete(scope.$index,scope.row)">删除</el-button> -->
                 </template>                             
                </el-table-column>
                
            </el-table>
            <el-row>
            <el-col :offset="10" :span="14">
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
			 <el-dialog title="重置密码" :visible.sync="dialogFormVisible" >
			<el-form :model="psw" >
			<el-form-item label="初始密码" :label-width="formLabelWidth" >
			 <el-input v-model="psw.initPsw" type="password" auto-complete="off"   required></el-input>
			 </el-form-item>
			 <el-form-item label="密码" :label-width="formLabelWidth" >
			 <el-input v-model="psw.firstPsw" type="password" auto-complete="off"   required></el-input>
			 </el-form-item>
			 <el-form-item label="确认密码" :label-width="formLabelWidth" >
			 <el-input v-model="psw.secondPsw" type="password" auto-complete="off"   required></el-input>
			 </el-form-item>
		    </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="onSubmit()"  >确定</el-button>
				<el-button @click="dialogFormVisible = false">取 消</el-button>				
			</div>
			</el-dialog>

			
			<el-dialog title="设置角色" :visible.sync="dialogFormVisible2" >
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
        psw:{initPsw:null,firstPsw:null,secondPsw:null},
		user:{userId:null,loginName:null,userName:null,userType:null,roles:[]},
        dialogFormVisible:false,
		formLabelWidth:"120px",
		tableData:[],
		usersUrl:"<s:url value='/user/listPage'/>",
		setRoleUrl:"<s:url value='/user/setRole'/>",										
		/* deleteUrl :"<s:url value='/userAdmin/delete'/>",
		addUrl :"<s:url value='/userAdmin/add'/>",
		updateUrl :"<s:url value='/userAdmin/update'/>", */
		roleTreeUrl : "<s:url value='/roleAdmin/listTree'/>",
        resetPswUrl : "<s:url value='/user/resetPsw'/>",
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
		total:null,
        userId : null,
        userType : null,
		userTypes : [{type:"t",name:"老师"},{type:"s",name:"学生"}],
		userName : null
		
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
			this.$http.get(this.usersUrl,{params:{currentPage:this.currentPage-1,pageSize:this.pageSize,userType: this.userType, userName: this.userName}}).then(
				function(response){
					this.tableData=response.data.content;
					this.total = response.data.totalElements;
				},
				function(response){}  			
				); 
			} ,
        resetPsw :  function(index,row){
            if(row.userId == null){
                this.$message({type:"info",  message:"请选择用户"});
                return ;
            }
            this.dialogFormVisible = true;
            this.userId = row.userId;
        },
        onSubmit :function(){
        	if(this.psw.initPsw == null  || this.psw.initPsw == "" ){
                this.$message({type:"info",  message:"请输入初始密码"});
                return ;
            }
        	if(this.psw.firstPsw == null || this.psw.secondPsw == null || this.psw.firstPsw == "" || this.psw.secondPsw == ""){
                this.$message({type:"info",  message:"请输入密码"});
                return ;
            }
            if(this.psw.firstPsw != this.psw.secondPsw){
                this.$message({type:"info",  message:"两次密码输入不一致"});
                return ;
            }
            app.$http.post(app.resetPswUrl,{userId:this.userId,initPsw:this.psw.initPsw,psw:this.psw.firstPsw},{emulateJSON:true}).then(
                function(response){
                    this.$message({type:"info",  message:"重置成功"});
                    this.dialogFormVisible = false;
                    this.psw = {initPsw:null,firstPsw:null,secondPsw:null};

                },
                function(response){}
            );


        },
        search : function(){
        	this.currentPage=1;
        	this.getUsers();
        }


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
