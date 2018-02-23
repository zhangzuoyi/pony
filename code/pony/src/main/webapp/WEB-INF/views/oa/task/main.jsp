<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>任务管理</title>
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
                        <b>任务管理</b>
                    </el-col>
                </el-row>
                <el-row>
                    <%--<el-col :span="2" >
                        <b>任务状态:</b>
                    </el-col>
                    <el-col :span="4" >
                        <el-select v-model="userType" placeholder="请选择" clearable>
                            <el-option v-for="x in userTypes" :label="x.name" :value="x.type"></el-option>
                        </el-select>
                    </el-col>--%>
                    <el-col :span="2">
                        <b>任务名称:</b>
                    </el-col>
                    <el-col :span="4">
                        <el-input v-model="conditionVo.taskName"/>
                    </el-col>
                    <el-col :offset="14" :span="4">
                        <el-button type="primary" @click="search">查询</el-button>
                        <el-button type="primary" @click="addTask">新建任务</el-button>

                        <!-- <el-button type="primary"  @click="showAdd">新增</el-button> -->
                    </el-col>
                </el-row>
            </div>
            <el-table
                    ref="multipleTable"
                    :data="tableData"
                    border
                    style="width: 100%"
                    highlight-current-row
                    @selection-change="handleSelectionChange"
            >
                <el-table-column
                        type="selection"
                        width="55"
                >
                </el-table-column>
               <%-- <el-table-column type="expand">
                    <template slot-scope="props">
                        <span>{{props.row.description}}</span>
                    </template>
                </el-table-column>--%>
                <el-table-column
                        prop="name"
                        label="名称"
                >
                </el-table-column>
                <el-table-column
                        prop="status"
                        label="状态"
                >
                </el-table-column>
                <el-table-column
                        prop="assignee"
                        label="负责人"
                >
                </el-table-column>
                <el-table-column
                        prop="createUser"
                        label="创建人"
                >
                </el-table-column>
                <el-table-column
                        prop="startTime"
                        label="开始时间"
                >
                </el-table-column>
                <el-table-column
                        prop="endTime"
                        label="结束时间"
                >
                </el-table-column>
                <%--<el-table-column
                		inline-template
                        label="用户类型"
                        >
                        <div>{{row.userType | userTypeFilter }}</div>
                </el-table-column>--%>

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

        <el-dialog title="新建任务" :visible.sync="dialogFormVisible">
            <el-form :model="task" :rules="rules">
                <el-form-item label="名称" :label-width="formLabelWidth" prop="name">
                    <el-input v-model="task.name"></el-input>
                </el-form-item>
                <el-form-item label="详细信息" :label-width="formLabelWidth" prop="description" >
                    <el-input v-model="task.description" type="textarea" ></el-input>
                </el-form-item>
                <el-form-item :label-width="formLabelWidth" prop="access">
                    <el-radio-group v-model="task.access">
                        <el-radio :label="1">公开</el-radio>
                        <el-radio :label="0">私密</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="负责人" :label-width="formLabelWidth" prop="assignee">
                    <el-input v-model="task.assignee" readonly ><el-button slot="append" icon="el-icon-plus" @click="openTransfer('assignee')"></el-button></el-input>
                </el-form-item>
                <el-form-item label="时间" :label-width="formLabelWidth" >
                    <el-time-picker v-model="task.startTime" prop="startTime"></el-time-picker>
                    --
                    <el-time-picker v-model="task.endTime" prop="endTime"></el-time-picker>
                </el-form-item>
                <el-form-item label="任务成员" :label-width="formLabelWidth" prop="members">
                    <el-input v-model="task.members" readonly ><el-button slot="append" icon="el-icon-plus" @click="openTransfer('members')"></el-button></el-input>
                </el-form-item>
                <el-form-item label="抄送人" :label-width="formLabelWidth" prop="endTime">
                    <el-input v-model="task.cc" readonly><el-button slot="append" icon="el-icon-plus" @click="openTransfer('cc')"></el-button></el-input>
                </el-form-item>
                <%--<el-form-item label="标签" :label-width="formLabelWidth" prop="tags">
                    <el-input v-model="task.tags" ></el-input>
                </el-form-item>--%>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="onSubmit()">确定</el-button>
                <el-button @click="dialogFormVisible = false">取 消</el-button>
            </div>
        </el-dialog>

        <el-dialog title="选择人员" :visible.sync="dialogFormVisible2">
            <el-transfer
                    filterable
                    :filter-method="filterMethod"
                    filter-placeholder="请输入人员姓名"
                    v-model="selected"
                    :data="candidate">
            </el-transfer>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible2 = false;closeTransfer(type)">确定</el-button>
            </div>
        </el-dialog>


    </div>


