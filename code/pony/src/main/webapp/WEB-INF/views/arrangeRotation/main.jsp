<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>走班设置</title>
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
              <b>走班设置</b>
              </el-col>
              </el-row>
              <el-row>                            
               <el-col :offset="20" :span="4">
               <el-button type="primary" @click="addArrangeRotation">新增走班</el-button>
               </el-col>             
              </el-row>
            </div>
            <el-table
                    :data="tableData"
                    border 
                    highlight-current-row
                   >               
                 
                <el-table-column
                        prop="rotationId"
                        label="ID"
                        style="width:100px;"                                   
                        
                        >
                </el-table-column>
                <el-table-column
                		prop="tsNames"
                        label="走班情况"
                        style="white-space: nowrap;   
							   text-overflow: ellipsis; 
							 overflow: hidden;" 
                        >
                </el-table-column>                                            
                <el-table-column                       
                        label="操作"
                        style="width:100px;"  
                        >
                 <template scope="scope">
                 <!-- <el-button size="small" @click="handleEdit(scope.$index,scope.row)">编辑</el-button> -->
                 <el-button size="small" type="danger" @click="handleDelete(scope.$index,scope.row)">删除</el-button>               
                 </template>                             
                </el-table-column>
            </el-table> 			
        </el-card>
			<el-dialog title="新增走班" :visible.sync="dialogFormVisible" >
			<el-input placeholder="请输入.." v-model="filterText"></el-input>
			<el-tree  
			class="filter-tree"         
            :data="treeData"
            :props="props"
            show-checkbox
            node-key="tsId"
            highlight-current           
            :check-strictly="true"  
            :filter-node-method="filterNode"
            accordion                     
            ref="tree"
            >           
            </el-tree>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submit"  >确定</el-button>
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				
			</div>
			</el-dialog>


		</div>
 

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : {
		filterText:'', 		
		dialogFormVisible:false,
		formLabelWidth:"120px",
		tableData:[],
		listUrl:"<s:url value='/arrangeRotation/list'/>",		
		deleteUrl :"<s:url value='/arrangeRotation/delete'/>",
		addUrl :"<s:url value='/arrangeRotation/add'/>",
		teacherSubjectUrl :"<s:url value='/teacherSubject/listTree'/>",		
		treeData:[],
		props:{
			children : 'children',
			label : 'label'
		},
	
	
		
	}, 
	
	mounted : function() { 
		this.getArrangeRotations();
		this.getTeacherSubject();
		
			
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
			getArrangeRotations : function(){
			this.$http.post(this.listUrl).then(
			function(response){
			this.dialogFormVisible=false;		
			this.tableData=response.data;},
			function(response){}  			
			); 
			} ,
			getTeacherSubject : function(){
			this.$http.get(this.teacherSubjectUrl).then(
			function(response){
			var firstNode = {label:'课程列表',children : response.data.treeData};			
			this.treeData.push(firstNode);
			},
			function(response){}  			
			); 
			
			},
			

		handleDelete : function(index,row){
			this.$confirm("确认删除吗？","提示",{
			confirmButtonText:'确认',
			cancleButtonText:'取消',
			type:'warning'			
			}).then(function(){  
			  app.$http.get(app.deleteUrl,{params:{rotationId:row.rotationId}}).then(
					function(response){
						app.getArrangeRotations();
						
					 },
					function(response){}  			
					);  						
			})
			.catch(function(){ app.$message({ type:'info',message:'已取消删除'})});
			
		},
		addArrangeRotation:function(){
			this.dialogFormVisible = true;
			
		},
	
		submit :function(){			
				this.add();
		},
		
		add : function(){
			
				this.$http.get(this.addUrl,{params:{tsIds:this.$refs.tree.getCheckedKeys()}}).then(
					function(response){						
						this.getArrangeRotations();
											
					 },
					function(response){}  			
					);
				
				
			
			
			 
			
			
		},
		
			
			 
			   	
	  
        }	        
	 
	
});  

</script>
</body>
</html>
