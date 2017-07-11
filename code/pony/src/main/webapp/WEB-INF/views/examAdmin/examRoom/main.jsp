<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考场管理</title>
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
              <b>考场管理</b>
              </el-col>
              </el-row>
              <el-row>
               
               <el-col :offset="18" :span="6">
               <el-button type="primary" @click="addExamRoom">新增</el-button>       
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
                        prop="id"
                        label="ID"
                        >
                </el-table-column>
                <el-table-column
                		prop="name"
                        label="考场名"
                        >
                </el-table-column>
                <el-table-column
                        prop="capacity"
                        label="容量"
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
            <el-row>
            <el-col :offset="18" :span="6">
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="pageSizes"
                :page-size="pageSize"
                layout="total,sizes,prev,pager,next,jumper"
                :total="total"
                ></el-pagination>
            </el-col>          
			</el-row>

			

        </el-card>
			<el-dialog  v-model="dialogFormVisible" >
			<div slot="title" class="dialog-title">
                    <b>{{title}}</b>
                </div>
			<el-form :model="examRoom" :rules="rules" ref="ruleForm">
			 <el-form-item label="考场名" :label-width="formLabelWidth" prop="name"> 
			 <el-input v-model="examRoom.name" auto-complete="off"   required></el-input> 
			 </el-form-item> 
			 <el-form-item label="容量" :label-width="formLabelWidth" prop="capacity"> 
			  <el-input-number v-model="examRoom.capacity"  required></el-input-number>
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
		
		
		examRoom:{name:null,capacity:null},
		dialogFormVisible:false,
		formLabelWidth:"120px",
		tableData:[],
		examRoomsUrl:"<s:url value='/examAdmin/examRoom/listByPage'/>",				
		deleteUrl :"<s:url value='/examAdmin/examRoom/delete'/>",
		addUrl :"<s:url value='/examAdmin/examRoom/add'/>",
		updateUrl :"<s:url value='/examAdmin/examRoom/update'/>",
		title:"",
		rules :{
		name: [{required :true,message:"请填写考场名",trigger:"blur"}]				
		},
		currentPage : 1,
		pageSizes :[20,50,100],
		pageSize:[20],
		total:null
		
	
		
	}, 
	filters: {    
    /* timeFilter : function(input){
        var date = new date(input);
        var hour = date.getHours();
        var minutes = date.getMinutes();
        var seconds = date.getSeconds();
        return hour+":"+minutes+":"+seconds;
    } */
  }	,
	
	mounted : function() { 
				
				this.getExamRooms();
		
			
	}, 
	methods : {
	
			handleSizeChange :function(val){
			//console.log('每页  ${val}条');
			this.currentPage = 1;
			this.pageSize = val;
			
			},
			handleCurrentChange: function(val){
				this.currentPage = val;
				//console.log('当前页 : ${val}');
			},
			
          getExamRooms : function(){
			this.$http.get(this.examRoomsUrl,{params:{currentPage:this.currentPage-1,pageSize:this.pageSize}}).then(
			function(response){
			this.tableData=response.data.content;
			this.total = response.data.totalElements;
			},
			function(response){}  			
			); 
			} ,	   		 
			
		handleEdit : function(index,row){
			this.title="修改考场";
			this.dialogFormVisible = true;
			this.examRoom = row;
		},
		
		handleDelete : function(index,row){
			this.$confirm("确认删除吗？","提示",{
			confirmButtonText:'确认',
			cancleButtonText:'取消',
			type:'warning'			
			}).then(function(){  
			  app.$http.get(app.deleteUrl,{params:{id:row.id}}).then(
					function(response){
						app.exaRoom = {name:null,capacity:null};
						app.getExamRooms();
					 },
					function(response){}  			
					);  						
			})
			.catch(function(){ app.$message({ type:'info',message:'已取消删除'})});
			
		},
		addExamRoom:function(){
			this.title="新增考场";
			this.dialogFormVisible = true;
			this.examRoom = {name:null,capacity:null};
			
		},		
		onSubmit :function(formName){
			if(this.examRoom.id == null){
				this.add(formName);
			}else{
				this.update(formName);
			}
			
		},
		update : function(formName){
			this.$refs[formName].validate(function(valid){
				if(valid){
				app.$http.post(app.updateUrl,app.examRoom).then(
					function(response){
						app.dialogFormVisible=false;
						app.examRoom = {name:null,capacity:null};
						app.getExamRooms();
						if(response.data == "1"){
							app.$alert("已存在相同的考场","提示",{
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
				app.$http.post(app.addUrl,app.examRoom).then(
					function(response){
						app.dialogFormVisible=false;
						app.examRoom = {name:null,capacity:null};
						app.getExamRooms();
						if(response.data == "1"){
							app.$alert("已存在相同的考场","提示",{
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
