package pojo;

import servlet.servletUtil;

public class myFile {
private String fileName;//原始文件名
private String filePath;//原始文件路径
private String ufileName;//unicoder编码的文件名
private String ufilePath;//unicoder编码的文件路径
public String getFileName() {
	return fileName;
}
public void setFileName(String fileName) {
	this.fileName = fileName;
	this.ufileName=servletUtil.stringToUnicoder(fileName);
}
public String getFilePath() {
	return filePath;
}
public void setFilePath(String filePath) {
	this.filePath = filePath;
	this.ufilePath=servletUtil.stringToUnicoder(filePath);
}
public String getUfileName() {
	return ufileName;
}
public void setUfileName(String nfileName) {
	this.ufileName = nfileName;
	this.fileName=servletUtil.postMethodUnicoderToString(nfileName);
}
public String getUfilePath() {
	return ufilePath;
}
public void setUfilePath(String nfilePath) {
	this.ufilePath = nfilePath;
	this.fileName=servletUtil.postMethodUnicoderToString(nfilePath);
}
}
