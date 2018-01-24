<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>测试管理</title>
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
    <script type="text/javascript" src="<s:url value='/static/vue/vue-router.js' />"></script>
    <script type="text/javascript" src="<s:url value='/static/elementUI/index.js' />"></script>
    <style type="text/css">
        .el-input {
            width: 200px;
        }
    </style>
</head>
<body>
<div id="app">

    <div v-if="listFlag">
        <el-card class="box-card content-margin">
            <div slot="header" class="clearfix">
                <el-row>
                    <el-col :span="4">
                        <b>测试管理</b>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="2">
                        <b>年级:</b>
                    </el-col>
                    <el-col :span="4">
                        <el-select v-model="conditionVo.gradeId" placeholder="请选择" clearable>
                            <el-option v-for="x in grades" :label="x.value" :value="x.code"></el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="4">
                        <el-select v-model="conditionVo.subjectId" placeholder="请选择" clearable>
                            <el-option v-for="x in subjects" :label="x.value" :value="x.code"></el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="4">
                        <el-button type="primary" @click="getZujuans">查询</el-button>
                    </el-col>
                </el-row>
            </div>
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    highlight-current-row
            >

                <el-table-column
                        prop="name"
                        label="试卷名"
                >
                </el-table-column>
                <el-table-column
                        prop="gradeName"
                        label="年级"
                >
                </el-table-column>
                <el-table-column
                        prop="subjectName"
                        label="科目"
                >
                </el-table-column>
                <el-table-column
                        label="操作"
                >
                    <template scope="scope">
                        <el-button size="small" @click="gotoTest(scope.$index,scope.row)">进入测试</el-button>
                    </template>
                </el-table-column>

            </el-table>
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
    </div>

    <div v-if="!listFlag">
        <div>
            <el-row>
                <el-col :offset="20" :span="4">
                    <el-button size="small" type="primary" @click="submitTest">提交</el-button>
                </el-col>
            </el-row>
        </div>
        <div>
            <div class="header" style="height: 50px;">
                <el-row>
                    <el-col :offset="10" :span="6" style="font-size: 20px"><b>{{zujuan.name}}</b>
                    </el-col>
                </el-row>
            </div>
            <div class="content">
                <div v-for="(question,index) in questions" :id="question.id">
                    <div class="question-header">
                        <span><b>{{index+1}}.({{question.score}}分)</b></span>&nbsp;&nbsp;
                        <span v-html="question.question"></span>
                        <span v-if="question.typeCode==3"><el-input v-model="question.answer"></el-input></span><%-- 3 填空题--%>


                    </div>
                    <div>
                        <span v-if="question.typeCode==4"><el-input type="textarea" :rows="10" v-model="question.answer"></el-input></span><%-- 4 计算题--%>
                        <span v-if="question.typeCode==5"><el-input type="textarea" :rows="10" v-model="question.answer"></el-input></span><%-- 5 解答题--%>
                        <span v-if="question.typeCode==6"><el-input type="textarea" :rows="10" v-model="question.answer"></el-input></span><%-- 6 综合题--%>
                    </div>
                    <div class="exam-answer">
                        <el-row v-if="question.typeCode==1"><%-- 1 单选题--%>
                            <el-radio-group style="width: 100%" v-model="question.answer">
                                <el-col v-for="(item,index) in question.itemArr" :span="6">
                                    <el-radio :label="index+1"><span v-html="item.item"></span></el-radio>
                                </el-col>
                            </el-radio-group>
                        </el-row>
                        <el-row v-if="question.typeCode==2"><%-- 1 多选题--%>
                            <el-checkbox-group style="width: 100%" v-model="question.answer">
                                <el-col v-for="(item,index) in question.itemArr" :span="6">
                                    <el-checkbox label="index+1"><span v-html="item.item"></span></el-checkbox>
                                </el-col>
                            </el-checkbox-group>
                        </el-row>


                    </div>

                </div>
            </div>
        </div>


    </div>

</div>


<script type="text/javascript">

    var app = new Vue({
        el: '#app',
        data: {
            conditionVo: {gradeId: null, subjectId: null, currentPage: 1, pageSize: 10},
            tableData: [],
            zujuansUrl: "<s:url value='/tiku/zujuan/list'/>",
            gradesUrl: "<s:url value='/tiku/dict/grades'/>",
            subjectsUrl: "<s:url value='/tiku/dict/subjects'/>",
            questionListUrl: "<s:url value='/tiku/zujuan/listQuestion'/>",
            submitTestUrl: "<s:url value='/tiku/test/submitTest'/>",
            grades: [],
            subjects: [],
            currentPage: 1,
            pageSizes: [10],
            pageSize: [10],
            total: null,
            zujuans: [],
            zujuan: {},
            listFlag: true,//展示or编辑
            question: {},
            questions: [],


        },
        /*filters: {
         userTypeFilter: function (value) {
         if(value == 't'){return "老师"; }
         if(value == 's'){return "学生"; }
         }
         }	,*/

        mounted: function () {
            this.getZujuans();
            this.getGrades();
            this.getSubjects();


        },
        methods: {


            handleCurrentChange: function (val) {
                this.conditionVo.currentPage = val;
                this.getZujuans();
                //console.log('当前页 : ${val}');
            },


            getZujuans: function () {
                this.$http.post(this.zujuansUrl, this.conditionVo).then(
                    function (response) {
                        this.tableData = response.data.content;
                        this.total = response.data.totalElements;
                    },
                    function (response) {
                    }
                );
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

            getQuestionList: function (id) {
                this.$http.get(this.questionListUrl, {params: {zujuanId: id}}).then(
                    function (response) {
                        this.questions = response.data;


                    },
                    function (response) {
                    }
                );
            },
            gotoTest: function (index, row) {

                this.listFlag = false;
                this.zujuan = row;
                this.getQuestionList(row.id);

            },

            submitTest: function () {

                this.$confirm("确认提交吗？", "提示", {
                    confirmButtonText: '确认',
                    cancleButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    app.$http.post
                    (app.submitTestUrl, app.questions).then(
                        function (response) {
                            app.listFlag = true;
                            app.zujuan = {};
                            app.getZujuans();
                            app.questions = [];
                            app.$message({type: 'info', message: '提交成功'})

                        },
                        function (response) {
                        }
                    );
                })
                    .catch(function () {
                        app.$message({type: 'info', message: '已取消删除'})
                    });


            },


        }


    });


</script>


</body>
</html>
