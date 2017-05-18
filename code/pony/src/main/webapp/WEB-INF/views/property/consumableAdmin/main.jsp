<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>损耗品管理</title>
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
              <b>损耗品管理</b>
              </el-col>
              </el-row>
              <el-row>
               <el-col :span="16">
               <!--  <el-button type="primary" @click="add">新增</el-button> -->
                <el-button type="primary" @click="instock">入库</el-button>
                <el-button type="primary" @click="outstock">出库</el-button>
               </el-col>              
              </el-row>
            </div>
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    highlight-current-row
                    @current-change="handleCurrentChange"
                   > 
                             
                 
                <el-table-column
                         inline-template
                        label="分类"
                        >
                 <div>{{row.propertyTypeName }}</div>       
                </el-table-column>
                <el-table-column
                		prop="name"
                        label="名称"
                        >
                </el-table-column>
                <el-table-column
                        prop="spec"
                        label="型号"
                        >
                </el-table-column>
                <el-table-column
                        prop="amount"
                        label="数量"
                        >
                </el-table-column>
                <el-table-column
                        prop="location"
                        label="存放地点"
                        >
                </el-table-column>              
                <el-table-column
                         inline-template
                        label="责任人"
                        >
                 <div>{{row.ownerName}}</div>       
                </el-table-column>
             
            </el-table> 

			

        </el-card>
		<el-dialog  v-model="dialogFormVisible" >
		<div slot="title" class="dialog-title">
                    <b>{{title}}</b>
        </div>			
			<el-form :model="ruleForm" >			 
			 <el-form-item label="数量" prop="number" :label-width="formLabelWidth"> 
			 <el-input-number size="large" v-model="ruleForm.number" style="width:200px;"></el-input-number>			 			
			</el-form-item>
			<el-form-item label="备注" prop="comments" :label-width="formLabelWidth"> 
			<el-input type="textarea" :rows="5" placeholder="请输入内容" v-model="ruleForm.comments"></el-input>			 			
			</el-form-item>
		    </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="onSubmit()">确定</el-button>
				<el-button @click="dialogFormVisible = false">取 消</el-button>				
			</div>
		</el-dialog>
			 
			
			
			


		</div>
 

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		
		tableData:[],
		consumablesUrl:"<s:url value='/property/consumableAdmin/list'/>",
		instockUrl :"<s:url value='/property/consumableAdmin/instock'/>",		
		outstockUrl :"<s:url value='/property/consumableAdmin/outstock'/>",
		selectConsumable:{},
		ruleForm :{number : 0,comments:null},
		dialogFormVisible:false,
		formLabelWidth:"120px",
		title:null,
		flag : true   //true入库，false出库
		
	},
	/* filters: {    
   		   
 		 statusFilter: function (value) {
     		 if(value == '0'){return "空闲"; }
     		 if(value == '1'){return "使用中"; }
     		 if(value == '2'){return "作废"; }
     		 
    		
 		 }	, 
 		}, */
	
	mounted : function() { 
		this.getConsumables();
		
			
	}, 
	methods : { 
		 handleCurrentChange:function(val) {
		             this.selectConsumable = val;                                   
		            },		  	    
		 getConsumables : function(){						 
			this.$http.get(this.consumablesUrl).then(
			function(response){
			this.tableData=response.data;},
			function(response){}  			
			); 
			} , 
		 instock : function(){
			
			if(this.selectConsumable == null){
				this.$alert("请选择消耗品","提示",{
				type:"warning",
				confirmButtonText:'确认'
				});
				return ;
			}										
			this.dialogFormVisible = true;	
			this.title = "入库";	
			this.flag = true;	
			},
		outstock : function(){
			
			 
			
			if(this.selectConsumable == null){
				this.$alert("请选择消耗品","提示",{
				type:"warning",
				confirmButtonText:'确认'
				});
				return ;
			}			
			this.dialogFormVisible = true;	
			this.title = "出库";	
			this.flag = false;
			  
			} ,
			
			onSubmit :function(){
				
				   if(this.ruleForm.number==null ||this.ruleForm.number ==0){
				   this.$alert("消耗品数量不能为空","提示",{
					type:"warning",
					confirmButtonText:'确认'
					});
					return ;
				   }	
				   if(this.flag){
				   this.$http.get(this.instockUrl,{params:{selectConsumable:this.selectConsumable.cseId,number:this.ruleForm.number,comments:this.ruleForm.comments}}
				    ).then(
					function(response){
					this.dialogFormVisible = false;
					this.selectConsumable = {};
					this.ruleForm={number : 0,comments:null};
					this.flag=true;
					this.getConsumables();
					this.$message({
						type:"info",
						message:"入库成功"
					});
					},
					function(response){}  			
					);
				   }else{
				   this.$http.get(this.outstockUrl,{params:{selectConsumable:this.selectConsumable.cseId,number:this.ruleForm.number,comments:this.ruleForm.comments}}
				    ).then(
					function(response){
					this.dialogFormVisible = false;
					this.selectConsumable = {};
					this.ruleForm={number : 0,comments:null};
					this.flag=true;
					this.getConsumables();
					this.$message({
						type:"info",
						message:"出库成功"
					});
					},
					function(response){}  			
					);
				   
				   }						   		
			
			},	
			
			
		 					
				
		
		
			
			 
			   	
	  
        }	        
	 
	
});  

</script>
</body>
</html>
