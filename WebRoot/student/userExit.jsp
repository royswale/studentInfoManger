<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'userExit.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<style type="text/css">
	   .bt{
	   color: red;
	   font-size: 40px;
	   font-family: sans-serif;
	   
	   }

	   
	</style>

  </head>
  
  <body>
  <div class="bt" align="center">
  <h2>学生个人信息管理</h2>
    用户:${sessionScope.student.name } &nbsp;[<a href="<%=path %>/userExit" target="_top">注销用户</a>]
  </div>
  </body>
</html>
