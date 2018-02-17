package com.madoka.sunb0002.springbootdemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO;
import com.madoka.sunb0002.springbootdemo.common.exceptions.ServiceException;
import com.madoka.sunb0002.springbootdemo.services.UserService;
import com.madoka.sunb0002.springbootdemo.webapi.internal.HomeController;
import com.madoka.sunb0002.springbootdemo.webapi.internal.HomeResponse;
import com.madoka.sunb0002.springbootdemo.webapi.profile.ProfileController;

/**
 * @author Sun Bo
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class IntegrationTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Rule
	public final ExpectedException expectedEx = ExpectedException.none();

	// Constants
	private final String userNRIC = "S8877665A";
	private final String userName = "Urobuchi Gen";

	// Configurations
	@Value("${app.name}")
	private String appName;
	@Autowired
	private DataSource dataSource;

	// Controllers
	@Autowired
	private HomeController homeCtrler;
	@Autowired
	private ProfileController profileCtrler;

	// Services
	@Autowired
	private UserService userSvc;

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
		logger.debug("Testing HomeController403: {}", homeCtrler.test());
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

}
