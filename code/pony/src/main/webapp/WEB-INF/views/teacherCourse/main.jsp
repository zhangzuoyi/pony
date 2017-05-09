<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教师课表</title>
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
              <b>教师课表</b>
              </el-col>
              </el-row>
              <el-row>
               <el-col >
                <el-col :span="6">
                    <div class="grid-content bg-purple">
                                                  老师:                  
					<el-select v-model="teacherId" clearable filterable placeholder="请选择..">
               		 
               		 <el-option
                        v-for="teacher in teachers" 
                        :label="teacher.name"                      
                        :value="teacher.teacherId">
                        <span style="float: left">{{teacher.name}}({{teacher.teacherNo}})</span>
               		 </el-option>
           			 </el-select>				
                    </div>
                </el-col>
                <el-col :offset="18">
                <el-button type="primary" @click="getTeacherCourse">查询</el-button> 
                </el-col>                                
               </el-col>              
              </el-row>
            </div>
            <el-table  
           	 		:data="tableData"                 
                    border
                    style="width: 100%"
                    >                            
                           
                  <el-table-column 
        			v-for="col in cols"
        			:prop="col.prop" 
        			:label="col.label"
        			width="150" 
        			>
     		 </el-table-column> 
     		   
                                               
            </el-table> 

			

        </el-card>
			


		</div>
 

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
	teacherId:null,	
	teachers : [],	
	teachersUrl :"<s:url value='/teacherAdmin/list'/>",
	teacherCourseUrl :"<s:url value='/teacherCourse/list'/>",
	weekdaysUrl :"<s:url value='/weekLessonAdmin/listHaveClass'/>",
	weekdays :[],
	
	tableData: [],
	cols:[{prop: 'period',
			label:'时间--星期'
		}],
	
		
	}, 
	
	mounted : function() { 
		this.getHaveClass();		
		this.getTeachers();	
	}, 
	methods : { 
	   getTeachers	:function(){ 
			this.$http.get(this.teachersUrl).then(
			function(response){this.teachers=response.data;
			
			 },
			function(response){}  	 			
			);
			},
	getHaveClass : function(){ 			
				this.$http.get(this.weekdaysUrl).then(
				function(response){
					this.weekdays  = response.data;				
					for(var index in this.weekdays){
						this.cols.push({prop: this.weekdays[index].seqName,
							label: this.weekdays[index].name
							});	//{prop:Monday,label:"星期一"}					
					} 
				 },
				function(response){}  			
				); 	
				},		
	getTeacherCourse	:function(){
			this.tableData = [];  //清空表格数据
			if(this.teacherId == null || this.teacherId == ""){
			this.$alert("教师不能为空","提示",{
			confirmButtonText : '确认',
			});
			return ;
			}	 
			this.$http.get(this.teacherCourseUrl,{params:{teacherId:this.teacherId}}).then(
			function(response){
			this.tableData=response.data.rows;
			 },
			function(response){}  	 			
			);
			},		
		      
	 
	}
});  

</script>
</body>
</html>
