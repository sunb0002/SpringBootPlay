/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.services.jms;

import java.io.Serializable;

import javax.jms.JMSException;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
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

	/**
	 * 
	 * @param msg
	 * @throws JMSException
	 */
	@JmsListener(destination = LocalMessageQueue.HUGTTO_DESTINATION)
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

}
