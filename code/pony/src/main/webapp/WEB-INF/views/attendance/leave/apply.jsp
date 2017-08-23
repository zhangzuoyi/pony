<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>请假申请</title>
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
                <span>请假申请单</span>
            </div>
            <el-form ref="form" :model="apply" label-width="80px">
				<el-form-item label="基本信息">
                    <el-col :span="3">姓名：{{person.name}}</el-col>
					<!-- <el-col :span="3">部门：{{person.dpt}}</el-col>
					<el-col :span="3">岗位：{{person.title}}</el-col> -->
					<!-- <el-col :span="2">申请日期</el-col>
					<el-col :span="5">
						<el-date-picker type="date" placeholder="选择日期" v-model="apply.applyDate"
											style="width: 100%;"></el-date-picker>
					</el-col> -->
                </el-form-item>
                <el-form-item label="请假类型">
                    <el-select v-model="apply.leaveType" placeholder="请选择请假类型">
                        <el-option v-for="x in leaveTypes" :label="x.name" :value="x.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="时间">
					<!-- <el-col :span="5">
						<el-select v-model="apply.timeType" placeholder="请选择时间类型">
							<el-option label="整天" value="0"></el-option>
							<el-option label="非整天" value="1"></el-option>
						</el-select>
					</el-col> -->
					<el-col :span="5">
                        <el-date-picker type="date" placeholder="开始日期" v-model="apply.startTime"
                                        style="width: 100%;"></el-date-picker>
                    </el-col>
                    <el-col class="line" :span="1">-</el-col>
                    <el-col :span="5">
                        <el-date-picker type="date" placeholder="结束日期" v-model="apply.endTime"
                                        style="width: 100%;"></el-date-picker>
                    </el-col>
                </el-form-item>
				<!-- <el-form-item label="请假时长">
                    <el-input-number v-model="apply.timeLength" :min="1" :max="10"></el-input-number>
                </el-form-item> -->
                <el-form-item label="原因">
                    <el-input type="textarea" v-model="apply.reason"></el-input>
                </el-form-item>
                <!-- <el-form-item label="附件">
                    <el-upload
						action="//jsonplaceholder.typicode.com/posts/"
						:on-preview="handlePreview"
						:on-remove="handleRemove">
						<el-button size="small" type="primary">点击上传</el-button>
						<div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
					</el-upload>
                </el-form-item> -->
                <el-form-item>
                    <el-button type="primary" @click="submitApply">提交</el-button>
                    <el-button>取消</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>

</div>
<script type="text/javascript">
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		person : {name:"${personName}"},
		applyUrl :"<s:url value='/attendance/leave/apply'/>",
		apply : {
			applyDate : "",
			leaveType : "",
			timeType : "",
			startTime : "",
			endTime : "",
			timeLength : "",
			reason : ""
		},
		leaveTypes : [{name:'年假',value:'01'},{name:'事假',value:'02'},{name:'病假',value:'03'}]
	}, 
	mounted : function() { 
			
	}, 
	methods : { 	
		submitApply:function(){
			this.$http.post(this.applyUrl,this.apply).then(
					function(response){
						alert("申请成功");
						this.apply={};
					 },
					function(response){}
			);  		  
       	}
      }
        
	 
	
});  

</script>
</body>
</html>