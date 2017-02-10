<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>登录</title>
    <%-- <link rel="stylesheet" href="<s:url value='/static/login/css/reset.css' />" type="text/css">
    <link rel="stylesheet" href="<s:url value='/static/login/css/tool.css' />" type="text/css">
    <link rel="stylesheet" href="<s:url value='/static/login/css/login.css' />" type="text/css"> --%>
</head>
<body>
    <div class="login-main">
        <div class="container">
            <h1 class="login-title">管理系统</h1>
        </div>
        <div class="login-content">
            <div class="container">
            <form class="form-signin" role="form" method="post">
                   <div class="login-box">
                       <ul class="login-list">
                           <li><div class="input-box"><input placeholder="用户名" type="text" name="username" /><i class="icon-login-user"></i></div></li>
                           <li><div class="input-box"><input placeholder="密码" type="password" name="password" /><i class="icon-login-password"></i></div></li>
                           <li class="yzm-tool"><input type="checkbox" name="rememberMe" />记住我</li>
                           <!--<li class="yzm-tool"><div class="input-box"><input placeholder="验证码" type="text" /></div><a class="yzm" title="点击切换验证码" href=""><img src="images/yzm-pic.gif" /></a></li>-->
                           <li>
                               <button class="login-btn" type="submit">登 录</button>
                               <!-- <a class="login-btn" href="">登 录</a> -->
                           </li>
                       </ul>
                   </div>
            </form>
            </div>
        </div>
        <p class="copyright">
              版权所有
        </p>
    </div>
</body>
</html>