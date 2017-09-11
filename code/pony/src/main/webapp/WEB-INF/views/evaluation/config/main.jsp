<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>评价主题管理</title>
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
	              	<b>评价主题管理</b>
	              </el-col>
              </el-row>             
            </div>
            <el-row>
	            <el-col :span="8">
		            <el-select v-model="subject" placeholder="请选择" @change="subjectChange()">
	                    <el-option v-for="x in subjects" :label="x.name" :value="x"></el-option>
	                </el-select>
		            <el-tree
			            class="filter-tree"
			            :data="treeData"
			            :props="props"
			            accordion
			            @node-click ="handleNodeClick"
			            ref="tree">
		            </el-tree>
	            </el-col>            
            <el-col :span="14" :offset="2">
            <el-row>当前选中节点————{{selectResource.name}}</el-row>
            <el-row>
	            <el-col :span="4">父节点:</el-col>
	            <el-col :span="20">
	            	{{selectResource.parentItemName}}
	            </el-col>
            </el-row>            
            <el-row type="flex" justify="end">
            	<el-col :span="4">节点名:<font style="color:red;">*</font></el-col>
	            <el-col :span="20">
	            	<el-input v-model="selectResource.name"></el-input>	
	            </el-col>
            </el-row>
            <el-row type="flex" justify="end">
	            <el-col :span="4">级别:<font style="color:red;">*</font></el-col>
	            <el-col :span="20">
	            	<el-input v-model="selectResource.level"></el-input>	
	            </el-col>
            </el-row>
            <el-row type="flex" justify="end">
	            <el-col :span="4">类型:<font style="color:red;">*</font></el-col>
	            <el-col :span="20">
	            	<el-select v-model="selectResource.type" placeholder="请选择" >
	                    <el-option v-for="x in itemTypes" :label="x.name" :value="x.value"></el-option>
	                </el-select>
	            </el-col>
            </el-row>
            <el-row type="flex" justify="end">
	            <el-col :span="4">分值:</el-col>
	            <el-col :span="20">
	            	<el-input v-model="selectResource.score"></el-input>	
	            </el-col>
            </el-row>
            <el-row type="flex" justify="end">
	            <el-col :span="4">序号:<font style="color:red;">*</font></el-col>
	            <el-col :span="20">
	            	<el-input v-model="selectResource.seq"></el-input>	
	            </el-col>
            </el-row>
            <el-row type="flex" justify="end">
	            <el-col :span="4">描述:</el-col>
	            <el-col :span="20">
	            	<el-input v-model="selectResource.description"></el-input>	
	            </el-col>
            </el-row>
            
            <el-row type="flex" justify="end">
            <el-col :span="20" :offset="4">              
             <el-button type="primary" @click="updateItem" v-if="!flag && selectResource.level >0">修改节点</el-button>
             <el-button type="primary" @click="deleteItem" v-if="!flag && selectResource.level >0">删除节点</el-button>
             <el-button type="primary" @click="addChild" v-if="!flag && selectResource.type=='D'">添加子节点</el-button>
             <el-button type="primary" @click="addItem" v-if="flag">添加节点</el-button>                            
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
		props:{
			children : 'children',
			label : 'name'
		},
		resoureTreeUrl : "<s:url value='/resourceAdmin/listTree'/>",
		parentResoureUrl : "<s:url value='/resourceAdmin/parentResource'/>",
		updateResoureUrl : "<s:url value='/resourceAdmin/update'/>",
		deleteResoureUrl : "<s:url value='/resourceAdmin/delete'/>",
		addResoureUrl : "<s:url value='/resourceAdmin/add'/>",		
		parentResource:{},
		flag : false,
		subjectsUrl : "<s:url value='/evaluation/config/subjects'/>",
		itemTreeUrl : "<s:url value='/evaluation/config/itemTree'/>",
		addItemUrl : "<s:url value='/evaluation/config/addItem'/>",
		updateItemUrl : "<s:url value='/evaluation/config/updateItem'/>",
		deleteItemUrl : "<s:url value='/evaluation/config/deleteItem'/>",
		subjects : [],
		subject : {},
		treeData : [],
		selectResource :{type : ""},
		lastSelectResource :{},
		itemTypes : [{name : "目录", value : "D"},{name : "叶子节点", value : "L"}]
		
	}, 
	
	mounted : function() { 
		this.getSubjects();
			
	}, 
	methods : {
		getSubjects : function(){
			this.$http.get(this.subjectsUrl).then(
				function(response){
					this.subjects=response.data;
				},
				function(response){}
			);
		},
		subjectChange : function(){
			this.$http.get(this.itemTreeUrl, {params:{subjectId : this.subject.subjectId}}).then(
				function(response){
					this.treeData=[];
					var firstNode = {name: this.subject.name , level : 0, type : "D", children : response.data};
					this.treeData.push(firstNode);
				},
				function(response){}
			);
		},
		handleNodeClick :function(data){
			this.selectResource = data;
			this.flag = false;
		},
		addChild :function(){
			var sr=this.selectResource;
			this.lastSelectResource=sr;
			var level=sr.level + 1;
			this.selectResource = {parentItemId : sr.itemId , parentItemName : sr.name, level : level, type : "", subject : {subjectId : this.subject.subjectId}};
			this.flag = true;
		},
		addItem : function(){
			var sr=this.selectResource;
			if(sr.name == null || sr.type == null || sr.seq == null  ){
				this.$message({type:"info",message:"请输入完整"});
				return ;
			}
			this.$http.post(this.addItemUrl,this.selectResource).then(
				function(response){
					this.selectResource = {type : ""};
					this.treeData = [];
					this.flag = false;
					this.subjectChange();
				},
				function(response){}
			);						
		},
		updateItem : function(){
			var sr=this.selectResource;
			if(sr.name == null || sr.type == null || sr.seq == null  ){
				this.$message({type:"info",message:"请输入完整"});
				return ;
			}
			this.$http.post(this.updateItemUrl,this.selectResource).then(
				function(response){
					this.$message({type:"info",message:"修改成功"});
					this.subjectChange();
				},
				function(response){}
			);						
		},
		deleteItem : function(){
     		this.$confirm("确认删除吗？","提示",{
	    			confirmButtonText:'确认',
	    			cancleButtonText:'取消',
	    			type:'warning'
  			}).then(function(){  
  				app.$http.delete(app.deleteItemUrl, {params:{itemId : app.selectResource.itemId}}).then(
 	       				function(response){
 	       					if(response.data.result == "success"){
	 	       					this.$message({
	 	    						type:"info",
	 	    						message:"删除成功"
	 	    					});
	 	       					app.selectResource = {type : ""};
	 	       					app.subjectChange();
 	       					}else{
 	       						this.$alert("存在子节点，不能删除");
 	       					}
 	       					
 	       				},
 	       				function(response){}
 	        		 );
  			}).catch(function(){ app.$message({ type:'info',message:'已取消删除'})});
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
			}
			
		   	
	  }
        	        
	 
	
}); 

</script>
</body>
</html>
