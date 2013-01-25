package servicesDao.impl;

import java.util.List;

import config.webAppConfig;

import dao.sharedFileDao;
import dao.studentDao;

import pojo.sharedFile;
import exception.myException;
import servicesDao.sharedFileServicesDao;
import servlet.servletUtil;

public class sharedFileServicesDaoImpl implements sharedFileServicesDao {

	public void delete(String condition, List<Object> values)
			throws myException {
		sharedFileDao sDao=(sharedFileDao) webAppConfig.getBean(sharedFileDao.class);
		studentDao stuDao=(studentDao)webAppConfig.getBean(studentDao.class);
		try{
			List<sharedFile> sharedFiles=sDao.findSharedFiles(condition, values);
		if(sharedFiles==null||sharedFiles.size()==0)
			return;
		sDao.deleteSharedFile(condition, values);
		String update="update 学生表 set 图片_id=-1 where 图片_id in(";
		values.clear();
		for(sharedFile sf:sharedFiles){
			update=update+"?,";
			values.add(sf.getId());
		}
		update=update+"-99)";
		stuDao.executeUpdate(update, values, false);
		stuDao.commit();
		}catch(myException e){
			throw e;
		}finally{
			if(sDao!=null)
				sDao.close();
			}
	}

	public int saveFile(String file, int stu_Id) throws myException {
		sharedFileDao sDao=(sharedFileDao) webAppConfig.getBean(sharedFileDao.class);
		int id=-9;
		try{
			String condition="路径=? and 学生_id=?";
			List<Object> list=servletUtil.changeToList(new Object[]{file,stu_Id});
			List<sharedFile> sharedFiles=sDao.findSharedFiles(condition, list);
			if(!(sharedFiles==null||sharedFiles.size()==0))
				throw new myException("该学生已经共享了该"+file+"文件");
			sharedFile sFile=new sharedFile();
			sFile.setFilePath(file);
			sFile.setStu_id(stu_Id);
			sFile=sDao.save(sFile);
			id=sFile.getId();
			sDao.commit();
		}catch(myException e){
			sDao.rollBack();
			throw e;
		}finally{
			if(sDao!=null)
				sDao.close();
			}
		return id;
	}

	public List<sharedFile> select(String condition, List<Object> values)
			throws myException {
		return select(condition, values,-1,-1);
	}

	public List select(String condition, List<Object> values,
			int startPostion, int pageSize) throws myException {
		sharedFileDao sDao=(sharedFileDao) webAppConfig.getBean(sharedFileDao.class);
		try{
			List<sharedFile> sharedFiles=sDao.findSharedFiles(condition, values, startPostion, pageSize);
			sDao.commit();
			return sharedFiles;
		}catch(myException e){
			sDao.rollBack();
			throw e;
		}finally{
			if(sDao!=null)
				sDao.close();
			}
	}

	public List<Object[]> executeQuery(String sql, List<Object> values)
			throws myException {
		sharedFileDao sDao=(sharedFileDao) webAppConfig.getBean(sharedFileDao.class);
		try{
			List<Object[]> sharedFiles=sDao.select(sql, values);
			sDao.commit();
			return sharedFiles;
		}catch(myException e){
			sDao.rollBack();
			throw e;
		}finally{
			if(sDao!=null)
				sDao.close();
			}
	}

	public List<Integer> executeUpdate(String sql, List<Object> values,
			boolean isauto) throws myException {
		sharedFileDao sDao=(sharedFileDao) webAppConfig.getBean(sharedFileDao.class);
		try{
			List<Integer> sharedFiles=sDao.executeUpdate(sql, values, isauto);
			sDao.commit();
			return sharedFiles;
		}catch(myException e){
			sDao.rollBack();
			throw e;
		}finally{
			if(sDao!=null)
				sDao.close();
			}
	}

}
