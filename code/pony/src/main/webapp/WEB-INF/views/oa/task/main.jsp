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
                    <el-col :span="4">
                        <el-button type="primary" @click="search">查询</el-button>
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
                <el-table-column type="expand">
                    <template slot-scope="props">
                        <span>{{props.row.text}}</span>
                    </template>
                </el-table-column>
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

        <el-dialog title="新建任务" v-model="dialogFormVisible">
            <el-form :model="task">
                <el-form-item label="名称" :label-width="formLabelWidth">
                    <el-input v-model="task.name" type="password" auto-complete="off" required></el-input>
                </el-form-item>
                <el-form-item label="详细信息" :label-width="formLabelWidth">
                    <el-input v-model="task.description" type="password" auto-complete="off" required></el-input>
                </el-form-item>
                <el-form-item :label-width="formLabelWidth">
                    <el-radio-group v-model="task.access">
                        <el-radio :label="1">公开</el-radio>
                        <el-radio :label="0">私密</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="负责人" :label-width="formLabelWidth">
                    <el-input v-model="task.assignee" auto-complete="off" required></el-input>
                </el-form-item>
                <el-form-item label="时间" :label-width="formLabelWidth">
                    <el-time-picker v-model="task.startTime"></el-time-picker>
                    --
                    <el-time-picker v-model="task.endTime"></el-time-picker>
                </el-form-item>
                <el-form-item label="任务成员" :label-width="formLabelWidth">
                    <el-input v-model="task.assignee" auto-complete="off" required></el-input>
                </el-form-item>
                <el-form-item label="抄送人" :label-width="formLabelWidth">
                    <el-input v-model="task.assignee" auto-complete="off" required></el-input>
                </el-form-item>
                <el-form-item label="标签" :label-width="formLabelWidth">
                    <el-input v-model="task.tags" auto-complete="off" required></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="onSubmit()">确定</el-button>
                <el-button @click="dialogFormVisible = false">取 消</el-button>
            </div>
        </el-dialog>

        <el-dialog title="设置角色" v-model="dialogFormVisible2">
            <el-tree
                    :data="treeData"
                    :props="props"
                    show-checkbox
                    node-key="roleCode"
                    highlight-current
                    default-expand-all
                    ref="tree"
            >
            </el-tree>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submit">确定</el-button>
                <el-button @click="dialogFormVisible2 = false">取 消</el-button>
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
            formLabelWidth: "120px",
            tableData: [],
            tasksUrl: "<s:url value='/oa/task/list'/>",
            deleteUrl: "<s:url value='/oa/task/delete'/>",
            addUrl: "<s:url value='/oa/task/add'/>",
            updateUrl: "<s:url value='/oa/task/update'/>",
            treeData: [],
            props: {
                children: 'children',
                label: 'label'
            },
            currentPage: 1,
            pageSizes: [20],
            pageSize: [20],
            total: null,
            task: {}


        },
        filters: {
            userTypeFilter: function (value) {
                if (value == 't') {
                    return "老师";
                }
                if (value == 's') {
                    return "学生";
                }
            }
        },

        mounted: function () {
            this.getTasks();

            //this.dialogFormVisible2 = false;//解决el-dialog中的el-tree第一次的动态渲染问题

        },
        methods: {


            handleCurrentChange: function (val) {
                this.currentPage = val;
                this.conditionVo.currentPage = val;
                this.getTasks();
            },


            getRoleTree: function () {
                this.$http.get(this.roleTreeUrl).then(
                    function (response) {
                        var firstNode = {label: '根节点', children: response.data.treeData};
                        this.treeData.push(firstNode);
                        //this.treeData = [{label:'知识库',children : [{"id":1,"resKey":"sys_admin","children":[],"label":"系统管理","presId":0,"resLevel":1}]}];
                    },
                    function (response) {
                    }
                );
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


            search: function () {
                this.currentPage = 1;
                this.getTasks();
            }


        }


    });

</script>
</body>
</html>
