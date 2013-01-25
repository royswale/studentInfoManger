<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
  <div align="center" style="border:1px;border-color:gray; ">
       <span style="color:red; border-color: blue; font-size:30px;" >学生个人信息管理系统</span><br><br>
  ${requestScope.error }<br>
    <form action="<%=path %>/login" method="post">
    请输入学号<input type="text" name="stuId" /><br>
    请输入密码<input type="text" name="pwd" /><br/>
    <input type="submit" value="登录"/>
    </form>
 </div>
  </body>
</html>
