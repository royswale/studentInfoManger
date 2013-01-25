package config;


import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import util.reflect.pojo.method;


public class initParser extends DefaultHandler{
	  //工厂类所对应的属性配置
	   private Map<String,Map<String,String>> factroyContext=new HashMap<String,Map<String,String>>();
	   private String tag;
	   //类所对应的静态方法
	   public Map<String, method> getMethodBean() {
		return methodBean;
	}
	   //工厂类的属性配置
	private Map<String,String> factoryProperites;
	   private boolean instance=true;
	   private Map<String,String> commonBean=new HashMap<String, String>();
	  private Map<String,method> methodBean=new HashMap<String, method>();
	   public Map<String, Map<String, String>> getFactroyContext() {
		return factroyContext;
	}

	public Map<String, String> getCommonBean() {
		return commonBean;
	}

		//读到的元素开始标签后的内容
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
		    String content=new String(ch,start,length).trim();
		    if(!isEmpty(content)&&!isEmpty(tag)){
		    	if(factoryProperites!=null)
		    		factoryProperites.put(tag, content);
		    }
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if("bean".equals(qName)){
			 if(factoryProperites!=null)
				 factoryProperites=null;
			   instance=true;
			}
			tag=null;
		}
	     //读到一个元素标签开始的时候击发,qName表示元素标签名，
		  //attributes表示当前元素标签中的的属性集合
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			tag=qName;
		        if("factoryBean".equals(qName)||"factoryCreate".equals(qName)){
				  method method=new method();
				  method.setClassName(attributes.getValue("class"));
				  method.setMethodName(attributes.getValue("method"));
				  methodBean.put(attributes.getValue("name"), method);
				  if("factoryBean".equals(qName)){
				  factoryProperites=new HashMap<String, String>();
				  instance=false;
				  factroyContext.put(attributes.getValue("name"), factoryProperites);
				  }
				  }
			  else if("bean".equals(qName)){
				  commonBean.put(attributes.getValue("name"), attributes.getValue("class"));
			  }
			  }
			
			

private void put(String key,String value,Map<String,String> map){
	map.put(key, value);
}
private boolean isEmpty(String content){
	if(content==null||"".equals(content))
		return true;
	else
		return false;
}
	}
