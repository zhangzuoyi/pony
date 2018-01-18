<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>题库管理</title>
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
              <b>题库管理</b>
              </el-col>
              </el-row>
              <el-row>                            
            	<%--<el-col :span="2" >
                    <b>题目类型:</b>
            	</el-col> 
            	<el-col :span="4" >
                    <el-select v-model="conditionVo.questionType" placeholder="请选择" clearable>
	                    <el-option v-for="x in questionTypes" :label="x.name" :value="x.type"></el-option>
	                </el-select>
            	</el-col>--%>
            	<el-col :span="4" >
               		<el-button type="primary"  @click="getQuestions">查询</el-button>
               		<!-- <el-button type="primary"  @click="showAdd">新增</el-button> -->
              	</el-col>                           
              </el-row>
            </div>
                <div >
                    <el-card v-for="x in questions">
                        <div class="exam-head" >
                            <el-row >
                                <el-col :span="5"><em>题型:{{x.type}}</em></el-col>
                                <el-col :span="5"><em>题类:</em></el-col>
                                <el-col :span="5"><em>难易度:{{x.difficulty}}</em></el-col>
                                <el-col :span="9"><em>试题来源:<a href="" target="_blank"></a></em></el-col>
                            </el-row>

                        </div>

                        <div class="exam-content">
                            <div class="exam-q"><span v-html="x.question"></span></div>
                            <div class="exam-a">
                                <el-row>
                                    <el-col   v-for="(item,index) in x.itemArr" :span="6">
                                       {{item.item}}
                                    </el-col>
                                </el-row>
                            </div>
                        </div>
                        <div class="analyticbox-brick analyticbox-brick-normal" style="display: block;">
                            <div class="analyticbox" style="display: block;">
                                <span class="exam-point">【考点】</span>
                                <div class="analyticbox-body">
                                    <img src="http://webshot.zujuan.com/q/8a/aa/81ea9d980b4a29254ea98bbd84a6_5492191kn.png?hash=b92fc93ff295d38640638b29f27f6ee6&amp;sign=f7527e2dda58996787edde0420e54d65&amp;from=2" style="vertical-align: middle;">
                                </div>
                            </div>
                        </div>
                        <div class="exam-foot">
                            <el-button type="primary"  @click="analyze(x.id)">解析</el-button>
                        </div>
                    </el-card>
                </div>
            <el-row>
            <el-col :offset="10" :span="14">
            <el-pagination
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="pageSizes"
                :page-size="pageSize"
                layout="total,sizes,prev,pager,next,jumper"
                :total="total"
                ></el-pagination>
            </el-col>
             
			</el-row>
			

        </el-card>
			 <el-dialog title="解析" v-model="dialogFormVisible" >
                 <el-card>
                     <div class="exam-head">{{question.answer}}</div>
                     <div class="exam-content">
                         <span>{{question.analysis}}</span>
                     </div>
                 </el-card>
			</el-dialog>

			



		</div>
 

</div>
<script type="text/javascript">
		
var app = new Vue({ 
	el : '#app' ,
	data : {
        conditionVo:{questionType:null,currentPage:1,pageSize:10},
        dialogFormVisible:false,
		formLabelWidth:"120px",
		tableData:[],
		questionsUrl:"<s:url value='/tiku/question/list'/>",
		currentPage : 1,
		pageSizes :[10],
		pageSize:[10],
		total:null,
        questions:[],
        question:{}

		
	}, 
	/*filters: {
    userTypeFilter: function (value) {
      if(value == 't'){return "老师"; }
      if(value == 's'){return "学生"; }
    }
  }	,*/
	
	mounted : function() { 
		this.getQuestions();
		//this.dialogFormVisible2 = false;//解决el-dialog中的el-tree第一次的动态渲染问题
		
			
	}, 
	methods : {
	

			handleCurrentChange: function(val){
				this.conditionVo.currentPage = val;
				this.getQuestions();
				//console.log('当前页 : ${val}');
			},



        getQuestions : function(){
			this.$http.post(this.questionsUrl,this.conditionVo).then(
				function(response){
					this.questions=response.data.content;


					this.total = response.data.totalElements;
				},
				function(response){}  			
				); 
			} ,

        analyze : function(id){
            this.dialogFormVisible = true;
            for(var x in this.questions){
                if(id == x.id){
                    this.question = x;
                }
            }

        }

		
			
			 
			   	
	  
        }	        
	 
	
});  

</script>
</body>
</html>
