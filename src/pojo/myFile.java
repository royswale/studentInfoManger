package pojo;

import servlet.servletUtil;

public class myFile {
private String fileName;//ԭʼ�ļ���
private String filePath;//ԭʼ�ļ�·��
private String ufileName;//unicoder������ļ���
private String ufilePath;//unicoder������ļ�·��
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
