/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
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

	@Value("${app.name}")
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
	 * Swagger API Info.
	 * 
	 * @return ApiInfo
	 */
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(appName).description(appDescription).version(appVersion)
				.contact(new Contact(contactName, contactHomePageUrl, contactEmail)).build();
	}

	/**
	 * @return Docket
	 */
	@Bean
	public Docket customImplementation() {
		return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).select()
				.apis(RequestHandlerSelectors.basePackage("com.madoka.sunb0002.springbootdemo")).build()
				.apiInfo(apiInfo()).forCodeGeneration(true);
	}

}
