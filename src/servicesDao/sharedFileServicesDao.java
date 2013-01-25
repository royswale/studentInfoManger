package servicesDao;

import java.util.List;

import exception.myException;
import pojo.sharedFile;
import pojo.student;

public interface sharedFileServicesDao extends servicesDao{
 int saveFile(String file, int stu_Id)throws myException;
 void delete(String condition,List<Object> values)throws myException;
 List<sharedFile> select(String condition,List<Object> values)throws myException;
 
}
