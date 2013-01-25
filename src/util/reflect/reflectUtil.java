package util.reflect;

import java.lang.reflect.Method;
import java.util.Map;

public class reflectUtil {
	public static Class getClassByName(String className){
		 Class classf=null;
		try {
			classf = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return classf;
	 }
	 public static Method getMethod(Class classF,String methodName,Class...classes){
		 Method method=null;
		 try {
			method=classF.getDeclaredMethod(methodName,classes);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return method;
	 }
}
