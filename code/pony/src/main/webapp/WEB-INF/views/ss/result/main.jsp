<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>选课结果</title>
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
    <script type="text/javascript" src="<s:url value='/static/js/moment.min.js' />"></script>
    <script type="text/javascript" src="<s:url value='/static/vue/myfilters.js' />"></script>
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
                        <b>选课结果</b>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="5">
                        <div class="grid-content ">学年：<b>{{currentSchoolYear.name}}</b></div>
                    </el-col>
                    <el-col :span="5">
                        <div class="grid-content">学期：<b>{{currentTerm.name}}</b></div>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="5">
                        <div class="grid-content bg-purple">
                            年级:
                            <el-select v-model="conditionVo.gradeId" clearable @change="getClasses(conditionVo.gradeId)"
                                       filterable placeholder="请选择..">
                                <el-option
                                        v-for="grade in grades"
                                        :label="grade.name"
                                        :value="grade.gradeId">
                                    <span style="float: left">{{grade.name}}</span>
                                </el-option>
                            </el-select>
                        </div>
                    </el-col>
                    <el-col :span="5">
                        <div class="grid-content bg-purple">
                            班级:
                            <el-select v-model="conditionVo.classId" clearable filterable placeholder="请选择..">
                                <el-option
                                        v-for="schoolClass in classes"
                                        :label="schoolClass.name"
                                        :value="schoolClass.classId">
                                    <span style="float: left">{{schoolClass.name}}</span>
                                </el-option>
                            </el-select>
                        </div>
                    </el-col>
                    <el-col :span="5">
                        <div class="grid-content bg-purple">
                            学生:
                            <el-select v-model="conditionVo.studentId" clearable filterable placeholder="请选择..">

                                <el-option
                                        v-for="student in students"
                                        :label="student.name"
                                        :value="student.studentId">
                                    <span style="float: left">{{student.name}}</span>
                                </el-option>
                            </el-select>
                        </div>
                    </el-col>


                    <el-col :span="5">
                        <div class="grid-content bg-purple">
                            状态:
                            <el-select v-model="conditionVo.status"   placeholder="请选择..">
                                <el-option label="已选" value="true"></el-option>
                                <el-option label="未选" value="false"></el-option>
                            </el-select>
                        </div>
                    </el-col>
                    <el-col  :span="4">
                    <el-button type="primary" @click="listByCondition">查询</el-button>
                    <el-button type="primary" @click="exportData">导出</el-button>
                    </el-col>
                </el-row>



            </div>
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%"
                    highlight-current-row>
                <el-table-column
                        prop="studentName"
                        label="姓名"
                >
                </el-table-column>
                <el-table-column
                        prop="className"
                        label="班级"
                >
                </el-table-column>
                <el-table-column
                        prop="groupName"
                        label="类别"
                        show-overflow-tooltip="true"
                >
                </el-table-column>

            </el-table>
        </el-card>


    </div>
</div>

<script type="text/javascript">
    var app = new Vue({
        el: '#app',
        data: {
            conditionVo: {gradeId: null,classId:null,studentId:null,status:'true'},
            tableData: [],
            grades: [],
            classes: [],
            students: [],
            currentSchoolYear: {},
            currentTerm: {},
            currentSchoolYearUrl: "<s:url value='/schoolYear/getCurrent'/>",
            currentTermUrl: "<s:url value='/term/getCurrent'/>",
            gradesUrl: "<s:url value='/grade/list'/>",
            classesUrl: "<s:url value='/schoolClass/findByYearAndGrade'/>",
            studentsUrl: "<s:url value='/studentAdmin/list'/>",
            listUrl: "<s:url value='/ss/result/listByCondition'/>"


        },

        mounted: function () {
            this.getCurrentSchoolYear();
            this.getCurrentTerm();
            this.getStudents();
            this.getGrades();
        },
        methods: {
            getCurrentSchoolYear: function () {
                this.$http.get(this.currentSchoolYearUrl).then(
                    function (response) {
                        this.currentSchoolYear = response.data;
                    },
                    function (response) {
                    }
                );
            },
            getCurrentTerm: function () {
                this.$http.get(this.currentTermUrl).then(
                    function (response) {
                        this.currentTerm = response.data;
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
            getStudents: function () {
                this.$http.get(this.studentsUrl).then(
                    function (response) {
                        this.students = response.data;
                    },
                    function (response) {
                    }
                );
            },
            getClasses: function (gradeId) {

                this.conditionVo.classId = null;

                this.$http.get(this.classesUrl, {
                    params: {
                        yearId: this.currentSchoolYear.yearId,
                        gradeId: gradeId
                    }
                }).then(
                    function (response) {
                        this.classes = response.data;
                    },
                    function (response) {
                    }
                );
            },
            listByCondition: function () {
                this.$http.post(this.listUrl, this.conditionVo).then(
                    function (response) {
                        this.tableData = response.data;
                    },
                    function (response) {
                    }
                );
            },
            exportData: function () {
                window.location.href = "<s:url value='/ss/result/export' />";
            }


        }


    });

</script>
</body>
</html>