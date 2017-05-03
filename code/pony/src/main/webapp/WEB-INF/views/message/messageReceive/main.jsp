<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>消息接收</title>
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

</head>
<body>
<div id="app">
  <div>   	           	
        	<el-card class="box-card content-margin">
            <div slot="header" class="clearfix">
              <el-row>
              <el-col :span="4">
              <b>消息接收</b>
              </el-col>
              </el-row>     
            <el-row>
            <el-col :span="2" :offset="16"> 
              <el-button type="primary" @click="read">标识已读</el-button>           
           </el-col>
            <el-col :span="2" :offset="18">
             <el-button type="primary" @click="delete">删除</el-button>  
            </el-col>                               
           </el-row>                 
            </div> 
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    @selection-change="handleSelectionChange">
                <el-table-column
                        type="selection"
                        width="50">
                </el-table-column>
                <!-- <el-table-column
                        inline-template
                        label="老师编号"
                        width="120">
                    <div>{{ row.date }}</div>
                </el-table-column> -->
                 <el-table-column
                        prop="title"
                        label="标题"
                        >
                </el-table-column>
                <el-table-column
                        prop="sendTime"
                        label="发送时间"
                       >
                </el-table-column>
                <el-table-column
                        prop="sendUser"
                        label="发送人"                       
                        >
                </el-table-column>                               
                <el-table-column
                        prop="attach"
                        label="附件"
                        show-overflow-tooltip>
                </el-table-column>
            </el-table>
            
            
            
            
              

        </el-card>
        
        
        <el-dialog title="消息" v-model="dialogFormVisible" size="large">
               
                
                <div slot="footer" class="dialog-footer">                   
                    <el-button type="primary" @click.native="submit()">确 定</el-button>
                    <el-button @click.native="dialogFormVisible = false">取 消</el-button>
                </div>
            </el-dialog>
          
               	
    </div>
 

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 		
		
		
		message:{userGroup:[],users:[],title:null,content:null},	
		userGroups:[],
		searchString:null,
		unselectUser:[],
		selectUser:[],
		dialogFormVisible:false,
		formLabelWidth : '120px',
		userGroupUrl: "<s:url value='/userGroup/list'/>",
		listByGroupIdUrl:"<s:url value='/userGroup/listByGroupId'/>",
		sendUrl:"<s:url value='/message/messageSend/send'/>",
		currentUser:null,
		fileList:[],
		selectUserGroup:null,
		groupId:null,
		conditionVo:{messageId:null}  
	
		
	}, 
	mounted : function() { 
		this.getUserGroups();
			
	}, 
	methods : { 
			 getUserGroups : function(){ 
			this.$http.get(this.userGroupUrl).then(
			function(response){
			this.userGroups=response.data;},
			function(response){}  			
			); 
			} ,
			listByGroupId :function(groupId){ 
			this.$http.get(this.listByGroupIdUrl,{params:{groupId: groupId}}).then(
			function(response){this.unselectUser=response.data;},
			function(response){}  			
			); 
			}, 
			 handleChange :function(value){
			 this.groupId = value;
			 this.listByGroupId(value);
			 
			 },
			 handleUserClick:function(){
			 this.dialogFormVisible=true;
			 this.unselectUser = [];
			 this.selectUser = [];
			 this.groupId=null;
			 	 
			 },
			 
			 createFilter :function(queryString) {
                return function users(user) {
                    return (user.userName.indexOf(queryString.toLowerCase()) >= 0);
                };
            } ,
			 
			  handleIconClick:function(){
			//
			var unselectUser = this.unselectUser;
            var results = this.searchString ? unselectUser.filter(this.createFilter(this.searchString)) : searchString;              		
			this.unselectUser = results;
			
			 	 
			 },
			 handleCurrentChange:function(currentRow){
			 
			 this.currentUser = currentRow;
			 
			 },
			 addOne:function(){
			 	if (!this.currentUser) {
					alert('请选择！');
					return;
				} else {
					for (var i = 0; i < this.unselectUser.length; i++) {
						var oneRow = this.unselectUser[i];
						if (oneRow.userId == this.currentUser.userId) {
							this.unselectUser.splice(i, 1);
							this.selectUser.push(oneRow);
							this.currentUser = null;
							break;
						}
					}
				}	
			 
			 
			 },
			 removeOne:function(){
			 	if (!this.currentUser) {
					alert('请选择！');
					return;
				} else {
					var unselectUserIds = [];
			  		 for(var index in this.unselectUser){
			   			unselectUserIds.push(this.unselectUser[index].userId);
			  			 }
				
					for (var i = 0; i < this.selectUser.length; i++) {
						var oneRow = this.selectUser[i];
						if (oneRow.userId == this.currentUser.userId ) {
							this.selectUser.splice(i, 1);
							if(oneRow.groupId == this.groupId && unselectUserIds.indexOf(oneRow.userId)< 0 ){
							 this.unselectUser.push(oneRow);
							}														
							this.currentUser = null;
							break;
						}
					}
				}	
			 },
			 addAll:function(){
			 
			   var selectUserIds = [];
			   for(var index in this.selectUser){
			   selectUserIds.push(this.selectUser[index].userId);
			   }
			 	for (var i = 0; i < this.unselectUser.length; i++) {
						var oneRow = this.unselectUser[i];
						if(selectUserIds.indexOf(oneRow.userId) < 0){
							this.selectUser.push(oneRow);
						}
					}
				this.unselectUser = [];
			 	
			 	
			 },
			 removeAll:function(){
				var unselectUserIds = [];
			   	for(var index in this.unselectUser){
			   		unselectUserIds.push(this.unselectUser[index].userId);
			   }
			 
				 for (var i = 0; i < this.selectUser.length; i++) {
						var oneRow = this.selectUser[i];
						if(oneRow.groupId == this.groupId && unselectUserIds.indexOf(oneRow.userId)< 0){
							this.unselectUser.push(oneRow);
						}
					}
			 
			 	 this.selectUser = [];
			 
			 
			 },
			 submit : function(){
			 	this.dialogFormVisible=false;
			 	for(var index in  this.selectUser){
			 	this.users += this.selectUser[index].userName+";";			 	 
			 	}
			 	
			 },
			 handleRemove : function(file, fileList) {
                console.log(file, fileList);
             },
             send : function(){
             if(this.message.userGroup==null && this.message.users ==null){
             alert("用户组和用户不能同时为空.."); 
             return;            
             }
             if(this.message.title==null ){
             alert("标题不能为空.."); 
             return;            
             }
             if(this.message.content==null ){
             alert("内容不能为空.."); 
             return;            
             }
             //消息发送
              
			this.$http.post(this.sendUrl,this.message).then(
			function(response){
			 this.message={userGroup:[],users:[],title:null,content:null};
			 this.conditionVo.messageId =  response.data;			
             this.$refs.upload.submit();			
			},
			function(response){}  			
			); 
			
             
             
             
             
             
             
             
             },
             save : function(){
             
             },
             close : function(){
             
             },
            clearFiles : function(){
                   this.$refs.upload.clearFiles();
            }
			
			 
			   	
	  
        }	        
	 
	
});  

</script>
</body>
</html>
