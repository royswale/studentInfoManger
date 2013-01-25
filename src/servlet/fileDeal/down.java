package servlet.fileDeal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import pojo.sharedFile;

import servicesDao.sharedFileServicesDao;
import servlet.servletUtil;

import config.extendsParser;
import config.initParser;
import config.webAppConfig;
import exception.myException;

public class down extends HttpServlet {
private Map<String,String> extsMap;
	public void init() throws ServletException {
		super.init();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
			extendsParser handler = new extendsParser();
			ServletContext sc=getServletContext();
			String xmlFile=sc.getInitParameter("extends");
			String filePath=sc.getRealPath(xmlFile);
			InputStream xmlStream=new FileInputStream(new File(filePath));
			parser.parse(xmlStream, handler);
			extsMap=handler.getExtMap();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
     String currentPath=request.getParameter("currentPath");
     if(servletUtil.noEmpty(currentPath))
     {
    	 String realCurrentPath=servletUtil.getMethodUnicoderToString(currentPath);
    	 if(servletUtil.noEmpty(realCurrentPath)){ 
    		 realCurrentPath=realCurrentPath.trim();
    		 request.setAttribute("currentPath",currentPath);
    	     File file=servletUtil.getFile(webAppConfig.webUplodPath, realCurrentPath);
           if(file!=null){
        	   response.setContentType("text/html");
        	   String fileName=file.getName();
        	    fileName=servletUtil.stringToUnicoder(fileName);
        	   response.setHeader("content-disposition","attachment; filename="+fileName);
        	   downBytes(response.getOutputStream(), file);}
           else
           throw new IOException(realCurrentPath+"文件不存在");
    	 }else{
    		 throw new IOException("CurrentPath读取错误");
    	 }
    	 }
     else
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
           String fileidS=request.getParameter("fileId");
           if(servletUtil.noEmpty(fileidS)){
        	   int file_id=Integer.parseInt(fileidS);
        	   try {
				sharedFileServicesDao sfDao=(sharedFileServicesDao)webAppConfig.getBean(sharedFileServicesDao.class);
			   List<sharedFile> sfs=sfDao.select("id=?", servletUtil.changeToList(new Object[]{file_id}));
			     if(servletUtil.hasElements(sfs)){
			    	 sharedFile sf=sfs.get(0);
			    	 String path=webAppConfig.webUplodPath+sf.getFilePath();
			    	 File file=new File(path);
			    	 if(file.exists()&&file.isFile()){
			    		 int extStar=path.lastIndexOf(".");
			    		 if(extStar<=0)
			    			 extStar=path.lastIndexOf("/");
			    		 String extName=path.substring(extStar+1,path.length());
		                 response.setContentType(extsMap.get(extName));
		                 ServletOutputStream sos=response.getOutputStream();
		                 downBytes(sos, file);
		                 return;
			    	 }else{
			    		 file=new File(webAppConfig.webUplodPath+"/nofound.pnp");
			    		 response.setContentType(extsMap.get("pnp"));
		                 ServletOutputStream sos=response.getOutputStream();
		                 downBytes(sos, file);
		                 return;
			    	 }
			    
			     }
        	   
        	   } catch (myException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	   return;
           }
	}
	/**
	 * 
	 * @param os 传入的输出流参数
	 * @param file 需要下载的文件
	 */
private void downBytes(OutputStream os,File file) throws IOException{
	if(file==null||!file.exists()){
		throw new IOException("文件不存在");
	}
	byte[] bytes=new byte[256];
	FileInputStream is=new FileInputStream(file);
	int size=-1;
	while((size=is.read(bytes))!=-1){
		os.write(bytes,0,size);
	}
	if(is!=null)
		is.close();
	os.flush();
	os.close();
}
}
