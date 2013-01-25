<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'left.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<style type="text/css">
	
     a:link {
		     text-decoration: none; 
		}
　　 a:active { 
               text-decoration:blink
        }
　　 a:hover { 
             text-decoration:underline;
			 } 
　　 a:visited { 
           text-decoration: none;
		   }
	 ul{
		font:18px verdana, arial, sans-serif;
	}
	
	 ul li {
		list-style:none; 
        margin:0;
		padding:0;
		
		width: 150px;
		padding-top: 1px;
		margin-top: 3px;
	}
	
	</style>

  </head>
  
  <body>
       <div style="color:green;">信息查询</div>
       <ul>
  <li> <a href="<%=path %>/student/getStudent?className=${sessionScope.student.stuId}" target="right">查看班级学生信息</a></li> 
  <li> <a href="<%=path %>/student/getStudent?id=${sessionScope.student.id}&type=disp" target="right">查看个人信息</a> </li>
 <li>  <a href="<%=path %>/student/getStudent?id=${sessionScope.student.id}&type=update" target="right">修改个人信息</a> </li>
   </ul>
<br>
<div style="color:green;">个人空间管理</div>
<ul>
<li><a href="<%=path %>/student/fileManage" target="right">进入个人空间</a></li>
</ul>
  </body>
</html>














