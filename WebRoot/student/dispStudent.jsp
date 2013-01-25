<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'dispStudent.jsp' starting page</title>
    
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
     学生学号:${requestScope.stu.stuId }<br>
   学生姓名:${requestScope.stu.name }<br>
      学生专业:${requestScope.stu.profession.name }<br>
   学生性别:${requestScope.stu.sexString }<br> 
  学生图像:<c:choose>
         <c:when test="${requestScope.stu.image_id<=0}">
          <img src="<%=path %>/student/image/head.png"/>
          </c:when>
          <c:otherwise>
           <img src="<%=path %>/file/down?fileId=${requestScope.stu.image_id}"/>
         </c:otherwise>
  </c:choose><br/>
  <c:if test="${not empty param.className}">
    <a href="<%=path %>/student/getStudent?className=${param.className}&page=${param.page}">返回</a>
  </c:if>
  </body>
</html>
