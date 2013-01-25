package dao;

import java.util.List;

import pojo.message;

import exception.myException;

public interface messageDao extends dao{
	message save(message message)throws myException;
	List<message> findMessages(String conidtion,List<Object> values)throws myException;
	List<message> findMessages(String conidtion,List<Object> values,int startResultset,int pageSize)throws myException;
	int deleteMessage(String conidtion,List<Object> values)throws myException;
}
