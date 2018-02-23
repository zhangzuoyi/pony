<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>自动排课</title>
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
              <b>自动排课</b>
              </el-col>
              </el-row>
              <el-row> 
              <el-col :span="1">
              <b>年级:</b>
              </el-col>                       
              <el-col :span="4" >
              	<div class="grid-content bg-purple">
					<el-select v-model="gradeId" clearable  filterable placeholder="请选择排课年级.." >
               		 <el-option
                        v-for="grade in grades" 
                        :label="grade.name"                      
                        :value="grade.gradeId">
                        <span style="float: left">{{grade.name}}</span>
               		 </el-option>
           			 </el-select>          			 				
                    </div>
              </el-col> 
              <el-col :span ="4" :offset="2">
                   <el-button type="success" @click="autoLessonArrange()" >自动排课</el-button>                          
              </el-col>           
              </el-row> 
              <el-row>
              <el-col :span="1" >
                    <b>年级:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="conditionVo.gradeId"  @change="getClasses(conditionVo.gradeId)"  filterable placeholder="请选择.." >
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
					<el-select v-model="conditionVo.classId"  filterable placeholder="请选择..">
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
               <el-button type="primary" @click="getListTableData()" >查询</el-button> 
               <el-button type="primary" v-on:click="exportData">导出</el-button> 
                           
              </el-col>
              
              
              </el-row>                                         
            </div> 
            <el-row> 
            <!-- <el-col :span="6"> 
            <el-table
                    :data="teacherLessonTableData"
                    border
                    style="width: 100%"
                    highlight-current-row
    				@current-change="handleCurrentChange"
                   >               
                 <el-table-column
                        prop="teacherNo"
                        label="老师编号"
                        width="120">
                </el-table-column>
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
                        {{selectData.length}}/
                        {{row.weekArrange}}</div>
                        
                </el-table-column>
            </el-table>        
            </el-col> -->
                                
            <el-col :span="18" >                   
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
            
            
            </el-col>          
            </el-row>   
        </el-card>
        
        <el-dialog     close-on-click-modal="false" close-on-press-escape="false" :visible.sync="dialogFormVisible" >
				
				 <el-table 
				    v-loading="loading" 
				    element-loading-text="自动排课中..."            	 		                
                    border
                    style="width: 100%"
                    empty-text=""
                    >                            
                  </el-table>         
				
		</el-dialog>
        
        
               	
    </div>
    
    
   

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : {
	    gradeId : null,
		conditionVo:{gradeId:null,classId:null},		
		grades : [],		
		classes:[],		
		currentSchoolYear : {},
		currentSchoolYearUrl: "<s:url value='/schoolYear/getCurrent'/>",		
		gradesUrl :"<s:url value='/grade/list'/>",
		classesUrl :"<s:url value='/schoolClass/findByYearAndGrade'/>",
		
		weekdaysUrl :"<s:url value='/weekLessonAdmin/listHaveClass'/>",
		listTableDataUrl :"<s:url value='/autoLessonArrange/listTableData'/>",
		autoLessonArrangeUrl : "<s:url value='/autoLessonArrange/autoLessonArrange'/>",
		weekdays :[],
		
		cols:[{prop: 'period',
			label:'时间--星期'
		}],
		
		tableData: [],
		dialogFormVisible:false,
		loading:false
		
	
		
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
		autoLessonArrange : function(){ 
					if(this.gradeId == null || this.gradeId == ""){
						this.$alert("请选择需要排课的年级","提示",{
					 		confirmButtonText : '确认',
						 });
						 return ;
					}
					
		            this.$confirm('自动排课将会删除之前的自动排课结果，是否继续操作？','提示',{
		            confirmButtonText : '确认',
		            cancleButtonText : '取消',
		            type : 'warning',		            
		            }).then(function(){  app.dialogFormVisible=true;   app.loading = true;
		           // console.log(app);
		            app.$http.get(app.autoLessonArrangeUrl,{params:{gradeId :app.gradeId}}).then(
					function(response){app.dialogFormVisible=false;   app.loading = false;},
					function(response){}  	 			
					);           
		            }).catch(function(){    });

					
					},	
		
		 getListTableData:function(){
					this.tableData = [];  //清空表格数据
				
					if(this.conditionVo.classId==null){					
						return ;
						}			 			
					 this.$http.post(this.listTableDataUrl,this.conditionVo).then(
							function(response){
								this.tableData  = response.data.rows;										
							 },
							function(response){}  			
							);   	
							},
			exportData : function(){
			var url = "../exportData";
			window.open(url);
		}	
	  
        },
                
	 
	
});  

</script>
</body>
</html>