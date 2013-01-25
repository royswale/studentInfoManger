package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import exception.myException;

import util.reflect.reflectUtil;
import util.reflect.pojo.method;
import util.reflect.pojo.staticClassMethod;

public class webAppConfig {
	public static String webPath;
	public static String webUplodPath;
	private static Map<Class,Class> beans=new HashMap<Class,Class>();
	private static Map<Class,staticClassMethod> factoryCreate=new HashMap<Class, staticClassMethod>();
	public static void configuration(String filePath){
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			initParser handler = new initParser();
			InputStream xmlStream=new FileInputStream(new File(filePath));
			parser.parse(xmlStream, handler);
			Map<String,Map<String, String>> factoryContext=handler.getFactroyContext();
			Map<String,String> commBean=handler.getCommonBean();
			Map<String,method> methodFa=handler.getMethodBean();
			if(commBean!=null&&commBean.size()>0)
			initBean(commBean);
			if(methodFa!=null&&methodFa.size()>0)
			initFactory(methodFa, factoryContext);
			if(methodFa!=null){
				initFactoryCreate(methodFa);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}
	}
	private static void initBean(Map<String,String> commBean){
		for(String key:commBean.keySet()){
			Class keyClass=reflectUtil.getClassByName(key);
			Class valueClass=reflectUtil.getClassByName(commBean.get(key));
			beans.put(keyClass, valueClass);
		}
	}
	private static void initFactoryCreate(Map<String,method> methodBean){
		for(String key:methodBean.keySet()){
			Class className=reflectUtil.getClassByName(key);
			method method=methodBean.get(key);
			String factoryBean=method.getClassName();
			String factoryMethodName=method.getMethodName();
			Class classFactroy=reflectUtil.getClassByName(factoryBean);
			Method factoryMethod=reflectUtil.getMethod(classFactroy, factoryMethodName,null);
			util.reflect.pojo.staticClassMethod methodI=new staticClassMethod();
			methodI.setClassName(classFactroy);
			methodI.setMethod(factoryMethod);
			factoryCreate.put(className, methodI);
		}
	}
 private static void initFactory(Map<String,method> methodBean,Map<String,Map<String, String>> factoryContext){
	 for(String key:factoryContext.keySet()){
		 method m=methodBean.get(key);
		 Map<String,String> properits=factoryContext.get(key);
		 Class classF=reflectUtil.getClassByName(m.getClassName());
		 Method methodN=reflectUtil.getMethod(classF, m.getMethodName(),Map.class);
		 methodBean.remove(key);
		 try {
			methodN.invoke(classF, properits);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
 }
 public static Object getBean(Class className) throws myException{
	if(factoryCreate.containsKey(className)){
	 staticClassMethod methods=factoryCreate.get(className);
	 Method methodName=methods.getMethod();
	 Class classF=methods.getClass();
	 try {
		return methodName.invoke(classF, null);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	} else if(beans.containsKey(className)){
		Class classn=beans.get(className);
		try {
			return classn.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}else{
	throw new myException("’“≤ªµΩ"+className.getName()+"¿‡");
 }
	return null;}
 
}
