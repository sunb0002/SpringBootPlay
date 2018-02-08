package com.madoka.sunb0002.springbootdemo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Sun Bo
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan({ "com.madoka.sunb0002.springbootdemo.webapi.*", "com.madoka.sunb0002.springbootdemo.services.impl" })
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorParameter(false).favorPathExtension(false).ignoreAcceptHeader(true)
				.defaultContentType(MediaType.APPLICATION_JSON).useJaf(false)
				.mediaType("xml", MediaType.APPLICATION_XML).mediaType("json", MediaType.APPLICATION_JSON);
	}

	/**
	 * Swagger configuration. SpringBoot default handlers:
	 * https://spring.io/blog/2013/12/19/serving-static-web-content-with-spring-boot
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		if (!registry.hasMappingForPattern("/webjars/**")) {
			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
	}

	/**
	 * SpringBoot properties endpoints.cors.* are used by Actuator endpoints
	 * only, you HAVE TO use JavaConfig to set the global CORS congfiguration.
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("http://localhost:4200");
	}

}
