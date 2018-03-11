<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>个人资料</title>
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
              <b>个人资料</b>
              </el-col>
              </el-row>
            </div>
                <el-row>
                    <el-form  label-position="left" label-width="80px" :model="user">
                        <el-form-item label="登录名">
                            <el-input v-model="user.loginName" readonly="readonly"></el-input>
                        </el-form-item>
                        <el-form-item label="用户名">
                            <el-input v-model="user.userName" readonly="readonly"></el-input>
                        </el-form-item>
                        <el-form-item label="密码">
                            <el-button size="small"  @click="resetPsw(user.userId)">重置密码</el-button>
                        </el-form-item>
                    </el-form>
                </el-row>

        </el-card>
			 <el-dialog title="重置密码" :visible.sync="dialogFormVisible" >
			 <el-form :model="psw" >		 
			 <el-form-item label="初始密码" :label-width="formLabelWidth" >
			 <el-input v-model="psw.initPsw" type="password" auto-complete="off"   required></el-input>
			 </el-form-item>
			 <el-form-item label="密码" :label-width="formLabelWidth" >
			 <el-input v-model="psw.firstPsw" type="password" auto-complete="off"   required></el-input>
			 </el-form-item>
			 <el-form-item label="确认密码" :label-width="formLabelWidth" >
			 <el-input v-model="psw.secondPsw" type="password" auto-complete="off"   required></el-input>
			 </el-form-item>
		    </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="onSubmit()"  >确定</el-button>
				<el-button @click="dialogFormVisible = false">取 消</el-button>				
			</div>
			</el-dialog>

			

			


		</div>
 

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : {
        psw:{initPsw:null,firstPsw:null,secondPsw:null},
		user:{userId:null,loginName:null,userName:null,userType:null,roles:[]},
        dialogFormVisible:false,
		formLabelWidth:"120px",
		userUrl:"<s:url value='/personalInfo/listOne'/>",
        resetPswUrl : "<s:url value='/user/resetPsw'/>",
        userId : null
	
	
		
	}, 
	filters: {    
    userTypeFilter: function (value) {
      if(value == 't'){return "老师"; }
      if(value == 's'){return "学生"; }
    }
  }	,
	
	mounted : function() { 
		this.getPersonalInfo();

	}, 
	methods : {
        getPersonalInfo : function(){
			this.$http.get(this.userUrl).then(
			function(response){
			this.user=response.data;
			},
			function(response){}  			
			); 
			} ,
        resetPsw :  function(userId){
            if(userId == null || userId == ""){
                this.$message({type:"info",  message:"请选择用户"});
                return ;
            }
            this.dialogFormVisible = true;
            this.userId = userId;
        },
        onSubmit :function(){
        	if(this.psw.initPsw == null || this.psw.init == "" ){
                this.$message({type:"info",  message:"请输入原始密码"});
                return ;
            }
            if(this.psw.firstPsw == null || this.psw.secondPsw == null || this.psw.firstPsw == "" || this.psw.secondPsw == ""){
                this.$message({type:"info",  message:"请输入密码"});
                return ;
            }
            if(this.psw.firstPsw != this.psw.secondPsw){
                this.$message({type:"info",  message:"两次密码输入不一致"});
                return ;
            }
            app.$http.post(app.resetPswUrl,{userId:this.userId,initPsw:this.psw.initPsw,psw:this.psw.firstPsw},{emulateJSON:true}).then(
                function(response){
                	if(response.data.result == "fail"){
	                    this.$message({type:"info",  message:"重置失败,原始密码不正确!"});
                	}
					if(response.data.result == "success"){
	                    this.$message({type:"info",  message:"重置成功"});
                	}               	
                    this.dialogFormVisible = false;
                    this.psw = {initPsw:null,firstPsw:null,secondPsw:null};
                },
                function(response){}
            );


        },



		
			
			 
			   	
	  
        }	        
	 
	
});  

</script>
</body>
</html>
