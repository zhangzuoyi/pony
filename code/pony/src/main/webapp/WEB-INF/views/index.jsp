<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- <title>学校管理系统</title> -->
<title>系统实施管理工具</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
</head>
<body id="mypage" class="easyui-layout">
	<!-- begin of header -->
	<div class="my-header" data-options="region:'north',border:false,split:true">
    	<div class="my-header-left">
        	<h1>学校管理系统</h1>
        </div>
        <div class="my-header-right">
        	<p><strong class="easyui-tooltip" ><shiro:principal /></strong>，欢迎您！</p>
            <p><a href="<s:url value='logout' />">安全退出</a></p>
        </div>
    </div>
    <!-- end of header -->
    <!-- begin of sidebar -->
	<div class="my-sidebar" data-options="region:'west',split:true,border:true,title:'导航菜单'"> 
    	<div class="easyui-accordion" data-options="border:false,fit:true"> 
        	<shiro:hasPermission name="sys_admin">
        	<div title="系统管理" data-options="iconCls:'icon-application-cascade'" style="padding:5px;">  	
    			<ul class="easyui-tree my-side-tree">
    				<shiro:hasPermission name="schoolyear_admin">
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/schoolYear/main/' />" iframe="1">学年管理</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="grade_admin">
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/grade/main/' />" iframe="1">年级管理</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="class_admin">
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/schoolClass/main/' />" iframe="1">班级管理</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="subject_admin">
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/subject/main/' />" iframe="1">科目管理</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="teacher_admin">
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/teacherAdmin/main/' />" iframe="1">教师管理</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="dict_admin">
                    <%-- <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/teacherSubject/main/' />" iframe="1">教师任课管理</a></li> --%>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/commonDict/main/' />" iframe="1">通用字典管理</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="resource_admin">
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/resourceAdmin/main/' />" iframe="1">资源管理</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="role_admin">
                 	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/roleAdmin/main/' />" iframe="1">角色管理</a></li>
                 	</shiro:hasPermission>
                    <shiro:hasPermission name="user_admin">
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/user/main/' />" iframe="1">用户管理</a></li>
                    </shiro:hasPermission>
                    
                </ul>
            </div>
            </shiro:hasPermission>
            <shiro:hasPermission name="score_analysis">
        	<div title="成绩分析" data-options="iconCls:'icon-application-cascade'" style="padding:5px;">  	
    			<ul class="easyui-tree my-side-tree">
    				<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/examResultRank/main/' />" iframe="1">成绩排名管理</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/studentComprehensiveTrack/main/' />" iframe="1">学生综合成绩追踪</a></li> 
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/studentSingleTrack/main/' />" iframe="1">学生单科成绩追踪</a></li>                          
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/classComprehensiveCompare/main/' />" iframe="1">班级综合成绩对比</a></li>  
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/classSingleCompare/main/' />" iframe="1">班级单科成绩对比</a></li>                        
                                             
                </ul>
            </div>
            </shiro:hasPermission>
            <shiro:hasPermission name="student_status">
        	<div title="学籍管理" data-options="iconCls:'icon-application-cascade'" style="padding:5px;">  	
    			<ul class="easyui-tree my-side-tree">
    				<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/studentAdmin/entrance/' />" iframe="1">入学管理</a></li>
    				<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/studentAdmin/main/' />" iframe="1">学生管理</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/studentRemark/main/' />" iframe="1">学生评语管理</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/prizePunish/main/' />" iframe="1">学生奖惩管理</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/upgrade/main/' />" iframe="1">升级管理</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/studentCard/main/' />" iframe="1">学籍卡</a></li>
                </ul>
            </div>
            </shiro:hasPermission>
            <shiro:hasPermission name="exam_admin">
        	<div title="考试管理" data-options="iconCls:'icon-application-cascade'" style="padding:5px;">  	
    			<ul class="easyui-tree my-side-tree">
    				<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/exam/main/' />" iframe="1">考试管理</a></li>
    				<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/examAdmin/examRoom/main/' />" iframe="1">考场管理</a></li>  		
    				<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/examAdmin/examArrange/main/' />" iframe="1">考试安排</a></li>
    				<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/examAdmin/examRoomArrange/main/' />" iframe="1">考场设置</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/examAdmin/examineeArrange/main/' />" iframe="1">考生设置</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/examAdmin/examineeRoomArrange/main/' />" iframe="1">考生考场安排</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/examResult/main/' />" iframe="1">成绩管理</a></li>
                </ul>
            </div>
            </shiro:hasPermission>
            <shiro:hasPermission name="lesson_arrange">
            <div title="排课管理" data-options="iconCls:'icon-application-form-edit'" style="padding:5px;">  	
    			<ul class="easyui-tree my-side-tree">
                    <shiro:hasPermission name="lesson_period">
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/lessonPeriod/main/' />" iframe="1">上课时段管理</a></li>
                	</shiro:hasPermission>
                    <shiro:hasPermission name="teacher_lesson_arrange">
                    <%-- <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/lessonArrange/main/' />" iframe="1">课程安排</a></li> --%>
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/teacherLessonArrange/main/' />" iframe="1">老师任课安排</a></li>
                	</shiro:hasPermission>
                    <shiro:hasPermission name="teacher_lesson_list">
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/teacherLesson/main/' />" iframe="1">老师任课列表</a></li>
                	</shiro:hasPermission>
                    <shiro:hasPermission name="week_lesson_admin">
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/weekLessonAdmin/main/' />" iframe="1">星期上课设置</a></li>
                	</shiro:hasPermission>
                    <shiro:hasPermission name="grade_no_course">
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/gradeNoCourse/main/' />" iframe="1">年级不排课设置</a></li>  
                	</shiro:hasPermission>
                    <shiro:hasPermission name="class_no_course">             	
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/classNoCourse/main/' />" iframe="1">班级不排课设置</a></li>
                	</shiro:hasPermission>
                    <shiro:hasPermission name="teacher_no_course">
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/teacherNoCourse/main/' />" iframe="1">老师不排课设置</a></li>
                	</shiro:hasPermission>
                    <shiro:hasPermission name="subject_no_course">
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/subjectNoCourse/main/' />" iframe="1">科目不排课设置</a></li>
                	</shiro:hasPermission>
                    <shiro:hasPermission name="arrange_combine">
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/arrangeCombine/main/' />" iframe="1">合班设置</a></li>
                	</shiro:hasPermission>
                    <shiro:hasPermission name="arrange_rotation">
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/arrangeRotation/main/' />" iframe="1">走班设置</a></li>        
                	</shiro:hasPermission>
                    <shiro:hasPermission name="prelesson_arrange">      	
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/preLessonArrange/main/' />" iframe="1">预排</a></li>
                	</shiro:hasPermission>
                    <shiro:hasPermission name="auto_lesson_arrange">
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/autoLessonArrange/main/' />" iframe="1">自动排课</a></li>
                	</shiro:hasPermission>
                    <shiro:hasPermission name="relesson_arrange">
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/reLessonArrange/main/' />" iframe="1">调课</a></li>
                	</shiro:hasPermission>
                    <shiro:hasPermission name="teacher_course">
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/teacherCourse/main/' />" iframe="1">教师课表</a></li>
                	</shiro:hasPermission>
                	
                </ul>
            </div>
            </shiro:hasPermission>
            <shiro:hasPermission name="lesson_select">
            <div title="选课管理" data-options="iconCls:'icon-application-form-edit'" style="padding:5px;">  	
    			<ul class="easyui-tree my-side-tree">
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/lessonSelectArrange/main/' />" iframe="1">可选课程设置</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/lessonSelect/main/' />" iframe="1">学生选课</a></li>
                </ul>
            </div>
            </shiro:hasPermission>
            <shiro:hasPermission name="attendance">
            <div title="考勤管理" data-options="iconCls:'icon-application-form-edit'" style="padding:5px;">  	
    			<ul class="easyui-tree my-side-tree">
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/attendance/attendance/main/' />" iframe="1">考勤打卡</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/attendance/attendance/my/' />" iframe="1">考勤记录</a></li>
                </ul>
            </div>
            </shiro:hasPermission>
            <shiro:hasPermission name="message">
            <div title="消息管理" data-options="iconCls:'icon-application-form-edit'" style="padding:5px;">  	
    			<ul class="easyui-tree my-side-tree">
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/userGroup/main/' />" iframe="1">用户组</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/message/messageSend/main/' />" iframe="1">消息发送</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/message/messageReceive/main/' />" iframe="1">消息接收</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/message/messageSent/main/' />" iframe="1">已发送消息</a></li>
                
                </ul>
            </div>
            </shiro:hasPermission>
            <shiro:hasPermission name="property">
            <div title="资产管理" data-options="iconCls:'icon-application-form-edit'" style="padding:5px;">  	
    			<ul class="easyui-tree my-side-tree">
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/property/propertyType/main' />" iframe="1">资产分类</a></li>                   
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/property/department/main/' />" iframe="1">部门管理</a></li>                                    
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/property/add/main' />" iframe="1">新增资产</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/property/propertyAdmin/main' />" iframe="1">资产管理</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/property/addConsumable/main' />" iframe="1">新增损耗品</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/property/consumableAdmin/main' />" iframe="1">损耗品管理</a></li>
                   
                
                </ul>
            </div>
            </shiro:hasPermission>
            
            <shiro:hasPermission name="teacher_page">
            <div title="任课管理" data-options="iconCls:'icon-application-form-edit'" style="padding:5px;">  	
    			<ul class="easyui-tree my-side-tree">
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/teacher/courses/' />" iframe="1">我的任课</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/teacher/mycourse/' />" iframe="1">我的课表</a></li>                   
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/teacher/examresult/' />" iframe="1">成绩管理</a></li>              
                </ul>
            </div>
            </shiro:hasPermission>
            <shiro:hasPermission name="student_page">
            <div title="学生菜单" data-options="iconCls:'icon-application-form-edit'" style="padding:5px;">  	
    			<ul class="easyui-tree my-side-tree">
                	<li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/student/chooseCourseMain/' />" iframe="1">选课</a></li>
                    <li iconCls="icon-users"><a href="javascript:void(0)" data-icon="icon-users" data-link="<s:url value='/student/myresultsMain/' />" iframe="1">我的成绩</a></li>
                </ul>
            </div>
            </shiro:hasPermission>
        </div>
    </div>	
    <!-- end of sidebar -->    
    <!-- begin of main -->
    <div class="my-main" data-options="region:'center'">
        <div id="my-tabs" class="easyui-tabs" data-options="border:false,fit:true">  
            <!-- <div title="首页" data-options="href:'temp/layout-1.html',closable:false,iconCls:'icon-tip',cls:'pd3'"></div> -->
        </div>
    </div>
    <!-- end of main --> 
    <!-- begin of footer -->
	<div class="my-footer" data-options="region:'south',border:true,split:true">
    	&copy; 2017 Pony All Rights Reserved
    </div>
    <!-- end of footer -->  
    <script type="text/javascript">
		$(function(){
			$('.my-side-tree a').bind("click",function(){
				var title = $(this).text();
				var url = $(this).attr('data-link');
				var iconCls = $(this).attr('data-icon');
				var iframe = $(this).attr('iframe')==1?true:false;
				addTab(title,url,iconCls,iframe);
			});	
		})
		
		/**
		* Name 载入树形菜单 
		*/
		$('#my-side-tree').tree({
			url:'temp/menu.php',
			cache:false,
			onClick:function(node){
				var url = node.attributes['url'];
				if(url==null || url == ""){
					return false;
				}
				else{
					addTab(node.text, url, '', node.attributes['iframe']);
				}
			}
		});
		
		/**
		* Name 选项卡初始化
		*/
		$('#my-tabs').tabs({
			/* tools:[{
				iconCls:'icon-reload',
				border:false,
				handler:function(){
					$('#my-datagrid').datagrid('reload');
				}
			}] */
		});
			
		/**
		* Name 添加菜单选项
		* Param title 名称
		* Param href 链接
		* Param iconCls 图标样式
		* Param iframe 链接跳转方式（true为iframe，false为href）
		*/	
		function addTab(title, href, iconCls, iframe){
			var tabPanel = $('#my-tabs');
			if(!tabPanel.tabs('exists',title)){
				var content = '<iframe scrolling="auto" frameborder="0"  src="'+ href +'" style="width:100%;height:100%;"></iframe>';
				if(iframe){
					tabPanel.tabs('add',{
						title:title,
						content:content,
						iconCls:iconCls,
						fit:true,
						cls:'pd3',
						closable:true
					});
				}
				else{
					tabPanel.tabs('add',{
						title:title,
						href:href,
						iconCls:iconCls,
						fit:true,
						cls:'pd3',
						closable:true
					});
				}
			}
			else
			{
				tabPanel.tabs('select',title);
			}
		}
		/**
		* Name 移除菜单选项
		*/
		function removeTab(){
			var tabPanel = $('#my-tabs');
			var tab = tabPanel.tabs('getSelected');
			if (tab){
				var index = tabPanel.tabs('getTabIndex', tab);
				tabPanel.tabs('close', index);
			}
		}
		
		/* function logout(){
			$("#mypage").href="<s:url value='logout' />";
		} */
	</script>
</body>
</html>
