<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>科目不上课设置</title>
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
              <b>科目不上课设置</b>
              </el-col>
              
              <el-col :span="4" :offset="16">
               <el-button type="primary" @click="getListTableData()" >查询</el-button>
               <el-button type="primary" @click="save()" >保存</el-button>             
              </el-col>
              
              </el-row>                                            
            </div> 
            <el-row> 
            <el-col :span="6"> 
            <el-row>           
            <el-col :span="4" >
                    <b>年级:</b>                                    
            </el-col> 
            <el-col :span="20" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="gradeId" filterable placeholder="请选择..">
               		 <el-option
                        v-for="grade in grades" 
                        :label="grade.name"                      
                        :value="grade.gradeId">
                        <span style="float: left">{{grade.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            </el-col>
            </el-row> 
            <el-row>
            
            <el-col :span="4" >
                    <b>科目:</b>                                    
            </el-col> 
            <el-col :span="20" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="subjectId" filterable placeholder="请选择..">
               		 <el-option
                        v-for="subject in subjects" 
                        :label="subject.name"                      
                        :value="subject.subjectId">
                        <span style="float: left">{{subject.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            </el-col>
            </el-row> 
            
            
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
        			width="150" 
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
		subjectId:null,
		subjects:[],
		gradesUrl :"<s:url value='/grade/list'/>",
		subjectsUrl :"<s:url value='/subject/list'/>",
		weekdaysUrl :"<s:url value='/weekLessonAdmin/listHaveClass'/>",
		listTableDataUrl :"<s:url value='/subjectNoCourse/listTableData'/>",
		saveUrl :"<s:url value='/subjectNoCourse/save'/>",
		deleteUrl:"<s:url value='/subjectNoCourse/delete'/>",
		findVoBySubjectUrl:"<s:url value='/subjectNoCourse/findVoBySubject'/>",
		weekdays :[],
		cols:[{prop: 'period',
			label:'时间--星期'
		}],
		tableData: [],
		selectData:[]
	
		
	}, 
	mounted : function() { 
		this.getSubjects();
		this.getGrades();
		this.getHaveClass();
			
	}, 
	methods : { 	
		  getSubjects	:function(){ 
			this.$http.get(this.subjectsUrl).then(
			function(response){this.subjects=response.data; },
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
		cellClick:function(row,column,cell){
		       		
		       		//row.period   9:00--9:45
		       		//column.label  星期一
		       		if(this.gradeId==null || this.subjectId==null){					
					return ;
					}
		       		if(cell.style.backgroundColor == "rgb(255, 0, 0)" ){
		       		//删除操作
                        var length = this.selectData.length;
       			for(var i=0;i<length;i++){
       			 if(this.selectData[i].period == row.period && this.selectData[i].weekday == column.label  ){
       			   this.selectData.splice(i,1);     			   
       			 }
       			}
       			cell.style.backgroundColor = "";
       			       			cell.innerText = "";
       			
		       			return;
		       		}
		       		//#F00
		       		cell.style.backgroundColor="#F00";
		       		this.selectData.push({period:row.period,weekday:column.label,gradeId:this.gradeId,subjectId:this.subjectId});
		   		
		       	},
		 mouseEnter:function(row,column,cell){ 
		       		if(cell.style.backgroundColor == "rgb(255, 0, 0)" ){
		       			return;
		       		}
		       		cell.style.backgroundColor="#F4A460";   		
		       	},
		 mouseLeave:function(row,column,cell){
		       		if(cell.style.backgroundColor == "rgb(255, 0, 0)" ){
		       			return;
		       		}
		       		cell.style.backgroundColor="";
		       	},
		 getListTableData:function(){
					this.tableData = [];  //清空表格数据
				
					if(this.gradeId==null||this.subjectId==null){					
						this.$alert("请选择年级和科目","提示",{				
		 				confirmButtonText : '确认',		
						 });
						return ;
						}			 			
					 this.$http.get(this.listTableDataUrl,{params:{gradeId:this.gradeId,subjectId:this.subjectId}}).then(
							function(response){
								this.tableData  = response.data.tableData;																 			
							 },
							function(response){}  			
							);  
					this.$http.get(this.findVoBySubjectUrl,{params:{gradeId:this.gradeId,subjectId:this.subjectId}}).then(
							function(response){
									this.selectData=[];																					
									for(var index in response.data){
										this.selectData.push({period:response.data[index].lessonPeriodSeq,weekday:response.data[index].weekdayName,gradeId:this.gradeId,subjectId:this.subjectId});
									}																													 			
							 },
							function(response){}  			
							); 			
							},
		save:function(){ 	
					  if(this.selectData.length ==0){
					 this.$http.get(this.deleteUrl, {params:{gradeId:this.gradeId,subjectId:this.subjectId}}).then(
							function(response){
								this.selectData = [];
								this.getListTableData();
                                this.$alert("保存成功","提示",{
                                    confirmButtonText : '确认',
                                });
							 },
							function(response){}  			
							); 
					 
					 }else{
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
							}  		  
		       			 },       	
		       	
			
			
		  
        }	        
	 
	
});  

</script>
</body>
</html>