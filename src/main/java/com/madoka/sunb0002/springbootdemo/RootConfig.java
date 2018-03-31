/**
 * 
 */
package com.madoka.sunb0002.springbootdemo;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Sun Bo
 *
 */
@EnableScheduling
@EnableZuulProxy
@Configuration
@ComponentScan("com.madoka.sunb0002.springbootdemo.config")
public class RootConfig {

	/**
	 * Activates the new configuration service which supports converting String to
	 * Collection types.
	 * 
	 * @note: SpringBoot will NOT create this bean by default.
	 * @return ConversionService
	 */
	@Bean
	public ConversionService conversionService() {
		return new DefaultConversionService();
	}

}
