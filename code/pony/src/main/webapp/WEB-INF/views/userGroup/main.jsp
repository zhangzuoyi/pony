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
                        label="类型"
                        >
                    <template slot-scope="scope">{{scope.row.groupType | typeFilter }}</template>
                </el-table-column>
                <el-table-column
                        prop="groupName"
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
        
        
        <el-dialog  :visible.sync="dialogFormVisible" size="large">
               <div slot="title" class="dialog-title">
                    <b>{{title}}</b>
                </div>
                <el-form :model="userGroup">
                    <el-form-item label="名称" :label-width="formLabelWidth">
                        <el-autocomplete
                            v-model="userGroup.groupName"
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
                   
                    <el-form-item label="筛选" :label-width="formLabelWidth" v-show="userGroup.groupType == '1'">
                      <el-row>
                      <el-col :span="10">
                      <el-input                             
                      		v-model="teacherSearch"                                                    
                            icon="search"
                            :on-icon-click="handleIconClick"
                            placeholder="请输入内容">
                      </el-input> 
                      </el-col> 
                      </el-row>                                                                                       
                    </el-form-item>                  
                    <el-form-item  :label-width="formLabelWidth" v-show="userGroup.groupType == '1'">
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
                    
                    
                      </el-col>
                      </el-row>                                                                                         
                    </el-form-item>                   
                    <el-form-item  :label-width="formLabelWidth" v-show="userGroup.groupType == '2'">
                      <el-row>
                      	<el-col :span="10">                     		
                      		<div style="height:250px;overflow :auto;">
                      		<el-tree
                      			 
                    			 :data="treeData"
                   				 highlight-current
                    			 ref="tree"
                    			 :props="props"
                   				 node-key="id"
                    			 show-checkbox
							  >
           					 </el-tree> 
           					 </div>
           					                    		
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
    						<el-table-column label="已选用户" prop="label">
                      		</el-table-column>                 		
                      		</el-table>
                      		                     		
                      	</el-col>                      	                      
                      </el-row>                                                                                                              
                    </el-form-item> 
                    
                    
                    
                    
                    
                    
                </el-form>
                <div slot="footer" class="dialog-footer">                   
                    <el-button type="primary" @click.native="submit()">确 定</el-button>
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
		studentTreeUrl :"<s:url value='/studentAdmin/listTree'/>",
		addUrl :"<s:url value='/userGroup/add'/>",
		updateUrl :"<s:url value='/userGroup/update'/>",
		deleteUrl :"<s:url value='/userGroup/delete'/>",
		tableData: [],
		userGroup:{groupName:null,groupType:null,teacherGroup:[],studentGroup:[]},
		dialogFormVisible:false,
		formLabelWidth : '120px',
		unselectTeacher:[],
		selectTeacher:[],
		teachers:[],
		currentTeacher:{},
		teacherSearch:null,
		unselectStudent:[],
		selectStudent:[],
		currentStudent:{},
		studentSearch:null,
		treeData: [],    
       	props: {
                    label: 'label',
                    children: 'children'
                },
        title:null
		
	}, 
	mounted : function() { 
		this.getUserGroups();
		this.getListTableData();
		this.getTeachers();	
		this.getStudentTree();
		
			
	}, 
	filters: {
        typeFilter: function(type) {
            if(type == '1'){
            return '老师';
            }
            if(type == '2'){
            return '学生';
            }
        }
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
                var results=[];
                var userGroups = this.userGroups;
                var result = queryString ? userGroups.filter(this.createFilter(queryString)) : userGroups;
                //调用 callback 返回建议列表的数据 key值必须为value
                for(var index in result){
                	var name = result[index].name;
                	results.push({"value": name  });
                }
                cb(results);
              // cb([{"value": "三全鲜食（北新泾店）", "address": "长宁区新渔路144号"},{"value": "Hot honey 首尔炸鸡（仙霞路）", "address": "上海市长宁区淞虹路661号"}]);
            },
          createFilter :function(queryString) {
                return function userGroups(userGroup) {
                    return (userGroup.name.indexOf(queryString.toLowerCase()) >= 0);
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
								this.tableData  = response.data;
							 },
							function(response){}  			
							);   	
							},
		handleEdit : function(index,row){	
			this.title="修改用户组";		
			this.userGroup = row;
			this.dialogFormVisible = true;
			this.unselectTeacher = [];
			this.selectTeacher = [];
			this.selectStudent=[];
			
			if(this.userGroup.groupType == '1'){
				
				var selectTeacherIds =this.userGroup.teacherGroup;//已选择			
				for(index in this.teachers){
			 		if(selectTeacherIds.indexOf(this.teachers[index].teacherId+"")>=0 || selectTeacherIds.indexOf(this.teachers[index].teacherId)>=0){
					this.selectTeacher.push({teacherName:this.teachers[index].name+'('+this.teachers[index].teacherNo+')',teacherId:this.teachers[index].teacherId});	 				 
					 }else{
				 	this.unselectTeacher.push({teacherName:this.teachers[index].name+'('+this.teachers[index].teacherNo+')',teacherId:this.teachers[index].teacherId});	 				 
				 	}			 
		 		}
			
			this.$refs.tree.setCheckedKeys([]);
			
			}
			if(this.userGroup.groupType == '2'){
				var selectStudentIds =this.userGroup.studentGroup;//已选择			
				for(var index in this.treeData){
			 		for(var x in this.treeData[index]){
			 		if(selectStudentIds.indexOf(this.treeData[index][x].id)){
			 		 this.selectStudent.push(this.treeData[index][x]);
			 		}			 		
			 		}		 
		 		}
			
			 this.$refs.tree.setCheckedKeys([]);
			
			for(index in this.teachers){
			 this.unselectTeacher.push({teacherName:this.teachers[index].name+'('+this.teachers[index].teacherNo+')',teacherId:this.teachers[index].teacherId});	 
			 }	
			
			}
			
			
			this.getListTableData();
		},
		handleDelete : function(index,row){	
			
			this.$confirm("确认删除吗？","提示",{
			confirmButtonText:'确认',
			cancleButtonText:'取消',
			type:'warning'			
			}).then(function(){  
			  app.$http.get(app.deleteUrl,{params:{groupId:row.groupId}}).then(
							function(response){
								app.getListTableData();
							 },
							function(response){}  			
							);  						
			})
			.catch(function(){ app.$message({ type:'info',message:'已取消删除'})});
			
			
			  	
							

		},	
		add : function(){
		this.title="新增用户组";
		 this.dialogFormVisible = true;
		 this.userGroup={groupName:null,groupType:'1',teacherGroup:[],studentGroup:[]}; //新增默认为老师组
		 //this.userGroup.groupType= '2' ;   
		 for(index in this.teachers){
		 //console.log(this.teachers[index].name+'('+this.teachers[index].teacherNo+')');
		 this.unselectTeacher.push({teacherName:this.teachers[index].name+'('+this.teachers[index].teacherNo+')',teacherId:this.teachers[index].teacherId});	 
		 }	
		 this.selectTeacher=[];
		 this.selectStudent=[];
		 teacherSearch= null;	
		 this.$refs.tree.setCheckedKeys([]);
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
			var selectTeacherIds =[];//已选择
			for(var x in this.selectTeacher){
				selectTeacherIds.push(this.selectTeacher[x].teacherId);
			}
			for(index in this.teachers){
			 if(selectTeacherIds.indexOf(this.teachers[index].teacherId)>=0){
				continue; 				 
			 }			 
			 this.unselectTeacher.push({teacherName:this.teachers[index].name+'('+this.teachers[index].teacherNo+')',teacherId:this.teachers[index].teacherId});	 
		 	}	
			
			
			var unselectTeacher = this.unselectTeacher;
            var results = this.teacherSearch ? unselectTeacher.filter(this.createFilterForSelectTeacher(this.teacherSearch)) : unselectTeacher;              		
			this.unselectTeacher = results;
		},
		addOne: function(){
		
				if (!this.currentTeacher) {
					this.$alert("请选择一个老师！","提示",{
		 							confirmButtonText : '确认',		 
								 });
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
		
		
		
		
		},
		removeOne: function(){
		
				if (!this.currentTeacher) {
					this.$alert("请选择一个老师！","提示",{
		 							confirmButtonText : '确认',		 
								 });
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
		
		
		
		},
		addAll: function(){
		
				this.selectTeacher = this.selectTeacher
						.concat(this.unselectTeacher);
				this.unselectTeacher = [];				
		
		
		
				
		},
		removeAll: function(){
		
				this.unselectTeacher = this.unselectTeacher
						.concat(this.selectTeacher);
				this.selectTeacher = [];				
		
		
		},	
		addOneStudent: function(){
			if(this.$refs.tree.getCheckedNodes().length>=1){
			var selectStudent=this.$refs.tree.getCheckedNodes();//需要排除班级节点	
			
			var selectStudentIds =[];//已选择
			for(var x in this.selectStudent){
				selectStudentIds.push(this.selectStudent[x].id);
			}				
			for(var index in selectStudent){
					if(selectStudent[index].id ==null  ||  selectStudentIds.indexOf(selectStudent[index].id) >=0 ){
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
					this.$alert("请选择一个学生！","提示",{
		 							confirmButtonText : '确认',		 
								 });
					return;
				} else {
					for (var i = 0; i < this.selectStudent.length; i++) {
						var oneRow = this.selectStudent[i];
						if (oneRow.id == this.currentStudent.id) {
							this.selectStudent.splice(i, 1);							
							this.currentStudent = null;
							break;
						}
					}
				}
		
		},	
		addAllStudent: function(){
			this.selectStudent=[];
			for(var index in this.treeData){								
				var treeDataChildren = this.treeData[index].children;
				for(var x in treeDataChildren)	{
					this.selectStudent.push(treeDataChildren[x]);	
				}
				}						
		},	
		removeAllStudent: function(){
			this.selectStudent=[];
		
		},
		submit :function(){
			this.userGroup.teacherGroup = [];
			this.userGroup.studentGroup=[];
			for(var index in this.selectTeacher){
				this.userGroup.teacherGroup.push(this.selectTeacher[index].teacherId);
			}
			for(var x in this.selectStudent){
				this.userGroup.studentGroup.push(this.selectStudent[x].id);
			}
			
			
			if(this.userGroup.groupType == '1' && this.selectTeacher ==null ){
			this.$alert("老师不能为空！","提示",{
		 							confirmButtonText : '确认',		 
								 });
			return ;
			}
			if(this.userGroup.groupType == '2' && this.selectStudent ==null ){
			this.$alert("学生不能为空！","提示",{
		 							confirmButtonText : '确认',		 
								 });
			return ;
			}
			if(this.userGroup.groupName ==null){
			 this.$alert("组号不能为空！","提示",{
		 							confirmButtonText : '确认',		 
								 });
			 return;
			}
			
			
			
			if(this.userGroup.groupId != null){			
					this.$http.post(this.updateUrl,this.userGroup).then(
					function(response){this.dialogFormVisible = false;this.getListTableData();},
					function(response){}  			
					); 
					
				
			}else{
				this.$http.post(this.addUrl,this.userGroup).then(
						function(response){this.dialogFormVisible = false;this.getListTableData();},
						function(response){}  			
						); 
				
			}
			
		}
  
       } 	        
	 
	
});  

</script>
</body>
</html>