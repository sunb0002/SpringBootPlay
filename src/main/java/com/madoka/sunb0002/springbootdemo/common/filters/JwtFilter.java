/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.filters;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.madoka.sunb0002.springbootdemo.common.utils.DateUtils;
import com.madoka.sunb0002.springbootdemo.common.utils.JwtHelper;
import com.madoka.sunb0002.springbootdemo.common.utils.Validators;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sunbo
 *
 */
@Slf4j
public class JwtFilter implements Filter {

	private static final String AUTH_HEADER_KEY = "Authorization";
	private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer "; // with trailing space to separate token

	private String jwtKey;

	@Override
	public void destroy() {
		// Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String jwtToken = getBearerToken(httpRequest);
		log.debug("Jwt filter will decode token=" + jwtToken + ", with key=" + jwtKey);

		DecodedJWT jwt = null;
		try {
			jwt = JwtHelper.parseToken(jwtToken, jwtKey);
		} catch (Exception e) {
			httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Unbale to decode jwt: " + e.getMessage());
			return;
		}
		String userName = jwt.getClaim("name").asString(); // NOSONAR
		Date expiry = jwt.getExpiresAt();

		if (Validators.isEmpty(userName) || DateUtils.compare(expiry) < 0) {
			httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid or expired JWT token.");
			return;
		}

		request.setAttribute("userName", userName);
		chain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.jwtKey = config.getInitParameter("jwtKey");
	}

	private String getBearerToken(HttpServletRequest request) {
		String authHeader = request.getHeader(AUTH_HEADER_KEY);
		if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
			return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
		}
		return null;
	}

}
