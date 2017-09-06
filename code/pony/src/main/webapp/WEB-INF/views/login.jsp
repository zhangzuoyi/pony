<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html>
<html>
<head>
	<!-- Basics -->
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>登录</title>
	<!-- CSS -->
	<link rel="stylesheet" href="<s:url value='/static/login/css/reset.css '/>" />
	<%-- <link rel="stylesheet" href="<s:url value='/static/login/css/animate.css '/>" /> --%>
	<link rel="stylesheet" href="<s:url value='/static/login/css/styles.css '/>" />
</head>
	<!-- Main HTML -->
<body>
	<!-- Begin Page Content -->
	<div id="container">
		<form method="post">
		<label for="name">用户名:</label>
		<input placeholder="用户名" type="name" name="username" />
		<label for="username">密码:</label>
		<!-- <p><a href="#">Forgot your password?</a> -->
		<input placeholder="密码" type="password" name="password" />
		<div id="lower">
		<input type="checkbox" name="rememberMe" /><label class="check" for="checkbox">记住我</label>
		<input type="submit" value="登录">
		</div>
		</form>
	</div>
	<!-- End Page Content -->
</body>
</html>