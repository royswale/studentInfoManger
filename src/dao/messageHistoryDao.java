package dao;

import java.util.List;

import pojo.messageHistory;

import exception.myException;

public interface messageHistoryDao  extends dao{
	messageHistory save(messageHistory messageHistory)throws myException;
	int updatemessageHistory(int messageHistory_id)throws myException;
	List<messageHistory> findmessageHistorys(String conidtion,List<Object> values)throws myException;
	List<messageHistory> findmessageHistorys(String conidtion,List<Object> values,int startResultset,int pageSize)throws myException;
	int deletemessageHistory(String conidtion,List<Object> values)throws myException;

}
