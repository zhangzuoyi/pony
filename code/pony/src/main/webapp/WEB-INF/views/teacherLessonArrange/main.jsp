<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>老师任课安排</title>
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
                <el-col :span="6">
                    <div class="grid-content ">学年：<b>{{currentSchoolYear.name}}</b></div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">学期：<b>{{currentTerm.name}}</b></div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                    <div class="grid-content bg-purple">
                                                  老师:                  
					<el-select v-model="teacherSubjectVo.teacherId" @change="getSubjectByTeacher(teacherSubjectVo.teacherId)" filterable placeholder="请选择..">
               		 <el-option
                        v-for="teacher in teachers" 
                        :label="teacher.name"                      
                        :value="teacher.teacherId">
                        <span style="float: left">{{teacher.name}}({{teacher.teacherNo}})</span>
               		 </el-option>
           			 </el-select>
           			 ( {{teacherSubject}} )				
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content bg-purple-light">
                     <el-button type="primary" v-on:click="save">保存</el-button> 
                     <el-button type="primary" @click="watch">查看任课列表</el-button>                    
                    </div>
                </el-col>              
            </el-row>           
            </div>                     
            <el-card class="box-card content-margin">
            <div slot="header" class="clearfix">
                <span>科目</span>
            </div>
          	<el-radio-group v-model="teacherSubjectVo.subjectId" @change="getClassByTeacherAndSubject(teacherSubjectVo.teacherId,teacherSubjectVo.subjectId)">
                <el-radio-button v-for="subject in subjects" :label="subject.subjectId" >{{subject.name}}</el-radio-button>
                
            </el-radio-group>
          
       		 </el-card>
       		  <el-card class="box-card content-margin">
            <div slot="header" class="clearfix">
                <span>班级</span>
            </div>
          	<el-checkbox-group v-model="teacherSubjectVo.schoolClassIds" >
   			 <el-checkbox  v-for="schoolClass  in schoolClasses"    :label="schoolClass.classId">{{schoolClass.name}}</el-checkbox>
   
  			</el-checkbox-group>
          
       		 </el-card>
            
          
        </el-card>
        
        
        <el-dialog title="任课列表" :visible.sync="dialogFormVisible">
                <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    >                
                <!-- <el-table-column
                        label="老师编号"
                        width="120">
                    <div>{{ row.date }}</div>
                </el-table-column> -->
                 <el-table-column
                        prop="teacherNo"
                        label="老师编号"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="teacherName"
                        label="老师姓名"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="subjectName"
                        label="科目"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="className"
                        label="班级"
                        width="120">
                </el-table-column>                
                <el-table-column
                        prop="weekArrange"
                        label="每周课时"
                        show-overflow-tooltip>
                </el-table-column>
            </el-table>
                
                <div slot="footer" class="dialog-footer">
                    <el-button @click.native="dialogFormVisible = false">关闭</el-button>                 
                </div>
            </el-dialog>
    
    
    </div>
    
    
   

