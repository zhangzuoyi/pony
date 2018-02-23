<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>消息发送</title>
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
              <b>消息发送</b>
              </el-col>
              </el-row>
              <el-row>
              
              <el-col :span="2" :offset="4">
                    <b>收件人:</b>                                    
            </el-col> 
            </el-row>
            <el-row>
            <el-col :span="1" :offset="5">
                    <b>用户组:</b>                                    
            </el-col> 
            <el-col :span="6" >
            <div class="grid-content bg-purple">                                     
					<el-select multiple v-model="message.userGroup"    filterable placeholder="请选择.." >
               		 <el-option              		 
                        v-for="userGroup in userGroups" 
                        :label="userGroup.name"                      
                        :value="userGroup.groupId">
                        <span style="float: left">{{userGroup.name}}</span>
               		 </el-option>
           			 </el-select>				
               </div>           
            </el-col>      
            </el-row>
            <el-row>
            <el-col :span="1" :offset="5">
                    <b>用户:</b>                                    
            </el-col> 
            <el-col :span="6" >
            <div class="grid-content bg-purple">                                     
				 <el-input                  
                    icon="search"
                    v-model="users"
                    @click="handleUserClick"
                    readonly="true">
            </el-input>				
               </div>           
            </el-col>      
            </el-row>
            <el-row>
            <el-col :span="2" :offset="4" >
                    <b>标题:</b>                                    
            </el-col> 
            <el-col :span="6" >
            <div class="grid-content bg-purple">                                     
				<el-input v-model="message.title" placeholder="请输入内容"></el-input>					
             </div>           
            </el-col>
            
            </el-row>
            <el-row>
            <el-col :span="2" :offset="4" >
                    <b>内容:</b>                                    
            </el-col> 
            <el-col :span="6" >
            <div class="grid-content bg-purple">                                     
				<el-input v-model="message.content" 
				type="textarea"
 				 :rows="5" placeholder="请输入内容"></el-input>					
             </div>           
            </el-col>           
            </el-row>
             
             <el-row>
            <el-col :span="2" :offset="4" >
                    <b>附件:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
			<el-upload					
  			 ref="upload" 	
  			 name="fileUpload"
  			 action="<s:url value='/message/messageSend/fileUpload'/>"		
  			:on-remove="handleRemove"
  			:file-list="fileList"
  			:auto-upload="false"
  			:data="conditionVo"
  			multiple
  			>
  			<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
  			<el-button style="margin-left:10px;" size="small" type="primary" @click="clearFiles">清空文件</el-button>
			</el-upload> 							
             </div>           
            </el-col>           
            </el-row>
            
            <el-row>
            <el-col :span="2" :offset="4"> 
              <el-button type="primary" @click="send">发送</el-button>           
           </el-col>
            <el-col :span="2" :offset="4">
             <el-button type="primary" @click="save">暂存</el-button>  
            </el-col>                               
           </el-row>                 
            </div> 
              

        </el-card>
        
        
        <el-dialog title="用户选择" :visible.sync="dialogFormVisible" size="large">
               
                <el-form>                   
                    <el-form-item label="用户组" :label-width="formLabelWidth">
                     <el-select   @change="handleChange" v-model="selectUserGroup"  filterable placeholder="请选择.." >
               		 <el-option              		 
                        v-for="userGroup in userGroups" 
                        :label="userGroup.name"                      
                        :value="userGroup.groupId">
                        <span style="float: left">{{userGroup.name}}</span>
               		 </el-option>  
               		 </el-select>      			 
           			 <el-input                             
                      		v-model="searchString"                                                    
                            icon="search"
                            :on-icon-click="handleIconClick"
                            placeholder="请输入内容">
                      </el-input>
           			          			 
                    </el-form-item>
                   
                    <el-form-item label="筛选" :label-width="formLabelWidth" >
                      <el-row>
                      	<el-col :span="10">
                      		<el-table 
                      		height="250"
                      		:data="unselectUser"
                   			 border
                   			style="width: 100%"
                    		highlight-current-row
                    		@current-change="handleCurrentChange">                      		
                    		<el-table-column label="备选用户" prop="userName">
                      		</el-table-column>                 		
                      		</el-table>                      		
                      	</el-col>
                        <el-col :span="4"  >
                            <el-row type="flex" justify="center">
                            <div>
                             	<div><el-button size="small" @click="addOne()">》</el-button></div>
                            	<div><el-button size="small" @click="removeOne">《</el-button></div>
                             	<div><el-button size="small" @click="addAll">》》</el-button></div>
                             	<div><el-button size="small" @click="removeAll">《《</el-button></div>                       
                       		</div>
                       		</el-row>
                        </el-col>
                      	<el-col :span="10">
                      	<el-table 
                      		height="250"
                      		:data="selectUser"
                   			 border
                   			 style="width: 100%"
                    		highlight-current-row
    						@current-change="handleCurrentChange">     				
    						<el-table-column label="已选用户" prop="userName">
                      		</el-table-column>                 		
                      		</el-table>
                      		                     		
                      	</el-col>                      	                      
                      </el-row>                                                                                      
                    </el-form-item>                                           
                </el-form>
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
		conditionVo:{messageId:null} ,
		users:"" 
	
		
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
			 if(value != null){
			 this.listByGroupId(value);			 
			 }
			 
			 },
			 handleUserClick:function(){
			 this.dialogFormVisible=true;
			 this.selectUserGroup = null;
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
            var results = this.searchString ? unselectUser.filter(this.createFilter(this.searchString)) : this.searchString;              		
			this.unselectUser = results;
			
			 	 
			 },
			 handleCurrentChange:function(currentRow){
			 
			 this.currentUser = currentRow;
			 
			 },
			 addOne:function(){
			 	if (!this.currentUser) {
					
					this.$alert("请选择","提示",{
		 			confirmButtonText : '确认',		 
					 });
				
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
					
					this.$alert("请选择","提示",{
		 			confirmButtonText : '确认',		 
					 });
					
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
			 	this.message.users.push(this.selectUser[index].userId);			 	 			 			 	 
			 	}
			 	
			 },
			 handleRemove : function(file, fileList) {
                console.log(file, fileList);
             },
             send : function(){
             if(this.message.userGroup==null && this.message.users ==null){
             
             this.$alert("用户组和用户不能同时为空..","提示",{
		 			confirmButtonText : '确认',		 
					 }); 
             return;            
             }
             if(this.message.title==null ){
             
             this.$alert("标题不能为空..","提示",{
		 			confirmButtonText : '确认',		 
					 });
             
             return;            
             }
             if(this.message.content==null ){
             
             this.$alert("内容不能为空..","提示",{
		 			confirmButtonText : '确认',		 
					 }); 
             return;            
             }
             //消息发送
              
			this.$http.post(this.sendUrl,this.message).then(
			function(response){
			 this.message={userGroup:[],users:[],title:null,content:null};
			 this.users = null;
			 this.conditionVo.messageId =  response.data;			
             this.$refs.upload.submit();
             this.clearFiles();		
             this.$message({ type:'info',message:'发送成功'});
             
             	
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
