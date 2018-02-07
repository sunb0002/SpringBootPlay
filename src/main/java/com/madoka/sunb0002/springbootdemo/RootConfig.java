/**
 * 
 */
package com.madoka.sunb0002.springbootdemo;

import java.nio.charset.StandardCharsets;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * @author Sun Bo
 *
 */
@Configuration
@ComponentScan("com.madoka.sunb0002.springbootdemo.config")
public class RootConfig {

	private static final String ENCODING_UTF_8 = StandardCharsets.UTF_8.name();

	/**
	 * Activates the new configuration service which supports converting String
	 * to Collection types.
	 * 
	 * @return ConversionService
	 */
	@Bean
	public ConversionService conversionService() {
		return new DefaultConversionService();
	}

	/**
	 * Defines message dictionary.
	 * 
	 * @return MessageSource
	 */
	@Bean("messageSource")
	public MessageSource messageDictionary() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding(ENCODING_UTF_8);
		return messageSource;
	}

}
