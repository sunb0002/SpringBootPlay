/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Sun Bo
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerDocConfig {

	@Value("${app.name:madoka}")
	private String appName = null;

	@Value("${app.description}")
	private String appDescription = null;

	@Value("${app.version}")
	private String appVersion = null;

	@Value("${app.contact.name}")
	private String contactName = null;

	@Value("${app.contact.homepage.url}")
	private String contactHomePageUrl = null;

	@Value("${app.contact.email}")
	private String contactEmail = null;

	/**
	 * @return Docket
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).select()
				.apis(RequestHandlerSelectors.basePackage("com.madoka.sunb0002.springbootdemo")).build()
				.apiInfo(apiInfo()).forCodeGeneration(true).securitySchemes(Arrays.asList(apiKey()));
	}

	/**
	 * @return ApiInfo
	 */
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(appName).description(appDescription).version(appVersion)
				.contact(new Contact(contactName, contactHomePageUrl, contactEmail)).build();
	}

	private ApiKey apiKey() {
		return new ApiKey("Authorization", "Bearer ", "header");
	}

}
