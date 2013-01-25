package servlet.student;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pojo.myFile;
import pojo.student;
import servicesDao.sharedFileServicesDao;
import servlet.servletUtil;

import config.webAppConfig;
import exception.myException;

public class fileManage extends HttpServlet {

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
        String currentPath=request.getParameter("currentPath");
        if(servletUtil.noEmpty(currentPath)){
        	currentPath=servletUtil.getMethodUnicoderToString(currentPath);
        	if(servletUtil.noEmpty(currentPath))
        		request.setAttribute("currentPath", currentPath.trim());
        }
		this.doPost(request, response);
	}
	private void exceptionDeal(Exception e,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		e.printStackTrace();
		request.setAttribute("message",e.getMessage());
		request.getRequestDispatcher("/student/fileManage").forward(request, response);
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
         student stu=(student) session.getAttribute("student");
         String userRootFile=stu.getId()+"";
        String currentPath=(String) request.getAttribute("currentPath");
      //表示是post方式传入的currentPath，所以不需要进行字符类型转换
        if(!servletUtil.noEmpty(currentPath)){
        	currentPath=request.getParameter("currentPath");
        	//第一次登陆时currentPath什么都没有
        	if(servletUtil.noEmpty(currentPath))
           currentPath=URLDecoder.decode(currentPath,servletUtil.stander_Encoder);	
        }
        String sharedFileRootPath =request.getParameter("sharedFileRootPath");
        if(servletUtil.noEmpty(sharedFileRootPath)){
        	sharedFileRootPath=servletUtil.getMethodUnicoderToString(sharedFileRootPath);
        }
        
        
        //使用post直接传入的
        String newFolder=request.getParameter("newFolder");
        
        String deleteFileName=request.getParameter("deleteFileName");
        //获得要删除的共享文件、目录的名字
        String deleteSharedFileName=request.getParameter("deleteSharedFileName");
        if(servletUtil.noEmpty(deleteSharedFileName)){
        	deleteSharedFileName=servletUtil.getMethodUnicoderToString(deleteSharedFileName);
        }
        
        //把用get传递的unicode的内容变成正常内容
        if(servletUtil.noEmpty(deleteFileName))
        deleteFileName=servletUtil.getMethodUnicoderToString(deleteFileName);
        //返回上一个目录
        String folder=request.getParameter("folder");
        if(servletUtil.noEmpty(folder)){
        	folder=servletUtil.changeCharater(folder);
        }
        //返回上一个目录的操作
        if(servletUtil.noEmpty(folder)&&"parentFolder".equals(folder)){
        	System.out.println("传递上来的path="+currentPath);
        	//判断当前目录的上一层目录是不是当前用户的根目录
        	String currentPathTemp=currentPath.substring(0,currentPath.lastIndexOf(File.separator));
        	if(!currentPathTemp.equals(File.separator))
            	currentPath=currentPathTemp;//如果不是用户的根目录，则当前目录变成上层目录
        }
        if(servletUtil.noEmpty(currentPath)){
    	   //创建一个目录
    	   if(servletUtil.noEmpty(newFolder)){
    		   servletUtil.createFile(webAppConfig.webUplodPath,currentPath+File.separator+newFolder);
    	   }
    	   //删除指定名字的文件
    	   if(servletUtil.noEmpty(deleteFileName)){
    		   System.out.println(deleteFileName);
    		   String folderPath=webAppConfig.webUplodPath+File.separator+currentPath;
    		  try {
				servletUtil.delete(folderPath,deleteFileName);
				//如果有共享文件则删除数据库中的共享记录，包括它的子目录中的共享记录
				deleteSharedFile(currentPath+File.separator+deleteFileName);
			} catch (myException e) {
				exceptionDeal(e, request, response);
				return;
			} 
    	   }
    	  if(servletUtil.noEmpty(deleteSharedFileName)){
    		  try {
				deleteSharedFile(currentPath+File.separator+deleteSharedFileName);
			} catch (myException e) {
				exceptionDeal(e, request, response);
				return;
			}
    	  }
    	  if(servletUtil.noEmpty(sharedFileRootPath)){
    		  request.setAttribute("sharedFileRootPath",servletUtil.stringToUnicoder(sharedFileRootPath));
    		  currentFolderDeal(request, response, currentPath, false);
    	  }else
    		  currentFolderDeal(request, response, currentPath);
       }else{
    	   firstEnter(request, response);
       }
        	 
	}
	public static void deleteSharedFile(String filePath) throws myException{
		String condition="路径  like ? and 路径 not like ?";
		List<Object> values=new ArrayList<Object>();
		values.add(servletUtil.pathToDB(filePath+"%"));
		values.add(servletUtil.pathToDB(filePath+File.separator+"%"+File.separator+"%"));
		sharedFileServicesDao sfsDao=(sharedFileServicesDao) webAppConfig.getBean(sharedFileServicesDao.class);
	   sfsDao.delete(condition, values);	
	}
	//进入共享文件夹的显示还是进入普通文件夹的显示
	private void currentFolderDeal(HttpServletRequest request,HttpServletResponse response,String currentFolder,boolean isDispSharedFileDisp){
		System.out.println("当前进入的目录是"+currentFolder);
		try {
			File file=servletUtil.getFile(webAppConfig.webUplodPath, currentFolder) ;
		if(file==null)
			throw new myException("请求的"+currentFolder+"不存在");
		
			String path=servletUtil.relativePath(webAppConfig.webUplodPath, file);
			System.out.println("relativePath"+path);
		    String realPath=path;
		    
			request.setAttribute("realCurrentPath", path);
			
			path=servletUtil.stringToUnicoder(path);
			
			System.out.println("relativePathToUnicoder"+path);
			
			request.setAttribute("currentPath", path);
		    getFolderInfo(request, realPath,isDispSharedFileDisp);
		    if(isDispSharedFileDisp)
		    request.getRequestDispatcher("/student/file/fileDisp.jsp").forward(request, response);
		    else
		    	request.getRequestDispatcher("/student/file/sharedFileDisp.jsp").forward(request, response);
		    	
		    return;
	      } catch (Exception e) {
			servletUtil.exceptionDeal(e, request, response);
			return;
		}	
		
	}
	
	private void currentFolderDeal(HttpServletRequest request,HttpServletResponse response,String currentFolder){
	 currentFolderDeal(request, response, currentFolder,true);
	}
	
