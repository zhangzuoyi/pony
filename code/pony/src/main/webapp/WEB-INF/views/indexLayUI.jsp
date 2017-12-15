<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
	<title>管理系统</title>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1">
    <link rel="stylesheet" href="<s:url value='/static/layui/index.css' />">
    <link rel="stylesheet" href="<s:url value='/static/layui/css/layui.css' />">
</head>
<body>
<div class="layui-layout-admin fly-body">
    <div class="layui-header">
        <div class="admin-title dp-ib">
            <h1 class="fs-24 dp-ib c-fff mgl-20 mgt-10">管理系统</h1></div>
        <%--<div class="layui-top-nav dp-ib ve-t">
            <ul class="layui-nav">
                <li class="layui-nav-item">
                    <a href="javascript:;">顶部菜单1</a></li>
                <li class="layui-nav-item layui-this">
                    <a href="javascript:;">顶部菜单2</a></li>
                <li class="layui-nav-item">
                    <a href="javascript:;">顶部菜单3</a></li>
                <li class="layui-nav-item">
                    <a href="javascript:;">顶部菜单4</a>
                    <dl class="layui-nav-child">
                        <dd>
                            <a href="javascript:;">顶部菜单4-1</a></dd>
                        <dd>
                            <a href="javascript:;">顶部菜单4-2</a></dd>
                        <dd>
                            <a href="javascript:;">顶部菜单4-3</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">顶部菜单5</a></li>
            </ul>
        </div>--%>
        <div href="#!" class="layui-right user">
            <a href="#!" class="user-avatar">
                <img src="<s:url value='/static/layui/images/user-photo.jpg' />" alt="" class="layui-circle">
                <span class="c-fff mgl-20"><strong ><shiro:principal /></strong></span></a>
            <ul class="user-nav">
                <%--<li class="user-nav-item">
                    <a href="#!">
                        <i class="layui-icon">&#xe612;</i>个人设置</a></li>--%>
                    <li class="user-nav-item">
                        <a href="javascript:;" data-url="<s:url value='personalInfo/main' />" id="btn-personalInfo"><i class="layui-icon">&#xe612;</i>个人资料</a></li>
                    <li class="user-nav-item">
                    <a href="<s:url value='logout' />" id="btn-exit">
                        <i class="layui-icon">&#xe623;</i>退出</a></li>

            </ul>
        </div>
    </div>
    <div class="layui-side">
        <ul class="layui-nav layui-nav-tree" id="sideNav" lay-filter="sideNav">
            <shiro:hasPermission name="sys_admin">
            <li class="layui-nav-item layui-nav-itemed">
                <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe614;</i>系统管理</a>
                <dl class="layui-nav-child">
                    <shiro:hasPermission name="schoolyear_admin">
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/schoolYear/main/' />"><label>学年管理</label></a>
                    </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="schoolyear_admin">
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/term/main/' />"><label>学期管理</label></a>
                    </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="grade_admin">
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/grade/main/' />"><label>年级管理</label></a>
                    </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="class_admin">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/schoolClass/main/' />"><label>班级管理</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="subject_admin">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/subject/main/' />"><label>科目管理</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="teacher_admin">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/teacherAdmin/main/' />"><label>教师管理</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="dict_admin">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/commonDict/main/' />"><label>通用字典管理</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="resource_admin">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/resourceAdmin/main/' />"><label>资源管理</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="role_admin">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/roleAdmin/main/' />"><label>角色管理</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="user_admin">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/user/main/' />"><label>用户管理</label></a>
                        </dd>
                    </shiro:hasPermission>
                </dl>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="score_analysis">
            <li class="layui-nav-item">
                <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe629;</i>成绩分析</a>
                <dl class="layui-nav-child">
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examResultRank/main/' />"><label>成绩排名管理</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/studentComprehensiveTrack/main/' />"><label>学生综合成绩追踪</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/studentSingleTrack/main/' />"><label>学生单科成绩追踪</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/classComprehensiveCompare/main/' />"><label>班级综合成绩对比</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/classSingleCompare/main/' />"><label>班级单科成绩对比</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/comprehensiveRank/main/' />"><label>学生整体成绩分析</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examAdmin/average/main/' />"><label>均量值</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examAdmin/average/main2/' />"><label>均量值(文件)</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examAdmin/average/mainAssign/' />"><label>7选3赋分</label></a>
                    </dd>
                </dl>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="student_status">
            <li class="layui-nav-item">
                <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe63c;</i>学籍管理</a>
                <dl class="layui-nav-child">
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/studentAdmin/entrance/' />"><label>入学管理</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/studentAdmin/main/' />"><label>学生管理</label></a>
                    </dd><dd>
                    <a href="javascript:;" data-url="<s:url value='/studentRemark/main/' />"><label>学生评语管理</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/prizePunish/main/' />"><label>学生奖惩管理</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/upgrade/main/' />"><label>升级管理</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/studentCard/main/' />"><label>学籍卡</label></a>
                    </dd>

                </dl>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="exam_admin">
            <li class="layui-nav-item">
                <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe62a;</i>考试管理</a>
                <dl class="layui-nav-child">
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/exam/main/' />"><label>考试管理</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examAdmin/examRoom/main/' />"><label>考场管理</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examAdmin/examArrange/main' />"><label>考试安排</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examAdmin/examRoomArrange/main/' />"><label>考场设置</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examAdmin/examineeArrange/main/' />"><label>考生设置</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examAdmin/examineeRoomArrange/main/' />"><label>自动考场安排</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examAdmin/classExamineeRoomArrange/main/' />"><label>班级考生安排</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examAdmin/roomExamineeRoomArrange/main/' />"><label>考场考生安排</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examAdmin/subjectExamineeRoomArrange/main/' />"><label>科目考生安排</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examAdmin/monitorArrange/main/' />"><label>监考设置</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examAdmin/monitorArrange/resultQuery/' />"><label>监考查询</label></a>
                    </dd>
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/examResult/main/' />"><label>成绩管理</label></a>
                    </dd>
                </dl>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="lesson_arrange">
            <li class="layui-nav-item" >
                <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe630;</i>排课管理</a>
                <dl class="layui-nav-child">
                    <shiro:hasPermission name="lesson_period">
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/lessonPeriod/main/' />"><label>上课时段管理</label></a>
                    </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="teacher_lesson_arrange">
                    <dd>
                        <a href="javascript:;" data-url="<s:url value='/teacherLessonArrange/main/' />"><label>老师任课安排</label></a>
                    </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="teacher_lesson_list">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/teacherLesson/main/' />"><label>老师任课列表</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="week_lesson_admin">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/weekLessonAdmin/main/' />"><label>星期上课设置</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="grade_no_course">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/gradeNoCourse/main/' />"><label>年级不排课设置</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="class_no_course">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/classNoCourse/main/' />"><label>班级不排课设置</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="teacher_no_course">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/teacherNoCourse/main/' />"><label>老师不排课设置</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="subject_no_course">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/subjectNoCourse/main/' />"><label>科目不排课设置</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="arrange_combine">
                    	<dd>
                            <a href="javascript:;" data-url="<s:url value='/arrangeCombine/main/' />"><label>合班设置</label></a>
                        </dd>
                	</shiro:hasPermission>
                    <shiro:hasPermission name="arrange_rotation">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/arrangeRotation/main/' />"><label>走班设置</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="prelesson_arrange">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/preLessonArrange/main/' />"><label>预排</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="auto_lesson_arrange">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/autoLessonArrange/main/' />"><label>自动排课</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="relesson_arrange">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/reLessonArrange/main/' />"><label>调课</label></a>
                        </dd>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="teacher_course">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/teacherCourse/main/' />"><label>教师课表</label></a>
                        </dd>
                    </shiro:hasPermission>
                </dl>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="lesson_select">
                <li class="layui-nav-item">
                    <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe637;</i>选课管理</a>
                    <dl class="layui-nav-child">
                        <%-- <dd>
                            <a href="javascript:;" data-url="<s:url value='/lessonSelectArrange/main/' />"><label>可选课程设置</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/lessonSelect/main/' />"><label>学生选课</label></a>
                        </dd> --%>
                        <dd>
	                        <a href="javascript:;" data-url="<s:url value='/ss/config/main/' />"><label>科目选择配置</label></a>
	                    </dd>
                        <%-- <dd>
                            <a href="javascript:;" data-url="<s:url value='/ss/select/main/' />"><label>科目选择</label></a>
                        </dd> --%>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/ss/statistics/main/' />"><label>选课统计</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/ss/admin/main/' />"><label>选课管理</label></a>
                        </dd>
                    </dl>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="attendance">
                <li class="layui-nav-item">
                    <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe62d;</i>考勤管理</a>
                    <dl class="layui-nav-child">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/attendance/attendance/main/' />"><label>考勤打卡</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/attendance/attendance/my/' />"><label>考勤记录</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/attendance/leave/applyMain/' />"><label>请假</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/attendance/leave/tasksMain/' />"><label>请假待办任务</label></a>
                        </dd>
                    </dl>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="message">
                <li class="layui-nav-item">
                    <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe63a;</i>消息管理</a>
                    <dl class="layui-nav-child">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/userGroup/main/' />"><label>用户组</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/message/messageSend/main/' />"><label>消息发送</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/message/messageReceive/main/' />"><label>消息接收</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/message/messageSent/main/' />"><label>已发送消息</label></a>
                        </dd>
                    </dl>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="property">
                <li class="layui-nav-item">
                    <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe600;</i>资产管理</a>
                    <dl class="layui-nav-child">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/property/propertyType/main/' />"><label>资产分类</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/property/department/main/' />"><label>部门管理</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/property/add/main/' />"><label>新增资产</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/property/propertyAdmin/main/' />"><label>资产管理</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/property/addConsumable/main/' />"><label>新增损耗品</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/property/consumableAdmin/main/' />"><label>损耗品管理</label></a>
                        </dd>
                    </dl>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="disk">
                <li class="layui-nav-item">
                    <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe61d;</i>网盘</a>
                    <dl class="layui-nav-child">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/disk/disk-info-list' />"><label>我的文件</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/disk/disk-share-list' />"><label>我的分享</label></a>
                        </dd>
                        <%-- <dd>
                            <a href="javascript:;" data-url="<s:url value='/disk/trash/' />"><label>回收站</label></a>
                        </dd> --%>
                    </dl>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="evaluation">
                <li class="layui-nav-item">
                    <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe605;</i>评价管理</a>
                    <dl class="layui-nav-child">
                    	<shiro:hasPermission name="myoutcome">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/evaluation/outcome/userMain' />"><label>我的成果</label></a>
                        </dd>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outcome_check">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/evaluation/outcome/checkMain' />"><label>成果审核</label></a>
                        </dd>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="evl_subjects">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/evaluation/config/main' />"><label>评价主题管理</label></a>
                        </dd>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="evl_teacher_dep">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/evaluation/make/main' />?subjectId=1"><label>教师发展性评价</label></a>
                        </dd>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="evl_teacher_dep_check">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/evaluation/make/checkMain' />?subjectId=1"><label>教师发展性评价审核</label></a>
                        </dd>
                        </shiro:hasPermission>
                    </dl>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="tour">
                <li class="layui-nav-item">
                    <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe610;</i>巡课管理</a>
                    <dl class="layui-nav-child">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/tour/main/' />"><label>巡课管理</label></a>
                        </dd>
                    </dl>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="teacher_page">
                <li class="layui-nav-item">
                    <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe612;</i>任课管理</a>
                    <dl class="layui-nav-child">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/teacher/courses/' />"><label>我的任课</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/teacher/mycourse/' />"><label>我的课表</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/teacher/examresult/' />"><label>成绩管理</label></a>
                        </dd>
                    </dl>
                </li>
            </shiro:hasPermission>
            <%-- <shiro:hasPermission name="student_page">
                <li class="layui-nav-item">
                    <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe613;</i>学生菜单</a>
                    <dl class="layui-nav-child">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/student/chooseCourseMain/' />"><label>选课</label></a>
                        </dd>
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/student/myresultsMain/' />"><label>我的成绩</label></a>
                        </dd>
                    </dl>
                </li>
            </shiro:hasPermission> --%>
            <shiro:hasRole name="student">
                <li class="layui-nav-item">
                    <a><i class="layui-icon" style="font-size: 25px;padding-right: 10px;">&#xe63c;</i>学生用户</a>
                    <dl class="layui-nav-child">
                        <dd>
                            <a href="javascript:;" data-url="<s:url value='/ss/select/main/' />"><label>科目选择</label></a>
                        </dd>
                    </dl>
                </li>
            </shiro:hasRole>
        </ul>
    </div>
    <div class="layui-body">
        <div class="layui-tab fly-tab layui-tab-card" lay-filter="page-tab" lay-allowclose="true">
            <ul class="layui-tab-title" id="tabTitle">
                <li class="layui-this" lay-id="0">
                    <label>首页</label></li>                                     
            </ul>
            <div class="layui-tab-content" id="tabContainers">
                <div class="layui-tab-item layui-show">
                <!-- <button onclick="addTab('学年管理','/pony/schoolYear/main/')">测试</button> -->
                    <%--<p>提示
                        <span class="icon-i-b tips-icon mgl-5 ve-m">
                  <span class="dialog-warp right">
                    <label class="dialog-box">床前明月光，疑是地上霜，举头望明月，低头思故乡。</label></span>
                </span>
                    </p>
                    <div class="fly-echart echart-map sw-100" id="user-form-map">地图</div>
                    <div class="fly-echart echart-pie sw-50 fl" id="user-form-pie">饼状图</div>
                    <div class="fly-echart echart-line sw-50 fl" id="user-form-line">折线图</div>
                    <div class="fly-echart echart-bar sw-100 fl" id="user-form-bar">柱状和折线</div>--%>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="<s:url value='/static/layui/layui.js' />"></script>
<script src="<s:url value='/static/layui/index.js' />"></script>
<script src="<s:url value='/static/layui/common.js' />"></script>
<%--<script src="../js/lib/echarts/echarts.min.js"></script>
<script src="../js/lib/echarts/macarons.js"></script>
<script src="../js/lib/echarts/china.js"></script>
<script src="../js/define/user-collect.js"></script>--%>
</body>

</html>
