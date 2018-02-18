/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

/**
 * @author Sun Bo
 *
 */
@Configuration
@EnableJms
public class JmsConfig {

	// <bean id="common.qosEnabledTopicJmsTemplate" abstract="true"
	// class="com.xx.QoSEnabledJmsTemplate">
	// <property name="connectionFactory" ref="common.topicConnectionFactory"/>
	// <property name="explicitQosEnabled" value="true"/>
	// <property name="priority" value="7"/>
	// <property name="deliveryMode" ref="common.deliveryMode"/>
	// <property name="timeToLive" ref="common.timeToLive"/>
	// </bean>
	// ====> For OldSpring, if to change JMS QoS properties, has to manually
	// create a "myJmsTemplate" bean.

	// org.springframework.jms.support.converter.MessageConversionException:
	// Cannot convert object of type
	// [com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO] to JMS message.
	// Supported message payloads are: String, byte array, Map<String,?>,
	// Serializable object.
	// ====> Must implement Serializable to DTO, or manually create
	// "MessageConverter" bean.

	// Caused by: java.lang.ClassNotFoundException: Forbidden class
	// com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO! This class is not
	// trusted to be serialized as ObjectMessage payload. Please take a look at
	// http://activemq.apache.org/objectmessage.html for more information on how
	// to configure trusted classes.
	// ====> SpringBoot to set "spring.activemq.packages.trusted". OldSpring to
	// manually create "ActiveMQConnectionFactory" bean.

}
