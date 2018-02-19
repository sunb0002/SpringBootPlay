/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.filters;

import java.io.IOException;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sun Bo
 * @note: see filter configurations with comments in WebConfig.java
 */
public class LoggingFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final Random randgen = new Random();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain)
			throws IOException, ServletException {
		logger.debug("***Filter is hit, your lucky number is {}", randgen.nextInt(10000) + 1);
		chain.doFilter(arg0, arg1);
	}

	@Override
	public void destroy() {
		// Auto-generated method stub
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		logger.debug("***Filter is inited, initParamName=>{}", config.getInitParameter("initParamName"));
	}

}
