package servlet.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pojo.student;

import config.webAppConfig;
import exception.myException;

import servicesDao.studentServicesDao;

public class login extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String stuId=request.getParameter("stuId");
       String pwd=request.getParameter("pwd");
       if(!(stuId==null||pwd==null||"".equals(stuId)||"".equals(pwd))){
    	   String condition="学号=? and 密码=?";
    	   List<Object> values=new ArrayList<Object>();
    	   values.add(stuId);
    	   values.add(pwd);
    	   try {
			studentServicesDao stuDao=(studentServicesDao) webAppConfig.getBean(studentServicesDao.class);
		     List<student> students=stuDao.select(condition, values);
		     if(students!=null&&students.size()>0){
		    	 student stu=students.get(0);
		         HttpSession session=request.getSession();
		         session.setAttribute("student", stu);
		         response.sendRedirect(this.getServletContext().getContextPath()+"/student/main.htm");
		         return;
		     }
    	   } catch (myException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	   request.setAttribute("error", "用户输入信息有错误");
    	   request.getRequestDispatcher("/index.jsp").forward(request, response);
		
	}

	}
	}
