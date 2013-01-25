package servlet.student;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import config.webAppConfig;
import exception.myException;

import pojo.student;

import servicesDao.sharedFileServicesDao;
import servlet.servletUtil;

public class fileSharedManage extends HttpServlet {

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
	 * @throws IOException 
	 * @throws ServletException 
	 * @throws myException 
	 * @throws IOException 
	 * @throws ServletException 
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	private void exceptionDeal(Exception e,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		e.printStackTrace();
		request.setAttribute("message",e.getMessage());
		request.getRequestDispatcher("/student/fileManage").forward(request, response);
	}
	private void addSharedFile(HttpServletRequest request,HttpServletResponse response,String currentPath,String sharedFileName) throws myException, ServletException, IOException{
		HttpSession session=request.getSession();
		student stu=(student) session.getAttribute("student");
		int id=stu.getId();
		sharedFileServicesDao sfsDao=(sharedFileServicesDao) webAppConfig.getBean(sharedFileServicesDao.class);
	    sfsDao.saveFile(currentPath+File.separator+sharedFileName,id);
	    request.getRequestDispatcher("/student/fileManage").forward(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
      String currentPath=request.getParameter("currentPath");
      String type=request.getParameter("type");
      String readySharedFileName=request.getParameter("fileName");
      if(servletUtil.noEmpty(type)){
    	  if("add".equals(type)){
    		  if(servletUtil.noEmpty(readySharedFileName))
    		readySharedFileName=servletUtil.changeCharater(readySharedFileName);
    		System.out.println("准备要添加的共享的文件"+readySharedFileName);
    		  currentPath=servletUtil.changeCharater(currentPath);
    		  System.out.println("当前的目录是"+currentPath);
    		request.setAttribute("currentPath",currentPath);
    		try {
				addSharedFile(request, response, currentPath, readySharedFileName);
			} catch (myException e) {
				exceptionDeal(e, request, response);
			}
    	   return;
    	  }
      }
		
	}

}
