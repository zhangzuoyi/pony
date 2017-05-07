<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增资产</title>
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
              <b>新增资产</b>
              </el-col>
              </el-row>                                
            </div>

			<el-form ref="form" :model="property" label-width="80px" >
			<el-row>
			<el-col :span="8">
			<el-form-item label="分类"> 
			<el-select v-model="property.propertyType" placeholder="请选择.."> 
				<el-option v-for="propertyType in propertyTypes" :label="propertyType.name" :value="propertyType.category">				
				</el-option> 				
			</el-select> 
			</el-form-item>
			</el-col>						
			<el-col :span="8">
			<el-form-item label="名称"> 
			<el-input v-model="property.name" ></el-input> 			
			</el-form-item>
			</el-col>
			<el-col :span="8">
			<el-form-item label="型号"> 
			<el-input v-model="property.spec" ></el-input> 			
			</el-form-item>
			</el-col>			
			</el-row>
			
			<el-row>
			<el-col :span="8">
			<el-form-item label="存放地点"> 
			<el-input v-model="property.location" ></el-input>
			</el-form-item>
			</el-col>						
			<el-col :span="8">
			<el-form-item label="使用部门"> 
			<el-select v-model="property.department" placeholder="请选择.."> 
				<el-option v-for="department in departments"  :label="department.name" :value="department.deptId"></el-option> 
				
			</el-select> 			 			
			</el-form-item>
			</el-col>
			<el-col :span="8">
			<el-form-item label="购买日期"> 
			<el-date-picker type="date" placeholder="选择日期" v-model="property.buyDate" style="width: 200px;"></el-date-picker> 			
			</el-form-item>
			</el-col>			
			</el-row>
			
			<el-row>
			<el-col :span="8">
			<el-form-item label="购买价格"> 
			<el-input-number size="large" v-model="property.price" style="width:200px;"></el-input-number>
			</el-form-item>
			</el-col>						
			<el-col :span="8">
			<el-form-item label="品牌"> 
			<el-input v-model="property.brand" ></el-input>						 			
			</el-form-item>
			</el-col>
			<el-col :span="8">
			<el-form-item label="生产日期"> 
			<el-date-picker type="date" placeholder="选择日期" v-model="property.productDate" style="width: 200px;"></el-date-picker> 			
			</el-form-item>
			</el-col>			
			</el-row>
			
			<el-row>
			<el-col :span="8">
			<el-form-item label="责任人"> 
			<el-select v-model="property.owner" placeholder="请选择" filterable> 
				<el-option v-for="user in users"  :label="user.name" :value="user.teacherId"></el-option> 				
			</el-select>
			</el-form-item>
			</el-col>						
			<el-col :span="8">
			<el-form-item label="使用人"> 
			<el-select v-model="property.user" placeholder="使用人"> 
					<el-option v-for="user in users"  :label="user.name" :value="user.teacherId"></el-option> 								
			</el-select> 			 			
			</el-form-item>
			</el-col>
			<el-col :span="8">
			<el-form-item label="资产编码"> 
			<el-input v-model="property.propCode" :disabled="true" ></el-input>						 			
			</el-form-item>
			</el-col>			
			</el-row>
			
			<el-row>
			<el-col :span="8">
			<el-form-item label="状态"> 
			<el-select v-model="property.status" placeholder="状态"> 
				<el-option label="区域一" value="shanghai"></el-option> 
				<el-option label="区域二" value="beijing"></el-option>
			</el-select>
			</el-form-item>
			</el-col>						
			<el-col :span="8">
			<el-form-item label="采购数量"> 
			<el-input-number size="large" v-model="property.number" style="width:200px;"></el-input-number>			
			</el-col>						
			</el-row>
			<el-row>
			<el-form-item label="详细配置">
			<el-input type="textarea" :rows="5" placeholder="请输入内容" v-model="property.description"></el-input>			
			</el-form-item>
			</el-row>
			<el-row>
			<el-form-item label="备注">
			<el-input type="textarea" :rows="5" placeholder="请输入内容" v-model="property.comments"></el-input>						
			</el-form-item>
			</el-row>
			<el-row>
			 <el-col :span="6" :offset="10">
			 <el-button type="primary" @click="onSubmit">提交</el-button>
			 <el-button type="primary" @click="cancle">取消</el-button>
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
		departmentUrl:"<s:url value='/property/department/list'/>",
		departments:[],
		property:{propertyType:null,department:null,owner:null,user:null},
		usersUrl:"<s:url value='/teacherAdmin/list'/>",	//责任人和使用人取教师	
		users:[],
			
	
	
		
	}, 
	
	mounted : function() { 
		
		this.getPropertyType();	
		this.getDepartment();
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
		getDepartment : function(){
				
				 
				this.$http.get(this.departmentUrl).then(
				function(response){
				this.departments=response.data;},
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
		onSubmit : function(){
			
		}	,
		cancle : function(){
		
		}
		
			
			 
			   	
	  
        }	        
	 
	
});  

</script>
</body>
</html>
