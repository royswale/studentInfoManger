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
		throw new UnsupportedEncodingException("内容为空");
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
//分页显示的模板
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
		throw new IOException(folderName+"已经存在了");
	}
	return file;
}
public static String relativePath(String rootFolder,File currentFile) throws myException, IOException{
	File file=new File(rootFolder);
	if(!file.exists())
		throw new myException("根目录"+rootFolder+"不存在");
	if(currentFile.getCanonicalPath().indexOf(file.getCanonicalPath())==0){
		return currentFile.getCanonicalPath().substring(file.getCanonicalPath().length());
	}else{
		throw new myException("当前文件"+currentFile.getCanonicalPath()+"不在"+rootFolder+"下");
	}
}
//删除指定目录下的文件
public static void delete(String folder,String file)throws myException{
	File fileTemp=getFile(folder,file);
	if(fileTemp!=null){
	   delete(fileTemp);	
	}else{
		throw new myException(file+"在"+folder+"中不存在");
	}
}
private static void delete(File file) throws myException{
   if(!file.exists())
	   throw new myException("文件不存在");
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
//得到文件夹下共享文件和物理文件的信息
public static Map<String,Map<String,myFile>> getFileInfo(String rootFolder,String currentFolder,boolean addShared_File)throws myException, IOException{
	File rootF=new File(rootFolder);
	File currentF=new File(rootFolder+File.separator+currentFolder);
	if(!currentF.exists())
		throw new myException(currentFolder+"不存在");
	return getFileInfo(rootF, currentF,addShared_File);
}
//得到指定File路径下的共享File路径的共享文件和物理文件的信息
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
	//查询数据库中的共享文件
    if(addShared_File==true)
	sharedList=findSharedFile(rootFolder, currentFolder);
	String fileName;
	//把当前目录下的文件和目录信息读出
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
				throw new myException("当前文件与根目录路径不吻合");}
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

//获得当前路径下的共享文件和物理文件信息，如addShared_file==false,表示不要读取共享文件
public static Map<String,Map<String,myFile>> getFileInfo(File rootFolder,File currentFolder) throws myException, IOException{
 return getFileInfo(rootFolder, currentFolder,true); 
}
//得到文件名或者目录名
public static String getFileName(String filePath) throws IOException{
  filePath=getFileAbsoultString(filePath);
   if(noEmpty(filePath)){
	   int index=filePath.lastIndexOf(File.separator);
	     if(index!=filePath.length())
	   return filePath.substring(index+1);
   }
   return null;
}
//获得指定文件的真实路径
public static String getFileAbsoultString(String filePath) throws IOException{
	File file=existFile(filePath);
	if(file!=null)
		return file.getCanonicalPath();
	return null;
}
//判断文件是否存在
public static File existFile(String filePath){
	File file=new File(filePath);
	if(file.exists())
		return file;
	else
		return null;
}
//获得指定目录的下的共享信息
public static Map<String,myFile> findSharedFile(String rootFolder,String currentFolder) throws myException, IOException{
	File rootFolderFile=existFile(rootFolder);
	if(rootFolderFile==null)
		throw new myException(rootFolder+"不存在");
	File currentFolderFile=existFile(rootFolder+File.separator+currentFolder);
	if(currentFolderFile==null)
		throw new myException(currentFolder+"不存在");
	return findSharedFile(rootFolderFile, currentFolderFile);
}


private static Map<String,myFile> findSharedFile(File rootFolder,File currentFolder) throws myException, IOException{
	String path=null;
	sharedFileServicesDao sfDao=(sharedFileServicesDao)webAppConfig.getBean(sharedFileServicesDao.class);
	String condition="路径 like ? and 路径 not like ?";
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
		System.err.println("共享资源："+sharedFs.size());
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
