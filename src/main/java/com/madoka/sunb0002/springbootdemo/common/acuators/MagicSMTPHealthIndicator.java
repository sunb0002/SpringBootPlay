/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.acuators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Sun Bo
 * @note: Add a health check item under /health endpoint
 *
 */
@Service
public class MagicSMTPHealthIndicator extends AbstractHealthIndicator {

	@Autowired(required = false)
	private JavaMailSenderImpl mailSender;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.actuate.health.AbstractHealthIndicator#
	 * doHealthCheck(org.springframework.boot.actuate.health.Health.Builder)
	 */
	@Override
	protected void doHealthCheck(Builder builder) throws Exception {

		Assert.notNull(mailSender, "Unable to autowire mailSenderImpl.");

		builder.withDetail("Magic SMTP server location:", this.mailSender.getHost() + ":" + this.mailSender.getPort());
		mailSender.testConnection();
		builder.up();

	}

}
