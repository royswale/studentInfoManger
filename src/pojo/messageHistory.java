package pojo;

public class messageHistory {
private int id=-1;
private int stu_id=-1;
private int message_id=-1;
private message message;
public message getMessage() {
	return message;
}
public void setMessage(message message) {
	this.message = message;
}
public boolean isRead() {
	return isRead;
}
private boolean isRead=false;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getStu_id() {
	return stu_id;
}
public void setStu_id(int stuId) {
	stu_id = stuId;
}
public int getMessage_id() {
	return message_id;
}
public void setMessage_id(int messageId) {
	message_id = messageId;
}
public boolean getRead() {
	return isRead;
}
public void setRead(boolean isRead) {
	this.isRead = isRead;
}


}
