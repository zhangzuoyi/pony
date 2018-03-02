<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课时管理</title>
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
              <b>课时管理</b>
              </el-col>
              </el-row>
              <el-row>                            
               <el-col :span="1" >
                    <b>学年:</b>                                    
            	</el-col> 
               <el-col  :span="2">
               		{{schoolYear.startYear}}—{{schoolYear.endYear}}
               </el-col>
               <el-col :span="1" >
                    <b>学期:</b>
            	</el-col> 
               <el-col  :span="1">
               		{{term.name}}
               </el-col>
             	<el-col :span="2" >
                    <b>业务日期:</b>                                    
            	</el-col> 
           		 <el-col :span="6" >
					<el-select v-model="searchDate" filterable clearable placeholder="请选择..">
	               		 <el-option
	                        v-for="bd in businessDateList" 
	                        :label="bd"                      
	                        :value="bd">
	                        <span style="float: left">{{bd}}</span>
	               		 </el-option>
           			 </el-select>				
            	</el-col>
            	<el-col :span="6" >
               		<el-button type="primary"  @click="list">查询</el-button>
               		<el-button type="primary"  @click="openUpload">导入</el-button>
               		<el-button type="primary"  @click="exportData">导出</el-button>
              	</el-col>                           
              </el-row>
            </div>
            <el-table
            		ref="multipleTable"
                    :data="tableData"
                    border
                    style="width: 100%"
                   >               
                <el-table-column
							prop="teacherNo"
							label="编号"
							>
				</el-table-column>
                <el-table-column
							prop="teacherName"
							label="姓名"
							>
				</el-table-column>
				<el-table-column
						label="业务日期">
						<template slot-scope="scope">{{scope.row.businessDate | date}}</template>
				</el-table-column>
				<el-table-column
						prop="planHours"
						label="计划课时">
				</el-table-column>
				<el-table-column
						prop="actualHours"
						label="实际课时">
				</el-table-column>
				<el-table-column
						label="超出课时">
						<template slot-scope="scope">{{scope.row.actualHours - scope.row.planHours}}</template>
				</el-table-column>
				<el-table-column                       
                        label="操作"
                        style="width:100px;"  
                        >
	                 <template scope="scope">
	                 <!-- <el-button size="small" @click="handleEdit(scope.$index,scope.row)">编辑</el-button> -->
	                 <!-- <el-button size="small" type="danger" @click="handleDelete(scope.$index,scope.row)">删除</el-button> -->               
	                 </template>                             
                </el-table-column>
            </el-table>
        </el-card>
		<el-dialog title="导入课时数据"  :visible.sync="uploadDialogFormVisible" >
			<el-row>
				<el-col>
					<el-date-picker type="date" placeholder="业务日期" v-model="uploadVo.businessDate" 
					value-format="yyyy-MM-dd"></el-date-picker>
				</el-col>
			</el-row>
			<el-row>
			<el-col>
				<el-upload					
		  			 ref="upload" 	
		  			 name="file"
		  			 :on-success="uploadSuccess"
		  			 action="<s:url value='/classHour/upload'/>"
		  			:file-list="fileList"
		  			:auto-upload="false"
		  			:data="uploadVo">
		  			<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
				</el-upload>
			</el-col>
			</el-row>
			<el-row>
			<el-button type="primary" size="small" @click="uploadData">提交</el-button>
			<el-button type="primary" size="small" @click="downloadTemplate">下载模板</el-button>
			</el-row>
		</el-dialog>
		
		</div>
</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		schoolYearUrl:"<s:url value='/schoolYear/getCurrent'/>",
		termUrl:"<s:url value='/term/getCurrent'/>",
		templateUrl:"<s:url value='/classHour/exportTemplate'/>",
		businessDateUrl:"<s:url value='/classHour/businessDateList'/>",
		findUrl:"<s:url value='/classHour/find'/>",
		schoolYear :null,
		term : null,
		uploadDialogFormVisible : false,
		fileList : [],
        tableData:[],
        searchDate: null,
        uploadVo : {},
        businessDateList : []
		
	}, 
	mounted : function() { 
		this.getCurrentSchoolYear();
		this.getCurrentTerm();
		this.getBusinessDateList();
	},
	methods : {
			getCurrentSchoolYear	:function(){ 			
				this.$http.get(this.schoolYearUrl).then(
					function(response){
					this.schoolYear=response.data;
					 },
					function(response){}
				);
			},
			getCurrentTerm	:function(){ 			
				this.$http.get(this.termUrl).then(
					function(response){
					this.term=response.data; },
					function(response){}
				);
			},
			uploadSuccess : function(response,file,fileList){
				this.$message({
					type:"info",
					message: "导入成功"
			  	});
			},
			uploadData : function(){
          	  this.$refs.upload.submit();
          	  this.$refs.upload.clearFiles();
          	  this.uploadDialogFormVisible = false;
          	  
            },
            openUpload : function(){
                this.uploadDialogFormVisible = true;
            },
            downloadTemplate : function(){
          	  window.location.href=this.templateUrl;
            },
            list : function(){
            	if(this.searchDate == null || this.searchDate == ""){
                 	this.$alert("请业务日期","提示",{
   					type:"warning",
   					confirmButtonText:'确认'
	   				});
	   				return;
                 }
            	 this.$http.get(this.findUrl,{params:{businessDate : this.searchDate}}).then(
   					function(response){						
   						this.tableData=response.data;
   											
   					},
   					function(response){}  			
   				 );
            },
            getBusinessDateList : function(){
            	this.$http.get(this.businessDateUrl).then(
   					function(response){
   						this.businessDateList=response.data; },
   					function(response){}
   				);
            },
            exportData : function(){
   			 /* if(this.examId == null || this.examId == ""){
                 	this.$alert("请选择考试","提示",{
   					type:"warning",
   					confirmButtonText:'确认'
   				});
   				return;
                 }			
   			if(this.gradeId == null || this.gradeId == ""){
                 	this.$alert("请选择年级","提示",{
   					type:"warning",
   					confirmButtonText:'确认'
   				});
   				return;
                 }              
   					var exportParams = {
   										examId : this.examId,
   										gradeId:this.gradeId
   								};
   					var url = "<s:url value='/examAdmin/average/exportAverage?'/>"+jQuery.param(exportParams);
   					window.location.href = encodeURI(encodeURI(url));
   				    window.open(encodeURI(encodeURI(url)));	 */
   			},
            
   			
            
        }	        
});  
</script>
</body>
</html>