private void firstEnter(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
	 HttpSession session=request.getSession();
     student stu=(student) session.getAttribute("student");
   File file=servletUtil.getFile(webAppConfig.webUplodPath,stu.getId()+"");
   if(file==null)
	   file=servletUtil.createFile(webAppConfig.webUplodPath,stu.getId()+"");
   //System.out.println("首次进入文件管理模块"+file);
   currentFolderDeal(request, response, stu.getId()+"");
  
}
//获得当前目录下的文件及共享文件的信息
private void getFolderInfo(HttpServletRequest request,String currentFolder,boolean isDispShared) throws myException, ServletException, IOException{
	Map<String,Map<String,myFile>> fileMap=servletUtil.getFileInfo(webAppConfig.webUplodPath, currentFolder,isDispShared);
   if(fileMap!=null&&fileMap.size()>0){
	   Map<String,myFile> files=fileMap.get("files");
	   if(files!=null&&files.size()>0)
		   request.setAttribute("files", files);
	   Map<String,myFile> folders=fileMap.get("folders");
	   if(folders!=null&&folders.size()>0)
		   request.setAttribute("folders", folders);
	   
	   if(isDispShared==true){
	   Map<String,myFile> sharedFiles=fileMap.get("sharedFiles");
	   if(sharedFiles!=null&&sharedFiles.size()>0)
		   request.setAttribute("sharedFiles", sharedFiles);
	   Map<String,myFile> sharedFolders=fileMap.get("sharedFolders");
	   if(sharedFolders!=null&&sharedFolders.size()>0)
		   request.setAttribute("sharedFolders", sharedFolders);
	   }
	   }
}
}
