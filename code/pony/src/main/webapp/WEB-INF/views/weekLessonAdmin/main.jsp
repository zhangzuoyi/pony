<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>星期上课设置</title>
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
              <b>星期上课设置</b>                                           
            </div> 
            <el-row>             
            <el-col :span="20" :offset="4">                   
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%">                            
                 <el-table-column
                        prop="seq"
                        label="序号"
                        width="120">
                </el-table-column>               
                <el-table-column
                        prop="name"
                        label="名称"
                        width="120">
                </el-table-column>
                <el-table-column
               			inline-template                  
                        label="是否上课"
                        width="120">
                        <div>
                          {{ row.haveClass | filter }}                                                      
                      </div>
                </el-table-column>
                <el-table-column 
                		inline-template                      
                        label="操作"
                        width="200"
                        >
                      <div>            
                        <el-button type="text" v-if="row.haveClass==0 " @click="weekdayUpdate(row.seq)">设为上课</el-button>
                        <el-button type="text" v-if="row.haveClass==1 " @click="weekdayUpdate(row.seq)">设为不上课</el-button>                       
                      </div>
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
		
		tableData: [],
        weekdayUrl : "<s:url value='/weekLessonAdmin/list'/>",
		weekdayUpdateUrl :"<s:url value='/weekLessonAdmin/update'/>"
		
	}, 
	mounted : function() { 
		this.getWeekday();	
	}, 
	methods : { 
		 getWeekday : function(){ 
			this.$http.get(this.weekdayUrl).then(
			function(response){this.tableData=response.data; },
			function(response){}  			
			); 
			} ,
		  weekdayUpdate : function(id){ 
			this.$http.get(this.weekdayUpdateUrl,{params:{id : id }}).then(
			function(response){this.getWeekday(); },
			function(response){}  	 			
			);
			}	        
	} ,
	filters: {    
    filter: function (value) {
      if(value == 1){return "是"; }
      if(value == 0){return "否"; }
    }
  }	
});  

</script>
</body>
</html>