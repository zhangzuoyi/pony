package com.zzy.pony.exam.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class ExamGradeFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest hrequest = (HttpServletRequest)arg0;
		String examId=hrequest.getParameter("examId");
		String gradeId=hrequest.getParameter("gradeId");
		if(StringUtils.isNotBlank(examId)){
			hrequest.getSession().setAttribute("examId", Integer.valueOf(examId));
		}
		if(StringUtils.isNotBlank(gradeId)){
			hrequest.getSession().setAttribute("gradeId", Integer.valueOf(gradeId));
		}
		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
