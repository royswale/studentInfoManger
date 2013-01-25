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
    
    <title>My JSP 'fileDisp.jsp' starting page</title>
    
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
    <c:if test="${not empty requestScope.message}">
    <font color="red">${requestScope.message }</font><br>
    </c:if>
    当前的目录是${requestScope.realCurrentPath },目录中的清单信息：
    <br/>
    <a href="<%=path %>/student/fileManage?folder=parentFolder&currentPath=<c:choose><c:when test="${empty requestScope.currentPath}">${sessionScope.student.ids}</c:when><c:otherwise>${requestScope.currentPath}</c:otherwise></c:choose>">返回上一层&nbsp;..</a><br/>
    <table border="0" width="700">
    <c:forEach items="${requestScope.folders}" var="folder">
<tr>
     <td>[个人-目录]&nbsp;</td>
     <td>${folder.value.fileName}</td>
     <td>
 <a href="<%=path %>/student/fileSharedManage?type=add&fileName=${folder.value.ufileName }&currentPath=<c:choose><c:when test="${empty requestScope.currentPath}">${sessionScope.student.ids}</c:when><c:otherwise>${requestScope.currentPath}</c:otherwise></c:choose>">  
     共享</a>
     </td>
     <td>
 <a href="<%=path %>/student/fileManage?currentPath=${folder.value.ufilePath}">打开</a>
     </td>
         <td>
 <a href="<%=path %>/student/fileManage?currentPath=${requestScope.currentPath }&deleteFileName=${folder.value.ufileName}">删除</a>
     </td>
     </tr>
 
    </c:forEach>
     <c:forEach items="${requestScope.sharedFolders}" var="folder">
      <tr><td>[共享-目录]&nbsp;</td><td>${folder.value.filePath}</td>
      <td>
 <a href="<%=path %>/student/fileManage?currentPath=${requestScope.currentPath }&deleteSharedFileName=${folder.value.ufileName}">取消共享</a></td>
      <td>
<a href="<%=path %>/student/fileManage?currentPath=${folder.value.ufilePath}&sharedFileRootPath=${folder.value.ufilePath}">打开</a>
</td>
      <td>
<a href="<%=path %>/student/fileManage?currentPath=${requestScope.currentPath }&deleteFileName=${folder.value.ufileName}">删除</a>
 
</td></tr>
    </c:forEach>
 <c:forEach items="${requestScope.files}" var="file">
   
    <tr><td>[个人-文件]&nbsp;</td><td>${file.value.fileName }</td>
    <td>
    <a href="<%=path %>/student/fileManage?type=add&fileName=${file.value.ufileName }&currentPath=<c:choose><c:when test="${empty requestScope.currentPath}">${sessionScope.student.ids}</c:when><c:otherwise>${requestScope.currentPath}</c:otherwise></c:choose>">  
 共享</a></td>
    <td>
    <a href="<%=path %>/file/down?currentPath=${file.value.ufilePath}" target="down">下载</a>
    </td>
    <td>
<a href="<%=path %>/student/fileManage?currentPath=${requestScope.currentPath }&deleteFileName=${file.value.ufileName}">删除</a>
</td>
    </tr>
 
    </c:forEach>
     <c:forEach items="${requestScope.sharedFiles}" var="file">
   
    <tr><td>[共享-文件]&nbsp;</td><td>${file.value.fileName }</td>
    <td>
<a href="<%=path %>/student/fileManage?currentPath=${requestScope.currentPath }&deleteSharedFileName=${file.value.ufileName}">取消共享</a>
</td>
    <td>
<a href="<%=path %>/file/down?currentPath=${file.value.ufilePath}" target="down">下载</a>
</td>
    <td>
<a href="<%=path %>/student/fileManage?currentPath=${requestScope.currentPath }&deleteFileName=${file.value.ufileName}">删除</a>
 
</td>
    </tr>
 </c:forEach>
   </table>
<form action="<%=path %>/student/fileManage" method="post">
请输入要建立的文件夹名称<input type="text" name="newFolder"/>
<c:choose>
     <c:when test="${empty requestScope.currentPath}">
     <input type="hidden" name="currentPath" value="${sessionScope.student.ids}"/> 
      </c:when>
     <c:otherwise>
     <input type="hidden" name="currentPath" value="${requestScope.currentPath}"/> 
     </c:otherwise>
     </c:choose>
<input type="submit" value="新建文件夹"/>
</form>
<form action="<%=path %>/fileUpload/fileUpload?userPath=<c:choose><c:when test="${empty requestScope.currentPath}">${sessionScope.student.ids}</c:when><c:otherwise>${requestScope.currentPath}</c:otherwise></c:choose>" enctype="multipart/form-data" method="post">
   上传文件的当前目录下<input type="file" name="file1"/>
     <input type="hidden" name="type" value="stuFileUpload"/>
     <input type="submit" value="文件上传"/>
   </form>
  </body>
</html>
