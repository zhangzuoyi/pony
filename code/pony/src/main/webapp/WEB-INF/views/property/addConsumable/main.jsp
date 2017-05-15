<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增损耗品</title>
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
              <b>新增损耗品</b>
              </el-col>
              </el-row>                                
            </div>

			<el-form ref="ruleForm" :model="consumable" label-width="80px"  :rules="rules" >
			<el-row>
			<el-col :span="8">
			<el-form-item label="分类" prop="propertyType"> 
			<el-select v-model="consumable.propertyType" placeholder="请选择.."> 
				<el-option v-for="propertyType in propertyTypes" :label="propertyType.name" :value="propertyType.typeId">				
				</el-option> 				
			</el-select> 
			</el-form-item>
			</el-col>						
			<el-col :span="8">
			<el-form-item label="名称" prop="name"> 
			<el-input v-model="consumable.name" ></el-input> 			
			</el-form-item>
			</el-col>
			<el-col :span="8">
			<el-form-item label="型号" prop="spec"> 
			<el-input v-model="consumable.spec" ></el-input> 			
			</el-form-item>
			</el-col>			
			</el-row>
			
			<el-row>
			<el-col :span="8">
			<el-form-item label="单价" prop="price"> 
			<el-input-number size="large" v-model="consumable.price" style="width:200px;"></el-input-number>
			</el-form-item>
			</el-col>
			<el-col :span="8">
			<el-form-item label="存放地点" prop="location"> 
			<el-input v-model="consumable.location" ></el-input>
			</el-form-item>
			</el-col>						
			<el-col :span="8">
			<el-form-item label="品牌" prop="brand"> 
			<el-input v-model="consumable.brand" ></el-input>						 			
			</el-form-item>
			</el-col>						
			</el-row>
		
			<el-row>
			<el-col :span="8">
			<el-form-item label="责任人" prop="owner"> 
			<el-select v-model="consumable.owner" placeholder="请选择" filterable> 
				<el-option v-for="user in users"  :label="user.name" :value="user.teacherId"></el-option> 				
			</el-select>
			</el-form-item>
			</el-col>						
			<el-col :span="8">
			<el-form-item label="采购数" prop="amount"> 
			<el-input-number size="large" v-model="consumable.amount" style="width:200px;" :min="0"></el-input-number>			
			</el-col>						
			</el-row>
			
			
			<el-row>
			<el-form-item label="详细配置" prop="description">
			<el-input type="textarea" :rows="5" placeholder="请输入内容" v-model="consumable.description"></el-input>			
			</el-form-item>
			</el-row>
			<el-row>
			<el-form-item label="备注" prop="comments">
			<el-input type="textarea" :rows="5" placeholder="请输入内容" v-model="consumable.comments"></el-input>						
			</el-form-item>
			</el-row>
			<el-row>
			 <el-col :span="6" :offset="10">
			 <el-button type="primary" @click="onSubmit('ruleForm')">提交</el-button>
			 <el-button type="primary" @click="reset('ruleForm')">重置</el-button>
			 </el-col>			 
			</el-row>			
			</el-form>

        </el-card>
        
        
                     	
    </div>
 

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		propertyTypeUrl:"<s:url value='/property/propertyType/list'/>",
		propertyTypes:[],		
		usersUrl:"<s:url value='/teacherAdmin/list'/>",	//责任人取教师	
		users:[],
		consumable:{propertyType:null,owner:null,amount:0,description:null,comments:null},
		rules :{
		name: [{required :true,message:"请选择名称",trigger:"blur"}],
		/* amount: [{required :true,message:"请填写数量",trigger:"blur"}],  数值暂无法用该方式校验 */
		},
		submitUrl:"<s:url value='/property/addConsumable/addConsumables'/>",
		
			
	
	
		
	}, 
	
	mounted : function() { 
		
		this.getPropertyType();	
		this.getUsers();
	}, 
	methods : { 
	   
		getPropertyType : function(){ 
			this.$http.get(this.propertyTypeUrl).then(
			function(response){
			this.propertyTypes=response.data;},
			function(response){}  			
			); 
			} ,
		getUsers : function(){ 
					this.$http.get(this.usersUrl).then(
					function(response){
					this.users=response.data;},
					function(response){}  			
					); 
					} ,				
					
		onSubmit : function(formName){
			
			this.$refs[formName].validate(function(valid){
				if(valid){
				
				app.$confirm("确认提交？","提示",{
				confirmButtonText :'确认',
				cancleButtonText : '取消',
				type:'info'	
				}).then(function(){ 
					app.$http.post(app.submitUrl,app.consumable).then(
					function(response){
						app.consumable={propertyType:null,owner:null,amount:0,description:null,comments:null};
						app.$message({type:'info',message:'提交成功'});												 												
					 },
					function(response){}  			
					);
				
				   })
				.catch(function(){ });							
				}else{
				console.log("error submit!");				
				}
			});
			
			
		}	,
		reset : function(ruleForm){
		  this.$refs[ruleForm].resetFields();
		},
		/* alert :function(){
		 this.$alert("内容","标题",{
		 confirmButtonText : '确认',
		 //回调(可选)
		 callback :  function(action,instance){
		   instance.$message({
		   type : 'info',
		   message : 'action :   '+ action
		   });
		 } 
		 });
		}, */
		
		
		
			
			 
			   	
	  
        }	        
	 
	
});  

</script>
</body>
</html>
