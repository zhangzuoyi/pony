<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>消息发送</title>
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
              <el-col :span="4">
              <b>消息发送</b>
              </el-col>
              </el-row>
              <el-row>
              
              <el-col :span="2" :offset="4">
                    <b>收件人:</b>                                    
            </el-col> 
            </el-row>
            <el-row>
            <el-col :span="2" :offset="5">
                    <b>用户组:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-select multiple v-model="userGroup"    filterable placeholder="请选择.." >
               		 <el-option              		 
                        v-for="userGroup in userGroups" 
                        :label="userGroup.name"                      
                        :value="userGroup.groupId">
                        <span style="float: left">{{userGroup.name}}</span>
               		 </el-option>
           			 </el-select>				
               </div>           
            </el-col>      
            </el-row>
            <el-row>
            <el-col :span="2" :offset="5">
                    <b>用户:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-select multiple v-model="users"    filterable placeholder="请选择.." >
               		 <el-option
                        v-for="user in users" 
                        :label="user.name"                      
                        :value="user.userId">
                        <span style="float: left">{{user.name}}</span>
               		 </el-option>
           			 </el-select>				
               </div>           
            </el-col>      
            </el-row>
            <el-row>
            <el-col :span="2" :offset="4" >
                    <b>标题:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
				<el-input v-model="title" placeholder="请输入内容"></el-input>					
             </div>           
            </el-col>
            
            </el-row>
            <el-row>
            <el-col :span="2" :offset="4" >
                    <b>内容:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
				<el-input v-model="content" 
				type="textarea"
 				 :rows="5" placeholder="请输入内容"></el-input>					
             </div>           
            </el-col>           
            </el-row>
             <el-row>
            <el-col :span="2" :offset="4" >
                    <b>附件:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
			<el-upload
  			 ref="upload"
 			 :on-preview="handlePreview"
  			:on-remove="handleRemove"
  			:file-list="fileList"
  			:auto-upload="false">
  			<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
</el-upload>

  
</el-upload>					
             </div>           
            </el-col>           
            </el-row>
            
            <el-row>
            <el-col :span="2" :offset="4"> 发送 </el-col>
            <el-col :span="2" :offset="4"> 暂存 </el-col>                   
            </el-row>
                  
            </div> 
              
        </el-card>       	
    </div>
    
    
   

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		gradeId:null,
		grades : [],
		classId:null,
		classes:[],
		subjectId:null,
		currentSchoolYear : {},
		currentSchoolYearUrl: "<s:url value='/schoolYear/getCurrent'/>",		
		gradesUrl :"<s:url value='/grade/list'/>",
		classesUrl :"<s:url value='/schoolClass/findByYearAndGrade'/>",
		teacherLessonUrl :"<s:url value='/teacherLesson/listBySchoolClass'/>",
		weekdaysUrl :"<s:url value='/weekLessonAdmin/listHaveClass'/>",
		listTableDataUrl :"<s:url value='/preLessonArrange/listTableData'/>",
		saveUrl :"<s:url value='/preLessonArrange/save'/>",
		weekdays :[],
		cols:[{prop: 'period',
			label:'时间--星期'
		}],
		teacherLessonTableData:[],
		tableData: [],
		selectData:[],
		currentRow:[]
		
	
		
	}, 
	mounted : function() { 
		this.getCurrentSchoolYear(); 		
		this.getGrades();
		this.getHaveClass();
			
	}, 
	methods : { 
			getCurrentSchoolYear : function(){ 
			this.$http.get(this.currentSchoolYearUrl).then(
			function(response){this.currentSchoolYear=response.data; },
			function(response){}  			
			); 
			} ,	
		  getClasses	:function(gradeId){ 
			
			this.$http.get(this.classesUrl,{params:{yearId:this.currentSchoolYear.yearId,gradeId:gradeId}}).then(
			function(response){this.classes=response.data; },
			function(response){}  	 			
			);
			},
		  getGrades	:function(){ 
			this.$http.get(this.gradesUrl).then(
			function(response){this.grades=response.data; },
			function(response){}  	 			
			);
			},
		  getTeacherLesson : function(gradeId,classId){
			  this.$http.get(this.teacherLessonUrl,{params:{gradeId:gradeId,classId:classId}}).then(
						function(response){this.teacherLessonTableData=response.data; },
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
		cellClick:function(row,column,cell){
		       		
		       		//row.period   9:00--9:45
		       		//column.label  星期一
		       		if(this.classId==null || this.subjectId==null){					
					return ;
					}
		       		if(cell.style.backgroundColor == "rgb(0, 255, 0)" ){
		       			return;
		       		} 
		       		//#F00
		       		cell.style.backgroundColor="#0F0";
		       		this.selectData.push({period:row.period,weekDay:column.label,classId:this.classId,subjectId:this.subjectId}); 
		   		
		       	},
		 mouseEnter:function(row,column,cell){ 
		       		if(cell.style.backgroundColor == "rgb(0, 255, 0)" ){
		       			return;
		       		}
		       		cell.style.backgroundColor="#F4A460";   		
		       	},
		 mouseLeave:function(row,column,cell){
		       		if(cell.style.backgroundColor == "rgb(0, 255, 0)" ){
		       			return;
		       		}
		       		cell.style.backgroundColor="";
		       	},
		 getListTableData:function(){
					this.tableData = [];  //清空表格数据
				
					if(this.classId==null||this.subjectId==null){					
						return ;
						}			 			
					 this.$http.get(this.listTableDataUrl,{params:{classId:this.classId,subjectId:this.subjectId}}).then(
							function(response){
								this.tableData  = response.data.tableData;
								this.selectData=[];
									
									/* for(var index in this.tableData){
										for(var key in this.tableData[index]){
											if(this.tableData[index][key] != "" && key !="period"){
												this.selectData.push({period:this.tableData[index][key],weekDay:key,classId:this.classId,subjectId:this.subjectId});
											}											
										}										
									} */
							       		
									
									
								
								
							 },
							function(response){}  			
							);   	
							},
		save:function(){ 	
					if(this.selectData.length ==0){return;}
					this.$http.post(this.saveUrl, this.selectData).then(
							function(response){
								this.selectData = [];
								this.getListTableData();
								alert("保存成功");
							 },
							function(response){}  			
							);   		  
		       			 },       	
		handleCurrentChange:function(currentRow) {
							if(currentRow!=null){
								this.currentRow = currentRow;
			       		        this.subjectId = this.currentRow.subjectId;
			       		        this.getListTableData();	
							}
		       		        
		       }     	
			
			
		  
        }	        
	 
	
});  

</script>
</body>
</html>