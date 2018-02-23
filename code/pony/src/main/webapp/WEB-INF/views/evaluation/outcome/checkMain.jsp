<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成果审核</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<%-- <link rel="stylesheet" type="text/css" href="<s:url value='/static/bootstrap/css/bootstrap.min.css' />" /> --%>
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
	              <b>成果列表</b>
	              </el-col>
              </el-row>
            </div>
            <el-table
                    :data="tableData"
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
                        label="分类">
                 <div>{{row.category }}</div>       
                </el-table-column>
                <el-table-column
                		prop="level1"
                        label="级别"
                        >
                </el-table-column>
                <el-table-column
                        prop="level2"
                        label="等级"
                        >
                </el-table-column>
                <el-table-column
                        prop="score"
                        label="得分"
                        >
                </el-table-column>
                <el-table-column
                        inline-template
                        label="日期">
                        <div>{{row.occurDate | date}}</div>
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
	                 </template>
                </el-table-column>
            </el-table> 
        </el-card>
        
		<el-dialog  :visible.sync="dialogFormVisible" >
			<div slot="title" class="dialog-title">
                  <b>成果审核</b>
            </div>
			<el-form :model="outcome" >			 
			  <el-form-item label="老师姓名" prop="teacherName" :label-width="formLabelWidth"> 
			 	<div>{{outcome.teacherName}}</div>
			  </el-form-item>
			  <el-form-item label="分类" prop="category" :label-width="formLabelWidth"> 
			 	<div>{{outcome.category}}</div>
			  </el-form-item>
			  <el-form-item label="类别" prop="level1" :label-width="formLabelWidth"> 
			 	<div>{{outcome.level1}}</div>
			  </el-form-item>
			  <el-form-item label="级别" prop="level2" :label-width="formLabelWidth"> 
			 	<div>{{outcome.level2}}</div>
			  </el-form-item>
			  <el-form-item label="发生日期" prop="occurDate" :label-width="formLabelWidth"> 
			 	<div>{{outcome.occurDate | date}}</div>
			  </el-form-item>
			  <el-form-item label="说明" prop="description" :label-width="formLabelWidth"> 
			 	<div>{{outcome.description}}</div>
			  </el-form-item>
			  <el-form-item label="附件" :label-width="formLabelWidth">
			  	<div v-for="attach in attaches">
			  		<a href="javascript:void(0)" @click="downloadAttach(attach.id)">{{attach.oldFileName}}</a>
			  	</div>
			  </el-form-item>
		    </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitCheck()">确定</el-button>
				<el-button @click="dialogFormVisible = false">取 消</el-button>
			</div>
		</el-dialog>

	</div>
</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		tableData:[],
		findPageUrl:"<s:url value='/evaluation/outcome/findPage'/>",
		findAttachUrl:"<s:url value='/evaluation/outcome/findAttach'/>",
		downloadAttachUrl:"<s:url value='/evaluation/outcome/downloadAttach'/>",
		checkUrl:"<s:url value='/evaluation/outcome/check'/>",
		outcome:{occurDate:"",category:"",level1:"",level2:""},
		dialogFormVisible : false,
		formLabelWidth:"120px",
		conditionVo:{outcomeId : null},
		fileList:[],
		attaches:[]
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
			
	}, 
	methods : { 
	      list : function(){ 
					this.$http.get(this.findPageUrl).then(
					function(response){
					this.tableData=response.data;},
					function(response){}  			
					); 
		  },
		  showCheck : function(row){
			  this.outcome=row;
			  this.$http.get(this.findAttachUrl, {params:{outcomeId : this.outcome.outcomeId}}).then(
    				function(response){
    					this.attaches =  response.data;
    		            this.dialogFormVisible = true;
    				},
    				function(response){}
    		   );
		  },
       	  downloadAttach : function(attachId){
       		  window.location.href=this.downloadAttachUrl+"?attachId="+attachId;
       	  },
       	  submitCheck : function(){
       		  this.$http.post(this.checkUrl,{outcomeId : this.outcome.outcomeId},{emulateJSON:true}).then(
					function(response){
						this.$message({
							type:"info",
							message:"审核成功"
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
