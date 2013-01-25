package servlet.listener;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import javax.jms.ConnectionFactory;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.Configurator;

import config.webAppConfig;
import exception.myException;

import servlet.servletUtil;
import util.DBUtil.connectionFactory;

public class applicationListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc=sce.getServletContext();
		webAppConfig.webPath=sc.getRealPath("/");
	
		
		//对日志进行配置
		//配置日志文件的路径
		System.setProperty("webPath",webAppConfig.webPath);
		//获得log4j.properites路径
		String log4jproperites=sc.getInitParameter("logger");
		//配置日志上下文
		PropertyConfigurator.configure(webAppConfig.webPath+log4jproperites);
		
		String initPath=webAppConfig.webPath+sc.getInitParameter("init_xml");
		webAppConfig.configuration(initPath);
		webAppConfig.webUplodPath=webAppConfig.webPath+sc.getInitParameter("uploadFolder");
		File file=new File(webAppConfig.webUplodPath);
		try {
			webAppConfig.webUplodPath=file.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String encoder=sc.getInitParameter("server_enconder");
		if(servletUtil.noEmpty(encoder))
			servletUtil.server_encoder=encoder;
		encoder=sc.getInitParameter("my_enconder");
		if(servletUtil.noEmpty(encoder))
			servletUtil.stander_Encoder=encoder;
		
		file=new File(webAppConfig.webUplodPath);
	    if(!file.exists())
	    	file.mkdirs();
	}

}
