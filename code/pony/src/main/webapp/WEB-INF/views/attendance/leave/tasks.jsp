<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>请假待办任务</title>
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

</head>
<body>
<div id="app">
  <div>   	           	
        <el-card class="box-card content-margin">
            <div slot="header" class="clearfix">
                <span>待办任务列表</span>
            </div>
            <el-table
                    :data="atts"
                    stripe
                    style="width: 100%">
                <el-table-column
                        prop="user.loginName"
                        label="请假人"
                        width="180">
                </el-table-column>
                <el-table-column
                        inline-template
                        label="申请日期"
                        width="180">
                        <div>{{row.applyDate | date}}</div>
                </el-table-column>
				<el-table-column
						prop="leaveType"
                        label="请假类型"
                        width="180">
                </el-table-column>
                <el-table-column
						inline-template
                        label="开始时间"
                        width="180">
                        <div>{{row.startTime | time}}</div>
                </el-table-column>
                <el-table-column
						inline-template
                        label="结束时间"
                        width="180">
                        <div>{{row.endTime | time}}</div>
                </el-table-column>
                <el-table-column
                        prop="taskName"
                        label="当前流程">
                </el-table-column>
                <el-table-column                       
                        label="操作"
                        >
                 <template scope="scope">
                 <el-button size="small" @click="claim(scope.row.taskId)" v-if="scope.row.taskAssignee == null">签收</el-button>
                 <el-button size="small" type="danger" @click="handleDelete(scope.$index,scope.row)" v-if="scope.row.taskAssignee != null">办理</el-button>
                 </template>                             
                </el-table-column>
            </el-table>
        </el-card>	
    </div>

</div>
<script type="text/javascript">
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		mylistUrl :"<s:url value='/attendance/leave/tasks'/>",
		claimUrl :"<s:url value='/attendance/leave/task/claim'/>",
		atts : []
	}, 
	mounted : function() { 
		this.mylist();
			
	}, 
	methods : { 	
		mylist:function(){ 	
			this.$http.get(this.mylistUrl).then(
					function(response){
						this.atts=response.body;
					 },
					function(response){}  			
			);  		  
       	},
       	claim:function(taskId){ 	
			this.$http.post(this.claimUrl+"/"+taskId).then(
					function(response){
						this.$alert("签收成功");
						this.mylist();
					 },
					function(response){}  			
			);  		  
       	}
    }
        
	 
	
});  

</script>
</body>
</html>