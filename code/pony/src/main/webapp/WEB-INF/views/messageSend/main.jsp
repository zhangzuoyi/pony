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
            <el-col :span="2" :offset="5">
                    <b>用户组:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-select multiple v-model="userGroup"    filterable placeholder="请选择.." >
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
            <el-col :span="2" :offset="5">
                    <b>用户:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
					<el-select multiple v-model="users"    filterable placeholder="请选择.." >
               		 <el-option
                        v-for="user in users" 
                        :label="user.name"                      
                        :value="user.userId">
                        <span style="float: left">{{user.name}}</span>
               		 </el-option>
           			 </el-select>				
               </div>           
            </el-col>      
            </el-row>
            <el-row>
            <el-col :span="2" :offset="4" >
                    <b>标题:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
				<el-input v-model="title" placeholder="请输入内容"></el-input>					
             </div>           
            </el-col>
            
            </el-row>
            <el-row>
            <el-col :span="2" :offset="4" >
                    <b>内容:</b>                                    
            </el-col> 
            <el-col :span="5" >
            <div class="grid-content bg-purple">                                     
				<el-input v-model="content" 
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
 			 :on-preview="handlePreview"
  			:on-remove="handleRemove"
  			:file-list="fileList"
  			:auto-upload="false">
  			<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
</el-upload>

  
</el-upload>					
             </div>           
            </el-col>           
            </el-row>
            
            <el-row>
            <el-col :span="2" :offset="4"> 发送 </el-col>
            <el-col :span="2" :offset="4"> 暂存 </el-col>                   
            </el-row>
                  
            </div> 
              
        </el-card>
        
        
        <el-dialog title="用户选择" v-model="dialogFormVisible" size="large">
               
                <el-form :model="">
                    
                    <el-form-item label="用户组" :label-width="formLabelWidth">
                        <el-select v-model=""    filterable placeholder="请选择.." >
               		 <el-option
                        v-for="item in userGroup"
                        :label="item.name"
                        :value="item.groupId">
                	</el-option>               		
           			 </el-select> 
           			 <el-input                             
                      		v-model="searchString"                                                    
                            icon="search"
                            :on-icon-click="handleIconClick"
                            placeholder="请输入内容">
                      </el-input>
           			          			 
                    </el-form-item>
                   
                    <el-form-item label="筛选" :label-width="formLabelWidth" ">
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
		
		
	
		
	}, 
	mounted : function() { 
		
			
	}, 
	methods : { 
			   	
			
			
		  
        }	        
	 
	
});  

</script>
</body>
</html>