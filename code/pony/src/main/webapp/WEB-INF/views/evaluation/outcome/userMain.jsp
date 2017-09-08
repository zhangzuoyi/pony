<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成果管理</title>
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
              <el-row>
               <el-col :span="16">
               	<el-button type="primary" @click="showAdd">新增</el-button>
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
	                 <template scope="scope" v-if="row.status == 0">
	                 <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
	                 <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>               
	                 </template>
                </el-table-column>
            </el-table> 
        </el-card>
        
		<el-dialog  v-model="dialogFormVisible" >
			<div slot="title" class="dialog-title">
                  <b v-if=" ! outcome.outcomeId">新增成果</b>
                  <b v-if="outcome.outcomeId">修改成果</b>
            </div>
			<el-form :model="outcome" >			 
			  <el-form-item label="分类" prop="category" :label-width="formLabelWidth"> 
			 	<el-input v-model="outcome.category" ></el-input>		 			
			  </el-form-item>
			  <el-form-item label="类别" prop="level1" :label-width="formLabelWidth"> 
			 	<el-input v-model="outcome.level1" ></el-input>		 			
			  </el-form-item>
			  <el-form-item label="级别" prop="level2" :label-width="formLabelWidth"> 
			 	<el-input v-model="outcome.level2" ></el-input>		 			
			  </el-form-item>
			  <el-form-item label="发生日期" prop="occurDate" :label-width="formLabelWidth"> 
			 	<el-date-picker
                    v-model="outcome.occurDate"
                    type="date"
                    placeholder="选择日期">
            	</el-date-picker>
			  </el-form-item>
			  <el-form-item label="说明" prop="description" :label-width="formLabelWidth"> 
			 	<el-input type="textarea" :rows="5" v-model="outcome.description" ></el-input>
			  </el-form-item>
			  <el-form-item label="附件" :label-width="formLabelWidth">
			  	<div v-for="attach in attaches">
			  		<a href="javascript:void(0)" @click="downloadAttach(attach.id)">{{attach.oldFileName}}</a>
			  		<el-button size="small" @click="deleteAttach(attach)">删除</el-button>
			  	</div>
			 	<el-upload					
	  			 ref="upload" 	
	  			 name="fileUpload"
	  			 action="<s:url value='/evaluation/outcome/fileUpload'/>"
	  			:on-remove="handleRemove"
	  			:file-list="fileList"
	  			:auto-upload="false"
	  			:data="conditionVo"
	  			multiple>
	  			<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
	  			<el-button style="margin-left:10px;" size="small" type="primary" @click="clearFiles">清空文件</el-button>
				</el-upload>
			  </el-form-item>
		    </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="addOutcome()">确定</el-button>
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
		mylistUrl:"<s:url value='/evaluation/outcome/mylist'/>",
		addUrl:"<s:url value='/evaluation/outcome/addOutcome'/>",
		updateUrl:"<s:url value='/evaluation/outcome/updateOutcome'/>",
		findAttachUrl:"<s:url value='/evaluation/outcome/findAttach'/>",
		downloadAttachUrl:"<s:url value='/evaluation/outcome/downloadAttach'/>",
		deleteAttachUrl:"<s:url value='/evaluation/outcome/deleteAttach'/>",
		outcome:{occurDate:""},
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
		this.mylist();
		
			
	}, 
	methods : { 
	      mylist : function(){ 
					this.$http.get(this.mylistUrl).then(
					function(response){
					this.tableData=response.data;},
					function(response){}  			
					); 
		  },
		  showAdd : function(){
			  this.outcome={occurDate:""};
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
					this.outcome={occurDate:""};
					this.dialogFormVisible=false;
					this.$message({
						type:"info",
						message:msg
					});
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
     	  }
		 
     }	        
	 
	
});  

</script>
</body>
</html>
