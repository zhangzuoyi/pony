<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户管理</title>
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
.long-input {width:500px;}
</style>
</head>
<body>
<div id="app">
  <div>   	           	
        <el-card class="box-card content-margin">
            <div slot="header" class="clearfix">
              <el-row>
	              <el-col :span="4">
	              <b>客户管理</b>
	              </el-col>
              </el-row>
              <el-row>             
               <el-col :offset="18" :span="6">
               <el-button type="primary" @click="showAdd">新增</el-button>       
               </el-col>             
              </el-row>
              <el-row>  
	              <!-- <el-col :span="2" >
	                    <b>学生:</b>                                    
	              </el-col>
                <el-col :span="4">
					<el-select v-model="studentId" clearable filterable placeholder="请选择..">           		 
               		 <el-option
                        v-for="student in students" 
                        :label="student.name"                      
                        :value="student.studentId">
               		 </el-option>
           			 </el-select>				
                </el-col> -->
                
                
                <!-- <el-col  :span="4" :offset="4">
                	<el-button type="primary" @click="list">查询</el-button> 
                </el-col> -->                                
              </el-row>
            </div>
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    highlight-current-row>               
                <el-table-column
                        prop="name"
                        label="客户名称"
                        >
                </el-table-column>
                <el-table-column
                        inline-template
                        label="客户类型">
                        <div>{{getTypeName( row.type )}}</div>
                </el-table-column>
                <el-table-column
                		inline-template
                        label="客户状态">
                        <div>{{getStatusName( row.status )}}</div>
                </el-table-column>
                <el-table-column
                        inline-template
                        label="客户级别">
                        <div>{{getLevelName( row.level )}}</div>
                </el-table-column>
                <el-table-column
                        prop="manager"
                        label="负责人"
                        >
                </el-table-column>
                <el-table-column
                		inline-template
                        label="创建日期"
                        >
                        <div>{{row.createTime | date}}</div>
                </el-table-column>
                <el-table-column
                		inline-template
                        label="更新日期"
                        >
                        <div>{{row.updateTime | date}}</div>
                </el-table-column>
                <el-table-column                       
                        label="操作"
                        >
                 <template scope="scope">
                 <el-button size="small" @click="showEdit(scope.row)">编辑</el-button>
                 <!-- <el-button size="small" type="danger" @click="handleDelete(scope.$index,scope.row)">删除</el-button>  -->              
                 </template>                             
                </el-table-column>
            </el-table> 
        </el-card>
        
		<el-dialog  v-model="dialogFormVisible" >
			<div slot="title" class="dialog-title">
                    <b>{{title}}</b>
            </div>
			<el-form :model="customer" :rules="rules" ref="ruleForm" label-width="90px">	
				<el-form-item label="客户名称" prop="name">
				    <el-input v-model="customer.name"></el-input>
				</el-form-item>				 
				<el-row>
					<el-col :span="12">
						<el-form-item label="负责人" prop="manager">
						    <el-input v-model="customer.manager"></el-input>
						</el-form-item>	
					</el-col>
					<el-col :span="12"> 
						<el-form-item label="客户状态" prop="status">
			            	<el-select v-model="customer.status" placeholder="请选择.."  > 
								<el-option v-for="d in statusList" :label="d.value"         
			                        :value="d.code">
			               		 </el-option>
							</el-select> 
						</el-form-item>
	            	</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="客户类型" prop="type">
						    <el-select v-model="customer.type" placeholder="请选择.."  > 
								<el-option v-for="d in typeList" :label="d.value"         
			                        :value="d.code">
			               		 </el-option>
							</el-select> 
						</el-form-item>	
					</el-col>
					<el-col :span="12"> 
						<el-form-item label="客户级别" prop="level">
			            	<el-select v-model="customer.level" placeholder="请选择.."  > 
								<el-option v-for="d in levelList" :label="d.value"         
			                        :value="d.code">
			               		 </el-option>
							</el-select> 
						</el-form-item>
	            	</el-col>
				</el-row>
				<!-- <el-row > 
	            	<el-col :span="2"> 
	            		<b>共享人:</b> 
	            	</el-col> 
	            	<el-col :span="22"> 
		            	<el-input v-model="customer.shares"></el-input>
	            	</el-col> 
				</el-row> -->
				<el-form-item label="所属行业" prop="industryId">
				    <el-select v-model="customer.industryId" placeholder="请选择.."  > 
						<el-option v-for="d in industryList" :label="d.name"         
	                        :value="d.id">
	               		 </el-option>
					</el-select>
				</el-form-item>				
				<el-row>
					<el-col :span="12">
						<el-form-item label="所属地区" prop="area">
						    <el-select v-model="customer.area" placeholder="请选择.."  > 
								<el-option v-for="d in areaList" :label="d.name"         
			                        :value="d.code">
			               		 </el-option>
							</el-select> 
						</el-form-item>	
					</el-col>
					<el-col :span="12"> 
						<el-form-item label="省市" prop="province">
			            	<el-select v-model="customer.province" placeholder="请选择.."  > 
								<el-option v-for="d in provinceList" :label="d.name"         
			                        :value="d.code">
			               		 </el-option>
							</el-select> 
						</el-form-item>
	            	</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="联系电话" prop="phone">
						    <el-input v-model="customer.phone"></el-input>
						</el-form-item>	
					</el-col>
					<el-col :span="12"> 
						<el-form-item label="网站" prop="website">
			            	<el-input v-model="customer.website"></el-input>
						</el-form-item>
	            	</el-col>
				</el-row>
				<el-form-item label="联系地址" prop="addr">
				    <el-input v-model="customer.addr" class="long-input"></el-input>
				</el-form-item>
				<el-form-item label="补充说明" prop="comments">
				    <el-input type="textarea" :rows="5" v-model="customer.comments"></el-input>
				</el-form-item>
		    </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="addCustomer()"  >确定</el-button>
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				
			</div>
		</el-dialog>


		</div>
 

