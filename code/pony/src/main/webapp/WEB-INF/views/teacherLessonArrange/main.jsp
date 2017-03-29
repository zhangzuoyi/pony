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
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/bootstrap/js/bootstrap.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/dateFormat.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue-resource.min.js' />"></script>
</head>
<body>
<div id="app">
  <div>
    	<div class="mainHeader">      
        	<div class="search">
        		<div class="row">     	   	 	
       	    		<div class="col-lg-4 col-md-4"><span>学年：{{currentSchoolYear.name}}</span></div>         	
         		    <div class="col-lg-4 col-md-4"><span>学期：{{currentTerm.name}}</span> </div>        
            		<div class="col-lg-4 col-md-4">
            		<button type="button" class="btn btn-primary btn-sm" v-on:click="save">保存</button>  
            		<button type="button" class="btn btn-primary btn-sm" >查看任课列表</button>                                 		                               		
            		</div>                           
           		 </div>
        	</div>
		</div>
    	<div class="mainBody">
    		<div class="panel panel-default">
   				 <div class="panel-heading">
        		<h3 class="panel-title">老师</h3>
    			</div>
   				 <div class="panel-body">
       			<select v-model="teacherSubjectVo.teacherId" class="form-control" style="width:200px;">
       				<option value="">请选择</option>  
  					<option v-for="teacher in teachers" v-bind:value="teacher.teacherId">  
   					 {{ teacher.name}}({{teacher.teacherNo}})  
 					 </option>  
				</select>  
   			 </div>
		   </div>
		   <div class="panel panel-default">
   				 <div class="panel-heading">
        		<h3 class="panel-title">科目</h3>
    			</div>
   				 <div class="panel-body">
       			 <div>	
       			 	<span  v-for="subject in subjects">
       			 	<label class="checkbox-inline">
    				 <input type="radio" name="subject"  v-bind:value="subject.subjectId" v-model="teacherSubjectVo.subjectId" >{{subject.name}}
 				 	</label>
 				 	</span>
  				 </div>
       			 	
       			 	
       			 	
   			 	 </div>
		   </div>
		   <div class="panel panel-default">
   				 <div class="panel-heading">
        		<h3 class="panel-title">班级</h3>
    			</div>
   				 <div class="panel-body">      			 
                    <div>
                        <label class="checkbox-inline" v-for="(schoolClass,index)  in schoolClasses">
                            <input type="checkbox" :true-value="schoolClass.classId" v-model="teacherSubjectVo.schoolClassIds[index]"   name="schoolClass"  >
                            <span>{{schoolClass.name}}</span>
                        </label>
                    </div>
                    
   			 </div>
		   </div>
    	
    	</div>
    
    
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
		currentSchoolYearUrl: "<s:url value='/schoolYear/getCurrent'/>",
		currentTermUrl: "<s:url value='/term/getCurrent'/>",
		subjectsUrl :"<s:url value='/subject/list'/>",
		schoolClassesUrl :"<s:url value='/schoolClass/list'/>",
		teachersUrl :"<s:url value='/teacherAdmin/list'/>",
		saveUrl :"<s:url value='/teacherLessonArrange/save'/>"
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
			/* this.teacherSubjectVo.schoolClassIds.forEach(function(val,index){
				if(val.length == 0){
				  this.teacherSubjectVo.schoolClassIds.splice(index,1);
				}
			}); */
			for(var i = 0;i < this.teacherSubjectVo.schoolClassIds.length; i++) {
				if(this.teacherSubjectVo.schoolClassIds[i] == undefined || this.teacherSubjectVo.schoolClassIds[i] == ''){
				  this.teacherSubjectVo.schoolClassIds.splice(i,1);
				}
			}
			
			
			if(this.teacherSubjectVo.schoolClassIds!=null && this.teacherSubjectVo.schoolClassIds.length>0){
			this.teacherSubjectVo.className = this.teacherSubjectVo.schoolClassIds.join(",");			
			}
			if(this.teacherSubjectVo.className&&this.teacherSubjectVo.teacherId&&this.teacherSubjectVo.subjectId){
			this.$http.post(this.saveUrl,this.teacherSubjectVo).then(
			function(response){ alert("123"); },
			function(response){}  	 			
			);
			}else{
				alert("请选择完整..");
			}
			
			
		
		}		
		} 
		
}); 

</script>
</body>
</html>