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
      //��ʾ��post��ʽ�����currentPath�����Բ���Ҫ�����ַ�����ת��
        if(!servletUtil.noEmpty(currentPath)){
        	currentPath=request.getParameter("currentPath");
        	//��һ�ε�½ʱcurrentPathʲô��û��
        	if(servletUtil.noEmpty(currentPath))
           currentPath=URLDecoder.decode(currentPath,servletUtil.stander_Encoder);	
        }
        String sharedFileRootPath =request.getParameter("sharedFileRootPath");
        if(servletUtil.noEmpty(sharedFileRootPath)){
        	sharedFileRootPath=servletUtil.getMethodUnicoderToString(sharedFileRootPath);
        }
        
        
        //ʹ��postֱ�Ӵ����
        String newFolder=request.getParameter("newFolder");
        
        String deleteFileName=request.getParameter("deleteFileName");
        //���Ҫɾ���Ĺ����ļ���Ŀ¼������
        String deleteSharedFileName=request.getParameter("deleteSharedFileName");
        if(servletUtil.noEmpty(deleteSharedFileName)){
        	deleteSharedFileName=servletUtil.getMethodUnicoderToString(deleteSharedFileName);
        }
        
        //����get���ݵ�unicode�����ݱ����������
        if(servletUtil.noEmpty(deleteFileName))
        deleteFileName=servletUtil.getMethodUnicoderToString(deleteFileName);
        //������һ��Ŀ¼
        String folder=request.getParameter("folder");
        if(servletUtil.noEmpty(folder)){
        	folder=servletUtil.changeCharater(folder);
        }
        //������һ��Ŀ¼�Ĳ���
        if(servletUtil.noEmpty(folder)&&"parentFolder".equals(folder)){
        	System.out.println("����������path="+currentPath);
        	//�жϵ�ǰĿ¼����һ��Ŀ¼�ǲ��ǵ�ǰ�û��ĸ�Ŀ¼
        	String currentPathTemp=currentPath.substring(0,currentPath.lastIndexOf(File.separator));
        	if(!currentPathTemp.equals(File.separator))
            	currentPath=currentPathTemp;//��������û��ĸ�Ŀ¼����ǰĿ¼����ϲ�Ŀ¼
        }
        if(servletUtil.noEmpty(currentPath)){
    	   //����һ��Ŀ¼
    	   if(servletUtil.noEmpty(newFolder)){
    		   servletUtil.createFile(webAppConfig.webUplodPath,currentPath+File.separator+newFolder);
    	   }
    	   //ɾ��ָ�����ֵ��ļ�
    	   if(servletUtil.noEmpty(deleteFileName)){
    		   System.out.println(deleteFileName);
    		   String folderPath=webAppConfig.webUplodPath+File.separator+currentPath;
    		  try {
				servletUtil.delete(folderPath,deleteFileName);
				//����й����ļ���ɾ�����ݿ��еĹ����¼������������Ŀ¼�еĹ����¼
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
		String condition="·��  like ? and ·�� not like ?";
		List<Object> values=new ArrayList<Object>();
		values.add(servletUtil.pathToDB(filePath+"%"));
		values.add(servletUtil.pathToDB(filePath+File.separator+"%"+File.separator+"%"));
		sharedFileServicesDao sfsDao=(sharedFileServicesDao) webAppConfig.getBean(sharedFileServicesDao.class);
	   sfsDao.delete(condition, values);	
	}
	//���빲���ļ��е���ʾ���ǽ�����ͨ�ļ��е���ʾ
	private void currentFolderDeal(HttpServletRequest request,HttpServletResponse response,String currentFolder,boolean isDispSharedFileDisp){
		System.out.println("��ǰ�����Ŀ¼��"+currentFolder);
		try {
			File file=servletUtil.getFile(webAppConfig.webUplodPath, currentFolder) ;
		if(file==null)
			throw new myException("�����"+currentFolder+"������");
		
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
   //System.out.println("�״ν����ļ�����ģ��"+file);
   currentFolderDeal(request, response, stu.getId()+"");
  
}
//��õ�ǰĿ¼�µ��ļ��������ļ�����Ϣ
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
