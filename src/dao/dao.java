package dao;

import java.util.List;

import exception.myException;

public interface dao {
	List<Object[]> select(String selectSQL,List<Object> values)throws myException;
	 void close();
	  void commit()throws myException;
	  void rollBack()throws myException;
	  List<Integer> executeUpdate(String sql,List<Object> values,boolean isauto)throws myException;
	  }
