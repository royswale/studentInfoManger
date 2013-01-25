package servlet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.event.FolderListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.webAppConfig;

import pojo.myFile;
import pojo.sharedFile;
import pojo.student;

import exception.myException;

import servicesDao.servicesDao;
import servicesDao.sharedFileServicesDao;

public class servletUtil {
	public static String server_encoder="ISO-8859-1";
	public static String stander_Encoder="UTF-8";
public static String stringToUnicoder(String content){
	try {
		return URLEncoder.encode(content,stander_Encoder);
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
	return null;
}	
public static String postMethodUnicoderToString(String content){
	try {
		if(noEmpty(content))
		return URLDecoder.decode(content,stander_Encoder);
		throw new UnsupportedEncodingException("����Ϊ��");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
}
public static String getMethodUnicoderToString(String getContent){
	try {if(noEmpty(getContent))
		getContent=changeCharater(getContent, server_encoder, stander_Encoder);
		if(noEmpty(getContent))
		return URLDecoder.decode(getContent,stander_Encoder);
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}
public static String pathToDB(String primiaryPath){
	if("\\".equals(File.separator)){
		return primiaryPath.replaceAll("\\\\", "/");
	}
	return primiaryPath;
}
public static String DBToPath(String primiaryPath){
	if("\\".equals(File.separator)){
		return primiaryPath.replaceAll("/", "\\\\");
	}
	return primiaryPath;
}
public static List<Object> changeToList(Object[] objs){
	List<Object> list=new ArrayList<Object>();
	if(objs!=null&&objs.length>0){
		for(int i=0;i<objs.length;i++){
			list.add(objs[i]);
		}
	}
	return list;
}
public static String changeCharater(String content,String primaryCharater,String newChararter){
	try {
		return new String(content.getBytes(primaryCharater),newChararter);
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
}
public static String changeCharater(String content){
    return changeCharater(content,server_encoder, stander_Encoder);
}
public static boolean noEmpty(String content){
	if(content!=null&&!"".equals(content.trim()))
		return true;
	return false;
}
public static boolean hasElements(List list){
	if(list!=null&&list.size()>0)
		return true;
	return false;
}
public static void exceptionDeal(Exception e,HttpServletRequest request,HttpServletResponse response){
	String message=e.getMessage();
	request.setAttribute("message", message);
	e.printStackTrace();
    try {
		request.getRequestDispatcher("/student/message.jsp").forward(request, response);
	} catch (ServletException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
}
//��ҳ��ʾ��ģ��
public static Map<String,Object> resultPageDisp(servicesDao dao,String condition,List<Object> values,String tableName,int page,int pageSize) throws myException{
	 String sql="select count(*) from "+tableName+" where "+condition;
	 List<Object[]> result=dao.executeQuery(sql,values);
	 Map<String,Object> resultMap=new HashMap<String, Object>();  
	 int num=-1;
	   if(servletUtil.hasElements(result)){
		   Long i=(Long)result.get(0)[0];
		   num=i.intValue();
		   result.clear();
	   }
	   int pageCount=(num+pageSize-1)/pageSize;
 	  if(page>=pageCount)
 		  page=pageCount-1;
 	  if(page<0)
 		  page=0;
 	  
 	result=dao.select(condition, values, page*pageSize==0?1:page*pageSize, pageSize);
   resultMap.put("resultCounts",num);
   resultMap.put("pageCount", pageCount);
   resultMap.put("currentPage", page);
   resultMap.put("results", result);
 	return resultMap;	
}
public static File getFile(String rootFolder,String filePath){
	File file=new File(rootFolder+File.separator+filePath);
	if(file.exists())
		return file;
	else
		return null;
}
public static File createFile(String rootFolder,String folderName) throws IOException{
	File file=new File(rootFolder+File.separator+folderName);
	if(!file.exists())
		file.mkdirs();
	else{
		throw new IOException(folderName+"�Ѿ�������");
	}
	return file;
}
public static String relativePath(String rootFolder,File currentFile) throws myException, IOException{
	File file=new File(rootFolder);
	if(!file.exists())
		throw new myException("��Ŀ¼"+rootFolder+"������");
	if(currentFile.getCanonicalPath().indexOf(file.getCanonicalPath())==0){
		return currentFile.getCanonicalPath().substring(file.getCanonicalPath().length());
	}else{
		throw new myException("��ǰ�ļ�"+currentFile.getCanonicalPath()+"����"+rootFolder+"��");
	}
}
//ɾ��ָ��Ŀ¼�µ��ļ�
public static void delete(String folder,String file)throws myException{
	File fileTemp=getFile(folder,file);
	if(fileTemp!=null){
	   delete(fileTemp);	
	}else{
		throw new myException(file+"��"+folder+"�в�����");
	}
}
private static void delete(File file) throws myException{
   if(!file.exists())
	   throw new myException("�ļ�������");
	if(file.isFile()){
	file.delete();}
	else{
		File[] files=file.listFiles();
		if(files!=null&&files.length!=0){
			for(int i=0;i<files.length;i++)
				if(files[i].isFile())
					files[i].delete();
				else
					delete(files[i]);}
		  file.delete();
	}
}

public static Map<String,Map<String,myFile>> getFileInfo(String rootFolder,String currentFolder)throws myException, IOException{
	return getFileInfo(rootFolder, currentFolder,true);
}
//�õ��ļ����¹����ļ��������ļ�����Ϣ
public static Map<String,Map<String,myFile>> getFileInfo(String rootFolder,String currentFolder,boolean addShared_File)throws myException, IOException{
	File rootF=new File(rootFolder);
	File currentF=new File(rootFolder+File.separator+currentFolder);
	if(!currentF.exists())
		throw new myException(currentFolder+"������");
	return getFileInfo(rootF, currentF,addShared_File);
}
//�õ�ָ��File·���µĹ���File·���Ĺ����ļ��������ļ�����Ϣ
public static Map<String,Map<String,myFile>> getFileInfo(File rootFolder,File currentFolder,boolean addShared_File) throws myException, IOException{
	Map<String,Map<String,myFile>> fileMap=new HashMap<String, Map<String,myFile>>();
	if(!currentFolder.exists()||!currentFolder.isDirectory())
		return null;
	File[] files=currentFolder.listFiles();
	Map<String,myFile> fileList=new HashMap<String,myFile>();
	Map<String,myFile> foldereList=new HashMap<String,myFile>();
	Map<String,myFile> sharedFolders=new HashMap<String,myFile>();
	Map<String,myFile> sharedFiles=new HashMap<String,myFile>();
	Map<String,myFile> sharedList=null;
	if(files==null||files.length==0){
		return null;
	}
	String path;
	//��ѯ���ݿ��еĹ����ļ�
    if(addShared_File==true)
	sharedList=findSharedFile(rootFolder, currentFolder);
	String fileName;
	//�ѵ�ǰĿ¼�µ��ļ���Ŀ¼��Ϣ����
	for(int i=0;i<files.length;i++){
		if(files[i].getCanonicalPath().indexOf(rootFolder.getCanonicalPath())==0)
			{path=files[i].getCanonicalPath().substring(rootFolder.getCanonicalPath().length());
			  fileName=getFileName(files[i].getCanonicalPath());
			  myFile myFile=new myFile();
			  myFile.setFileName(fileName);
			  myFile.setFilePath(path);
			  if(files[i].isFile())
			  fileList.put(path,myFile);
			  else
			  foldereList.put(path,myFile);	  }
			else{
				throw new myException("��ǰ�ļ����Ŀ¼·�����Ǻ�");}
		}
	if(fileList.size()>0)
		fileMap.put("files",fileList);
	if(foldereList.size()>0)
		fileMap.put("folders", foldereList);
	if(sharedList!=null&&sharedList.size()>0){
		for(String key:sharedList.keySet()){
			if(sharedList.get(key).getFileName().lastIndexOf(".")!=-1){
				fileList.remove(key);
				sharedFiles.put(key, sharedList.get(key));
			}else{
				foldereList.remove(key);
				sharedFolders.put(key, sharedList.get(key));
			}
				}
		if(sharedFiles!=null&&sharedFiles.size()>0)
		fileMap.put("sharedFiles", sharedFiles);
		if(sharedFolders!=null&&sharedFolders.size()>0)
			fileMap.put("sharedFolders",sharedFolders);
	}
	return fileMap;
		
}

//��õ�ǰ·���µĹ����ļ��������ļ���Ϣ����addShared_file==false,��ʾ��Ҫ��ȡ�����ļ�
public static Map<String,Map<String,myFile>> getFileInfo(File rootFolder,File currentFolder) throws myException, IOException{
 return getFileInfo(rootFolder, currentFolder,true); 
}
//�õ��ļ�������Ŀ¼��
public static String getFileName(String filePath) throws IOException{
  filePath=getFileAbsoultString(filePath);
   if(noEmpty(filePath)){
	   int index=filePath.lastIndexOf(File.separator);
	     if(index!=filePath.length())
	   return filePath.substring(index+1);
   }
   return null;
}
//���ָ���ļ�����ʵ·��
public static String getFileAbsoultString(String filePath) throws IOException{
	File file=existFile(filePath);
	if(file!=null)
		return file.getCanonicalPath();
	return null;
}
//�ж��ļ��Ƿ����
public static File existFile(String filePath){
	File file=new File(filePath);
	if(file.exists())
		return file;
	else
		return null;
}
//���ָ��Ŀ¼���µĹ�����Ϣ
public static Map<String,myFile> findSharedFile(String rootFolder,String currentFolder) throws myException, IOException{
	File rootFolderFile=existFile(rootFolder);
	if(rootFolderFile==null)
		throw new myException(rootFolder+"������");
	File currentFolderFile=existFile(rootFolder+File.separator+currentFolder);
	if(currentFolderFile==null)
		throw new myException(currentFolder+"������");
	return findSharedFile(rootFolderFile, currentFolderFile);
}


private static Map<String,myFile> findSharedFile(File rootFolder,File currentFolder) throws myException, IOException{
	String path=null;
	sharedFileServicesDao sfDao=(sharedFileServicesDao)webAppConfig.getBean(sharedFileServicesDao.class);
	String condition="·�� like ? and ·�� not like ?";
	List<Object> values=new ArrayList<Object>();
	String relativePath=currentFolder.getCanonicalPath().substring(rootFolder.getCanonicalPath().length());
	values.add(pathToDB(relativePath+File.separator+"%"));
	values.add(pathToDB(relativePath+File.separator+"%"+File.separator+"%"));
	for(int i=0;i<2;i++){
		System.err.println(values.get(i));
	}
	List<sharedFile> sharedFs=sfDao.select(condition, values);
	Map<String,myFile> sharedMap=null;
	if(hasElements(sharedFs)){
		System.err.println("������Դ��"+sharedFs.size());
		sharedMap=new HashMap<String, myFile>();
		String filePath=null;
		String fileName;
		myFile myFile;
		for(sharedFile sf:sharedFs){
		  filePath=sf.getFilePath();
		   fileName=getFileName(webAppConfig.webUplodPath+File.separator+filePath);
		  myFile=new myFile();
		  myFile.setFileName(fileName);
		  myFile.setFilePath(filePath);
		  sharedMap.put(filePath, myFile);
		}
	}
	return sharedMap;
}
}
