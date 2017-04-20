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
                        :label="'老师'"                   
                        :value="'1'">
                        <span style="float: left">老师</span>
               		 </el-option>
               		 <el-option                       
                        :label="'学生'"                       
                        :value="'2'">
                        <span style="float: left">学生</span>
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
        
        
        <el-dialog title="新增用户组" v-model="dialogFormVisible" size="large">
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
                        :label="'老师'"                   
                        :value="'1'">
                        <span style="float: left">老师</span>
               		 </el-option>
               		 <el-option                       
                        :label="'学生'"                       
                        :value="'2'">
                        <span style="float: left">学生</span>
               		 </el-option>
           			 </el-select>          			 
                    </el-form-item>
                   
                    <el-form-item  :label-width="formLabelWidth" v:show="userGroup.groupType == '1'">
                      <el-input  
                      		v-model="teacherSearch"                                                    
                            icon="search"
                            :on-icon-click="handleIconClick"
                            placeholder="请输入内容">
                      </el-input>                                                                                         
                    </el-form-item>                  
                    <el-form-item  :label-width="formLabelWidth" v:show="userGroup.groupType == '1'">
                      <el-row>
                      	<el-col :span="10">
                      		<el-table 
                      		height="250"
                      		:data="unselectTeacher"
                   			 border
                   			style="width: 100%"
                    		highlight-current-row
                    		@current-change="handleCurrentChange">                      		
                    		<el-table-column label="备选用户" prop="teacherName">
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
                      		:data="selectTeacher"
                   			 border
                   			 style="width: 100%"
                    		highlight-current-row
    						@current-change="handleCurrentChange">     				
    						<el-table-column label="已选用户" prop="teacherName">
                      		</el-table-column>                 		
                      		</el-table>
                      		                     		
                      	</el-col>                      	                      
                      </el-row>                                                                                                              
                    </el-form-item> 
                    
                    <el-form-item  :label-width="formLabelWidth" v:show="userGroup.groupType == '2'">
                      <el-input  
                      		v-model="studentSearch"                                                    
                            icon="search"
                            :on-icon-click="handleIconClick"
                            placeholder="请输入内容">
                      </el-input>                                                                                         
                    </el-form-item>                   
                    <el-form-item  :label-width="formLabelWidth" v:show="userGroup.groupType == '2'">
                      <el-row>
                      	<el-col :span="10">
                      		<!-- <el-table 
                      		height="250"
                      		:data="unselectStudent"
                   			 border
                   			style="width: 100%"
                    		highlight-current-row
                    		@current-change="handleCurrentChange">                      		
                    		<el-table-column label="备选用户" prop="studentName">
                      		</el-table-column>                 		
                      		</el-table>  --> 
                      		<el-tree
                    			 :data="treeData"
                   				 highlight-current
                    			 ref="tree"
                    			 :props="props"
                   				 node-key="id"
                    			 show-checkbox
							  >
           					 </el-tree>                    		
                      	</el-col>
                        <el-col :span="4"  >
                            <el-row type="flex" justify="center">
                            <div>
                             	<div><el-button size="small" @click="addOneStudent()">》</el-button></div>
                            	<div><el-button size="small" @click="removeOneStudent">《</el-button></div>
                             	<div><el-button size="small" @click="addAllStudent">》》</el-button></div>
                             	<div><el-button size="small" @click="removeAllStudent">《《</el-button></div>                       
                       		</div>
                       		</el-row>
                        </el-col>
                      	<el-col :span="10">
                      	<el-table 
                      		height="250"
                      		:data="selectStudent"
                   			 border
                   			 style="width: 100%"
                    		highlight-current-row
    						@current-change="handleCurrentChange">     				
    						<el-table-column label="已选用户" prop="studentName">
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
		teachersUrl :"<s:url value='/teacherAdmin/list'/>",	
		studentTreeUrl :"<s:url value='/schoolClass/listTree'/>",	
		tableData: [],
		userGroup:{groupName:null,groupType:null},
		dialogFormVisible:false,
		formLabelWidth : '120px',
		unselectTeacher:[],
		selectTeacher:[],
		teachers:[],
		currentTeacher:{},
		teacherSearch:null,
		unselectStudent:[],
		selectStudent:[],
		students:[],
		currentStudent:{},
		studentSearch:null,
		treeData: [],    
       	props: {
                    label: 'label',
                    children: 'children'
                },
		
	}, 
	mounted : function() { 
		this.getUserGroups();
		this.getTeachers();		
		
			
	}, 
	methods : { 
	
		getStudentTree : function(){ 			
			this.$http.get(this.studentTreeUrl).then(
			function(response){
				this.treeData  = response.data.treeData;
			 },
			function(response){}  			
			); 	
			},
	
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
         createFilterForSelectTeacher :function(queryString) {
                return function unselectTeachers(unselectTeacher) {
                    return (unselectTeacher.teacherName.indexOf(queryString.toLowerCase()) >= 0 );
                };
            } ,	
            
            getTeachers	:function(){ 
			this.$http.get(this.teachersUrl).then(
			function(response){this.teachers=response.data; },
			function(response){}  	 			
			);
			},
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
		 this.userGroup={groupName:null,groupType:'1'}; //新增默认为老师组
		 //this.userGroup.groupType= '2' ;   
		 for(index in this.teachers){
		 //console.log(this.teachers[index].name+'('+this.teachers[index].teacherNo+')');
		 this.unselectTeacher.push({teacherName:this.teachers[index].name+'('+this.teachers[index].teacherNo+')',teacherId:this.teachers[index].teacherId});	 
		 }	
		 this.selectTeacher=[];
		 teacherSearch= null;	 
		},
		handleCurrentChange : function(currentRow){
		if(this.userGroup.groupType == '1'){
				this.currentTeacher=currentRow;				
		}
		if(this.userGroup.groupType == '2'){
				this.currentStudent=currentRow;				
		}
		
		},
		handleIconClick	: function(){
			this.unselectTeacher = [];
			for(index in this.teachers){
			 this.unselectTeacher.push({teacherName:this.teachers[index].name+'('+this.teachers[index].teacherNo+')',teacherId:this.teachers[index].teacherId});	 
		 	}				
			var unselectTeacher = this.unselectTeacher;
            var results = this.teacherSearch ? unselectTeacher.filter(this.createFilterForSelectTeacher(this.teacherSearch)) : unselectTeacher;              		
			this.unselectTeacher = results;
		},
		addOne: function(){
		if(this.userGroup.groupType == '1'){
				if (!this.currentTeacher) {
					alert('请选择一个老师！');
					return;
				} else {
					for (var i = 0; i < this.unselectTeacher.length; i++) {
						var oneRow = this.unselectTeacher[i];
						if (oneRow.teacherId == this.currentTeacher.teacherId) {
							this.unselectTeacher.splice(i, 1);
							this.selectTeacher.push(oneRow);
							this.currentTeacher = null;
							break;
						}
					}
				}				
		}
		if(this.userGroup.groupType == '2'){
				if (!this.currentStudent) {
					alert('请选择一个学生！');
					return;
				} else {
					for (var i = 0; i < this.unselectStudent.length; i++) {
						var oneRow = this.unselectStudent[i];
						if (oneRow.studentId == this.currentStudent.teacherId) {
							this.unselectStudent.splice(i, 1);
							this.selectStudent.push(oneRow);
							this.currentStudent = null;
							break;
						}
					}
				}				
		}
		
		
		},
		removeOne: function(){
		if(this.userGroup.groupType == '1'){
				if (!this.currentTeacher) {
					alert('请选择一个老师！');
					return;
				} else {
					for (var i = 0; i < this.selectTeacher.length; i++) {
						var oneRow = this.selectTeacher[i];
						if (oneRow.teacherId == this.currentTeacher.teacherId) {
							this.selectTeacher.splice(i, 1);
							this.unselectTeacher.push(oneRow);
							this.currentTeacher = null;
							break;
						}
					}
				}				
		}
		if(this.userGroup.groupType == '2'){
				if (!this.currentStudent) {
					alert('请选择一个学生！');
					return;
				} else {
					for (var i = 0; i < this.selectStudent.length; i++) {
						var oneRow = this.selectStudent[i];
						if (oneRow.studentId == this.currentStudent.teacherId) {
							this.selectStudent.splice(i, 1);
							this.unselectStudent.push(oneRow);
							this.currentStudent = null;
							break;
						}
					}
				}				
		}
		
		},
		addAll: function(){
		if(this.userGroup.groupType == '1'){
				this.selectTeacher = this.selectTeacher
						.concat(this.unselectTeacher);
				this.unselectTeacher = [];				
		}
		if(this.userGroup.groupType == '2'){
				this.selectStudent = this.selectStudent
						.concat(this.unselectStudent);
				this.unselectStudent = [];					
		}
		
				
		},
		removeAll: function(){
		if(this.userGroup.groupType == '1'){
				this.unselectTeacher = this.unselectTeacher
						.concat(this.selectTeacher);
				this.selectTeacher = [];				
		}
		if(this.userGroup.groupType == '2'){
				this.unselectStudent = this.unselectStudent
						.concat(this.selectStudent);
				this.selectStudent = [];					
		}
		},	
		addOneStudent: function(){
			if(this.$refs.tree.getCheckedKeys().length>=1){
			var selectStudent=this.$refs.tree.getCheckedKeys();	
				for(var index in selectStudent){
					if(this.selectStudent.indexOf(selectStudent[index]) >=0 ){
						continue;
					}
					else{
						this.selectStudent.push(selectStudent[index]);
					}					
				}
										
			}else{
			return ;
			}	
		},	
		removeOneStudent: function(){
		if (!this.currentStudent) {
					alert('请选择一个学生！');
					return;
				} else {
					for (var i = 0; i < this.selectStudent.length; i++) {
						var oneRow = this.selectStudent[i];
						if (oneRow.studentId == this.currentStudent.teacherId) {
							this.selectStudent.splice(i, 1);							
							this.currentStudent = null;
							break;
						}
					}
				}
		
		},	
		addAllStudent: function(){
			this.selectStudent=[];
			for(var index in this.students){
				this.selectStudent.push(this.students[index].studentId);
			}
		
		},	
		removeAllStudent: function(){
			this.selectStudent=null;
		
		},	
			
		     	
			
			
		  
        }	        
	 
	
});  

</script>
</body>
</html>