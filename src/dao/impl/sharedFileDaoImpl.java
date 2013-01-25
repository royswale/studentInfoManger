package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pojo.sharedFile;
import pojo.student;
import servlet.servletUtil;
import dao.daoTemplate;
import dao.sharedFileDao;
import exception.myException;

public class sharedFileDaoImpl extends daoTemplate implements sharedFileDao {

	public int deleteSharedFile(String conidtion, List<Object> values)
			throws myException {
		String sql="delete  from �ļ������";
		List<Integer> result=createSQLandExecuteUpdate(sql, conidtion, values);
		return result.get(0);
	}

	public List<sharedFile> findSharedFiles(String conidtion,
			List<Object> values) throws myException {
		return findSharedFiles(conidtion, values,-1,-1);
	}

	public List<sharedFile> findSharedFiles(String conidtion,
			List<Object> values, int startResultset,int pageSize)throws myException{
			String sql="select a.id,ѧ��_id,·��,b.���� from �ļ������ a left join ѧ���� b on a.ѧ��_id=b.id";
	
	ResultSet rs=queryPage(sql,conidtion, values, startResultset, pageSize);
	List<sharedFile> files=new ArrayList<sharedFile>();
	sharedFile file=null;
	try {
		while(rs.next()){
			file=new sharedFile();
			file.setId(rs.getInt("id"));
			file.setStu_id(rs.getInt("ѧ��_id"));
			student stu=new student();
			stu.setId(file.getStu_id());
			stu.setName(rs.getString("����"));
			file.setStu(stu);
			file.setFilePath(servletUtil.DBToPath(rs.getString("·��")));
			files.add(file);			
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new myException(e);
	}finally{
		close(rs, pre, null);
	}
		return files;
	}

	public sharedFile save(sharedFile sharedFile) throws myException {
		String sql="insert into �ļ������(";
		String sqlnext=" values(";
		List<Object> values=new ArrayList<Object>();
		if(!isEmpty(sharedFile.getFilePath())){
			sql=sql+"·��,";
			sqlnext=sqlnext+"?,";
			values.add(servletUtil.pathToDB(sharedFile.getFilePath()));
		}
		if(sharedFile.getStu_id()>0){
			sql=sql+"ѧ��_id,";
			sqlnext=sqlnext+"?,";
			values.add(sharedFile.getStu_id());
		}
		if(!sql.equals("insert into �ļ������(")){
			 sql=sql.substring(0,sql.length()-1)+")";
			 sqlnext=sqlnext.substring(0,sqlnext.length()-1)+")";
   List<Integer> ids=createSQLandExecuteUpdate(sql+sqlnext, null, values,true);
            sharedFile.setId(ids.get(0));
        return sharedFile;
        }else{
        	throw new myException("������������Զ�Ϊ��");
        }
		}

}
