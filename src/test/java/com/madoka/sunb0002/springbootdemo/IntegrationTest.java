package com.madoka.sunb0002.springbootdemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.jms.JMSException;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO;
import com.madoka.sunb0002.springbootdemo.common.exceptions.ServiceException;
import com.madoka.sunb0002.springbootdemo.config.Constants.LocalMessageQueue;
import com.madoka.sunb0002.springbootdemo.services.UserService;
import com.madoka.sunb0002.springbootdemo.services.jms.Consumer;
import com.madoka.sunb0002.springbootdemo.services.jms.Producer;
import com.madoka.sunb0002.springbootdemo.webapi.internal.HomeController;
import com.madoka.sunb0002.springbootdemo.webapi.internal.HomeResponse;
import com.madoka.sunb0002.springbootdemo.webapi.profile.ProfileController;

/**
 * @author Sun Bo
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Rule
	public final ExpectedException expectedEx = ExpectedException.none();
	@Rule
	public OutputCapture capture = new OutputCapture();

	// Constants
	private final String userNRIC = "S8877665A";
	private final String userName = "Urobuchi Gen";

	// Configurations
	@LocalServerPort
	private int port; // It's from EmbeddedWebApplicationContext
	@Value("${app.name:madoka}")
	private String appName;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private JmsListenerEndpointRegistry jmsListenerReg;
	@Autowired
	private FilterRegistrationBean filterRegBean;

	// Controllers
	@Autowired
	private HomeController homeCtrler;
	@Autowired
	private ProfileController profileCtrler;

	// Services
	@Autowired
	private UserService userSvc;
	@Autowired
	private Producer mqProducer;
	@Autowired
	private Consumer mqConsumer;

	@Before
	public void setUp() {
		// Use @BeforeAll in Junit5 so setUp can run only once!
		logger.debug("***This testing is running on a radom port={}", port);
		// My approach to disable all JmsListeners.
		jmsListenerReg.destroy();
		// Disable my filter also
		filterRegBean.setEnabled(false);
	}

	@Test
	public void checkAppNameAndDataSource() {
		logger.debug("Testing appName: {}", appName);
		assertTrue(appName.indexOf("SpringBoot-TEST") > -1);

		org.apache.tomcat.jdbc.pool.DataSource ds = (org.apache.tomcat.jdbc.pool.DataSource) dataSource;
		logger.debug("Testing dataSource: {}--{}--{}", ds.getUrl(), ds.getDriverClassName(), ds.getDbProperties());
		assertTrue(ds.getDriverClassName().indexOf("org.h2.Driver") > -1);
	}

	@Test(expected = ServiceException.class)
	public void testHomeController() {
		logger.debug("Testing HomeController200.");
		HomeResponse hr200 = homeCtrler.allHail();
		assertTrue(hr200.getData().indexOf("SpringBoot-TEST") > -1);
		logger.debug("Testing HomeController403: {}", homeCtrler.test403());
	}

	@Ignore("Skip, tested already.")
	@Test
	public void testProfileController1() {
		logger.debug("Testing getAllUsers.");
		expectedEx.expect(ServiceException.class);
		expectedEx.expectMessage("No magical girl");
		profileCtrler.getAllUsers();
	}

	@Test
	public void testProfileController2() {
		logger.debug("Testing get-save-getUsers.");
		String exMsg = "";
		try {
			profileCtrler.getAllUsers();
		} catch (ServiceException se) {
			exMsg = se.getMessage();
		}
		assertTrue(exMsg.indexOf("No magical girl") > -1);

		UserDTO userDto = new UserDTO(null, userNRIC, userName);
		profileCtrler.saveUserProfile(userDto);
		List<UserDTO> result = userSvc.getSomeUsersWithSimilarName(userName);
		logger.debug("Extracted user list: {}", result);
		assertEquals(1, result.size());

		logger.debug("Testing getAllUsers: {}", profileCtrler.getAllUsers());
		expectedEx.expect(ServiceException.class);
		expectedEx.expectMessage("No magical girl");
		profileCtrler.searchUsersByName("TestNobody");
	}

	// @Test
	public void testMQ() throws JMSException, InterruptedException {
		logger.debug("Testing MQ producer and consumer.");

		UserDTO userDto = new UserDTO(2018L, userNRIC, userName);
		String strMsg = "Test StringMsg for destination=" + LocalMessageQueue.HUGTTO_DESTINATION;

		capture.flush();
		mqProducer.sendStrMsg(strMsg);
		assertTrue(capture.toString().indexOf(strMsg) > -1);

		capture.flush();
		mqProducer.sendDtoMsg(userDto);
		assertTrue(capture.toString().indexOf(userNRIC) > -1);

		Object msg1 = mqConsumer.receiveAdhocMsg(LocalMessageQueue.HUGTTO_DESTINATION);
		assertTrue(msg1 instanceof String);

		Object msg2 = mqConsumer.receiveAdhocMsg(LocalMessageQueue.HUGTTO_DESTINATION);
		assertTrue(msg2 instanceof UserDTO);
		assertEquals(userName, ((UserDTO) msg2).getName());
	}

}
