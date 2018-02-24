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
                        label="申请日期"
                        width="180">
					<template slot-scope="scope">{{scope.row.applyDate | date }}</template>
                </el-table-column>
				<el-table-column
                        label="请假类型"
                        width="180">
					<template slot-scope="scope">{{getLeaveType(scope.row.leaveType)}}</template>
                </el-table-column>
                <el-table-column
                        label="开始时间"
                        width="180">
					<template slot-scope="scope">{{scope.row.startTime | date }}</template>
                </el-table-column>
                <el-table-column
                        label="结束时间"
                        width="180">
					<template slot-scope="scope">{{scope.row.endTime | date }}</template>
                </el-table-column>
                <el-table-column
                        label="当前流程">
                    <template slot-scope="scope"><a class="trace" href='#' @click="showGraph(scope.row.processInstanceId)" :pid="scope.row.processInstanceId" :pdid="scope.row.processDefinitionId" title="点击查看流程图">{{scope.row.taskName }}</a><!-- onclick="graphTrace(this)" -->
                    </template>

                </el-table-column>
                <el-table-column                       
                        label="操作"
                        >
                 <template scope="scope">
                 <el-button size="small" @click="claim(scope.row.taskId)" v-if="scope.row.taskAssignee == null">签收</el-button>
                 <el-button size="small" type="danger" @click="showCompleteTask(scope.row)" v-if="scope.row.taskAssignee != null">办理</el-button>
                 </template>                             
                </el-table-column>
            </el-table>
        </el-card>	
    </div>
    
    <el-dialog  v-model="dialogFormVisible" >
		<div slot="title" class="dialog-title">
                   <b>{{task.taskName}}</b>
               </div>
		<el-form :model="task" ref="ruleForm">			
		 <el-form-item label="请假人" :label-width="formLabelWidth" > 
		 	{{task.user.loginName }}
		 </el-form-item>
		 <el-form-item label="请假类型" :label-width="formLabelWidth" > 
		 	{{getLeaveType(task.leaveType)}}
		 </el-form-item>
		 <el-form-item label="开始时间" :label-width="formLabelWidth" > 
           	{{task.startTime | date}}
		 </el-form-item> 
		 <el-form-item label="结束时间" :label-width="formLabelWidth" > 
		 	{{task.endTime | date}}
         </el-form-item>
         <el-form-item label="处理结果" :label-width="formLabelWidth" > 
		  	<el-select v-model="handleResult" placeholder="请选择">
                <el-option v-for="x in handleTypes" :lable="x" :value="x"></el-option>
                <!-- <el-option label="同意" value="同意"></el-option>
                <el-option label="驳回" value="驳回"></el-option> -->
            </el-select>
		 </el-form-item>
		 <el-form-item label="说明" :label-width="formLabelWidth" v-if="handleResult == '驳回'"> 
		 	<el-input type="textarea" v-model="task.backReason"></el-input>
         </el-form-item>
	    </el-form>
		<div slot="footer" class="dialog-footer">
			<el-button type="primary" @click="submitCompleteTask()"  >提交</el-button>
			<el-button @click="dialogFormVisible = false">取 消</el-button>
			
		</div>
	</el-dialog>
	
	<el-dialog  v-model="modifyApplyVisible" >
		<div slot="title" class="dialog-title">
            <b>{{task.taskName}}</b>
        </div>
		<el-form :model="task" ref="ruleForm">
		 <el-form-item label="驳回理由" :label-width="formLabelWidth" >
		 	<span v-html="backReason"></span>
		 </el-form-item>
		 <el-form-item label="调整类型" :label-width="formLabelWidth" >
		 	<el-radio-group v-model="reApply">
		 		<el-radio v-for="x in modifyTypes" :label="x.value">{{x.name}}</el-radio>
		 	</el-radio-group>
		 </el-form-item>
		 <el-form-item label="请假类型" :label-width="formLabelWidth" v-if="reApply">
		 	<el-select v-model="task.leaveType" placeholder="请选择请假类型">
                <el-option v-for="x in leaveTypes" :label="x.name" :value="x.value"></el-option>
            </el-select>
		 </el-form-item>
		 <el-form-item label="开始时间" :label-width="formLabelWidth" v-if="reApply"> 
           	<el-date-picker type="date" placeholder="开始日期" v-model="task.startTime"
                                        style="width: 100%;"></el-date-picker>
		 </el-form-item> 
		 <el-form-item label="结束时间" :label-width="formLabelWidth" v-if="reApply"> 
		 	<el-date-picker type="date" placeholder="结束日期" v-model="task.endTime"
                                        style="width: 100%;"></el-date-picker>
         </el-form-item>
         <el-form-item label="原因" :label-width="formLabelWidth" v-if="reApply"> 
		 	<el-input type="textarea" v-model="task.reason"></el-input>
         </el-form-item>
	    </el-form>
		<div slot="footer" class="dialog-footer">
			<el-button type="primary" @click="submitModifyTask()"  >提交</el-button>
			<el-button @click="modifyApplyVisible = false">取 消</el-button>
			
		</div>
	</el-dialog>
	
	<el-dialog  v-model="reportBackVisible" >
		<div slot="title" class="dialog-title">
            <b>{{task.taskName}}</b>
        </div>
		<el-form :model="task" ref="ruleForm">			
		 <el-form-item label="请假人" :label-width="formLabelWidth" > 
		 	{{task.user.loginName }}
		 </el-form-item>
		 <el-form-item label="请假类型" :label-width="formLabelWidth" > 
		 	{{getLeaveType(task.leaveType)}}
		 </el-form-item>
		 <el-form-item label="开始时间" :label-width="formLabelWidth" > 
           	{{task.startTime | date}}
		 </el-form-item> 
		 <el-form-item label="结束时间" :label-width="formLabelWidth" > 
		 	{{task.endTime | date}}
         </el-form-item>
         <el-form-item label="实际开始日期" :label-width="formLabelWidth" > 
		  	<el-date-picker type="date" placeholder="开始日期" v-model="actualStartTime"
                                        style="width: 100%;"></el-date-picker>
		 </el-form-item>
		 <el-form-item label="实际结束日期" :label-width="formLabelWidth" > 
		 	<el-date-picker type="date" placeholder="结束日期" v-model="actualEndTime"
                                        style="width: 100%;"></el-date-picker>
         </el-form-item>
	    </el-form>
		<div slot="footer" class="dialog-footer">
			<el-button type="primary" @click="submitReportBack()"  >提交</el-button>
			<el-button @click="reportBackVisible = false">取 消</el-button>
			
		</div>
	</el-dialog>

