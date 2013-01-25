package pojo;

import java.util.Date;

public class message {
private int id=-1;
private int messageCreate_id=-1;
private student stu;
public student getStu() {
	return stu;
}
public void setStu(student stu) {
	this.stu = stu;
}
private Date createDate;
private String messageContent;
private String title;

public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getMessageCreate_id() {
	return messageCreate_id;
}
public void setMessageCreate_id(int messageCreateId) {
	messageCreate_id = messageCreateId;
}
public Date getCreateDate() {
	return createDate;
}
public void setCreateDate(Date createDate) {
	this.createDate = createDate;
}
public String getMessageContent() {
	return messageContent;
}
public void setMessageContent(String messageContent) {
	this.messageContent = messageContent;
}

}
