<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>老师任课列表</title>
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
              <el-col :span="6">
                    <div class="grid-content bg-purple">
                                                  年级:                  
					<el-select v-model="conditionVo.gradeId" clearable @change="getClasses(conditionVo.gradeId)"  filterable placeholder="请选择.." >
               		 <el-option
                        v-for="grade in grades" 
                        :label="grade.name"                      
                        :value="grade.gradeId">
                        <span style="float: left">{{grade.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>				
                </el-col>
                <el-col :span="6">
                    <div class="grid-content bg-purple">
                                                  班级:                  
					<el-select v-model="conditionVo.classId" clearable filterable placeholder="请选择..">
               		 <el-option
                        v-for="schoolClass in classes" 
                        :label="schoolClass.name"                      
                        :value="schoolClass.classId">
                        <span style="float: left">{{schoolClass.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
                </el-col>                
                </el-row>
                
                <el-row>
                <el-col :span="6">
                    <div class="grid-content bg-purple">
                                                  科目:                  
					<el-select v-model="conditionVo.subjectId" clearable filterable placeholder="请选择..">
               		 <el-option
                        v-for="subject in subjects" 
                        :label="subject.name"                      
                        :value="subject.subjectId">
                        <span style="float: left">{{subject.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
                </el-col>
              
              
                <el-col :span="6">
                    <div class="grid-content bg-purple">
                                                  老师:                  
					<el-select v-model="conditionVo.teacherId" clearable filterable placeholder="请选择..">
               		 
               		 <el-option
                        v-for="teacher in teachers" 
                        :label="teacher.name"                      
                        :value="teacher.teacherId">
                        <span style="float: left">{{teacher.name}}({{teacher.teacherNo}})</span>
               		 </el-option>
           			 </el-select>				
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content bg-purple-light">
                     <el-button type="primary" v-on:click="list">查询</el-button> 
                     <el-button type="primary" @click.native="dialogFormVisible = true">每周课时设置</el-button>                    
                    </div>
                </el-col>              
            </el-row>

            <el-dialog title="每周课时设置" v-model="dialogFormVisible">
                <el-form :model="selectTeachers">
                    <el-form-item label="课时数" :label-width="formLabelWidth">
                        <el-input v-model="selectTeachers.weekArrange"   ></el-input>
                    </el-form-item>                  
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click.native="dialogFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="submit">确 定</el-button>
                </div>
            </el-dialog>
                      
            </div>                     
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    @selection-change="handleSelectionChange">
                <el-table-column
                        type="selection"
                        width="50">
                </el-table-column>
                <!-- <el-table-column
                        inline-template
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
            
          
        </el-card>
        	
    </div>
    
    
   

<!-- End of easyui-dialog -->
</div>
<script type="text/javascript">
		
 var app = new Vue({ 
	el : '#app', 
	data : { 
		dialogFormVisible:false,
		formLabelWidth:'120px',
	    conditionVo:{gradeId:null,classId:null,subjectId:null,teacherId:null},  
		currentSchoolYear : {},
		currentTerm : {},		
		teachers : [],
		grades:[],
		classes:[],
		subjects:[],
		currentSchoolYearUrl: "<s:url value='/schoolYear/getCurrent'/>",
		currentTermUrl: "<s:url value='/term/getCurrent'/>",		
		teachersUrl :"<s:url value='/teacherAdmin/list'/>",
		teacherLessonUrl : "<s:url value='/teacherLesson/list'/>",
		submitUrl : "<s:url value='/teacherLesson/submit'/>",
		gradesUrl :"<s:url value='/grade/list'/>",
		classesUrl :"<s:url value='/schoolClass/findByYearAndGrade'/>",
		subjectUrl:"<s:url value='/subject/list'/>",
		tableData: [],
        multipleSelection: [],
        selectTeachers :{selectTeacherId:[],weekArrange:1}
		
	}, 
	mounted : function() { 
		this.getGrades();
		this.getSubjects();			
		this.getCurrentSchoolYear(); 
		this.getCurrentTerm();	
		this.getTeachers();
		this.list(); 
		
		
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
		getGrades	:function(){ 
			this.$http.get(this.gradesUrl).then(
			function(response){this.grades=response.data; },
			function(response){}  	 			
			);
			},
		getSubjects	:function(){ 
			this.$http.get(this.subjectUrl).then(
			function(response){this.subjects=response.data; },
			function(response){}  	 			
			);
			},
		getClasses	:function(gradeId){ 
		 
		 	this.conditionVo.classId = null;
			
			this.$http.get(this.classesUrl,{params:{yearId:this.currentSchoolYear.yearId,gradeId:gradeId}}).then(
			function(response){this.classes=response.data; },
			function(response){}  	 			
			);
			},	
		getTeachers	:function(){ 
			this.$http.get(this.teachersUrl).then(
			function(response){this.teachers=response.data;
			
			 },
			function(response){}  	 			
			);
			},
		handleSelectionChange:function(teacherSubjects) {
                for(index in teacherSubjects){
                this.selectTeachers.selectTeacherId.push(teacherSubjects[index].tsId);               
                }                                
            },
        list : function(){
        	this.selectTeachers.selectTeacherId=[];this.selectTeachers.weekArrange=1;
			this.$http.post(this.teacherLessonUrl,this.conditionVo).then(
			function(response){this.tableData=response.data; },
			function(response){}  			
			); 	
		},
		submit : function(){
		if(this.selectTeachers.selectTeacherId!=null && this.selectTeachers.selectTeacherId.length>0){
			this.$http.get(this.submitUrl,{params:{teacherIds:this.selectTeachers.selectTeacherId,weekArrange:this.selectTeachers.weekArrange}}).then(
			function(response){ this.dialogFormVisible=false;this.list();},
			function(response){}  			
			); 
		}else{
			this.$alert("请选择完整..","提示",{
		 							confirmButtonText : '确认',		 
								 });
			this.dialogFormVisible=false;
		}				
		}
		
           
            
	}	
});  

</script>
</body>
</html>