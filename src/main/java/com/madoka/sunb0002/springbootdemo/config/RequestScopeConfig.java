package com.madoka.sunb0002.springbootdemo.config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.validation.BindException;
import org.springframework.web.context.WebApplicationContext;

import com.madoka.sunb0002.springbootdemo.common.context.UserI18nRequestContext;
import com.madoka.sunb0002.springbootdemo.common.context.UserRequestContext;
import com.madoka.sunb0002.springbootdemo.common.utils.Validators;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RequestScopeConfig {

	@Autowired
	private HttpServletRequest req;
	@Autowired
	private UserRequestContext reqCtx;

	private Map<String, UserI18nRequestContext> memoized18nCtx = new ConcurrentHashMap<>();

	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	@Primary
	@Lazy
	@Bean
	public UserRequestContext loadReqCtx() {
		log.debug("Request URL={}", this.req.getRequestURI());
		return new UserRequestContext(this.req.getParameter("country"));
	}

	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	@ConditionalOnBean(UserRequestContext.class)
	@Lazy
	@Bean
	public UserI18nRequestContext loadI18nRequestContext() throws IOException, BindException {
		final String country = this.reqCtx.getCountry();
		log.debug("Loading i18n config with country={}", country);
		return Validators.isNull(this.getI18nContext(country)) ? this.forceLoad18nContext(country)
				: this.getI18nContext(country);
	}

	/**
	 * 
	 * @param country
	 * @return
	 */
	public UserI18nRequestContext getI18nContext(final String country) {
		return Validators.isEmpty(country) ? null : this.memoized18nCtx.get(country);
	}

	/**
	 * 
	 * @param country
	 * @throws IOException
	 * @throws BindException
	 */
	public UserI18nRequestContext forceLoad18nContext(final String country) throws IOException, BindException {

		if (Validators.isEmpty(country)) {
			log.debug("Empty input country, unable to load i18n context");
			return null;
		}

		UserI18nRequestContext config = new UserI18nRequestContext();
		config.setCountry(country);

		MutablePropertySources propertySources = new MutablePropertySources();
		ClassPathResource rsc = new ClassPathResource("/i18n/config_" + country + ".properties");
		propertySources.addFirst(new ResourcePropertySource(rsc));

		PropertiesConfigurationFactory<UserI18nRequestContext> factory = new PropertiesConfigurationFactory<>(config);
		factory.setPropertySources(propertySources);
		factory.setIgnoreUnknownFields(true);
		factory.bindPropertiesToTarget();

		log.info("Adding i18n config={}", config);
		this.memoized18nCtx.put(country, config);
		return config;

	}

}
