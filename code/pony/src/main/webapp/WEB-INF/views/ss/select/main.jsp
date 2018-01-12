<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>科目选择</title>
<link rel="stylesheet" type="text/css"
	href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css"
	href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css"
	href="<s:url value='/static/css/icon.css' />" />
<%-- <link rel="stylesheet" type="text/css" href="<s:url value='/static/bootstrap/css/bootstrap.min.css' />" /> --%>
<link rel="stylesheet" type="text/css"
	href="<s:url value='/static/elementUI/index.css' />" />
<link rel="stylesheet" type="text/css"
	href="<s:url value='/static/elementUI/element.css' />" />
<script type="text/javascript"
	src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/bootstrap/js/bootstrap.min.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/easyui/dateFormat.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/vue/vue.min.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/vue/vue-resource.min.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/vue/vue-validator.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/elementUI/index.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/js/moment.min.js' />"></script>
<script type="text/javascript"
	src="<s:url value='/static/vue/myfilters.js' />"></script>
<style type="text/css">
.el-input {
	width: 200px;
}
</style>
</head>
<body>
	<div id="app">
		<div>
			<el-card class="box-card content-margin" v-if=" ! canSelect()">
				<div>您没有选课资格！</div>
			</el-card>
			<el-card class="box-card content-margin" v-if="canSelect() && showEdit">
			<div slot="header" class="clearfix">
				<el-row> 
				<el-col :span="4"> 
				<b>科目选择</b> 
				</el-col> 
				</el-row>
				<el-row> 
				<el-col :span="4"> 
				<span>学年</span> 
				</el-col> <el-col :span="6"> <span>{{config.schoolYear.name}}</span>
				</el-col> </el-row>
				<el-row> <el-col :span="4"> <span>可选数量</span> </el-col> <el-col
					:span="6"> <span>{{config.selectNum}}</span> </el-col> </el-row>
				<el-row> 
				<el-row> <el-col :span="4"> <span>选课时间范围</span>
				</el-col> <el-col :span="6"> <span>{{new
					Date(config.startTime).toLocaleString()}}--{{new
					Date(config.endTime).toLocaleString()}}</span> </el-col> 
				</el-row> 
				<el-row v-if="before"> 
					<el-col :span="4"> 
					<b>选课暂未开始</b> 
					</el-col> 
				</el-row>
					<el-row v-if="!before  && !after "> <el-col
					:span="4"> <span>科目</span> </el-col> <el-col :span="10"> <el-checkbox-group
					v-model="selectSubjects" id="subjectsGroup"> <el-checkbox
					v-for="x in config.subjectArray" :label="x">{{x}}</el-checkbox> </el-checkbox-group> </el-col> 
					</el-row>
					<el-row v-if="!before  && !after "> <el-col :offset="5" :span="10"> <el-button
					size="small" @click="saveSelect()">保存</el-button> <el-button
					size="small" @click="cancelEdit()"
					v-if="subjects != null && subjects.length>0">取消</el-button> </el-col> </el-row>
					<el-row v-if="after"> 
				<el-col :span="4"> 
				<b>选课已经结束</b> 
				</el-col> 
				</el-row>
				<el-row v-if="after"> <el-col :span="4"> <span>我的选择</span> </el-col> <el-col
					:span="6"> <span>{{mySelect}}</span> </el-col> </el-row>
				
			</div>
			</el-card>
			<el-card class="box-card content-margin" v-if="canSelect() && ! showEdit">
			<div slot="header" class="clearfix">
				<el-row> <el-col :span="4"> <b>科目选择</b> </el-col> </el-row>
				<el-row> <el-col :span="4"> <span>学年</span> </el-col> <el-col
					:span="6"> <span>{{config.schoolYear.name}}</span> </el-col> </el-row>
				<el-row> <el-col :span="4"> <span>选课时间范围</span> </el-col> <el-col
					:span="6"> <span>{{new
					Date(config.startTime).toLocaleString()}}--{{new
					Date(config.endTime).toLocaleString()}}</span> </el-col> </el-row>
				<el-row> <el-col :span="4"> <span>可选数量</span> </el-col> <el-col
					:span="6"> <span>{{config.selectNum}}</span> </el-col> </el-row>
				<el-row v-if="before"> 
				<el-col :span="4"> 
				<b>选课暂未开始</b> 
				</el-col> 
				</el-row>
				
				<el-row v-if="!before  && !after "> <el-col :span="4"> <span>已选科目</span> </el-col> <el-col
					:span="10"> <span v-for="x in subjects"
					style="margin: 5px;">{{x}}</span> </el-col> </el-row>
				<el-row v-if="!before  && !after "> <el-col :offset="5" :span="10"> <el-button
					size="small" @click="editSelect()">重新选择</el-button> </el-col> </el-row>
					
					<el-row v-if="after"> 
				<el-col :span="4"> 
				<b>选课已经结束</b> 
				</el-col> 
				</el-row>
				<el-row v-if="after"> <el-col :span="4"> <span>我的选择</span> </el-col> <el-col
					:span="6"> <span>{{mySelect}}</span> </el-col> </el-row>
			   </div>
			</el-card>



		</div>
	</div>

	<script type="text/javascript">
		var app = new Vue({
			el : '#app',
			data : {
				config : {},
				subjects : [],
				selectSubjects : [],
				dialogFormVisible : false,
				currentConfigUrl : "<s:url value='/ss/config/current'/>",
				selectedUrl : "<s:url value='/ss/select/selected'/>",
				saveUrl : "<s:url value='/ss/select/save'/>",
				currentStudentGradeIdUrl:"<s:url value='/ss/select/currentStudentGradeId'/>",
				types : [ {
					id : "0",
					name : "否"
				}, {
					id : "1",
					name : "是"
				} ],
				showEdit : true,
				before : false,
				after : false,
				studentGradeId:null,
				mySelect : "",
				
				rules : {
				/* seq: [{required :true,message:"请填写顺序..",trigger:"blur"}]	 */
				}

			},

			mounted : function() {
				this.getCurrentConfig();
				this.getSelectedSubjects();
				this.getStudentGradeId();
			},
			methods : {
				getCurrentConfig : function() {
					this.$http.get(this.currentConfigUrl).then(
							function(response) {
								this.config = response.data;
								var d = new Date();								
								if (d.getTime() < this.config.startTime) {
									this.before = true;
								}else if (d.getTime() > this.config.endTime) {
									this.after = true;
								}

							}, function(response) {
							});
				},
				getStudentGradeId : function(){
					this.$http.get(this.currentStudentGradeIdUrl).then(
					function(response){
						this.studentGradeId=response.data;
					},
					function(response){}  			
					); 
				},	
				getSelectedSubjects : function() {
					this.$http.get(this.selectedUrl).then(function(response) {
						this.subjects = response.data;
						if (this.subjects != null && this.subjects.length > 0) {
							this.showEdit = false;
							for(var idx in this.subjects){
								this.mySelect+= this.subjects[idx]+" ";
							}						
						}
						if(this.mySelect.length<=0){
							this.mySelect = "您暂未选课记录";
						}
					}, function(response) {
					});
				},
				saveSelect : function() {
					if (this.config.selectNum != this.selectSubjects.length) {
						this
								.$alert("请选择" + this.config.selectNum + "门科目",
										"提示");
					} else {
						this.$http.post(this.saveUrl, {
							configId : this.config.configId,
							subjects : this.selectSubjects.join(",")
						}, {
							emulateJSON : true
						}).then(function(response) {
							this.subjects = this.selectSubjects;
							this.showEdit = false;
						}, function(response) {
						});
					}

				},
				editSelect : function() {
					this.showEdit = true;
					this.selectSubjects = this.subjects;
				},
				cancelEdit : function() {
					this.showEdit = false;
				},
				canSelect : function(){
					return this.config.gradeIds.indexOf(this.studentGradeId) >=0;
				}
			}

		});
	</script>
</body>
</html>