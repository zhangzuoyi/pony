<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>班级不上课设置</title>
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
              <b>班级不上课设置</b>
              </el-col>
              
              <el-col :span="4" :offset="16">
               <el-button type="primary" @click="getListTableData()" >查询</el-button>
               <el-button type="primary" @click="save()" >保存</el-button> 
               <el-button type="primary" @click.native="findAll" >查看</el-button>             
              </el-col>
              
              </el-row>
              
                                                         
            </div> 
            <el-row>
            <el-col :span="4" >
            <el-tree
                    :data="treeData"
                    default-expand-all
                    highlight-current
                    ref="tree"
                    :props="props"
                    node-key="id"
                    show-checkbox                  
                   >
            </el-tree>
            
            </el-col>                      
            <el-col :span="20" >                   
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
    
    <el-dialog title="查看"  :visible.sync="dialogFormVisible" >
          <el-row>
              <el-table
                      :data="tableData2"
                      highlight-current-row
                      border
                      height="250"
                      >
                  <el-table-column
                          prop="schoolYearName"
                          label="学年"
                  >
                  </el-table-column>
                  <el-table-column
                          prop="termName"
                          label="学期"
                  >
                  </el-table-column>
                  <el-table-column
                          prop="className"
                          label="班级"
                  >
                  </el-table-column>
                  <el-table-column
                          prop="week"
                          label="星期"
                          >
                  </el-table-column>
                  <el-table-column
                          prop="period"
                          label="上课时段">
                  </el-table-column>                
              </el-table>
          </el-row>
          <div slot="footer" class="dialog-footer">                                     
                    <el-button @click.native="dialogFormVisible = false">取 消</el-button>
                </div>         
          </el-dialog>
    
    
   

</div>
<script type="text/javascript">
		
 var app = new Vue({ 
	el : '#app' ,
	data : { 		
		
		tableData: [],
		singleClassId :{},    
		schoolClassTreeUrl :"<s:url value='/schoolClass/listTree'/>",	
		weekdaysUrl :"<s:url value='/weekLessonAdmin/listHaveClass'/>",
		lessonPeriodsUrl   :"<s:url value='/lessonPeriod/findBySchoolYearAndTerm'/>",
      	classNoCourseUrl :"<s:url value='/classNoCourse/listVo'/>",
      	listTableDataUrl : "<s:url value='/classNoCourse/listTableData'/>",  
      	saveUrl : "<s:url value='/classNoCourse/save'/>", 
      	deleteUrl:"<s:url value='/classNoCourse/delete'/>",
		findVoByClassUrl:"<s:url value='/classNoCourse/findVoByClass'/>",  
		findAllUrl :  "<s:url value='/classNoCourse/findAll'/>",  	
		treeData: [],    
       	props: {
                    label: 'label',
                    children: 'children'
                },
        weekdays :[],
        lessonPeriods:[],
        classNoCourse:[],
        cols:[{prop: 'period',
			label:'时间--星期'
		}],
		selectData:[],
		dialogFormVisible : false,
		tableData2 :[]
                
	
		
	}, 
	mounted : function() { 	
		this.getSchoolClassTree();
		this.getHaveClass();
		
			
	}, 
	methods : { 		
		  getSchoolClassTree : function(){ 			
			this.$http.get(this.schoolClassTreeUrl).then(
			function(response){
				this.treeData  = response.data.treeData;
			 },
			function(response){}  			
			); 	
			},
		getCheckedKeys : function(){
			/* console.log(this.$refs.tree.getCheckedKeys());
			if(this.$refs.tree.getCheckedKeys().length==1){
			this.singleClassId=this.$refs.tree.getCheckedKeys()[0];
			this.getListTableData(this.singleClassId); 
			} */				
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
		getLessonPeriods : function(){ 			
				this.$http.get(this.lessonPeriodsUrl).then(
				function(response){
					this.lessonPeriods  = response.data;
					 /* for(var index in this.lessonPeriods){
						var colData ={period :  this.lessonPeriods[index].startTime+"--"+this.lessonPeriods[index].endTime};						
						for(idx in this.weekdays){
					}						
						this.tableData.push(colData);																						
					}  */ 										 			
				 },
				function(response){}  			
				); 	
				},
		getClassNoCourse:function(){ 			
			this.$http.get(this.classNoCourseUrl).then(
					function(response){
						this.classNoCourse  = response.data;
						
												 			
					 },
					function(response){}  			
					); 	
					},
		findAll : function(){
		this.tableData2 = [];
		this.$http.get(this.findAllUrl).then(
					function(response){
						this.tableData2  = response.data.tableData;																 			
					 },
					function(response){}  			
					); 
		 this.dialogFormVisible = true;
		},
					
		getListTableData:function(){
			this.tableData = [];  //清空表格数据
		
			if(this.$refs.tree.getCheckedKeys().length==1){
			this.singleClassId=this.$refs.tree.getCheckedKeys()[0];			
			}else{
			this.$alert("请选择班级","提示",{				
		 				confirmButtonText : '确认',		
						 });
			return ;
			}					 			
			this.$http.get(this.listTableDataUrl,{params:{classId:this.singleClassId}}).then(
					function(response){
						this.tableData  = response.data.tableData;																 			
					 },
					function(response){}  			
					); 
			this.$http.get(this.findVoByClassUrl,{params:{classId:this.singleClassId}}).then(
							function(response){
									this.selectData=[];																						
									for(var index in response.data){
										this.selectData.push({period:response.data[index].lessonPeriodSeq,weekday:response.data[index].weekdayName,classIds:[this.singleClassId]});
									}																													 			
							 },
							function(response){}  			
							);		
						
					},
		 save:function(){ 	
			if(this.selectData.length ==0){
					 this.$http.get(this.deleteUrl, {params:{classId:this.singleClassId}}).then(
							function(response){
								this.selectData = [];
								this.getListTableData();		
								this.alert();
							 },
							function(response){}  			
							); 
					 
					 }else{
			this.$http.post(this.saveUrl, this.selectData).then(
					function(response){
						this.selectData = [];
						this.getListTableData();		
						this.alert();
					 },
					function(response){}  			
					); 
					}		  
       			 },
       	alert :function(){
		 this.$alert("保存成功","提示",{
		 confirmButtonText : '确认',
		 //回调(可选)
		 /* callback :  function(action,instance){
		   instance.$message({
		   type : 'info',
		   message : 'action :   '+ action
		   });
		 }  */
		 });
		},		 
       			 
       	cellClick:function(row,column,cell){
       		
       		//row.period   9:00--9:45
       		//column.label  星期一
       		if(this.$refs.tree.getCheckedKeys().length>=1){
			var selectClassIds=this.$refs.tree.getCheckedKeys();			
			}else{
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
       		this.selectData.push({period:row.period,weekday:column.label,classIds:selectClassIds});
   		
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
       	} 	
       		
       		
       		
   } 
       		
       		        
	 
	
});  

</script>
</body>
</html>