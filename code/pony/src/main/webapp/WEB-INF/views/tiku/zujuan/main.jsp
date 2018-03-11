<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>组卷管理</title>
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
                        <b>组卷管理</b>
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
                        <el-button size="small" @click="handleEdit(scope.$index,scope.row)">编辑</el-button>
                        <el-button size="small" type="danger" @click="handleDelete(scope.$index,scope.row)">删除
                        </el-button>
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
                    <el-button size="small" type="primary" @click="back">返回</el-button>
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
                    <div v-on:mouseenter="mouserenter(question.id)" v-on:mouseleave="mouserleave(question.id)">
                        <div class="question-edit" v-if="selectId==question.id">
                            <el-button size="small" type="danger" plain style="float:right"
                                       @click="removeQuestion(question.id)">删除
                            </el-button>
                            <el-button size="small" type="primary" plain style="float:right"
                                       @click="setScore(question)">设置得分
                            </el-button>
                            <%--<el-button size="small" type="primary" plain style="float:right" @click="setSeq(question)">
                                设置顺序
                            </el-button>--%>
                        </div>
                        <div class="question-header">
                            <span><b>{{index+1}}.({{question.score}}分)</b></span>&nbsp;&nbsp;
                            <span v-html="question.question"></span>
                        </div>
                        <div class="exam-answer">
                            <el-row>
                                <el-col v-for="(item,index) in question.itemArr" :span="6">
                                    <span v-html="item.item"></span>
                                </el-col>
                            </el-row>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <el-dialog title="设置得分" :visible.sync="dialogFormVisible">
            <el-card>
                <el-input-number
                        v-model="question.score"
                        placeholder="请输入分值...">
                </el-input-number>
            </el-card>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" size="small" @click="submitScore">确定</el-button>
                <el-button @click="dialogFormVisible = false">取 消</el-button>
            </div>
        </el-dialog>
        <%--<el-dialog title="设置顺序" v-model="dialogFormVisible1">
            <el-card>
                <el-input-number
                        v-model="question.seq"
                        placeholder="请输入顺序..."
                        :min="1"
                        :max="questions.length">
                </el-input-number>
            </el-card>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" size="small" @click="submitSeq">确定</el-button>
                <el-button @click="dialogFormVisible1 = false">取 消</el-button>
            </div>
        </el-dialog>--%>

    </div>

</div>


<script type="text/javascript">

    var app = new Vue({
        el: '#app',
        data: {
            conditionVo: {gradeId: null, subjectId: null, currentPage: 1, pageSize: 10},
            dialogFormVisible: false,
            dialogFormVisible1: false,
            formLabelWidth: "120px",
            tableData: [],
            zujuansUrl: "<s:url value='/tiku/zujuan/list'/>",
            deleteUrl: "<s:url value='/tiku/zujuan/delete'/>",
            gradesUrl: "<s:url value='/tiku/dict/grades'/>",
            subjectsUrl: "<s:url value='/tiku/dict/subjects'/>",
            questionUpdateUrl: "<s:url value='/tiku/zujuan/questionUpdate'/>",
            questionDeleteUrl: "<s:url value='/tiku/zujuan/questionDelete'/>",
            questionListUrl: "<s:url value='/tiku/zujuan/listQuestion'/>",
            grades: [],
            subjects: [],
            currentPage: 1,
            pageSizes: [10],
            pageSize: [10],
            total: null,
            zujuans: [],
            zujuan: {},
            listFlag: true,//展示or编辑
            selectId: null,
            question: {},
            questions: []


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
            handleEdit: function (index, row) {
                this.listFlag = false;
                this.zujuan = row;
                this.getQuestionList(row.id);

            },
            back: function () {
                this.listFlag = true;
                this.zujuan = {};
                this.getZujuans();


            },
            handleDelete: function (index, row) {
                this.$confirm("确认删除吗？", "提示", {
                    confirmButtonText: '确认',
                    cancleButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    app.$http.get(app.deleteUrl, {params: {id: row.id}}).then(
                        function (response) {
                            app.getZujuans();
                            app.$message({type: 'info', message: '删除成功'})

                        },
                        function (response) {
                        }
                    );
                })
                    .catch(function () {
                        app.$message({type: 'info', message: '已取消删除'})
                    });

            },

            mouserenter: function (id) {
                this.selectId = id;
                document.getElementById(id).style.border = "1px solid #20a0ff";
            },
            mouserleave: function (id) {
                this.selectId = null;
                document.getElementById(id).style.border = "";
            },

            removeQuestion: function (id) {


                this.$confirm("确认删除吗？", "提示", {
                    confirmButtonText: '确认',
                    cancleButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    app.$http.get(app.questionDeleteUrl, {params: {id: id}}).then(
                        function (response) {
                            app.getQuestionList(app.zujuan.id);
                            app.$message({type: 'info', message: '删除成功'})

                        },
                        function (response) {
                        }
                    );
                })
                    .catch(function () {
                        app.$message({type: 'info', message: '已取消删除'})
                    });

            },

            setScore: function (question) {
                this.dialogFormVisible = true;
                this.question = question;
            },

            setSeq: function (question) {
                this.dialogFormVisible1 = true;
                this.question = question;


            },

            submitScore: function () {

                this.$http.post(this.questionUpdateUrl, this.question).then(
                    function (response) {
                        this.getQuestionList(this.zujuan.id);
                        this.question = {};
                        this.dialogFormVisible = false;
                        app.$message({type: 'info', message: '更新成功'})


                    },
                    function (response) {
                    }
                );
            },
            submitSeq: function () {
                this.$http.post(this.questionUpdateUrl, this.question).then(
                    function (response) {
                        this.getQuestionList(this.zujuan.id);
                        this.question = {};
                        this.dialogFormVisible1 = false;
                        app.$message({type: 'info', message: '更新成功'})


                    },
                    function (response) {
                    }
                );
            },


        }


    });


</script>


</body>
</html>
