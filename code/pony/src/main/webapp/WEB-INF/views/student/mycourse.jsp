<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的课程</title>
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
<body style="padding:10px;" ng-app="myApp" ng-controller="StudentController"> 
<div class="row"> 
	<div class="col-md-2">
	</div>
	<div class="col-md-10">
		<table id="mytable" class="table table-bordered" style="width:800px;"> 
			<tr> 
				<td></td> 
				<td ng-repeat="x in arrange.weekDays">{{x.weekDayName}}</td> 
			</tr> 
			<tr ng-repeat="x in arrange.weekDays[0].periods" ng-init="outerIndex = $index"> 
				<td>{{x.lessonPeriod.seq}}</td> 
				<td ng-repeat="y in arrange.weekDays" class="content" ng-click="addClass($event);" >{{y.periods[outerIndex].subject.name}}</td> 
			</tr> 
		</table> 
	</div>


</div> 
<script type="text/javascript" src="<s:url value='/static/angularjs/angular.min.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/app/app.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/app/subject/service.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/app/student/service.js' />"></script>
<script type="text/javascript" src="<s:url value='/static/app/student/controller.js' />"></script>
</body> 
</html>