</div>
<script type="text/javascript" src="<s:url value='/static/activiti/workflow.js' />"></script>
<script type="text/javascript">
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		mylistUrl :"<s:url value='/attendance/leave/tasks'/>",
		claimUrl :"<s:url value='/attendance/leave/task/claim'/>",
		completeUrl :"<s:url value='/attendance/leave/task/complete'/>",
		varsUrl :"<s:url value='/attendance/leave/task/vars'/>",
		graphUrl :"<s:url value='/activiti/workspace-viewHistory'/>",
		atts : [],
		task : {user:{},handleResult:"",taskName:"",leaveType:"",startTime:"",endTime:"",backReason:""},
		dialogFormVisible : false,
		modifyApplyVisible : false,
		reportBackVisible : false,
		formLabelWidth:"120px",
		handleTypes : ['同意','驳回'],
		modifyTypes : [{name:"调整申请",value:true},{name:"取消申请",value:false}],
		handleResult : "",
		backReason : "",
		reApply : false,
		actualStartTime : "",
		actualEndTime : "",
		leaveTypes : [{name:'年假',value:'01'},{name:'事假',value:'02'},{name:'病假',value:'03'}]
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
       	},
       	showCompleteTask:function(task){
       		this.task=task;
       		if(this.task.taskName == "调整申请"){
       			this.$http.get(this.varsUrl+"/"+task.taskId).then(
   					function(response){
   						var data=response.body;
   						this.backReason="<b>领导：</b>" + (data.leaderBackReason || "") + "<br/><b>HR：</b>" + (data.hrBackReason || "");
   						this.reApply=false;
   						this.modifyApplyVisible=true;
   					 },
   					function(response){}
    			);
       		}else if(this.task.taskName == "销假"){
       			this.actualStartTime=null;
       			this.actualEndTime=null;
       			this.reportBackVisible=true;
       		}else{
       			this.handleResult="同意";
           		this.dialogFormVisible=true;
       		}
       	},
       	submitCompleteTask:function(){
       		var params=[];
       		var passKey="";
       		var reasonKey="";
   			if(this.task.taskName == "部门领导审批"){
   				passKey="deptLeaderPass";
   				reasonKey="leaderBackReason";
   			}else if(this.task.taskName == "人事审批"){
   				passKey="hrPass";
   				reasonKey="hrBackReason";
   			}
       		if(this.handleResult == "同意"){
       			params.push({key: passKey,value: true,type: 'B'});
       		}else{
       			params.push({key: passKey,value: false,type: 'B'});
       			params.push({key: reasonKey,value: this.task.backReason,type: 'S'});
       		}
       		this.$http.post(this.completeUrl+"/"+this.task.taskId,params).then(
					function(response){
						this.$alert("办理成功");
						this.dialogFormVisible=false;
						this.mylist();
					 },
					function(response){}  			
			);
       	},
       	submitModifyTask:function(){
       		var params=[];
       		params.push({key: "reApply",value: this.reApply,type: 'B'});
       		if(this.reApply){
       			var formatString = 'YYYY-MM-DD';
       		    var startTime=moment(this.task.startTime).format(formatString);
       		 	var endTime=moment(this.task.endTime).format(formatString);
       			params.push({key: "leaveType",value: this.task.leaveType,type: 'S'});
       			params.push({key: "startTime",value: startTime,type: 'D'});
       			params.push({key: "endTime",value: endTime,type: 'D'});
       			params.push({key: "reason",value: this.task.reason,type: 'S'});
       		}
       		this.$http.post(this.completeUrl+"/"+this.task.taskId,params).then(
					function(response){
						this.$alert("办理成功");
						this.modifyApplyVisible=false;
						this.mylist();
					 },
					function(response){}  			
			);
       		
       	},
       	submitReportBack:function(){
       		var params=[];
       		var formatString = 'YYYY-MM-DD';
   		    var startTime=moment(this.actualStartTime).format(formatString);
   		 	var endTime=moment(this.actualEndTime).format(formatString);
   			params.push({key: "actualStartTime",value: startTime,type: 'D'});
   			params.push({key: "actualEndTime",value: endTime,type: 'D'});
       		this.$http.post(this.completeUrl+"/"+this.task.taskId,params).then(
					function(response){
						this.$alert("办理成功");
						this.reportBackVisible=false;
						this.mylist();
					 },
					function(response){}  			
			);
       		
       	},
       	showGraph:function(instanceId){
       		window.open(this.graphUrl+"?processInstanceId="+instanceId);
       	},
       	getLeaveType:function(vl){
       		var len=this.leaveTypes.length;
       		for(var i=0;i<len;i++){
       			var le=this.leaveTypes[i];
       			if(le.value == vl){
       				return le.name;
       			}
       		}
       		return "";
       	}
    }
        
	 
	
});  

</script>
</body>
</html>