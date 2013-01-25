package dao.impl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pojo.message;
import pojo.student;
import dao.daoTemplate;
import dao.messageDao;
import exception.myException;

public class messageDaoImpl extends daoTemplate implements messageDao{

	public int deleteMessage(String conidtion, List<Object> values)
			throws myException {
		String sql="delete from ��Ϣ��";
		List<Integer> result=createSQLandExecuteUpdate(sql, conidtion, values);
		return result.get(0);
	}

	public List<message> findMessages(String conidtion, List<Object> values)
			throws myException {
		return findMessages(conidtion, values,-1,-1);
	}

	public List<message> findMessages(String conidtion, List<Object> values,
			int startResultset,int pageSize) throws myException {
String sql="select a.id,����,ʱ��,������_id,����,b.���� from ��Ϣ�� a left join ѧ���� b on a.������_id=b.id";
ResultSet rs=queryPage(sql,conidtion, values, startResultset, pageSize);
		List<message> messages=new ArrayList<message>();
		message message=null;
		try {
			while(rs.next()){
				message=new message();
				message.setId(rs.getInt("id"));
				message.setCreateDate(rs.getDate("ʱ��"));
				message.setMessageContent(rs.getString("����"));
				message.setMessageCreate_id(rs.getInt("������_id"));
				message.setTitle(rs.getString("����"));
				student stu=new student();
				stu.setId(message.getMessageCreate_id());
				stu.setName(rs.getString("����"));
				message.setStu(stu);
				messages.add(message);			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new myException(e);
		}finally{
			close(rs, pre, null);
		}
			return messages;
	}

	public message save(message message) throws myException {
		String sql="insert into ��Ϣ��(";
		String sqlnext=" values(";
		List<Object> values=new ArrayList<Object>();
		if(!isEmpty(message.getMessageContent())){
			sql=sql+"����,";
			sqlnext=sqlnext+"?,";
			values.add(message.getMessageContent());
		}
		if(!isEmpty(message.getTitle())){
			sql=sql+"����,";
			sqlnext=sqlnext+"?,";
			values.add(message.getTitle());
		}
		if(message.getMessageCreate_id()>0){
			sql=sql+"������_id,";
			sqlnext=sqlnext+"?,";
			values.add(message.getMessageCreate_id());
		}
		sql=sql+"ʱ��)";
		sqlnext=sqlnext+"?)";
		values.add(new Date(new java.util.Date().getTime()));
   List<Integer> ids=createSQLandExecuteUpdate(sql+sqlnext, null, values,true);
   message.setId(ids.get(0));
        return message;
	}



}
