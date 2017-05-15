<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>损耗品管理</title>
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
              <b>损耗品管理</b>
              </el-col>
              </el-row>
              <el-row>
               <el-col :span="16">
               <!--  <el-button type="primary" @click="add">新增</el-button> -->
                <el-button type="primary" @click="instock">入库</el-button>
                <el-button type="primary" @click="outstock">出库</el-button>
               </el-col>              
              </el-row>
            </div>
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    highlight-current-row
                    @selection-change="handleSelectionChange"
                   > 
                <el-table-column
                        type="selection"
                        width="50">
                </el-table-column>              
                 
                <el-table-column
                         inline-template
                        label="分类"
                        >
                 <div>{{row.propertyTypeName }}</div>       
                </el-table-column>
                <el-table-column
                		prop="name"
                        label="名称"
                        >
                </el-table-column>
                <el-table-column
                        prop="spec"
                        label="型号"
                        >
                </el-table-column>
                <el-table-column
                        prop="amount"
                        label="数量"
                        >
                </el-table-column>
                <el-table-column
                        prop="location"
                        label="存放地点"
                        >
                </el-table-column>              
                <el-table-column
                         inline-template
                        label="责任人"
                        >
                 <div>{{row.ownerName}}</div>       
                </el-table-column>
             
            </el-table> 

			

        </el-card>
		<el-dialog title="领用" v-model="dialogFormVisible" >			
			<el-form :model="ruleForm" :rules="rules" ref="ruleForm">			 
			 <el-form-item label="使用人" prop="user" :label-width="formLabelWidth"> 
			 <el-select v-model="ruleForm.user" placeholder="使用人" filterable> 
					<el-option v-for="user in users"  :label="user.name" :value="user.teacherId+''"></el-option> 								
			</el-select> 			 			
			</el-form-item>
		    </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="onSubmit('ruleForm')">确定</el-button>
				<el-button @click="dialogFormVisible = false">取 消</el-button>				
			</div>
		</el-dialog>
			 
			
			
			


		</div>
 

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		
		tableData:[],
		propertysUrl:"<s:url value='/property/propertyAdmin/list'/>",
		receiveUrl :"<s:url value='/property/propertyAdmin/receive'/>",		
		backUrl :"<s:url value='/property/propertyAdmin/back'/>",
		deleteUrl :"<s:url value='/property/propertyAdmin/cancle'/>",
		selectPropertys:[],
		usersUrl:"<s:url value='/teacherAdmin/list'/>",	//责任人和使用人取教师	
		users:[],
		ruleForm :{user : ''},
		dialogFormVisible:false,
		formLabelWidth:"120px",
		rules :{
		 user: [{required: true, message: '请选择用户', trigger: 'change'}],
		},
		propertyTypeUrl:"<s:url value='/property/propertyType/list'/>",
		propertyTypes:[],
		userNameMapUrl:"<s:url value='/user/userNameMap'/>",
		userNameMap:[],
	
	
		
	},
	filters: {    
   		 /* typeFilter: function (value) {
     		for(var index in app.propertyTypes){
     		  if(app.propertyTypes[index].typeId == value){
     		    return app.propertyTypes[index].name;
     		  }
     		}    		
 		 }	, 
 		  ownerFilter: function (value) {
     		for(var index in app.userNameMap){
     		  if(app.userNameMap[index].key == value){
     		    return app.propertyTypes[index].value;
     		  }
     		}   
 		 }	, 
 		 userFilter: function (value) {
     		 for(var index in app.userNameMap){
     		  if(app.userNameMap[index].key == value){
     		    return app.propertyTypes[index].value;
     		  }
     		} 
    		}, */
 		   
 		 statusFilter: function (value) {
     		 if(value == '0'){return "空闲"; }
     		 if(value == '1'){return "使用中"; }
     		 if(value == '2'){return "作废"; }
     		 
    		
 		 }	, 
 		},
	
	mounted : function() { 
		this.getPropertys();
		this.getUsers();
		//this.getPropertyType();
		//this.getUserNameMap(); 
		
			
	}, 
	methods : { 
		 handleSelectionChange:function(val) {
		             this.selectPropertys = val;                                   
		            },
		  /*  getUserNameMap: function(){		 
			this.$http.get(this.userNameMapUrl).then(
			function(response){
			this.userNameMap=response.data;},
			function(response){}  			
			); 
			} , 
		  getPropertyType : function(){		 
			this.$http.get(this.propertyTypeUrl).then(
			function(response){
			this.propertyTypes=response.data;},
			function(response){}  			
			); 
			} , */
	     getUsers : function(){ 
					this.$http.get(this.usersUrl).then(
					function(response){
					this.users=response.data;},
					function(response){}  			
					); 
					} , 
		 getPropertys : function(){
			
			 
			this.$http.get(this.propertysUrl).then(
			function(response){
			this.tableData=response.data;},
			function(response){}  			
			); 
			} , 
		 receive : function(){
			//只有空闲状态(0)才可以使用
			if(this.selectPropertys == null || this.selectPropertys.length == 0){
				this.$alert("请选择资产","提示",{
				type:"warning",
				confirmButtonText:'确认'
				});
				return ;
			}			
			for(var index in this.selectPropertys){
			 if(this.selectPropertys[index].status != '0'){
			 	this.$alert("只有空闲的资产才能领用","提示",{
				type:"warning",
				confirmButtonText:'确认'
				});
				return ;
			 }
			}				
			this.dialogFormVisible = true;			
			},
			
			onSubmit :function(formName){
			//传递数组
			 this.$refs[formName].validate(function(valid){
				if(valid){	
				   var selectPropertys = []; 
				   for(var index in app.selectPropertys){
				    selectPropertys.push(app.selectPropertys[index].propId);
				   }
							   
				    app.$http.post(app.receiveUrl,{selectPropertys:selectPropertys,user:app.ruleForm.user},{
                            emulateJSON:true
                        }
				    ).then(
					function(response){
					app.dialogFormVisible = false;
					app.selectPropertys = [];
					app.ruleForm={user : ''};
					app.getPropertys();
					app.$message({
						type:"info",
						message:"领取成功"
					});
					},
					function(response){}  			
					);   				
				}else{
				
				console.log("error submit!");				
				}
			}); 	
			
			
			},	
			
		 back : function(){
			
			 
			//只有使用状态(1)才可以使用
			if(this.selectPropertys == null || this.selectPropertys.length == 0){
				this.$alert("请选择资产","提示",{
				type:"warning",
				confirmButtonText:'确认'
				});
				return ;
			}			
			for(var index in this.selectPropertys){
			 if(this.selectPropertys[index].status != '1'){
			 	this.$alert("只有使用中的资产才能归还","提示",{
				type:"warning",
				confirmButtonText:'确认'
				});
				return ;
			 }
			}	
			//传递数组
			var selectPropertys = []; 
				   for(var index in this.selectPropertys){
				    selectPropertys.push(this.selectPropertys[index].propId);
				   }		 
			this.$http.post(this.backUrl,selectPropertys).then(
			function(response){
			this.selectPropertys = [];
			this.getPropertys();
			this.$message({
				type:"info",
				message:"归还成功"
			});
			},
			function(response){}  			
			);  
			} ,	
		cancle : function(){
						 		
			if(this.selectPropertys == null || this.selectPropertys.length == 0){
				this.$alert("请选择资产","提示",{
				type:"warning",
				confirmButtonText:'确认'
				});
				return ;
			}			
			
			//传递数组
			var selectPropertys = []; 
				   for(var index in this.selectPropertys){
				    selectPropertys.push(this.selectPropertys[index].propId);
				   }		 
			this.$http.post(this.deleteUrl,selectPropertys).then(
			function(response){
			this.selectPropertys = [];
			this.getPropertys();			
			this.$message({
				type:"info",
				message:"删除成功"
			});
			},
			function(response){}  			
			);
			} 					
				
		
		
			
			 
			   	
	  
        }	        
	 
	
});  

</script>
</body>
</html>
