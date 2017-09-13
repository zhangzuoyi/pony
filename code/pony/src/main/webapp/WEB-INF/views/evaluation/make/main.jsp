<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>评价</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/bootstrap/css/bootstrap.min.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/elementUI/index.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/elementUI/element.css' />" />
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/bootstrap/js/bootstrap.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/vue-resource.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/elementUI/index.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/js/moment.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/vue/myfilters.js' />"></script>
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
	              <b>${subject.name}</b>
	              </el-col>
              </el-row>
            </div>
            <table class="table table-bordered">
            	<thead>
            		<tr>
            			<th colspan="2">标题</th>
            			<th>分值</th>
            			<th>描述</th>
            			<th>打分依据</th>
            			<th>本人评分</th>
            		</tr>
            	</thead>
            	<tbody>
            		<tr v-for="item in tableData">
            			<td v-if="item.category" :rowspan="item.categoryRowspan">{{item.category}}</td>
            			<td :colspan="item.colspan">{{item.name}}</td>
            			<td>{{item.score}}</td>
            			<td>{{item.description}}</td>
            			<td><el-input v-model="item.according"></el-input></td>
            			<td><el-input v-model="item.inputScore"></el-input></td>
            		</tr>
            	</tbody>
            </table>
            <el-row>
	           <el-col :span="10">
	             <el-button type="primary" @click="submitRecord()" v-if=" ! recordData.recordId">提交新增</el-button>
	             <el-button type="primary" @click="updateRecord()" v-if="recordData.recordId">提交修改</el-button>
	           </el-col>
            </el-row>
        </el-card>

	</div>
