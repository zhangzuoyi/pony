<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>通用字典管理</title>
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
              <b>通用字典管理</b>
              </el-col>
              </el-row>
              <el-row>
               <el-col :span="6">
                <el-select v-model="dictType" placeholder="请选择.." clearable> 
				<el-option v-for="(x,y) in commonDicts" :label="x" :value="y">				
				</el-option> 				
				</el-select>                                                
               </el-col> 
               <el-col :offset="18" :span="6">
               <el-button type="primary" @click="getCommonDictByDictType">查询</el-button>
               <el-button type="primary" @click="addCommonDict">新增</el-button>       
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
                        prop="dictId"
                        label="ID"
                        >
                </el-table-column>
                <el-table-column
                		prop="dictType"
                        label="字典类型"
                        >
                </el-table-column>
                <el-table-column
                        prop="code"
                        label="字典编码"
                        >
                </el-table-column>
               <el-table-column
                        prop="value"
                        label="字典值"
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
			<el-form :model="commonDict" :rules="rules" ref="ruleForm">
			<el-form-item label="字典类型" :label-width="formLabelWidth" prop="dictType"> 
			 <el-select v-model="commonDict.dictType" placeholder="请选择.."  :disabled="commonDict.dictId != null"> 
				<el-option v-for="(x,y) in commonDicts" :label="x" :value="y">				
				</el-option> 				
				</el-select> 
			 </el-form-item>
			 <el-form-item label="字典编码" :label-width="formLabelWidth" prop="code"> 
			 <el-input v-model="commonDict.code" auto-complete="off"   required></el-input> 
			 </el-form-item> 
			 <el-form-item label="字典值" :label-width="formLabelWidth" prop="value"> 
			  <el-input v-model="commonDict.value" auto-complete="off" required></el-input>
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
		dictType : null,
		commonDict:{dictType:null,code:null,value:null},
		dialogFormVisible:false,
		formLabelWidth:"120px",
		tableData:[],
		commonDictTypesUrl:"<s:url value='/commonDict/commonDictTypes'/>",				
		listByDictTypeUrl:"<s:url value='/commonDict/listByDictType'/>",		
		deleteUrl :"<s:url value='/commonDict/delete'/>",
		addUrl :"<s:url value='/commonDict/add'/>",
		updateUrl :"<s:url value='/commonDict/update'/>",
		title:"",
		commonDicts:[],
		rules :{
		dictType: [{required :true,message:"请选择类型..",trigger:"change"}],
		code: [{required :true,message:"请填写字典编码",trigger:"blur"}],
		value: [{required :true,message:"请填写字典值",trigger:"blur"}]		
		}
	
	
		
	}, 
	
	mounted : function() { 
		this.getCommonDictTypes();
		this.getCommonDictByDictType();
		
			
	}, 
	methods : { 
			getCommonDictTypes : function(){
			this.$http.get(this.commonDictTypesUrl).then(
			function(response){
			this.commonDicts=response.data;},
			function(response){}  			
			); 
			} ,
	   
		 getCommonDictByDictType : function(){
			this.$http.get(this.listByDictTypeUrl,{params:{dictType:this.dictType}}).then(
			function(response){
			this.tableData=response.data;},
			function(response){}  			
			); 
			} ,
			
		handleEdit : function(index,row){
			this.title="修改字典";
			this.dialogFormVisible = true;
			this.commonDict = row;
		},
		handleDelete : function(index,row){
			this.$confirm("确认删除吗？","提示",{
			confirmButtonText:'确认',
			cancleButtonText:'取消',
			type:'warning'			
			}).then(function(){  
			  app.$http.get(app.deleteUrl,{params:{dictId:row.dictId}}).then(
					function(response){
						app.dictType = null;
						app.getCommonDictByDictType();
					 },
					function(response){}  			
					);  						
			})
			.catch(function(){ app.$message({ type:'info',message:'已取消删除'})});
			
		},
		addCommonDict:function(){
			this.title="新增字典";
			this.dialogFormVisible = true;
			this.commonDict = {dictType:null,code:null,value:null};
			
		},		
		onSubmit :function(formName){
			if(this.commonDict.dictId == null){
				this.add(formName);
			}else{
				this.update(formName);
			}
			
		},
		update : function(formName){
			this.$refs[formName].validate(function(valid){
				if(valid){
				app.$http.post(app.updateUrl,app.commonDict).then(
					function(response){
						app.dialogFormVisible=false;
						app.commonDict = {dictType:null,code:null,value:null};
						app.dictType = null;
						app.getCommonDictByDictType();
						if(response.data == "1"){
							app.$alert("已存在相同的编码","提示",{
		 							confirmButtonText : '确认',		 
								 });
						}						 												
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
				app.$http.post(app.addUrl,app.commonDict).then(
					function(response){
						app.dialogFormVisible=false;
						app.commonDict = {dictType:null,code:null,value:null};
						app.dictType = null;
						app.getCommonDictByDictType();
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
