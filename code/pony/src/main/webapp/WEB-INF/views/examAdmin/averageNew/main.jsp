<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>均量值</title>
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
<script type="text/javascript" src="<s:url value='/static/vue/vue-validator.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/elementUI/index.js' />"></script>
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
              <b>均量值</b>
              </el-col>
              </el-row>
              <el-row>                            
               <el-col :span="1" >
                    <b>学年:</b>                                    
            	</el-col> 
               <el-col  :span="2">
               		{{schoolYear.startYear}}——{{schoolYear.endYear}}
               </el-col>
               <el-col :span="1" >
                    <b>学期:</b>
            	</el-col> 
               <el-col  :span="1">
               		{{term.name}}
               </el-col>            	          		           	
            	<el-col :span="6" >
               		<el-button type="primary"  @click="downloadTemplate">下载模板</el-button>
               		<el-button type="primary"  @click="importTemplate" :disabled="exportFlag" >导出</el-button>
               		
              	</el-col>                           
              </el-row>
            </div>           
        </el-card>
		
		<el-dialog title="导入模板"  v-model="uploadDialogFormVisible" >
			<el-row>
			<el-col>
				<el-upload					
	  			 ref="upload" 	
	  			 name="file"
	  			 :before-upload="beforeUpload"
	  			 action="<s:url value='/examAdmin/examinee/fileUpload'/>"
	  			:file-list="fileList"
	  			:auto-upload="false"
	  			:data="conditionVo">
	  			<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
				</el-upload>
			</el-col>
			</el-row>
			<el-row>
			<el-button type="primary" size="small" @click="upload">提交</el-button>
			</el-row>
		</el-dialog>
		
		</div>
</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		schoolYearUrl:"<s:url value='/schoolYear/getCurrent'/>",
		termUrl:"<s:url value='/term/getCurrent'/>",
		exportResultUrl :"<s:url value='/examAdmin/average/exportNewAverage'/>",
		schoolYear :null,
		term : null,	
		uploadDialogFormVisible : false,	
		fileList : [],
        conditionVo : {},
        exportFlag : false
		
	}, 
	mounted : function() { 
		this.getCurrentSchoolYear();
		this.getCurrentTerm();		
	},	
	methods : {		
			getCurrentSchoolYear	:function(){ 			
				this.$http.get(this.schoolYearUrl).then(
					function(response){
					this.schoolYear=response.data;
					 },
					function(response){}
				);
			},
			getCurrentTerm	:function(){ 			
				this.$http.get(this.termUrl).then(
					function(response){
					this.term=response.data; },
					function(response){}
				);
			},			
			downloadTemplate : function(){
          	  window.location.href="<s:url value='/examAdmin/average/exportNewTemplate' />";
            },  
            beforeUpload : function(file){
            	
                var formData = new FormData();
                formData.append('file',file);
                this.$http.post(this.exportResultUrl,formData).then(
                        function(response){ 
             				this.exportFlag = false;
                            this.$message({type: "info", message: "生成成功"});
                            if(response.data.error != undefined && response.data.error != null ){
                                this.$message({type: "info", message: response.data.error});
                            }
                            if (response.data.name != undefined && response.data.name != null) {
                                var  exportParams = {name :response.data.name};
                                window.location.href = "<s:url value='/examAdmin/average/exportNewAverageFile?'/>"+jQuery.param(exportParams);
                            }
                            });
                return false;           
          },
            upload  : function(){
                this.exportFlag = true;
                if(this.$refs.upload.uploadFiles.length <= 0){
            	  this.$message({
						type:"info",
						message: "文件不能为空"
				  });
            	  this.exportFlag = false;
                    return ;
              }        	
          	  this.$refs.upload.submit();
          	  this.$refs.upload.clearFiles();
          	  this.uploadDialogFormVisible = false;       	  
            },
            importTemplate : function(){          	
            	this.uploadDialogFormVisible = true;
            }
            
            
   			
            
        }	        
});  
</script>
</body>
</html>
