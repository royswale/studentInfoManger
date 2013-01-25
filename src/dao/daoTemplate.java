package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import config.webAppConfig;

import exception.myException;

public class daoTemplate {
   private static ThreadLocal<Connection> threadConns=new ThreadLocal<Connection>();
protected Connection conn;
protected ResultSet rs;
protected PreparedStatement pre;
private static Logger log=Logger.getLogger(daoTemplate.class);
public Connection getConnection(){
	Connection conn=threadConns.get();
	if(conn==null){
		try {
			conn=(Connection) webAppConfig.getBean(java.sql.Connection.class);
		} catch (myException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		threadConns.set(conn);
	}
	return conn;
}
public daoTemplate(){
	this.conn=getConnection();
}
protected void close(ResultSet rs,PreparedStatement  pre,Connection conn){
	if(rs!=null)
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	if(pre!=null)
		try {
			pre.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	if(conn!=null)
		try {
			threadConns.remove();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
protected List<Integer> createSQLandExecuteUpdate(String preSQL,String condition,List<Object> values,boolean isAuto)throws myException{
	String sql=preSQL;
	if(!isEmpty(condition)){
		sql=sql+" where "+condition;
	}
	return executeUpdate(sql, values,isAuto);
}
protected List<Integer> createSQLandExecuteUpdate(String preSQL,String condition,List<Object> values)throws myException{
	String sql=preSQL;
	if(!isEmpty(condition)){
		sql=sql+" where "+condition;
	}
	return executeUpdate(sql, values);
}
//执行insert、update和delete方法
protected List<Integer> executeUpdate(String sql,List<Object> values)throws myException{
	return executeUpdate(sql, values,false);	
}
//在insert中是否有自动生成内容生成，isauto==true,表示要获得自动生成的键值
public List<Integer> executeUpdate(String sql,List<Object> values,boolean isauto)throws myException{
	log.info("daoTemplate执行操作性语句:"+sql);
	int count=0;
	try{	
		pre=conn.prepareStatement(sql,(isauto==true)?Statement.RETURN_GENERATED_KEYS:Statement.NO_GENERATED_KEYS );
		int i=1;
		if(values!=null){
		for(Object o:values){
			pre.setObject(i, o);
			i++;
		}}
		count=pre.executeUpdate();
	  if(!isauto){
		close(null, pre, null);
	  List<Integer> ids=new ArrayList<Integer>();
	  ids.add(new Integer(count));
	  return ids;
	  }
	  else{
		  List<Integer> ids=new ArrayList<Integer>();
		 ResultSet rs=pre.getGeneratedKeys();
		 while(rs.next()){
			 ids.add(rs.getInt(1));
		 }
		 close(rs, pre,null);
		 return ids;
	  }
	}
	catch(Exception e){
		throw new myException(e);
	}
	}
//把查询结果集的每条记录转化成object[]数组，在多表查询中应用
protected List<Object[]> getResultObject(ResultSet rs)throws myException{
	List<Object[]> result=new ArrayList<Object[]>();
	   try{ 
		   ResultSetMetaData rsMeta=rs.getMetaData();
	    int length=rsMeta.getColumnCount();
	    while(rs.next()){
	    Object[] objs=new Object[length];
	      for(int i=1;i<=length;i++){
	    	objs[i-1]=rs.getObject(i);
	    }
	    result.add(objs);
	    }
	   }catch(Exception e){throw new myException(e);}finally{
		   close(rs, pre, null);
	   }
	    return result;	
	
}
//单笔业务dao查询，里面没有关闭pre和rs
  protected ResultSet executeQuerty(String sql,List<Object> values)throws myException{
	  try{pre=conn.prepareStatement(sql);
		int i=1;
		if(values!=null&&values.size()>0)
		for(Object o:values){
			pre.setObject(i, o);
			i++;
		}
		rs=pre.executeQuery();	
	}
	catch(Exception e){
		throw new myException(e);
	}
	return rs;	
	  
  }
public void commit() throws myException{
	try {
		conn.commit();
	} catch (SQLException e) {
		e.printStackTrace();
		throw new myException(e);
	}
}
public void close(){
	close(rs, pre, conn);
}
public void rollBack() throws myException{
	try {
		conn.rollback();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new myException(e);
	}
}
  //多表的综合查询
public List<Object[]> select(String selectSQL,List<Object> values)throws myException{
	List<Object[]> result=null;
   try{ rs=executeQuerty(selectSQL, values);
    result=getResultObject(rs);
    }catch(Exception e){throw new myException(e);}
    return result;	
}
public static boolean isEmpty(String content){
	if(content!=null&&!"".equals(content.trim())){
		return false;
	}
	else
		return true;
}
//用于把条件中所有没有其他修饰的id变为a.id
private String changeCondition(String condition){
	 condition=condition.toLowerCase();
	 Pattern pattern=Pattern.compile("([\\s=])(id)\\b");
		Matcher matcher=pattern.matcher(condition);
		StringBuffer sb=new StringBuffer();
		while(matcher.find()){
			matcher.appendReplacement(sb, matcher.group(1)+"a.id");
		}
		matcher.appendTail(sb);
    return sb.toString();
}
public ResultSet queryPage(String sql,String condition,List<Object> values,int startResultset,int pageSize)throws myException{
	if(isEmpty(condition)){
		values=new ArrayList<Object>();
	}else{
		if(sql.indexOf("a.")==-1){
		sql=sql+" where "+condition;
		}else{
		sql=sql+" where"+changeCondition(" "+condition);	
		}
	}
		sql=sql+" order by id desc";
	if(startResultset>=0&&pageSize>0){
		sql=sql+" limit ?,?";
		values.add(startResultset);
		values.add(pageSize);
	}
	log.info("daoTemplate执行查询:"+sql);
	return executeQuerty(sql, values);
}
}
