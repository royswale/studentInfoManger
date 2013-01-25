package servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class encoderFiter implements Filter {
   private String encoder="utf-8";
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest)req;
		HttpServletResponse response=(HttpServletResponse)res;
		request.setCharacterEncoding(encoder);
		response.setCharacterEncoding(encoder);
		chain.doFilter(request, response);
		response.addHeader("pragma", "no-cache");
		response.addHeader("cache-control", "no-cache");
		response.addHeader("expires", "0");
}

	public void init(FilterConfig config) throws ServletException {
		String tempEncoder=config.getInitParameter("encoder");
		if(tempEncoder!=null&&!"".equals(tempEncoder.trim()))
			encoder=tempEncoder;
	}

}