</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		mylistUrl:"<s:url value='/evaluation/outcome/mylist'/>",
		addUrl:"<s:url value='/evaluation/outcome/addOutcome'/>",
		updateUrl:"<s:url value='/evaluation/outcome/updateOutcome'/>",
		deleteUrl:"<s:url value='/evaluation/outcome/deleteOutcome'/>",
		findAttachUrl:"<s:url value='/evaluation/outcome/findAttach'/>",
		downloadAttachUrl:"<s:url value='/evaluation/outcome/downloadAttach'/>",
		deleteAttachUrl:"<s:url value='/evaluation/outcome/deleteAttach'/>",
		findCategoryUrl:"<s:url value='/evaluation/outcomeValues/categories'/>",
		findLevel1Url:"<s:url value='/evaluation/outcomeValues/findLevel1'/>",
		findLevel2Url:"<s:url value='/evaluation/outcomeValues/findLevel2'/>",
		outcome:{occurDate:"",category:"",level1:"",level2:""},
		dialogFormVisible : false,
		formLabelWidth:"120px",
		conditionVo:{outcomeId : null},
		fileList:[],
		attaches:[],
		categories:[],
		level1List:[],
		level2List:[],
		itemTableDataUrl:"<s:url value='/evaluation/make/itemTableData'/>",
		recordDataUrl:"<s:url value='/evaluation/make/recordData'/>",
		submitRecordUrl:"<s:url value='/evaluation/make/submitRecord'/>",
		updateRecordUrl:"<s:url value='/evaluation/make/updateRecord'/>",
		tableData:[],
		subjectId : ${subject.subjectId},
		recordData : {}
	},
	filters: {    
 		 statusFilter: function (value) {
     		 if(value == 0){return "未审核"; }
     		 if(value == 1){return "已审核"; }
     		 return "";
 		 }	, 
 	},
	
	mounted : function() { 
		this.getTableData();
			
	}, 
	methods : { 
		  getTableData : function(){
			  this.$http.get(this.itemTableDataUrl, {params:{subjectId : this.subjectId}}).then(
    				function(response){
    					this.tableData =  response.data;
    					this.getRecordData();
    				},
    				function(response){}
    		  );
		  },
		  getRecordData : function(){
			  this.$http.get(this.recordDataUrl, {params:{subjectId : this.subjectId}}).then(
    				function(response){
    					this.recordData =  response.data;
    					if(this.recordData.recordId){
    						var tbData=this.tableData;
        					var itemData=this.recordData.itemData;
        					for(var i=0;i<tbData.length;i++){
        						var tbItem=tbData[i];
        						for(var k=0;k<itemData.length;k++){
        							var item=itemData[k];
        							if(tbItem.itemId == item.itemId){
        								tbItem.id=item.id;
        								tbItem.inputScore=item.inputScore;
        								tbItem.according=item.according;
        							}
        						}
        					}
    					}
    					
    				},
    				function(response){}
    		  );
		  },
		  submitRecord : function(){
			  var record={subjectId : this.subjectId, itemData : this.tableData};
			  this.$http.post(this.submitRecordUrl, record).then(
    				function(response){
    					this.getTableData();
    					this.$message({
    						type:"info",
    						message: "提交成功"
    					});
    				},
    				function(response){}
    		  );
		  },
		  updateRecord : function(){
			  var record={subjectId : this.subjectId,recordId : this.recordData.recordId, itemData : this.tableData};
			  this.$http.post(this.updateRecordUrl, record).then(
    				function(response){
    					this.getTableData();
    					this.$message({
    						type:"info",
    						message: "修改成功"
    					});
    				},
    				function(response){}
    		  );
		  },
	      mylist : function(){ 
					this.$http.get(this.mylistUrl).then(
					function(response){
					this.tableData=response.data;},
					function(response){}  			
					); 
		  },
		  showAdd : function(){
			  this.outcome={occurDate:"",category:"",level1:"",level2:""};
			  this.attaches=[];
			  this.dialogFormVisible=true;
		  },
		  addOutcome : function(){ 
			 var url=this.addUrl;
			 var msg="新增成功";
			 if(this.outcome.outcomeId){
				 url=this.updateUrl;
				 msg="修改成功";
			 }
			 this.$http.post(url, this.outcome).then(
				function(response){
					this.conditionVo.outcomeId =  response.data;
		            this.$refs.upload.submit();
		            this.clearFiles();
					this.outcome={occurDate:"",category:"",level1:"",level2:""};
					this.dialogFormVisible=false;
					this.$message({
						type:"info",
						message:msg
					});
					this.mylist();
				},
				function(response){}
			 ); 
	  	  },
	  	  handleRemove : function(file, fileList) {
            console.log(file, fileList);
          },
          clearFiles : function(){
              this.$refs.upload.clearFiles();
       	  },
       	  handleEdit : function(row){
       		  this.outcome=row;
       		  this.$http.get(this.findAttachUrl, {params:{outcomeId : this.outcome.outcomeId}}).then(
    				function(response){
    					this.attaches =  response.data;
    		            this.dialogFormVisible = true;
    				},
    				function(response){}
    		   );
       	  },
       	  handleDelete : function(row){
       		  this.$confirm("确认删除吗？","提示",{
	    			confirmButtonText:'确认',
	    			cancleButtonText:'取消',
	    			type:'warning'
    			}).then(function(){  
    				app.$http.delete(app.deleteUrl, {params:{outcomeId : row.outcomeId}}).then(
   	       				function(response){
   	       					this.$message({
   	    						type:"info",
   	    						message:"删除成功"
   	    					});
   	       					app.mylist();
   	       				},
   	       				function(response){}
   	        		 );
    			}).catch(function(){ app.$message({ type:'info',message:'已取消删除'})});
       	  },
       	  downloadAttach : function(attachId){
       		  window.location.href=this.downloadAttachUrl+"?attachId="+attachId;
       	  },
       	  deleteAttach : function(attach){
       		  this.$http.delete(this.deleteAttachUrl, {params:{attachId : attach.id}}).then(
    				function(response){
    					for(var i in this.attaches){
    						if(this.attaches[i] == attach){
    							this.attaches.splice(i,1);
    						}
    					}
    				},
    				function(response){}
    		   );
     	  },
     	  getCategories : function(){ 
				this.$http.get(this.findCategoryUrl).then(
					function(response){
						this.categories=response.data;
					},
					function(response){}
				); 
	      },
	      categoryChange : function(){
	    	  this.$http.get(this.findLevel1Url, {params:{category : this.outcome.category}}).then(
    				function(response){
    					this.level1List=response.data;
    				},
    				function(response){}
	    	  );
	      },
	      level1Change : function(){
	    	  this.$http.get(this.findLevel2Url, {params:{category : this.outcome.category , level1 : this.outcome.level1}}).then(
    				function(response){
    					this.level2List=response.data;
    				},
    				function(response){}
		      );
	      }
		 
     }	        
	 
	
});  

</script>
</body>
</html>
