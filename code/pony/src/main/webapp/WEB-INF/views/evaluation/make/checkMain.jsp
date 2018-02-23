<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>评价审核</title>
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
	              <b>${subject.name }列表</b>
	              </el-col>
              </el-row>
            </div>
            <el-table
                    :data="records"
                    border
                    style="width: 100%"
                    highlight-current-row> 
                <el-table-column
                         inline-template
                        label="老师姓名">
                 <div>{{row.teacherName }}</div>       
                </el-table-column>
                <el-table-column
                         inline-template
                        label="提交时间">
                 <div>{{row.createTime | date }}</div>       
                </el-table-column>
                <el-table-column
                         inline-template
                        label="审核时间">
                 <div v-if="row.checkTime">{{row.checkTime | date }}</div>
                </el-table-column>
                <el-table-column
                        prop="totalScore"
                        label="总分">
                </el-table-column>
                <el-table-column
                         inline-template
                        label="状态"
                        >
                 <div>{{row.status | statusFilter}}</div>
                </el-table-column>
                <el-table-column                       
                        label="操作">
	                 <template scope="scope" >
	                 <el-button v-if="scope.row.status == 0" size="small" @click="showCheck(scope.row)">审核</el-button>
	                 <el-button v-if="scope.row.status == 1" size="small" @click="showCheck(scope.row)">查看</el-button>
	                 </template>
                </el-table-column>
            </el-table> 
        </el-card>
        
		<el-dialog  :visible.sync="dialogFormVisible" size="large">
			<div slot="title" class="dialog-title">
                  <b v-if="recordData.status == 0">${subject.name }审核</b>
                  <b v-if="recordData.status == 1">${subject.name }查看</b>
            </div>
			<table class="table table-bordered">
            	<thead>
            		<tr>
            			<th colspan="2">标题</th>
            			<th>分值</th>
            			<th>描述</th>
            			<th>打分依据</th>
            			<th>本人评分</th>
            			<th>审核评分</th>
            		</tr>
            	</thead>
            	<tbody>
            		<tr v-for="item in tableData">
            			<td v-if="item.category" :rowspan="item.categoryRowspan">{{item.category}}</td>
            			<td :colspan="item.colspan">{{item.name}}</td>
            			<td>{{item.score}}</td>
            			<td>{{item.description}}</td>
            			<td>{{item.according}}</td>
            			<td>{{item.inputScore}}</td>
            			<td v-if="recordData.status == 0"><el-input v-model="item.checkScore" style="width:50px;"></el-input></td>
            			<td v-if="recordData.status == 1">{{item.checkScore}}</el-input></td>
            		</tr>
            	</tbody>
            </table>
			<div slot="footer" class="dialog-footer">
				<el-button v-if="recordData.status == 0" type="primary" @click="submitCheck()">确定</el-button>
				<el-button v-if="recordData.status == 0" @click="dialogFormVisible = false">取消</el-button>
				<el-button v-if="recordData.status == 1" @click="dialogFormVisible = false">关闭</el-button>
			</div>
		</el-dialog>

	</div>
</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		findPageUrl:"<s:url value='/evaluation/outcome/findPage'/>",
		findAttachUrl:"<s:url value='/evaluation/outcome/findAttach'/>",
		downloadAttachUrl:"<s:url value='/evaluation/outcome/downloadAttach'/>",
		checkUrl:"<s:url value='/evaluation/outcome/check'/>",
		outcome:{occurDate:"",category:"",level1:"",level2:""},
		formLabelWidth:"120px",
		conditionVo:{outcomeId : null},
		fileList:[],
		attaches:[],
		subjectId : ${subject.subjectId},
		findRecordsUrl:"<s:url value='/evaluation/make/records'/>",
		itemTableDataUrl:"<s:url value='/evaluation/make/itemTableData'/>",
		getRecordUrl:"<s:url value='/evaluation/make/getRecord'/>",
		checkRecordUrl:"<s:url value='/evaluation/make/checkRecord'/>",
		records:[],
		tableData:[],
		recordData : {},
		dialogFormVisible : false
	},
	filters: {    
 		 statusFilter: function (value) {
     		 if(value == 0){return "未审核"; }
     		 if(value == 1){return "已审核"; }
     		 return "";
 		 }	, 
 		},
	
	mounted : function() { 
		this.list();
		this.getTableData();
			
	}, 
	methods : { 
	      list : function(){ 
				this.$http.get(this.findRecordsUrl,{params:{subjectId : this.subjectId}}).then(
				function(response){
					this.records=response.data;},
				function(response){}  			
				);
		  },
		  getTableData : function(){
			  this.$http.get(this.itemTableDataUrl, {params:{subjectId : this.subjectId}}).then(
    				function(response){
    					this.tableData =  response.data;
    				},
    				function(response){}
    		  );
		  },
		  showCheck : function(row){
			  this.recordData=row;
			  this.$http.get(this.getRecordUrl, {params:{recordId : this.recordData.recordId}}).then(
    				function(response){
    					this.recordData =  response.data;
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
    								tbItem.checkScore=item.checkScore;
    							}
    						}
    					}
    					this.dialogFormVisible=true;
    				},
    				function(response){}
    		  );
		  },
       	  submitCheck : function(){
       		  for(var i in this.tableData){
       			  if(this.tableData[i].checkScore == null || this.tableData[i].checkScore == ''){
       				  this.$alert("审核评分不能为空");
       				  return;
       			  }
       		  }
	       	  var record={subjectId : this.subjectId,recordId : this.recordData.recordId, itemData : this.tableData};
			  this.$http.post(this.checkRecordUrl, record).then(
	  				function(response){
	  					this.$message({
	  						type:"info",
	  						message: "审核成功"
	  					});
	  					this.dialogFormVisible = false;
						this.list();
	  				},
	  				function(response){}
	  		  );
       	  }
		 
     }	        
	 
	
});  

</script>
</body>
</html>
