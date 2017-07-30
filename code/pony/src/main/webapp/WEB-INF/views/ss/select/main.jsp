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
        <el-card class="box-card content-margin">
            <div slot="header" class="clearfix">
              <el-row>
	              <el-col :span="4">
	              <b>科目选择</b>
	              </el-col>
              </el-row>  
              <el-row>             
               <el-col :offset="18" :span="6">
               <el-button type="primary" @click="addConfig">新增</el-button>       
               </el-col>
              </el-row>
            </div>
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    highlight-current-row>
                <el-table-column
                        prop="schoolYear.name"
                        label="学年"
                        >
                </el-table-column>
                <el-table-column
                        prop="subjects"
                        label="学科"
                        >
                </el-table-column>
                <el-table-column
                		inline-template
                        label="开始时间"
                        >
                        <div>{{row.startTime | date}}</div>
                </el-table-column>
                <el-table-column
                        inline-template
                        label="结束时间"
                        >       
                        <div>{{row.endTime | date}}</div>               
                </el-table-column>
                <el-table-column
                		prop="selectNum"
                        label="可选数量"
                        >
                </el-table-column>
                <el-table-column
                        inline-template
                        label="是否当前"
                        >        
                        <div>{{getIsCurrentName(row.isCurrent)}}</div>              
                </el-table-column>
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
			<el-form :model="config" :rules="rules" ref="ruleForm">			
			 <el-form-item label="学年" :label-width="formLabelWidth" > 
			 	{{config.schoolYear.name }}
			 </el-form-item>
			 <el-form-item label="科目" :label-width="formLabelWidth" > 
            	<el-input v-model="config.subjects"></el-input>
			 </el-form-item> 
             <el-form-item label="可选数量" :label-width="formLabelWidth" > 
			  	<el-input v-model="config.selectNum"></el-input>
			 </el-form-item>
			 <el-form-item label="开始时间" :label-width="formLabelWidth" > 
			 	<el-date-picker
                    v-model="config.startTime"
                    type="date"
                    placeholder="选择日期">
            	</el-date-picker>
			 </el-form-item>
			 <el-form-item label="结束时间" :label-width="formLabelWidth" > 
			 	<el-date-picker
                    v-model="config.endTime"
                    type="date"
                    placeholder="选择日期">
            	</el-date-picker>
			 </el-form-item>
			 <el-form-item label="是否当前" :label-width="formLabelWidth" >
			 	<el-select v-model="config.isCurrent" placeholder="请选择">
                    <el-option v-for="x in types" :label="x.name" :value="x.id"></el-option>
                </el-select>
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
		config:{configId:null,subjects:null,selectNum:null,startTime:null,endTime:null,isCurrent:null,schoolYear:{}},
		dialogFormVisible:false,
		formLabelWidth:"120px",
		tableData:[],
		schoolYearUrl:"<s:url value='/schoolYear/getCurrent'/>",
		listUrl:"<s:url value='/ss/config/list'/>",
		deleteUrl :"<s:url value='/ss/config/delete'/>",
		addUrl :"<s:url value='/ss/config/add'/>",
		updateUrl :"<s:url value='/ss/config/edit'/>",
		title:"",
		types:[{id:"0",name:"否"},{id:"1",name:"是"}],
		choolYear:null, 
		rules :{
		/* seq: [{required :true,message:"请填写顺序..",trigger:"blur"}]	 */			
		}
		
	}, 
	
	mounted : function() { 
		this.getConfigList();
		this.getCurrentSchoolYear();
	}, 
	methods : { 
			getConfigList : function(){
				this.$http.get(this.listUrl).then(
				function(response){
					this.tableData=response.data;
				},
				function(response){}  			
				); 
			},
			getCurrentSchoolYear	:function(){ 			
				this.$http.get(this.schoolYearUrl).then(
				function(response){
				this.schoolYear=response.data;
				 },
				function(response){}  	 			
				);
			},
		handleEdit : function(index,row){
			this.title="修改配置";
			this.dialogFormVisible = true;
			this.config = row;
			
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