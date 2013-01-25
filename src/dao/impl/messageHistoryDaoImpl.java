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
		String sql="delete from 消息历史";
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
String sql="select a.id,学生_id,消息_id,已读,b.内容,b.标题,c.姓名 from 消息历史 a left join 消息表 b on b.id=a.消息_id left join 学生表 c on c.id=b.创建人_id";
ResultSet rs=queryPage(sql,conidtion, values, startResultset, pageSize);
		List<messageHistory> messageHistorys=new ArrayList<messageHistory>();
		messageHistory messageHistory=null;
		try {
			while(rs.next()){
				messageHistory=new messageHistory();
				messageHistory.setId(rs.getInt("id"));
				messageHistory.setMessage_id(rs.getInt("消息_id"));
				messageHistory.setStu_id(rs.getInt("学生_id"));
				messageHistory.setRead(rs.getBoolean("已读"));
				message message=new message();
				message.setId(message.getMessageCreate_id());
				student stu=new student();
				stu.setName(rs.getString("姓名"));
				message.setStu(stu);
				message.setMessageContent(rs.getString("内容"));
				message.setTitle(rs.getString("标题"));
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
		String sql="insert into 消息历史(";
		String sqlnext=" values(";
		List<Object> values=new ArrayList<Object>();
		if(messageHistory.getStu_id()>0){
			sql=sql+"学生_id,";
			sqlnext=sqlnext+"?,";
			values.add(messageHistory.getStu_id());
		}
		if(messageHistory.getMessage_id()>0){
			sql=sql+"消息_id,";
			sqlnext=sqlnext+"?,";
			values.add(messageHistory.getMessage_id());	
		}
		sql=sql+"已读)";
		sqlnext=sqlnext+"?)";
		values.add(messageHistory.getRead());
   List<Integer> ids=createSQLandExecuteUpdate(sql+sqlnext, null, values,true);
        messageHistory.setId(ids.get(0));
        return messageHistory;
	}

	public int updatemessageHistory(int messageHistoryId)throws myException{
		String sql="update 消息历史 set 已读=? where id=?";
	    List<Object> values=new ArrayList<Object>();
	    values.add(true);
	    values.add(messageHistoryId);
	    return executeUpdate(sql, values).get(0);
	   }

}
