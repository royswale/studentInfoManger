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
     //condition:学号=? and 姓名=? or id>=?
	public int deleteStudent(String conidtion, List<Object> values)
			throws myException {
		String sql="delete from 学生表";
		List<Integer> result=createSQLandExecuteUpdate(sql, conidtion, values);
		return result.get(0);
	}

	public List<student> findStudents(String conidtion, List<Object> values)
			throws myException {
			return findStudents(conidtion, values,-1,-1);
	}

	public List<student> findStudents(String conidtion, List<Object> values,
			int startResultset,int pageSize) throws myException {
		String sql="select a.id,学号,姓名,专业_id,图片_id,性别,b.专业名称,c.路径 from 学生表 a left join 专业表 b on a.专业_id=b.id left join 文件共享表 c on c.id = a.图片_id";
		
		ResultSet rs=queryPage(sql,conidtion, values, startResultset-1, pageSize);
		List<student> students=new ArrayList<student>();
		student stu=null;
		try {
			while(rs.next()){
				stu=new student();
				stu.setId(rs.getInt("id"));
				stu.setSex(rs.getBoolean("性别"));
				stu.setName(rs.getString("姓名"));
				stu.setProfession_id(rs.getInt("专业_id"));
				profession profession=new profession();
				profession.setId(rs.getInt("专业_id"));
				profession.setName(rs.getString("专业名称"));
				stu.setProfession(profession);
				sharedFile file=new sharedFile();
				file.setFilePath(rs.getString("路径"));
				stu.setImage(file);
				//stu.setPwd(rs.getString("密码"));
				stu.setStuId(rs.getString("学号"));
				stu.setImage_id(rs.getInt("图片_id"));
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
		String sql="insert into 学生表(学号,姓名,专业_id,密码,图片_id,性别) values(?,?,?,?,?,?)";
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
		String sql="update 学生表 set ";
		String condition="id=?";
		List<Object> values=new ArrayList<Object>();
		if(!isEmpty(newStu.getName())){
			sql=sql+"姓名=?,";
			values.add(newStu.getName());
		}
		if(newStu.getProfession_id()>0){
			sql=sql+"专业_id=?,";
			values.add(newStu.getProfession_id());
		}
		if((newStu.getImage_id()>0)){
			sql=sql+"图片_id=?,";
			values.add(newStu.getImage_id());
		}
		if(!isEmpty(newStu.getPwd())){
			sql=sql+"密码=?,";
			values.add(newStu.getPwd());
		}
		sql=sql+"性别=?";
		values.add(newStu.getSex());
		values.add(stuId);
		return createSQLandExecuteUpdate(sql, condition, values).get(0);
	}

	public int[] saveStudents(List<student> students)
			throws myException {
		String sql="insert into 学生表(学号,姓名,专业_id,密码,图片_id,性别) values(?,?,?,?,?,?)";
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
