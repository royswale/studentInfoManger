package servicesDao.impl;

import java.util.ArrayList;
import java.util.List;

import config.webAppConfig;

import dao.professionDao;

import pojo.profession;
import exception.myException;
import servicesDao.professionServicesDao;

public class professionServicesDaoImpl implements professionServicesDao{

	public profession saveProfession(profession profession) throws myException {
		if(!invalidate(profession))
			throw new myException("专业对象非法");
		professionDao pDao=(professionDao) webAppConfig.getBean(professionDao.class);
		String condition="专业名称=?";
		List<Object> values=new ArrayList<Object>();
		values.add(profession.getName());
		try{
			List<profession> pfs=pDao.findProfessions(condition, values);
			if(pfs.size()>0)
				throw new myException(profession.getName()+"已经存在");
			profession=pDao.save(profession);
			pDao.commit();
			return profession;
		}catch(myException e){
			e.printStackTrace();
			pDao.rollBack();
			throw e;
		}finally{
			if(pDao!=null)
				pDao.close();
		}
	}

	public List<profession> select(String condition, List<Object> values)
			throws myException {
		professionDao pDao=(professionDao) webAppConfig.getBean(professionDao.class);
		try{
			List<profession> pfs=pDao.findProfessions(condition, values);
			pDao.commit();
			return pfs;
		}catch(myException e){
			e.printStackTrace();
			pDao.rollBack();
			throw e;
		}finally{
			if(pDao!=null)
				pDao.close();
		}
	}
 private boolean invalidate(profession p){
	  boolean isOk=true;
	  if(p==null||p.getName()==null||"".equals(p.getName().trim()))
		  isOk=false;
	  return isOk;
 }
}
