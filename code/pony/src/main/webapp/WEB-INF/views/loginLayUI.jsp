<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<meta charset="utf-8">
	<meta name="renderer" content="webkit"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>用户登录&nbsp;·&nbsp;</title>
	<link rel="stylesheet" href="<s:url value='/static/bootstrap/css/bootstrap.css '/>"/>
	<link rel="stylesheet" href="<s:url value='/static/layui/css/layui.css '/>"/>
	<link rel="stylesheet" href="<s:url value='/static/login/css/login.css '/>">

</head>

<body>

<div class="login-container" style="height:100%">



	<!-- 顶部导航条 开始 -->
	<div class="header">
		<span style="font-size: 20px;">欢迎登录</span>
	</div>
	<!-- 顶部导航条 结束 -->

	<!-- 页面表单主体 开始 -->
	<div class="container" style="top:50%;margin-top:-300px">

		<form  method="post" class="content layui-form">
			<div class="people">
				<div class="tou"></div>
				<div id="left-hander" class="initial_left_hand transition"></div>
				<div id="right-hander" class="initial_right_hand transition"></div>
			</div>
			<ul>
				<li>

					<input required="required"
						   pattern="^\S{4,}$"
						   type="text"
						   name="username"
						   autocomplete="off"
						   autofocus="autofocus"
						   class="login-input username"
						   placeholder="请输入用户名"/>
				</li>
				<li>

					<input required="required"
						   pattern="^\S{4,}$"
						   type="password"
						   name="password"
						   autocomplete="off"
						   class="login-input password"
						   placeholder="请输入密码"/>
				</li>
				<li class="text-center">
					<button type="submit" class="layui-btn " >立 即 登 入</button>
				</li>
			</ul>
		</form>
	</div>
	<!-- 页面表单主体 结束 -->


</div>


</body>

</html>