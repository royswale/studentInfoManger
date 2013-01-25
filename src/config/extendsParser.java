package config;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class extendsParser extends DefaultHandler{
	private Map<String,String> extMap=new HashMap<String, String>();
	private String tag;
	private String extName;
	private String mime;

		//������Ԫ�ؿ�ʼ��ǩ�������
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
		    String content=new String(ch,start,length).trim();
		    if(!isEmpty(content)&&!isEmpty(tag)){
		    	if("extension".equals(tag))
		    		extName=content;
		    	if("mime-type".equals(tag))
		    		mime=content;
		    }
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if("mime-mapping".equals(tag)){
				tag=null;
				extMap.put(extName, mime);
				extName=null;
				mime=null;
			}
		}
	     //����һ��Ԫ�ر�ǩ��ʼ��ʱ�����,qName��ʾԪ�ر�ǩ����
		  //attributes��ʾ��ǰԪ�ر�ǩ�еĵ����Լ���
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			tag=qName;
			  }

public Map<String, String> getExtMap() {
			return extMap;
		}

		public void setExtMap(Map<String, String> extMap) {
			this.extMap = extMap;
		}

private boolean isEmpty(String content){
	if(content==null||"".equals(content))
		return true;
	else
		return false;
}
	}
