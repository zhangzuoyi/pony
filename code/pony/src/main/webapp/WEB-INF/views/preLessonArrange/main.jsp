<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预排</title>
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
              <b>预排</b>
              </el-col>
              </el-row>
              <el-row>
              
              <el-col :span="1" >
                    <b>年级:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="gradeId"  @change="getClasses(gradeId)"  filterable placeholder="请选择.." >
               		 <el-option
                        v-for="grade in grades" 
                        :label="grade.name"                      
                        :value="grade.gradeId">
                        <span style="float: left">{{grade.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            </el-col>
            <el-col :span="1" >
                    <b>班级:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="classId" @change="getTeacherLesson(gradeId,classId)" filterable placeholder="请选择..">
               		 <el-option
                        v-for="schoolClass in classes" 
                        :label="schoolClass.name"                      
                        :value="schoolClass.classId">
                        <span style="float: left">{{schoolClass.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            </el-col>
             
              <el-col :span="4" :offset="8">
              <!--  <el-button type="primary" @click="getListTableData()" >查询</el-button> -->
               <el-button type="primary" @click="save()" >保存</el-button>             
              </el-col>
              
              </el-row>                                          
            </div> 
            <el-row> 
            <el-col :span="6"> 
            <el-table
                    :data="teacherLessonTableData"
                    border
                    style="width: 100%"
                    highlight-current-row
    				@current-change="handleCurrentChange"
                   >               
                 <!-- <el-table-column
                        prop="teacherNo"
                        label="老师编号"
                        width="120">
                </el-table-column> -->
                <el-table-column
                        prop="teacherName"
                        label="老师"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="subjectName"
                        label="科目"
                        width="70">
                </el-table-column>                              
                <el-table-column
               			inline-template
                       
                        label="每周课时"
                        show-overflow-tooltip>
                        <div>
                       <!--  {{selectData.length}}/ -->
                        {{row.weekArrange}}</div>
                        
                </el-table-column>
            </el-table>        
            </el-col>
                                
            <el-col :span="18" >                   
            <el-table  
           	 		:data="tableData"                 
                    border
                    style="width: 100%"
                    @cell-click="cellClick"
                    @cell-mouse-enter="mouseEnter"
                    @cell-mouse-leave="mouseLeave"
                    >                            
                           
                  <el-table-column 
        			v-for="col in cols"
        			:prop="col.prop" 
        			:label="col.label"
        			width="120" 
        			>
     		 </el-table-column> 
     		   
                                               
            </el-table>
            
            
            </el-col>          
            </el-row>   
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
								this.$alert("保存成功","提示",{
		 							confirmButtonText : '确认',		 
								 });
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