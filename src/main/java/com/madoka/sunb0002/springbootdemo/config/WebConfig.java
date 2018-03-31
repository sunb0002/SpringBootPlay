package com.madoka.sunb0002.springbootdemo.config;

import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.madoka.sunb0002.springbootdemo.common.filters.JwtFilter;
import com.madoka.sunb0002.springbootdemo.common.filters.LoggingFilter;

/**
 * @author Sun Bo
 * @see SpringBoot WebMvcAutoConfiguration will do @EnableWebMvc
 *      and @ComponentScan itself.
 */

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${app.jwt.secret}")
	private String jwtKey;

	@Value("${app.filter.enable:false}")
	private boolean filterEnable;
	@Value("${app.filter.log.urlpattern:/home/*}")
	private String logFilterUrlPattern;
	@Value("${app.filter.jwt.urlpattern:/profile/*}")
	private String jwtFilterUrlPattern;

	// Just to verify whether beans have been created.
	@Autowired(required = false)
	private ConversionService conversionService;
	@Autowired(required = false)
	@Qualifier("Test-Conditional-Executor")
	private Executor testExecutor;

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorParameter(false).favorPathExtension(false).ignoreAcceptHeader(true)
				.defaultContentType(MediaType.APPLICATION_JSON).useJaf(false)
				.mediaType("xml", MediaType.APPLICATION_XML).mediaType("json", MediaType.APPLICATION_JSON);
	}

	/**
	 * Swagger configuration. SpringBoot has these default handlers:
	 * https://spring.io/blog/2013/12/19/serving-static-web-content-with-spring-boot
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		if (!registry.hasMappingForPattern("/webjars/**")) {
			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}

	/**
	 * applicatoin.properties endpoints.cors.* are used by Actuator endpoints only,
	 * so STILL HAVE TO manually set the global CORS configuration.
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("http://localhost:4200");
	}

	/**
	 * @note: Filter configuration with UrlPattern and InitParam details. If to
	 *        apply a filter on all endpoints, just declare the Filter as @Bean.
	 *        Spring will pickup the filter bean automatically.
	 */
	@Bean
	@Order(1)
	public FilterRegistrationBean myFilterRegBean() {
		FilterRegistrationBean reg = new FilterRegistrationBean();
		reg.setFilter(new LoggingFilter());
		reg.addUrlPatterns(logFilterUrlPattern);
		reg.addInitParameter("initParamName", "Hello filter-chan");
		reg.setEnabled(filterEnable);
		return reg;
	}

	@Profile("dev1")
	@Bean
	@Order(2)
	public FilterRegistrationBean jwtFilterRegBean() {
		FilterRegistrationBean reg = new FilterRegistrationBean();
		reg.setFilter(new JwtFilter());
		reg.addUrlPatterns(jwtFilterUrlPattern);
		reg.addInitParameter("jwtKey", this.jwtKey); // My solution for filter @value injection
		reg.setEnabled(filterEnable);
		return reg;
	}

	@PostConstruct
	public void verifyConfigs() {
		logger.debug("***Has ConversionService(DefaultConversionService) been created by SpringBoot: {}",
				conversionService instanceof DefaultConversionService);
		logger.debug("***Has Test-Conditional-Executor been created: {}", testExecutor != null);
	}

}
