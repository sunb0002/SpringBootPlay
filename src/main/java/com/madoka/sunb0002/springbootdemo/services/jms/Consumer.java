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
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO;
import com.madoka.sunb0002.springbootdemo.config.Constants.LocalMessageQueue;

/**
 * @author Sun Bo
 *
 */
@Service
public class Consumer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * 
	 * @param msg
	 * @throws JMSException
	 * @note: JmsListener will block the thread, and performance got issue as
	 *        document says. Use it carefully.
	 */
	@Profile("!test")
	@JmsListener(id = "HUGTTO", destination = LocalMessageQueue.HUGTTO_DESTINATION)
	public void receiveMsg(ActiveMQObjectMessage mqObj) throws JMSException {

		Serializable msg = mqObj.getObject();
		if (msg == null) {
			logger.debug(">>>>>Received MQ Null Msg.");
		} else if (msg instanceof String) {
			logger.debug(">>>>>Received MQ StringMsg={}", msg);
		} else if (msg instanceof UserDTO) {
			logger.debug(">>>>>Received MQ UserDTOMsg={}", msg);
		} else {
			logger.debug(">>>>>Received MQ UFO={}", mqObj);
		}
	}

	/**
	 * 
	 * @param destination
	 * @note: jmsTemplate.receiveAndConvert will also block the thread, until
	 *        message is available in the MQ or receive-timeout is hit. Use it
	 *        carefully.
	 */
	public Object receiveAdhocMsg(String destination) {
		Object obj = jmsTemplate.receiveAndConvert(destination);
		logger.debug(">>>>>Received Adhoc MQ Object={}", obj);
		return obj;
	}

}
