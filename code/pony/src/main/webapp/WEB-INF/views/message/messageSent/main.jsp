<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>已发送消息</title>
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
              <b>已发送消息</b>
              </el-col>
              </el-row>     
            <el-row>
            <el-col :span="6" :offset="16"> 
             <el-button type="primary" @click="deleteMessage">删除</el-button> 
             <el-button type="primary" @click="refresh">更新</el-button>                           
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
                        label="老师编号"
                        width="120">
                    <div>{{ row.date }}</div>
                </el-table-column> -->
                 <el-table-column
                        label="标题"
                        >
                     <template slot-scope="scope"> <div><a href="javascript:void(0)" @click="watchMessage(scope.row.id)">{{scope.row.title}}</a></div></template>

                </el-table-column>
                <el-table-column
                        label="发送时间"
                       >
                    <template slot-scope="scope">{{scope.row.sendTime }}</template>
                </el-table-column>
                <el-table-column
                        prop="receiveUser"
                        label="收件人"                       
                        >
                </el-table-column>                                               
                <el-table-column
                        label="附件"
                        show-overflow-tooltip>
                    <template slot-scope="scope"><div><a v-for="attach in scope.row.attachs"  href="javascript:void(0)" @click="downloadAttach(scope.row.messageId,attach)" >{{attach}}&nbsp;</a></div></template>


                </el-table-column>
            </el-table>
        </el-card>
        
        
        <el-dialog title="消息" :visible.sync="dialogFormVisible" size="large">
               <el-form>                   
                    <el-form-item label="收件人" :label-width="formLabelWidth">                          			 
           			 <el-input v-model="selectMessage.receiveUser" :disabled="true"></el-input>          			          			 
                    </el-form-item>
                    <el-form-item label="标题" :label-width="formLabelWidth">                          			 
           			 <el-input v-model="selectMessage.title" :disabled="true"></el-input>          			          			 
                    </el-form-item>
                    <el-form-item label="内容" :label-width="formLabelWidth">                          			 
           			 <el-input v-model="selectMessage.content" type="textarea" :rows="5" :disabled="true"></el-input>          			          			 
                    </el-form-item>           
             </el-form>
         	 <div slot="footer" class="dialog-footer">                                     
                    <el-button @click.native="dialogFormVisible = false">取 消</el-button>
                </div>
        </el-dialog>              	
    </div>
 

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : { 				
		messageSentUrl:"<s:url value='/message/messageSent/list'/>",
		deleteMessageUrl:"<s:url value='/message/messageSent/delete'/>",
		dialogFormVisible:false,
		formLabelWidth:"120px",
		tableData:[],
		selectMessage:{},
		selectMessages:[]
	
		
	}, 	
	mounted : function() { 
		this.getMessageSent();
			
	}, 
	methods : { 
	   refresh :function(){
	    this.getMessageSent();
	   },
		getMessageSent : function(){ 
			this.$http.get(this.messageSentUrl).then(
			function(response){
			this.tableData=response.data;},
			function(response){}  			
			); 
			} ,
	deleteMessage : function(val){
			var selectMessage = [];
			for(var message in this.selectMessages){
			selectMessage.push(this.selectMessages[message].messageId);
			}		 
			this.$http.get(this.deleteMessageUrl,{params:{selectMessage:selectMessage}}).then(
					function(response){
					this.getMessageSent();},
					function(response){}  			
					); 
					} ,	
		watchMessage : function(id){ 
						this.dialogFormVisible = true;
						for(var i=0;i<this.tableData.length;i++){
						if(id == this.tableData[i].id){
								this.selectMessage = this.tableData[i];
								break;
							}
						
						}						
					} ,	
		downloadAttach:function(messageId,attachName){
		  var exportParams = {
										messageId : messageId,
										attachName  :  attachName,																				
								};
					var url = "../downloadAttach?"+jQuery.param(exportParams);
								/*  window.location.href = encodeURI(encodeURI(url));*/
					            window.open(encodeURI(encodeURI(url)));
		},		
					
	    handleSelectionChange:function(val) {
		             this.selectMessages = val;                                   
		            },	
			
			 
			   	
	  
        }	        
	 
	
});  

</script>
</body>
</html>
