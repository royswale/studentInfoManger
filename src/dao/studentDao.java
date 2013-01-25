package dao;

import java.util.List;

import pojo.student;
import exception.myException;

public interface studentDao  extends dao {
student save(student stu)throws myException;
int updateStudent(int stu_id,student newStu)throws myException;
List<student> findStudents(String conidtion,List<Object> values)throws myException;
//�ӵ�startResult����¼��ʼ����ʾresultSetLength��
List<student> findStudents(String conidtion,List<Object> values,int startResultset,int pageSize)throws myException;
int deleteStudent(String conidtion,List<Object> values)throws myException;
int[] saveStudents(List<student> students)throws myException;
}
