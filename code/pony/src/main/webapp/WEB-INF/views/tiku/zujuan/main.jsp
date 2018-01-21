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

        <div v-if="listFlag" >
            <el-card class="box-card content-margin">
                <div slot="header" class="clearfix">
                    <el-row>
                        <el-col :span="4">
                            <b>组卷管理</b>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="2" >
                            <b>年级:</b>
                        </el-col>
                        <el-col :span="4" >
                            <el-select v-model="conditionVo.gradeId" placeholder="请选择" clearable>
                                <el-option v-for="x in grades" :label="x.name" :value="x.id"></el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="4" >
                            <el-select v-model="conditionVo.subjectId" placeholder="请选择" clearable>
                                <el-option v-for="x in subjects" :label="x.name" :value="x.id"></el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="4" >
                            <el-button type="primary"  @click="getZujuans">查询</el-button>
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
                            <el-button size="small" type="danger" @click="handleDelete(scope.$index,scope.row)">删除</el-button>
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
        <div >
            <div class="header" style="height: 50px;">
                <el-row>
                    <el-col :offset="6" :span="12" style="text-align: center;font-size: 20px"><b>aaaa{{zujuan.name}}</b></el-col>
                </el-row>
            </div>
            <div class="content">
                <div v-for="question in zujuan.questions">

                </div>
            </div>
        </div>
    </div>

</div>


    <script type="text/javascript">

        var app = new Vue({
            el: '#app',
            data: {
                conditionVo: {gradeId: null,subjectId:null, currentPage: 1, pageSize: 10},
                dialogFormVisible: false,
                formLabelWidth: "120px",
                tableData: [],
                zujuansUrl: "<s:url value='/tiku/zujuan/list'/>",
                deleteUrl:"<s:url value='/tiku/zujuan/delete'/>",
                currentPage: 1,
                pageSizes: [10],
                pageSize: [10],
                total: null,
                zujuans: [],
                zujuan: {},
                listFlag : false,//展示or编辑




            },
            /*filters: {
             userTypeFilter: function (value) {
             if(value == 't'){return "老师"; }
             if(value == 's'){return "学生"; }
             }
             }	,*/

            mounted: function () {
               // this.getZujuans();


            },
            methods: {


                handleCurrentChange: function (val) {
                    this.conditionVo.currentPage = val;
                    this.getQuestions();
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
                handleEdit : function(index,row){
                    /* this.title="修改用户";
                     this.dialogFormVisible = true;
                     this.user = row;
                     this.addOrUpdate = false;*/
                },
                handleDelete : function(index,row){
                    this.$confirm("确认删除吗？","提示",{
                        confirmButtonText:'确认',
                        cancleButtonText:'取消',
                        type:'warning'
                    }).then(function(){
                        app.$http.get(app.deleteUrl,{params:{id:row.id}}).then(
                            function(response){
                                app.getZujuans();
                            },
                            function(response){}
                        );
                    })
                        .catch(function(){ app.$message({ type:'info',message:'已取消删除'})});

                }

            }


        });


    </script>




</body>
</html>
