package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class patternTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String c="id=bb.id and id=a and stuid=b and aida=id id=aid ";
		Pattern pattern=Pattern.compile("=id ");
        Matcher matcher=pattern.matcher(c);
        String temp=matcher.replaceAll("=a.id ");
       System.out.println(temp);
       pattern=Pattern.compile(" id=");
       matcher=pattern.matcher(temp);
        temp=matcher.replaceAll(" a.id=");
       System.out.println(temp);
      /* matcher=pattern.matcher(temp);
        temp=matcher.replaceAll("a.id");
     */  System.out.println(temp);
       
       
       
        
	}

}
