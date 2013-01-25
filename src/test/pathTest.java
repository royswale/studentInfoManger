package test;

import java.io.File;
import java.io.IOException;

public class pathTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	String sql="\\a\\b\\cc\\dd\\";
	System.out.println(sql.replaceAll("\\\\", "/"));
	sql="/a/b/cc/dd/";
	System.out.println(sql.replaceAll("/", "\\\\"));
	
	}

}
