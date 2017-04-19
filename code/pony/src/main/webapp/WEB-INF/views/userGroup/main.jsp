<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户组</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/bootstrap/css/bootstrap.min.css' />" />
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

</head>
<body>
<div id="app">
  <div>   	           	
        	<el-card class="box-card content-margin">
            <div slot="header" class="clearfix">
              <el-row>
              <el-col :span="4">
              <b>用户组</b>
              </el-col>
              </el-row>
              <el-row>
              
              <el-col :span="1" >
                    <b>类型:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="groupType"    filterable placeholder="请选择.." >
               		 <el-option
                        v-for="groupType in groupTypes" 
                        :label="groupType.name"                      
                        :value="groupType.groupType">
                        <span style="float: left">{{groupType.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            </el-col>
            <el-col :span="1" >
                    <b>名称:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-autocomplete
                            v-model="groupName"
                            :fetch-suggestions="querySearch"
                            placeholder="请输入内容"                            
                    ></el-autocomplete>				
                    </div>
            
            </el-col>
             
              <el-col :span="4" :offset="8">
              <!--  <el-button type="primary" @click="getListTableData()" >查询</el-button> -->
               <el-button type="primary" @click="getListTableData()" >查询</el-button>             
              </el-col>
              
              </el-row> 
              <el-row>
              <el-col :span="4" :offset="20">
              <el-button type="primary" @click="add()" >新增</el-button>
              </el-col>
              </el-row>                                         
            </div> 
            <el-row> 
            <el-col :span="20"> 
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    highlight-current-row
                   >               
                 
                <el-table-column
                        prop="groupId"
                        label="ID"
                        >
                </el-table-column>
                <el-table-column
                        prop="groupType"
                        label="类型"
                        >
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="名称"
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
            </el-col>
           </el-row>   
        </el-card> 
        
        
        <el-dialog title="新增" v-model="dialogFormVisible" size="large">
                <el-form :model="userGroup">
                    <el-form-item label="名称" :label-width="formLabelWidth">
                        <el-autocomplete
                            v-model="userGroup.name"
                            :fetch-suggestions="querySearch"
                            placeholder="请输入内容"                            
                    ></el-autocomplete>	
                    </el-form-item>
                    <el-form-item label="类型" :label-width="formLabelWidth">
                        <el-select v-model="userGroup.groupType"    filterable placeholder="请选择.." >
               		 <el-option
                        v-for="groupType in groupTypes" 
                        :label="groupType.name"                      
                        :value="groupType.groupType">
                        <span style="float: left">{{groupType.name}}</span>
               		 </el-option>
           			 </el-select>
                    </el-form-item>
                    <el-form-item  :label-width="formLabelWidth">
                      <el-autocomplete                           
                            :fetch-suggestions="querySearch"
                            icon="search"
                            :on-icon-click="handleIconClick"
                            placeholder="请输入内容"                            
                      ></el-autocomplete>                                                                                         
                    </el-form-item>
                    <el-form-item  :label-width="formLabelWidth">
                      <el-row>
                      	<el-col :span="10">
                      		<el-table 
                      		height="250"
                      		:data="unselectUser"
                   			 border
                   			style="width: 100%"
                    		highlight-current-row
                    		@current-change="handleCurrentChange">   
                    		
                    		<el-table-column label="备选用户" prop="userName">
                      		</el-table-column>                 		
                      		</el-table>
                      		
                      		
                      	</el-col>
                        <el-col :span="4"  >
                            <el-row type="flex" justify="center">
                            <div>
                             	<div><el-button size="small" @click="addOne()">》</el-button></div>
                            	<div><el-button size="small" @click="removeOne">《</el-button></div>
                             	<div><el-button size="small" @click="addAll">》》</el-button></div>
                             	<div><el-button size="small" @click="removeAll">《《</el-button></div>                       
                       		</div>
                       		</el-row>
                        </el-col>
                      	<el-col :span="10">
                      	<el-table 
                      		height="250"
                      		:data="selectUser"
                   			 border
                   			 style="width: 100%"
                    		highlight-current-row
    						@current-change="handleCurrentChange">   
   				
    						<el-table-column label="已选用户" prop="userName">
                      		</el-table-column>                 		
                      		</el-table>
                      		                     		
                      	</el-col>                      	                      
                      </el-row>                                                                                                              
                    </el-form-item> 
                    
                    
                    
                    
                    
                    
                </el-form>
                <div slot="footer" class="dialog-footer">                   
                    <el-button type="primary" @click.native="dialogFormVisible = false">确 定</el-button>
                    <el-button @click.native="dialogFormVisible = false">取 消</el-button>
                </div>
            </el-dialog>
        
        
              	
    </div>
    
    
   

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		groupType:null,
		groupTypes : [],
		groupName:null,				
		userGroups:[],		
		userGroupUrl: "<s:url value='/userGroup/list'/>",
		listTableDataUrl :	"<s:url value='/userGroup/listByCondition'/>",			
		tableData: [],
		userGroup:{},
		dialogFormVisible:false,
		formLabelWidth : '120px',
		unselectUser:[],
		selectUser:[],
		
	}, 
	mounted : function() { 
		this.getUserGroups(); 		
		
			
	}, 
	methods : { 
	
		  querySearch :function(queryString, cb) {
                var userGroups = this.userGroups;
                var results = queryString ? userGroups.filter(this.createFilter(queryString)) : userGroups;
                // 调用 callback 返回建议列表的数据
                cb(results);
            },
          createFilter :function(queryString) {
                return function userGroups() {
                    return (userGroups.name.indexOf(queryString.toLowerCase()) === 0);
                };
            } ,	
		getUserGroups : function(){ 
			this.$http.get(this.userGroupUrl).then(
			function(response){this.userGroups=response.data;},
			function(response){}  			
			); 
			} ,			 
		 getListTableData:function(){
					this.tableData = [];  //清空表格数据												 			
					 this.$http.get(this.listTableDataUrl,{params:{groupType:this.groupType,groupName:this.groupName}}).then(
							function(response){
								this.tableData  = response.data.tableData;
							 },
							function(response){}  			
							);   	
							},
		handleEdit : function(index,row){
		
		
		},
		handleDelete : function(index,row){
		
		
		},	
		add : function(){
		 this.dialogFormVisible = true;
		},
		handleCurrentChange : function(currentRow){
		
		},
		handleIconClick	: function(){
		
		
		},
		addOne: function(){
		
		},
		removeOne: function(){
		
		},
		addAll: function(){
		
		},
		removeAll: function(){
		
		},			
		     	
			
			
		  
        }	        
	 
	
});  

</script>
</body>
</html>