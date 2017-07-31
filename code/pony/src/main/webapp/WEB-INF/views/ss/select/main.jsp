<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>科目选择</title>
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
        <el-card class="box-card content-margin" v-if="showEdit">
            <div slot="header" class="clearfix">
              <el-row>
	              <el-col :span="4">
	              <b>科目选择</b>
	              </el-col>
              </el-row>  
              <el-row>             
               <el-col :span="4">
               	<span>学年</span>
               </el-col>
               <el-col :span="6">
               	<span>{{config.schoolYear.name}}</span>
               </el-col>
              </el-row>
              <el-row>             
               <el-col :span="4">
               	<span>可选数量</span>
               </el-col>
               <el-col :span="6">
               	<span>{{config.selectNum}}</span>
               </el-col>
              </el-row>
              <el-row>             
               <el-col :span="4">
               	<span>科目</span>
               </el-col>
               <el-col :span="10">
               	<el-checkbox-group v-model="selectSubjects" id="subjectsGroup" >
					<el-checkbox v-for="x in config.subjectArray" :label="x">{{x}}</el-checkbox>
				</el-checkbox-group>
               </el-col>
              </el-row>
              <el-row>
              	<el-col :offset="5" :span="10">
              		<el-button size="small" @click="saveSelect()">保存</el-button>
              	</el-col>
              </el-row>
            </div>
        </el-card>
        <el-card class="box-card content-margin" v-if=" ! showEdit">
            <div slot="header" class="clearfix">
              <el-row>
	              <el-col :span="4">
	              <b>科目选择</b>
	              </el-col>
              </el-row>  
              <el-row>             
               <el-col :span="4">
               	<span>学年</span>
               </el-col>
               <el-col :span="6">
               	<span>{{config.schoolYear.name}}</span>
               </el-col>
              </el-row>
              <el-row>             
               <el-col :span="4">
               	<span>可选数量</span>
               </el-col>
               <el-col :span="6">
               	<span>{{config.selectNum}}</span>
               </el-col>
              </el-row>
              <el-row>             
               <el-col :span="4">
               	<span>已选科目</span>
               </el-col>
               <el-col :span="10">
               	<span v-for="x in subjects">{{x}}</span>
               </el-col>
              </el-row>
              <el-row>
              	<el-col :offset="5" :span="10">
              		<el-button size="small" @click="saveSelect()">重新选择</el-button>
              	</el-col>
              </el-row>
            </div>
        </el-card>
        


  </div>
</div>

<script type="text/javascript">
	var app = new Vue({ 
	el : '#app' ,
	data : { 		
		config:{},
		subjects:[],
		selectSubjects:[],
		dialogFormVisible:false,
		formLabelWidth:"120px",
		tableData:[],
		currentConfigUrl:"<s:url value='/ss/config/current'/>",
		selectedUrl:"<s:url value='/ss/select/selected'/>",
		saveUrl :"<s:url value='/ss/select/save'/>",
		title:"",
		types:[{id:"0",name:"否"},{id:"1",name:"是"}],
		showEdit:true,
		rules :{
		/* seq: [{required :true,message:"请填写顺序..",trigger:"blur"}]	 */			
		}
		
	}, 
	
	mounted : function() { 
		this.getCurrentConfig();
		this.getSelectedSubjects();
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
		getSelectedSubjects :function(){ 			
			this.$http.get(this.selectedUrl).then(
			function(response){
			this.subjects=response.data;
			 },
			function(response){}  	 			
			);
		},
		saveSelect : function(){
			if(this.config.selectNum != this.selectSubjects.length){
				alter("请选择"+this.config.selectNum+"门科目");
			}else{
				this.$http.post(this.saveUrl,{configId:this.config.configId,subjects:this.selectSubjects.join(",")},{emulateJSON:true}).then(
						function(response){
							this.subjects=this.selectSubjects;
							this.showEdit=false;
						 },
						function(response){}  			
				);  
			}
			
		},
		handleDelete : function(index,row){
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
			
		},
		addConfig:function(){
			this.title="新增配置";
			this.dialogFormVisible = true;
			this.config={configId:null,subjects:null,selectNum:null,startTime:null,endTime:null,isCurrent:null,schoolYear:this.schoolYear};
		},		
		onSubmit :function(formName){
			if(this.config.configId == null ){
				this.add(formName);
			}else{
				this.update(formName);
			}
			
		},
		update : function(formName){
			app.$http.post(app.updateUrl,app.config).then(
					function(response){
						app.dialogFormVisible=false;
						app.getConfigList();
					 },
					function(response){console.log("error submit!");}
					);
		},
		add : function(formName){
			app.$http.post(app.addUrl,app.config).then(
					function(response){
						app.dialogFormVisible=false;
						app.getConfigList();
					 },
					function(response){console.log("error submit!");}
					);
		},
		getIsCurrentName: function(id){
			for(var i in this.types){
				if(this.types[i].id == id){
					return this.types[i].name
				}
			}
			return "";
		}
      }
	 
	
});  
	
</script>
</body>
</html>