<!-- End of easyui-dialog -->
</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app', 
	data : { 
		
	    teacherSubjectVo : {schoolClassIds:[],teacherId:null,subjectId:null},		
		currentSchoolYear : {},
		currentTerm : {},
		subjects : [],
		schoolClasses :[],
		teachers : [],
		teacherSubject :null,
		currentSchoolYearUrl: "<s:url value='/schoolYear/getCurrent'/>",
		currentTermUrl: "<s:url value='/term/getCurrent'/>",
		subjectsUrl :"<s:url value='/subject/list'/>",
		schoolClassesUrl :"<s:url value='/schoolClass/list'/>",
		teachersUrl :"<s:url value='/teacherAdmin/list'/>",
		saveUrl :"<s:url value='/teacherLessonArrange/save'/>",
		subjectTeacherUrl:"<s:url value='/teacherAdmin/findSubjectByTeacher'/>",
		classTeacherUrl:"<s:url value='/teacherSubject/findByTeacherAndSubject'/>",
		jumpUrl:"<s:url value='/teacherLesson/main/' />",
		teacherLessonUrl : "<s:url value='/teacherLesson/listByTeacher'/>",
		dialogFormVisible:false,
		tableData:[]
		
	}, 
	mounted : function() { 
		this.getCurrentSchoolYear(); 
		this.getCurrentTerm();
		this.getSubjects();
		this.getSchoolClasses(); 
		this.getTeachers(); 
		
		
		
	}, 
	methods : { 
	

		 getCurrentSchoolYear : function(){ 
			this.$http.get(this.currentSchoolYearUrl).then(
			function(response){this.currentSchoolYear=response.data; },
			function(response){}  			
			); 
			} ,
		 getCurrentTerm : function(){ 
			this.$http.get(this.currentTermUrl).then(
			function(response){this.currentTerm=response.data; },
			function(response){}  	 			
			);
			},
		getSubjects : function(){ 
			this.$http.get(this.subjectsUrl).then(
			function(response){this.subjects=response.data; },
			function(response){}  	 			
			);
			},
		getSubjectByTeacher : function(teacherId){ 
		    //取消选中
		    this.teacherSubjectVo.subjectId = null;
		    this.teacherSubjectVo.schoolClassIds = [];
		
			if(teacherId == null ||teacherId ==""){
			return ;
			}
			this.$http.get(this.subjectTeacherUrl,{params:{teacherId:teacherId}}).then(
			function(response){this.teacherSubject=response.data.name; },
			function(response){}  	 			
			);
			},
		getClassByTeacherAndSubject :function(teacherId,subjectId){ 
			if(teacherId == null ||subjectId ==null||teacherId==""||subjectId==""){
			return ;
			}
			this.teacherSubjectVo.schoolClassIds = [];
			this.$http.get(this.classTeacherUrl,{params:{teacherId:teacherId,subjectId:subjectId}}).then(
			function(response){
			for(var index in response.data){
			this.teacherSubjectVo.schoolClassIds.push(response.data[index].classId);			
			}			
			},
			function(response){}  	 			
			);
			},
		getSchoolClasses : function(){ 
			this.$http.get(this.schoolClassesUrl).then(
			function(response){this.schoolClasses=response.data; },
			function(response){}  	 			
			);
			},		
		getTeachers	:function(){ 
			this.$http.get(this.teachersUrl).then(
			function(response){this.teachers=response.data; },
			function(response){}  	 			
			);
			},
		save : function(teacherSubjectVo){
			this.teacherSubjectVo.yearId = this.currentSchoolYear.yearId;
			this.teacherSubjectVo.termId = this.currentTerm.termId;
			
			/* for(var i = 0;i < this.teacherSubjectVo.schoolClassIds.length; i++) {
				if(this.teacherSubjectVo.schoolClassIds[i] == undefined || this.teacherSubjectVo.schoolClassIds[i] == ''){
				  this.teacherSubjectVo.schoolClassIds.splice(i,1);
				}
			} */
			
			
			if(this.teacherSubjectVo.schoolClassIds!=null && this.teacherSubjectVo.schoolClassIds.length>0){
			this.teacherSubjectVo.className = this.teacherSubjectVo.schoolClassIds.join(",");			
			}
			if(this.teacherSubjectVo.className&&this.teacherSubjectVo.teacherId&&this.teacherSubjectVo.subjectId){
			this.$http.post(this.saveUrl,this.teacherSubjectVo).then(
			function(response){
				this.$alert("保存成功","提示",{
		 							confirmButtonText : '确认',		 
								 });
				teacherSubjectVo={schoolClassIds:[],teacherId:null,subjectId:null};
			
			},
			function(response){}  	 			
			);
			}else{
				this.$alert("请选择完整..","提示",{
		 							confirmButtonText : '确认',		 
								 });
			}	
		},
		listByTeacher : function(teacherId){        	
			this.$http.get(this.teacherLessonUrl,{params:{teacherId:teacherId}}).then(
			function(response){this.tableData=response.data; },
			function(response){}  			
			); 	
		},		
		watch : function(){
		 if(this.teacherSubjectVo.teacherId == null || this.teacherSubjectVo.teacherId == ""){
		   return ;
		 }
		 this.dialogFormVisible = true;
		 this.listByTeacher(this.teacherSubjectVo.teacherId);				
		} 
	}
		
}); 

</script>
</body>
</html>