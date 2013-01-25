package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pojo.profession;
import pojo.sharedFile;
import dao.daoTemplate;
import dao.professionDao;
import exception.myException;

public class professionDaoImpl extends daoTemplate implements professionDao {

	public int deleteProfession(String conidtion, List<Object> values)
			throws myException {
		String sql="delete  from רҵ��";
		List<Integer> result=createSQLandExecuteUpdate(sql, conidtion, values);
		return result.get(0);
	}

	public List<profession> findProfessions(String conidtion,
			List<Object> values) throws myException {
		String sql="select id,רҵ����  from רҵ�� a";
		ResultSet rs=queryPage(sql,conidtion, values, 0, -1);
		List<profession> professions=new ArrayList<profession>();
		profession pf=null;
		try {
			while(rs.next()){
				pf=new profession();
				pf.setId(rs.getInt("id"));
				pf.setName(rs.getString("רҵ����"));
				professions.add(pf);			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new myException(e);
		}finally{
			close(rs, pre, null);
		}
			return professions;
	}

	public profession save(profession profession) throws myException {
		String sql="insert into רҵ��(רҵ����) values(?)";
		List<Object> values=new ArrayList<Object>();
         if(!isEmpty(profession.getName())){
        	 values.add(profession.getName());
        	 List<Integer> ids=createSQLandExecuteUpdate(sql, null, values,true);
        	 profession.setId(ids.get(0));
        	 return profession;
         }
         else{
        	 throw new myException("רҵ���Ʋ���Ϊ��");
         }  
	}

	public int updateProfession(int professionId, profession profession)
			throws myException {
		String sql="update רҵ��  set רҵ����=? where id=?";
		List<Object> values=new ArrayList<Object>();
         if(!isEmpty(profession.getName())){
        	 values.add(profession.getName());
        	 List<Integer> ids=createSQLandExecuteUpdate(sql, null, values);
        	 return (ids.get(0));
        	}
         else{
        	 throw new myException("רҵ���Ʋ���Ϊ��");
         }  
	}

}
