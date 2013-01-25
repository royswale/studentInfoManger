package servicesDao;

import java.util.Collection;
import java.util.List;

import exception.myException;
import pojo.student;

public interface studentServicesDao extends servicesDao{
 public void saveStudent(student stu)throws myException;
 public void saveStudents(Collection<student> students)throws myException;
 public List<student> select(String condition,List<Object> values)throws myException;
 public void updateStudent(student stu)throws myException;
 public void deleteStudent(String condition,List<Object> values)throws myException;
 }
