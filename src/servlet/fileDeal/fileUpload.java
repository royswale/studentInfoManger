package servlet.fileDeal;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pojo.profession;
import pojo.sharedFile;
import pojo.student;

import com.sun.faces.application.WebappLifecycleListener;
import com.sun.faces.config.WebConfiguration;

import config.webAppConfig;
import exception.myException;

import servicesDao.professionServicesDao;
import servicesDao.sharedFileServicesDao;
import servicesDao.studentServicesDao;
import servlet.servletUtil;
import util.fileUploadUtil;
import util.excelUtil.excelUtil;

public class fileUpload extends HttpServlet {

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
		HttpSession session=request.getSession();
		student stu1=(student) session.getAttribute("student");
		String userPath=request.getParameter("userPath");
		String userFileAbsolut="";
		if(servletUtil.noEmpty(userPath)){
		userPath=new String(userPath.getBytes("iso-8859-1"),"utf-8");
		File file=new File(webAppConfig.webUplodPath+File.separator+userPath.trim());
		userFileAbsolut=file.getCanonicalPath();
		}else{
			if(stu1!=null)
				userPath=""+stu1.getId();
			else
				userPath="";
		    File file=new File(webAppConfig.webUplodPath+File.separator+userPath.trim());
		    if(!file.exists())
		    	file.mkdirs();
		    userFileAbsolut=file.getCanonicalPath();
		}
  Map<String,Object> params=fileUploadUtil.parse(request, userFileAbsolut);
	if(params==null||params.size()==0)
		return ;
    String dealType=(String)params.get("type");
    //学生自己上传文件到自己的空间中
    if("stuFileUpload".equals(dealType)){
    //request中包含了currentPath信息，直接交给fileManage处理就行了
    	request.setAttribute("currentPath",userPath.trim());
      request.getRequestDispatcher("/student/fileManage").forward(request, response);
    	return;
    }
	List<File> files=(List<File>) params.get("files");
	//用来解析excel数据的导入问题
	if("xls".equals(dealType)&&files!=null&&files.size()>0){
		File excelFile=files.get(0);
		excelDeal(excelFile, request, response);
		return;
	 	}
	if("stu_update".equals(dealType)){
		try {
			updateStudent(params, request, response);
		} catch (myException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String message=e.getMessage();
			request.setAttribute("message", message);
		     request.getRequestDispatcher("/student/message.jsp").forward(request, response);
			
		}
		return;
	}
	
	
	}
	
	
private void updateStudent(Map<String,Object> params,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, myException{
	List<File> files=(List<File>)params.get("files");
	String filePath;
	String ids=(String)params.get("id");
	
	if(servletUtil.noEmpty(ids)){
		studentServicesDao stuDao=(studentServicesDao)webAppConfig.getBean(studentServicesDao.class);
		int id=Integer.parseInt(ids);
		student stu=new student();
		stu.setId(id);
		List<Object> values=new ArrayList<Object>();
		values.add(id);
		String oldPwd=(String)params.get("pwd");
		boolean isChangePwd=false;
		String condition=null;
		if(oldPwd==null||"".equals(oldPwd)){
			condition="id=?";	
		}else{
		condition="id=? and 密码=?";
		values.add(oldPwd);
		isChangePwd=true;
		}
		
		List<student> stusTemp=stuDao.select(condition, values);
		if(!servletUtil.hasElements(stusTemp)){
			throw new myException("学生找不到");
		}else{
			stu=stusTemp.get(0);
		}
		values.clear();
		condition="";
		if(isChangePwd){
		String pwd=(String) params.get("newPwd");
		String rePwd=(String) params.get("reNewPwd");
		if(servletUtil.noEmpty(pwd)&&pwd.equals(rePwd)){
			stu.setPwd(pwd);
		}
		}
		String profession_id=(String) params.get("profession");
		if(servletUtil.noEmpty(profession_id))
			stu.setProfession_id(Integer.parseInt(profession_id));
		String sex=(String) params.get("sex");
		if(servletUtil.noEmpty(sex))
			stu.setSex(sex);
		
		
	if(servletUtil.hasElements(files))
	{   
		File file=files.get(0);
		System.out.println("上传的图片信息是"+file.getCanonicalPath());
		try {
			filePath=file.getCanonicalPath().substring(webAppConfig.webUplodPath.length());
		   System.out.println("保存的路径是"+filePath);
			sharedFileServicesDao sfDao=(sharedFileServicesDao)webAppConfig.getBean(sharedFileServicesDao.class);
		   if(stu.getImage_id()>0)
		   sfDao.delete("id=?",servletUtil.changeToList(new Object[]{stu.getImage_id()}));
              
		   int fileId=sfDao.saveFile(filePath, id);
		   stu.setImage_id(fileId);
		   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (myException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	stuDao.updateStudent(stu);
	request.getRequestDispatcher("/student/getStudent?id="+id+"&type=disp").forward(request, response);
	return;	
	}
	else{
		String message="学生信息没有发现";
		request.setAttribute("message", message);
	     request.getRequestDispatcher("/student/message.jsp").forward(request, response);
			
	}
}	
	
private void excelDeal(File excelFile,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	List<student> students=excelUtil.readExcel(excelFile);
	int count=students.size();
	List<student> hasSavedStudent=null;
	Map<String,profession> proMap=new HashMap<String, profession>();
	List<profession> allPros=null;
 try {
	studentServicesDao stuDao=(studentServicesDao) webAppConfig.getBean(studentServicesDao.class);
    professionServicesDao proDao=(professionServicesDao)webAppConfig.getBean(professionServicesDao.class);
     allPros=proDao.select(null, null);
     if(allPros!=null&&allPros.size()>0){
    	 for(profession p:allPros){
    		 proMap.put(p.getName(), p);
    	 }
     }
     StringBuffer condition=new StringBuffer();
     condition.append("a.学号 in (");
     List<Object> stu_ids=new ArrayList<Object>();
     for(student stu:students){
    	 if(proMap.get(stu.getProfession().getName())==null){
    		profession p=proDao.saveProfession(stu.getProfession());
    		stu.setProfession(p);
    		proMap.put(p.getName(), p);
    	 }else{
    		 stu.setProfession(proMap.get(stu.getProfession().getName())); 
    	 }
    	 stu.setPwd("123");
       condition.append("?,");
       stu_ids.add(stu.getStuId());
     }
     condition.append("'c')");
     hasSavedStudent=stuDao.select(condition.toString(), stu_ids);
     if(hasSavedStudent!=null||hasSavedStudent.size()>0)
    	 students.removeAll(hasSavedStudent);
      if(students.size()>0)
     stuDao.saveStudents(students);
      int saveCount=students.size();
      String message="文件上传成功,excel文件中有记录数:"+count+";保存了"+saveCount+"条记录";
      request.setAttribute("message",message);
      request.setAttribute("hasSaved", hasSavedStudent);
     request.getRequestDispatcher("/fileUpload/result.jsp").forward(request, response);
 } catch (myException e) {
	e.printStackTrace();
	String message=e.getMessage();
	request.setAttribute("message", message);
    request.getRequestDispatcher("/student/message.jsp").forward(request, response);
}
}	


}
