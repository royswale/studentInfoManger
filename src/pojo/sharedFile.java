package pojo;


public class sharedFile {
private int id=-1;
private int stu_id=-1;
private student stu;

public student getStu() {
	return stu;
}
public void setStu(student stu) {
	this.stu = stu;
}
private String filePath;
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
public String getFilePath() {
	return filePath;
}
public void setFilePath(String filePath) {
	this.filePath = filePath;
}

}
