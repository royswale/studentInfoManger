package pojo;

import servlet.servletUtil;
import dao.daoTemplate;

public class student {
private int id;
private String stuId;
private String name;
private int profession_id=-1;
private profession profession;
public profession getProfession() {
	return profession;
}
private String ids;

public String getIds() {
	return ids;
}

public void setIds(String ids) {
	this.ids = ids;
}

public void setProfession(profession profession) {
	this.profession = profession;
	if(profession!=null)
		this.profession_id=profession.getId();
}

private boolean sex;
private int image_id=-1;
private sharedFile image;
private String pwd;

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
	ids=servletUtil.stringToUnicoder(id+"");
}

public String getStuId() {
	return stuId;
}

public void setStuId(String stuId) {
	this.stuId = stuId;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public int getProfession_id() {
	return profession_id;
}

public void setProfession_id(int professionId) {
	profession_id = professionId;
}



public boolean getSex() {
	return sex;
}

public void setSex(boolean sex) {
	this.sex = sex;
}



public int getImage_id() {
	return image_id;
}

public void setImage_id(int imageId) {
	image_id = imageId;
}

public sharedFile getImage() {
	return image;
}

public void setImage(sharedFile image) {
	this.image = image;
	if(image!=null&&image.getId()>0)
		this.image_id=image.getId();
}

public String getPwd() {
	return pwd;
}

public void setPwd(String pwd) {
	this.pwd = pwd;
}

public String toString(){
	return "学号="+stuId+";姓名="+name+";专业名称="+profession_id+";性别="+sex;
}
public void setSex(String sex){
	this.sex=stringToBoolean(sex);
}
public String getSexString(){
	return booleanToString(sex);
}
private String booleanToString(boolean sex){
	if(sex==true)
		return "男";
		else{
			return "女";
		}
}
private boolean stringToBoolean(String sex){
	if("男".equals(sex))
		return true;
	else
		return false;
}

@Override
public boolean equals(Object obj) {
	if(stuId==null||"".equals(stuId))
		return false;
	if(obj instanceof student){
		student stu=(student)obj;
		return this.stuId.equals(stu.getStuId());
	}
	else
		return false;
	
}

}
