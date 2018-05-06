<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>工作报告</title>
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
                        <b>工作报告</b>
                    </el-col>
                </el-row>
                <el-row>

                    <el-col :span="2">
                        <b>报告名称:</b>
                    </el-col>
                    <el-col :span="4">
                        <el-input v-model="conditionVo.workReportName"/>
                    </el-col>
                    <el-col :span="2" >
                        <b>报告状态:</b>
                    </el-col>
                    <el-col :span="4" >
                        <el-select v-model="conditionVo.oaStatus" placeholder="请选择" clearable>
                            <el-option  value="0" label="新建"></el-option>
                            <el-option  value="1" label="进行中"></el-option>
                            <el-option  value="2" label="已暂停"></el-option>
                            <el-option  value="3" label="已完成"></el-option>
                        </el-select>
                    </el-col>
                    <el-col :offset="8" :span="4">
                        <el-button type="primary" @click="search">查询</el-button>
                        <el-button type="primary" @click="addReport">新建报告</el-button>
                    </el-col>
                </el-row>
            </div>
            <el-table
                    ref="multipleTable"
                    :data="tableData"
                    border
                    style="width: 100%"
                    highlight-current-row
            >
                <%--<el-table-column
                        type="selection"
                        width="55"
                >--%>
                </el-table-column>
                <el-table-column type="expand">
                    <template slot-scope="props">
                        <span>{{props.row.content}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="名称"
                >
                </el-table-column>
                <el-table-column
                        label="状态"
                >
                    <template slot-scope="scope">{{scope.row.status | statusFilter }}</template>
                </el-table-column>
                <el-table-column
                        prop="auditorStr"
                        label="点评人"
                >
                </el-table-column>
                <el-table-column
                        prop="createUser"
                        label="创建人"
                >
                </el-table-column>
                <el-table-column
                        prop="startDateStr"
                        label="开始时间"
                >
                </el-table-column>
                <el-table-column
                        prop="endDateStr"
                        label="结束时间"
                >
                </el-table-column>
                <el-table-column
                        label="操作"
                        >
                    <template slot-scope="scope">
                        <%--<el-button size="mini" @click="handleEdit(scope.$indexmscope.row)">编辑</el-button>--%>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.$index,scope.row)">删除</el-button>
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

        <el-dialog title="新建报告" :visible.sync="dialogFormVisible">
            <el-form :model="workReport" :rules="rules">
                <el-form-item :label-width="formLabelWidth" prop="access">
                    <el-radio-group v-model="workReport.period">
                        <el-radio :label="0">日报</el-radio>
                        <el-radio :label="1">周报</el-radio>
                        <el-radio :label="2">月报</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="名称" :label-width="formLabelWidth" prop="name">
                    <el-input v-model="workReport.name"></el-input>
                </el-form-item>
                <el-form-item :label-width="formLabelWidth" prop="access">
                    <el-radio-group v-model="workReport.access">
                        <el-radio :label="1">公开</el-radio>
                        <el-radio :label="0">私密</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="时间" :label-width="formLabelWidth" >
                    <el-date-picker v-model="workReport.startDate" prop="startDate"></el-date-picker>
                    --
                    <el-date-picker v-model="workReport.endDate" prop="endDate"></el-date-picker>
                </el-form-item>
                <el-form-item label="详细信息" :label-width="formLabelWidth" prop="description" >
                    <el-input v-model="workReport.content" type="textarea" ></el-input>
                </el-form-item>
                <el-form-item label="报告人" :label-width="formLabelWidth" prop="reporter">
                    <el-input v-model="workReport.reporter" readonly ><el-button slot="append" icon="el-icon-plus" @click="openTransfer('reporter')"></el-button></el-input>
                </el-form-item>
                <el-form-item label="点评人" :label-width="formLabelWidth" prop="auditor">
                    <el-input v-model="workReport.auditor" readonly ><el-button slot="append" icon="el-icon-plus" @click="openTransfer('auditor')"></el-button></el-input>
                </el-form-item>

                <el-form-item label="附件" :label-width="formLabelWidth">
                <el-upload
                        action="<s:url value='/examAdmin/examinee/fileUpload'/>"
                        ref="upload"
                        name="fileUpload"
                        :before-upload="beforeUpload"
                        :file-list="fileList"
                        :auto-upload="false"
                >
                    <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                    <el-button style="margin-left:10px;" size="small" type="primary" @click="clearFiles">清空文件</el-button>
                </el-upload>
                </el-form-item>
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
            conditionVo: {currentPage: null, pageSize: 20, workReportName: null,oaStatus:null},
            dialogFormVisible: false,
            dialogFormVisible2:false,
            formLabelWidth: "120px",
            tableData: [],
            workReportsUrl: "<s:url value='/oa/workReport/list'/>",
            deleteUrl: "<s:url value='/oa/workReport/delete'/>",
            addUrl: "<s:url value='/oa/workReport/add'/>",
            updateUrl: "<s:url value='/oa/workReport/update'/>",
            teachersUrl: "<s:url value='/teacherAdmin/list'/>",
            addFileUrl : "<s:url value='/oa/workReport/addFile'/>",
            currentPage: 1,
            pageSizes: [20],
            pageSize: [20],
            total: null,
            workReport: {},
            rules: {
                name: [
                    { required: true, message: '请输入报告名称', trigger: 'blur' },
                ],
                content: [
                    { required: true, message: '请输入报告内容', trigger: 'blur' },
                ],
                startDate: [
                    { required: true, message: '请输入开始时间', trigger: 'change' },
                ],
                reporter: [
                    { required: true, message: '请选择报告人', trigger: 'change' }
                ]
            },
            candidate:[],
            selected:[],
            type:'',
            fileList:[],
            reportId : null



        },
        filters: {
            statusFilter: function (value) {
                if(value == 0){return "新建"; }
                if(value == 1){return "进行中"; }
                if(value == 2){return "已暂停"; }
                if(value == 3){return "已完成"; }
            }
        },

        mounted: function () {
            this.getWorkReport();
            this.getCandidate();


        },
        methods: {

            filterMethod:function (query, item) {
                    return item.label.indexOf(query) > -1;

            },
            handleCurrentChange: function (val) {
                this.currentPage = val;
                this.conditionVo.currentPage = val;
                this.getWorkReport();
            },

            getWorkReport: function () {
                this.$http.post(this.workReportsUrl, this.conditionVo).then(
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
                this.getWorkReport();
            },
            onSubmit:function () {

                this.$http.post(this.addUrl, this.workReport).then(
                    function (response) {
                        this.reportId = response.data;
                        this.$refs.upload.submit();
                        this.workReport = {};
                        this.dialogFormVisible = false;
                        this.getWorkReport();

                    },
                    function (response) {
                    }
                );

            },
            addReport:function () {
                this.dialogFormVisible = true;
            },
            openTransfer:function (type) {
                if(type == 'auditor'){
                  this.selected = this.workReport.auditor;
                }
                if(type == 'reporter'){
                    this.selected = this.workReport.reporter;
                }
                this.type=type;
                this.dialogFormVisible2 = true;
            },
            closeTransfer:function (type) {
                if(type == 'auditor'){
                    this.workReport.auditor = this.selected;
                }
                if(type == 'reporter'){
                    this.workReport.reporter = this.selected;
                }
            },
            /*handleEdit :function (index,row) {

            },*/
            handleDelete :function (index,row) {
                this.$http.get(this.deleteUrl, {params:{id:row.id}}).then(
                    function (response) {
                        this.getWorkReport();
                    },
                    function (response) {
                    }
                );
            },
            beforeUpload : function(file){

                var formData = new FormData();
                formData.append('fileUpload',file);
                formData.append('id',this.reportId);

                this.$http.post(this.addFileUrl,formData).then(
                    function(response){
                        this.clearFiles();
                        this.workReport = {};
                        this.reportId = null;
                        this.dialogFormVisible = false;
                        this.getWorkReport();
                    });
                return false;
            },
            clearFiles : function(){
                this.$refs.upload.clearFiles();
            },



        }


    });

</script>
</body>
</html>
