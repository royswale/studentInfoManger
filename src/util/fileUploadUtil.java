package util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.filefilter.SuffixFileFilter;

public class fileUploadUtil {
 private static SimpleDateFormat format=new SimpleDateFormat("yyyymmDDhhMMss");
private static DiskFileItemFactory disk=new DiskFileItemFactory();
private static SuffixFileFilter filter;
private static ServletFileUpload up;
private static int maxSize;
static{
	disk.setRepository(disk.getRepository());//�趨��ʱĿ¼
	up=new ServletFileUpload(disk);
	maxSize=2*1024*1024;
	String[] suffixs=new String[]{".exe",".bat",".jsp",".sn"};//�����ϴ��ļ�����չ��
	filter=new SuffixFileFilter(suffixs);//�����ļ���׺������
    
}
public static Map<String,Object> parse(HttpServletRequest request,String userfilePath){
	Map<String,Object> params=new HashMap<String, Object>();
	try {
		List list=up.parseRequest(request);
		Iterator i=list.iterator();
		List<File> files=new ArrayList<File>();
		params.put("files", files);
        while(i.hasNext()){
       	 FileItem fm=(FileItem) i.next();
       	 if(!fm.isFormField()){//fm.isFormField()==true��ʾfm������������
       		 String filePath=fm.getName();//����ļ�ȫ·����
       		 String fileName="";
       		 int startIndex=filePath.lastIndexOf("\\");
       		 if(startIndex!=-1){
       			 fileName=filePath.substring(startIndex+1);
       		 }else{
       			 fileName=filePath;
       		 }
       		 if(fm.getSize()>maxSize){
       			 throw new FileUploadException("�ļ�̫���ˣ�������2M");
       			 
       		 }
       		 String fileSize=new Long(fm.getSize()).toString();
       		 if((fileName==null||"".equals(fileName))&&("0".equals(fileSize))){
       			continue;
       			 
       		 }
       		 File savefiFile=new File(userfilePath,fileName);
       		 boolean res=filter.accept(savefiFile);//�����ļ���׺�������Ĺ��˷��������ϴ��ļ����ͽ��й���
       		 if(!res){
       			 if(savefiFile.exists()){
       				 Date date=new Date();
       				 int dotPosition=fileName.indexOf(".");
       				 fileName=fileName.substring(0,dotPosition)+format.format(date)+"."+fileName.substring(dotPosition+1);
       			    savefiFile=new File(userfilePath,fileName);
       			 }
       		 fm.write(savefiFile);
       		  files.add(savefiFile);
       		//System.out.println(savefiFile.getCanonicalPath());
       		 }
       		 else{
       			throw new FileUploadException("�ļ������ǽ�ֹ�ϴ���");
       		 }
       	}
        else{
   	String paramName=fm.getFieldName();
   	String content=fm.getString("utf-8");
   	params.put(paramName, content);
   	}
	  }
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return params;
}
}