</div>

<script type="text/javascript">
	var app = new Vue({ 
	el : '#app' ,
	data : { 		
		dialogFormVisible:false,
		tableData:[],
		listUrl: "<s:url value='/crm/customer/list'/>",
		//deleteUrl :"<s:url value='/ss/config/delete'/>",
		addUrl :"<s:url value='/crm/customer/add'/>",
		updateUrl :"<s:url value='/ss/admin/edit'/>",
		dictUrl :"<s:url value='/commonDict/listByDictType'/>",
		areaUrl :"<s:url value='/region/areaList'/>",
		provinceUrl :"<s:url value='/region/provinceList'/>",
		industryUrl :"<s:url value='/industry/list'/>",
		title:"",
		customer : {},
		rules :{
			name : [
				{ required: true, message: '请输入客户名称', trigger: 'blur' }
			] 
		},
		statusList : [],
		typeList : [],
		levelList : [],
		areaList : [],
		provinceList : [],
		industryList : []
		
	}, 
	
	mounted : function() { 
		this.getDicts();
		this.getAreaList();
		this.getProvinceList();
		this.getIndustryList();
		this.list();
	
	}, 
	methods : { 
		getDicts : function(){
			this.$http.get(this.dictUrl+"?dictType=customer_status").then(
				function(response){
					this.statusList=[];
					for(var i in response.data){
						var obj={code: parseInt(response.data[i].code), value: response.data[i].value};
						this.statusList.push(obj);
					}
				},
				function(response){}
			);
			this.$http.get(this.dictUrl+"?dictType=customer_type").then(
				function(response){
					this.typeList=[];
					for(var i in response.data){
						var obj={code: parseInt(response.data[i].code), value: response.data[i].value};
						this.typeList.push(obj);
					}
				},
				function(response){}
			);
			this.$http.get(this.dictUrl+"?dictType=customer_level").then(
				function(response){
					this.levelList=[];
					for(var i in response.data){
						var obj={code: parseInt(response.data[i].code), value: response.data[i].value};
						this.levelList.push(obj);
					}
				},
				function(response){}
			);
		},
		getStatusName : function(code){
			for(var i=0;i<this.statusList.length;i++){
				if(this.statusList[i].code == code){
					return this.statusList[i].value;
				}
			}
			return null;
		},
		getTypeName : function(code){
			for(var i=0;i<this.typeList.length;i++){
				if(this.typeList[i].code == code){
					return this.typeList[i].value;
				}
			}
			return null;
		},
		getLevelName : function(code){
			for(var i=0;i<this.levelList.length;i++){
				if(this.levelList[i].code == code){
					return this.levelList[i].value;
				}
			}
			return null;
		},
		getAreaList : function(){
			this.$http.get(this.areaUrl).then(
				function(response){
					this.areaList=response.data;
				},
				function(response){}
			);
		},
		getProvinceList : function(){
			this.$http.get(this.provinceUrl).then(
				function(response){
					this.provinceList=response.data;
				},
				function(response){}
			);
		},
		getIndustryList : function(){
			this.$http.get(this.industryUrl).then(
				function(response){
					this.industryList=response.data;
				},
				function(response){}
			);
		},
		showAdd:function(){
			this.dialogFormVisible = true;
			this.title="新增客户";
			this.customer = {name:null, manager:null, status:null, type:null, level:null, industryId:null, area:null, province:null, phone:null, website:null, addr:null, comments:null};
			
		},
		showEdit:function(obj){
			this.dialogFormVisible = true;
			this.title="修改客户";
			this.customer = obj;
			
		},
		list : function(){
			this.$http.get(this.listUrl).then(
					function(response){
						this.tableData=response.data;
					},
					function(response){}  			
			);
		},
		addCustomer : function(){
			app.$refs["ruleForm"].validate(function(result){
				if(result){
					app.$http.post(app.addUrl, app.customer).then(
						function(response){
							app.dialogFormVisible=false;
							app.$message({
								type:"info",
								message:"新增成功"
							});
							app.list();
						},
						function(response){}
					 ); 
				}else{
					return;
				}
			});
			
		}
		
      }
	 
	
});  
	
</script>
</body>
</html>