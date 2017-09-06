<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年级管理</title>
<link rel="stylesheet" type="text/css" href="<s:url value='/static/easyui/themes/default/easyui.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/bootstrap/css/bootstrap.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/style.css' />" />
<link rel="stylesheet" type="text/css" href="<s:url value='/static/css/icon.css' />" />
<script type="text/javascript" src="<s:url value='/static/js/jquery.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/bootstrap/js/bootstrap.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/easyui/dateFormat.js' />"></script>
<style type="text/css"> 
#mytable tr{ 
padding-bottom:10px; 
} 
#mytable td{ 
text-align:center; 
vertical-align:middle; 
} 
#mytable td.content{ 
height:80px; 

} 
#mytable td.content:hover 
{ 
background-color:yellow; 
} 
#mytable td.selected{ 
background-color:yellow; 

} 
input,textarea{ 
margin:5px; 
} 
</style> 
</head>
<body style="padding:10px;" ng-app="myApp" ng-controller="LessonArrangeController"> 
<div class="row"> 
	<div class="col-md-2">
		<div class="easyui-panel" style="padding:5px">
			<ul class="easyui-tree">
				<li>
					<span>班级列表</span>
					<ul>
						<c:forEach items="${classes }" var="g">
                			<li><a href="javascript:void(0)" ng-click="changeClass(${g.classId })">${g.name }</a></li>
                		</c:forEach>
					</ul>
				</li>
			</ul>
		</div>
	</div>
	<div class="col-md-10">
		<table id="mytable" class="table table-bordered" style="width:800px;"> 
			<tr> 
				<td></td> 
				<td ng-repeat="x in arrange.weekDays">{{x.weekDayName}}</td> 
			</tr> 
			<tr ng-repeat="x in arrange.weekDays[0].periods" ng-init="outerIndex = $index"> 
				<td>{{x.lessonPeriod.seq}}</td> 
				<td ng-repeat="y in arrange.weekDays" class="content" ng-click="addClass($event);" ng-dblclick="editArrange($event,$index,outerIndex);">{{y.periods[outerIndex].subject.name}}</td> 
			</tr> 
		</table> 
	</div>

<div id="my-dialog" class="easyui-dialog" data-options="modal:true,closed:true,iconCls:'icon-save',title:'课程安排'" style="width:400px; padding:10px;"> 
<form id="my-form" method="post"> 
		<input type="hidden" name="arrangeId" ng-model="la.arrangeId" />
        <table class="table table-bordered"> 
        	<tr> 
                <td width="60" align="right">学年:</td> 
                <td>{{la.schoolYear.name}}</td> 
            </tr> 
            <tr> 
                <td width="60" align="right">学期:</td> 
                <td>{{la.term.name}}</td> 
            </tr> 
            <tr> 
                <td width="60" align="right">班级:</td> 
                <td>{{arrange.className}}</td> 
            </tr> 
            <tr> 
                <td align="right">星期:</td> 
                <td>{{weekDayVo.weekDayName}}</td> 
            </tr> 
            <tr> 
                <td align="right">时段:</td> 
                <td>{{la.lessonPeriod.seq}}</td> 
            </tr> 
            <%-- <tr> 
                <td align="right">科目类型:</td> 
                <td>
                	<select name="lessonType" ng-model="la.lessonType" ng-options="x.id as x.name for x in [{id:0,name:'主科目'},{id:1,name:'选修'}]">
                	</select>
                </td> 
            </tr>  --%>
            <tr> 
                <td align="right">科目:</td> 
                <td>
                	<select name="subject" ng-model="la.subject.subjectId" ng-options="x.subjectId as x.name for x in subjects" >
                		<!-- <option ng-repeat="x in subjects" value="{{x.subjectId}}">{{x.name}}</option> -->
                	</select>
                </td> 
            </tr>
            <!-- <tr ng-show="la.lessonType == 1"> 
                <td align="right">其它科目:</td> 
                <td>
                	<input type="text" name="otherLesson" ng-model="la.otherLesson" class="my-text" />
                </td> 
            </tr> -->
            <tr style="text-align:center"> 
                <td colspan="2">
                	<button type="button" class="easyui-linkbutton" ng-click="submitLa()">提交</button>
                	<button type="button" ng-show="la.subject != null" class="easyui-linkbutton" ng-click="deleteLa()">删除</button>
                	<button type="button" class="easyui-linkbutton" ng-click="closeDialog()">取消</button>
                </td> 
            </tr> 
        </table> 
    </form> 
</div> 
</div> 
<script type="text/javascript" src="<s:url value='/static/angularjs/angular.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/app/app.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/app/subject/service.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/app/lessonArrange/service.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/app/lessonArrange/controller.js' />"></script>
<script type="text/javascript"> 
$(document).ready(function(){ 
$("td.content").click(function(){ 
$("td.content").removeClass("selected"); 
$(this).addClass("selected"); 
}); 
$("td.content").dblclick(function(){ 
var html=$(this).html(); 
$('#my-dialog').dialog('open') 
}); 

}); 
</script> 
</body> 
</html>