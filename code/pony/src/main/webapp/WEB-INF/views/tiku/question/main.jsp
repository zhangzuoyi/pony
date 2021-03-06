<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>题库管理</title>
    <link rel="stylesheet" href="<s:url value='/static/layui/css/layui.css' />">
    <link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />"/>
    <link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />"/>
    <link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />"/>
    <%-- <link rel="stylesheet" type="text/css" href="<s:url value='/static/bootstrap/css/bootstrap.min.css' />" /> --%>
    <link rel="stylesheet" type="text/css" href="<s:url value='/static/elementUI/index.css' />"/>
    <link rel="stylesheet" type="text/css" href="<s:url value='/static/elementUI/element.css' />"/>
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
            width: 200px;
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
                    <%--el-col :span="4" >
                           <el-button type="primary"  @click="getQuestions">查询</el-button>
                           <!-- <el-button type="primary"  @click="showAdd">新增</el-button> -->
                      </el-col>  --%>
                </el-row>
            </div>
            <div>
                <el-card v-for="x in questions" :id="x.id">
                    <div v-on:mouseenter="mouserenter(x.id)" v-on:mouseleave="mouserleave(x.id)">
                        <div class="exam-head">
                            <el-row>
                                <el-col :span="5"><em>题型:{{x.type}}</em></el-col>
                                <el-col :span="5"><em>题类:</em></el-col>
                                <el-col :span="5"><em>难易度:{{x.difficulty}}</em></el-col>
                                <el-col :span="9"><em>试题来源:<a v-bind:href="x.paperCollectUrl" target="_blank">{{x.paperName}}</a></em>
                                </el-col>
                            </el-row>

                        </div>
                        <hr class="layui-bg-grey" style="margin: 5px 0px">
                        <div style="cursor: pointer" @click="switchShowPoint(x)">

                            <div class="exam-content" style="border-bottom: 2px dashed #dcdcdc">
                                <div class="exam-q"><span v-html="x.question"></span></div>
                                <div class="exam-a">
                                    <el-row>
                                        <el-col v-for="(item,index) in x.itemArr" :span="6">
                                            <%--{{item.item}}--%>
                                            <span v-html="item.item"></span>
                                        </el-col>
                                    </el-row>
                                </div>
                            </div>
                            <div v-show="x.showPoint" style="padding-top: 5px;padding-bottom: 5px;">
                                <div>
                                    <span class="exam-point"><b>【考点】</b></span>
                                    <div>
                                        <span>{{x.checkPoints}}</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="exam-foot" style="padding-top: 5px">
                            <el-button type="primary" size="small" round @click="analyze(x.id)">解析</el-button>
                            <el-button v-if="!addFlag(x)" type="primary" size="small" round @click="add(x)">+选题
                            </el-button>
                            <el-button v-if="addFlag(x)" type="danger" size="small" round @click="remove(x)">-选题
                            </el-button>


                        </div>
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
        <div class="basket"
             style="position: fixed;top: 50%;right: 0; z-index: 999;overflow: hidden;padding-left: 40px;">
            <div class="basket_tit"
                 style="position: absolute;top: 0;left:0;width: 40px;height:100%;background:#4498ee;color: #fff;">
                <p style=" margin-top: 20px;line-height: 20px;font-size: 14px;">
                    <i class="layui-icon" style="display: inline-block;margin-left: 10px;font-size: 25px;">&#xe60a;</i>
                    <em style="display: block;width: 1em;margin-left: 13px;margin-right: 13px;">试题篮</em>
                </p>
                <span style="position: absolute;bottom: 0;left: 0;width: 40px;height: 40px;display: block; border-top:1px solid #eee;cursor: pointer;">
                    <i v-if="openFlag" class="layui-icon"
                       style="display: inline-block;margin-top: 12px; margin-left: 10px;font-size: 25px;"
                       @click="closeBasket()">&#xe602;</i>
                    <i v-if="!openFlag" class="layui-icon"
                       style="display: inline-block;margin-top: 12px; margin-left: 10px;font-size: 25px;"
                       @click="openBasket()">&#xe603;</i>
                </span>

            </div>
            <div class="basket_con"
                 style="float: left;width: 100px;border:1px solid #dad4ae;border-left: 0;min-height: 198px;">
                <div class="basket_count" style="margin: 2px;min-height: 150px;">
                    <div style="background: #d5eaff;font-size: 12px;text-align: center;">共计<span style="color: #fe8a00">{{count}}</span>道题
                    </div>
                    <div v-for="(item,key) in selectList" style="text-align: center;padding-top: 5px;">
                        <span>{{key}}:<span style="color: #fe8a00"> {{item.length}}</span>道</span>
                    </div>
                </div>
                <div class="basket_foot" style="text-align: center;">
                    <el-button type="primary" size="small" round @click="generatePaper">生成试卷</el-button>
                </div>

            </div>

        </div>

        <el-dialog title="解析" :visible.sync="dialogFormVisible">
            <el-card>
                <div class="exam-head">
                    <b>答案:</b> <span v-html="question.answer"></span>
                </div>
                <div class="exam-content" style="padding-top: 10px">
                    <span v-html="question.analysis"></span>
                </div>

            </el-card>
        </el-dialog>

        <el-dialog title="生成试卷" :visible.sync="dialogFormVisible2">
            <el-card>
                <el-row>
                    <el-col :span="8">
                        试卷名:
                <el-input
                        v-model="zujuanName"
                        placeholder="请输入试卷名...">
                </el-input>
                    </el-col>

                </el-row>
                <el-row>
                    <el-col :span="8">
                        年级:
                        <el-select v-model="gradeId" placeholder="请选择" clearable>
                            <el-option v-for="x in grades" :label="x.value" :value="x.code"></el-option>
                        </el-select>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        科目:
                        <el-select v-model="subjectId" placeholder="请选择" clearable>
                            <el-option v-for="x in subjects" :label="x.value" :value="x.code"></el-option>
                        </el-select>
                    </el-col>
                    <el-col :offset="2" :span="4">
                        <el-button :disabled="zujuanName == null || zujuanName == '' || gradeId == null || gradeId == ''||subjectId == null || subjectId == '' "
                                   type="primary" size="small" round
                                   @click="submitZujuan()">确认
                        </el-button>
                    </el-col>
                </el-row>


            </el-card>
        </el-dialog>


    </div>


