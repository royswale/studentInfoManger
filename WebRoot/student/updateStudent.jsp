<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'updateStudent.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body><form action="<%=path %>/fileUpload/fileUpload" enctype="multipart/form-data" method="post"> 
  学生学号:${requestScope.student.stuId }<br>
  学生姓名:${requestScope.student.name }<br/>
  学生专业:<select name="profession">
    <option value="${requestScope.student.profession_id }">${requestScope.student.profession.name }</option>
  <c:forEach items="${requestScope.professions}" var="pf">
  <option value="${pf.id }">${pf.name }</option>
  </c:forEach>
  </select> <br>
 学生性别:<select name="sex">
   <option value="${requestScope.student.sexString }">${requestScope.student.sexString }</option>
   <option value="男">男</option>
  <option value="女">女</option>
 </select><br>
 学生原始密码<input type="password" name="pwd"/><br/>
 学生新密码<input type="password" name="newPwd"/><br/>
学生再次新密码<input type="password" name="reNewPwd"/><br/>
     学生相貌：<input type="file" name="headImage"/><br/>
 <input type="hidden" name="type" value="stu_update"/>"
 <input type="hidden" name="id" value="${requestScope.student.id }"/>
   <input type="submit" value="更新">
   </form>
  </body>
</html>
