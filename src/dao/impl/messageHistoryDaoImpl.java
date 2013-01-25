package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pojo.*;
import pojo.student;
import dao.daoTemplate;
import dao.messageHistoryDao;
import exception.myException;

public class messageHistoryDaoImpl extends daoTemplate implements messageHistoryDao  {

	public int deletemessageHistory(String conidtion, List<Object> values)
			throws myException {
		String sql="delete from ��Ϣ��ʷ";
		List<Integer> result=createSQLandExecuteUpdate(sql, conidtion, values);
		return result.get(0);
	}

	public List<messageHistory> findmessageHistorys(String conidtion,
			List<Object> values) throws myException {
		return findmessageHistorys(conidtion, values,-1,-1);
		}

	public List<messageHistory> findmessageHistorys(String conidtion,
			List<Object> values,int startResultset,int pageSize)
			throws myException {
String sql="select a.id,ѧ��_id,��Ϣ_id,�Ѷ�,b.����,b.����,c.���� from ��Ϣ��ʷ a left join ��Ϣ�� b on b.id=a.��Ϣ_id left join ѧ���� c on c.id=b.������_id";
ResultSet rs=queryPage(sql,conidtion, values, startResultset, pageSize);
		List<messageHistory> messageHistorys=new ArrayList<messageHistory>();
		messageHistory messageHistory=null;
		try {
			while(rs.next()){
				messageHistory=new messageHistory();
				messageHistory.setId(rs.getInt("id"));
				messageHistory.setMessage_id(rs.getInt("��Ϣ_id"));
				messageHistory.setStu_id(rs.getInt("ѧ��_id"));
				messageHistory.setRead(rs.getBoolean("�Ѷ�"));
				message message=new message();
				message.setId(message.getMessageCreate_id());
				student stu=new student();
				stu.setName(rs.getString("����"));
				message.setStu(stu);
				message.setMessageContent(rs.getString("����"));
				message.setTitle(rs.getString("����"));
				messageHistory.setMessage(message);
				messageHistorys.add(messageHistory);			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new myException(e);
		}finally{
			close(rs, pre, null);
		}
			return messageHistorys;
	}

	public messageHistory save(messageHistory messageHistory)
			throws myException {
		String sql="insert into ��Ϣ��ʷ(";
		String sqlnext=" values(";
		List<Object> values=new ArrayList<Object>();
		if(messageHistory.getStu_id()>0){
			sql=sql+"ѧ��_id,";
			sqlnext=sqlnext+"?,";
			values.add(messageHistory.getStu_id());
		}
		if(messageHistory.getMessage_id()>0){
			sql=sql+"��Ϣ_id,";
			sqlnext=sqlnext+"?,";
			values.add(messageHistory.getMessage_id());	
		}
		sql=sql+"�Ѷ�)";
		sqlnext=sqlnext+"?)";
		values.add(messageHistory.getRead());
   List<Integer> ids=createSQLandExecuteUpdate(sql+sqlnext, null, values,true);
        messageHistory.setId(ids.get(0));
        return messageHistory;
	}

	public int updatemessageHistory(int messageHistoryId)throws myException{
		String sql="update ��Ϣ��ʷ set �Ѷ�=? where id=?";
	    List<Object> values=new ArrayList<Object>();
	    values.add(true);
	    values.add(messageHistoryId);
	    return executeUpdate(sql, values).get(0);
	   }

}
