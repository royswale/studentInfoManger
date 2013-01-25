package util.DBUtil;

import java.beans.PropertyVetoException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.taglibs.standard.lang.jstl.test.Bean1;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class connectionFactory {
private static connectionFactory bean;
public static connectionFactory getObject(){
	synchronized (connectionFactory.class) {
		if(bean==null)
			bean=new connectionFactory();
	}
	return bean;
}
public static void init(Map<String,String> propeties){
	for(String key:propeties.keySet()){
		propertyInit(key, propeties.get(key));
	}
}
private connectionFactory(){}
private static ComboPooledDataSource ds=new ComboPooledDataSource(); 
private static void setDriverClass(String driverClass){
	try {
		ds.setDriverClass(driverClass);
	} catch (PropertyVetoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
private static void setAutoCommitOnClose(String booleanv){
	ds.setAutoCommitOnClose(Boolean.parseBoolean(booleanv));
}
private static void setMinPoolSize(String num){
	ds.setMinPoolSize(Integer.parseInt(num));
}
private static void setInitialPoolSize(String num){
	ds.setInitialPoolSize(Integer.parseInt(num));
}
private static void setJdbcUrl(String jdbcUrl){
	ds.setJdbcUrl(jdbcUrl);
}
private static void setMaxPoolSize(String num){
	ds.setMaxPoolSize(Integer.parseInt(num));
}
private static void setMaxStatements(String num){
	ds.setMaxStatements(Integer.parseInt(num));
}
private static void setPassword(String pwd){
	ds.setPassword(pwd);
}
private static void setUser(String user){
	ds.setUser(user);
}
public static Connection getConnection(){
	Connection conn=null;
	try {
		conn=ds.getConnection();
		conn.setAutoCommit(false);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return conn;
}
private static void propertyInit(String method,String param){
Class classFile=connectionFactory.class;
method=method.substring(0,1).toUpperCase()+method.substring(1);
try {
	Method methodName=classFile.getDeclaredMethod("set"+method,String.class);
   if(methodName!=null){
	methodName.setAccessible(true);
   methodName.invoke(null, param);}
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
}
}
