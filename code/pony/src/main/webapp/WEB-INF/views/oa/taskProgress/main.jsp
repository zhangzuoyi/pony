<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>我的任务</title>
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
                        <b>我的任务</b>
                    </el-col>
                </el-row>
                <el-row>

                    <el-col :span="2">
                        <b>任务名称:</b>
                    </el-col>
                    <el-col :span="4">
                        <el-input v-model="conditionVo.taskName"/>
                    </el-col>
                    <el-col :span="2" >
                        <b>任务状态:</b>
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
                        <span>{{props.row.description}}</span>
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
                        prop="assigneeStr"
                        label="负责人"
                >
                </el-table-column>
                <el-table-column
                        prop="createUser"
                        label="创建人"
                >
                </el-table-column>
                <el-table-column
                        prop="startTimeStr"
                        label="开始时间"
                >
                </el-table-column>
                <el-table-column
                        prop="endTimeStr"
                        label="结束时间"
                >
                </el-table-column>
                <el-table-column
                        label="操作"
                        >
                    <template slot-scope="scope">
                        <el-button size="mini" @click="handleEdit(scope.$index,scope.row)">编辑</el-button>
                        <el-button size="mini" @click="handlePending(scope.$index,scope.row)">暂停</el-button>
                        <el-button size="mini" @click="handleFinish(scope.$index,scope.row)">完成</el-button>

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

        <el-dialog title="编辑任务" :visible.sync="dialogFormVisible">
            <el-form :model="task" >
                <el-form-item label="名称" :label-width="formLabelWidth" prop="name">
                    <el-input v-model="task.name" readonly></el-input>
                </el-form-item>
                <el-form-item label="详细信息" :label-width="formLabelWidth" prop="description" >
                    <el-input v-model="task.description" type="textarea" readonly></el-input>
                </el-form-item>
                <el-form-item label="保密级别" :label-width="formLabelWidth" prop="access">
                    <span v-if="task.access == 1">公开</span>
                    <span v-if="task.access == 0">私密</span>
                </el-form-item>
                <el-form-item label="负责人" :label-width="formLabelWidth" prop="assignee">
                    <el-input v-model="task.assigneeStr" readonly ></el-input>
                </el-form-item>
                <el-form-item label="时间" :label-width="formLabelWidth" >
                    <el-input  v-model="task.startTimeStr" prop="startTime" readonly></el-input >
                    --
                    <el-input  v-model="task.endTimeStr" prop="endTime" readonly></el-input >
                </el-form-item>
                <el-tabs type="border-card">
                    <el-tab-pane label="进展">
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
                        <el-form-item label="进展" :label-width="formLabelWidth" prop="description" >
                            <el-input v-model="content" type="textarea" ></el-input>
                        </el-form-item>

                        <div>
                            <el-row>
                                <el-col :offset="18" :span="6">
                                    <el-button size="mini" type="primary" @click="onSubmit()">添加进展</el-button>
                                    <el-button size="mini" type="primary" @click="dialogFormVisible = false">取 消</el-button>
                                </el-col>
                            </el-row>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="动态">
                        <el-steps direction="vertical">
                            <el-step v-for="item in taskProgress"  :description="item.content"></el-step>
                        </el-steps>
                    </el-tab-pane>
                    <el-tab-pane label="附件">
                        <div v-for="item in taskAttach"><a @click="downloadAttach(item.id)" >{{item.originalName}}</a></div>
                    </el-tab-pane>

                </el-tabs>


            </el-form>

        </el-dialog>






    </div>


</div>
<script type="text/javascript">

    var app = new Vue({
        el: '#app',
        data: {
            conditionVo: {currentPage: null, pageSize: 20, taskName: null,oaStatus:null},
            dialogFormVisible: false,
            formLabelWidth: "120px",
            tableData: [],
            tasksUrl: "<s:url value='/oa/task/listMy'/>",
            addUrl :"<s:url value='/oa/taskProgress/add'/>",
            addFileUrl :"<s:url value='/oa/taskProgress/addFile'/>",
            pendingUrl :"<s:url value='/oa/task/pending'/>",
            finishUrl :"<s:url value='/oa/task/finish'/>",
            taskProgressUrl :"<s:url value='/oa/taskProgress/getTaskProgress'/>",
            taskAttachUrl :"<s:url value='/oa/task/getTaskAttach'/>",
            currentPage: 1,
            pageSizes: [20],
            pageSize: [20],
            total: null,
            task: {},
            fileList:[],
            content:'',
            progressId : null,
            taskProgress:[],
            taskAttach:[],




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
            this.getTasks();


        },
        methods: {


            handleCurrentChange: function (val) {
                this.currentPage = val;
                this.conditionVo.currentPage = val;
                this.getTasks();
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
            },
            onSubmit:function () {

                this.$http.get(this.addUrl, {params:{taskId: this.task.id,content:this.content}}).then(
                    function (response) {
                        this.progressId = response.data;
                        this.content = '';
                        this.$refs.upload.submit();
                        this.task = {};
                        this.dialogFormVisible = false;
                        this.getTasks();

                    },
                    function (response) {
                    }
                );

            },
            handleEdit :function (index,row) {
                this.task = row;
                this.dialogFormVisible = true;
                this.getTaskProgress(row.id);//获取动态
                this.getTaskAttach(row.id);//获取附件
            },
            handlePending :function (index,row) {
                this.$confirm('确认暂停？','提示',{
                    confirmButtonText : '确认',
                    cancleButtonText : '取消',
                    type : 'warning',
                }).then(function(){
                    app.$http.get(app.pendingUrl, {params:{id:row.id}}).then(
                        function (response) {
                            app.getTasks();
                            this.$message({type:"info",message:"已暂停"});

                        },
                        function (response) {
                        }
                    );
                }).catch(function(){ });



            },
            handleFinish :function (index,row) {
                this.$confirm('确认完成？','提示',{
                    confirmButtonText : '确认',
                    cancleButtonText : '取消',
                    type : 'warning',
                }).then(function(){
                    app.$http.get(app.finishUrl, {params:{id:row.id}}).then(
                        function (response) {
                            app.getTasks();
                            this.$message({type:"info",message:"已完成"});

                        },
                        function (response) {
                        }
                    );
                }).catch(function(){ });

            },
            beforeUpload : function(file){

                var formData = new FormData();
                formData.append('fileUpload',file);
                formData.append('id',this.task.id);

                this.$http.post(this.addFileUrl,formData).then(
                    function(response){
                        this.clearFiles();
                        this.task = {};
                        this.progressId = null;
                        this.dialogFormVisible = false;
                        this.getTasks();
                    });
                return false;
            },
            clearFiles : function(){
                this.$refs.upload.clearFiles();
            },
            getTaskProgress : function (id) {
                this.$http.get(this.taskProgressUrl, {params:{id:id}}).then(
                    function (response) {
                        this.taskProgress = response.data;
                    },
                    function (response) {
                    }
                );
            },
            getTaskAttach : function (id) {
                this.$http.get(this.taskAttachUrl, {params:{id:id}}).then(
                    function (response) {
                        this.taskAttach = response.data;
                    },
                    function (response) {
                    }
                );
            },
            downloadAttach : function(id){
                window.location.href = "<s:url value='/oa/task/downloadAttach?id='/>"+ id;
            }



        }


    });

</script>
</body>
</html>
