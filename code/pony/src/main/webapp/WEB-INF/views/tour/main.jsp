<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡课管理</title>
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
.myselect .el-input{
width:100px;
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
              <b>巡课管理</b>
              </el-col>
              </el-row>
              <el-row>                            
                <el-col :span="1" >
                    <b>开始日期:</b>
            	</el-col> 
            	<el-col :span="3" >
                    <el-date-picker
	                    v-model="condition.startDate"
	                    type="date"
	                    style="width:120px;"
	                    placeholder="选择日期">
	            	</el-date-picker>
            	</el-col> 
            	<el-col :span="1" >
                    <b>结束日期:</b>
            	</el-col> 
            	<el-col :span="3" >
                    <el-date-picker
	                    v-model="condition.endDate"
	                    type="date"
	                    style="width:120px;"
	                    placeholder="选择日期">
	            	</el-date-picker>
            	</el-col> 
            	<el-col :span="1" >
                    <b>年级:</b>
            	</el-col> 
            	<el-col :span="2" >
                    <el-select v-model="condition.gradeId" placeholder="请选择" @change="findConditionClasses" class="myselect">
	                    <el-option v-for="x in grades" :label="x.name" :value="x.gradeId"></el-option>
	                </el-select>
            	</el-col> 
            	<el-col :span="1" >
                    <b>班级:</b>
            	</el-col> 
            	<el-col :span="2" >
                    <el-select v-model="condition.classId" placeholder="请选择" class="myselect">
	                    <el-option v-for="x in conditionClasses" :label="x.name" :value="x.classId"></el-option>
	                </el-select>
            	</el-col> 
            	<el-col :span="1" >
                    <b>节次:</b>
            	</el-col> 
            	<el-col :span="3" >
                    <el-select v-model="condition.periodSeq" placeholder="请选择" class="myselect">
	                    <el-option v-for="x in periods" :label="x.seq" :value="x.seq"></el-option>
	                </el-select>
            	</el-col> 
            	<el-col :span="4" >
               		<el-button type="primary"  @click="list">查询</el-button>
               		<el-button type="primary"  @click="showAdd">新增</el-button>
              	</el-col>                           
              </el-row>
            </div>
            <el-table
            		ref="multipleTable"
                    :data="tableData"
                    border
                    style="width: 100%">               
                <el-table-column
						prop="className"
						label="班级"
						>
				</el-table-column>
				<el-table-column
						prop="periodSeq"
						label="节次">
				</el-table-column>
				<el-table-column
						label="日期">
					<template slot-scope="scope">{{scope.row.tourDate | date }}</template>

				</el-table-column>
				<el-table-column
						prop="itemSummary"
						label="巡课情况">
				</el-table-column>
				<el-table-column
						prop="description"
						label="其它情况">
				</el-table-column>
            </el-table>
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="pageSizes"
                :page-size="pageSize"
                layout="total,sizes,prev,pager,next,jumper"
                :total="total"></el-pagination>
        </el-card>
		</div>
		<el-dialog  :visible.sync="dialogFormVisible" >
			<div slot="title" class="dialog-title">
                  <b>新增巡课</b>
            </div>
			<el-form :model="tour" >			 
			  <el-form-item label="日期" prop="tourDate" :label-width="formLabelWidth"> 
			 	<el-date-picker
                    v-model="tour.tourDate"
                    type="date"
                    placeholder="选择日期">
            	</el-date-picker>
			  </el-form-item>
			  <el-form-item label="年级" :label-width="formLabelWidth"> 
			 	<el-select v-model="gradeId" placeholder="请选择" @change="findClasses">
                    <el-option v-for="x in grades" :label="x.name" :value="x.gradeId"></el-option>
                </el-select>
			  </el-form-item>
			  <el-form-item label="班级" prop="classId" :label-width="formLabelWidth"> 
			 	<el-select v-model="tour.classId" placeholder="请选择" >
                    <el-option v-for="x in classes" :label="x.name" :value="x.classId"></el-option>
                </el-select>
			  </el-form-item>
			  <el-form-item label="节次" prop="periodSeq" :label-width="formLabelWidth"> 
			 	<el-select v-model="tour.periodSeq" placeholder="请选择">
                    <el-option v-for="x in periods" :label="x.seq" :value="x.seq"></el-option>
                </el-select> 			
			  </el-form-item>
			  <el-form-item v-for="x in categories" :label="x.category" :label-width="formLabelWidth"> 
			 	<span v-for="c in x.items"><el-switch v-model="c.check"></el-switch>{{c.name}}</span>
			  </el-form-item>
			  <el-form-item label="其它情况说明" prop="description" :label-width="formLabelWidth"> 
			 	<el-input type="textarea" :rows="5" v-model="tour.description" ></el-input>
			  </el-form-item>
		    </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="addTour()">确定</el-button>
				<el-button @click="dialogFormVisible = false">取 消</el-button>
			</div>
		</el-dialog>
</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		gradeUrl:"<s:url value='/grade/list'/>",
		findClassesUrl:"<s:url value='/schoolClass/findByGrade'/>",
		periodsUrl:"<s:url value='/lessonPeriod/findBySchoolYearAndTerm'/>",
		tourCategoriesUrl:"<s:url value='/tour/categories'/>",
		addUrl:"<s:url value='/tour/add'/>",
		findPageUrl:"<s:url value='/tour/findPage'/>",
		formLabelWidth:"120px",
		tour: {tourDate:null, classId:null, periodSeq:null, description:null},
		periods : [],
		grades : [],
		gradeId : null,
		classes : [],
		dialogFormVisible : false,
		categories : [],
		tableData : [],
		condition : {startDate : null, endDate: null,gradeId : null, classId :null, periodSeq : null},
		conditionClasses : [],
		pageSizes :[20,50,100],
		currentPage : 1,
		pageSize:20,
		total:null
	}, 
	mounted : function() { 
		this.findGrades();
		this.findPeriods();
		this.findCategories();

	},
	methods : {
		findGrades : function(){
			this.$http.get(this.gradeUrl).then(
				function(response){
					this.grades=response.data;
				},
				function(response){}
			);
		},
		findClasses : function(){
			this.$http.get(this.findClassesUrl, {params:{gradeId : this.gradeId}}).then(
				function(response){
					this.classes=response.data;
				},
				function(response){}
			);
		},
		findConditionClasses : function(){
			this.$http.get(this.findClassesUrl, {params:{gradeId : this.condition.gradeId}}).then(
				function(response){
					this.conditionClasses=response.data;
				},
				function(response){}
			);
		},
		findPeriods : function(){
			this.$http.get(this.periodsUrl).then(
				function(response){
					this.periods=response.data;
				},
				function(response){}
			);
		},
		findCategories : function(){
			this.$http.get(this.tourCategoriesUrl).then(
				function(response){
					this.categories=response.data;
				},
				function(response){}
			);
		},
		showAdd : function(){
			this.dialogFormVisible=true;
		},
		addTour : function(){
			this.tour.categories=this.categories;
			this.$http.post(this.addUrl, this.tour).then(
				function(response){
					this.dialogFormVisible=false;
					this.$message({
						type:"info",
						message:"新增成功"
					});
				},
				function(response){}
			);
		},
		list : function(){
			this.condition.pageSize=this.pageSize;
			this.condition.currentPage=this.currentPage;
			this.$http.post(this.findPageUrl, this.condition).then(
                function(response){
                	console.log(response.data.content.length);
                    this.tableData=response.data.content;
                    this.total = response.data.totalElements;
            	}
            );
		},
		handleSizeChange :function(val){
			this.currentPage = 1;
			this.pageSize = val;
			this.list();
		},
		handleCurrentChange: function(val){
			this.currentPage = val;
			this.list();
		}
    }	        
});  
</script>
</body>
</html>
