package servicesDao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import config.webAppConfig;

import dao.professionDao;
import dao.studentDao;

import pojo.profession;
import pojo.student;
import exception.myException;
import servicesDao.studentServicesDao;

public class studentDaoServicesImpl implements studentServicesDao {

	public void deleteStudent(String condition, List<Object> values)
			throws myException {
		studentDao stuDao=(studentDao) webAppConfig.getBean(studentDao.class);
		try{stuDao.deleteStudent(condition, values);
		   stuDao.commit();
		}catch(myException e){
			stuDao.rollBack();
		}finally{
			if(stuDao!=null)
			stuDao.close();}
	}
  private void bindStudentAndProfession(student stu,studentDao stuDao,professionDao proDao,Map<String,Integer> prosMap) throws myException{
	 if(stu.getProfession()!=null){
			  String proName=stu.getProfession().getName();
			  Integer id=prosMap.get(proName);
			  if(id!=null){
				  stu.setProfession_id(id);}
			  else{
				  profession p=stu.getProfession();
				  p=proDao.save(p);
				  proDao.commit();
				  stu.setProfession_id(p.getId());
				  prosMap.put(p.getName(), p.getId());
			  }
			}
  }
	public void saveStudent(student stu) throws myException {
		studentDao stuDao=(studentDao) webAppConfig.getBean(studentDao.class);
	    try{
	    	if(stu==null||stu.getStuId()==null||"".equals(stu.getStuId().trim()))
	    		throw new myException("学生对象为空，或者学号为空");
	    	List<Object> values=new ArrayList<Object>();
	    	values.add(stu.getStuId());
	    	List<student> students=stuDao.findStudents("学号=?", values);
	    	if(students!=null&&students.size()>0)
	    		throw new myException(stu.getStuId()+"在数据库中已经存在");
			stuDao.save(stu);
		   stuDao.commit();
		}catch(Exception e){
			e.printStackTrace();
			stuDao.rollBack();
			throw new myException(e);
		}
		finally{
			if(stuDao!=null)
				stuDao.close();
		}
		}
  //没有判读学生学号是否在数据库中存在
	public void saveStudents(Collection<student> students) throws myException {
		int size=100;
		List<student> saveStudentTemp=new ArrayList<student>();
		student stu=null;
		Iterator<student> iterator=students.iterator();
		
		studentDao stuDao=(studentDao) webAppConfig.getBean(studentDao.class);
		try{
		while(iterator.hasNext()){
			stu=iterator.next();
			saveStudentTemp.add(stu);
			if(saveStudentTemp.size()>=size){
		        stuDao.saveStudents(saveStudentTemp);
		        saveStudentTemp.clear();
			}
		}
		if(saveStudentTemp.size()>0)
			stuDao.saveStudents(saveStudentTemp);
		 stuDao.commit();
		}catch(myException e){
			stuDao.rollBack();
			throw e;
		}finally{
			if(stuDao!=null)
				stuDao.close();
		}
		
	}

	public List<student> select(String condition, List<Object> values)
			throws myException {
		return select(condition, values,-1,-1);
			}

	public void updateStudent(student stu) throws myException {
		studentDao stuDao=(studentDao) webAppConfig.getBean(studentDao.class);
	   		try{ if(stu!=null&&stu.getId()>0){
	   			stuDao.updateStudent(stu.getId(),stu);
		    stuDao.commit();}
	   		else{
	   			throw new myException("更新的学生为空或者id不正确");
	   		}
		}catch(myException e){
			stuDao.rollBack();
			e.printStackTrace();
			throw e;
		}finally{
			if(stuDao!=null)
				stuDao.close();
		}
		
	}

	public List select(String condition, List<Object> values,
			int startPostion, int pageSize) throws myException {
		studentDao stuDao=(studentDao) webAppConfig.getBean(studentDao.class);
	    List<student> students=null;
		try{ students=stuDao.findStudents(condition, values, startPostion, pageSize);
		    stuDao.commit();
		}catch(myException e){
			stuDao.rollBack();
			e.printStackTrace();
			throw e;
		}finally{
			if(stuDao!=null)
				stuDao.close();
		}
		return students;

	}
	public List<Object[]> executeQuery(String sql, List<Object> values) throws myException{
	studentDao stuDao=(studentDao) webAppConfig.getBean(studentDao.class);
	try{ List<Object[]> list=stuDao.select(sql, values);
	    stuDao.commit();
	    return list;
	}catch(myException e){
		stuDao.rollBack();
		e.printStackTrace();
		throw e;
	}finally{
		if(stuDao!=null)
			stuDao.close();
	}
	}
	public List<Integer> executeUpdate(String sql, List<Object> values,boolean isauto)
			throws myException {
		studentDao stuDao=(studentDao) webAppConfig.getBean(studentDao.class);
		try{ List<Integer> list=stuDao.executeUpdate(sql, values, isauto);
		    stuDao.commit();
		    return list;
		    }catch(myException e){
			stuDao.rollBack();
			e.printStackTrace();
			throw e;
		}finally{
			if(stuDao!=null)
				stuDao.close();
		}}

}
