package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pojo.profession;
import pojo.sharedFile;
import pojo.student;
import dao.daoTemplate;
import dao.studentDao;
import exception.myException;

public class studentDaoImpl extends daoTemplate implements studentDao{
     //condition:ѧ��=? and ����=? or id>=?
	public int deleteStudent(String conidtion, List<Object> values)
			throws myException {
		String sql="delete from ѧ����";
		List<Integer> result=createSQLandExecuteUpdate(sql, conidtion, values);
		return result.get(0);
	}

	public List<student> findStudents(String conidtion, List<Object> values)
			throws myException {
			return findStudents(conidtion, values,-1,-1);
	}

	public List<student> findStudents(String conidtion, List<Object> values,
			int startResultset,int pageSize) throws myException {
		String sql="select a.id,ѧ��,����,רҵ_id,ͼƬ_id,�Ա�,b.רҵ����,c.·�� from ѧ���� a left join רҵ�� b on a.רҵ_id=b.id left join �ļ������ c on c.id = a.ͼƬ_id";
		
		ResultSet rs=queryPage(sql,conidtion, values, startResultset-1, pageSize);
		List<student> students=new ArrayList<student>();
		student stu=null;
		try {
			while(rs.next()){
				stu=new student();
				stu.setId(rs.getInt("id"));
				stu.setSex(rs.getBoolean("�Ա�"));
				stu.setName(rs.getString("����"));
				stu.setProfession_id(rs.getInt("רҵ_id"));
				profession profession=new profession();
				profession.setId(rs.getInt("רҵ_id"));
				profession.setName(rs.getString("רҵ����"));
				stu.setProfession(profession);
				sharedFile file=new sharedFile();
				file.setFilePath(rs.getString("·��"));
				stu.setImage(file);
				//stu.setPwd(rs.getString("����"));
				stu.setStuId(rs.getString("ѧ��"));
				stu.setImage_id(rs.getInt("ͼƬ_id"));
				students.add(stu);			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new myException(e);
		}finally{
			close(rs, pre, null);
		}
			return students;
	}

	public student save(student stu) throws myException {
		String sql="insert into ѧ����(ѧ��,����,רҵ_id,����,ͼƬ_id,�Ա�) values(?,?,?,?,?,?)";
		List<Object> values=new ArrayList<Object>();
		values.add(stu.getStuId());
		values.add(stu.getName());
		values.add(stu.getProfession_id());
		values.add( stu.getPwd());
		values.add( stu.getImage_id());
		values.add(stu.getSex());
   List<Integer> ids=createSQLandExecuteUpdate(sql, null, values,true);
        stu.setId(ids.get(0));
        return stu;
	}

	public int updateStudent(int stuId, student newStu) throws myException {
		String sql="update ѧ���� set ";
		String condition="id=?";
		List<Object> values=new ArrayList<Object>();
		if(!isEmpty(newStu.getName())){
			sql=sql+"����=?,";
			values.add(newStu.getName());
		}
		if(newStu.getProfession_id()>0){
			sql=sql+"רҵ_id=?,";
			values.add(newStu.getProfession_id());
		}
		if((newStu.getImage_id()>0)){
			sql=sql+"ͼƬ_id=?,";
			values.add(newStu.getImage_id());
		}
		if(!isEmpty(newStu.getPwd())){
			sql=sql+"����=?,";
			values.add(newStu.getPwd());
		}
		sql=sql+"�Ա�=?";
		values.add(newStu.getSex());
		values.add(stuId);
		return createSQLandExecuteUpdate(sql, condition, values).get(0);
	}

	public int[] saveStudents(List<student> students)
			throws myException {
		String sql="insert into ѧ����(ѧ��,����,רҵ_id,����,ͼƬ_id,�Ա�) values(?,?,?,?,?,?)";
		try {
			pre=conn.prepareStatement(sql);
			for(student stu:students){
			pre.setObject(1,stu.getStuId() );
			pre.setObject(2, stu.getName());
			pre.setObject(3, stu.getProfession_id());
			pre.setObject(4, stu.getPwd());
			pre.setObject(5, stu.getImage_id());
			pre.setObject(6, stu.getSex());
			pre.addBatch();
			}
			return pre.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new myException(e);
		}
		finally{
			close(null, pre, null);
		}
	}

	public List<Object[]> executeQuery(String sql, List<Object> values)
			throws myException {
		return super.select(sql, values);
	}

}
