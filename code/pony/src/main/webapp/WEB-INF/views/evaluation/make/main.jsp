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
            			<td><el-input type="textarea" v-model="item.according" :disabled="item.dataSource != null"></el-input></td>
            			<td><el-input v-model="item.inputScore" :disabled="item.dataSource != null"></el-input></td>
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
		itemTableDataUrl:"<s:url value='/evaluation/make/itemTableData'/>",
		recordDataUrl:"<s:url value='/evaluation/make/recordData'/>",
		submitRecordUrl:"<s:url value='/evaluation/make/submitRecord'/>",
		updateRecordUrl:"<s:url value='/evaluation/make/updateRecord'/>",
		tableData:[],
		subjectId : ${subject.subjectId},
		recordData : {}
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
		  }
		 
     }	        
	 
	
});  

</script>
</body>
</html>
