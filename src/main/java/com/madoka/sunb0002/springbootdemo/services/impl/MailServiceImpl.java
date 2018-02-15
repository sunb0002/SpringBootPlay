/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.services.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.madoka.sunb0002.springbootdemo.services.MailService;

/**
 * @author Sun Bo
 * @note Must replace the two JCE policy files for Java 7/8.
 * 
 */
@Service
public class MailServiceImpl implements MailService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private JavaMailSenderImpl mailSenderImpl;

	@Override
	public void sendSimpleMail() {

		SimpleMailMessage msg = new SimpleMailMessage();
		logger.debug("Checking mailSender implementation: {}--{}--{}", mailSenderImpl.getHost(),
				mailSenderImpl.getPort(), mailSenderImpl.getJavaMailProperties());

		msg.setFrom("incubator@madoka.com");
		msg.setTo("tomoemami@madoka.com");
		msg.setCc("nagisamomoe@madoka.com");
		msg.setSubject("Contract with me and become a magical girl!");
		msg.setText("I will make all your wishes come true!");
		msg.setSentDate(new Date());

		logger.debug("Sending email: {}", msg);
		mailSender.send(msg);
	}

}
