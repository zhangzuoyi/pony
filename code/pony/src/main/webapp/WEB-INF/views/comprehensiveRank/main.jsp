<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生整体成绩分析</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/elementUI/index.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/elementUI/element.css' />" />
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/dateFormat.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/echarts/echarts.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue-resource.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/elementUI/index.js' />"></script>

</head>
<body >
<div id="app">
 <div>   	           	
        	<el-card class="box-card content-margin">
            <div slot="header" class="clearfix">
              <el-row>
              
              <el-col :span="1" >
                    <b>年级:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="conditionVo.gradeId"    filterable placeholder="请选择.." >
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
                    <b>考试:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-select v-model="conditionVo.examId"    filterable placeholder="请选择..">
               		 <el-option
                        v-for="exam in exams" 
                        :label="exam.name"                      
                        :value="exam.examId">
                        <span style="float: left">{{exam.name}}</span>
               		 </el-option>
           			 </el-select>				
                    </div>
            
            </el-col>
            
             
              <el-col :span="4" :offset="2">
               <el-button type="success" @click="rank()" >统计排名</el-button>
               <el-button type="primary" @click="getListTableData()" >查询</el-button>
                       
              </el-col>
              
              </el-row> 
                                                                      
            </div> 
            <el-row> 
            <el-col :span="22" :offset="1"  >
            <el-table  
           	 		:data="tableData"                 
                    border
                    style="width: 100%"                  
                    >                            
                           
                  <el-table-column 
        			v-for="col in cols"
        			:prop="col.prop" 
        			:label="col.label"
        			>
     		 </el-table-column> 
     		   
                                               
            </el-table>
            </el-col> 
                                                              
            </el-row> 
            
        </el-card>  
        
         <el-dialog     close-on-click-modal="false" close-on-press-escape="false" :visible.sync="dialogFormVisible" >
				
				 <el-table 
				    v-loading="loading" 
				    element-loading-text="排名统计中..."            	 		                
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
	
		conditionVo : {gradeId:null,examId:null},				
		grades : [],			
		exams:[],					
		gradesUrl :"<s:url value='/grade/list'/>",
		examsUrl : "<s:url value='/exam/findCurrentExam' />",		
		listTableDataUrl :"<s:url value='/comprehensiveRank/findByCondition'/>",
		rankUrl :"<s:url value='/comprehensiveRank/rank'/>",				
		tableData: [],		
        cols:[]	,
        dialogFormVisible :false,
        loading : false		
		
	}, 
	mounted : function() { 				
		this.getGrades();
		this.getExams();
			
	}, 
	methods : { 
		  
			  
		  getGrades	:function(){ 
			this.$http.get(this.gradesUrl).then(
			function(response){this.grades=response.data; },
			function(response){}  	 			
			);
			},
			
		  getExams	:function(){		  		  		   
			this.$http.get(this.examsUrl).then(
			function(response){this.exams=response.data; },
			function(response){}  	 			
			);
			},
		  rank :function(){
		  if(this.conditionVo.examId==null||this.conditionVo.gradeId==null){					
						this.$alert("请选择考试和年级","提示",{confirmButtonText : '确认'});
						return ;
						}
			this.dialogFormVisible=true;  
		 	this.loading = true;
		  this.$http.post(this.rankUrl,this.conditionVo).then(
							function(response){
							   if(response.data == 0){
                                   this.dialogFormVisible=false;
                                   this.loading = false;
                                   this.$message({type:"info",message:"该排名已经生成!"});
                               }
                                if(response.data == 1){
                                    this.dialogFormVisible=false;
                                    this.loading = false;
                                    this.$message({type:"info",message:"生成成功!"});
                                }


                    	
				 },
							function(response){}  			
							); 				
		  
		  },
	
		  getListTableData:function(){
					this.tableData = [];  //清空表格数据
				
					if(this.conditionVo.examId==null||this.conditionVo.gradeId==null){					
						this.$alert("请选择考试和年级","提示",{confirmButtonText : '确认'});
						return ;
						}			 			
					 this.$http.post(this.listTableDataUrl,this.conditionVo).then(
							function(response){
                    this.cols= response.data.title;
                   	this.tableData  = response.data.rows;                   
                    	
				 },
							function(response){}  			
							);   	
				}, 
				
	
	  
        }	        	
});  


	
</script>
</body>
</html>