package servlet.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.webAppConfig;

import pojo.profession;
import pojo.student;
import servicesDao.professionServicesDao;
import servicesDao.studentServicesDao;
import servlet.servletUtil;

import dao.daoTemplate;
import exception.myException;

public class getStudent extends HttpServlet {
    private int pageSize=10;
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	   String pageSizes=config.getInitParameter("pageSize");
	   if(servletUtil.noEmpty(pageSizes)){
		   pageSize=Integer.parseInt(pageSizes);
	   }
	}

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
      String type=request.getParameter("type");
      String idS=request.getParameter("id");
      String className=request.getParameter("className");
      String dispPages=request.getParameter("page");
      if(servletUtil.noEmpty(className)){
    	  className=className.substring(0,5);
      }
      String message=null;
      if(servletUtil.noEmpty(type)&&servletUtil.noEmpty(idS)){
        	  student stu=null;
    	  try {
			studentServicesDao stuDao=(studentServicesDao) webAppConfig.getBean(studentServicesDao.class);
		    String condition="id=?";
		    List<Object> values=servletUtil.changeToList(new Object[]{Integer.parseInt(idS)});
		   List<student> students= stuDao.select(condition, values);
		   if(servletUtil.hasElements(students))
		   {
			stu=students.get(0);
			request.setAttribute("stu", stu);
			if("disp".equals(type))
			request.getRequestDispatcher("/student/dispStudent.jsp").forward(request, response);
			else if("update".equals(type)){
			  professionServicesDao psd=(professionServicesDao) webAppConfig.getBean(professionServicesDao.class);
				List<profession> pfs=psd.select(null, null);
				request.setAttribute("professions", pfs);
				request.setAttribute("student", stu);
			  request.getRequestDispatcher("/student/updateStudent.jsp").forward(request, response);
			}	
			return;
		   }
		   else{
			   message="用户名输入错误;";
		   }
    	  } catch (myException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  
      }else if(servletUtil.noEmpty(className)){//显示班级的学生信息
    	  String condition= "学号 like ?";
    	 try{ 
    		 int page=0;
    		 if(servletUtil.noEmpty(dispPages))
    			 page=Integer.parseInt(dispPages);
    			 
    		 studentServicesDao dao=(studentServicesDao) webAppConfig.getBean(studentServicesDao.class);
    	  Map<String,Object> resultMap=servletUtil.resultPageDisp(dao, condition, servletUtil.changeToList(new Object[]{className+"%"}),"学生表", page, pageSize);
    	 List<student> students=(List<student>) resultMap.get("results");
    	  if(servletUtil.hasElements(students)){
    	  request.setAttribute("pageCount",resultMap.get("pageCount"));
    	  request.setAttribute("currentPage", resultMap.get("currentPage"));
    	  request.setAttribute("className", className);
    	  request.setAttribute("students", students);
    	  request.getRequestDispatcher("/student/dispStudents.jsp").forward(request, response);
    	 return;}
    	  else{
    		  throw new myException("数据库中没有关于["+className+"]的班级记录");
    	  }
    	  } catch (myException e) {
			servletUtil.exceptionDeal(e, request, response);
		}
      
      }
      else{
    	  message="没有对应学生信息";
    	  request.setAttribute("message", message);
    	request.getRequestDispatcher("/student/message.jsp").forward(request, response);
    		
      }
     }

}
