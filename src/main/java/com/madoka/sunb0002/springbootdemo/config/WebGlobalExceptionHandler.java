/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.config;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.madoka.sunb0002.springbootdemo.common.exceptions.ServiceException;
import com.madoka.sunb0002.springbootdemo.webapi.GeneralResponse;

/**
 * @author Sun Bo
 *
 */
@ControllerAdvice
public class WebGlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Locale locale = LocaleContextHolder.getLocale();

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	public GeneralResponse handleServiceException(HttpServletRequest request, HttpServletResponse response,
			ServiceException se) {
		logger.error("ServiceException Occured: URL={}", request.getRequestURL(), se);
		int status = se.getStatus() != null ? se.getStatus().intValue() : HttpStatus.INTERNAL_SERVER_ERROR.value();
		response.setStatus(status);
		return new GeneralResponse(status, se.getMessage(),
				messageSource.getMessage("response.error.service", null, locale));
	}

	@ExceptionHandler({ NoHandlerFoundException.class, HttpMessageNotReadableException.class,
			HttpMediaTypeNotSupportedException.class, MethodArgumentNotValidException.class,
			MethodArgumentTypeMismatchException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public GeneralResponse handleNoMappingException(HttpServletRequest request, Exception ex) {
		logger.error("NoMappingException Occured: URL={}", request.getRequestURL(), ex);
		return new GeneralResponse(HttpStatus.BAD_REQUEST.value(), ex.getClass().getName(),
				messageSource.getMessage("response.error.request.invalid", null, locale));
	}

	/**
	 * Handle any other kind of exceptions.
	 * 
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public GeneralResponse handleException(HttpServletRequest request, Exception ex) {
		logger.error("Exception Occured: URL=" + request.getRequestURL(), ex);
		return new GeneralResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getClass().getName(),
				messageSource.getMessage("response.error.general", null, locale));
	}

}
