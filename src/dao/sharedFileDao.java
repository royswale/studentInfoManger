package dao;

import java.util.List;

import pojo.sharedFile;
import exception.myException;

public interface sharedFileDao  extends dao{
	sharedFile save(sharedFile sharedFile)throws myException;
	//boolean updatesharedFile(int sharedFile_id,sharedFile newsharedFile)throws myException;
	List<sharedFile> findSharedFiles(String conidtion,List<Object> values)throws myException;
	List<sharedFile> findSharedFiles(String conidtion,List<Object> values, int startResultset,int pageSize)throws myException;
	int deleteSharedFile(String conidtion,List<Object> values)throws myException;

}
