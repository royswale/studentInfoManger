package util.excelUtil;

import java.io.File;

import java.net.URL;
import java.util.ArrayList;

import java.util.List;


import pojo.profession;
import pojo.student;



import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class excelUtil {

	public static List<student> readExcel(File inputWrokbook){
		List<student> students=new ArrayList<student>(0);
    	student stu=null;
		try {
			Workbook w1 = Workbook.getWorkbook(inputWrokbook);
				Sheet sheet=w1.getSheet("学生表");
	        	Cell[] row = null;
	        	
	        	if( sheet.getRows()<=1)
	        		return null;
	        	
	        	for (int i = 1 ; i < sheet.getRows() ; i++)
	            {    stu=new student();
	        		row = sheet.getRow(i);//获得每一行
	                for(int j=0;j<row.length;j++){
	                	String content=row[j].getContents();
	                	switch(j){
	                	case 0:
	                	stu.setStuId(content.toUpperCase().trim());
	                	break;
	                	case 1:
		                	stu.setName(content);
		                	break;
	                	case 2:
	                		profession profession=new pojo.profession();
		                	profession.setName(content.toLowerCase().trim());
	                		stu.setProfession(profession);
		                	break;
	                	case 3:
		                	stu.setSex(content.trim());
		                	break;
		                		
	                	}
	                	
	                }
	                students.add(stu);
	                  
	            }
	        	
	       
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return students;
		
	}

}
