package com.madoka.sunb0002.springbootdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author Sun Bo
 *
 */
@Configuration
@PropertySource("classpath:application.properties")
public class PropertyConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
		pspc.setIgnoreResourceNotFound(false);
		pspc.setIgnoreUnresolvablePlaceholders(false);
		return pspc;
	}

}
