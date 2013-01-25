<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'dispStudents.jsp' starting page</title>
    
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
    <c:choose>
    <c:when test="${empty requestScope.className}">
    没有班级信息
    </c:when>
    <c:otherwise>
    <table border="1" width="600">
     <tr><td>学号</td><td>姓名</td><td>专业</td><td>性别</td><td>操&nbsp;&nbsp;作</td></tr>
    <c:forEach items="${requestScope.students}" var="stu">
     <tr><td>${stu.stuId }</td><td>${stu.name }</td><td>${stu.profession.name }</td><td>${stu.sexString }</td><td><a href="<%=path %>/student/getStudent?id=${stu.id}&type=disp&className=${requestScope.className}&page=${requestScope.page}">详细</a></td></tr>
 </c:forEach>
    </table>
    总共${requestScope.pageCount }页&nbsp&nbsp
      <c:forEach begin="0" end="${requestScope.pageCount-1}" step="1" var="index" >
      &nbsp;
      <c:choose>
      <c:when test="${requestScope.currentPage eq index}">第${index+1 }页&nbsp;&nbsp;</c:when>
      
      <c:otherwise>
      <a href="<%=path %>/student/getStudent?className=${requestScope.className}&page=${index}">第${index+1 }页</a>&nbsp;&nbsp;
  	</c:otherwise>
  	</c:choose>
    </c:forEach>
    </c:otherwise>
    </c:choose>
  <c:if test="${requestScope.currentPage gt 0}">
  <a href="<%=path %>/student/getStudent?className=${requestScope.className}&page=${requestScope.currentPage-1}">&nbsp;&nbsp;上一页&nbsp;&nbsp;</a>&nbsp;&nbsp;	
  </c:if>
  <c:if test="${requestScope.currentPage lt requestScope.pageCount-1}">
  <a href="<%=path %>/student/getStudent?className=${requestScope.className}&page=${requestScope.currentPage+1}">&nbsp;&nbsp;下一页&nbsp;&nbsp;</a>&nbsp;&nbsp;	
  </c:if> 
  </body>
</html>