</div>
<script type="text/javascript">

    var app = new Vue({
        el: '#app',
        data: {
            conditionVo: {questionType: null, currentPage: 1, pageSize: 10},
            dialogFormVisible: false,
            formLabelWidth: "120px",
            tableData: [],
            questionsUrl: "<s:url value='/tiku/question/list'/>",
            submitZujuanUrl: "<s:url value='/tiku/zujuan/add'/>",
            gradesUrl:"<s:url value='/tiku/dict/grades'/>",
            subjectsUrl:"<s:url value='/tiku/dict/subjects'/>",
            currentPage: 1,
            pageSizes: [10],
            pageSize: [10],
            total: null,
            questions: [],
            question: {},
            openFlag: true,//选题篮打开标记
            count: 0,//总选题数目
            selectList: {},//已选集合,key  类型  value  选题id的数组
            selectId: null,
            dialogFormVisible2: false,
            zujuanName: null,
            gradeId:null,
            subjectId:null,
            grades:[],
            subjects:[]


        },
        /*filters: {
        userTypeFilter: function (value) {
          if(value == 't'){return "老师"; }
          if(value == 's'){return "学生"; }
        }
      }	,*/

        mounted: function () {
            this.getQuestions();
            this.getGrades();
            this.getSubjects();
            //this.dialogFormVisible2 = false;//解决el-dialog中的el-tree第一次的动态渲染问题


        },
        methods: {


            handleCurrentChange: function (val) {
                this.conditionVo.currentPage = val;
                this.getQuestions();
                //console.log('当前页 : ${val}');
            },
            getGrades: function () {
                this.$http.get(this.gradesUrl).then(
                    function (response) {
                        this.grades = response.data;
                    },
                    function (response) {
                    }
                );
            },
            getSubjects: function () {
                this.$http.get(this.subjectsUrl).then(
                    function (response) {
                        this.subjects = response.data;
                    },
                    function (response) {
                    }
                );
            },

            getQuestions: function () {
                this.$http.post(this.questionsUrl, this.conditionVo).then(
                    function (response) {
                        this.questions = response.data.content;


                        this.total = response.data.totalElements;
                    },
                    function (response) {
                    }
                );
            },

            analyze: function (id) {
                this.dialogFormVisible = true;
                for (var index in this.questions) {
                    if (id == this.questions[index].id) {
                        this.question = this.questions[index];
                    }
                }

            },

            switchShowPoint: function (x) {
                x.showPoint = !x.showPoint;
            },

            mouserenter: function (id) {
                document.getElementById(id).style.border = "1px solid #20a0ff";
            },
            mouserleave: function (id) {
                document.getElementById(id).style.border = "1px solid #eeeeee";
            },
            closeBasket: function () {
                document.getElementsByClassName('basket')[0].style.right = '-100px';
                this.openFlag = !this.openFlag;
            },
            openBasket: function () {
                document.getElementsByClassName('basket')[0].style.right = '0px';
                this.openFlag = !this.openFlag;

            },

            add: function (x) {
                this.count++;
                this.selectId = x.id;
                if (this.selectList[x.type] != null) {
                    this.selectList[x.type].push(x.id);
                } else {
                    var arr = new Array();
                    arr.push(x.id);
                    this.selectList[x.type] = arr;
                }
            },
            remove: function (x) {
                this.count--;
                for (var i = 0; i < this.selectList[x.type].length; i++) {
                    if (this.selectList[x.type][i] == x.id) {
                        this.selectList[x.type].splice(i, 1);
                        if (this.selectList[x.type].length <= 0) {
                            delete this.selectList[x.type];
                        }
                        break;
                    }
                }
            },
            addFlag: function (x) {
                if (this.selectList[x.type] != null) {
                    for (var i = 0; i < this.selectList[x.type].length; i++) {
                        if (this.selectList[x.type][i] == x.id) {
                            return true;
                        }
                    }
                }
                return false;
            },
            generatePaper: function () {
                //1 保存 2 跳转
                if (this.count <= 0) {
                    this.$message({type:"info",message:"选题数不能为空!"});

                    return;
                }
                this.dialogFormVisible2 = true;

            },
            submitZujuan: function () {

                var questionIds = new Array();
                for (var key in this.selectList) {
                    questionIds.push.apply(questionIds,this.selectList[key]);
                }

                this.$http.get(this.submitZujuanUrl, {params: {name: this.zujuanName,gradeId:this.gradeId,subjectId:this.subjectId, questionIds: questionIds}}).then(
                    function (response) {
                        this.dialogFormVisible2 = false;
                        this.zujuanName = null;
                        this.gradeId = null;
                        this.subjectId = null;
                        this.count = 0;
                        this.selectList = {};
                        this.$message({type:"info",message:"组卷成功!"});

                    },
                    function (response) {
                    }
                );


            }
        }



    });

</script>
</body>
</html>