</div>
<script type="text/javascript">

    var app = new Vue({
        el: '#app',
        data: {
            conditionVo: {currentPage: null, pageSize: 20, taskName: null},
            dialogFormVisible: false,
            dialogFormVisible2:false,
            formLabelWidth: "120px",
            tableData: [],
            tasksUrl: "<s:url value='/oa/task/list'/>",
            deleteUrl: "<s:url value='/oa/task/delete'/>",
            addUrl: "<s:url value='/oa/task/add'/>",
            updateUrl: "<s:url value='/oa/task/update'/>",
            teachersUrl: "<s:url value='/teacherAdmin/list'/>",
            currentPage: 1,
            pageSizes: [20],
            pageSize: [20],
            total: null,
            task: {},
            multipleSelection: [],
            rules: {
                name: [
                    { required: true, message: '请输入任务名称', trigger: 'blur' },
                ],
                assignee: [
                    { required: true, message: '请选择负责人', trigger: 'change' }
                ]
            },
            candidate:[],
            selected:[],
            type:'',


        },
        filters: {

        },

        mounted: function () {
            this.getTasks();
            this.getCandidate();

            //this.dialogFormVisible2 = false;//解决el-dialog中的el-tree第一次的动态渲染问题

        },
        methods: {

            filterMethod:function (query, item) {
                    return item.label.indexOf(query) > -1;

            },
            handleCurrentChange: function (val) {
                this.currentPage = val;
                this.conditionVo.currentPage = val;
                this.getTasks();
            },
            handleSelectionChange:function(val) {
                this.multipleSelection = val;
            },
            getTasks: function () {
                this.$http.post(this.tasksUrl, this.conditionVo).then(
                    function (response) {
                        this.tableData = response.data.content;
                        this.total = response.data.totalElements;
                    },
                    function (response) {
                    }
                );
            },
            getCandidate:function () {
                this.$http.get(this.teachersUrl).then(
                    function (response) {

                        for(var idx in response.data){
                            this.candidate.push({
                                key: response.data[idx].name,//使用名字作为唯一标识
                                label: response.data[idx].name,
                                disabled: false
                            });
                        }

                    },
                    function (response) {
                    }
                );
            },
            search: function () {
                this.currentPage = 1;
                this.getTasks();
            },
            submit:function () {

            },
            addTask:function () {
                this.dialogFormVisible = true;
            },
            openTransfer:function (type) {
                if(type == 'assignee'){
                  this.selected = this.task.assignee;
                }
                if(type == 'members'){
                    this.selected = this.task.members;
                }
                if(type == 'cc'){
                    this.selected = this.task.cc;
                }
                this.type=type;
                this.dialogFormVisible2 = true;
            },
            closeTransfer:function (type) {
                if(type == 'assignee'){
                    this.task.assignee = this.selected;
                }
                if(type == 'members'){
                    this.task.members = this.selected;
                }
                if(type == 'cc'){
                    this.task.cc = this.selected;
                }
            },
            add :function () {
                
            }


        }


    });

</script>
</body>
</html>
