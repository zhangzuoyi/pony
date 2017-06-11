<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>资源管理</title>
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
              <b>资源管理</b>
              </el-col>
              </el-row>             
            </div>
            <el-row>
            <el-col :span="8">
            <el-input placeholder="请输入菜单名.." v-model="filterText"></el-input>
            <el-tree
            class="filter-tree"
            :data="treeData"
            :props="props"
            :filter-node-method="filterNode"
            accordion
            @node-click ="handleNodeClick"
            ref="tree"
            >
            
            </el-tree>
            </el-col>            
            <el-col :span="14" :offset="2">
            <el-row>当前选中菜单————{{selectResource.resName}}</el-row>
            <el-row>
            <el-col :span="4">父节点名称:<font style="color:red;">*</font></el-col>
            <el-col :span="20">
            <el-select  v-model="selectResource.presId">
            		 <el-option                       
                        :label="'知识库'"                   
                        :value="0">
                        <span style="float: left">知识库</span>
               		 </el-option>
               		 <el-option              		 
                        v-for="parent in parentResource" 
                        :label="parent.resName"                      
                        :value="parent.resId">
                        <span style="float: left">{{parent.resName}}</span>
               		 </el-option>
           			 </el-select>
            </el-col>          
            </el-row>            
            <el-row type="flex" justify="end">
            <el-col :span="4">资源名:<font style="color:red;">*</font></el-col>
            <el-col :span="20">
            <el-input v-model="selectResource.resName"></el-input>	
            </el-col>
            </el-row>
            
            <el-row type="flex" justify="end">
            <el-col :span="4">资源KEY:<font style="color:red;">*</font></el-col>
            <el-col :span="20">
             <el-input v-model="selectResource.resKey"></el-input>	           
            </el-col>
            </el-row>
            
            <el-row type="flex" justify="end">
            <el-col :span="4">资源级别:<font style="color:red;">*</font></el-col>
            <el-col :span="20">
            <el-input v-model="selectResource.resLevel"></el-input>	
            </el-col>
            </el-row>
            
            <el-row type="flex" justify="end">
            <el-col :span="4">备注:</el-col>
            <el-col :span="20">
            <el-input v-model="selectResource.comments"></el-input>	
            </el-col>            
            </el-row>
            
            <el-row type="flex" justify="end">
            <el-col :span="20" :offset="4">              
             <el-button type="primary" @click="update" v-if="!flag">修改菜单</el-button> 
             <el-button type="primary" @click="cancle" v-if="!flag">删除菜单</el-button>           
             <el-button type="primary" @click="addChild" v-if="!flag">添加子菜单</el-button>  
             <el-button type="primary" @click="add" v-if="flag">添加菜单</el-button>                            
            </el-col>
            </el-row>
            
            
            
            </el-col>
            
            </el-row>

			

        </el-card>
			


		</div>
 

</div>
<script type="text/javascript">
		
 var app = new Vue({ 
	el : '#app' ,
	data : { 		
		filterText:'',
		treeData:[],
		props:{
			children : 'children',
			label : 'label'
		},
		resoureTreeUrl : "<s:url value='/resourceAdmin/listTree'/>",
		parentResoureUrl : "<s:url value='/resourceAdmin/parentResource'/>",
		updateResoureUrl : "<s:url value='/resourceAdmin/update'/>",
		deleteResoureUrl : "<s:url value='/resourceAdmin/delete'/>",
		addResoureUrl : "<s:url value='/resourceAdmin/add'/>",		
		selectResource :{presId:null},
		parentResource:{},
		flag : false,
	
		
	}, 
	
	mounted : function() { 
		this.getResoureTree();
		this.getParentResource();
		
			
	}, 
	watch:{
		filterText :function(val){
			this.$refs.tree.filter(val);
		}
	
	},
	methods : {
	
			 filterNode :function(value,data){
				if(!value) return true;
				return data.label.indexOf(value) !== -1;			
			},
			handleNodeClick :function(data){
			//console.log(data);
			this.selectResource = data;
			this.flag = false;
			},
	 
			getResoureTree : function(){
			this.$http.get(this.resoureTreeUrl).then(
			function(response){
			var firstNode = {label:'根节点',children : response.data.treeData};			
			this.treeData.push(firstNode); 
			//this.treeData = [{label:'知识库',children : [{"id":1,"resKey":"sys_admin","children":[],"label":"系统管理","presId":0,"resLevel":1}]}];
			},
			function(response){}  			
			); 
			} ,
			getParentResource : function(){
			this.$http.get(this.parentResoureUrl).then(
			function(response){
			this.parentResource=response.data;},
			function(response){}  			
			); 
			} ,
			update :function(){
			if(this.selectResource.presId == null ||this.selectResource.resName == null ||this.selectResource.resKey == null ||this.selectResource.resLevel == null  ){
			this.$message({type:"info",message:"请输入完整"});
			return ;
			}
			this.$http.post(this.updateResoureUrl,this.selectResource).then(
			function(response){
			this.selectResource = {presId:null,resName :null,resLevel:null,resKey:null,comments:null};
			this.treeData = [];
			this.flag = false;
			this.getResoureTree();
			},
			function(response){}  			
			);
			
			},
		    cancle :function(){
			if(this.selectResource.resId == null){
			this.$message({type:"info",message:"请选择菜单"});
			return ;
			}
			this.$http.get(this.deleteResoureUrl,{params:{resId:this.selectResource.resId}}).then(
			function(response){
			this.selectResource = {presId:null,resName :null,resLevel:null,resKey:null,comments:null};
			this.treeData = [];
			this.flag = false;
			this.getResoureTree();
			},
			function(response){}  			
			);						
			},  
			addChild :function(){
			if(this.selectResource.presId == null){
			var resLevel = 1 ;
			var presId =0;			
			}else{
			var presId =  this.selectResource.resId ;
			var resLevel =  parseInt(this.selectResource.resLevel)+1 ;
			}
			
			this.selectResource = {presId:presId,resLevel:resLevel};	
			this.flag = true;								
			},
			add : function(){
		
			if(this.selectResource.presId == null ||this.selectResource.resName == null ||this.selectResource.resKey == null ||this.selectResource.resLevel == null  ){
			this.$message({type:"info",message:"请输入完整"});
			return ;
			}
			this.$http.post(this.addResoureUrl,this.selectResource).then(
			function(response){
			this.selectResource = {presId:null,resName :null,resLevel:null,resKey:null,comments:null};
			this.treeData = [];
			this.flag = false;
			this.getResoureTree();
			},
			function(response){}  			
			);						
			} 
		   	
	  }
        	        
	 
	
}); 

</script>
</body>
</html>
