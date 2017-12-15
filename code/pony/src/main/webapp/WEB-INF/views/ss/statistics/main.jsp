<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选课统计</title>
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
        <el-card class="box-card content-margin" >
            <div slot="header" class="clearfix">
              <el-row>
	              <el-col :span="4">
	              <b>选课统计</b>
	              </el-col>
              </el-row>  
              <el-row>             
               <el-col :span="4">
               	<span>学年</span>
               </el-col>
               <el-col :span="6">
               	<span>{{config.schoolYear.name}}</span>
               </el-col>
              </el-row>
              <el-row>             
               <el-col :span="4">
               	<span>已选总人数</span>
               </el-col>
               <el-col :span="6">
               	<span>{{totalSelect}}</span>
               </el-col>             
               <el-col  :span="4" :offset="4">
                <el-button type="primary" @click="exportStatistics">导出</el-button> 
                </el-col>
              </el-row>                      
            </div>
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    highlight-current-row>
                <el-table-column
                        prop="group"
                        label="组合形式"
                        >
                </el-table-column>
                <el-table-column
                        prop="count"
                        label="人数"
                        >
                </el-table-column>
                <el-table-column
                        prop="students"
                        label="学生名单"
                        show-overflow-tooltip="true"
                        >
                </el-table-column>
                                            
            </el-table>
        </el-card>
        
        


  </div>
</div>

<script type="text/javascript">
	var app = new Vue({ 
	el : '#app' ,
	data : { 		
		config : {},
		totalSelect : 0,
		tableData : [],
		currentConfigUrl:"<s:url value='/ss/config/current'/>",
		totalSelectUrl:"<s:url value='/ss/statistics/totalSelect'/>",
		groupUrl:"<s:url value='/ss/statistics/group'/>",


		
	}, 
	
	mounted : function() { 
		this.getCurrentConfig();
		this.getTotalSelect();
		this.getGroup();
	}, 
	methods : { 
		getCurrentConfig : function(){
			this.$http.get(this.currentConfigUrl).then(
			function(response){
				this.config=response.data;
			},
			function(response){}  			
			); 
		},
		getTotalSelect : function(){
			this.$http.get(this.totalSelectUrl).then(
			function(response){
				this.totalSelect=response.data;
			},
			function(response){}  			
			); 
		},
		getGroup : function(){
			this.$http.get(this.groupUrl).then(
					function(response){
						this.tableData=response.data;
					},
					function(response){}  			
					); 
				},
		exportStatistics : function(){
        	  window.location.href="<s:url value='/ss/statistics/exportStatistics' />";
		}
		
		
      }
	 
	
});  
	
</script>
</body>
</html>