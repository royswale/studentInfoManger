package servicesDao;

import java.util.List;

import pojo.student;

import exception.myException;

public interface servicesDao {
	List<Object[]> executeQuery(String sql,List<Object> values)throws myException;
	List<Integer> executeUpdate(String sql,List<Object> values,boolean isauto)throws myException;
	 List select(String condition,List<Object> values,int startPostion,int pageSize)throws myException;
	 	  
}
