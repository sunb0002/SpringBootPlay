/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.services.jms;

import java.io.Serializable;

import javax.jms.JMSException;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO;
import com.madoka.sunb0002.springbootdemo.config.Constants.LocalMessageQueue;

/**
 * @author Sun Bo
 *
 */
@Service
public class Producer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * 
	 * @param msg
	 * @throws JMSException
	 */
	public void sendStrMsg(String msg) throws JMSException {
		sendMsg(LocalMessageQueue.HUGTTO_DESTINATION, msg);
	}

	/**
	 * 
	 * @param userDto
	 * @throws JMSException
	 */
	public void sendDtoMsg(UserDTO userDto) throws JMSException {
		sendMsg(LocalMessageQueue.HUGTTO_DESTINATION, userDto);
	}

	private void sendMsg(String destinationName, Serializable msg) throws JMSException {

		if (msg == null) {
			logger.debug("I don't want to send empty message.");
			return;
		}
		logger.debug("<<<<<Sending MQ msgType={}, msg={} to destination={}", msg.getClass().getSimpleName(), msg,
				destinationName);

		ActiveMQObjectMessage msgObj = new ActiveMQObjectMessage();
		msgObj.setObject(msg);
		jmsTemplate.convertAndSend(destinationName, msgObj);
	}

}
